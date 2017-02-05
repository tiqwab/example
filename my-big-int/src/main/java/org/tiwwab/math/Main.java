package org.tiwwab.math;

import java.math.BigInteger;

/**
 * Created by nm on 2/4/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MyMutableBigInt m1 = new MyMutableBigInt(MyBigInt.valueOf("100000000000"));
        MyMutableBigInt m2 = new MyMutableBigInt(MyBigInt.valueOf("10000000000"));
        m1.divideTest(m2);

        BigInteger b1 = new BigInteger("100000000000");
        b1.divide(new BigInteger("10"));

        System.out.println(Integer.numberOfLeadingZeros(8));
        System.out.println(Long.MIN_VALUE);
        System.out.println(Long.toBinaryString(Long.MIN_VALUE));
        System.out.println(new Integer(0x80000000));
        System.out.println(Integer.MIN_VALUE);
    }

}
