import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class FieldTest {
    private Field field, field2;
    private char color = 'R', color2 = 'G';
    private int number = 3, number2 = 2;
    private int gridCoordinateX = 10, gridCoordinateX2 = 11;
    private int gridCoordinateY = 12, gridCoordinateY2 = 13;

    @Mock
    Player player = mock(Player.class);

    @Before
    public void m1() {
        field = new Field(color, number, gridCoordinateX, gridCoordinateY);
        field2 = new Field(color2, number2, gridCoordinateX2, gridCoordinateY2);
    }

    @Test
    public void test1() {
        assertEquals('R', field.getColor());
        assertEquals(3, field.getNumber());
        assertEquals(10, field.getGridCoordinateX());
        assertEquals(12, field.getGridCoordinateY());

    }

    @Test
    public void test2() {

        field.setOccupant(player);
        field2.setOccupant(player);
        assertEquals(field.getOccupant(), player);
        assertEquals(field.getOccupant(), field2.getOccupant());
    }

    @Test
    public void test3() {
        field.setTargetOf(player);
        assertEquals(player, field.getTargetOf());
    }

    @Test
    public void test4() {
        field.addNeighbor(field2, "north");

        List<Pair> tempList = new ArrayList<>();
        tempList.add(new Pair(field2, "north"));

        assertEquals(tempList, field.getNeighbors());
    }

    @Test
    public void test5() {
        Field field2 = new Field('R', 3, 11, 23);
        field.addNeighbor(field2, "north");
        assertEquals(field.isNeighbor(field2.getColor(), field2.getNumber()), true);
    }


}