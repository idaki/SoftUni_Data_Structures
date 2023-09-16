package implementations;

import interfaces.ReversedListInterface;

import java.util.Arrays;
import java.util.Iterator;

public class ReversedList<E> implements ReversedListInterface<E> {

    public static final int INITIAL_CAPACITY = 2;
    public static final int LAST_ELEMENT_POSITION = 2;
    private Object[] elements;
    private int size;
    private int head;

    public ReversedList() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.head = LAST_ELEMENT_POSITION;
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
    public Object get(int index) {
        ensureIndex(index);
        return this.elements[size - index - 1];
    }



    @Override
    public Object removeAt(int index) {
        ensureIndex(index);

        int indexToRemove = size - index - 1;
        E element = (E) this.elements[index];
        this.elements[indexToRemove] = null;

        for (int i = indexToRemove; i < this.size - 1; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.size--;
        return element;
    }



    private Object[] grow() {
        Object[] tmp = new Object[capacity() * 2];
        int begin = capacity();
        int currentIndex = 0;
        for (int i = begin; i < tmp.length; i++) {
            tmp[i] = this.elements[currentIndex++];
        }
        this.head = capacity();

        return tmp;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int currentIndex = head;
            @Override
            public boolean hasNext() {
                return currentIndex<capacity();
            }

            @Override
            public E next() {
                return (E) elements[currentIndex++];
            }
        };
    }


    private void ensureIndex(int index) {
        if (index> this.head && index>=capacity()) {
            throw new IndexOutOfBoundsException("Index out of bound for index: "
                    + (index));
        }
    }
}
