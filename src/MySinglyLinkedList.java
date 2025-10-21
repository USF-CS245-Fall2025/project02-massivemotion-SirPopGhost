public class MySinglyLinkedList<T> implements List<T> {
    /** First node in the list, or null when empty. */
    private SNode<T> head;
    /** Logical number of elements currently stored. */
    private int size;

    /** Constructs an empty list. */
    public MySinglyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Appends data at the end of the list.
     * Traverses from head to the last node to link the new node.
     */
    @Override
    public boolean add(T data) {    
        SNode<T> n = new SNode<>(data);
        if (this.head == null) {
            this.head = n;
        } else {
            SNode<T> p = this.head;
            while (p.getNext() != null) {
                p = p.getNext();
            }
            p.setNext(n);
        }
        size++;
        return true;
    }

    /**
     * Inserts data at the given index.
     * Shifts links by relinking predecessor to the new node and the new node to successor.
     */
    @Override
    public void add(int element, T data) {
        if (element < 0 || element > this.size) throw new IndexOutOfBoundsException();
        SNode<T> node = new SNode<>(data);
        if (element == 0) {
            node.setNext(this.head);
            this.head = node;
        } else {
            SNode<T> prev = this.head;
            for (int i = 0; i < element - 1; i++) {
                prev = prev.getNext();
            }
            node.setNext(prev.getNext());
            prev.setNext(node);
        }
        this.size++;
    }

    /**
     * Removes and returns the element at the given index.
     * Relinks predecessor to skip the removed node.
     */
    @Override
    public T remove(int element) {
        if (element < 0 || element >= size) throw new IndexOutOfBoundsException();
        SNode<T> n;
        if (element == 0) {
            n = head;
            head = head.getNext();
        } else {
            SNode<T> p = head;
            for (int i = 0; i < element - 1; i++) {
                p = p.getNext();
            }
            n = p.getNext();
            p.setNext(n.getNext());
        }
        size--;
        return n.getData();
    }   

    /**
     * Returns the element at the given index.
     * Traverses from head to the target position.
     */
    @Override
    public T get(int element) {
        if (element < 0 || element >= size) throw new IndexOutOfBoundsException();
        SNode<T> p = head;
        for (int i = 0; i < element; i++) {
            p = p.getNext();
        }
        return p.getData();
    }

    /** Returns the number of elements in the list. */
    @Override
    public int size() {
        return size;
    }
}
