package sample;

public class NamedPlace extends Place {


    public NamedPlace(String name, Position position, String category, Triangle triangle){
        super(name, position, category, triangle);
    }

    @Override
    public String toString(){
        return "Named" + "," + getCategory() + "," + getPosition() + "," + getName();
    }
}
