package hr.fer.oprpp1.custom.scripting.lexer;

public class SmartScriptLexerToken {

    private SmartScriptLexerTokenType type;
    private Object value;

    public SmartScriptLexerToken(SmartScriptLexerTokenType type, Object value) {
        if(type == null) throw new SmartScriptLexerException("Token type can not be null.");
        this.type = type;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public SmartScriptLexerTokenType getType() {
        return type;
    }
}
