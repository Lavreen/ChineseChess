import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FieldCodeTest {
    @Test
    public void test1(){
        FieldCode fieldCode  = new FieldCode('T', 123);
        assertEquals('T', fieldCode.getKey());
        assertEquals(123, fieldCode.getValue());
    }
}
