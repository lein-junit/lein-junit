import org.junit.*;
import static org.junit.Assert.*;

public class SubscriptionTest {

    @Test
    public void testgetPricePerMonth() {
        Subscription s = new Subscription(200, 2);
        assertTrue(s.getPricePerMonth() == 100.0);
    }

}
