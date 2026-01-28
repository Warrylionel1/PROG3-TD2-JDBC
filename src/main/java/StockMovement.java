import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class StockMovement {
    private Integer id;
    private StockValue value;
    private MovementType type;
    private Instant creationDateTime;
    private List<StockMovement> stockMovementList;

    public StockMovement() {
    }

    public StockMovement(Integer id, StockValue value, MovementType type, Instant creationDateTime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
    }

    public StockMovement(Integer id, StockValue value, MovementType type, Instant creationDateTime, List<StockMovement> stockMovementList) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
        this.stockMovementList = stockMovementList;
    }

    public StockMovement(StockValue value, MovementType type, Instant creationDateTime, List<StockMovement> stockMovementList) {
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
        this.stockMovementList = stockMovementList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockMovement that = (StockMovement) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && type == that.type && Objects.equals(creationDateTime, that.creationDateTime) && Objects.equals(stockMovementList, that.stockMovementList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, type, creationDateTime, stockMovementList);
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", creationDateTime=" + creationDateTime +
                ", stockMovementList=" + stockMovementList +
                '}';
    }
}
