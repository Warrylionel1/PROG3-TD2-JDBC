import java.util.Objects;

public class DishIngredient {
    private Integer id;
    private Ingredient ingredient;
    private Double quantityRequired;
    private UnitEnum unit;

    public DishIngredient() {
    }

    public DishIngredient(Integer id, Ingredient ingredient, Double quantityRequired, UnitEnum unit) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    public DishIngredient(UnitEnum unit, Double quantityRequired, Ingredient ingredient) {
        this.unit = unit;
        this.quantityRequired = quantityRequired;
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

    public Double getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(Double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return Objects.equals(id, that.id) && Objects.equals(ingredient, that.ingredient) && Objects.equals(quantityRequired, that.quantityRequired) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredient, quantityRequired, unit);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", ingredient=" + ingredient +
                ", quantityRequired=" + quantityRequired +
                ", unit=" + unit +
                '}';
    }
}