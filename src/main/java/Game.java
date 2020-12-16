import java.util.ArrayList;

public class Game{
    private Board board;
    private Player[] playersList;
    private int expectedNumberOfPlayers;
    private int currentNumberOfPlayers;
    private Prophet prophet;

    private Player currentPlayer;

    public Game(int expectedNumberOfPlayers, Prophet prophet){
        currentNumberOfPlayers =  0;
        this.expectedNumberOfPlayers = expectedNumberOfPlayers;
        this.prophet = prophet;

        playersList = new Player[expectedNumberOfPlayers];
        board = new Board(5);   // for now always size 5
    }

    public void addPlayer(Player player){
        player.setNumber(currentNumberOfPlayers);
        playersList[currentNumberOfPlayers] = player;
        currentNumberOfPlayers++;
    }

    public boolean hasWinner(){
        return true;        //for now
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    private void nextPlayer(){
        currentPlayer  = playersList[(currentPlayer.getNumber() + 1) % expectedNumberOfPlayers];
    }

    public synchronized void move(Field fieldFrom, Field fieldTo, int playerNumber){    //Jutro tutaj zacznij, zrób bląd  który jeśli if nie spełniony wysyla odpowiadnia wiadomosc do player
        if(currentPlayer.getNumber() == playerNumber && prophet.move(fieldFrom, fieldTo, playersList[playerNumber])){
            fieldFrom.setOwner(null);
            fieldTo.setOwner(playersList[playerNumber]);
            nextPlayer();
        }


    }

}
