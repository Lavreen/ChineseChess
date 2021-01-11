import java.util.ArrayList;
import java.util.List;


/**
 * Class which connects all aspect of games: rules, players and board
 * @see Prophet_1
 * @see Player
 * @see Board
 */

public class Game{
    private final List<Integer> numberOfPlayersList = new ArrayList<>(List.of(2, 3, 4, 6));
    private Board board;
    private final int sizeOfBoard;
    private Player[] playersList;
    private char[] playersColours;
    private final int expectedNumberOfPlayers;
    private int currentNumberOfPlayers;
    private final Prophet prophet;
    private boolean play = false;

    private Player currentPlayer;

    public Game(int expectedNumberOfPlayers, Prophet prophet, int sizeOfBoard) throws Exception {
        if(!numberOfPlayersList.contains(expectedNumberOfPlayers)){
            throw new Exception("Wrong number of players");
        }
        currentNumberOfPlayers =  0;
        this.expectedNumberOfPlayers = expectedNumberOfPlayers;
        this.prophet = prophet;
        this.sizeOfBoard = sizeOfBoard;

        playersList = new Player[expectedNumberOfPlayers];
    }


    /**
     * Method which adds player to game
     * @param player player
     */
    public synchronized void addPlayer(Player player){
        player.setNumber(currentNumberOfPlayers);
        playersList[currentNumberOfPlayers] = player;
        currentNumberOfPlayers++;

        try {
            wait(1000);
        } catch (InterruptedException e) {
            e.getLocalizedMessage();
        }
        player.writeSetup(sizeOfBoard, expectedNumberOfPlayers);
        writeMessageToAll("New player joined! Current state: " + currentNumberOfPlayers + "/" + expectedNumberOfPlayers);


        if(currentNumberOfPlayers == expectedNumberOfPlayers){
            currentPlayer = playersList[0];
            writeMessageToAll("Game begins now!");
            board = new Board(sizeOfBoard, playersList);
            play= true;
            currentPlayer.writeMessage("Your turn");
            playersColours = board.getPlayersColours();
            setColours();
        }

    }


    /**
     * Checks if player is winner
     * @param player player
     * @see Player
     */
    private void isWinner(Player player){
        if(prophet.isWinner(player, board)) {
            writeMessageToAll("Player " + player.getNumber() + " has won!");
            stopGame();
        }
    }

    /**
     * Signal about player who left
     * @param playerNumber number of player
     * @see Player
     */
    public void playerLeft(int playerNumber){
        playersList[playerNumber] = null;
        writeMessageToAll("Player " + playerNumber + " left the game :(");
        stopGame();
    }

    /**
     * Method which switch currentPlayer to next player from list
     */
    private void nextPlayer(){
        currentPlayer.writeMessage("Wait for your opponents' moves");
        currentPlayer  = playersList[(currentPlayer.getNumber() + 1) % expectedNumberOfPlayers];
        currentPlayer.writeMessage("Your turn");
    }

    /**
     * Analysis of move which player wanna make.
     * @param fieldFrom field from player wanna move
     * @param fieldTo   field to player wanna move
     * @param playerNumber number of player
     * @throws MoveException move not allowed
     * @see Player
     */
    public synchronized void move(FieldCode fieldFrom, FieldCode fieldTo, int playerNumber) throws MoveException{
        if(play) {
            if (currentPlayer.getNumber() == playerNumber) {
                if (prophet.move(fieldFrom, fieldTo, playersList[playerNumber],  board)) {
                    board.setFieldOccupant(fieldFrom.getKey(), fieldFrom.getValue(), null);
                    board.setFieldOccupant(fieldTo.getKey(), fieldTo.getValue(), playersList[playerNumber]);

                    writeMoveToAll(fieldFrom.getKey() + "" + fieldFrom.getValue() + " " + fieldTo.getKey() + "" + fieldTo.getValue());
                    isWinner(currentPlayer);
                    nextPlayer();
                } else {
                    throw new MoveException("You can't make that move!");
                }
            } else {
                throw new MoveException("Not your turn!");
            }
        }
        else {
            throw new MoveException("The game has not started!");
        }
    }


    public void skipMove(Player player){
        if(play) {
            if (currentPlayer.equals(player)) {
                nextPlayer();
            }
        }
    }

    /**
     * Send message signal to players
     * @param message message
     * @see Player
     */
    private void writeMessageToAll(String message){
        for(Player player: playersList){
            if(player != null){
                player.writeMessage(message);
            }
        }
    }

    private void setColours(){
        int i = 0;
        for(Player player: playersList){
            if(player != null){
                switch (playersColours[i]){
                    case 'R' -> player.writeColour("Red");
                    case 'B' -> player.writeColour("Blue");
                    case 'V' -> player.writeColour("Violet");
                    case 'G' -> player.writeColour("Green");
                    case 'W' -> player.writeColour("White");
                    case 'Y' -> player.writeColour("Yellow");
                }
                i++;
            }
        }
    }


    /**
     * Send move signal to players
     * @param message move information
     * @see Player
     */
    private void writeMoveToAll(String message){
        for(Player player: playersList){
            if(player != null){
                player.move(message);
            }
        }
    }

    /**
     * Send end game signal to players
     * @see Player
     */
    private void stopGame(){
        play = false;
        for(Player player: playersList){
            if(player != null){
                player.endGame();
            }
        }
    }
}
