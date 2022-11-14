package hr.fer.oprpp1.hw04.db;

import java.util.List;

public class QueryFilter implements IFilter{

    List<ConditionalExpression> list;

    @Override
    public boolean accepts(StudentRecord record) {
        return false;
    }

    public QueryFilter(List<ConditionalExpression> list) {
        this.list = list;
    }
}
