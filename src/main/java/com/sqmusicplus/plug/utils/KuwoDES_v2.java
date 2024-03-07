//package com.sqmusicplus.plug.utils;
//
//import cn.hutool.core.codec.Base64Encoder;
//import cn.hutool.core.util.ArrayUtil;
//import com.sqmusicplus.plug.utils.KuwoDES;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.IntBuffer;
//import java.nio.LongBuffer;
//import java.nio.ShortBuffer;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
///**
// * @author JumpAlang
// * @create 2023-07-18 21:33
// * GITHUB: https://github.com/JumpAlang/Jusic-Serve-Houses
// */
//public final class KuwoDES_v2 {
//    private static final String SECRET_KEY = "ylzsxkwm";
//    private static final int DES_MODE_DECRYPT = 1;
//    private static final int[] arrayE = {
//            31, 0, DES_MODE_DECRYPT, 2, 3, 4, -1, -1, 3, 4, 5, 6, 7, 8, -1, -1, 7, 8, 9, 10, 11, 12, -1, -1, 11, 12, 13, 14,
//            15, 16, -1, -1, 15, 16, 17, 18, 19, 20, -1, -1, 19, 20, 21, 22, 23, 24, -1, -1, 23, 24, 25, 26, 27, 28, -1, -1,
//            27, 28, 29, 30, 31, 30, -1, -1
//    };
//    private static final int[] arrayIP = {
//            57, 49, 41, 33, 25, 17, 9, DES_MODE_DECRYPT, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63,
//            55, 47, 39, 31, 23, 15, 7, 56, 48, 40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20,
//            12, 4, 62, 54, 46, 38, 30, 22, 14, 6
//    };
//    private static final int[] arrayIP_1 = {
//            39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52,
//            20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, DES_MODE_DECRYPT, 41, 9, 49, 17,
//            57, 25, 32, 0, 40, 8, 48, 16, 56, 24
//    };
//    private static final int[] arrayLs = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
//    private static final int[] arrayLsMask = {0, 0x100001, 0x300003};
//
//    private static final long[] arrayMask = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};
//
//
//    private static final int[] arrayP = {
//            15, 6, 19, 20, 28, 11, 27, 16,
//            0, 14, 22, 25, 4, 17, 30, 9,
//            1, 7, 23, 13, 31, 26, 2, 8,
//            18, 12, 29, 5, 21, 10, 3, 24
//    };
//    private static final int[] arrayPC_1 = {
//            56, 48, 40, 32, 24, 16, 8, 0,
//            57, 49, 41, 33, 25, 17, 9, 1,
//            58, 50, 42, 34, 26, 18, 10, 2,
//            59, 51, 43, 35, 62, 54, 46, 38,
//            30, 22, 14, 6, 61, 53, 45, 37,
//            29, 21, 13, 5, 60, 52, 44, 36,
//            28, 20, 12, 4, 27, 19, 11, 3
//    };
//    private static final int[] arrayPC_2 = {
//            13, 16, 10, 23, 0, 4, -1, -1,
//            2, 27, 14, 5, 20, 9, -1, -1,
//            22, 18, 11, 3, 25, 7, -1, -1,
//            15, 6, 26, 19, 12, 1, -1, -1,
//            40, 51, 30, 36, 46, 54, -1, -1,
//            29, 39, 50, 44, 32, 47, -1, -1,
//            43, 48, 38, 55, 33, 52, -1, -1,
//            45, 41, 49, 35, 28, 31, -1, -1
//    };
//    private static final int[][] matrixNSBox = {{
//            14, 4, 3, 15, 2, 13, 5, 3,
//            13, 14, 6, 9, 11, 2, 0, 5,
//            4, 1, 10, 12, 15, 6, 9, 10,
//            1, 8, 12, 7, 8, 11, 7, 0,
//            0, 15, 10, 5, 14, 4, 9, 10,
//            7, 8, 12, 3, 13, 1, 3, 6,
//            15, 12, 6, 11, 2, 9, 5, 0,
//            4, 2, 11, 14, 1, 7, 8, 13,}, {
//            15, 0, 9, 5, 6, 10, 12, 9,
//            8, 7, 2, 12, 3, 13, 5, 2,
//            1, 14, 7, 8, 11, 4, 0, 3,
//            14, 11, 13, 6, 4, 1, 10, 15,
//            3, 13, 12, 11, 15, 3, 6, 0,
//            4, 10, 1, 7, 8, 4, 11, 14,
//            13, 8, 0, 6, 2, 15, 9, 5,
//            7, 1, 10, 12, 14, 2, 5, 9,}, {
//            10, 13, 1, 11, 6, 8, 11, 5,
//            9, 4, 12, 2, 15, 3, 2, 14,
//            0, 6, 13, 1, 3, 15, 4, 10,
//            14, 9, 7, 12, 5, 0, 8, 7,
//            13, 1, 2, 4, 3, 6, 12, 11,
//            0, 13, 5, 14, 6, 8, 15, 2,
//            7, 10, 8, 15, 4, 9, 11, 5,
//            9, 0, 14, 3, 10, 7, 1, 12,}, {
//            7, 10, 1, 15, 0, 12, 11, 5,
//            14, 9, 8, 3, 9, 7, 4, 8,
//            13, 6, 2, 1, 6, 11, 12, 2,
//            3, 0, 5, 14, 10, 13, 15, 4,
//            13, 3, 4, 9, 6, 10, 1, 12,
//            11, 0, 2, 5, 0, 13, 14, 2,
//            8, 15, 7, 4, 15, 1, 10, 7,
//            5, 6, 12, 11, 3, 8, 9, 14,}, {
//            2, 4, 8, 15, 7, 10, 13, 6,
//            4, 1, 3, 12, 11, 7, 14, 0,
//            12, 2, 5, 9, 10, 13, 0, 3,
//            1, 11, 15, 5, 6, 8, 9, 14,
//            14, 11, 5, 6, 4, 1, 3, 10,
//            2, 12, 15, 0, 13, 2, 8, 5,
//            11, 8, 0, 15, 7, 14, 9, 4,
//            12, 7, 10, 9, 1, 13, 6, 3,}, {
//            12, 9, 0, 7, 9, 2, 14, 1,
//            10, 15, 3, 4, 6, 12, 5, 11,
//            1, 14, 13, 0, 2, 8, 7, 13,
//            15, 5, 4, 10, 8, 3, 11, 6,
//            10, 4, 6, 11, 7, 9, 0, 6,
//            4, 2, 13, 1, 9, 15, 3, 8,
//            15, 3, 1, 14, 12, 5, 11, 0,
//            2, 12, 14, 7, 5, 10, 8, 13,}, {
//            4, 1, 3, 10, 15, 12, 5, 0,
//            2, 11, 9, 6, 8, 7, 6, 9,
//            11, 4, 12, 15, 0, 3, 10, 5,
//            14, 13, 7, 8, 13, 14, 1, 2,
//            13, 6, 14, 9, 4, 1, 2, 14,
//            11, 13, 5, 0, 1, 10, 8, 3,
//            0, 11, 3, 5, 9, 4, 15, 2,
//            7, 8, 12, 15, 10, 7, 6, 12,}, {
//            13, 7, 10, 0, 6, 9, 5, 15,
//            8, 4, 3, 10, 11, 14, 12, 5,
//            2, 11, 9, 6, 15, 12, 0, 3,
//            4, 1, 14, 13, 1, 2, 7, 8,
//            1, 2, 12, 15, 10, 4, 0, 3,
//            13, 14, 6, 9, 7, 8, 9, 6,
//            15, 1, 5, 12, 3, 10, 14, 5,
//            8, 7, 11, 0, 4, 13, 2, 11,}
//    };
//
//    private static String csrf = "";
//
//
//
//    public  static  String base64Encrypt(String msg) {
//        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
//        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        byte[] b = encrypt(msgBytes, secretKeyBytes);
//        String s = Base64Encoder.encode(b);
//        return s.replace("\n", "");
//    }
//    public  static  byte[] base64EncryptToBytes(String msg) {
//        byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
//        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//        byte[] b = encrypt(msgBytes, secretKeyBytes);
//        return  b ;
//    }
//
//    public static byte[] encrypt(byte[] msg, byte[] key) {
//        // 处理密钥块
//        long l = 0;
//        for (int i = 0; i < 8; i++) l = l | (long) key[i] << i * 8;
//
//        int j = msg.length / 8;
//        // arrLong1 存放的是转换后的密钥块, 在解密时只需要把这个密钥块反转就行了
//        long[] arrLong1 = new long[16];
//        subKeys(l, arrLong1, 0);
//
//        // arrLong2 存放的是前部分的明文
//        long[] arrLong2 = new long[j];
//        for (int m = 0; m < j; m++)
//            for (int n = 0; n < 8; n++)
//                arrLong2[m] |= (long) msg[n + m * 8] << n * 8;
//
//        // 用于存放密文
//        long[] arrLong3 = new long[(1 + 8 * (j + 1)) / 8];
//        // 计算前部的数据块(除了最后一部分)
//        for (int i1 = 0; i1 < j; i1++)
//            arrLong3[i1] = DES64(arrLong1, arrLong2[i1]);
//
//        // 保存多出来的字节
//        byte[] arrByte1 = Arrays.copyOfRange(msg, j * 8, msg.length);
//        long l2 = 0;
//        for (int i1 = 0, msgLen = msg.length; i1 < msgLen % 8; i1++)
//            l2 |= (long) arrByte1[i1] << i1 * 8;
//        // 计算多出的那一位(最后一位)
//        arrLong3[j] = DES64(arrLong1, l2);
//
//        // 将密文转为字节型
//        byte[] arrByte2 = new byte[8 * arrLong3.length];
//        int i4 = 0;
//        for (long l3 : arrLong3)
//            for (int i6 = 0; i6 < 8; i6++)
//                arrByte2[i4++] = (byte) (255 & l3 >> i6 * 8);
//
//        return arrByte2;
//    }
//
//    private static long DES64(long[] longs, long l) {
//        long out;
//        int SOut;
//        long[] pR = new long[8];
//        long[] pSource = new long[2];
//        long L;
//        long R;
//        out = bitTransform(arrayIP, 64, l);
//        pSource[0] = 0xFFFFFFFFL & out;
//        pSource[1] = (-4294967296L & out) >> 32;
//        for (int i = 0; i < 16; i++) {
//            R = pSource[1];
//            R = bitTransform(arrayE, 64, R);
//            R ^= longs[i];
//            for (int j = 0; j < 8; j++)
//                pR[j] = 255 & R >> j * 8;
//            SOut = 0;
//            for (int sbi = 7; sbi > -1; sbi--) {
//                SOut <<= 4;
//                SOut |= matrixNSBox[sbi][(int) pR[sbi]];
//            }
//
//            R = bitTransform(arrayP, 32, SOut);
//            L = pSource[0];
//            pSource[0] = pSource[1];
//            pSource[1] = L ^ R;
//        }
//        ArrayUtil.reverse(pSource);
//        out = -4294967296L & pSource[1] << 32 | 0xFFFFFFFFL & pSource[0];
//        out = bitTransform(arrayIP_1, 64, out);
//        return out;
//    }
//
//    private static void subKeys(long l, long[] longs, int n) {
//        long l2 = bitTransform(arrayPC_1, 56, l);
//        for (int i = 0; i < 16; i++) {
//            l2 = (l2 & arrayLsMask[arrayLs[i]]) << 28 - arrayLs[i] | (l2 & ~arrayLsMask[arrayLs[i]]) >> arrayLs[i];
//            longs[i] = bitTransform(arrayPC_2, 64, l2);
//        }
//        int j = 0;
//        while (n == 1 && j < 8) {
//            long t = longs[j];
//            longs[j] = longs[15 - j];
//            longs[15 - j] = t;
//            j++;
//        }
//    }
//
//    private static long bitTransform(int[] arrInt, int n, long l) {
//        long l2 = 0;
//        for (int i = 0; i < n; i++) {
//            if (arrInt[i] < 0 || (l & arrayMask[arrInt[i]]) == 0) continue;
//            l2 |= arrayMask[i];
//        }
//        return l2;
//    }
//
//}
