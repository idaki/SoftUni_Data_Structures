package implementations;


import interfaces.ReversedListInterface;

import java.util.Arrays;
import java.util.Iterator;


public class ReversedList<E> implements ReversedListInterface<E> {

    private int size;
    private Object[] elements;

    public ReversedList() {
        this.size = 0;
        this.elements = new Object[2];
    }

    @Override
    public void add(E element) {
        if (this.size == this.elements.length) {
            this.elements = Arrays.copyOf(this.elements, this.size * 2);
            this.elements[size++] = element;
            return;
        }
        this.elements[size++] = element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.elements.length;
    }

    @Override
    public E get(int index) {
        ensureIndex(index);
        return getAt(size - index - 1);
    }

    @Override
    public E removeAt(int index) {
        ensureIndex(index);

        int indexToRemove = size - index - 1;
        E element = getAt(indexToRemove);
        this.elements[indexToRemove] = null;

        for (int i = indexToRemove; i < this.size - 1; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.size--;
        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                return getAt(index--);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void ensureIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index out of bound for index: "
                    + (index));
        }
    }
}
