import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by dzvyagin on 26.04.2017.
 * -XmX512m
 * -XmS512m
 *
 */
public class Main {


    public static void main(String[] args) {

        System.out.println("Starting: " + ManagementFactory.getRuntimeMXBean().getName());

        try {
            // sleep to start monitoring tool
            Thread.sleep(10000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }


        Queue<Object> list = new LinkedList<>();
        GCStatistics gc = new GCStatistics();
        long iterator = 0;
        try {
        while (iterator < 100_000_000){
            list.add(new Object());
            if (iterator%10000==0){
                for (int k=0; k<6000;k++){
                    list.poll();
                }
                Thread.sleep(20);
            }
            if (iterator%500000==0){

                    Thread.sleep(100);
                    gc.printGC();

            }
            iterator++;
        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
