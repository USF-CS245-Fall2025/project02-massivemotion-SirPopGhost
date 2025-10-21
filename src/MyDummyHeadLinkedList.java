public class MyDummyHeadLinkedList<T> implements List<T> {
    /** Sentinel node before the first real element (never holds data). */
    private final SNode<T> dummy;
    /** Number of elements. */
    private int size;

    /** Create empty list with a sentinel head. */
    public MyDummyHeadLinkedList() {
        this.dummy = new SNode<>(null); // sentinel head
        this.size = 0;
    }

    /**
     * Append to the end.
     * Walks from dummy to tail, then links new node.
     */
    @Override
    public boolean add(T data) {
        SNode<T> p = dummy;
        while (p.getNext() != null) p = p.getNext();
        p.setNext(new SNode<>(data));
        size++;
        return true;
    }

    /**
     * Insert at index.
     */
    @Override
    public void add(int index, T data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        SNode<T> prev = dummy;
        for (int i = 0; i < index; i++) prev = prev.getNext();
        SNode<T> node = new SNode<>(data);
        node.setNext(prev.getNext());
        prev.setNext(node);
        size++;
    }

    /**
     * Remove at index and return its data.
     */
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        SNode<T> prev = dummy;
        for (int i = 0; i < index; i++) prev = prev.getNext();
        SNode<T> target = prev.getNext();
        prev.setNext(target.getNext());
        size--;
        return target.getData();
    }

    /**
     * Get element at index.
     * Starts at first real node dummy.next and advances index steps.
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        SNode<T> p = dummy.getNext();
        for (int i = 0; i < index; i++) p = p.getNext();
        return p.getData();
    }

    @Override
    public int size() {
        return size;
    }
}
