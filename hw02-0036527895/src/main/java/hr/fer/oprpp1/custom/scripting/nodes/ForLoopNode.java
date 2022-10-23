package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

public class ForLoopNode extends Node implements DeParseInterface {

    private ElementVariable variable;
    private Element startExpression;
    private Element endExpression;
    private Element stepExpression;

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = null;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }


    @Override
    public String toString() {
        return getVariable().asText() +
                " " +
                getStartExpression().asText() +
                " " +
                getEndExpression().asText() +
                " " +
                getStepExpression().asText();
    }

    @Override
    public String deParse() {
        return "{$FOR " + this.toString() + " $}";
    }
}
