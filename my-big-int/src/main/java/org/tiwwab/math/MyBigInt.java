package org.tiwwab.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.PrimitiveIterator;

public class MyBigInt {

    public static void main(String[] args) throws Exception {
        // radix 10
        final String input1 = "112233445566778899001234567890987865432177737261529472618";
        MyBigInt mb1 = MyBigInt.valueOf(input1, 10);
        System.out.println(mb1.repr());
        BigInteger b1 = new BigInteger(input1); // mag = [76793239, -1261205781, 1514226581, -488063686, 1376914250, -1087332758]

        // radix 2
        final String input2 = "0010011010100010100101010101010100000100101010101010101010110101010100000001011111111111110010101000000";
        MyBigInt mb2 = MyBigInt.valueOf(input2, 2);
        System.out.println(mb2.repr());
        BigInteger b2 = new BigInteger(input2, 2); // mag = [19, 1363847810, 1431657128, 201319744]

        // This is why LONG_MASK is necessary
        int a = -2;
        System.out.println("-2 as binary         : " + Integer.toBinaryString(a));
        System.out.println("cast to long         : " + Long.toBinaryString((long) a));
        System.out.println("LONG_MASK as binary  : " + Long.toBinaryString(LONG_MASK));
        System.out.println("cast to long and mask: " + Long.toBinaryString(a & LONG_MASK));

        System.out.println();

        MyBigInt mb3 = MyBigInt.valueOf("1000000000000000", 10);
        MyBigInt mb4 = MyBigInt.valueOf("2000020000200000", 10);
        System.out.println(mb3.add(mb4).repr());

        BigInteger b4 = new BigInteger("1000000000000000");
        BigInteger b5 = new BigInteger("2000020000200000");
        BigInteger b6 = b4.add(b5); // mag=[698496, -1771154112]
        System.out.println(b6);
        System.out.println(Integer.toString(Integer.MAX_VALUE));

        System.out.println();

        // add
        MyBigInt mb5 = MyBigInt.valueOf("2147483647", 10);
        for (int i = 0; i < 4; i++) {
            mb5 = mb5.add(MyBigInt.valueOf("2147483647", 10));
        }
        System.out.println(mb5.repr());

        BigInteger b7 = new BigInteger("2147483647");
        for (int i = 0; i < 4; i++) {
            b7 = b7.add(new BigInteger("2147483647"));
        }
        System.out.println(b7); // mag=[2, 2147483643]

        System.out.println();

        // subtract
        System.out.println(mb4.subtract(mb3).repr());
        BigInteger b8 = b5.subtract(b4);
        System.out.println(b8); // mag=[232835, 1289835840], signum=1

        System.out.println(mb3.subtract(mb4).repr());
        BigInteger b9 = b4.subtract(b5);
        System.out.println(b9); // mag=[232835, 1289835840], signum=-1

        for (int i = 0; i < 4; i++) {
            mb5 = mb5.subtract(MyBigInt.valueOf("2147483647", 10));
        }
        System.out.println(mb5.repr());

        for (int i = 0; i < 4; i++) {
            b7 = b7.subtract(new BigInteger("2147483647"));
        }
        System.out.println(b7); // mag=[2147483647], signum=1

        System.out.println();

        // multiply
        MyBigInt mb6 = MyBigInt.valueOf("2147483647").multiply(MyBigInt.valueOf("2147483648")
                                                     .multiply(MyBigInt.valueOf("3729467293"))
                                                     .multiply(MyBigInt.valueOf("999231111234847372")));
        System.out.println(mb6.repr());

        BigInteger b10 = new BigInteger("2147483647").multiply(new BigInteger("2147483648")
                                                          .multiply(new BigInteger("3729467293"))
                                                          .multiply(new BigInteger("999231111234847372")));
        System.out.println(b10); // mag=[50504844, 1092009086, -1270796856, 2064485906, 0]
    }

    final int[] mag;
    final int signum;

    private static final long LONG_MASK = 0xffffffffL;

    private static final int[] digitsPerInt = { 0, 0, /*two*/ 30, 0, 0, 0, 0, 0, 0, 0, /*ten*/ 9 };
    private static final int[] intRadix = { 0, 0, /*two, 2^30*/ 0x40000000, 0, 0, 0, 0, 0, 0, 0, /*ten, 10^9*/ 1000000000 };

    private MyBigInt(final int[] mag, final int signum) {
       this.mag = mag;
       this.signum = signum;
    }

    public static MyBigInt valueOf(final String val) {
        return valueOf(val, 10);
    }

    public static MyBigInt valueOf(final String val, final int radix) {
        final int numDigits = val.length();
        final int numBits = numDigits * digitsPerInt[radix];
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

        return new MyBigInt(trustedStripLeadingZeroInts(mag), 1);
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

    private int compareMagnitude(int[] other) {
        final int[] m1 = this.mag;
        final int len1 = m1.length;
        final int[] m2 = other;
        final int len2 = m2.length;

        if (len1 > len2) {
            return 1;
        }
        if (len1 < len2) {
            return -1;
        }

        for (int i = 0; i < len1; i++) {
            final int x1 = m1[i];
            final int x2 = m2[i];
            if (x1 != x2) {
                return (x1 & LONG_MASK) > (x2 & LONG_MASK) ? 1 : -1;
            }
        }

        return 0;
    }

    private int[] add(int[] x, int[] y) {
        if (x.length < y.length) {
            int[] tmp = x;
            x = y;
            y = tmp;
        }

        int[] result = new int[x.length+1];
        long sum = 0;
        long carry = 0;
        int cursor = 0;

        while (cursor < y.length) {
            sum = (x[x.length-cursor-1] & LONG_MASK) + (y[y.length-cursor-1] & LONG_MASK) + carry;
            result[result.length-cursor-1] = (int) sum;
            carry = sum >>> 32;
            cursor++;
        }

        while (cursor < x.length && carry != 0) {
            sum = (x[x.length-cursor-1] & LONG_MASK) + carry;
            result[result.length-cursor-1] = (int) sum;
            carry = sum >>> 32;
            cursor++;
        }

        while (cursor < x.length) {
            result[result.length-cursor-1] = x[x.length-cursor-1];
            cursor++;
        }

        if (carry > 0) {
            result[result.length-cursor-1] = (int) carry;
            cursor++;
        }

        return trustedStripLeadingZeroInts(result);
    }

    public MyBigInt add(MyBigInt val) {
        if (this.signum == 0) {
            return val;
        }
        if (val.signum == 0) {
            return this;
        }

        if (this.signum == val.signum) {
            return new MyBigInt(add(this.mag, val.mag), this.signum);
        }

        int cmp = compareMagnitude(val.mag);
        if (cmp == 0) {
            return new MyBigInt(new int[0], 0);
        }
        int[] newMag = (cmp > 0) ? subtract(this.mag, val.mag) : subtract(val.mag, this.mag);
        newMag = trustedStripLeadingZeroInts(newMag);
        return (cmp == this.signum) ? new MyBigInt(newMag, 1) : new MyBigInt(newMag, -1);
    }

    /**
     * <p>
     * Subtract y from x, assuming that x is bigger than y.
     * @param x
     * @param y
     * @return
     */
    private int[] subtract(int[] x, int[] y) {
        // allocate a new mag with same length as x
        int[] result = new int[x.length];

        // subtract each element
        long sub = 0;
        long carry = 0;
        int cursor = 0;
        while (cursor < y.length) {
            sub = (x[x.length-cursor-1] & LONG_MASK) - (y[y.length-cursor-1] & LONG_MASK) + carry;
            result[result.length-cursor-1] = (int) sub;
            carry = sub >>> 32;
            cursor++;
        }

        // handle carry
        while (carry != 0 && cursor < x.length) {
            sub = (x[x.length-cursor-1] & LONG_MASK) + carry;
            result[result.length-cursor-1] = (int) sub;
            carry = sub >>> 32;
            cursor++;
        }

        while (cursor < x.length) {
            result[result.length-cursor-1] = x[x.length-cursor-1];
            cursor++;
        }

        result = trustedStripLeadingZeroInts(result);
        return result;
    }

    public MyBigInt subtract(MyBigInt val) {
        if (this.signum == 0) {
            return val.negate();
        }
        if (val.signum == 0) {
            return this;
        }

        if (this.signum != val.signum) {
            final int[] newMag = add(this.mag, val.mag);
            return new MyBigInt(newMag, this.signum);
        }

        int cmp = compareMagnitude(val.mag);
        if (cmp == 0) {
            return new MyBigInt(new int[0], 0);
        }
        int[] newMag = (cmp > 0) ? subtract(this.mag, val.mag) : subtract(val.mag, this.mag);
        newMag = trustedStripLeadingZeroInts(newMag);
        return cmp == this.signum ? new MyBigInt(newMag, 1) : new MyBigInt(newMag, -1);
    }

    private int[] multiply(int[] bigger, int[] smaller) {
        int[] result = new int[bigger.length + smaller.length];

        long mult = 0;
        long carry = 0;
        for (int s = 0; s < smaller.length; s++) {
            for (int b = 0; b < bigger.length; b++) {
                mult = (smaller[smaller.length-s-1] & LONG_MASK) * (bigger[bigger.length-b-1] & LONG_MASK) + carry;
                result[result.length - 1 - b - s] = (int) ((result[result.length - 1 - b - s] & LONG_MASK) + mult);
                carry = mult >>> 32;
            }
            int cursor = 0;
            while (carry != 0) {
                mult = (result[result.length - 1 - bigger.length - s - cursor] & LONG_MASK) + carry;
                result[result.length - 1 - bigger.length - s] = (int) mult;
                carry = mult >>> 32;
                cursor++;
            }
        }

        result = trustedStripLeadingZeroInts(result);
        return result;
    }

    public MyBigInt multiply(MyBigInt val) {
        // return zero if either of MyBigInt is zero
        if (this.signum == 0 || val.signum == 0) {
            return new MyBigInt(new int[0], 0);
        }

        // compare this and val
        int cmp = compareMagnitude(val.mag);

        // calc new mag
        int[] newMag = (cmp > 0) ? multiply(this.mag, val.mag) : multiply(val.mag, this.mag);

        return (this.signum == val.signum) ? new MyBigInt(newMag, 1) : new MyBigInt(newMag, -1);
    }

    /*
    public String toString(int radix) {
        if (signum == 0)
            return "0";
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10;
        return smallToString(radix);
    }

    private String smallToString(int radix) {
        if (signum == 0) {
            return "0";
        }

        // Compute upper bound on number of digit groups and allocate space
        int maxNumDigitGroups = (4*mag.length + 6)/7;
        String digitGroup[] = new String[maxNumDigitGroups];

        // Translate number to string, a digit group at a time
        BigInteger tmp = this.abs();
        int numGroups = 0;
        while (tmp.signum != 0) {
            BigInteger d = longRadix[radix];

            MutableBigInteger q = new MutableBigInteger(),
                              a = new MutableBigInteger(tmp.mag),
                              b = new MutableBigInteger(d.mag);
            MutableBigInteger r = a.divide(b, q);
            BigInteger q2 = q.toBigInteger(tmp.signum * d.signum);
            BigInteger r2 = r.toBigInteger(tmp.signum * d.signum);

            digitGroup[numGroups++] = Long.toString(r2.longValue(), radix);
            tmp = q2;
        }

        // Put sign (if any) and first digit group into result buffer
        StringBuilder buf = new StringBuilder(numGroups*digitsPerLong[radix]+1);
        if (signum < 0) {
            buf.append('-');
        }
        buf.append(digitGroup[numGroups-1]);

        // Append remaining digit groups padded with leading zeros
        for (int i=numGroups-2; i >= 0; i--) {
            // Prepend (any) leading zeros for this digit group
            int numLeadingZeros = digitsPerLong[radix]-digitGroup[i].length();
            if (numLeadingZeros != 0) {
                buf.append(zeros[numLeadingZeros]);
            }
            buf.append(digitGroup[i]);
        }
        return buf.toString();
    }
    */

    public MyBigInt negate() {
        return new MyBigInt(this.mag, signum * -1);
    }

    public String repr() {
        return String.format("MyBigInt {mag=%s, signum=%d}", Arrays.toString(this.mag), this.signum);
    }
}
