import static java.util.Objects.isNull;

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

    private static int redOriginX, redOriginY;  // coordinates of the vertex from which the triangle generation starts
    private static int blackOriginX, blackOriginY;
    private static int violetOriginX, violetOriginY;
    private static int greenOriginX, greenOriginY;
    private static int whiteOriginX, whiteOriginY;
    private static int yellowOriginX, yellowOriginY;
    private static int hexagonOriginX, hexagonOriginY;

    private int generatedTrianglesCount = 0;

    public Board(int size, Player[] players) {
        this.players = players;
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
                switch (players.length) { // do dorobienia dla 4 i 6 graczy
                    case 2:
                        if (generatedTrianglesCount == 0 || generatedTrianglesCount == 3) {
                            targetOf = (generatedTrianglesCount + 1) % 2;
                            fields[x][y].setTargetOf(players[targetOf]);
                        }
                        if (generatedTrianglesCount == 0)
                            fields[x][y].setOccupant(players[0]);
                        if(generatedTrianglesCount == 3)
                            fields[x][y].setOccupant(players[1]);
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
                        if (generatedTrianglesCount % 2 == 0)
                            fields[x][y].setOccupant(players[generatedTrianglesCount]);
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

    public boolean isWinner(Player player) {
        int count = 0;
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                if(!isNull(fields[x][y]) && !isNull(fields[x][y].getOccupant()) && !isNull(fields[x][y].getTargetOf())){
                    if (fields[x][y].getOccupant().equals(player) && fields[x][y].getTargetOf().equals(fields[x][y].getOccupant()))
                        count++;
                    if (count == triangleFieldCount())
                        return true;
                }
            }
        }
        return false;
    }

    private int triangleFieldCount() {
        int count = 1;
        for (int i = 2; i <= SIZE; i++)
            count += i;
        return count;
    }

    public boolean areNeighbours(char  codeCharOne, int codeIntOne, char  codeCharTwo, int codeIntTwo){

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


}
