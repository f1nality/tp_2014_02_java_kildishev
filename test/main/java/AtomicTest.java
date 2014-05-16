import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author d.kildishev
 */
public class AtomicTest extends Thread {
    private static int i = 0;
    private static AtomicInteger iAtomic = new AtomicInteger(0);

    @Test
    public void AtomicTest() throws InterruptedException {
        i = 0;
        iAtomic.set(0);

        Incrementer t1 = new Incrementer();
        Incrementer t2 = new Incrementer();

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("i=" + i);
        System.out.println("iAtomic=" + iAtomic);

        Assert.assertNotEquals(i, iAtomic);
    }

    private static class Incrementer extends Thread {
        public void run() {
            while (iAtomic.get() < 100000000) {
                ++i;
                iAtomic.incrementAndGet();
            }
        }
    }
}
