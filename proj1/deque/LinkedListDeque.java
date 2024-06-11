package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node sentinel;
    private int size;

    public class Node {
        public T item;
        public Node prev;
        public Node next;

        public Node(T x, Node p, Node n) {
            item = x;
            prev = p;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null,null);
        size = 0;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }


    public T getRecursive(int index){
        return recusiveHelp( sentinel.next ,index);
    }

    public T recusiveHelp(Node x, int index){
        if ( index == 0){
            return x.item;
        }
        return recusiveHelp(x.next, index-1);
    }

    public void addFirst(T x) {
        sentinel.next = new Node( x, sentinel,  sentinel.next);
        sentinel.next.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T x) {
        sentinel.prev = new Node ( x, sentinel.prev, sentinel);
        sentinel.prev.next = sentinel.prev;
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T removefirst = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return removefirst;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T removelast = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return removelast;
    }

    public T get(int index) {
        int count = 0;
        Node ptr = sentinel;
        while (ptr.next != sentinel) {
            ptr = ptr.next;
            if (count == index) {
                return ptr.item;
            }
            count++;
        }
        return null;
    }

    public int size() {
        return size;
    }


    public void printDeque() {
        Node n = sentinel.next;
        while (n != sentinel) {
            System.out.print(n.item + " ");
            n = n.next;
        }
    }

    public Iterator<T> iterator() {
        return new listIterator();
    }
    private class listIterator implements  Iterator<T>{
        private Node current = sentinel.next;
        private int count;

        public boolean hasNext() {
            return count < size;
        }

        public T next() {
            T item = current.item;
            current= current.next;
            count += 1;
            return item;
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
