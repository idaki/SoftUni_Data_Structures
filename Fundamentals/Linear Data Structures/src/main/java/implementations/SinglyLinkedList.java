package implementations;

import interfaces.LinkedList;

import java.util.Iterator;

public class SinglyLinkedList<E> implements LinkedList<E> {

    private Node<E> head;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    private static class Node<E> {
        private E value;
        private Node<E> previous;
        private Node<E> next;

        public Node(E value) {
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void addFirst(E element) {
        Node<E> toInsert = new Node<>(element);
        if (isEmpty()) {
            this.head = toInsert;
        } else {
            this.head.next = toInsert;
            this.head = toInsert;
        }
        size++;

    }

    @Override
    public void addLast(E element) {
        Node<E> toInsert = new Node<>(element);
        if (isEmpty()) {
            addFirst(element);
        } else {
            Node<E> current = this.head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = toInsert;

            size++;


        }
    }

    @Override
    public E removeFirst() {

        if (isEmpty()) {
            throw new IllegalStateException();
        }
        Node<E> toRemove = this.head;
        this.head = toRemove.next;
        size--;
        return toRemove.value;
    }

    @Override
    public E removeLast() {
        if (size <= 1) {
            return removeFirst();
        }

        Node<E> preLast = head;
        while (preLast.next.next != null) {
            preLast = preLast.next;
        }

        E toRemovedValue = preLast.next.value;
        preLast.next = null;
        size--;

        return toRemovedValue;
    }


    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        return this.head.value;
    }

    @Override
    public E getLast() {
        Node<E> current = this.head;
        while (current.next != null) {
            current = current.next;
        }

        return current.value;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.next;
                return value;
            }
        };
    }


}
