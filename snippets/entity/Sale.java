@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private SettlementState settlementState = SettlementState.UNSETTLED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_request_id")
    private SettlementRequest settlementRequest;

    public enum SettlementState {
        UNSETTLED,
        SETTLED
    }
}