package hr.fer.oprpp1.custom.collections;

/**
 *  Class Collection represents some general collection of objects
 */
public class Collection {

    protected Collection() {}

    /**
     * Returns true if collection contains no objects and false otherwise.
     *
     * @return   true if collection contains no elements.
     */
    public boolean isEmpty() {
        if(this.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of currently stored objects in this collections.
     *
     * @return   number of elements of a collection.
     */
    public int size() {
        return 0;
    }

    /**
     * Adds the given object into this collection.
     * @param value object to be added to collection.
     */
    public void add(Object value) {}

    /**
     * Returns true only if the collection contains given value, as determined by equals method.
     * It is OK to ask if collection contains null.
     * @param value Object to check.
     * @return true if given object is found.
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Returns true only if the collection contains given value as determined by equals method and removes
     * one occurrence of it (in this class it is not specified which one).
     * @param value Object to be removed.
     * @return true if object is removed.
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns null.
     * @return array of objects.
     * @throws UnsupportedOperationException ? not sure when
     */
    public Object[] toArray() {
        Object[] array = new Object[this.size()];
        return array;
    }

    /**
     * Method calls processor.process(.) for each element of this collection. The order in which elements
     * will be sent is undefined in this class.
     * @param processor object with custom process() method.
     */
    public void forEach(Processor processor) {}

    /**
     * Method adds into the current collection all elements from the given collection. This other collection
     * remains unchanged.
     * @param other collection we want to add to our collection.
     */
    public void addAll(Collection other) {

        class Processor extends hr.fer.oprpp1.custom.collections.Processor {
            public void process(Object object) {
                add(object);
            }
        }

        other.forEach(new Processor());
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {}
}
