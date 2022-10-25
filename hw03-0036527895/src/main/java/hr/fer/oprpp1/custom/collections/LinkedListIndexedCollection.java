package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;

public class LinkedListIndexedCollection implements List{

    private static class ElementsGetter implements hr.fer.oprpp1.custom.collections.ElementsGetter {

        private long savedModificationCount;

        public ElementsGetter(long savedModificationCount) {
            this.savedModificationCount = savedModificationCount;
        }


        LinkedListIndexedCollection linkedListIndexedCollection;
        ListNode currentNode = null;

        @Override
        public boolean hasNextElement() {
            if(linkedListIndexedCollection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("Cannot read from modified collection!");

            if(linkedListIndexedCollection.isEmpty()) return false;

            if(currentNode == null && linkedListIndexedCollection.first != null) return true;

            return currentNode.next != null;
        }

        @Override
        public Object getNextElement() {
            if(linkedListIndexedCollection.modificationCount != savedModificationCount)
                throw new ConcurrentModificationException("Cannot read from modified collection!");

            if(hasNextElement()) {
                if(currentNode == null) {
                    currentNode = linkedListIndexedCollection.first;
                } else currentNode = currentNode.next;
                //.value
                return currentNode.value;
            } else throw new IndexOutOfBoundsException("No next element found!");
        }
    }

    private long modificationCount = 0;


    private int size;
    private ListNode first;
    private ListNode last;

    private static class ListNode {
        private ListNode previous;
        private ListNode next;
        private Object value;
    }

    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    public int getSize() {
        return size;
    }

    public ListNode getFirst() {
        return first;
    }

    public ListNode getLast() {
        return last;
    }

    public LinkedListIndexedCollection(Collection collection) {
        if(collection == null) throw new NullPointerException("Given collection is null!");
        this.addAll(collection);
    }

    /**
     * Gets object at the given index.
     * @param index at which object is taken
     * @throws IndexOutOfBoundsException if index is less than 0 and bigger than collection size-1
     * @return given object
     */
    public Object get(int index) {
        if(index > size-1 || index < 0) throw new IndexOutOfBoundsException("Index value is expected between 0 and " + (size-1));
        ListNode current = this.first;
        int currentNo = 0;
        while(true) {
            if(currentNo == index) {
                return current.value;
            }
            if(currentNo+1 == index) {
                return current.next.value;
            }
            currentNo+=2;
            if(current.next.next != null) {
                current = current.next.next;
            } else {
                current = current.next;
            }
        }
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
        ListNode current = first;
        int i = 0;
        do {
            if(i == position) {
                ListNode inserted = new ListNode();
                inserted.value = value;
                current.previous.next = inserted;
                inserted.previous = current.previous;
                inserted.next = current;
                current.previous = inserted;
                size++;
                modificationCount++;
            }
            i++;
            current = current.next;
        } while(current != null);
    }

    /**
     *
     * @param value to be look for in collection
     * @return index of given element
     */
    public int indexOf(Object value) {
        if(value == null) return -1;
        int index = 0;
        ListNode current = first;
        do {
            if(current.value.equals(value)) return index;
            index++;
            current = current.next;
        } while(current != null);
        return -1;
    }

    /**
     * Removes element at the given index, others are squashed, no null left behind
     * @param index to be removed
     * @throws IndexOutOfBoundsException if index is less than 0 or bigger than collection size - 1
     */
    public void remove(int index) {
        if(index < 0 || index > size-1) throw new IndexOutOfBoundsException("Index must be between 0 and " + (size-1));
        ListNode current = first;
        int currentIndex = 0;
        do {
            if(index == currentIndex) {
                ListNode previous = current.previous;
                ListNode next = current.next;
                previous.next = next;
                next.previous = previous;
                size--;
                modificationCount++;
            }
            currentIndex++;
            current = current.next;
        } while(current != null);
    }

    /**
     * Returns true if collection contains no objects and false otherwise.
     *
     * @return   true if collection contains no elements.
     */
    @Override
    public boolean isEmpty() {
        if(first == null) return true;
        return first.value == null;
    }

    /**
     * Returns the number of currently stored objects in this collections.
     *
     * @return   number of elements of a collection.
     */
    @Override
    public int size() {
        int size = 0;
        ListNode current = first;
        if(current == null) return 0;
        if(current.value != null) size = 1;
        while(current.next != null) {
            current = current.next;
            size++;
        }
        this.size = size;
        return size;
    }

    /**
     * Adds the given object into this collection.
     * @param value object to be added to collection.
     * @throws NullPointerException if adding null to collection
     */
    @Override
    public void add(Object value) {
        if(value == null) throw new NullPointerException("Object cannot be null!");

        if(this.last == null) {
            ListNode currentLast;
            currentLast = new ListNode();
            currentLast.value = value;
            first = currentLast;
            last = currentLast;
        } else {
            ListNode currentLast;
            currentLast = last;
            ListNode nowLast = new ListNode();
            nowLast.value = value;
            last = nowLast;
            last.previous = currentLast;
            currentLast.next = nowLast;
        }
        size++;
        modificationCount++;
    }

    /**
     * Returns true only if the collection contains given value, as determined by equals method.
     * It is OK to ask if collection contains null.
     * @param value Object to check.
     * @return true if given object is found.
     */
    @Override
    public boolean contains(Object value) {
        ListNode current = first;
        do {
            if (current.value.equals(value)) return true;
            current = current.next;
        } while (current != null);
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
        ListNode current = first;
        do {
            if (current.value.equals(value)) {
                ListNode previous = current.previous;
                ListNode next = current.next;
                if(previous == null) {
                    next.previous = null;
                    first = next;
                } else {
                    previous.next = next;
                    next.previous = previous;
                }
                size--;
                modificationCount++;
                return true;
            }
            current = current.next;
        } while (current != null);
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
        ListNode current = first;
        if(current == null) throw new UnsupportedOperationException("Collection cannot be null!");

        Object[] array = new Object[size()];
        int i = 0;
        do {
            array[i] = current.value;
            i++;
            current = current.next;
        } while(current != null);
        return array;
    }


    /**
     * Removes all elements from this collection.
     */
    @Override
    public void clear() {
        first.next = null;
        first.value = null;
        first = null;
        last.previous = null;
        last.value = null;
        last = null;
        size = 0;
        modificationCount++;
    }


    @Override
    public ElementsGetter createElementsGetter() {
        LinkedListIndexedCollection linkedListIndexedCollection = LinkedListIndexedCollection.this;
        ElementsGetter elementsGetter = new ElementsGetter(linkedListIndexedCollection.modificationCount);
        elementsGetter.linkedListIndexedCollection = linkedListIndexedCollection;
        /*LinkedListIndexedCollection.ElementsGetter elementsGetter = new LinkedListIndexedCollection.ElementsGetter(LinkedListIndexedCollection.this.modificationCount);
        elementsGetter.linkedListIndexedCollection = LinkedListIndexedCollection.this;*/
        return elementsGetter;
    }
}
