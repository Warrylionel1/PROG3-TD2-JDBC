import java.time.Instant;
import java.util.Objects;

public class Sale {
    private Integer id;
    private Instant creationDateTime;
    private Order order;

    public Sale() {
    }

    public Sale(Integer id, Instant creationDateTime, Order order) {
        this.id = id;
        this.creationDateTime = creationDateTime;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id) && Objects.equals(creationDateTime, sale.creationDateTime) && Objects.equals(order, sale.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDateTime, order);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", creationDateTime=" + creationDateTime +
                ", order=" + order +
                '}';
    }
}
