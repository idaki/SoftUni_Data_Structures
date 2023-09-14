package interfaces;

public interface ReversedListInterface<E> extends Iterable<E>{

    void add(E element);
    int size();
    int capacity();
    Object get(int index);
    Object removeAt(int index);







}
