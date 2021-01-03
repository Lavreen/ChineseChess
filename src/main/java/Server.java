import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

/**
 * Server class.
 * Run it to start a server.
 */
public class Server {
    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Input: numberOfPlayers mode boardSize");
            return;
        }

        int numberOfPlayers, mode, boardSize;

        try {
            numberOfPlayers = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            System.out.println("Wrong number of players");
            return;
        }

        try {
            mode = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e){
            System.out.println("Wrong mode number");
            return;
        }

        try {
            boardSize = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e){
            System.out.println("Wrong board size");
            return;
        }

        ProphetFactory prophetFactory =  new ConcreteProphetFactory();

        Prophet prophet;

        try {
            prophet = prophetFactory.getProphet(mode);
        } catch (Exception e) {
            System.out.println("Mode doesn't exist");
            return;
        }

        try {
            ServerSocket listener =  new ServerSocket(6666);

            System.out.println("Our server is running...  YEY!");

            ExecutorService pool = Executors.newFixedThreadPool(10);

            while(true) {
                Game game = new Game(numberOfPlayers, prophet, boardSize);
                for(int i = 0; i < numberOfPlayers; i++){
                    Player player;
                    pool.execute(player = new Player(listener.accept(), game));
                    game.addPlayer(player);
                }
                System.out.println("All players joined");
            }
        }
        catch (Exception e) {
            System.out.println("Something wrong happend with server in a start phase");
        }
    }
}
