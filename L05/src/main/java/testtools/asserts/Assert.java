package testtools.asserts;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public final class Assert {

    private Assert(){}

    public static void assertTrue(Boolean item){
        if (!item.equals(Boolean.TRUE)){
            throw new AssertException("Assert TRUE failed");
        }
    }

    public static void assertNotNull(Object obj){
        if (obj==null){
            throw new AssertException("Assert NOT NULL failed");
        }
    }

    public static void assertFail(String message){
        throw new AssertException(message);
    }

}
