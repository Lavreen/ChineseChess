import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    enum orientation {
        DOWNWARDS,
        UPWARDS
    }
    private int SIZE; // "size" means the number of fields in every triangle-shaped board "arm's" base
    int X; // dimensions of the rectangle which the board fits in
    int Y; //

    public boolean[][] grid = new boolean[100][100]; // "true" - field exists
    public Field[][] fields = new Field[100][100];

    int redOriginX, redOriginY;  // coordinates of the vertex from which the triangle generation starts
    int blackOriginX, blackOriginY;
    int violetOriginX, violetOriginY;
    int greenOriginX, greenOriginY;
    int whiteOriginX, whiteOriginY;
    int yellowOriginX, yellowOriginY;
    int hexagonOriginX, hexagonOriginY;

    public Board(int size) {
        SIZE = size;
        if(SIZE < 1)
            SIZE = 1;
        X = 7 + 6 * (SIZE - 1);
        Y = 5 + 4 * (SIZE - 1);

        redOriginX = X / 2;
        redOriginY = 0;

        blackOriginX = X - SIZE;
        blackOriginY = Y / 2 - 1;

        violetOriginX = X - SIZE;
        violetOriginY = Y / 2 + 1;

        greenOriginX = X / 2;
        greenOriginY = Y - 1;

        whiteOriginX = SIZE - 1;
        whiteOriginY = Y / 2 + 1;

        yellowOriginX = SIZE - 1;
        yellowOriginY = Y / 2 - 1;

    }


    /**
     *  yellow      red       black
     *
     *              HEX
     *
     *  white       green       violet
     */


    public void generateBoard() {
        generateTriangle(orientation.DOWNWARDS,'R', redOriginX, redOriginY);
        generateTriangle(orientation.UPWARDS, 'B', blackOriginX, blackOriginY);
        generateTriangle(orientation.DOWNWARDS, 'V', violetOriginX, violetOriginY);
        generateTriangle(orientation.UPWARDS, 'G', greenOriginX, greenOriginY);
        generateTriangle(orientation.DOWNWARDS, 'W', whiteOriginX, whiteOriginY);
        generateTriangle(orientation.UPWARDS, 'Y', yellowOriginX, yellowOriginY);

        generateHexagon(hexagonOriginX, hexagonOriginY);
    }

    private void generateTriangle(orientation orientation, char color, int x, int y) {
        int rowBeginningX, rowBeginningY;
        int generatedFieldNumber = 1;
        for (int i = 0; i < SIZE; i++) {
            rowBeginningX = x;
            rowBeginningY = y;
            for (int j = 0; j < i+1; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field(color, generatedFieldNumber, x, y);
                generatedFieldNumber++;
                if(j < i) {
                    x += 2;
                }
                else {
                    if(color == 'Y') {
                        hexagonOriginX = x + 2;
                        hexagonOriginY = y;
                    }
                    x = rowBeginningX - 1;
                    switch (orientation) {
                        case DOWNWARDS -> y = rowBeginningY + 1;
                        case UPWARDS -> y = rowBeginningY - 1;
                    }
                }
            }

        }

    }

    private void generateHexagon(int x, int y) {
        int rowBeginningX, rowBeginningY;
        int generatedFieldNumber = 1;
        int lastRowFieldCount = 0;
        for (int i = 0; i < SIZE + 1; i++) {
            rowBeginningX = x;
            rowBeginningY = y;
            lastRowFieldCount = 0;
            for (int j = 0; j < i + SIZE + 1; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field('H', generatedFieldNumber, x, y);
//                System.out.print(x);
//                System.out.print(", ");
//                System.out.println(y);
                generatedFieldNumber++;
                lastRowFieldCount++;
                if(j < i + SIZE) {
                    x += 2;
                }
                else {
                    x = rowBeginningX - 1;
                    y = rowBeginningY + 1;
                }

            }
        }
        x += 2;
        for (int i = 0; i < SIZE; i++) {
            rowBeginningX = x;
            rowBeginningY = y;
            for (int j = 0; j < lastRowFieldCount - 1; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field('H', generatedFieldNumber, x, y);
                generatedFieldNumber++;
                if(j < lastRowFieldCount - 2) {
                    x += 2;
                }
                else {
                    x = rowBeginningX + 1;
                    y = rowBeginningY + 1;
                    lastRowFieldCount--;
                }
            }
        }
    }

    public void printBoard() {
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (grid[x][y] != true) {
                    System.out.print("  ");
                } else {
                    System.out.print('W');
                }
            }
            System.out.println();
        }
    }
    private void setFieldsNeighbours() {}

    public ArrayList<Field> getFields(){return null;}

    public Boolean isWinner(Player player){return false;}
}
