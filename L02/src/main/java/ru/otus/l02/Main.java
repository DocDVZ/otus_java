package ru.otus.l02;

import java.lang.management.ManagementFactory;

/**
 * Created by DocDVZ on 13.04.2017.
 */

// VM options -Xmx512m -Xms512m
public class Main {

    public static  int size = 10 * 1024 * 1024;

    public static void main(String[] args) {
        System.out.println("Starting: " + ManagementFactory.getRuntimeMXBean().getName());
        Object[] objArr = new Object[size];
        System.out.println("\n==== Object ====\n");
        System.gc();
        measureObject(objArr);
        System.out.println("\n==== String ====\n");
        System.gc();
        measureString(objArr);
        System.out.println("\n==== Array ====\n");
        System.gc();
        measureArray(objArr);
    }

    public static long getHeapSize(){
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
    }

    public static void measureObject(Object[] arr){
        long heapInit = getHeapSize();
        System.out.println("Heap size on Object measure startup is " + heapInit + " bytes");
        for (int i = 0; i < size; i++){
            arr[i] = new Object();
        }
        measure(arr, heapInit, "java.lang.Object");
    }



    public static void measureString(Object[] arr){
        long heapInit = getHeapSize();
        for (int i = 0; i < size; i++){
//          No interning, result is 40
            arr[i] = new String(new char[]{});
//          Result is 24
//            arr[i] = new String(new char[]{});
        }
        measure(arr, heapInit, "java.lang.String");
    }

    public static void measureArray(Object[] arr){
        long heapInit = getHeapSize();
        for (int i = 0; i < size; i++){
            arr[i] = new Object[]{};
        }
        measure(arr, heapInit, "Array");
    }

    public static void measure(Object[] arr, long heapInit, String name){
        long heapAfterCreate = getHeapSize();
        System.out.println("Approx. size of " + size + " " + name + " after create measure is " + ((heapAfterCreate - heapInit)) + " bytes");
        double itemSize =  (double) (heapAfterCreate - heapInit)/size;
        System.out.println("Approx. size of "+ name +" is " + itemSize + " bytes");
        for (int i = 0; i < size; i++){
            arr[i] = null;
        }
        System.gc();
        long heapAfterGC = getHeapSize();
        System.out.println("Approx. size of " + size + " " + name + " after gc measure is " + ((heapAfterCreate - heapAfterGC)) + " bytes");
        itemSize =  (double) (heapAfterCreate - heapAfterGC)/size;
        System.out.println("Approx. size of "+ name +" is " + itemSize + " bytes");
        System.out.println("HeapSize change " + (getHeapSize() - heapInit) + " bytes");
    }


}
