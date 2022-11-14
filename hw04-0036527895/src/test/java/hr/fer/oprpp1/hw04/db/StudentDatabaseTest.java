package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StudentDatabaseTest {

    static StudentDatabase sdb;

    @BeforeAll
    static void initializeDatabase() throws IOException {
        sdb = new StudentDatabase(Files.readAllLines(
                Path.of("src/test/resources/prva.txt"),
                StandardCharsets.UTF_8
        ));
    }

    static class IFilterImpl1 implements IFilter {

        @Override
        public boolean accepts(StudentRecord record) {
            return true;
        }
    }

    static class IFilterImpl2 implements IFilter {

        @Override
        public boolean accepts(StudentRecord record) {
            return false;
        }
    }

    @Test
    void testForJMBAG() {
        assertEquals(sdb.forJMBAG("0000000003"),
                new StudentRecord(
                        "0000000003",
                        "Bosnić",
                        "Andrea",
                        4
                ));
    }

    @Test
    void testFilterFalse() {
        assertEquals(new ArrayList<>() ,sdb.filter(new IFilterImpl2()));
    }

    @Test
    void testFilterTrue() {
        assertEquals(sdb.getStudentRecords(), sdb.filter(new IFilterImpl1()));
    }

    @Test
    void testComparisonOperatorLess() {
        IComparisonOperator oper = ComparisonOperators.LESS;
        assertTrue(oper.satisfied("Ana", "Jasna"));
    }


    @Test
    void testComparisonOperatorLike() {
        IComparisonOperator oper = ComparisonOperators.LIKE;
        assertFalse(oper.satisfied("Zagreb", "Aba*"));
        assertFalse(oper.satisfied("AAA", "AA*AA"));
        assertTrue(oper.satisfied("AAAA", "AA*AA"));
    }

    @Test
    void testIfieldValueGetters() {
        StudentRecord record = sdb.forJMBAG("0000000003");
        assertEquals("Bosnić", FieldValueGetters.LAST_NAME.get(record));
        assertEquals("Andrea", FieldValueGetters.FIRST_NAME.get(record));
        assertEquals("0000000003", FieldValueGetters.JMBAG.get(record));
    }

    @Test
    void testConditionalExpression() {
        ConditionalExpression expression = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Bos*",
                ComparisonOperators.LIKE
        );

        StudentRecord record = sdb.forJMBAG("0000000003");

        assertTrue(expression.getStrategyComparison().satisfied(
                expression.getStrategyField().get(record),
                expression.getLiteral()
        ));
    }

    @Test
    void testConditionalExpression2() {
        ConditionalExpression expression = new ConditionalExpression(
                FieldValueGetters.JMBAG,
                "0000000003",
                ComparisonOperators.EQUALS
        );

        StudentRecord record = sdb.forJMBAG("0000000001");

        System.out.println(expression.getStrategyField().get(record) + "|" + expression.getLiteral());

        assertFalse(expression.getStrategyComparison().satisfied(
                expression.getStrategyField().get(record),
                expression.getLiteral()
        ));
    }

    @Test
    void testIsDirectQuery() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");

        assertTrue(qp1.isDirectQuery());
    }

    @Test
    void testGetQueriedJMBAG() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");

        assertEquals("0123456789", qp1.getQueriedJMBAG());
    }

    @Test
    void testQueryParserDirect() {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");

        assertTrue(qp1.isDirectQuery());
        assertEquals(qp1.getQueriedJMBAG(), "0123456789");
        assertEquals(1, qp1.getQuery().size());
    }

    @Test
    void testQueryParserMultiple() {
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");


        assertFalse(qp2.isDirectQuery());
        assertThrows(IllegalArgumentException.class, qp2::getQueriedJMBAG);
        assertEquals(2, qp2.getQuery().size());
    }

    @Test
    void testQueryParserFirstNameGrater() {
        QueryParser qp1 = new QueryParser("lastName LIKE \"J\"");

        for(ConditionalExpression c : qp1.getQuery()) {
            System.out.println(c.toString());
        }
    }

    @Test
    void test() {
        QueryParser parser = new QueryParser("jmbag=\"0000000003\" and lastName>\"L*\"");
        if(parser.isDirectQuery()) {
            StudentRecord r = sdb.forJMBAG(parser.getQueriedJMBAG());
            System.out.println(r);
        } else {
            for(StudentRecord r : sdb.filter(new QueryFilter(parser.getQuery()))) {
                System.out.println(r);
            }
        }
    }
}
