import java.util.*;

/**
 * Created by DocDVZ on 15.04.2017.
 */
public class Main {



    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10};
        MyArrayQueue<Integer> q = new MyArrayQueue<>();
        log("====== add ======");
        for (Integer i : arr){
            q.add(i);
        }
        log(q);
        log("====== poll ======");
        q.poll();
        q.add(11);
        q.add(12);
        q.poll();
        log(q);
        Integer[] arr2 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8 ,9, 10};
        for (Integer i : arr2){
            q.add(i);
        }
        log(q);
        q.poll();
        q.size();
        q.add(11);
        q.add(12);
        q.poll();
        log(q);
        log("====== Iterator ======");
        Iterator<Integer> i = q.iterator();
        StringBuilder sb = new StringBuilder();
        while (i.hasNext()){
            sb.append(" " + i.next());
        }
        log(sb);
        q.remove(12);
        log(q);
        log("====== removeAll ======");
        List<Integer> l2 = Arrays.asList(3,4,5);
        q.removeAll(l2);
        log(q);
        log("====== retainAll ======");
        List<Integer> l3 = Arrays.asList(6,7,8);
        q.retainAll(l3);
        log(q);
    }

    private static void log(Object s){
        System.out.println(s);
    }
}
