/**
 * Holds an interface to require all lists to have the methods below
 */
public interface List<T> {

    public void add (int index, T element) throws IndexOutOfBoundsException;
    public boolean add (T element);
    public T get (int index) throws IndexOutOfBoundsException;
    public T remove (int index) throws IndexOutOfBoundsException;
    public int size ();
}
