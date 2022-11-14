package hr.fer.oprpp1.custom.collections;

import hr.fer.oprpp1.custom.collections.demo.EvenIntegerTester;

/**
 *  Interface Collection represents some general collection of objects
 */
public interface Collection {


    ElementsGetter createElementsGetter();

    default boolean isEmpty() {
        return this.size() == 0;
    }

    default void addAll(Collection other) {

        class Processor implements hr.fer.oprpp1.custom.collections.Processor {
            @Override
            public void process(Object object) {
                add(object);
            }
        }
        other.forEach(new Processor());
    }

    default void addAllSatisfying(Collection col, Tester tester) {
        if(col.isEmpty()) throw new NullPointerException("Given collection is empty!");

        ElementsGetter elementsGetter = col.createElementsGetter();

        do {
            Object nextElement = elementsGetter.getNextElement();
            if(tester.test(nextElement)) this.add(nextElement);
        } while(elementsGetter.hasNextElement());
    }

    default void forEach(Processor processor) {
        ElementsGetter elementsGetter = this.createElementsGetter();
        while(elementsGetter.hasNextElement()) processor.process(elementsGetter.getNextElement());
    }

    int size();

    void add(Object value);

    boolean contains(Object value);

    boolean remove(Object value);

    Object[] toArray();

    void clear();
}
