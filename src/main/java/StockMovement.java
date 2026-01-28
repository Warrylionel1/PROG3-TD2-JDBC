import java.time.Instant;
import java.util.Objects;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private Double quantity;
    private MovementType type;
    private UnitEnum unit;
    private Instant creationDateTime;

    public StockMovement() {
    }

    public StockMovement(Ingredient ingredient, Integer id, Double quantity, MovementType type, UnitEnum unit, Instant creationDateTime) {
        this.ingredient = ingredient;
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.unit = unit;
        this.creationDateTime = creationDateTime;
    }

    public StockMovement(Double quantity, MovementType type, UnitEnum unit, Instant creationDateTime, Ingredient ingredient) {
        this.quantity = quantity;
        this.type = type;
        this.unit = unit;
        this.creationDateTime = creationDateTime;
        this.ingredient = ingredient;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
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
        return Objects.equals(id, that.id) && Objects.equals(ingredient, that.ingredient) && Objects.equals(quantity, that.quantity) && type == that.type && unit == that.unit && Objects.equals(creationDateTime, that.creationDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredient, quantity, type, unit, creationDateTime);
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", ingredient=" + ingredient +
                ", quantity=" + quantity +
                ", type=" + type +
                ", unit=" + unit +
                ", creationDateTime=" + creationDateTime +
                '}';
    }
}
