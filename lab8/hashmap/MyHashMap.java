package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    private static final int INITIAL_SIZE = 16;
    private static final double LOAD_FACTOR =0.75;
    private int size;
    private  double loadFactor;
    private Set<K> keys;

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(INITIAL_SIZE, LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this( initialSize, LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        loadFactor = maxLoad;
        keys = new HashSet<>();

    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        buckets = createTable( INITIAL_SIZE);
        size = 0;
        keys.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    private int getBucket( K key, int length){
        return Math.floorMod(key.hashCode(),  length);
    }


    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node == null){
            return null;
        }else{
            return node.value;
        }
    }

    private Node getNode(K key){
        int bucket = getBucket(key, buckets.length);
        Collection<Node> nodes = buckets[bucket];

        if (nodes == null){
            return null;
        }
        for (Node node: nodes){
            if (node.key.equals(key)){
                return node;
            }
        }
        return null;
    }



    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Node node= getNode(key);
        if (node != null){
            node.value= value;
            return;
        }
        if ((double)size/ buckets.length > loadFactor){
            resize( buckets.length * 2);
        }
        int bucket = getBucket(key, buckets.length);
        size += 1;
        keys.add(key);
        if( buckets[bucket] == null){
            buckets[bucket] = createBucket();
        }
        buckets[bucket].add(createNode(key, value));
    }


    private void resize( int newSize ){
        Collection<Node>[] newBuckets = createTable( newSize);
        for (K key : keys){
            int bucket = getBucket( key, newBuckets.length);
            if (newBuckets[bucket] == null){
                newBuckets[bucket] = createBucket();
            }
            newBuckets[bucket].add(getNode(key));
        }
        buckets = newBuckets;
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

}
