import java.util.ArrayList;

public class Field {

    private static char color;
    private static int number, gridCoordinateX, GridCoordinateY;

    private ArrayList<Field> neighbors = new ArrayList<>();

    private Player occupant = null;
    private Player targetOf = null;

    public Field (char color, int number, int gridCoordinateX, int gridCoordinateY)
    {
        this.color = color;
        this.number = number;
        this.gridCoordinateX = gridCoordinateX;
        this.GridCoordinateY = gridCoordinateY;
    }

    public void addNeighbor(Field neighbor) {
        neighbors.add(neighbor);
    }

    public static char getColor() {
        return color;
    }

    public static void setColor(char color) {
        Field.color = color;
    }

    public static int getNumber() {
        return number;
    }

    public static void setNumber(int number) {
        Field.number = number;
    }

    public static int getGridCoordinateX() {
        return gridCoordinateX;
    }

    public static void setGridCoordinateX(int gridCoordinateX) {
        Field.gridCoordinateX = gridCoordinateX;
    }

    public static int getGridCoordinateY() {
        return GridCoordinateY;
    }

    public static void setGridCoordinateY(int gridCoordinateY) {
        Field.GridCoordinateY = gridCoordinateY;
    }

    public Player getTargetOf() {
        return targetOf;
    }

    public void setTargetOf(Player targetOf) {
        this.targetOf = targetOf;
    }

    public Player getOccupant() {
        return occupant;
    }

    public void setOccupant(Player occupant) {
        this.occupant = occupant;
    }
}
