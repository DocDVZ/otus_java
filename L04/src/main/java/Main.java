import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * Created by dzvyagin on 26.04.2017.
 * -Xmx512m
 * -Xms512m
 *  (version 8u92+)
 */
public class Main {

    static Queue<Object> list = new LinkedList<>();


    public static void main(String[] args) {

        System.out.println("Starting: " + ManagementFactory.getRuntimeMXBean().getName());

        try {
            // sleep to start monitoring tool
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread gcThread = new Thread(() -> {
            GCStatistics gc = new GCStatistics();
            Object lock = new Object();
            try {
                while (true) {
                    gc.printGC();
                    Thread.sleep(60000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        gcThread.setDaemon(false);
        gcThread.start();
        Object lock = new Object();
        long iterator = 0;
        try {
            while (iterator < 100_000_000) {
                list.add(new Object());
                if (iterator % 10000 == 0) {
                    for (int k = 0; k < 8000; k++) {
                        list.poll();
                    }
                    Thread.sleep(30);
                }
                iterator++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
