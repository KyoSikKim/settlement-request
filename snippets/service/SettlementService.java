public class SettlementService {

    private final SellerRepository sellerRepository;
    private final SaleRepository saleRepository;
    private final SettlementRequestRepository settlementRequestRepository;

    public SettlementRequest requestSettlement(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow();
        GradePolicy grade = seller.getGrade();
        BigDecimal commissionRate = grade.getCommissionRate();

        List<Sale> unsettledSales = saleRepository.findBySellerAndSettlementState(seller, UNSETTLED);

        if (unsettledSales.isEmpty()) {
            throw new NoUnsettledSalesException();
        }

        BigDecimal totalAmount = unsettledSales.stream()
                .map(Sale::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal feeAmount = totalAmount.multiply(commissionRate);
        BigDecimal payoutAmount = totalAmount.subtract(feeAmount);

        SettlementRequest request = new SettlementRequest(seller, totalAmount, feeAmount, payoutAmount, commissionRate);
        settlementRequestRepository.save(request);

        unsettledSales.forEach(sale -> {
            sale.setSettlementRequest(request);
            sale.setSettlementState(Sale.SettlementState.SETTLED);
        });
        saleRepository.saveAll(unsettledSales);

        return request;
    }

    public void approve(Long requestId) {
        SettlementRequest request = settlementRequestRepository.findById(requestId).orElseThrow();
        if (request.getStatus() == SettlementRequest.SettlementStatus.APPROVED) {
            throw new InvalidSettlementStateException();
        }
        request.setStatus(SettlementRequest.SettlementStatus.APPROVED);
    }

    public void reject(Long requestId) {
        SettlementRequest request = settlementRequestRepository.findById(requestId).orElseThrow();

        if (request.getStatus() == SettlementRequest.SettlementStatus.APPROVED) {
            throw new InvalidSettlementStateException();
        }

        request.setStatus(SettlementRequest.SettlementStatus.REJECTED);

        List<Sale> sales = request.getSales();
        for (Sale sale : sales) {
            sale.setSettlementRequest(null);
            sale.setSettlementState(Sale.SettlementState.UNSETTLED);
        }
        saleRepository.saveAll(sales);
    }
}