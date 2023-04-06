import hr.fer.zemris.math.Complex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {

    @Test
    void testModule() {
        Complex complex = new Complex(2, 3);

        assertEquals(complex.module(), Math.sqrt(13));
    }

    @Test
    void testMultiply() {
        Complex complex = new Complex(2, 3);

        assertEquals(complex.multiply(new Complex(2, 3)).toString(), new Complex(-5, 12).toString());
    }

    @Test
    void testDevide() {
        Complex complex = new Complex(1, 8);

        assertEquals(complex.divide(new Complex(2, 3)).toString(), new Complex(2, 1).toString());
    }

    @Test
    void testAdd() {
        Complex complex = new Complex(2, 3);

        assertEquals(complex.add(new Complex(2, 3)).toString(), new Complex(4, 6).toString());
    }

    @Test
    void testSub() {
        Complex complex = new Complex(5, 3);

        assertEquals(complex.sub(new Complex(2, 3)).toString(), new Complex(3, 0).toString());
    }

    @Test
    void testNegate() {
        Complex complex = new Complex(2, 3);

        assertEquals(complex.negate().toString(), new Complex(-2, -3).toString());
    }

    @Test
    void testPower() {
        Complex complex = new Complex(2, 3);

        assertEquals(complex.power(4).toString(), new Complex(-119, -120).toString());

    }

    @Test
    void testRoot() {
        Complex complex = new Complex(2, 3);

    }
}
