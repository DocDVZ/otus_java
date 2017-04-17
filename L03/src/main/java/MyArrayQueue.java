import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by DocDVZ on 15.04.2017.
 */
public class MyArrayQueue<E> implements Queue<E> {

    private Object[] arr;
    private int first =0 ;
    private int last =0 ;
    private final static int DEAFULT_SIZE = 16;

    public MyArrayQueue() {
        arr = new Object[DEAFULT_SIZE];
    }


    public int size() {
        return last - first;
    }


    public boolean isEmpty() {
        return last == first;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
        return null;
    }

    public boolean add(E e) {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        arr = new Object[DEAFULT_SIZE];
    }

    public boolean offer(E e) {
        return false;
    }

    public E remove() {
        return null;
    }

    public E poll() {
        return null;
    }

    public E element() {
        return null;
    }

    public E peek() {
        return null;
    }
}
