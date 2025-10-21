	/**
	 * This class is to set a single node for the singly and dummyhead linkedlist
	 * Both of the lists only have one pointer
	 */
public class SNode<T>{
    private T data;
    private SNode<T> next;

    public SNode(T data){
        this.data = data;
        this.next = null;
    }
    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public SNode<T> getNext() {
		return next;
	}

	public void setNext(SNode<T> next) {
		this.next = next;
	}
}
