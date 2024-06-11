package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private int size;

    private class Node {
        K key;
        V value;
        Node left, right;


        public Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    public BSTMap() {
        size = 0;
        root = null;
    }


    @Override
    public void clear() {
        root = null;
        size =0;
    }

    @Override
    public boolean containsKey(K key) {
        return getHelper(root, key) != null;
    }

    @Override
    public V get(K key) {
        Node result = getHelper( root, key);
        if ( result == null){
            return null;
        }
        return result.value;
    }
    private Node getHelper(Node x, K key){
        if ( x==null){
            return null;
        }
        int cmp = key.compareTo(x.key);
        if ( cmp < 0 ){
            return getHelper( x.left, key);
        }else if ( cmp  > 0){
            return getHelper( x.right, key);
        }else {
            return x;
        }

    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public void put(K key, V value) {
        root = put( root, key, value);
    }
    private  Node put( Node x, K key, V value){
        if ( x == null) {
            size = size + 1;
            return new Node( key, value);
        }
        int cmp = key.compareTo( x.key );
        if (cmp < 0){
            x.left = put (x.left, key, value);
        }else if( cmp > 0) {
            x.right = put(x.right, key, value);
        }else{
            x.value = value;
        }
        return x;

    }


    public void printInOrder(){
        printInOrder(root);
        System.out.println();
    }
    private void printInOrder(Node x){
        if (x == null){
            return;
        }
        printInOrder(x.left);
        System.out.println("(" + x.key.toString() + "," + x.value.toString() + ")");
        printInOrder(x.right);

    }


    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        size = size -1;
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }


}
