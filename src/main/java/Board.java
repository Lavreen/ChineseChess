import java.util.ArrayList;

public class Board {

    private boolean[][] grid; // "true" - field exists
    public Field[][] fields;
    final private int SIZE;
    public Board(int size) {
        SIZE = size;
    }

    /**
     *  yellow      red       black
     *
     *              HEX
     *
     *  white       green       blue
     */
    private void generateBoard() {
        final int X = 7 + 6 * (SIZE - 1);
        final int Y = 5 + 4 * (SIZE - 1);

        // red triangle generation
        final int redOriginX = X / 2 + 1; // coordinates of the vertex from which the triangle generation starts
        final int redOriginY = 0;         //
        int x = redOriginX;
        int y = redOriginY;
        int rowBeginningX, rowBeginningY;
        int count = 1;
        for (int i = 0; i < SIZE; i++) {
            rowBeginningX = x;
            rowBeginningY = y;
            for (int j = 0; j < i+1; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field('R', count, x, y);
                count++;
                if(j < i) {
                    x += 2;
                }
                else {
                    x = rowBeginningX - 1;
                    y = rowBeginningY + 1;
                }
            }

        }
    }









//    private void createFields(){} ---> generateBoard

    private void setFieldsNeighbours() {}

    public ArrayList<Field> getFields(){return null;}

    public Boolean isWinner(Player player){return false;}
}
