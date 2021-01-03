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
    private int sizeOfBoard;
    private Player[] playersList;
    private int expectedNumberOfPlayers;
    private int currentNumberOfPlayers;
    private Prophet prophet;
    private boolean play = false;

    private Player currentPlayer;

    public Game(int expectedNumberOfPlayers, Prophet prophet, int sizeOfBoard) throws Exception {
        if(!numberOfPlayersList.contains(expectedNumberOfPlayers)){
            throw new Exception();
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
            wait(1000); //narazie brzydko
        } catch (InterruptedException e) {
        }

        writeMessageToAll("New player joined! Current state: " + currentNumberOfPlayers + "/" + expectedNumberOfPlayers);
        player.writeSetup(sizeOfBoard, expectedNumberOfPlayers);

        if(currentNumberOfPlayers == expectedNumberOfPlayers){
            currentPlayer = playersList[0];
            writeMessageToAll("Game begins now!!!");
            board = new Board(sizeOfBoard, playersList);
            play= true;
        }

    }


    /**
     * Checks if player is winner
     * @param player player
     * @see Player
     */
    private void isWinner(Player player){
        if(prophet.isWinner(player, board)) {
            writeMessageToAll("Winner is Player " + player.getNumber());
            stopGame();
        }
    }

    /**
     * Signal about player who left
     * @param playerNumber number of player
     * @see Player
     */
    public void playerLeft(int playerNumber){
        writeMessageToAll("Player " + playerNumber +" left the game :(");
        stopGame();
    }

    /**
     * Method which switch currentPlayer to next player from list
     */
    private void nextPlayer(){
        currentPlayer  = playersList[(currentPlayer.getNumber() + 1) % expectedNumberOfPlayers];
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
            throw new MoveException("Game not stated!");
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
