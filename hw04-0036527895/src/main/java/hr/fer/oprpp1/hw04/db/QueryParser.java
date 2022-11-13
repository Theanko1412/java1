package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryParser {

    private String query;
    private List<String> subqueryArray;
    private boolean isDirect = false;

    public QueryParser(String query) {
        this.query = query.strip();
        parseQuery();
    }

    void parseQuery() {
        subqueryArray = Arrays.stream(query.split("(?i)and")).map(String::strip).collect(Collectors.toList());
        if (subqueryArray.size() == 1) {
            subqueryArray.set(0, subqueryArray.get(0).replaceAll("\\s", ""));
            isDirect = subqueryArray.get(0).startsWith("jmbag=\"") &&
                    subqueryArray.get(0).endsWith("\"");
        }
    }

    boolean isDirectQuery() {
        return isDirect;
    }

    String getQueriedJMBAG() {
        if (!isDirectQuery()) throw new IllegalArgumentException("Query is not direct!");
        return Arrays.stream(
                        subqueryArray.get(0)
                                .split("="))
                .collect(Collectors.toList())
                .get(1)
                .replace("\"", "");
    }

    List<ConditionalExpression> getQuery() {
        List<ConditionalExpression> listOfQueries = new ArrayList<>();
        for (String value : subqueryArray) {
            IFieldValueGetter iFieldValueGetter;
            IComparisonOperator iComparisonOperator;
            List<String> decoupled;
            String s = value;
            String s1 = value;
            if (!s1.contains(" LIKE ")) s1 = Arrays.stream(s1.split("\"")).toList().get(0).replaceAll("[a-zA-Z]", "");
            switch (s1.strip()) {
                case ">" -> {
                    decoupled = Arrays.stream(s.split(">")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.GREATER;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case ">=" -> {
                    decoupled = Arrays.stream(s.split(">=")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case "<" -> {
                    decoupled = Arrays.stream(s.split("<")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.LESS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case "<=" -> {
                    decoupled = Arrays.stream(s.split("<=")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case "==" -> {
                    decoupled = Arrays.stream(s.split("==")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.EQUALS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case "!=" -> {
                    decoupled = Arrays.stream(s.split("!=")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.NOT_EQUALS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                case "=" -> {
                    decoupled = Arrays.stream(s.split("=")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.EQUALS;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
                default -> {
                    decoupled = Arrays.stream(s.split("LIKE")).map(String::strip).toList();
                    iComparisonOperator = ComparisonOperators.LIKE;
                    iFieldValueGetter = testGivenArgument(decoupled);
                }
            }
            ConditionalExpression conditionalExpression = new ConditionalExpression(
                    iFieldValueGetter,
                    decoupled.get(1).replace("\"", ""),
                    iComparisonOperator
            );
            listOfQueries.add(conditionalExpression);
        }
        return listOfQueries;
    }

    IFieldValueGetter testGivenArgument(List<String> decoupled) {
        IFieldValueGetter iFieldValueGetter;
        if(Objects.equals(decoupled.get(0).strip(), "firstName")) {
            iFieldValueGetter = FieldValueGetters.FIRST_NAME;
        } else if(Objects.equals(decoupled.get(0).strip(), "lastName")) {
            iFieldValueGetter = FieldValueGetters.LAST_NAME;
        } else if(Objects.equals(decoupled.get(0).strip(), "jmbag")) {
            iFieldValueGetter = FieldValueGetters.JMBAG;
        } else throw new IllegalArgumentException("Given argument doesnt match column in database: " + decoupled.get(0));
        return iFieldValueGetter;
    }
}
