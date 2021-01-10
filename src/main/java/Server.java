import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Server class.
 * Run it to start a server.
 */
public class Server {
    public static void main(String[] args) throws Exception {
        if(args.length != 3){
            throw new Exception("Input: numberOfPlayers mode boardSize");
        }

        int numberOfPlayers, mode, boardSize;

        try {
            numberOfPlayers = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            throw new Exception("Wrong number of players");
        }

        try {
            mode = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e){
            throw new Exception("Wrong mode number");
        }

        try {
            boardSize = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e){
            throw new Exception("Wrong board size");
        }

        ProphetFactory prophetFactory =  new ConcreteProphetFactory();

        Prophet prophet;

        try {
            prophet = prophetFactory.getProphet(mode);
        } catch (Exception e) {
            throw new Exception("Mode doesn't exist");
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
            throw e;
        }
    }
}
