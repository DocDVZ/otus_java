package ru.otus.L14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParallelSorter {

    private List<Thread> threads = new ArrayList<>();
    private final Integer thrNum;

    public ParallelSorter(Integer thrNum) {
        if (thrNum < 1) {
            throw new IllegalArgumentException();
        }
        this.thrNum = thrNum;
    }


    public int[] sort(int[] values) {
        if (values.length <= threads.size()) {
            int[] newValues = new int[values.length];
            System.arraycopy(values, 0, newValues, 0, values.length);
            new Mergesort().sort(newValues);
            return newValues;
        } else {
            List<int[]> subarrays = splitArray(values, thrNum);

            for (int p = 0; p < thrNum; p++) {
                final int k = p;
                threads.add(new Thread(
                        () -> new Mergesort().sort(subarrays.get(k))
                ));
            }
            try {
                // TODO sychronize parts join do it

                for (Thread t : threads) {
                    t.start();
                }
                for (Thread t : threads) {
                    t.join();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] comb = null;
            List<int[]> list = subarrays;
            while (list.size() > 1) {
                list = combine(list);
            }
            comb = list.get(0);
            return comb;
        }
    }

    public List<int[]> combine(List<int[]> initial) {
        List<int[]> result = new ArrayList<>();
        int size = initial.size();
        for (int i = 0; i < size; i++) {
            if ((i + 1) % 2 == 0) {
                continue;
            } else {
                if (i + 1 < size) {
                    result.add(merge(initial.get(i), initial.get(i + 1)));
                } else {
                    result.add(initial.get(i));
                }
            }
        }
        return result;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public int[] merge(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        //Index Position in first array - starting with first element
        int iFirst = 0;

        //Index Position in second array - starting with first element
        int iSecond = 0;

        //Index Position in merged array - starting with first position
        int iMerged = 0;

        //Compare elements at iFirst and iSecond,
        //and move smaller element at iMerged
        while (iFirst < first.length && iSecond < second.length) {
            if (first[iFirst] < (second[iSecond])) {
                result[iMerged] = first[iFirst];
                iFirst++;
            } else {
                result[iMerged] = second[iSecond];
                iSecond++;
            }
            iMerged++;
        }
        //copy remaining elements from both halves - each half will have already sorted elements
        System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
        System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
        return result;
    }

    private int[] combine(int[] array1, int[] array2) {
        int[] comb = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, comb, 0, array1.length);
        System.arraycopy(array2, 0, comb, array1.length, array2.length);
        return comb;
    }

    private List<int[]> splitArray(int[] array, int L) {
        List<int[]> parts = new ArrayList<int[]>();
        final int N = array.length;
        for (int i = 0; i < N; i += L) {
            int length = N - i < L ? N - i : L;
            int[] part = new int[length];
            System.arraycopy(array, i, part, 0, Math.min(N - i, L));
            parts.add(part);
        }
        return parts;

    }


    private class Mergesort {
        private int[] numbers;
        private int[] helper;

        private int number;

        public void sort(int[] values) {
            this.numbers = values;
            number = values.length;
            this.helper = new int[number];
            mergesort(0, number - 1);
        }

        private void mergesort(int low, int high) {
            // check if low is smaller than high, if not then the array is sorted
            if (low < high) {
                // Get the index of the element which is in the middle
                int middle = low + (high - low) / 2;
                // Sort the left side of the array
                mergesort(low, middle);
                // Sort the right side of the array
                mergesort(middle + 1, high);
                // Combine them both
                merge(low, middle, high);
            }
        }

        private void merge(int low, int middle, int high) {

            // Copy both parts into the helper array
            for (int i = low; i <= high; i++) {
                helper[i] = numbers[i];
            }

            int i = low;
            int j = middle + 1;
            int k = low;
            // Copy the smallest values from either the left or the right side back
            // to the original array
            while (i <= middle && j <= high) {
                if (helper[i] <= helper[j]) {
                    numbers[k] = helper[i];
                    i++;
                } else {
                    numbers[k] = helper[j];
                    j++;
                }
                k++;
            }
            // Copy the rest of the left side of the array into the target array
            while (i <= middle) {
                numbers[k] = helper[i];
                k++;
                i++;
            }

        }
    }
}
