package hr.fer.oprpp1.hw02.prob1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private char[] data;
    private Token token;
    private int currentIndex;
    private LexerState state;

    public Lexer(String text) {
        this.data = text.trim().toCharArray();
        this.currentIndex = 0;
        this.state = LexerState.BASIC;
    }

    public void setState(LexerState state) {
        if(state == null) throw new NullPointerException("State cannot be null!");
        this.state = state;
    }

    public Token nextToken() {
        if(currentIndex == data.length) {
            token = new Token(TokenType.EOF, null);
            currentIndex++;
            return token;
        } else if(currentIndex > data.length && token.getType() == TokenType.EOF) {
            throw new LexerException("Reached end of file.");
        } else if (data[currentIndex] == ' ' || data[currentIndex] == '\t' || data[currentIndex] == '\r' || data[currentIndex] == '\n') {
            while (data[currentIndex] == ' ' || data[currentIndex] == '\t' || data[currentIndex] == '\r' || data[currentIndex] == '\n') {
                currentIndex++;
            }
        }
        if(state == LexerState.BASIC) {
            token = evaluateTokenBasic();
        } else if(state == LexerState.EXTENDED) {
            token = evaluateTokenExtended();
        }
        return token;
    }

    public Token getToken() {
        return token;
    }

    private Token evaluateTokenBasic() {
        if (Character.isDigit(data[currentIndex])) {
            int start = currentIndex;
            while(currentIndex < data.length && Character.isDigit(data[currentIndex])) currentIndex++;
            int end = currentIndex;
            String string = new String(data, start, end-start);
            try {
                token = new Token(TokenType.NUMBER, Long.valueOf(string));
            } catch (NumberFormatException e) {
                throw new LexerException("Ulaz ne valja!");
            }
            return token;
        } else if (Character.isLetter(data[currentIndex])) {
            int start = currentIndex;
            while (currentIndex < data.length && ((Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\'+'\\') || data[currentIndex] == '\\')) {
                try {
                    if(Character.isDigit(data[currentIndex]) && data[currentIndex-1] != '\\') break;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                if(currentIndex < data.length && data[currentIndex] == '\\') {
                    try {
                        if((!Character.isDigit(data[currentIndex+1]) && (data[currentIndex+1] != '\\')))
                            throw new LexerException("Backslash is always followed by a number!");
                    } catch (IndexOutOfBoundsException e) {
                        if(data[currentIndex-1] == '\\' && data[currentIndex] == '\\') {
                            currentIndex++;
                            break;
                        }
                    }
                }
                currentIndex++;
            }
            int end = currentIndex;
            String string = new String(data, start, end - start);
            String pattern = "(\\\\)(\\d)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(string);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), ""));
            }
            m.appendTail(sb);
            string = sb.toString();
            string = string.replace("\\\\", "\\");
            token = new Token(TokenType.WORD, string);
            return token;
        } else if(data[currentIndex] == '\\') {
            currentIndex++;
            int start = currentIndex;
            while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\')) currentIndex++;
            int end = currentIndex;
            String string = new String(data, start, end - start);
            char[] arrayFromString = string.toCharArray();
            if(arrayFromString.length == 0) {
                throw new LexerException("Backslash is always followed by a number!");
            } else {
                if(!Character.isDigit(arrayFromString[0])) throw new LexerException("Backslash is always followed by a number!");
            }
            token = new Token(TokenType.WORD, string);
            return token;
        } else if (!Character.isWhitespace(data[currentIndex])) {
            token = new Token(TokenType.SYMBOL, data[currentIndex]);
            currentIndex++;
            return token;
        }
        throw new LexerException("Unreachable code");
    }

    private Token evaluateTokenExtended() {
        int start = currentIndex;
        while (currentIndex < data.length && data[currentIndex] != '#' && !Character.isWhitespace(data[currentIndex])) currentIndex++;
        int end = currentIndex;
        String string = new String(data, start, end-start);
        if(string.equals("") && data[currentIndex] == '#') {
            //setState??
            Token token = new Token(TokenType.SYMBOL, data[currentIndex]);
            currentIndex++;
            return token;
        }
        return new Token(TokenType.WORD, string);
    }
}
