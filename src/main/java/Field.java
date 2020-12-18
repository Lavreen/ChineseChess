import java.util.ArrayList;

public class Field {

    public Field (char color, int number, int gridCoordinateX, int gridCoordinateY)
    {

    }





    private ArrayList<Field> neighbours;
    private Player owner;
    private Player target;
    /*
    Wpisałem narazie x,y. Jak będziesz to robił to zamień na twój pomysł na numeracje.
     */
    private int x;
    private int y;


    public Field(){}

    public void addNeighbour(Field field ){}

    public ArrayList<Field> getNeighbours(){return null;}

    public void setOwner(Player player){}

    public Player getOwner(){return null;}


    public int getX(){return 0;}

    public void setX(int x){}

    public int getY(){return 0;}

    public void setY(int y){}
}
