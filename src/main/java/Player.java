import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable{
    private Socket socket;
    Scanner input;
    PrintWriter output;
    Game game;

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

    @Override
    public void run() {
        while(number == -1){
            try {
                wait(500);
            } catch (InterruptedException e) {}
        }

        setup();

    }

    private void setup(){

    }
}
