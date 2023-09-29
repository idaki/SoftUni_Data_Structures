package implementations;

import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxHeap<E extends Comparable<E>> implements Heap<E>{
        private List<E> elements;

    public MaxHeap() {
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

        private boolean isLess(int childIndex, int parentIndex) {
            return getAt(childIndex).compareTo(getAt(parentIndex)) < 0;
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
}