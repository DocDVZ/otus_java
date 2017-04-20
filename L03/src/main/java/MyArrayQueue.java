import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by DocDVZ on 15.04.2017.
 */
public class MyArrayQueue<E> implements Queue<E> {

    private Object[] arr;
    private int first = 0;
    private int last = 0;
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
        return new Itr();
    }


    private class Itr implements Iterator<E> {
        int cursor = first;       // index of next element to return


        @Override
        public boolean hasNext() {
            return cursor != last;
        }

        @Override
        public E next() {
            E item = (E) arr[cursor++];
            return item;
        }
    }


    public Object[] toArray() {
        return Arrays.copyOfRange(arr, first, last);
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size())
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOfRange(arr, first, last, a.getClass());
        for (int i = first; i < last; i++) {
            a[i - first] = (T) arr[i];
        }
        if (a.length > size())
            a[size()] = null;
        return a;
    }

    public boolean add(E e) {
        checkCapacity();
        arr[last++] = e;

        return true;
    }

    private void checkCapacity() {
        if (last >= arr.length - 1) {
            int newSize = arr.length < (size() * 10) ? (2 * arr.length) : (arr.length);
            Object[] newArr = new Object[newSize];
            for (int i = first; i < last; i++) {
                newArr[i - first] = arr[i];
            }
            arr = newArr;
            last = size();
            first = 0;
        }
    }

    public boolean remove(Object o) {
//        TODO later
//        throw new UnsupportedOperationException("(╯°□°）╯︵ ┻━┻ ");
        if (o == null) {
            return false;
        }
        ;
        for (int i = first; i < last; i++) {
            if (o.equals(arr[i])) {
                arr[i] = null;
                for (int k = i; k < last - 1; k++) {
                    arr[k] = arr[k + 1];
                }
                arr[--last] = null;
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        //TODO later
        throw new UnsupportedOperationException("(╯°□°）╯︵ ┻━┻ ");
    }

    public boolean addAll(Collection<? extends E> c) {
        c.forEach(p -> add(p));
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        c.forEach(p -> remove(p));
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        Object[] newArr = new Object[arr.length];
        int iter = 0;
        for (Object o : arr){
            if (c.contains(o)){
                newArr[iter++] = o;
            }
        }
        arr = newArr;
        first = 0;
        last = iter;
        return true;
    }

    public void clear() {
        arr = new Object[DEAFULT_SIZE];
    }

    public boolean offer(E e) {
        if (last >= arr.length - 1) {
            return false;
        }
        return add(e);
    }

    public E remove() {
        if (!isEmpty()) {
            E item = (E) arr[first];
            arr[first] = null;
            first++;
            return item;
        } else {
            return null;
        }
    }

    public E poll() {
        return remove();
    }

    public E element() {
        return (E) arr[last];
    }

    public E peek() {
        return (E) arr[first];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (arr.length == 0) {
            return "";
        }
        for (Object o : arr) {
            if (o == null) {
                sb.append("null");
            } else {
                sb.append(((E) o).toString());
            }
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
