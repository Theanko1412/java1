package hr.fer.oprpp1.custom.scripting.nodes;

import java.util.HashMap;
import java.util.Objects;

public class DocumentNode extends Node implements DeParseInterface{


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Object o : this.getCollection().toArray()) {
            Node node = (Node) o;
            testChildren(node, sb);
        }
        return sb.toString();
    }


    @Override
    public String deParse() {
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        DocumentNode documentNode = (DocumentNode) obj;
        return Objects.equals(this.toString(), documentNode.toString());
    }

    private void testChildren(Node node, StringBuilder sb) {
        try {
            int i = 0;
            while(i < node.numberOfChildren()) {
                if(i == 0) sb.append(node.deParse());
                testChildren(node.getChild(i), sb);
                i++;
            }
        } catch (NullPointerException e) {
            sb.append(node.deParse());
        }
    }
}

