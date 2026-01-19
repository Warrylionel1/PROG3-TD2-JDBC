import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
    private List<Ingredient> ingredients;

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getPrice() {
        return sellingPrice;
    }

    // Méthode setPrice gardée pour compatibilité
    public void setPrice(Double price) {
        this.sellingPrice = price;
    }

    public Double getDishCost() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        double totalCost = 0.0;
        for (Ingredient ingredient : ingredients) {
            Double quantity = ingredient.getQuantityRequired();
            if (quantity == null) {
                throw new RuntimeException("Quantité requise manquante pour l'ingrédient: " + ingredient.getName());
            }
            totalCost += ingredient.getPrice() * quantity;
        }
        return totalCost;
    }

    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, Double sellingPrice, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
        this.ingredients = ingredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Double getGrossMargin() {
        if (sellingPrice == null) {
            throw new RuntimeException("Prix de vente non défini pour le plat: " + name);
        }
        return sellingPrice - getDishCost();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) &&
                Objects.equals(name, dish.name) &&
                dishType == dish.dishType &&
                Objects.equals(sellingPrice, dish.sellingPrice) &&
                Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, sellingPrice, ingredients);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", sellingPrice=" + sellingPrice +
                ", ingredients=" + ingredients +
                '}';
    }
}