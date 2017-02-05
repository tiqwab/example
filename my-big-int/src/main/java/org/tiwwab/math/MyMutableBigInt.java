package org.tiwwab.math;

import java.util.Arrays;
import static org.tiwwab.math.MyBigInt.LONG_MASK;

/**
 * Created by nm on 2/4/17.
 */
public class MyMutableBigInt {

    int[] value;
    int intLen;
    int offset = 0;

    MyMutableBigInt() {
        value = new int[1];
        intLen = 0;
    }

    MyMutableBigInt(int val) {
        value = new int[1];
        intLen = 1;
        value[0] = val;
    }

    MyMutableBigInt(int[] val) {
        value = val;
        intLen = val.length;
    }

    MyMutableBigInt(MyBigInt b) {
        intLen = b.mag.length;
        value = Arrays.copyOf(b.mag, intLen);
    }

    final void normalize() {
        if (intLen == 0) {
            offset = 0;
            return;
        }

        int index = offset;
        if (value[index] != 0)
            return;

        int indexBound = index+intLen;
        do {
            index++;
        } while(index < indexBound && value[index] == 0);

        int numZeros = index - offset;
        intLen -= numZeros;
        offset = (intLen == 0 ?  0 : offset+numZeros);
    }

    private static void copyAndShift(int[] src, int srcFrom, int srcLen, int[] dst, int dstFrom, int shift) {
        int n2 = 32 - shift;
        int c=src[srcFrom];
        for (int i=0; i < srcLen-1; i++) {
            int b = c;
            c = src[++srcFrom];
            dst[dstFrom+i] = (b << shift) | (c >>> n2);
        }
        dst[dstFrom+srcLen-1] = c << shift;
    }

    public void divideTest(MyMutableBigInt val) {
        MyMutableBigInt q = new MyMutableBigInt(),
                a = new MyMutableBigInt(this.value),
                b = new MyMutableBigInt(val.value);

        a.divideMagnitude(b, q, false);
    }

    public MyMutableBigInt divideMagnitude(MyMutableBigInt div,
                                              MyMutableBigInt quotient,
                                              boolean needRemainder ) {
        // assert div.intLen > 1
        // D1 normalize the divisor
        int shift = Integer.numberOfLeadingZeros(div.value[div.offset]);
        // Copy divisor value to protect divisor
        final int dlen = div.intLen;
        int[] divisor;
        MyMutableBigInt rem; // Remainder starts as dividend with space for a leading zero
        if (shift > 0) { // shift == 0 ならば div.value[div.offset] >= 2^31, つまり b/2より大きくなる
            // divisorにdivとshiftを考慮した値をかけてコピー
            // remにも被除数とshiftを考慮した値をかける計算をし、remに再代入
            divisor = new int[dlen];
            copyAndShift(div.value,div.offset,dlen,divisor,0,shift);
            if (Integer.numberOfLeadingZeros(value[offset]) >= shift) { // 被除数にも2^shiftをかける。桁数は変わらない
                int[] remarr = new int[intLen + 1];
                rem = new MyMutableBigInt(remarr);
                rem.intLen = intLen;
                rem.offset = 1;
                copyAndShift(value,offset,intLen,remarr,1,shift);
            } else { // かけた結果、桁が1つ増える
                int[] remarr = new int[intLen + 2];
                rem = new MyMutableBigInt(remarr);
                rem.intLen = intLen+1;
                rem.offset = 1;
                int rFrom = offset;
                int c=0;
                int n2 = 32 - shift;
                for (int i=1; i < intLen+1; i++,rFrom++) {
                    int b = c;
                    c = value[rFrom];
                    remarr[i] = (b << shift) | (c >>> n2);
                }
                remarr[intLen+1] = c << shift;
            }
        } else {
            // divisorは単純にdivのコピー
            // rem.value[0]は空けて被除数の値を1からコピーしている
            divisor = Arrays.copyOfRange(div.value, div.offset, div.offset + div.intLen);
            rem = new MyMutableBigInt(new int[intLen + 1]);
            System.arraycopy(value, offset, rem.value, 1, intLen);
            rem.intLen = intLen;
            rem.offset = 1;
        }

        // ここから使用するのはremとdivisor? divは出てこなそう

        int nlen = rem.intLen;

        // Set the quotient size
        final int limit = nlen - dlen + 1;
        if (quotient.value.length < limit) {
            quotient.value = new int[limit];
            quotient.offset = 0;
        }
        quotient.intLen = limit;
        int[] q = quotient.value;


        // Must insert leading 0 in rem if its length did not change
        // 常にtrueにならない？
        if (rem.intLen == nlen) {
            rem.offset = 0;
            rem.value[0] = 0;
            rem.intLen++;
        }

        int dh = divisor[0];
        long dhLong = dh & LONG_MASK;
        int dl = divisor[1];

        // D2 Initialize j
        for (int j=0; j < limit-1; j++) {
            // D3 Calculate qhat
            // estimate qhat
            int qhat = 0;
            int qrem = 0;
            boolean skipCorrection = false;
            int nh = rem.value[j+rem.offset];
            int nh2 = nh + 0x80000000;
            int nm = rem.value[j+1+rem.offset];

            if (nh == dh) { // qhatがb-1になる場合 本の(2)式
                qhat = ~0;
                qrem = nh + nm; // この2行はなぞ。qrem=nmで良い気がするのに
                skipCorrection = qrem + 0x80000000 < nh2;
            } else {
                long nChunk = (((long)nh) << 32) | (nm & LONG_MASK);
                if (nChunk >= 0) {
                    qhat = (int) (nChunk / dhLong);
                    qrem = (int) (nChunk - (qhat * dhLong));
                } else {
                    long tmp = divWord(nChunk, dh);
                    qhat = (int) (tmp & LONG_MASK);
                    qrem = (int) (tmp >>> 32);
                }
            }

            if (qhat == 0)
                continue;

            if (!skipCorrection) { // Correct qhat
                long nl = rem.value[j+2+rem.offset] & LONG_MASK;
                long rs = ((qrem & LONG_MASK) << 32) | nl;
                long estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);

                if (unsignedLongCompare(estProduct, rs)) { // qhat >= b のような条件が抜けているのは、dh > (2^31)で条件が満たされないことが確定だから？
                    qhat--;
                    qrem = (int)((qrem & LONG_MASK) + dhLong);
                    if ((qrem & LONG_MASK) >=  dhLong) { // ここは本ではqremが基数bより小さいことを見る場所だと思うけど...? ->
                        estProduct -= (dl & LONG_MASK);
                        rs = ((qrem & LONG_MASK) << 32) | nl;
                        if (unsignedLongCompare(estProduct, rs))
                            qhat--;
                    }
                }
            }

            // D4 Multiply and subtract
            rem.value[j+rem.offset] = 0;
            int borrow = mulsub(rem.value, divisor, qhat, dlen, j+rem.offset);

            // D5 Test remainder
            if (borrow + 0x80000000 > nh2) { // なぞの条件 0x80000000はInteger.MIN_VALUEと同じ 筆算的に考えると最上位がnh2-borrowになるので、それが負になるなら足し戻す必要があるということかな
                // D6 Add back
                divadd(divisor, rem.value, j+1+rem.offset);
                qhat--;
            }

            // Store the quotient digit
            q[j] = qhat;
        } // D7 loop on j
        // D3 Calculate qhat
        // estimate qhat
        int qhat = 0;
        int qrem = 0;
        boolean skipCorrection = false;
        int nh = rem.value[limit - 1 + rem.offset];
        int nh2 = nh + 0x80000000;
        int nm = rem.value[limit + rem.offset];

        if (nh == dh) {
            qhat = ~0;
            qrem = nh + nm;
            skipCorrection = qrem + 0x80000000 < nh2;
        } else {
            long nChunk = (((long) nh) << 32) | (nm & LONG_MASK);
            if (nChunk >= 0) {
                qhat = (int) (nChunk / dhLong);
                qrem = (int) (nChunk - (qhat * dhLong));
            } else {
                long tmp = divWord(nChunk, dh);
                qhat = (int) (tmp & LONG_MASK);
                qrem = (int) (tmp >>> 32);
            }
        }
        if (qhat != 0) {
            if (!skipCorrection) { // Correct qhat
                long nl = rem.value[limit + 1 + rem.offset] & LONG_MASK;
                long rs = ((qrem & LONG_MASK) << 32) | nl;
                long estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);

                if (unsignedLongCompare(estProduct, rs)) {
                    qhat--;
                    qrem = (int) ((qrem & LONG_MASK) + dhLong);
                    if ((qrem & LONG_MASK) >= dhLong) {
                        estProduct -= (dl & LONG_MASK);
                        rs = ((qrem & LONG_MASK) << 32) | nl;
                        if (unsignedLongCompare(estProduct, rs))
                            qhat--;
                    }
                }
            }


            // D4 Multiply and subtract
            int borrow;
            rem.value[limit - 1 + rem.offset] = 0;
            if(needRemainder)
                borrow = mulsub(rem.value, divisor, qhat, dlen, limit - 1 + rem.offset);
            else
                borrow = mulsubBorrow(rem.value, divisor, qhat, dlen, limit - 1 + rem.offset);

            // D5 Test remainder
            if (borrow + 0x80000000 > nh2) {
                // D6 Add back
                if(needRemainder)
                    divadd(divisor, rem.value, limit - 1 + 1 + rem.offset);
                qhat--;
            }

            // Store the quotient digit
            q[(limit - 1)] = qhat;
        }


        if (needRemainder) {
            // D8 Unnormalize
            if (shift > 0)
                // rem.rightShift(shift);
            rem.normalize();
        }
        quotient.normalize();
        return needRemainder ? rem : null;
    }

    /**
     * This method is used for division of an n word dividend by a one word
     * divisor. The quotient is placed into quotient. The one word divisor is
     * specified by divisor.
     *
     * @return the remainder of the division is returned.
     *
     */
    int divideOneWord(int divisor, MyMutableBigInt quotient) {
        long divisorLong = divisor & LONG_MASK;

        // Special case of one word dividend
        if (intLen == 1) {
            long dividendValue = value[offset] & LONG_MASK;
            int q = (int) (dividendValue / divisorLong);
            int r = (int) (dividendValue - q * divisorLong);
            quotient.value[0] = q;
            quotient.intLen = (q == 0) ? 0 : 1;
            quotient.offset = 0;
            return r;
        }

        if (quotient.value.length < intLen)
            quotient.value = new int[intLen];
        quotient.offset = 0;
        quotient.intLen = intLen;

        // Normalize the divisor
        // なぜ正規化がいるのかはなぞ
        int shift = Integer.numberOfLeadingZeros(divisor);

        // 最上位の桁の計算
        int rem = value[offset];
        long remLong = rem & LONG_MASK;
        if (remLong < divisorLong) {
            quotient.value[0] = 0;
        } else {
            quotient.value[0] = (int)(remLong / divisorLong);
            rem = (int) (remLong - (quotient.value[0] * divisorLong));
            remLong = rem & LONG_MASK;
        }
        // 残りの桁の計算
        // 各桁の計算時、筆算のように一つ上の計算時のrem(剰余)を持ってきて一つ上の桁におく
        // つまりこの場合remLong << 32をもともとの被除数に足す
        int xlen = intLen;
        while (--xlen > 0) {
            long dividendEstimate = (remLong << 32) |
                    (value[offset + intLen - xlen] & LONG_MASK);
            int q;
            if (dividendEstimate >= 0) { // 最上位の桁を使用していなければ、単純に除算できる
                q = (int) (dividendEstimate / divisorLong);
                rem = (int) (dividendEstimate - q * divisorLong);
            } else { // 最上位の桁を使用しているならば、divWordで計算する
                long tmp = divWord(dividendEstimate, divisor);
                q = (int) (tmp & LONG_MASK);
                rem = (int) (tmp >>> 32);
            }
            quotient.value[intLen - xlen] = q;
            remLong = rem & LONG_MASK;
        }

        quotient.normalize();
        // Unnormalize
        if (shift > 0)
            return rem % divisor;
        else
            return rem;
    }

    /**
     * This method divides a long quantity by an int to estimate
     * qhat for two multi precision numbers. It is used when
     * the signed value of n is less than zero.
     * Returns long value where high 32 bits contain remainder value and
     * low 32 bits contain quotient value.
     */
    static long divWord(long n, int d) {
        long dLong = d & LONG_MASK;
        long r;
        long q;
        if (dLong == 1) {
            q = (int)n;
            r = 0;
            return (r << 32) | (q & LONG_MASK);
        }

        // Approximate the quotient and remainder
        q = (n >>> 1) / (dLong >>> 1);
        r = n - q*dLong;

        // Correct the approximation
        while (r < 0) {
            r += dLong;
            q--;
        }
        while (r >= dLong) {
            r -= dLong;
            q++;
        }
        // n - q*dlong == r && 0 <= r <dLong, hence we're done.
        return (r << 32) | (q & LONG_MASK);
    }

    private boolean unsignedLongCompare(long one, long two) {
        return (one+Long.MIN_VALUE) > (two+Long.MIN_VALUE);
    }

    /**
     * This method is used for division. It multiplies an n word input a by one
     * word input x, and subtracts the n word product from q. This is needed
     * when subtracting qhat*divisor from dividend.
     */
    private int mulsub(int[] q, int[] a, int x, int len, int offset) {
        long xLong = x & LONG_MASK;
        long carry = 0;
        offset += len;

        for (int j=len-1; j >= 0; j--) {
            long product = (a[j] & LONG_MASK) * xLong + carry;
            long difference = q[offset] - product;
            q[offset--] = (int)difference;
            carry = (product >>> 32)
                    + (((difference & LONG_MASK) >
                    (((~(int)product) & LONG_MASK))) ? 1:0); // 3項演算子部分はdifferenceの計算時に、左の桁から借りてくるような場合
        }
        return (int)carry;
    }

    private int mulsubBorrow(int[] q, int[] a, int x, int len, int offset) {
        long xLong = x & LONG_MASK;
        long carry = 0;
        offset += len;
        for (int j=len-1; j >= 0; j--) {
            long product = (a[j] & LONG_MASK) * xLong + carry;
            long difference = q[offset--] - product;
            carry = (product >>> 32)
                    + (((difference & LONG_MASK) >
                    (((~(int)product) & LONG_MASK))) ? 1:0);
        }
        return (int)carry;
    }

    private int divadd(int[] a, int[] result, int offset) {
        long carry = 0;

        for (int j=a.length-1; j >= 0; j--) {
            long sum = (a[j] & LONG_MASK) +
                    (result[j+offset] & LONG_MASK) + carry;
            result[j+offset] = (int)sum;
            carry = sum >>> 32;
        }
        return (int)carry;
    }

}
