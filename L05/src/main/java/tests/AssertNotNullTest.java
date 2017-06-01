package tests;

import testtools.annotations.Test;

import static testtools.asserts.Assert.assertNotNull;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public class AssertNotNullTest extends SimpleTest {

    @Test
    public void thirdTest() {
        System.out.println("This is assertNotNull test, it shouldn't pass");
        assertNotNull(null);
    }

    @Test
    public void fourthTest() {
        System.out.println("This is assertNotNull test, it should pass");
        assertNotNull(new Object());
    }
}
