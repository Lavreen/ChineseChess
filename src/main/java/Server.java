import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class.
 * Run it to start a server.
 */
public class Server {
    public static void main(String[] args) {
        int numberOfPlayers;

        try {
            ServerSocket listener =  new ServerSocket(6666);

            System.out.println("Our server is running...  YEY!");

            ExecutorService pool = Executors.newFixedThreadPool(10);

            numberOfPlayers = 2;    //for now always 2

            //mode                    //plan for future

            Prophet prophet = new Prophet();        //connected with mode

            while(true) {
                Game game = new Game(numberOfPlayers, prophet);
                for(int i = 0; i < numberOfPlayers; i++){
                    Player player;
                    pool.execute(player = new Player(listener.accept(), game));
                    game.addPlayer(player);
                }
                System.out.println("All players joined");
            }
        }
        catch (IOException e) {
            System.out.println("Something wrong happend with server in a start phase");
        }
    }



}
