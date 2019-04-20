package sample;


public class Position {     //fungera som nycklar i hashtabeller.
    private double x;
    private double y;
    private String name;
    private String colour;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                '}';
    }
}
