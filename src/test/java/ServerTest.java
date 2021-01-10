import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class ServerTest {

    Server server = new Server();

    @Test (expected = Exception.class)
    public void test1() throws Exception {
        try {
            String[] args = {"1", "2"};
            server.main(args);
        } catch(Exception e){
            assertEquals("Input: numberOfPlayers mode boardSize", e.getMessage());
            throw e;
        }
    }

    @Test (expected = Exception.class)
    public void test2() throws Exception {
        try {
            String[] args = {"a1", "1", "1"};
            server.main(args);
        } catch(Exception e){
            assertEquals("Wrong number of players", e.getMessage());
            throw e;
        }
    }

    @Test (expected = Exception.class)
    public void test3() throws Exception {
        try {
            String[] args = {"1", "a1", "1"};
            server.main(args);
        } catch(Exception e){
            assertEquals("Wrong mode number", e.getMessage());
            throw e;
        }
    }

    @Test (expected = Exception.class)
    public void test4() throws Exception {
        try {
            String[] args = {"1", "1", "a1"};
            server.main(args);
        } catch(Exception e){
            assertEquals("Wrong board size", e.getMessage());
            throw e;
        }
    }

    @Test (expected = Exception.class)
    public void test5() throws Exception {
        try {
            String[] args = {"1", "-1", "1"};
            server.main(args);
        } catch(Exception e){
            assertEquals("Mode doesn't exist", e.getMessage());
            throw e;
        }
    }
}
