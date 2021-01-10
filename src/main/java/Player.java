import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class which supports connection with Class Client
 * @see Client
 */

public class  Player implements Runnable{
    private final Socket socket;
    private Scanner input;
    private PrintWriter output;
    private final Game game;
    private boolean ready = false;

    private int number = -1;

    public Player(Socket socket, Game game){
        this.socket = socket;
        this.game = game;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

    /**
     * Send message to Client
     * @param message message
     */
    public void writeMessage(String message){
        if(ready) {
            output.println("MESSAGE " + message);       //space is very important there
        }
    }

    /**
     * Send setup to Client
     * @param sizeOfBoard size of board
     * @param numberOfPlayers number  of players
     */
    public void writeSetup(int sizeOfBoard, int numberOfPlayers){
        if(ready) {
            output.println("SETUP " + sizeOfBoard + " " + numberOfPlayers);       //space is very important there
        }
    }

    public void writeColour(String colour){
        output.println("COLOUR "  + colour);
    }

    /**
     * Send move command to Client
     * @param message move command
     */
    public void move(String message){
        if(ready) {
            output.println("MOVE " + message);      //space is very important there
        }
    }

    @Override
    public void run() {
        while(number == -1){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        try {
            setup();
            ready = true;
            processCommands();
        }
        catch (IOException e) {}
        finally {
                game.playerLeft(number);
            try {
                socket.close();
            } catch (IOException e) {}
        }

    }

    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Method which supports connection with Class Client
     */
    private void processCommands(){
        while(input.hasNextLine()){
            String command = input.nextLine();

            if(command.startsWith("QUIT")){
                return;
            }
            else if(command.startsWith("MOVE")){
                int temp1 = command.indexOf(" ", 1) + 1;
                int temp2 = command.indexOf(" ", temp1);

                if(temp1 == 0 || temp2 == -1){
                    writeMessage("Not enought arguments");
                }
                else{

                    String fieldFromS = command.substring(temp1, temp2);      // second word
                    String fieldToS = command.substring(temp2 + 1);       // third word

                    try {
                        FieldCode fieldFrom = new FieldCode(fieldFromS.charAt(0), Integer.parseInt(fieldFromS.substring(1)));
                        FieldCode fieldTo = new FieldCode(fieldToS.charAt(0), Integer.parseInt(fieldToS.substring(1)));
                        game.move(fieldFrom, fieldTo, number);
                    } catch (NumberFormatException e) {
                        writeMessage("Wrong field codes: " + fieldFromS + " " + fieldToS);
                    } catch (MoveException e) {
                        writeMessage(e.getMessage());
                    }
                }

            }

        }
    }

    /**
     * Send game_over command to Client
     */
    public void endGame(){
            output.println("GAME_OVER");
    }
}
