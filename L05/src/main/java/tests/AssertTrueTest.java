package tests;

import testtools.annotations.Test;

import static testtools.asserts.Assert.assertTrue;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public class AssertTrueTest extends SimpleTest {

    @Test
    public void firstTest() {
        System.out.println("This is assertTrue test, it should pass");
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void secondTest() {
        System.out.println("This is assertTrue test, it shouldn't pass");
        assertTrue(Boolean.FALSE);
    }

}
