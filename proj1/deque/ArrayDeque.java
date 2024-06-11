package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{
    private T[] items;
    private int size;
    private int first;
    private int last;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        first = 0;
        last = 1;
    }

    public void addFirst(T x){
        if (size == items.length){
            resize( items.length * 2);
        }
        size += 1;
        items[first] = x;
        first = (first -1 + items.length)% items.length;

    }

    public void addLast(T x) {
        if (size == items.length) {
            resize(items.length * 2);
        }
        items[last] = x;
        last = (last + 1) % items.length;
        size += 1;
    }

    public T removeFirst() {
        if ((double) size / items.length < 0.25 && items.length > 16) {
            resize(items.length / 2);
        }
        first = (first + 1) % items.length;
        T x = items[first];
        items[first] = null;
        size -= 1;
        return x;
    }

    public T removeLast() {
        if ((double) size / items.length < 0.25 && items.length > 16) {
            resize(items.length / 2);
        }
        last = (last - 1 + items.length) % items.length;
        T x = items[last];
        items[last] = null;
        size -= 1;
        return x;
    }

    public T get(int x) {
        int point= first;
        for (int i = 0; i <= x; i += 1){
            point = (point +1) % items.length;
        }
        return items[point];
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i += 1) {
            first = (first + 1) % items.length;
            a[i] = items[first];
        }
        items = a;
        first = items.length - 1;
        last = size;
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }
    private class ArrayIterator implements  Iterator<T>{
        private int current;
        private int count;

        public boolean hasNext() {
            return count < size;
        }

        public T next() {
            T index = get(current);
            current += 1;
            count += 1;
            return index;
        }

    }

    public void printDeque() {
        int point = (first + 1) % items.length;
        for (int i = 0; i < size; i += 1) {
            System.out.print(items[point] + " ");
            point = (point + 1) % items.length;
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) other;
        if (o.size() != size()) {
            return false;
        }
        for( int i = 0; i < size(); i += 1){
            if ( ! o.get(i).equals(get(i))){
                return false;
            }
        }
        return true;
    }






}
