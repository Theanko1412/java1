package hr.fer.oprpp1.hw04.db;

public class ConditionalExpression {

    private IFieldValueGetter strategyField;
    private String literal;
    private IComparisonOperator strategyComparison;

    public ConditionalExpression(IFieldValueGetter strategyField, String literal, IComparisonOperator strategyComparison) {
        this.strategyField = strategyField;
        this.literal = literal;
        this.strategyComparison = strategyComparison;
    }

    public IFieldValueGetter getStrategyField() {
        return strategyField;
    }

    public String getLiteral() {
        return literal;
    }

    public IComparisonOperator getStrategyComparison() {
        return strategyComparison;
    }
}
