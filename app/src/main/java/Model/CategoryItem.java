package Model;

public class CategoryItem {

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public CategoryItem(String categoryName, int numberOfItems, double percentage) {
        this.categoryName = categoryName;
        this.numberOfItems = numberOfItems;
        this.percentage = percentage;
    }

    String categoryName;
    int numberOfItems;

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    double percentage;


}
