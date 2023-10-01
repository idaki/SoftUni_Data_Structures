package implementations;

import interfaces.AbstractBinarySearchTree;

public class BinarySearchTree<E extends Comparable<E>> implements AbstractBinarySearchTree<E> {
    private Node<E> root;
    private Node<E> leftChild;
    private Node<E> rightChild;

    public BinarySearchTree() {
    }

    public BinarySearchTree(Node<E> root) {
        this.copy(root);
    }

    private void copy(Node<E> node) {
        if (node!= null){
        this.insert(node.value);
        this.copy(node.leftChild);
        this.copy(node.rightChild);

        }
    }

    @Override
    public void insert(E element) {
        Node<E> newNode = new Node<>(element);
        if (this.root == null) {
            this.root = newNode;
        } else {
            Node<E> current = this.root;
            Node<E> parent;

            while (true) {
                parent = current;
                if (isLess(element, current.value)) {
                    current = current.leftChild;
                    if (current == null) {
                        parent.leftChild = newNode;
                        return;
                    }
                } else if (isGreater(element, current.value)) {
                    current = current.rightChild;
                    if (current == null) {
                        parent.rightChild = newNode;
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    private boolean isLess(E first, E second) {
        return first.compareTo(second) < 0;
    }

    private boolean isGreater(E first, E second) {
        return first.compareTo(second) > 0;
    }

    @Override
    public boolean contains(E element) {
        Node<E> current = this.root;

        while (current != null) {
            if (isLess(element, current.value)) {
                current = current.leftChild;
            } else if (isGreater(element, current.value)) {
                current = current.rightChild;
            } else {
                return true;
            }
        }


        return false;
    }

    @Override
    public AbstractBinarySearchTree<E> search(E element) {
        AbstractBinarySearchTree<E> result = new BinarySearchTree<>();
        Node<E> current = this.root;

        while (current != null) {
            if (isLess(element, current.value)) {
                current = current.leftChild;
            } else if (isGreater(element, current.value)) {
                current = current.rightChild;
            } else {
                return new BinarySearchTree<>(current);
            }
        }

        return result;
    }

    @Override
    public Node<E> getRoot() {
        return this.root;
    }

    @Override
    public Node<E> getLeft() {
        return this.leftChild;
    }

    @Override
    public Node<E> getRight() {
        return this.rightChild;
    }

    @Override
    public E getValue() {
        return this.root.value;
    }
}
