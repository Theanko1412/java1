package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
            String[] showing = null;
            if(input.startsWith("query ")) {
                input = input.replace("query ", "");
                if(input.contains(" showing ")) {
                    String[] splitted = input.split(" showing ");
                    input = splitted[0];
                    showing = splitted[1].split(", ");
                }
                QueryParser queryParser = new QueryParser(input);
                queryFormater(queryParser, showing);
            } else if(input.equals("exit")) {
                System.out.println("Goodbye!");
                exit(0);
            } else {
                System.out.println("Avaliable arguments: query [String] *showing*, exit");
            }
        }
    }

    static void queryFormater(QueryParser queryParser, String[] showing) {
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
            if(showing == null) {
                if(s.getJmbag().length() > maxSizeJmbag) maxSizeJmbag = s.getJmbag().length();
                if(s.getLastName().length() > maxSizeLastName) maxSizeLastName = s.getLastName().length();
                if(s.getFirstName().length() > maxSizeFirstName) maxSizeFirstName = s.getFirstName().length();
            } else {
                if(Arrays.asList(showing).contains("jmbag")) {
                    if(s.getJmbag().length() > maxSizeJmbag) maxSizeJmbag = s.getJmbag().length();
                } else if(Arrays.asList(showing).contains("firstName")) {
                    if(s.getFirstName().length() > maxSizeFirstName) maxSizeFirstName = s.getFirstName().length();
                } else if(Arrays.asList(showing).contains("lastName")) {
                    if(s.getLastName().length() > maxSizeLastName) maxSizeLastName = s.getLastName().length();
                }
            }
        }
        printHeader(maxSizeJmbag, maxSizeLastName, maxSizeFirstName, showing);

        if(showing == null) {
            for(StudentRecord s : records) {
                System.out.println(
                        "| " +
                        s.getJmbag() + String.join("", Collections.nCopies(maxSizeJmbag-s.getJmbag().length(), " ")) + " | " +
                        s.getLastName() + String.join("", Collections.nCopies(maxSizeLastName-s.getLastName().length(), " ")) + " | " +
                        s.getFirstName() + String.join("", Collections.nCopies(maxSizeFirstName-s.getFirstName().length(), " ")) + " | " +
                        s.getFinalGrade() + " |");
            }
        } else {
            for(StudentRecord s : records) {
                System.out.printf("| ");
                for(String table : showing) {
                    switch (table) {
                        case "jmbag" -> System.out.printf("%s", s.getJmbag() + String.join("", Collections.nCopies(maxSizeJmbag-s.getJmbag().length(), " ")));
                        case "lastName" -> System.out.printf("%s", s.getLastName() + String.join("", Collections.nCopies(maxSizeLastName-s.getLastName().length(), " ")));
                        case "firstName" -> System.out.printf("%s", s.getFirstName() + String.join("", Collections.nCopies(maxSizeFirstName-s.getFirstName().length(), " ")));
                        case "grade" -> System.out.printf("%s", s.getFinalGrade());
                    }
                    System.out.printf(" | ");
                }
                System.out.printf("\n");
            }
        }

        printHeader(maxSizeJmbag, maxSizeLastName, maxSizeFirstName, showing);

        System.out.println("Records selected: " + records.size());
    }

    static void printHeader(int maxSizeJmbag, int maxSizeLastName, int maxSizeFirstName, String[] showing) {
        String plus = "+";
        String equals = "=";

        if(showing == null) {
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
        } else {
            for(String table : showing) {
                System.out.printf("%s", plus);
                switch (table) {
                    case "jmbag" -> System.out.printf("%s", String.join("", Collections.nCopies(maxSizeJmbag + 2, equals)));
                    case "firstName" -> System.out.printf("%s", String.join("", Collections.nCopies(maxSizeFirstName + 2, equals)));
                    case "lastName" -> System.out.printf("%s", String.join("", Collections.nCopies(maxSizeLastName + 2, equals)));
                    case "grade" -> System.out.printf("%s", String.join("", Collections.nCopies(3, equals)));
                }
            }
            System.out.printf("%s\n", plus);
        }
    }
}
