@Entity
public class GradePolicy {

    @Id
    private String grade; // BASIC, VIP 등

    private BigDecimal commissionRate;

    public GradePolicy(String grade, BigDecimal commissionRate) {
        this.grade = grade;
        this.commissionRate = commissionRate;
    }
}