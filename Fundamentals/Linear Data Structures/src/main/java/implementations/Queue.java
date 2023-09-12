package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

public class Queue<E> implements AbstractQueue<E> {


    private Node<E> head;
    private int size;

    public Queue() {
        this.head = null;
        this.size = 0;
    }

    private static class Node<E> {
        private E value;
        private Node<E> next;

        public Node(E value) {
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void offer(E element) {
        Node<E> toInsert = new Node<>(element);

        if (isEmpty()) {
            this.head = toInsert;
        } else {
            Node<E> current = this.head;
            while (current.next != null){
            current = current.next;
            }
            current.next = toInsert;
        }
        this.size++;
    }

    @Override
    public E poll() {
        if (isEmpty()){
            throw new IllegalStateException();
        }
        Node<E> toRemove = this.head;
        this.head = toRemove.next;
        this.size--;

        return toRemove.value;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
        return (E) this.head.value;
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
            Node <E> current = head;

            @Override
            public boolean hasNext() {

                return current != null;
            }

            @Override
            public E next() {
                E value  = current.value;
                current = current.next;
                return value;
            }
        };
    }
}
