public class Game{
    private Board board;
    private Player[] playersList;
    private int expectedNumberOfPlayers;
    private int currentNumberOfPlayers;
    private Prophet prophet;
    private boolean play = false;

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
        currentPlayer = player;         //who connect last starts the game

        writeToAll("New player joined! Current state: " + currentNumberOfPlayers + "/" + expectedNumberOfPlayers);

        if(currentNumberOfPlayers == expectedNumberOfPlayers){
            play= true;
        }

    }

    public void isWinner(Player player){
        if(prophet.isWinner(player, board)) {
            writeToAll("Winner is Player" + player.getNumber());
            stopGame();
        }
    }

    public void playerLeft(int playerNumber){
        writeToAll("Player " + playerNumber +"left the game :(");
        stopGame();
    }

    private void nextPlayer(){
        currentPlayer  = playersList[(currentPlayer.getNumber() + 1) % expectedNumberOfPlayers];
    }

    public synchronized void move(FieldCode fieldFrom, FieldCode fieldTo, int playerNumber) throws MoveException{
        if(play) {
            if (currentPlayer.getNumber() == playerNumber) {
                if (prophet.move(fieldFrom, fieldTo, playersList[playerNumber],  board)) {
                    board.setField(fieldFrom.getKey(), fieldFrom.getValue(), null);
                    board.setField(fieldFrom.getKey(), fieldFrom.getValue(), playersList[playerNumber]);
                    writeToAll("Player " + playerNumber + " made move from  " + fieldFrom.getKey() + fieldFrom.getValue() + " to " + fieldTo.getKey() + fieldTo.getValue());
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

    private void writeToAll(String message){
        for(Player player: playersList){
            if(player != null){
                player.write(message);
            }
        }
    }

    public void stopGame(){
        play = false;
        for(Player player: playersList){
            if(player != null){
                player.endGame();
            }
        }
    }
}
