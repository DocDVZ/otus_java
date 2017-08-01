package ru.otus.L14.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.L14.ParallelSorter;

import java.util.Arrays;
import java.util.List;


public class ParallelSorterTest extends Assert {

    @Test
    public void testMerge() {
        int[] arr1 = {3, 5, 12, 13, 15, 16};
        int[] arr2 = {1, 9, 99};
        ParallelSorter ps = new ParallelSorter(4);
        int[] m = ps.merge(arr1, arr2);
        System.out.println(Arrays.toString(m));
        int[] expected = new int[]{1, 3, 5, 9, 12, 13, 15, 16, 99};
        System.out.println(Arrays.toString(expected));
        System.out.println();
        assertArrayEquals(m, expected);

    }

    @Test
    public void testSort() {
        int[] arr = {3, 123, 14, 51, 2, 5251, 12, 5, 3, 33, 123, 1, 23, 31, 122};
        ParallelSorter ps = new ParallelSorter(4);
        int[] sorted = ps.sort(arr);
        assertTrue(isSorted(sorted));
    }

    @Test
    public void testCombine(){
        List<int[]> arrays = Arrays.asList(new int[] {1, 7, 8}, new int[] {2, 9, 10}, new int[] {0, 2, 8});
        ParallelSorter ps = new ParallelSorter(1);
        List<int[]> combined = ps.combine(arrays);
        assertArrayEquals(combined.get(0), new int[] {1, 2, 7, 8, 9, 10});
        assertArrayEquals(combined.get(1), new int[] {0, 2, 8});
    }

    private Boolean isSorted(int[] a){
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1]) {
                System.out.println("i=" + i + " a[i]=" + a[i] + " a[i+1]=" + a[i + 1]);
                return false;
            }
        }
        return true;
    }
}
