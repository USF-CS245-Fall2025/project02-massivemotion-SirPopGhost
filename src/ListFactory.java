public final class ListFactory {
    private ListFactory() {}

    /**
     * Returns a new class depending on which user asks for the class
     * @param <T>
     * @param value
     */
    public static <T> List<T> fromProperty(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing 'list' property (expected arraylist|single|double|dummyhead)");
        }
        switch (value.trim().toLowerCase()) {
            case "arraylist":  return new MyArrayList<>();       
            case "single":     return new MySinglyLinkedList<>();   
            case "double":     return new MyDoublyLinkedList<>();     
            case "dummyhead":  return new MyDummyHeadLinkedList<>();  
            default:
                throw new IllegalArgumentException(
                    "Invalid 'list' value: " + value + " (expected arraylist|single|double|dummyhead)");
        }
    }
}
