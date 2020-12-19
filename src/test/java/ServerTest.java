import org.junit.Test;

import java.lang.ref.Cleaner;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ServerTest {

    @Test   //testing if Server and client can connect
    public void test1(){

        Thread serverTest = new Thread(new TestThreadServer());
        serverTest.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientTest1 = new Thread(new TestThreadClient());
        clientTest1.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread clientTest2 = new Thread(new TestThreadClient());
        clientTest2.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class TestThreadServer implements Runnable{
        @Override
        public void run() {
            Server.main(new String[0]);
        }
    }

    class TestThreadClient implements Runnable{
        @Override
        public void run() {
            String[] temp = {"127.0.0.1"};
            Client.main(temp);
        }
    }

}
