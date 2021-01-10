import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class ProphetTest {
    private Prophet prophet;
    private final ProphetFactory prophetFactory = new ConcreteProphetFactory();

    @Mock
    Board board = mock(Board.class);

    @Mock
    Player player = mock(Player.class);


    @Before
    public void m1(){
        when(board.getFieldOccupant('R', 1)).thenReturn(player);
        when(board.getFieldOccupant('R', 2)).thenReturn(null);
        when(board.areNeighbors(anyChar(), anyInt(), anyChar(), anyInt())).thenReturn(false);
        when(board.areFarNeighbors(anyChar(), anyInt(), anyChar(), anyInt())).thenReturn(true);
    }

    @Test
    public void test1() throws Exception {
        prophet = prophetFactory.getProphet(1);

        boolean temp  = prophet.move(new FieldCode('R',1), new FieldCode('R',2), player, board);
        assertTrue(temp);

        verify(board, times(2)).getFieldOccupant('R', 1);
        verify(board).getFieldOccupant('R', 2);
        verify(board).areNeighbors('R', 1, 'R', 2);
        verify(board).areFarNeighbors('R', 1, 'R', 2);

    }

    @Test
    public void test2() throws Exception {
        when(board.areFarNeighbors(anyChar(), anyInt(), anyChar(), anyInt())).thenReturn(false);

        prophet = prophetFactory.getProphet(1);

        boolean temp = prophet.move(new FieldCode('R',1), new FieldCode('R',2), player, board);

        assertFalse(temp);
    }

    @Test
    public void test2_2() throws Exception {
        when(board.areNeighbors(anyChar(), anyInt(), anyChar(), anyInt())).thenReturn(true);

        prophet = prophetFactory.getProphet(1);

        boolean temp  = prophet.move(new FieldCode('R',1), new FieldCode('R',2), player, board);
        assertTrue(temp);

        verify(board, times(2)).getFieldOccupant('R', 1);
        verify(board).getFieldOccupant('R', 2);
        verify(board).areNeighbors('R', 1, 'R', 2);
        verify(board, never()).areFarNeighbors(anyChar(), anyInt(), anyChar(),anyInt());

    }

    @Test
    public void test3() throws Exception {

        prophet = prophetFactory.getProphet(2);

        boolean temp  = prophet.move(new FieldCode('R',1), new FieldCode('R',2), player, board);
        assertTrue(temp);

        verify(board, times(2)).getFieldOccupant('R', 1);
        verify(board).getFieldOccupant('R', 2);
    }

    @Test
    public void test4() throws Exception {
        when(board.hasHeClaimedAllTargets(any())).thenReturn(true);

        prophet = prophetFactory.getProphet(1);

        boolean temp  = prophet.isWinner(player, board);
        assertTrue(temp);

        verify(board).hasHeClaimedAllTargets(player);
    }

    @Test
    public void test5() throws Exception {
        when(board.hasHeClaimedAllTargets(any())).thenReturn(true);

        prophet = prophetFactory.getProphet(2);

        boolean temp  = prophet.isWinner(player, board);
        assertTrue(temp);

        verify(board).hasHeClaimedAllTargets(player);
    }
}
