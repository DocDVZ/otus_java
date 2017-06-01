package testtools.asserts;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public class AssertException extends RuntimeException {

    public AssertException(){}

    public AssertException(String message){super(message, null, true, false);}

}
