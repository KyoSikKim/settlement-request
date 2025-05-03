@Entity
public class Seller {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade")
    private GradePolicy grade;

    @OneToMany(mappedBy = "seller")
    private List<Sale> sales = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<SettlementRequest> settlementRequests = new ArrayList<>();

    public Seller(Long id, String name, GradePolicy grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }
}