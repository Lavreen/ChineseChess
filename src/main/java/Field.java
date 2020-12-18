import java.util.ArrayList;

public class Field {

    char color;
    int number, gridCoordinateX, getGridCoordinateY;
    public ArrayList<Field> neighbors;
    public Field (char color, int number, int gridCoordinateX, int gridCoordinateY)
    {
        this.color = color;
        this.number = number;
        this.gridCoordinateX = gridCoordinateX;
        this.getGridCoordinateY = gridCoordinateY;
    }




//    public ArrayList<Field> getNeighbors(Field field) {
//        return null;
//    }
//
//    public void setOwner(Player player){}
//
//    public Player getOwner(){return null;}
//
//
//    public int getX(){return 0;}
//
//    public void setX(int x){}
//
//    public int getY(){return 0;}
//
//    public void setY(int y){}
}
