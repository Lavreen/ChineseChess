import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        int numberOfPlayers;

        try {
            ServerSocket listner =  new ServerSocket(6666);

            System.out.println("Our server is running...  YEY!");

            ExecutorService pool = Executors.newFixedThreadPool(10);

            numberOfPlayers = 2;    //for now always 2

            //mode                    //plan for future

            while(true) {
                Prophet prophet = new Prophet();        //connected with mode  plans
                Game game = new Game(numberOfPlayers, prophet);
                for(int i = 0; i < numberOfPlayers; i++){
                    Player player;
                    pool.execute(player = new Player(listner.accept(), game));
                    game.addPlayer(player);
                }
            }
        }
        catch (IOException e) {
            System.out.println("Something wrong happend with server a start phase");
        }
    }



}
