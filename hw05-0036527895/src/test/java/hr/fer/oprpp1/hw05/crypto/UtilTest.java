package hr.fer.oprpp1.hw05.crypto;

import hr.fer.oprpp1.hw05.crypto.Util;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {

    @Test
    void testHexToByte() {
        String test = "01aE22";
        byte[] byteArray = {1, -82, 34};

        assertEquals(Arrays.toString(byteArray), Arrays.toString(Util.hexToByte(test)));
    }


    @Test
    void testByteToHex() {
        String test = "01ae22";
        byte[] byteArray = {1, -82, 34};

        assertEquals(test, Util.byteToHex(byteArray));
    }
}
