import java.util.ArrayList;

public class Board {
    enum orientation {
        DOWNWARDS,
        UPWARDS
    }
    private int SIZE; // "size" means the number of fields in every triangle-shaped board "arm's" base
    int X; // dimensions of the rectangle which the board fits in
    int Y; //

    private boolean[][] grid; // "true" - field exists
    public Field[][] fields;

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


    private void generateBoard() {
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
                    x = rowBeginningX - 1;
                    switch (orientation) {
                        case DOWNWARDS -> y = rowBeginningY + 1;
                        case UPWARDS -> y = rowBeginningY - 1;
                    }
                }
            }

        }
        if(color == 'Y') {
            hexagonOriginX = x + 2;
            hexagonOriginY = y;
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
            for (int j = 0; j < i + SIZE + 2; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field('H', generatedFieldNumber, x, y);
                generatedFieldNumber++;
                lastRowFieldCount++;
                if(j < i + SIZE + 1) {
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
            for (int j = 0; j < lastRowFieldCount; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field('H', generatedFieldNumber, x, y);
                generatedFieldNumber++;
                if(j < i + SIZE + 1) {
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

    private void setFieldsNeighbours() {}

    public ArrayList<Field> getFields(){return null;}

    public Boolean isWinner(Player player){return false;}
}
