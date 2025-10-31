package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NULL_KEY_INDEX = 0;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
        updateThreshold();
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int bucketIndex = getIndex(key);
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getIndex(key);
        Node<K, V> current = table[bucketIndex];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return NULL_KEY_INDEX;
        }
        int hash = key.hashCode();
        return hash & (capacity - 1);
    }

    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        final Node<K, V>[] oldTable = table;
        this.capacity = newCapacity;
        this.table = newTable;
        updateThreshold();
        for (Node<K, V> oldNode : oldTable) {
            Node<K, V> current = oldNode;
            while (current != null) {
                Node<K, V> next = current.next;
                int newBucketIndex = getIndex(current.key);
                current.next = newTable[newBucketIndex];
                newTable[newBucketIndex] = current;
                current = next;
            }
        }
    }

    private void updateThreshold() {
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }
}
