package org.tiwwab.math;

import java.math.BigInteger;
import java.util.Arrays;

public class MyBigIntOne {

    public static void main(String[] args) throws Exception {
        final String input1 = "12345678901234567890";
        MyBigIntOne mb1 = MyBigIntOne.valueOf(input1, 10); // mag=[-1420514932, -350287150], signum=1
        BigInteger b1 = new BigInteger(input1, 10); // mag=[-1420514932, -350287150], signum=1
        System.out.println();

        final String input2 = "11011011110000100110101010011010101010000110101000";
        MyBigIntOne mb2 = MyBigIntOne.valueOf(input2, 2); // mag=[225033, -1435852376], signum=1
        BigInteger b2 = new BigInteger(input2, 2); // mag=[225033, -1435852376], signum=1
        System.out.println();
    }

    final int[] mag;
    final int signum;

    private static final long LONG_MASK = 0xffffffffL;

    private static final int[] digitsPerInt = { 0, 0, /*two*/ 1, 0, 0, 0, 0, 0, 0, 0, /*ten*/ 1 };
    private static final int[] intRadix = { 0, 0, /*two, 2^1*/ 2, 0, 0, 0, 0, 0, 0, 0, /*ten, 10^1*/ 10 };

    private MyBigIntOne(final int[] mag, final int signum) {
       this.mag = mag;
       this.signum = signum;
    }

    public static MyBigIntOne valueOf(final String val) {
        return valueOf(val, 10);
    }

    public static MyBigIntOne valueOf(final String val, final int radix) {
        final int numDigits = val.length();
        final int numBits = numDigits * ((int) Math.ceil(Math.log(radix) / Math.log(2)));// digitsPerInt[radix];
        final int numWords = (numBits + 31) >>> 5;
        int[] mag = new int[numWords];

        int firstGroupLen = numDigits % digitsPerInt[radix];
        if (firstGroupLen == 0) {
            firstGroupLen = digitsPerInt[radix];
        }
        mag[numWords-1] = Integer.parseInt(val.substring(0, firstGroupLen), radix);

        for (int i = firstGroupLen; i < numDigits; i += digitsPerInt[radix]) {
            int parsedInt = Integer.parseInt(val.substring(i, i+digitsPerInt[radix]), radix);
            destructiveMulAdd(mag, intRadix[radix], parsedInt);
        }

        return new MyBigIntOne(trustedStripLeadingZeroInts(mag), 1);
    }

    private static void destructiveMulAdd(int[] mag, final int radix, final int val) {
        final long radixL = radix & LONG_MASK;
        final long valL = val & LONG_MASK;
        long product = 0;
        long carry = 0;

        for (int i = 0; i < mag.length; i++) {
            product = (mag[mag.length-i-1] & LONG_MASK) * radixL + carry;
            mag[mag.length-i-1] = (int) product;
            carry = product >>> 32;
        }

        long sum = (mag[mag.length-1] & LONG_MASK) + valL;
        mag[mag.length-1] = (int) sum;
        carry = sum >>> 32;
        for (int i = 1; i < mag.length; i++) {
            sum = (mag[mag.length-i-1] & LONG_MASK) + carry;
            mag[mag.length-i-1] = (int) sum;
            carry = sum >>> 32;
        }
    }

    private static int[] trustedStripLeadingZeroInts(final int[] mag) {
        final int magLen = mag.length;
        int start;
        for (start = 0; start < magLen && mag[start] == 0; start++) {}
        return Arrays.copyOfRange(mag, start, magLen);
    }

    public String repr() {
        return String.format("MyBigInt {mag=%s, signum=%d}", Arrays.toString(this.mag), this.signum);
    }
}
