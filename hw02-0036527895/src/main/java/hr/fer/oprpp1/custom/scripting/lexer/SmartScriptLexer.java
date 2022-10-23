package hr.fer.oprpp1.custom.scripting.lexer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartScriptLexer {

    private char[] data;
    private SmartScriptLexerToken token;
    private int currentIndex;
    private SmartScriptLexerState state;

    public SmartScriptLexer(String documentBody) {
        this.data = documentBody.trim().toCharArray();
        this.currentIndex = 0;
        this.state = SmartScriptLexerState.TEXT;
    }

    public void setState(SmartScriptLexerState state) {
        if(state == null) throw new NullPointerException("State cannot be null!");
        this.state = state;
    }

    public SmartScriptLexerToken nextToken() {
        if(currentIndex == data.length) {
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.EOF, null);
            currentIndex++;
            return token;
        } else if(currentIndex > data.length && token.getType() == SmartScriptLexerTokenType.EOF) {
            throw new SmartScriptLexerException("Reached end of file.");
        } else if (data[currentIndex] == ' ') {
            while (data[currentIndex] == ' ') currentIndex++;
        }
        if(state == SmartScriptLexerState.TEXT) {
            token = evaluateTokenText();
        } else if(state == SmartScriptLexerState.TAG) {
            token = evaluateTokenTag();
        }
        return token;
    }

    public SmartScriptLexerToken getToken() {
        return token;
    }

    private SmartScriptLexerToken evaluateTokenText() {
        int start = currentIndex;
        try {
            while(currentIndex < data.length) {
                if(data[currentIndex] == '{' && data[currentIndex+1] == '$') {
                    try {
                        if(data[currentIndex-1] == '\\') {
                            currentIndex++;
                            currentIndex++;
                            continue;
                        } else {
                            break;
                        }
                    } catch (IndexOutOfBoundsException ignored) {break;}
                }
                if(data[currentIndex] != '\\' && (data[currentIndex+1] == '{' && data[currentIndex+2] == '$')) {
                    currentIndex++;
                    break;
                }
                currentIndex++;
            }
        } catch (IndexOutOfBoundsException ignored) {
            currentIndex++;
        }
        int end = currentIndex;
        String string = new String(data, start, end-start);
        if(string.equals("") && data[currentIndex] == '{' && data[currentIndex+1] == '$') {
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.TAG, String.valueOf("{$"));
            setState(SmartScriptLexerState.TAG);
            currentIndex++;
            currentIndex++;
            return token;
        }
        while(string.contains("\\\\")) {
            string = string.replace("\\\\", "\\");
        }
        String pattern = "(\\\\)(\\{)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), ""));
        }
        m.appendTail(sb);
        string = sb.toString();
        if(string.contains("\\")) {
            throw new SmartScriptLexerException("Invalid char sequence");
        }
        token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_STRING, string);
        return token;

    }

    private SmartScriptLexerToken evaluateTokenTag() {
        try {
            if(data[currentIndex] == '$' && data[currentIndex+1] == '}') {
                currentIndex++;
                currentIndex++;
                token = new SmartScriptLexerToken(SmartScriptLexerTokenType.TAG, "$}");
                setState(SmartScriptLexerState.TEXT);
                return token;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new SmartScriptLexerException("Invalid tag");
        }
        if(data[currentIndex] == '=') {
            currentIndex++;
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_TAG_NAME, String.valueOf('='));
            return token;
        }
        if(Character.isLetter(data[currentIndex])) {
            int start = currentIndex;
            try {
                while(currentIndex < data.length &&
                        (Character.isLetter(data[currentIndex+1]) || Character.isDigit(data[currentIndex+1]) || data[currentIndex+1] == '_')) currentIndex++;

            } catch (IndexOutOfBoundsException e) {
                throw new SmartScriptLexerException("CHECK LINE 90");
            }
            currentIndex++;
            int end = currentIndex;
            String string = new String(data, start, end-start);
            if(string.equals("")) {
                string = String.valueOf(data[currentIndex]);
                currentIndex++;
            }
            if(token.getType() == SmartScriptLexerTokenType.TAG) {
                token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_TAG_NAME, string);
            } else {
                token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_VARIABLE, string);
            }
            return token;
        }
        if(data[currentIndex] == '@') {
            int start = currentIndex;
            try {
                if(Character.isLetter(data[currentIndex+1])) {
                    while(currentIndex < data.length &&
                            (Character.isLetter(data[currentIndex+1]) || Character.isDigit(data[currentIndex+1]) || data[currentIndex+1] == '_')) currentIndex++;
                } else throw new SmartScriptLexerException("Invalid function name!");
            } catch (IndexOutOfBoundsException e) {
                throw new SmartScriptLexerException("Invalid function name!");
            }
            int end = currentIndex;
            String string = new String(data, start, end-start+1);
            if(string.equals("")) throw new SmartScriptLexerException("Invalid function name!");
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_FUNCTION, string);
            currentIndex++;
            return token;
        }
        if(data[currentIndex] == '+' || data[currentIndex] == '*' || data[currentIndex] == '/' || data[currentIndex] == '^') {
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_OPERATOR, String.valueOf(data[currentIndex]));
            currentIndex++;
            return token;
        }
        if(Character.isDigit(data[currentIndex])) {
            int start = currentIndex;
            int end;
            return checkIfNumber(start);
        }
        if(data[currentIndex] == '-') {
            int start = currentIndex;
            int end;
            try {
                if(!Character.isDigit(data[currentIndex+1])) {
                    token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_OPERATOR, data[currentIndex]);
                    currentIndex++;
                    return token;
                } else {
                    return checkIfNumber(start);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new SmartScriptLexerException("Check 162");
            }
        }
        if(data[currentIndex] == '"') {
            int start = currentIndex;
            int end;
            int count = 0;
            try {
                while(currentIndex < data.length) {
                    if(data[currentIndex] == '\\' && data[currentIndex+1] == '"') {
                        currentIndex++;
                        currentIndex++;
                        continue;
                    }
                    if(data[currentIndex] == '"' && count == 1) {
                        break;
                    } else if(data[currentIndex] == '"' && count !=1) {
                        count++;
                    } else if(data[currentIndex] == '\\' && data[currentIndex+1] == 'n') {
                        currentIndex++;
                    } else if(data[currentIndex] == '\\' && data[currentIndex+1] == 'r') {
                        currentIndex++;
                    } else if(data[currentIndex] == '\\' && data[currentIndex+1] == 't') {
                        currentIndex++;
                    } else if(data[currentIndex] == '\\' && ((data[currentIndex+1] != '\\') && data[currentIndex+1] != '"')) throw new SmartScriptLexerException("Invalid tag string");
                    currentIndex++;
                }
            } catch (IndexOutOfBoundsException e) {
                throw new SmartScriptLexerException("Invalid string!");
            }
            if(currentIndex+1 < data.length) {
                currentIndex++;
            } else {
                throw new SmartScriptLexerException("Invalid string!");
            }
            end = currentIndex;
            String string = new String(data, start, end-start);
            String pattern = "(\\\\)(\\\")";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(string);
            StringBuffer sb = new StringBuffer();
            while(m.find()) {
                m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), ""));
            }
            m.appendTail(sb);
            string = sb.toString();
            string = string.replace("\\\\", "\\");
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_STRING, string);
            return token;
        }
        throw new SmartScriptLexerException("Dont ask me 190");
    }

    private SmartScriptLexerToken checkIfNumber(int start) {
        int end;
        try {
            while(currentIndex < data.length && Character.isDigit(data[currentIndex+1])) currentIndex++;
        } catch (IndexOutOfBoundsException e) {
            throw new SmartScriptLexerException("CHECK 132");
        }
        if(data[currentIndex] == '.') {
            try {
                while(currentIndex < data.length && Character.isDigit(data[currentIndex+1])) currentIndex++;
            } catch (IndexOutOfBoundsException e) {
                throw new SmartScriptLexerException("CHECK 138");
            }
            currentIndex++;
            end = currentIndex;
            String string = new String(data, start, end-start);
            if(string.endsWith(".")) throw new SmartScriptLexerException("Invalid double!");
            token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_CONSTANT_DOUBLE, Double.parseDouble(string));
            return token;
        }
        currentIndex++;
        end = currentIndex;
        String string = new String(data, start, end-start);
        if(string.equals("")) {
            string = String.valueOf(data[currentIndex]);
            currentIndex++;
        }
        token = new SmartScriptLexerToken(SmartScriptLexerTokenType.ELEMENT_CONSTANT_INTEGER, Integer.parseInt(string));
        return token;
    }
}
