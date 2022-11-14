package hr.fer.oprpp1.hw04.db;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComparisonOperators {

    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;

    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;

    public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;

    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;

    public static final IComparisonOperator LIKE = (value1, value2) -> {
        /*Pattern pattern = Pattern.compile(value2);
        Matcher matcher = pattern.matcher(value1);
        return matcher.find();*/
        return value1.matches(value2.strip().replace("*", ".*"));
    };

}
