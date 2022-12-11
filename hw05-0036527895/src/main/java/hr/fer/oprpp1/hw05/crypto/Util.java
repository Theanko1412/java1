package hr.fer.oprpp1.hw05.crypto;

public class Util {

    public static byte[] hexToByte(String keyTest) {
        if(keyTest.length() == 0) return new byte[0];
        else if(keyTest.length() %2 != 0) throw new IllegalArgumentException("Given string is not valid!");


        byte[] byteArray = new byte[keyTest.length()/2];
        for(int i = 0; i < keyTest.length(); i++) {
            if(i%2 != 0) {
                byteArray[i/2] = (byte) Integer.parseInt(keyTest.substring(i-1, i+1), 16);
            }
        }
        return byteArray;
    }

    public static String byteToHex(byte[] byteArray) {
        if(byteArray.length == 0) return String.valueOf("");

        StringBuilder sb = new StringBuilder();
        for(byte each : byteArray) {
            int asNumber = (int) each & 0xff;
            String asHex = Integer.toHexString(asNumber);
            if(asHex.length()%2 == 1) {
                asHex = "0" + asHex;
            }

            sb.append(asHex);
        }
        return sb.toString();
    }
}
