package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SmartScriptTester {

    public static void main(String[] args) throws IOException {
        //
        //lexer test
        //
        /*String docBody = Files.readString(Paths.get("hw02-0036527895/src/main/java/hr/fer/oprpp1/custom/scripting/parser/examples/doc1.txt"));
        //SmartScriptParser parser = null;
        try {
            //parser = new SmartScriptParser(docBody);
        } catch(SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        SmartScriptLexer lexer = new SmartScriptLexer(docBody);
        SmartScriptLexerToken token;
        while(lexer.nextToken().getType() != SmartScriptLexerTokenType.EOF) {
            token = lexer.getToken();
            System.out.println(token.getType());
            System.out.println(token.getValue());
        }*/

        //
        //parser test
        //
        /*String docBody = Files.readString(Paths.get("hw02-0036527895/src/main/java/hr/fer/oprpp1/custom/scripting/parser/examples/doc1.txt"));
        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch(SmartScriptParserException | SmartScriptLexerException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();*/

        //
        //toString test
        //
        /*String docBody = Files.readString(Paths.get("hw02-0036527895/src/main/java/hr/fer/oprpp1/custom/scripting/parser/examples/doc1.txt"));
        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();
// now document and document2 should be structurally identical trees
        boolean same = document.equals(document2); // ==> "same" must be true
        System.out.println("Fields are the same? ->" + same);*/
    }
}
