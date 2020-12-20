import java.util.ArrayList;

class Pair {

    Field first;
    String second;

    public Pair(Field first, String second) {
        this.first = first;
        this.second = second;
    }
}

/**
 * Fields on the board.
 */
public class Field {

    private final char color;
    private final int number, gridCoordinateX, GridCoordinateY;

    private ArrayList<Pair> neighbors = new ArrayList<>();

    private Player occupant = null;
    private Player targetOf = null;

    /**
     * The constructor of Field.
     * @param color the first letter of the color of the board's arm the field belongs to
     * @param number number
     * @param gridCoordinateX position x of the field
     * @param gridCoordinateY position y of the field
     */
    public Field (char color, int number, int gridCoordinateX, int gridCoordinateY)
    {
        this.color = color;
        this.number = number;
        this.gridCoordinateX = gridCoordinateX;
        this.GridCoordinateY = gridCoordinateY;
    }

    /**
     * Adds the given neighbor to the field's information
     * @param neighbor this field's neighbor
     * @param direction direction in which you have to look for the neighbor
     */
    public void addNeighbor(Field neighbor, String direction) {
        Pair pair = new Pair(neighbor, direction);
        neighbors.add(pair);
    }

    /**
     * Checks if the given field is the neighbor of this field
     * @param codeChar color
     * @param codeInt number
     * @return
     */
    public boolean isNeighbor(char codeChar, int codeInt){
        for(Pair neighbor : neighbors){
            if(neighbor.first.getColor() == codeChar && neighbor.first.getNumber() == codeInt){
                return true;
            }
        }
        return false;
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

    /**
     * Sets this field as a target of the given player
     * @param targetOf player which targets this field
     */
    public void setTargetOf(Player targetOf) {
        this.targetOf = targetOf;
    }

    public Player getOccupant() {
        return occupant;
    }

    /**
     * Sets the given player as the one occupying the field
     * @param occupant occupant
     */
    public void setOccupant(Player occupant) {
        this.occupant = occupant;
    }

    public ArrayList<Pair> getNeighbors() {
        return neighbors;
    }
}
