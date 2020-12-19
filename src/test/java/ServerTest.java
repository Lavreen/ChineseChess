import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;


public class ServerTest {


    @Test   //testing if Server and client can connect
    public void test() {

        Thread serverTest = new Thread(new TestThreadServer());
        serverTest.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread serverTest1 = new Thread(new TestThreadServer());
        serverTest1.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientTest1 = new Thread(new TestThreadClient());
        clientTest1.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientTest2 = new Thread(new TestThreadClient());
        clientTest2.start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

