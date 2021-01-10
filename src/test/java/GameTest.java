import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.mockito.Mock;

import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GameTest {
    private Game game;
    private int numberOfPlayers;
    private int sizeOfBoard;


    @Mock
    private Prophet prophet = mock(Prophet.class);

    @Mock
    private Player player1= mock(Player.class);
    @Mock
    private Player player2= mock(Player.class);


    @Before
    public void m1() {
        numberOfPlayers = 2;
        sizeOfBoard = 5;

        doNothing().when(player1).setNumber(anyInt());
        doNothing().when(player1).writeSetup(anyInt(), anyInt());
        doNothing().when(player1).move(any());
        doNothing().when(player1).writeColour(any());
        doNothing().when(player1).endGame();
        when(player1.getNumber()).thenReturn(0);

        doNothing().when(player2).setNumber(anyInt());
        doNothing().when(player2).writeSetup(anyInt(), anyInt());
        doNothing().when(player2).move(any());
        doNothing().when(player2).writeColour(any());
        doNothing().when(player2).endGame();
        when(player2.getNumber()).thenReturn(1);

        when(prophet.move(any(), any(), any(), any())).thenReturn(true);
        when(prophet.isWinner(any(), any())).thenReturn(true);        //first move wins game

    }

    @Test (expected = Exception.class)
    public void test1() throws Exception {
        numberOfPlayers = 7;
        try {
            game = new Game(numberOfPlayers, prophet, sizeOfBoard);
        }catch(Exception e){
            assertEquals("Wrong number of players", e.getMessage());
            throw e;
        }
    }

    @Test
    public void test2() throws Exception {
        game = new Game(numberOfPlayers, prophet, sizeOfBoard);

        game.addPlayer(player1);
        verify(player1).setNumber(0);
        verify(player1).writeSetup(5, 2);
        verify(player1).writeMessage("New player joined! Current state: 1/2");

        game.addPlayer(player2);
        verify(player2).setNumber(1);
        verify(player2).writeSetup(5, 2);
        verify(player1).writeMessage("New player joined! Current state: 2/2");
        verify(player2).writeMessage("New player joined! Current state: 2/2");

        verify(player1).writeMessage("Game begins now!");
        verify(player2).writeMessage("Game begins now!");

        verify(player1).writeMessage("Your turn");

        verify(player1).writeColour("Red");
        verify(player2).writeColour("Green");

        game.move(new FieldCode('R', 1), new FieldCode('R', 2), player1.getNumber());       //automatically win, because of @Before

        verify(player1).move("R1 R2");
        verify(player2).move("R1 R2");

        verify(player1).writeMessage("Player 0 has won!");
        verify(player2).writeMessage("Player 0 has won!");

        verify(player1).endGame();
        verify(player2).endGame();
    }

    @Test
    public void test3() throws Exception {
        game = new Game(numberOfPlayers, prophet, sizeOfBoard);

        game.addPlayer(player1);
        verify(player1).setNumber(0);
        verify(player1).writeSetup(5, 2);
        verify(player1).writeMessage("New player joined! Current state: 1/2");

        game.addPlayer(player2);
        verify(player2).setNumber(1);
        verify(player2).writeSetup(5, 2);
        verify(player1).writeMessage("New player joined! Current state: 2/2");
        verify(player2).writeMessage("New player joined! Current state: 2/2");

        verify(player1).writeMessage("Game begins now!");
        verify(player2).writeMessage("Game begins now!");

        verify(player1).writeMessage("Your turn");

        verify(player1).writeColour("Red");
        verify(player2).writeColour("Green");

        game.playerLeft(player1.getNumber());       //IMPORTANT LINE

        verify(player2).writeMessage("Player " + player1.getNumber() + " left the game :(");

        verify(player1, never()).endGame();
        verify(player2).endGame();
    }
}
