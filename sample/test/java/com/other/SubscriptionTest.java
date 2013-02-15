package com.other;

import com.example.Subscription;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SubscriptionTest {

    @Test
    public void getPricePerMonth() {
        Subscription s = new Subscription(200, 2);
        assertTrue(s.getPricePerMonth() == 100.0);
    }

}
