import java.util.ArrayList;
import java.util.List;

/**
 * Created by DocDVZ on 15.04.2017.
 */
public class Main {



    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10};
        MyArrayQueue<Integer> q = new MyArrayQueue<>();
        for (Integer i : arr){
            q.add(i);
        }
        log(q);
        log(q.size());
        log(q.poll());
        log(q.size());
        q.add(11);
        q.add(12);
        log(q.poll());
        log(q.size());
        log(q);
    }

    private static void log(Object s){
        System.out.println(s);
    }
}
