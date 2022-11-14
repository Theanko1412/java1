package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class DatabaseDemo {

    static StudentDatabase sdb;

    static void initializeDatabase() throws IOException {
        sdb = new StudentDatabase(Files.readAllLines(
                Path.of(Path.of("").toAbsolutePath() + "/hw04-0036527895/src/main/resources/database.txt"),
                StandardCharsets.UTF_8
        ));
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        initializeDatabase();

        while(true) {
            System.out.print("> ");
            String input = in.nextLine().strip();
            if(input.startsWith("query ")) {
                input = input.replace("query ", "");
                QueryParser queryParser = new QueryParser(input);
                queryFormater(queryParser);
            } else if(input.equals("exit")) {
                System.out.println("Goodbye!");
                exit(0);
            } else {
                System.out.println("Avaliable arguments: query [String], exit");
            }
        }
    }

    static void queryFormater(QueryParser queryParser) {
        int maxSizeJmbag = 0;
        int maxSizeLastName = 0;
        int maxSizeFirstName = 0;
        List<StudentRecord> records = new ArrayList<>();
        if(queryParser.isDirectQuery()) {
            System.out.println("Using index for record retrieval.");
            if(sdb.forJMBAG(queryParser.getQueriedJMBAG()) == null) {
                System.out.println("Student with given jmbag doesnt exist!");
                return;
            }
            records.add(sdb.forJMBAG(queryParser.getQueriedJMBAG()));
        } else {
            List<ConditionalExpression> c = queryParser.getQuery();
            for(int i = 0; i < c.size(); i++) {
                if(i == 0) {
                    for(StudentRecord studentRecord : sdb.getStudentRecords()) {
                        if(c.get(i).getStrategyComparison().satisfied(c.get(i).getStrategyField().get(studentRecord), c.get(i).getLiteral())) {
                            records.add(studentRecord);
                        }
                    }
                } else {
                    for(StudentRecord studentRecord : new ArrayList<>(records)) {
                        if(!c.get(i).getStrategyComparison().satisfied(c.get(i).getStrategyField().get(studentRecord), c.get(i).getLiteral())) {
                            records.remove(studentRecord);
                        }
                    }
                }
            }
        }

        if(records.size() == 0) {
            System.out.println("Records selected: 0");
            return;
        }

        for(StudentRecord s : records) {
            if(s.getJmbag().length() > maxSizeJmbag) maxSizeJmbag = s.getJmbag().length();
            if(s.getLastName().length() > maxSizeLastName) maxSizeLastName = s.getLastName().length();
            if(s.getFirstName().length() > maxSizeFirstName) maxSizeFirstName = s.getFirstName().length();
        }
        printHeader(maxSizeJmbag, maxSizeLastName, maxSizeFirstName);

        for(StudentRecord s : records) {
            System.out.println(
                    "| " +
                    s.getJmbag() + String.join("", Collections.nCopies(maxSizeJmbag-s.getJmbag().length(), " ")) + " | " +
                    s.getLastName() + String.join("", Collections.nCopies(maxSizeLastName-s.getLastName().length(), " ")) + " | " +
                    s.getFirstName() + String.join("", Collections.nCopies(maxSizeFirstName-s.getFirstName().length(), " ")) + " | " +
                    s.getFinalGrade() + " |");
        }

        printHeader(maxSizeJmbag, maxSizeLastName, maxSizeFirstName);

        System.out.println("Records selected: " + records.size());
    }

    static void printHeader(int maxSizeJmbag, int maxSizeLastName, int maxSizeFirstName) {
        String plus = "+";
        String equals = "=";

        System.out.println(
                plus +
                String.join("", Collections.nCopies(maxSizeJmbag+2, equals)) +
                plus +
                String.join("", Collections.nCopies(maxSizeLastName+2, equals)) +
                plus +
                String.join("", Collections.nCopies(maxSizeFirstName+2, equals)) +
                plus +
                String.join("", Collections.nCopies(3, equals)) +
                plus
        );
    }
}
