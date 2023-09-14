package implementations;

import interfaces.Deque;
import java.util.Iterator;

public class ArrayDeque<E> implements Deque<E> {

    public static final int INITIAL_CAPACITY = 7;
    private Object[] elements;
    private int head;
    private int tail;
    private int size;

    public ArrayDeque() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void add(E element) {
        if (tail + 1 == capacity()) {
            Object[] newElements = grow();
            head = 0;
            tail = size - 1;
            elements = newElements;
        }

        int middle = elements.length / 2;
        if (isEmpty()) {
            this.head = this.tail = middle;
            elements[tail] = element;
        } else {
            elements[++tail] = element;
        }
        size++;
    }

    private Object[] grow() {
        Object[] tmp = new Object[capacity() * 2 + 1];
        int begin = (tmp.length - this.size) / 2;
        for (int i = begin; i < begin + this.size; i++) {
            tmp[i] = this.elements[head++];
        }
        this.head = begin;
        this.tail = begin + this.size - 1;
        return tmp;
    }

    @Override
    public void offer(E element) {
        addLast(element);
    }

    @Override
    public void addFirst(E element) {
        if (this.head == 0) {
            Object[] newElements = grow();
            head = elements.length - 1;
            tail = size - 1;
            elements = newElements;
        }

        int middle = this.elements.length / 2;
        if (isEmpty()) {
            this.head = this.tail = middle;
            elements[head] = element;
        } else {
            elements[--head] = element;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        add(element);
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public void insert(int index, E element) {
        int realIndex = getRealIndex(index);

        if (realIndex - this.head < this.tail - realIndex) {
            insertAndShiftLeft(realIndex - 1, element);
        } else {
            insertAndShiftRight(realIndex, element);
        }
    }

    private void insertAndShiftRight(int index, E element) {
        E lastElement = (E) this.elements[this.tail];
        for (int i = this.tail; i > index; i--) {
            this.elements[i] = this.elements[i - 1];
        }
        this.elements[index] = element;
        this.addLast(lastElement);
    }

    private void insertAndShiftLeft(int index, E element) {
        E firstElement = (E) this.elements[this.head];
        for (int i = this.head; i < index; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.elements[index] = element;
        this.addFirst(firstElement);
    }

    @Override
    public void set(int index, E element) {
        ensureValidIndex(index);
        this.elements[index] = element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        } else {
            return (E) this.elements[this.head];
        }
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        return removeLast();
    }

    @Override
    public E get(int index) {
        int realIndex = getRealIndex(index);
        ensureValidIndex(realIndex);
        return (E) this.elements[realIndex];
    }

    private int getRealIndex(int index) {
        int emptyMemoryLeft = this.head;
        return emptyMemoryLeft + index;
    }

    private void ensureValidIndex(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public E get(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = this.head; i <= this.tail; i++) {
            if (this.elements[i] != null && this.elements[i].equals(object)) {
                return (E) this.elements[i];
            }
        }
        return null;
    }

    @Override
    public E remove(int index) {
        if (isEmpty()) {
            return null;
        }

        int realIndex = getRealIndex(index);
        ensureValidIndex(realIndex);
        E toRemove = (E) this.elements[realIndex];
        this.elements[realIndex] = null;
        this.size--;

        if (index - this.head < this.tail - index) {
            shiftRight(index);
        } else {
            shiftLeft(index);
        }

        return toRemove;
    }

    @Override
    public E remove(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = this.head; i <= this.tail; i++) {
            if (this.elements[i] != null && this.elements[i].equals(object)) {
                E element = (E) this.elements[i];
                this.elements[i] = null;

                for (int j = i; j < this.tail; j++) {
                    this.elements[j] = this.elements[j + 1];
                }
                this.removeLast();
                return element;
            }
        }
        return null;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E removedElement = (E) this.elements[this.head];
        this.elements[head++] = null;
        size--;
        return removedElement;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        E removedElement = (E) this.elements[this.tail];
        this.elements[tail--] = null;
        this.size--;
        return removedElement;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return elements.length;
    }

    @Override
    public void trimToSize() {
        Object[] newElements = new Object[this.size];
        int index = 0;
        for (int i = this.head; i <= this.tail; i++) {
            newElements[index++] = this.elements[i];
        }
        this.elements = newElements;
        this.head = 0;
        this.tail = this.size - 1;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int currentIndex = head;

            @Override
            public boolean hasNext() {
                return currentIndex <= tail;
            }

            @Override
            public E next() {
                return (E) elements[currentIndex++];
            }
        };
    }

    private void shiftLeft(int index) {
        int begin = index;
        int end = tail;
        for (int i = begin; i < end; i++) {
            this.elements[i] = this.elements[i + 1];
        }
        this.tail = tail - 1;
    }

    private void shiftRight(int index) {
        int begin = head;
        int end = index;
        for (int i = begin; i < index; i++) {
            this.elements[i + 1] = this.elements[i];
        }
        this.head = head + 1;
    }
}
