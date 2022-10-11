package hr.fer.oprpp1.custom.collections;

public class ArrayIndexedCollection extends Collection{

    private int size;
    private Object[] elements;

    public ArrayIndexedCollection() {
        this(16);
        //this.size = 16;
        //this.elements[16] = null;
    }

    public ArrayIndexedCollection(int initialCapacity) {
        if(initialCapacity < 1) throw new IllegalArgumentException("Initial capacity cannot be less than 1!");
        this.size = initialCapacity;
        this.elements[initialCapacity] = null;
    }

    public ArrayIndexedCollection(Collection collection) {
        //array never smaller than 16? junk?
        this();
        if(collection == null) throw new NullPointerException("Given collection is null!");
        //
        //what if we give collection bigger than default 16 size without initialCapacity?
        //
        /*if(this.size < collection.size()) {
            this.size = collection.size();
            this.elements[collection.size()] = null;
        }*/
    }

    public ArrayIndexedCollection(Collection collection, int initialCapacity) {
        this(collection);
        if(initialCapacity < collection.size()) {
            this.size = collection.size();
            this.elements[collection.size()] = null;
        }
    }

    public Object get(int index) {
        if(index > elements.length || index < 0) throw new IndexOutOfBoundsException("Index value is expected between" +
                " 0 and " + elements.length);
        for(Object object : elements) {
            if(index == 0) return object;
            index--;
        }
        //unreachable
        throw new IndexOutOfBoundsException("Index value not as expected!");
    }

    public int indexOf(Object value) {
        if(value == null) return -1;
        int index = 0;
        for(Object object : elements) {
            if(object.equals(value)) return index;
            ++index;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public void add(Object value) {
        if(value == null) throw new NullPointerException("Cannot add null to collection!");
        if(elements[elements.length-1] != null) {
            Object[] newElements = new Object[elements.length*2];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }
        for(Object object : elements) {
            if(object == null) {
                object = value;
                break;
            }
        }
    }

    @Override
    public boolean contains(Object value) {
        return super.contains(value);
    }

    @Override
    public boolean remove(Object value) {
        return super.remove(value);
    }

    @Override
    public Object[] toArray() {
        return super.toArray();
    }

    @Override
    public void forEach(Processor processor) {
        super.forEach(processor);
    }

    @Override
    public void addAll(Collection other) {
        super.addAll(other);
    }

    @Override
    public void clear() {
        for(Object object : elements) {
            if(object == null) break;
            object = null;
        }
    }
}
