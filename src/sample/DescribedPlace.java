package sample;

public class DescribedPlace extends Place {
    private String description;

    public DescribedPlace(String name, String description){
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return " Description: " + description;
    }
}
