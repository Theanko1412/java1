package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

public class SmartScriptParser {

    private String documentBody;
    private SmartScriptLexer lexer;
    private ObjectStack stack;
    private ObjectStack stackOriginal;
    private DocumentNode documentNode;

    public SmartScriptParser(String documentBody) {
        this.documentBody = documentBody;
        this.lexer = new SmartScriptLexer(documentBody);
        this.stack = new ObjectStack();
        this.stackOriginal = new ObjectStack();
        parseDocument();
    }

    public SmartScriptLexer getLexer() {
        return lexer;
    }

    public ObjectStack getStackOriginal() {
        return stackOriginal;
    }

    public DocumentNode getDocumentNode() {
        return documentNode;
    }


    private void parseDocument() {
        documentNode = new DocumentNode();
        stack.push(documentNode);
        System.out.println("Added DocumentNode");
        while(lexer.nextToken().getType() != SmartScriptLexerTokenType.EOF) {
            stackOriginal.push(lexer.getToken().getValue());
            if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_STRING) {
                TextNode text = new TextNode(lexer.getToken().getValue().toString());
                if(stack.isEmpty()) throw new SmartScriptParserException("More ENDS than opened non empty tags");
                Node lastElement = (Node) stack.peek();
                lastElement.addChildNode(text);
                System.out.println("--- Added TextNode -> " + text.getText());
            } else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_TAG_NAME && lexer.getToken().getValue().equals("=")) {
                Element[] vars;
                ArrayIndexedCollection arrayIndexedCollection = new ArrayIndexedCollection();
                while(lexer.nextToken().getType() != SmartScriptLexerTokenType.TAG) {
                    arrayIndexedCollection.add(lexer.getToken().getValue());
                }
                vars = new Element[arrayIndexedCollection.getSize()];
                Object[] array = arrayIndexedCollection.toArray();
                int i = 0;
                for(Object o : array) {
                    vars[i] = new ElementVariable(o.toString());
                    i++;
                }
                EchoNode echo = new EchoNode(vars);
                if(stack.isEmpty()) throw new SmartScriptLexerException("More ENDS than opened non empty tags");
                Node lastElement = (Node) stack.peek();
                lastElement.addChildNode(echo);
                System.out.println("--- Added EchoNode -> " + echo);
            }
            else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_TAG_NAME && !lexer.getToken().getValue().toString().toLowerCase().equals("end") &&
                    lexer.getToken().getValue() != "=") {
                if(lexer.nextToken().getType() != SmartScriptLexerTokenType.ELEMENT_VARIABLE) throw new SmartScriptLexerException("Expecting variable as first parameter.");
                ElementVariable variable = new ElementVariable(lexer.getToken().getValue().toString());
                lexer.nextToken();
                Element startExpression;
                if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_INTEGER) {
                    startExpression = new ElementConstantInteger(Integer.parseInt(lexer.getToken().getValue().toString()));
                } else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_DOUBLE) {
                    startExpression = new ElementConstantDouble(Double.parseDouble(lexer.getToken().getValue().toString()));
                } else throw new SmartScriptLexerException("Expected integer or double");
                lexer.nextToken();
                Element endExpression;
                if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_INTEGER) {
                    endExpression = new ElementConstantInteger(Integer.parseInt(lexer.getToken().getValue().toString()));
                } else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_DOUBLE) {
                    endExpression = new ElementConstantDouble(Double.parseDouble(lexer.getToken().getValue().toString()));
                } else throw new SmartScriptLexerException("Expected integer or double");
                lexer.nextToken();
                Element stepExpression;
                if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_INTEGER) {
                    stepExpression = new ElementConstantInteger(Integer.parseInt(lexer.getToken().getValue().toString()));
                } else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_CONSTANT_DOUBLE) {
                    stepExpression = new ElementConstantDouble(Double.parseDouble(lexer.getToken().getValue().toString()));
                } else stepExpression = null;

                ForLoopNode nonEmptyTag = new ForLoopNode(
                        variable,
                        startExpression,
                        endExpression,
                        stepExpression
                );
                if(stack.isEmpty()) throw new SmartScriptLexerException("More ENDS than opened non empty tags");
                Node lastElement = (Node) stack.peek();
                lastElement.addChildNode(nonEmptyTag);
                stack.push(nonEmptyTag);
                System.out.println("--- Added ForLoopNode -> " + nonEmptyTag);
            }
            else if(lexer.getToken().getType() == SmartScriptLexerTokenType.ELEMENT_TAG_NAME && lexer.getToken().getValue().toString().toLowerCase().equals("end")) {
                try {
                    Node peek = (Node) stack.peek();
                    //not sure if correct
                    TextNode textNode = new TextNode("{$END$}");
                    peek.addChildNode(textNode);
                    System.out.println("--- Added END");
                    stack.pop();
                    System.out.println("Removed from stack -> " + peek.toString());
                } catch (EmptyStackException e) {
                    throw new SmartScriptParserException("More ENDS than opened non empty tags");
                }
            }
        }
        if(stack.isEmpty()) throw new SmartScriptParserException("More ENDS than opened non empty tags");
    }
}
