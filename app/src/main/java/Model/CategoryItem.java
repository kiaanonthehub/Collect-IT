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

    public CategoryItem(String categoryName, int numberOfItems) {
        this.categoryName = categoryName;
        this.numberOfItems = numberOfItems;
    }

    String categoryName;
    int numberOfItems;


}
