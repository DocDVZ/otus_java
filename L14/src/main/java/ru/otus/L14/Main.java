package ru.otus.L14;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {

    private static final Random RAND = new Random();   // random number generator


    public static void main(String[] args) {
        int LENGTH = 1000;   // initial length of array to sort
        int RUNS   =  10;   // how many times to grow by 2?

        for (int i = 1; i <= RUNS; i++) {
            int[] a = createRandomArray(LENGTH);
            int threads = i*2;
            // run the algorithm and time how long it takes
            long startTime1 = System.currentTimeMillis();
            ParallelSorter ps = new ParallelSorter(threads);
            int[] b = ps.sort(a);
            long endTime1 = System.currentTimeMillis();

            if (!isSorted(b)) {
                throw new RuntimeException("not sorted afterward: " + Arrays.toString(b));
            }
            System.out.printf("%10d elements, my sort, %2d threads  =>  %6d ms \n", LENGTH,  threads, endTime1 - startTime1);
            long startTime2 = System.currentTimeMillis();
            int[] c = IntStream.of(a).parallel().sorted().toArray();
            long endTime2 = System.currentTimeMillis();
            System.out.printf("%10d elements, parallelStream %6s=>  %6d ms \n", LENGTH, "", endTime2 - startTime2);
            long startTime3 = System.currentTimeMillis();
            int[] d = IntStream.of(a).sorted().toArray();
            long endTime3 = System.currentTimeMillis();
            System.out.printf("%10d elements, stream %14s=>  %6d ms \n", LENGTH, "", endTime3 - startTime3);
            System.out.println();
            LENGTH *= 2;   // double size of array for next time
        }
    }

    // Creates an array of the given length, fills it with random
    // non-negative integers, and returns it.
    public static int[] createRandomArray(int length) {
        int[] a = new int[length];
        for (int i = 0; i < a.length; i++) {
            a[i] = RAND.nextInt(1000000);
            // a[i] = RAND.nextInt(40);
        }
        return a;
    }

    // Returns true if the given array is in sorted ascending order.
    public static boolean isSorted(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                return false;
            }
        }
        return true;
    }

}
