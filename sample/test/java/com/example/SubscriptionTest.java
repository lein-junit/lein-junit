package com.example;

import org.junit.*;
import junit.framework.TestCase;

public class SubscriptionTest extends TestCase {

    public void testGetLength() {
        Subscription s = new Subscription(200, 2);
        assertTrue(s.getLength() == 2);
    }

    public void testGetPrice() {
        Subscription s = new Subscription(200, 2);
        assertTrue(s.getPrice() == 0);
    }

    public void testGetPricePerMonth() {
        Subscription s = new Subscription(200, 2);
        assertTrue(s.getPricePerMonth() == 100.0);
    }

}
