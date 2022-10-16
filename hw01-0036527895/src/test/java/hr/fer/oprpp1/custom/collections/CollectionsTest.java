package hr.fer.oprpp1.custom.collections;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//I was doing homework one problem at the time and later realized test should have been simplified
//I decided since I wrote all of them to keep them as is, but I will check in future for that
class CollectionsTest {

    //problem 2 tests
    @Test
    void testDefaultConstructor() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();

        assertEquals(collection.size(), 0);
        assertEquals(collection.getElements().length, 16);
    }

    @Test
    void testConstructorWithInitialCapacity() {
        int initialCapacity = 20;
        ArrayIndexedCollection collection = new ArrayIndexedCollection(initialCapacity);

        assertEquals(collection.size(), 0);
        assertEquals(collection.getElements().length, initialCapacity);
    }

    @Test
    void testConstructorWithInitialCapacityExceptionThrown() {
        int initialCapacity = 0;

        assertThrows(IllegalArgumentException.class, () -> {ArrayIndexedCollection collection = new ArrayIndexedCollection(initialCapacity);});
    }

    @Test
    void testConstructorWithGivenCollection() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        Object testObject = new Object();
        collection.add(testObject);

        ArrayIndexedCollection collection1 = new ArrayIndexedCollection(collection);

        assertEquals(collection1.getElements().length, 16);
        assertSame(testObject, collection1.get(0));
    }

    @Test
    void testConstructorGivenCollectionTooSmall() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        Object testObject = new Object();
        collection.add(testObject);
        collection.add(testObject);
        collection.add(testObject);
        collection.add(testObject);


        ArrayIndexedCollection collection1 = new ArrayIndexedCollection(collection, 3);

        assertEquals(collection1.getElements().length, 4);
        assertSame(testObject, collection1.get(3));
    }

    @Test
    void testConstructorWithGivenCollectionException() {

        assertThrows(NullPointerException.class, () -> {ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(null);});
    }


    @Test
    void testAddToCollection() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        arrayIndexedCollection.add("test");

        assertNotNull(arrayIndexedCollection.get(0));
    }

    @Test
    void testAddToCollectionNotBigEnough() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(1);

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");

        assertEquals(arrayIndexedCollection.getElements().length, 2);

        arrayIndexedCollection.add("test");

        assertEquals(arrayIndexedCollection.getElements().length, 2*2);
        assertNotNull(arrayIndexedCollection.get(2));
    }

    @Test
    void testAddToCollectionThrowsException() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        assertThrows(NullPointerException.class, () -> {arrayIndexedCollection.add(null);});
    }

    @Test
    void testGetAtIndex() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);
        Object testObject = new Object();
        arrayIndexedCollection.add(testObject);

        assertSame(arrayIndexedCollection.get(0), testObject);
    }

    @Test
    void testGetAtWrongIndex() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);

        assertThrows(IndexOutOfBoundsException.class, () -> {arrayIndexedCollection.get(2);});
        assertThrows(IndexOutOfBoundsException.class, () -> {arrayIndexedCollection.get(-1);});
    }

    @Test
    void testClearCollection() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(5);

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");

        arrayIndexedCollection.clear();

        assertThrows(IndexOutOfBoundsException.class, () -> {arrayIndexedCollection.get(0);});
        assertEquals(arrayIndexedCollection.getElements().length, 5);
    }

    @Test
    void testInsertInCollection() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);
        Object testObject = new Object();


        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");

        arrayIndexedCollection.insert(testObject, 0);

        assertEquals(arrayIndexedCollection.getElements().length, 2*2);
        assertSame(arrayIndexedCollection.get(0), testObject);
        assertEquals(arrayIndexedCollection.get(2), "test");
    }

    @Test
    void testInsertInCollectionExceptionThrown() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);

        assertThrows(NullPointerException.class, () -> {arrayIndexedCollection.insert(null, 0);});
        assertThrows(IndexOutOfBoundsException.class, () -> {arrayIndexedCollection.insert("test", 3);});
    }

    @Test
    void testIndexOfCollection() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test1");
        arrayIndexedCollection.add("test2");

        assertEquals(arrayIndexedCollection.indexOf("test"), 0);
        assertEquals(arrayIndexedCollection.indexOf("test2"), 2);
        assertEquals(arrayIndexedCollection.indexOf("doesNotExist"), -1);
    }

    @Test
    void testRemoveAtIndex() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test2");

        arrayIndexedCollection.remove(0);

        assertNotNull(arrayIndexedCollection.get(0));
        assertEquals(arrayIndexedCollection.getElements().length, 2);
        assertEquals(arrayIndexedCollection.indexOf("test2"), 0);
    }

    @Test
    void testRemoveAtIndexThrowsException() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection(2);

        assertThrows(IndexOutOfBoundsException.class, () -> {arrayIndexedCollection.remove(2);});
    }

    @Test
    void testArrayCollectionIsEmpty() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        assertTrue(arrayIndexedCollection.isEmpty());

        arrayIndexedCollection.add("test");

        assertFalse(arrayIndexedCollection.isEmpty());
    }

    @Test
    void testExampleFromHW() {
        ArrayIndexedCollection col = new ArrayIndexedCollection(2);
        col.add(20);
        col.add("New York");
        col.add("San Francisco");

        assertNotEquals(col.indexOf("New York"), -1);

        col.remove(1);

        assertEquals(col.get(1), "San Francisco");
        assertEquals(col.size(), 2);

        col.add("Los Angeles");
        LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);

        class P extends Processor {
            public void process(Object o) {}
        };

        assertTrue(col.contains(col2.get(1)));
        assertTrue(col2.contains(col.get(1)));

        col.remove(Integer.valueOf(20));
        assertFalse(col.contains("20"));
    }

    @Test
    void testArrayCollectionContains() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test1");

        assertTrue(arrayIndexedCollection.contains("test"));
        assertTrue(arrayIndexedCollection.contains("test1"));
        assertFalse(arrayIndexedCollection.contains("DOES NOT"));
    }

    @Test
    void testRemoveValue() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test1");
        arrayIndexedCollection.add("test1");
        arrayIndexedCollection.add("test2");

        assertTrue(arrayIndexedCollection.remove("test1"));
        assertEquals(arrayIndexedCollection.get(2), "test2");
        assertTrue(arrayIndexedCollection.remove("test"));
        assertTrue(arrayIndexedCollection.remove("test2"));
    }

    @Test
    void testToArray() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();

        Object[] array = new Object[]{"test", "test"};

        arrayIndexedCollection.add("test");
        arrayIndexedCollection.add("test");

        assertArrayEquals(array, arrayIndexedCollection.toArray());
    }


    //problem 3 tests
    @Test
    void testDefaultConstructorLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        assertEquals(linkedListIndexedCollection.getSize(), 0);
    }

    @Test
    void testConstructorWithGivenCollectionLinkedList() {
        ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();
        Object testObject = new Object();
        arrayIndexedCollection.add(testObject);

        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection(arrayIndexedCollection);

        assertSame(linkedListIndexedCollection.get(0), testObject);
    }

    @Test
    void testConstructorWithGivenCollectionLinkedListException() {
        assertThrows(NullPointerException.class, () -> {new LinkedListIndexedCollection(null);});
    }

    @Test
    void testGetAsIndexLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test");

        assertEquals(linkedListIndexedCollection.get(0), "test");
    }

    @Test
    void testGetAsIndexLinkedListException() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        assertThrows(IndexOutOfBoundsException.class, () -> {linkedListIndexedCollection.get(-1);});
    }

    @Test
    void testInsertIntoLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test");

        linkedListIndexedCollection.insert("INSERTED", 1);

        assertEquals(linkedListIndexedCollection.size(), 3);
        assertEquals(linkedListIndexedCollection.get(0), "test");
        assertEquals(linkedListIndexedCollection.get(1), "INSERTED");
        assertEquals(linkedListIndexedCollection.get(2), "test");
    }

    @Test
    void testInsertIntoLinkedListOutOfBounds() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        assertThrows(IndexOutOfBoundsException.class, () -> {linkedListIndexedCollection.insert("test", 1);});
        assertThrows(NullPointerException.class, () -> {linkedListIndexedCollection.insert(null, 0);});
    }

    @Test
    void testIndexOfLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test1");

        assertEquals(linkedListIndexedCollection.indexOf("test1"), 1);
        assertEquals(linkedListIndexedCollection.indexOf("doesnt exist"), -1);
    }

    @Test
    void testRemoveFromLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("testRemove");
        linkedListIndexedCollection.add("test");

        linkedListIndexedCollection.remove(1);

        assertEquals(linkedListIndexedCollection.get(0), "test");
        assertEquals(linkedListIndexedCollection.get(1), "test");
    }

    @Test
    void testIfLinkedListIsEmpty() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        assertTrue(linkedListIndexedCollection.isEmpty());

        linkedListIndexedCollection.add("test");

        assertFalse(linkedListIndexedCollection.isEmpty());
    }

    @Test
    void testSizeLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();
        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test");

        assertEquals(linkedListIndexedCollection.size(), 3);
    }

    @Test
    void testAddLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();
        linkedListIndexedCollection.add("start");
        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("end");

        //ListNode is private class cannot test links and values
        assertEquals(linkedListIndexedCollection.size(), 3);
    }

    @Test
    void testContains() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();
        linkedListIndexedCollection.add("test");

        assertTrue(linkedListIndexedCollection.contains("test"));
    }

    @Test
    void removeFromLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();
        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test1");
        linkedListIndexedCollection.add("test");

        assertTrue(linkedListIndexedCollection.remove("test"));
        assertEquals(linkedListIndexedCollection.size(), 2);
        assertEquals(linkedListIndexedCollection.get(0), "test1");
    }

    @Test
    void testLinkedListToArray() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test");
        linkedListIndexedCollection.add("test");

        Object[] array = new Object[]{"test", "test"};

        assertArrayEquals(linkedListIndexedCollection.toArray(), array);
    }

    @Test
    void testLinkedListToArrayException() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        assertThrows(UnsupportedOperationException.class, linkedListIndexedCollection::toArray);
    }

    @Test
    void testClearLinkedList() {
        LinkedListIndexedCollection linkedListIndexedCollection = new LinkedListIndexedCollection();

        linkedListIndexedCollection.add("test1");
        linkedListIndexedCollection.add("test2");
        linkedListIndexedCollection.add("test3");

        linkedListIndexedCollection.clear();

        assertEquals(linkedListIndexedCollection.size(), 0);
        assertNull(linkedListIndexedCollection.getFirst());
        assertNull(linkedListIndexedCollection.getLast());
    }

    @Test
    void testPushPop() {

        ObjectStack objectStack = new ObjectStack();
        objectStack.push("test");
        objectStack.push("test2");

        assertEquals(objectStack.size(), 2);

        objectStack.pop();
        assertEquals(objectStack.size(), 1);

        objectStack.pop();
        assertThrows(EmptyStackException.class, objectStack::pop);
    }
}