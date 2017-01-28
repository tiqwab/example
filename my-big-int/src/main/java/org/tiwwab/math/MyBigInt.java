package org.tiwwab.math;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * BigInteger
 *
 * - int[] magとint signumで整数を表す
 * - magにはbig-endian方式で値が入る。つまり数字を文字列で表記したときに上部のもの(左側のもの)が配列上先頭に(小さいindexへ)入る。
 * - magの基数は2^32
 * - (Javaのintは32bit, longは64bitと仕様で決められており、処理系には依存しない)
 * - (Javaのprimitiveな整数型は2の補数表現で値が保持される)
 *
 * l495. BigInteger(char[] val, int sign, int len)
 *       一番考えやすいコンストラクタなのでこれをまず見る
 * l515. 渡された値がbitで表して何桁になるか。
 *       渡された数が10進数なら(その桁数 x log10 / log2)の整数値への切り上げで2進数での桁数が求められる。
 *       ここでは何故かbitsPerDigit[10]から(log10 / log2 * 2^10)の値をもらってきて、そのあとに右に10シフトするという動きをしている。
 * l516. 2^Integer.MAX_VALUEより大きくなるならエラーということかな
 * l519. int[]という配列で値を持つが、その配列の大きさはいくつになるか
 * l527. 上位の桁に当たるものをmag[]の末尾にセットする。
 *       (最終的にはbig-endian方式で値を格納するが、まずは末尾にいれ、あとで基数をかけながら先頭に移動していくイメージ)。
 *       Integer.MAX_VALUEは10桁だが、ここでのdigitsPerInt[10]は9。
 * l530. 残りのmag[]にあたるものを入れていく。
 *       このとき、すでにmagに入っているものは基数をかけながら先頭にずれていく。
 *       2^32より大きくなった値は繰り上がりする。
 */
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

    public String repr() {
        return String.format("MyBigInt {mag=%s, signum=%d}", Arrays.toString(this.mag), this.signum);
    }
}
