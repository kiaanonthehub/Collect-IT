package Model;

public class Item {

    // declare variables
    private String name, description, category, date_of_acquisition, uri;


    // blank constructor
    public Item() {
    }

    // constructor
    public Item(String name, String description, String category, String date_of_acquisition, String uri) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.date_of_acquisition = date_of_acquisition;
        this.uri = uri;
    }
    
    // getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate_of_acquisition() {
        return date_of_acquisition;
    }

    public void setDate_of_acquisition(String date_of_acquisition) {
        this.date_of_acquisition = date_of_acquisition;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
