package implementations;

import interfaces.AbstractStack;

import java.util.Iterator;

public class Stack<E> implements AbstractStack<E> {

    private Node<E> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    private class Node<E> {
        private E value;
        private Node<E> next;

        public Node(E element) {
            this.value = element;
        }
    }

    @Override
    public void push(E element) {
        Node<E> toInsert = new Node<>(element);
        toInsert.next = top;
        top = toInsert;
        size++;
    }

    @Override
    public E pop() {
        Node<E> toRemove = this.top;
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        this.top = toRemove.next;
        size--;
        return (E) toRemove.value;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        return this.top.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {

        return new Iterator<E>() {
           private Node<E> current = top;
            @Override
            public boolean hasNext() {
                return this.current.next != null;
            }

            @Override
            public E next() {
                E value = current.value;
                this.current = current.next;
                return value;
            }
        };
    }
}
