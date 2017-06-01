package testtools.annotations;

import java.lang.annotation.*;

/**
 * Created by DocDVZ on 09.05.2017.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface Test {


}
