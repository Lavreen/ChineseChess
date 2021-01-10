import org.junit.Test;

import static org.junit.Assert.*;

public class ProphetFactoryTest {
    @Test (expected = Exception.class)
    public void test1() throws Exception {
        ProphetFactory prophetFactory = new ConcreteProphetFactory();
        Prophet prophet = prophetFactory.getProphet(-1);
    }

    @Test
    public void test2() throws Exception {
        ProphetFactory prophetFactory = new ConcreteProphetFactory();
        Prophet prophet = prophetFactory.getProphet(1);
        assertTrue(prophet instanceof Prophet_1);
        assertFalse(prophet instanceof Prophet_2);
    }

    @Test
    public void test3() throws Exception {
        ProphetFactory prophetFactory = new ConcreteProphetFactory();
        Prophet prophet = prophetFactory.getProphet(2);
        assertFalse(prophet instanceof Prophet_1);
        assertTrue(prophet instanceof Prophet_2);
    }
}
