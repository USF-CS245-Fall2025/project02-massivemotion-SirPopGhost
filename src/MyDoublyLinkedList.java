public class MyDoublyLinkedList<T> implements List<T>{
    /** First node (null if empty). */
    private DNode<T> head;
    /** Last node (null if empty). */
    private DNode<T> tail;
    /** Number of elements. */
    private int size;

    /** Create empty list. */
    public MyDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Append to tail.
     * Links new node after current tail; initializes head/tail when empty.
     */
    @Override
    public boolean add(T data) {
        DNode<T> n = new DNode<>(data);
        if (head == null) {
            head = n;
            tail = n;
        } else {
            n.setLast(tail);
            tail.setNext(n);
            tail = n;
        }
        size++;
        return true;
    }

    /**
     * Insert at index.
     * Handles four cases: empty, at head, at tail, middle (link prev ↔ node ↔ next).
     */
    @Override
    public void add(int index, T data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        DNode<T> node = new DNode<>(data);
        if (size == 0) {                    // empty list
            head = tail = node;
        } else if (index == 0) {           // insert at head
            node.setNext(head);
            head.setLast(node);
            head = node;
        } else if (index == size) {        // insert at tail
            node.setLast(tail);
            tail.setNext(node);
            tail = node;
        } else {                           // insert in middle
            DNode<T> next = nodeAt(index);
            DNode<T> prev = next.getLast();
            node.setLast(prev);
            node.setNext(next);
            prev.setNext(node);
            next.setLast(node);
        }
        size++;
    }   

    /**
     * Remove at index and return its data.
     * Updates head/tail as needed and unlinks target to avoid loitering.
     */
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        DNode<T> target;
        if (size == 1) {                   // removing sole node
            target = head;
            head = tail = null;
        } else if (index == 0) {           // remove head
            target = head;
            head = head.getNext();
            head.setLast(null);
            target.setNext(null);
        } else if (index == size - 1) {    // remove tail
            target = tail;
            tail = tail.getLast();
            tail.setNext(null);
            target.setLast(null);
        } else {                           // remove middle
            target = nodeAt(index);
            DNode<T> prev = target.getLast();
            DNode<T> next = target.getNext();
            prev.setNext(next);
            next.setLast(prev);
            target.setNext(null);
            target.setLast(null);
        }
        size--;
        return target.getData();
    }

    /**
     * Get element at index.
     * Walks from nearer end using nodeAt.
     */
    @Override
    public T get(int element) {
        if (element < 0 || element >= size) throw new IndexOutOfBoundsException();
        return nodeAt(element).getData();
    }

    /** Current number of elements. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Internal: node at index.
     * Uses bidirectional traversal (from head if first half; from tail otherwise).
     */
    private DNode<T> nodeAt(int index) {
        if (index <= (size >> 1)) {
            DNode<T> p = head;
            for (int i = 0; i < index; i++) p = p.getNext();
            return p;
        } else {
            DNode<T> p = tail;
            for (int i = size - 1; i > index; i--) p = p.getLast();
            return p;
        }
    }
}
