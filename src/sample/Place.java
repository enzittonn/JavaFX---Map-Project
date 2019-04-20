package sample;

public class Place {
    private String name;
    private Position position;

    public Place(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return " Name: " + name;
    }
}
