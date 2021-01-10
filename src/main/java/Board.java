import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

/**
 * Class managing generation of the board, including all dependencies between the fields, including move connections between fields.
 */
public class Board {
    enum direction {
        DOWNWARDS,
        UPWARDS
    }

    private static int SIZE; // "size" means the number of fields in every triangle-shaped board "arm's" base
    private static int X; // dimensions of the rectangle which the board fits in
    private static int Y; //

    private final boolean[][] grid; // "true" - field exists
    private final Field[][] fields;
    private final Player[] players;
    private char[] playersColours;

    private static int redOriginX, redOriginY;  // coordinates of the vertex from which the triangle generation starts
    private static int blackOriginX, blackOriginY;
    private static int violetOriginX, violetOriginY;
    private static int greenOriginX, greenOriginY;
    private static int whiteOriginX, whiteOriginY;
    private static int yellowOriginX, yellowOriginY;
    private static int hexagonOriginX, hexagonOriginY;

    private int generatedTrianglesCount = 0;

    /**
     * Board constructor, runs board generation.
     * @param size means the number of fields in every triangle-shaped board "arm's" base
     * @param players an array of players joining the game
     */
    public Board(int size, Player[] players) {
        this.players = players;
        playersColours = new char[players.length];
        SIZE = size;

        if (SIZE < 1)
            SIZE = 1;
        X = 7 + 6 * (SIZE - 1);
        Y = 5 + 4 * (SIZE - 1);
        grid = new boolean[X][Y];
        fields = new Field[X][Y];

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

        generateBoard();
    }


    // Default arms' colors used as pivot names

    /**
     * yellow      red       black
     *
     *              HEX
     *
     * white       green       violet
     */

    /**
     * Generates the whole board, which includes it's fields with assigned neighbors, occupants and targets
     */
    public void generateBoard() {
        generateTriangle(direction.DOWNWARDS, 'R', redOriginX, redOriginY);
        generateTriangle(direction.UPWARDS, 'B', blackOriginX, blackOriginY);
        generateTriangle(direction.DOWNWARDS, 'V', violetOriginX, violetOriginY);
        generateTriangle(direction.UPWARDS, 'G', greenOriginX, greenOriginY);
        generateTriangle(direction.DOWNWARDS, 'W', whiteOriginX, whiteOriginY);
        generateTriangle(direction.UPWARDS, 'Y', yellowOriginX, yellowOriginY);
        generateHexagon(hexagonOriginX, hexagonOriginY);

        appendNeighbors();
    }

    /**
     * Assigns information to every Field object about its neighbors
     */
    public void appendNeighbors() {
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (grid[x][y]) {
                    if (x >= 2 && grid[x - 2][y]) {
                        fields[x][y].addNeighbor(fields[x - 2][y], "west");
                    }
                    if (x >= 1 && y >= 1 && grid[x - 1][y - 1]) {
                        fields[x][y].addNeighbor(fields[x - 1][y - 1], "north_west");
                    }
                    if (x < X - 1 && y >= 1 && grid[x + 1][y - 1]) {
                        fields[x][y].addNeighbor(fields[x + 1][y - 1], "north_east");
                    }
                    if (x < X - 2 && grid[x + 2][y]) {
                        fields[x][y].addNeighbor(fields[x + 2][y], "east");
                    }
                    if (x < X - 1 && y < Y - 1 && grid[x + 1][y + 1]) {
                        fields[x][y].addNeighbor(fields[x + 1][y + 1], "south_east");
                    }
                    if (x >= 1 && y < Y - 1 && grid[x - 1][y + 1]) {
                        fields[x][y].addNeighbor(fields[x - 1][y + 1], "south_west");
                    }
                }
            }
        }
    }

    /**
     * Creates fields in one of the board's triangular arms along with most of their parameters
     * @param direction the direction in which the triangle will be being generated starting from its @origin
     * @param color specifies which of the board's arms will be generated
     * @param x coordinate x of the structure's origin
     * @param y coordinate y of the structure's origin
     */
    private void generateTriangle(direction direction, char color, int x, int y) {
        int rowBeginningX, rowBeginningY;
        int generatedFieldNumber = 1;
        int targetOf;
        for (int i = 0; i < SIZE; i++) {
            rowBeginningX = x;
            rowBeginningY = y;
            for (int j = 0; j < i + 1; j++) {
                grid[x][y] = true;
                fields[x][y] = new Field(color, generatedFieldNumber, x, y);
                switch (players.length) {
                    case 2:
                        if (generatedTrianglesCount == 0 || generatedTrianglesCount == 3) {
                            targetOf = (generatedTrianglesCount + 1) % 2;
                            fields[x][y].setTargetOf(players[targetOf]);
                        }
                        if (generatedTrianglesCount == 0) {
                            fields[x][y].setOccupant(players[0]);
                            playersColours[0] = color;
                        }
                        if (generatedTrianglesCount == 3) {
                            fields[x][y].setOccupant(players[1]);
                            playersColours[1] = color;
                        }
                        break;
                    case 3:
                        if (generatedTrianglesCount % 2 == 1) {
                            switch (generatedTrianglesCount) {
                                case 1 -> targetOf = 2;
                                case 3 -> targetOf = 0;
                                case 5 -> targetOf = 1;
                                default -> targetOf = 1337;
                            }
                            fields[x][y].setTargetOf(players[targetOf]);
                        }
                        if (generatedTrianglesCount % 2 == 0) {

                            switch (generatedTrianglesCount) {
                                case 0 :
                                    fields[x][y].setOccupant(players[0]);
                                    playersColours[0] = color;
                                    break;
                                case 2 :
                                    fields[x][y].setOccupant(players[1]);
                                    playersColours[1] = color;
                                    break;
                                case 4 :
                                    fields[x][y].setOccupant(players[2]);
                                    playersColours[2] = color;
                                    break;
                            }
                        }
                        break;
                    case 4:
                        if(generatedTrianglesCount == 1 || generatedTrianglesCount == 2 || generatedTrianglesCount == 4 || generatedTrianglesCount == 5) {
                            switch (generatedTrianglesCount) {
                                case 1 -> {
                                    targetOf = 2;
                                    fields[x][y].setOccupant(players[0]);
                                    playersColours[0] = color;
                                }
                                case 2 -> {
                                    targetOf = 3;
                                    fields[x][y].setOccupant(players[1]);
                                    playersColours[1] = color;
                                }
                                case 4 -> {
                                    targetOf = 0;
                                    fields[x][y].setOccupant(players[2]);
                                    playersColours[2] = color;
                                }
                                case 5 -> {
                                    targetOf = 1;
                                    fields[x][y].setOccupant(players[3]);
                                    playersColours[3] = color;
                                }
                                default -> targetOf = 1337;
                            }
                            fields[x][y].setTargetOf(players[targetOf]);
                        }
                        break;
                    case 6:
                        fields[x][y].setTargetOf(players[(generatedTrianglesCount + 3) % 6]);
                        fields[x][y].setOccupant(players[generatedTrianglesCount]);
                        playersColours[generatedTrianglesCount] = color;
                        break;
                }
                generatedFieldNumber++;
                if (j < i) {
                    x += 2;
                } else {
                    if (color == 'Y') {
                        hexagonOriginX = x + 2;
                        hexagonOriginY = y;
                    }
                    x = rowBeginningX - 1;
                    switch (direction) {
                        case DOWNWARDS -> y = rowBeginningY + 1;
                        case UPWARDS -> y = rowBeginningY - 1;
                    }
                }
            }
        }
        generatedTrianglesCount++;
    }

    /**
     * Creates fields in the board's hexagonal center along with most of their parameters
     * @param x coordinate x of the hexagon's origin (upper left corner of the hexagon)
     * @param y coordinate y of the hexagon's origin (upper left corner of the hexagon)
     */
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
                generatedFieldNumber++;
                lastRowFieldCount++;
                if (j < i + SIZE) {
                    x += 2;
                } else {
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
                if (j < lastRowFieldCount - 2) {
                    x += 2;
                } else {
                    x = rowBeginningX + 1;
                    y = rowBeginningY + 1;
                    lastRowFieldCount--;
                }
            }
        }
    }

    /**
     * Debugging feature. Prints 'W's representing the board's generated fields and leaves blank space where there aren't any.
     */
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

    /**
     * Sets occupant of the field given by the color-position.
     * @param codeChar color of the field represented by its first letter
     * @param codeInt field's number
     * @param player the occupant
     */
    public void setFieldOccupant(char codeChar, int codeInt, Player player) {
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == codeChar && fields[x][y].getNumber() == codeInt) {
                        fields[x][y].setOccupant(player);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Returns field's occupant
     * @param codeChar color of the field represented by its first letter
     * @param codeInt field's number
     * @return field's occupant
     */
    public Player getFieldOccupant(char codeChar, int codeInt) {
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == codeChar && fields[x][y].getNumber() == codeInt)
                        return fields[x][y].getOccupant();
                }
            }
        }
        return null;
    }

    /**
     * Checks whether the given player has moved all his pieces to their target fields.
     * @param player given player
     * @return true - the player has claimed all the targets, false - the player has not claimed all the targets.
     */
    public boolean hasHeClaimedAllTargets(Player player) {
        int count = 0;
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (!isNull(fields[x][y]) && !isNull(fields[x][y].getOccupant()) && !isNull(fields[x][y].getTargetOf())) {
                    if (fields[x][y].getOccupant().equals(player) && fields[x][y].getTargetOf().equals(fields[x][y].getOccupant()))
                        count++;
                    if (count == triangleFieldCount())
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Supportive function of isWinner
     * @return count
     */
    private int triangleFieldCount() {
        int count = 1;
        for (int i = 2; i <= SIZE; i++)
            count += i;
        return count;
    }

    /**
     * Checks if fields of given color-position codes are neighbors.
     * @param codeCharOne color of the first field
     * @param codeIntOne number of the first field
     * @param codeCharTwo color of the second field
     * @param codeIntTwo number of the second field
     * @return true - the fields are each other's neighbors, false - they are not
     */
    public boolean areNeighbors(char  codeCharOne, int codeIntOne, char  codeCharTwo, int codeIntTwo){

        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == codeCharOne && fields[x][y].getNumber() == codeIntOne)
                        return fields[x][y].isNeighbor(codeCharTwo, codeIntTwo);
                }
            }
        }
        return false;
    }

    /**
     * Checks if fields of given color-position codes are within 1-move reach.
     * @param codeCharOne color of the first field
     * @param codeIntOne number of the first field
     * @param codeCharTwo color of the second field
     * @param codeIntTwo number of the second field
     * @return true - the fields are each other's "far neighbors", false - they are not
     */
    public boolean areFarNeighbors(char  codeCharOne, int codeIntOne, char  codeCharTwo, int codeIntTwo){
        Field fieldOne = null, fieldTwo = null;

        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == codeCharOne && fields[x][y].getNumber() == codeIntOne)
                        fieldOne = fields[x][y];

                    if (fields[x][y].getColor() == codeCharTwo && fields[x][y].getNumber() == codeIntTwo)
                        fieldTwo = fields[x][y];
                }
            }
        }

        if(Objects.isNull(fieldOne) || Objects.isNull(fieldTwo)){
            return false;
        }

        Stack<Field> stack = new Stack<>();
        ArrayList<Field> list = new ArrayList<>();

        stack.push(fieldOne);

        while(!stack.empty()){
            Field temp = stack.pop();

            if(temp.equals(fieldTwo)){
                return true;
            }

            list.add(temp);

            for(Field field: jumpNeighbors(temp)){
                if(!list.contains(field)){
                    stack.push(field);
                }
            }
        }

        return false;
    }

    /**
     * Returns an array of fields within one jump of the given field.
     * @param field field
     * @return array of fields within one jump of the given field.
     */
    public ArrayList<Field> jumpNeighbors(Field field) {
        ArrayList<Field> jumpNeighbors = new ArrayList<>();
        for (Pair neighbor : field.getNeighbors()) {
            if (!isNull(neighbor.first.getOccupant())) {
                try {
                    switch (neighbor.second) {
                        case "west":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() - 2][neighbor.first.getGridCoordinateY()].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() - 2][neighbor.first.getGridCoordinateY()]);
                            break;
                        case "north_west":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() - 1][neighbor.first.getGridCoordinateY() - 1].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() - 1][neighbor.first.getGridCoordinateY() - 1]);
                            break;
                        case "north_east":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() + 1][neighbor.first.getGridCoordinateY() - 1].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() + 1][neighbor.first.getGridCoordinateY() - 1]);
                            break;
                        case "east":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() + 2][neighbor.first.getGridCoordinateY()].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() + 2][neighbor.first.getGridCoordinateY()]);
                            break;
                        case "south_east":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() + 1][neighbor.first.getGridCoordinateY() + 1].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() + 1][neighbor.first.getGridCoordinateY() + 1]);
                            break;
                        case "south_west":
                            if (isNull(fields[neighbor.first.getGridCoordinateX() - 1][neighbor.first.getGridCoordinateY() + 1].getOccupant()))
                                jumpNeighbors.add(fields[neighbor.first.getGridCoordinateX() - 1][neighbor.first.getGridCoordinateY() + 1]);
                            break;
                    }
                } catch (NullPointerException e) {}
                catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return jumpNeighbors;
    }

    public int getY() {return Y;}
    public int getX() {return X;}
    public boolean[][] getGrid() {return grid;}
    public Field[][] getFields() {return fields;}
    public char[] getPlayersColours(){
        return playersColours;
    }

}


