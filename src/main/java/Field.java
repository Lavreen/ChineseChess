import java.util.ArrayList;

class Pair {

    Field first;
    String second;

    public Pair(Field first, String second) {
        this.first = first;
        this.second = second;
    }
}
public class Field {

    private final char color;
    private final int number, gridCoordinateX, GridCoordinateY;

    private ArrayList<Pair> neighbors = new ArrayList<>();

    private Player occupant = null;
    private Player targetOf = null;

    public Field (char color, int number, int gridCoordinateX, int gridCoordinateY)
    {
        this.color = color;
        this.number = number;
        this.gridCoordinateX = gridCoordinateX;
        this.GridCoordinateY = gridCoordinateY;
    }

    public void addNeighbor(Field neighbor, String direction) {
        Pair pair = new Pair(neighbor, direction);
        neighbors.add(pair);
    }

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

    public void setTargetOf(Player targetOf) {
        this.targetOf = targetOf;
    }

    public Player getOccupant() {
        return occupant;
    }

    public void setOccupant(Player occupant) {
        this.occupant = occupant;
    }

    public ArrayList<Pair> getNeighbors() {
        return neighbors;
    }
}
