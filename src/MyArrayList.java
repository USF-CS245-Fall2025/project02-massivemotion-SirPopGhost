public class MyArrayList<T> implements List<T> {
    private Object[] array;
    /** Logical number of elements (next insertion index). */
    private int size;

    /**
     * Constructs a list with an initial capacity of 10.
     */
    public MyArrayList() {
        this.array = new Object[10];
        this.size = 0;
    }
    
    /**
     * Appends the element to the end of the list, growing the backing array if needed.
     */
    @Override
    public boolean add(T addMe) {
        if (size >= array.length) {
            Object[] newArray = new Object[array.length * 2];
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }
            array = newArray;
        }

        array[size] = addMe;
        size++;
        return true;
    }

    /**
     * Removes and returns the element at index i, shifting subsequent elements left.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T remove(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("remove: index " + i + " out of bounds (size " + size + ")");
        }
        T old = (T) array[i];
        for (int j = i; j < size - 1; j++) {
            array[j] = array[j + 1];
        }
        array[size - 1] = null; // avoid loitering
        size--;
        return old;
    }

    /**
     * Inserts element at position i, shifting existing elements right.
    */
    @Override
    public void add(int i, T element)  {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException("add: index " + i + " out of bounds for insert (size " + size + ")");
        }
        if (size >= array.length) {
            Object[] newArray = new Object[array.length * 2];
            for (int k = 0; k < array.length; k++) {
                newArray[k] = array[k];
            }
            array = newArray;
        }
        for (int j = size; j > i; j--) {
            array[j] = array[j - 1];
        }
        array[i] = element;
        size++;
    }

    /**
     * Returns the element at index i.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("get: index " + i + " out of bounds (size " + size + ")");
        }
        return (T) array[i];
    }

    /**
     * Returns the number of elements currently stored.
     */
    @Override
    public int size() {
        return size;
    }

}
