package tests;

import testtools.annotations.Test;

import static testtools.asserts.Assert.assertFail;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public class AssertFailTest extends SimpleTest {

    @Test
    public void fifthTest() {
        System.out.println("This is assertFail test, it shouldn't pass");
        assertFail("This test is intentionally failed");
    }
}
