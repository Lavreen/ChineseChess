import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private final Scanner readFromConsole = new Scanner(System.in);     //until graphic interface

    public Client(String serverAddress) throws Exception{
        socket = new Socket(serverAddress, 6666);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        TerminalListener terminalListener  = new TerminalListener();
        Thread thread = new Thread(terminalListener);
        thread.start();
    }

    public void play() throws Exception{
        String response;
        try {
            while (in.hasNextLine()) {
                response = in.nextLine();
                if(response.startsWith("MESSAGE")){
                    System.out.println(response.substring(8));
                }
                else if(response.startsWith("MOVE")){       //Later move in  GUI

                    int temp1 = response.indexOf(" ", 1) + 1;
                    int temp2 = response.indexOf(" ", temp1);

                    String fieldFrom = response.substring(temp1, temp2);      // second word
                    String fieldTo = response.substring(temp2 + 1);       // third word

                    System.out.println("Move from " + fieldFrom +  " to " + fieldTo);
                }
                else if(response.startsWith("GAME_OVER")){
                    System.out.println("Game over");
                    break;
                }
            }
            out.println("QUIT");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
                socket.close();
        }

    }

    class TerminalListener implements Runnable{         //that need to be tested

        public TerminalListener(){}
        @Override
        public void run() {
            while (readFromConsole.hasNextLine()) {
                out.println(readFromConsole.nextLine());
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Bad input, only IP is needed");
            return;
        }
        Board board = new Board(3);
        board.generateBoard();

        //board.printBoard();
        Client client;
        try {
            client = new Client(args[0]);
            client.play();
        } catch (Exception e) {
            System.out.println("Something went wrong!!!");
        }
    }
}
