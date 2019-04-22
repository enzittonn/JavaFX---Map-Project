package sample;

import java.util.Objects;

public class Place {
    private String name;
    private Position position;

    public Place(String name, Position position){
        this.name = name;
        this.position = position;
    }


    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) &&
                Objects.equals(position, place.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }

    @Override
    public String toString() {
        return " Name: " + name;
    }
}
