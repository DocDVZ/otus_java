package tests;

import testtools.annotations.After;
import testtools.annotations.Before;
import testtools.annotations.Test;
import testtools.asserts.Assert;

import static testtools.asserts.Assert.*;


/**
 * Created by DocDVZ on 09.05.2017.
 */
@Test
public abstract class SimpleTest {

    @Before
    public void before() {
        System.out.println("=== New test starts ===");
    }

    @After
    public void after() {
        System.out.println("===  Test finished  ===\n\n");
    }


}
