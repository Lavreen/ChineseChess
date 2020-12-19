import java.util.ArrayList;

public class Field {

    private final char color;
    private final int number, gridCoordinateX, GridCoordinateY;

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

    public char getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    public int getGridCoordinateX() {
        return gridCoordinateX;
    }

    public int getGridCoordinateY() {
        return GridCoordinateY;
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
