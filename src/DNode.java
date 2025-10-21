	/**
	 * This class is to set double node pointers for the doubly linkedlist
	 * The doubly linkedlist has two pointers 
	 */
public class DNode<T>{
    private T data;
    private DNode<T> next;
    private DNode<T> last;

    public DNode(T data){
        this.data = data;
        this.next = null;
    }
    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public DNode<T> getNext() {
		return next;
	}

	public void setNext(DNode<T> next) {
		this.next = next;
	}

    public DNode<T> getLast() {
		return last;
	}

	public void setLast(DNode<T> last) {
		this.last = last;
	}
}
