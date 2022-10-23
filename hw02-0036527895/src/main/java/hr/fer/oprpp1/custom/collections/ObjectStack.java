package hr.fer.oprpp1.custom.collections;


public class ObjectStack {

    //private?
    public ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

    /**
     * Returns true if collection contains no objects and false otherwise.
     *
     * @return   true if collection contains no elements.
     */
    public boolean isEmpty() {
        return arrayIndexedCollection.isEmpty();
    }

    /**
     * Returns the number of currently stored objects in this collections.
     *
     * @return   number of elements of a collection.
     */
    public int size() {
        return arrayIndexedCollection.getSize();
    }

    /**
     * Puts given object on top of stack
     * @param value to be added on stack
     */
    public void push(Object value) {
        //O(1)
        if(value == null) throw new NullPointerException("Null is not allowed to be placed on stack!");

        Object[] array = arrayIndexedCollection.getElements();
        int size = arrayIndexedCollection.getSize();

        if(size == 0) {
            array[0] = value;
            size++;
            arrayIndexedCollection.setSize(size);
            arrayIndexedCollection.setElements(array);
            return;
        } else if (size < array.length && array[size] == null) {
            array[size] = value;
            size++;
            arrayIndexedCollection.setSize(size);
            arrayIndexedCollection.setElements(array);
            return;
        }
        Object[] newElements = new Object[size*2];
        System.arraycopy(array, 0, newElements, 0, size-1);
        array = newElements;
        array[size-1] = value;
        size++;
        arrayIndexedCollection.setSize(size);
        arrayIndexedCollection.setElements(array);
        //O(n)
        /*arrayIndexedCollection.add(value);*/
    }

    /**
     * Removes last element from top of the stack
     * @return elements that was removed
     */
    public Object pop() {
        //O(1)
        if(arrayIndexedCollection.isEmpty()) throw new EmptyStackException("Your stack cannot be empty!");
        Object[] array = arrayIndexedCollection.getElements();
        int size = arrayIndexedCollection.getSize();
        Object lastObject = array[size-1];
        array[size-1] = null;
        arrayIndexedCollection.setSize(size-1);
        //O(n)
        /*
        if(arrayIndexedCollection.isEmpty()) throw new EmptyStackException("Your stack cannot be empty!");
        Object popped = arrayIndexedCollection.get(arrayIndexedCollection.getSize()-1);
        arrayIndexedCollection.remove(arrayIndexedCollection.getSize()-1);
        */
        return lastObject;
    }

    /**
     * Similar as pop, gives last element on a stack
     * @return last element placed on stack but does not delete it from stack.
     */
    public Object peek() {
        if(arrayIndexedCollection.isEmpty()) throw new EmptyStackException("Your stack cannot be empty!");
        return arrayIndexedCollection.get(arrayIndexedCollection.getSize()-1);
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {
        arrayIndexedCollection.clear();
    }
}

