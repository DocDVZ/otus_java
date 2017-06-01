package testtools;

import org.reflections.Reflections;
import testtools.annotations.After;
import testtools.annotations.Before;
import testtools.annotations.Test;
import testtools.asserts.AssertException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DocDVZ on 09.05.2017.
 */
public class TestRunner {

    public void runTests() {
        Reflections ref = new Reflections();
        for (Class<?> clazz : ref.getTypesAnnotatedWith(Test.class)) {
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                }
                if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    Object obj = ReflectionHelper.instantiate(clazz);

                    try {
                        for (Method m : beforeMethods) {
                            ReflectionHelper.callMethod(obj, m.getName());
                        }
                        ReflectionHelper.callMethod(obj, method.getName());
                        System.out.println("Test " + method.getName() + " successfully passed");
                    } catch (Exception e) {
                        if (e.getCause() instanceof AssertException) {
                            System.out.println("Test " + method.getName() + " failed: " + e.getCause().getMessage());
                        } else {
                            e.printStackTrace();
                        }
                    }

                    try{
                        for (Method m : afterMethods){
                            ReflectionHelper.callMethod(obj, m.getName());
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
