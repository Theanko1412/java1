package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

public class EchoNode extends Node implements DeParseInterface{

    private Element[] elements;

    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }


    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(Element e: elements) {
            string.append(e.asText());
            string.append(" ");
        }
        return string.toString();
    }

    @Override
    public String deParse() {
        return "{$=" + this.toString() + "$}";
    }
}
