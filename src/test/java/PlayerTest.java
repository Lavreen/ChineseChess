import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.mockito.Mock;

import org.junit.Before;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player;

    @Mock
    Game game = mock(Game.class);

    @Mock
    Socket socket = mock(Socket.class);

    OutputStream outputStream = new OutputStream() {
        private StringBuilder string = new StringBuilder();

        @Override
        public void write(int b) {
            this.string.append((char) b);
        }

        public String toString() {
            StringBuilder temp = string;
            string = new StringBuilder();
            return temp.toString();
        }
    };

    InputStream inputStream;

    @Before
    public void m1() throws Exception {
        String input = "MOVE A1 B2\nMOVE AA BB\nMOVE AA";
        inputStream = new ByteArrayInputStream(input.getBytes());
        player = new Player(socket, game);
        doNothing().when(game).addPlayer(any());
        doNothing().when(game).move(any(), any(), anyInt());
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
    }

    @Test
    public void test1() {
        player.setNumber(1);
        assertEquals(1, player.getNumber());
    }

    @Test
    public void test2() {
        try {
            Player spyPlayer = Mockito.spy(player);
            player.setNumber(1);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(player);
            executor.awaitTermination(2, TimeUnit.SECONDS);

            verify(spyPlayer, times(0)).endGame();
            verify(socket).getInputStream();
            verify(socket).getOutputStream();
            verify(game).playerLeft(anyInt());

            String trash = outputStream.toString(); //MOVE AA BB and MOVE AA are incorrect commends
            assertEquals("MESSAGE Wrong field codes: AA BB\r\n" +
                    "MESSAGE Not enought arguments\r\n", trash);

            player.writeMessage("message");
            assertEquals("MESSAGE message\r\n", outputStream.toString());     //\r\n because I used printf

            player.writeSetup(2, 2);
            assertEquals("SETUP 2 2\r\n", outputStream.toString());

            player.writeColour("Red");
            assertEquals("COLOUR Red\r\n", outputStream.toString());

            player.move("A1 B2");
            assertEquals("MOVE A1 B2\r\n", outputStream.toString());

            player.endGame();
            assertEquals("GAME_OVER\r\n", outputStream.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}