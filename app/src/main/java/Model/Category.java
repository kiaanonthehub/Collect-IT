package Model;

public class Category {

    // declare variables
    private String name;
    private String description;
    private String goal;

    // default constructor

    public Category() {
    }

    // constructor

    public Category(String name, String description, String goal) {
        this.name = name;
        this.description = description;
        this.goal = goal;
    }

    // getters and setters

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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
