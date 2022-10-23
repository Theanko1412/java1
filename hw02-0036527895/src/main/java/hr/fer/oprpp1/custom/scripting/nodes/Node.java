package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

public class Node implements DeParseInterface{

    private ArrayIndexedCollection collection;

    public void addChildNode(Node child) {
        if(collection == null) collection = new ArrayIndexedCollection();
        collection.add(child);
    }

    public int numberOfChildren() {
        return collection.getSize();
    }

    public Node getChild(int index) {
        return (Node) collection.get(index);
    }

    public ArrayIndexedCollection getCollection() {
        return collection;
    }

    @Override
    public String deParse() {
        return "X";
    }
}
