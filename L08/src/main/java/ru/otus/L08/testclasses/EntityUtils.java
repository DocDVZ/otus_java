package ru.otus.L08.testclasses;

import java.util.List;
import java.util.Random;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public final class EntityUtils {

    private static Random random = new Random();

    private EntityUtils(){}

    public static  <E> E getRandomElement(List<E> list){
        return list.get(random.nextInt(list.size()));
    }


}
