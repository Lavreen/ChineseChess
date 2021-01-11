import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class BoardTest {

    private final int size = 4;
    private final Player[] players = new Player[2];
    private Board board;

    @Mock
    Player player1 = mock(Player.class);
    Player player2 = mock(Player.class);
    Player player3 = mock(Player.class);
    Player player4 = mock(Player.class);
    Player player5 = mock(Player.class);
    Player player6 = mock(Player.class);
    Field field1 = mock(Field.class);
    Field field2 = mock(Field.class);

    @Before
    public void m1() {
        players[0] = player1;
        players[1] = player2;
        board = new Board(size, players);
    }

    @Test
    public void test1() {
        board.generateBoard();
        Player[] players2 = new Player[3];
        players2[0] = player1;
        players2[1] = player2;
        players2[2] = player3;
        board = new Board(size, players);
        board.generateBoard();
        Player[] players3 = new Player[4];
        players3[0] = player1;
        players3[1] = player2;
        players3[2] = player3;
        players3[3] = player4;
        board = new Board(size, players);
        board.generateBoard();
        Player[] players4 = new Player[5];
        players4[0] = player1;
        players4[1] = player2;
        players4[2] = player3;
        players4[3] = player4;
        players4[4] = player5;
        board = new Board(size, players);
        board.generateBoard();
        Player[] players5 = new Player[6];
        players5[0] = player1;
        players5[1] = player2;
        players5[2] = player3;
        players5[3] = player4;
        players5[4] = player5;
        players5[5] = player6;
        board = new Board(size, players);
        board.generateBoard();
    }

    @Test
    public void test2() {
        assertEquals(board.getFieldOccupant('R', 1), board.getFieldOccupant('R', 2));
        assertEquals(board.getFieldOccupant('R', 1), players[0]);
        board.setFieldOccupant('R', 1, players[1]);
        assertNotEquals(board.getFieldOccupant('R', 1), players[0]);
    }

    @Test
    public void test3() {
        assertEquals(board.hasHeClaimedAllTargets(players[0]), false);
        assertEquals(board.areNeighbors('R', 1, 'R', 2), board.areNeighbors('R', 1, 'R', 3));
        assertNotEquals(board.areFarNeighbors('R', 4, 'H', 1), board.areNeighbors('R', 6, 'H', 5));
        assertEquals(board.jumpNeighbors(field1), board.jumpNeighbors(field2));
    }

    @Test
    public void test4() {
        Board board1 = new Board(1, players);
        Board board2 = new Board(2, players);
        assertNotEquals(board1.getPlayersColours(), board2.getPlayersColours());
        assertEquals(board1.getX(), board2.getX());
        assertEquals(board1.getY(), board2.getY());
        assertNotEquals(board1.getGrid(), board2.getGrid());
        assertNotEquals(board1.getFields(), board2.getFields());
    }

}