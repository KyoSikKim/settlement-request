@Entity
public class SettlementRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private BigDecimal totalAmount;

    private BigDecimal feeAmount;

    private BigDecimal payoutAmount;

    private BigDecimal commissionRate;

    @Enumerated(EnumType.STRING)
    private SettlementStatus status = SettlementStatus.PENDING;

    private LocalDateTime requestedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "settlementRequest")
    private List<Sale> sales = new ArrayList<>();

    public enum SettlementStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}