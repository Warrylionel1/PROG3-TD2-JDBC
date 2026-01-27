import java.util.Objects;

public class DishIngredient {
    private Integer id;
    private Ingredient ingredient;
    private Dish dish;
    private Double quantity;
    private UnitEnum unit;

    public DishIngredient() {
    }

    public DishIngredient(Integer id, Ingredient ingredient, Dish dish, Double quantity, UnitEnum unit) {
        this.id = id;
        this.ingredient = ingredient;
        this.dish = dish;
        this.quantity = quantity;
        this.unit = unit;
    }

    public DishIngredient(UnitEnum unit, Double quantity, Ingredient ingredient, Dish dish) {
        this.unit = unit;
        this.quantity = quantity;
        this.ingredient = ingredient;
        this.dish = dish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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
        return Objects.equals(id, that.id) &&
                Objects.equals(ingredient, that.ingredient) &&
                Objects.equals(quantity, that.quantity) &&
                unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredient, quantity, unit);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", ingredient=" + (ingredient != null ? ingredient.getName() : "null") +
                ", dish=" + (dish != null ? dish.getName() : "null") +
                ", quantity=" + quantity +
                ", unit=" + unit +
                '}';
    }
}