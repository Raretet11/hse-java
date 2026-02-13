package hse.java.lectures.lecture3.practice.randomSet;

import java.util.Random;

public class RandomSet<T> {
    private static class Node<T> {
        T key;
        int index;

        Node(T key, int index) {
            this.key = key;
            this.index = index;
        }
    }

    private final Random random = new Random();

    private static final int TABLE_SIZE = 500_000;

    private final Node<T>[] table;
    private Object[] values;
    private int size;

    public RandomSet() {
        table = new Node[TABLE_SIZE];
        values = new Object[4];
        size = 0;
    }

    private int hash(T key) {
        return key.hashCode() % TABLE_SIZE;
    }

    private int index(T key) {
        int index = hash(key);
        for (int t = 0; t < TABLE_SIZE; t++) {
            Node<T> entry = table[index];
            if (entry == null) {
                return -1;
            }
            if (entry.key.equals(key)) {
                return index;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        return -1;
    }

    private void updateElements() {
        if (size < values.length) {
            return;
        }
        Object[] newElements = new Object[values.length * 2];
        System.arraycopy(values, 0, newElements, 0, values.length);
        values = newElements;
    }

    public boolean insert(T value) {
        if (size == TABLE_SIZE) {
            return false;
        }
        if (index(value) != -1) {
            return false;
        }

        int i = hash(value);
        while (table[i] != null) {
            i = (i + 1) % TABLE_SIZE;
        }

        updateElements();
        int newIndex = size;
        values[size++] = value;

        table[i] = new Node<>(value, newIndex);
        return true;
    }

    public boolean remove(T value) {
        int pos = index(value);
        if (pos == -1) {
            return false;
        }

        int oldIndex = table[pos].index;
        table[pos] = null;

        if (oldIndex != size - 1) {
            T last = (T) values[size - 1];
            values[oldIndex] = last;
            int lastPos = index(last);
            table[lastPos].index = oldIndex;
        }

        size--;
        values[size] = null;
        return true;
    }

    public boolean contains(T value) {
        return index(value) != -1;
    }

    public T getRandom() {
        if (size == 0) {
            throw new EmptySetException("set is empty");
        }
        int index = random.nextInt(size);
        return (T) values[index];
    }
}
