package implementations;

import interfaces.AbstractQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityQueue<E extends Comparable<E>> implements AbstractQueue<E> {
    private List<E> elements;

    public PriorityQueue() {
        this.elements = new ArrayList<>();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public void add(E element) {
        this.elements.add(element);
        this.heapifyUp(this.size() - 1);
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = getParentIndex(index);
            if (isLess(parentIndex, index)) {
                Collections.swap(this.elements, index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private boolean isLess(int firstIndex, int secondIndex) {
        return getAt(firstIndex).compareTo(getAt(secondIndex)) < 0;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private E getAt(int index) {
        return this.elements.get(index);
    }

    @Override
    public E peek() {
        if (this.size() == 0) {
            throw new IllegalStateException("Illegal empty heap");
        }
        return getAt(0);
    }

    @Override
    public E poll() {
        if (this.size() == 0) {
            throw new IllegalStateException("Illegal empty peek/poll");
        }
        Collections.swap(this.elements, 0, this.size() - 1);
        this.elements.remove(this.size() - 1);
        this.heapifyDown(0);
        return getAt(0);
    }

    private E getLeftChild(int index) {
        return this.elements.get(this.getLeftChildIndex(index));
    }

    private E getRightChild(int index) {
        return this.elements.get(this.getRightChildIndex(index));
    }

    private int getLeftChildIndex(int index) {
        return 2 * (index + 1);
    }

    private int getRightChildIndex(int index) {
        return 2 * (index + 2);
    }

    public void heapifyDown(int index) {
        while (getLeftChildIndex(index) < this.size()
                && isLess(index, getLeftChildIndex(index))) {
            int child = getLeftChildIndex(index);
            int rightChildIndex = getRightChildIndex(index);
            if (rightChildIndex < this.size() && isLess(index, rightChildIndex)) {
                index = rightChildIndex;
            }else{
                break;
            }
            Collections.swap(this.elements, child, index);
        }
    }
}
