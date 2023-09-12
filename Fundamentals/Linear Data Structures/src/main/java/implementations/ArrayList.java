package implementations;

import interfaces.List;

import java.util.Arrays;
import java.util.Iterator;


public class ArrayList<E> implements List<E> {

    public static final int INITIAL_CAPACITY = 4;
    private Object[] elements;
    private int size;


    public ArrayList() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public boolean add(E element) {
        if (!enoughCapacity()) {
            this.elements = increaseArrayCapacity();
        }
        this.elements[size++] = element;
        return true;
    }


    @Override
    public boolean add(int index, E element) {

        if (!isValidIndex(index)) {
            return false;
        }

        if (!enoughCapacity()) {
            this.elements = increaseArrayCapacity();
        }

        moveRightElementsByOneIndex(index);
        elements[index] = element;
        size++;
        return true;
    }


    @Override
    public E get(int index) {
        if (!isValidIndex(index)){
            throw new IndexOutOfBoundsException();
        }
        return (E) this.elements[index];
    }



    @Override
    public E set(int index, E element) {
        if (!isValidIndex(index)){
            throw new IndexOutOfBoundsException();
        }
       E previous = (E) this.elements[index];
        this.elements[index] = element;
        return previous;
    }

    @Override
    public E remove(int index) {
        if (!isValidIndex(index)|| isEmpty()){
            throw new IndexOutOfBoundsException();
        }


        E previous = (E) this.elements[index];
        moveLeftElementsByOneIndex(index);

        return previous;
    }

    private void moveLeftElementsByOneIndex(int index) {
        for (int i = index; i < size  ; i++) {
            this.elements[i] = this.elements[i+1];
        }
        size--;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int indexOf(E element) {
        return Arrays.asList(this.elements).indexOf(element);
    }

    @Override
    public boolean contains(E element) {
        boolean isTure = false;

       if ( isEmpty()){
           return  false;
       }

        for (int i = 0; i <= size ; i++) {
            if (this.elements[i].equals(element)){
                isTure = true;
                break;
            }
        }
        return isTure;
    }

    @Override
    public boolean isEmpty() {
        return this.size==0;
    }

    @Override
    public Iterator<E> iterator() {

        return new Iterator<E>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index<size();
            }

            @Override
            public E next() {
                return get(index++);
            }
        };
    }

    private boolean enoughCapacity() {
        return this.size < this.elements.length - 1;
    }

    private Object[] increaseArrayCapacity() {
        return Arrays.copyOf(this.elements,
                elements.length * 2);
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }

    private void moveRightElementsByOneIndex(int index) {
        for (int i = size; i >= index; i--) {
            elements[i + 1] = elements[i];
        }
    }
}


