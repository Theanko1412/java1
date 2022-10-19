package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class ArrayIndexedCollection implements List{

    private static class ElementsGetter implements hr.fer.oprpp1.custom.collections.ElementsGetter {

        private long savedModificationCount;

        public ElementsGetter(long savedModificationCount) {
            this.savedModificationCount = savedModificationCount;
        }


        private int currentElementIndex = -1;
        ArrayIndexedCollection arrayIndexedCollection;

        @Override
        public boolean hasNextElement() {
            if(arrayIndexedCollection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("Cannot read from modified collection!");
            return arrayIndexedCollection.size != 0 && arrayIndexedCollection.elements[currentElementIndex+1] != null;
        }

        @Override
        public Object getNextElement() {
            if(arrayIndexedCollection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("Cannot read from modified collection!");
            if(hasNextElement()) {
                ++currentElementIndex;
                return arrayIndexedCollection.elements[currentElementIndex];
            } else throw new IndexOutOfBoundsException("No next element found!");
        }
    }

    private long modificationCount = 0;


    private int size;
    private Object[] elements;

    public int getSize() {
        return size;
    }

    public Object[] getElements() {
        return elements;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setElements(Object[] elements) {
        this.elements = elements;
    }


    public ArrayIndexedCollection() {
        this(16);
    }

    public ArrayIndexedCollection(int initialCapacity) {
        if(initialCapacity < 1) throw new IllegalArgumentException("Initial capacity cannot be less than 1!");
        this.size = 0;
        this.elements = new Object[initialCapacity];
    }

    public ArrayIndexedCollection(Collection collection) {
        this(collection, 16);
    }

    public ArrayIndexedCollection(Collection collection, int initialCapacity) {
        this(initialCapacity);
        if(collection == null) throw new NullPointerException("Given collection is null!");
        int collectionSize = collection.size();
        if(initialCapacity < collectionSize) {
            this.elements = new Object[collectionSize];
        }
        this.addAll(collection);
        this.size = collectionSize;
    }


    /**
     * Gets object at the given index.
     * @param index at which object is taken
     * @throws IndexOutOfBoundsException if index is less than 0 and bigger than collection size-1
     * @return given object
     */
    public Object get(int index) {
        if(index > size-1 || index < 0) throw new IndexOutOfBoundsException("Index value is expected " +
                "between 0 and " + (size-1));
        for (Object object : elements) {
            if (index == 0) return object;
            index--;
        }
        //unreachable
        throw new IndexOutOfBoundsException("Index value not as expected!");
    }

    /**
     *
     * @param value to be look for in collection
     * @return index of given element
     */
    public int indexOf(Object value) {
        if(value == null) return -1;
        int index = 0;
        for(Object object : elements) {
            if(object == null) break;
            if(value.equals(object)) return index;
            ++index;
        }
        return -1;
    }

    /**
     * Inserts given object into collection at a given position
     * @param value object to be inserted
     * @param position in collection
     * @throws IndexOutOfBoundsException if position is less than 0 or bigger than collection size
     */
    public void insert(Object value, int position) {
        if(position < 0 || position > size) throw new IndexOutOfBoundsException("Index must be between 0 and " + size);
        if(value == null) throw new NullPointerException("Cannot add object 'null' to collection!");
        if(elements[elements.length-1] != null || position == size) {
            Object[] newElements = new Object[elements.length*2];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        /*for(int i = size()-1; i > position; i--) {
            elements[i] = elements[i-1];
        }*/
        if (size() - 1 - position >= 0) System.arraycopy(elements, position, elements, position + 1, size() - position);
        elements[position] = value;
        this.size++;
        modificationCount++;
    }

    /**
     * Removes element at the given index, others are squashed, no null left behind
     * @param index to be removed
     * @throws IndexOutOfBoundsException if index is less than 0 or bigger than collection size - 1
     */
    public void remove(int index) {
        if(index < 0 || index > size-1) throw new IndexOutOfBoundsException("Index must be between 0 and " + (size-1));
        /*for(int i = index; i < size()-1; i++) {
            elements[i] = elements[i+1];
        }*/
        if (size - 1 - index >= 0) {
            System.arraycopy(elements, index + 1, elements, index, size - 1 - index);
            elements[size-1] = null;
            size--;
            modificationCount++;
        }
    }

    /**
     * Returns true if collection contains no objects and false otherwise.
     *
     * @return   true if collection contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
        /*for(Object object : elements) {
            if(object != null) return false;
        }
        return true;*/
    }

    /**
     * Returns the number of currently stored objects in this collections.
     *
     * @return   number of elements of a collection.
     */
    @Override
    public int size() {
        int size = 0;
        for(Object object : elements) {
            if(object == null) return size;
            size++;
        }
        this.size = size;
        return size;
    }

    /**
     * Adds the given object into this collection.
     * @param value object to be added to collection.
     */
    @Override
    public void add(Object value) {
        if(value == null) throw new NullPointerException("Cannot add object 'null' to collection!");
        size++;
        modificationCount++;
        int i;
        for(i = 0; i < elements.length; i++) {
            if(elements[i] == null) {
                elements[i] = value;
                return;
            }
        }
        Object[] newElements = new Object[elements.length*2];
        System.arraycopy(elements, 0, newElements, 0, size-1);
        elements = newElements;
        elements[size-1] = value;
    }

    /**
     * Returns true only if the collection contains given value, as determined by equals method.
     * It is OK to ask if collection contains null.
     * @param value Object to check.
     * @return true if given object is found.
     */
    @Override
    public boolean contains(Object value) {
        for(Object object : elements) {
            if(value.equals(object)) return true;
            if(object == null) return false;
        }
        return false;
    }

    /**
     * Returns true only if the collection contains given value as determined by equals method and removes
     * one occurrence of it (in this class it is not specified which one).
     * @param value Object to be removed.
     * @return true if object is removed.
     */
    @Override
    public boolean remove(Object value) {
        for(int i = 0; i < size; i++) {
            if(elements[i] == null) break;
            if(value.equals(elements[i])) {
                /*for(int j = i; j < size()-1; j++) {
                    elements[j] = elements[j+1];
                }*/
                if (size - 1 - i >= 0) {
                    System.arraycopy(elements, i + 1, elements, i, size - 1 - i);
                    elements[size-1] = null;
                    this.size--;
                    modificationCount++;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns null.
     * @return array of objects.
     * @throws UnsupportedOperationException ? not sure when
     */
    @Override
    public Object[] toArray() {
        if (size == 0) throw new UnsupportedOperationException("Collection cannot be null!");
        Object[] array = new Object[size];
        int i = 0;
        for(Object object : elements) {
            if(object == null) break;
            array[i] = object;
            i++;
        }
        return array;
    }

    /**
     * Removes all elements from this collection.
     */
    @Override
    public void clear() {
        /*for(int i = 0; i < size; i++) {
            if(elements[i] == null) break;
            elements[i] = null;
        }*/
        Arrays.fill(elements, null);
        size = 0;
        modificationCount++;
    }


    @Override
    public ElementsGetter createElementsGetter() {
        ArrayIndexedCollection arrayIndexedCollection = ArrayIndexedCollection.this;
        ArrayIndexedCollection.ElementsGetter elementsGetter = new ArrayIndexedCollection.ElementsGetter(arrayIndexedCollection.modificationCount);
        elementsGetter.arrayIndexedCollection = arrayIndexedCollection;
        return elementsGetter;
    }
}
