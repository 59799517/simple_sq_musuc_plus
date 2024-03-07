//package com.sqmusicplus.plug.utils;
//
//public final class KuwoDES_zj {
//
//    public static final long ACTION_SET_SHUFFLE_MODE = 2097152;
//    public static final char WHITESPACE_CARRIAGE_RETURN = '\r';
//    public static final long f61782b = 4194304;
//
//    public static final long AV_CH_STEREO_LEFT = 536870912;
//
//    public static final long AV_CH_STEREO_RIGHT = 1073741824;
//
//    public static final long AV_CH_WIDE_LEFT = 2147483648L;
//
//    public static final long AV_CH_WIDE_RIGHT = 4294967296L;
//
//    public static final long AV_CH_SURROUND_DIRECT_LEFT = 8589934592L;
//    public static final long AV_CH_SURROUND_DIRECT_RIGHT = 17179869184L;
//    public static final long AV_CH_LOW_FREQUENCY_2 = 34359738368L;
//
//
//
//
//    /* renamed from: n */
//    private static long f4069n;
//
//    /* renamed from: o */
//    private static long f4070o;
//
//    /* renamed from: r */
//    private static int f4073r;
//
//    /* renamed from: s */
//    private static int f4074s;
//
//    /* renamed from: t */
//    private static int f4075t;
//
//    /* renamed from: a */
//    public static final byte[] f4056a = "ylzsxkwm".getBytes();
//
//    /* renamed from: b */
//    public static final int f4057b = f4056a.length;
//
//    /* renamed from: c */
//    private static final long[] f4058c = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, ACTION_SET_SHUFFLE_MODE, f61782b, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, AV_CH_STEREO_LEFT, AV_CH_STEREO_RIGHT, AV_CH_WIDE_LEFT, AV_CH_WIDE_RIGHT, AV_CH_SURROUND_DIRECT_LEFT, AV_CH_SURROUND_DIRECT_RIGHT, AV_CH_LOW_FREQUENCY_2, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};
//
//    /* renamed from: d */
//    private static final int[] f4059d = {57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7, 56, 48, 40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6};
//
//    /* renamed from: e */
//    private static final int[] f4060e = {31, 0, 1, 2, 3, 4, -1, -1, 3, 4, 5, 6, 7, 8, -1, -1, 7, 8, 9, 10, 11, 12, -1, -1, 11, 12, 13, 14, 15, 16, -1, -1, 15, 16, 17, 18, 19, 20, -1, -1, 19, 20, 21, 22, 23, 24, -1, -1, 23, 24, 25, 26, 27, 28, -1, -1, 27, 28, 29, 30, 31, 30, -1, -1};
//
//    /* renamed from: f */
//    private static final char[][] f4061f = {new char[]{14, 4, 3, 15, 2, WHITESPACE_CARRIAGE_RETURN, 5, 3, WHITESPACE_CARRIAGE_RETURN, 14, 6, '\t', 11, 2, 0, 5, 4, 1, '\n', '\f', 15, 6, '\t', '\n', 1, '\b', '\f', 7, '\b', 11, 7, 0, 0, 15, '\n', 5, 14, 4, '\t', '\n', 7, '\b', '\f', 3, WHITESPACE_CARRIAGE_RETURN, 1, 3, 6, 15, '\f', 6, 11, 2, '\t', 5, 0, 4, 2, 11, 14, 1, 7, '\b', WHITESPACE_CARRIAGE_RETURN}, new char[]{15, 0, '\t', 5, 6, '\n', '\f', '\t', '\b', 7, 2, '\f', 3, WHITESPACE_CARRIAGE_RETURN, 5, 2, 1, 14, 7, '\b', 11, 4, 0, 3, 14, 11, WHITESPACE_CARRIAGE_RETURN, 6, 4, 1, '\n', 15, 3, WHITESPACE_CARRIAGE_RETURN, '\f', 11, 15, 3, 6, 0, 4, '\n', 1, 7, '\b', 4, 11, 14, WHITESPACE_CARRIAGE_RETURN, '\b', 0, 6, 2, 15, '\t', 5, 7, 1, '\n', '\f', 14, 2, 5, '\t'}, new char[]{'\n', WHITESPACE_CARRIAGE_RETURN, 1, 11, 6, '\b', 11, 5, '\t', 4, '\f', 2, 15, 3, 2, 14, 0, 6, WHITESPACE_CARRIAGE_RETURN, 1, 3, 15, 4, '\n', 14, '\t', 7, '\f', 5, 0, '\b', 7, WHITESPACE_CARRIAGE_RETURN, 1, 2, 4, 3, 6, '\f', 11, 0, WHITESPACE_CARRIAGE_RETURN, 5, 14, 6, '\b', 15, 2, 7, '\n', '\b', 15, 4, '\t', 11, 5, '\t', 0, 14, 3, '\n', 7, 1, '\f'}, new char[]{7, '\n', 1, 15, 0, '\f', 11, 5, 14, '\t', '\b', 3, '\t', 7, 4, '\b', WHITESPACE_CARRIAGE_RETURN, 6, 2, 1, 6, 11, '\f', 2, 3, 0, 5, 14, '\n', WHITESPACE_CARRIAGE_RETURN, 15, 4, WHITESPACE_CARRIAGE_RETURN, 3, 4, '\t', 6, '\n', 1, '\f', 11, 0, 2, 5, 0, WHITESPACE_CARRIAGE_RETURN, 14, 2, '\b', 15, 7, 4, 15, 1, '\n', 7, 5, 6, '\f', 11, 3, '\b', '\t', 14}, new char[]{2, 4, '\b', 15, 7, '\n', WHITESPACE_CARRIAGE_RETURN, 6, 4, 1, 3, '\f', 11, 7, 14, 0, '\f', 2, 5, '\t', '\n', WHITESPACE_CARRIAGE_RETURN, 0, 3, 1, 11, 15, 5, 6, '\b', '\t', 14, 14, 11, 5, 6, 4, 1, 3, '\n', 2, '\f', 15, 0, WHITESPACE_CARRIAGE_RETURN, 2, '\b', 5, 11, '\b', 0, 15, 7, 14, '\t', 4, '\f', 7, '\n', '\t', 1, WHITESPACE_CARRIAGE_RETURN, 6, 3}, new char[]{'\f', '\t', 0, 7, '\t', 2, 14, 1, '\n', 15, 3, 4, 6, '\f', 5, 11, 1, 14, WHITESPACE_CARRIAGE_RETURN, 0, 2, '\b', 7, WHITESPACE_CARRIAGE_RETURN, 15, 5, 4, '\n', '\b', 3, 11, 6, '\n', 4, 6, 11, 7, '\t', 0, 6, 4, 2, WHITESPACE_CARRIAGE_RETURN, 1, '\t', 15, 3, '\b', 15, 3, 1, 14, '\f', 5, 11, 0, 2, '\f', 14, 7, 5, '\n', '\b', WHITESPACE_CARRIAGE_RETURN}, new char[]{4, 1, 3, '\n', 15, '\f', 5, 0, 2, 11, '\t', 6, '\b', 7, 6, '\t', 11, 4, '\f', 15, 0, 3, '\n', 5, 14, WHITESPACE_CARRIAGE_RETURN, 7, '\b', WHITESPACE_CARRIAGE_RETURN, 14, 1, 2, WHITESPACE_CARRIAGE_RETURN, 6, 14, '\t', 4, 1, 2, 14, 11, WHITESPACE_CARRIAGE_RETURN, 5, 0, 1, '\n', '\b', 3, 0, 11, 3, 5, '\t', 4, 15, 2, 7, '\b', '\f', 15, '\n', 7, 6, '\f'}, new char[]{WHITESPACE_CARRIAGE_RETURN, 7, '\n', 0, 6, '\t', 5, 15, '\b', 4, 3, '\n', 11, 14, '\f', 5, 2, 11, '\t', 6, 15, '\f', 0, 3, 4, 1, 14, WHITESPACE_CARRIAGE_RETURN, 1, 2, 7, '\b', 1, 2, '\f', 15, '\n', 4, 0, 3, WHITESPACE_CARRIAGE_RETURN, 14, 6, '\t', 7, '\b', '\t', 6, 15, 1, 5, '\f', 3, '\n', 14, 5, '\b', 7, 11, 0, 4, WHITESPACE_CARRIAGE_RETURN, 2, 11}};
//
//    /* renamed from: g */
//    private static final int[] f4062g = {15, 6, 19, 20, 28, 11, 27, 16, 0, 14, 22, 25, 4, 17, 30, 9, 1, 7, 23, 13, 31, 26, 2, 8, 18, 12, 29, 5, 21, 10, 3, 24};
//
//    /* renamed from: h */
//    private static final int[] f4063h = {39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25, 32, 0, 40, 8, 48, 16, 56, 24};
//
//    /* renamed from: i */
//    private static final int[] f4064i = {56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19, 11, 3};
//
//    /* renamed from: j */
//    private static final int[] f4065j = {13, 16, 10, 23, 0, 4, -1, -1, 2, 27, 14, 5, 20, 9, -1, -1, 22, 18, 11, 3, 25, 7, -1, -1, 15, 6, 26, 19, 12, 1, -1, -1, 40, 51, 30, 36, 46, 54, -1, -1, 29, 39, 50, 44, 32, 47, -1, -1, 43, 48, 38, 55, 33, 52, -1, -1, 45, 41, 49, 35, 28, 31, -1, -1};
//
//    /* renamed from: k */
//    private static final int[] f4066k = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
//
//    /* renamed from: l */
//    private static final long[] f4067l = {0, 1048577, 3145731};
//
//    /* renamed from: m */
//    private static long f4068m = 0;
//
//    /* renamed from: p */
//    private static int[] f4071p = new int[2];
//
//    /* renamed from: q */
//    private static byte[] f4072q = new byte[8];
//
//    /* renamed from: u */
//    public static final byte[] f4076u = "kwks&@69".getBytes();
//
//    /* renamed from: a */
//    private static long a(int[] iArr, int i, long j) {
//        long j2 = 0;
//        for (int i2 = 0; i2 < i; i2++) {
//            if (iArr[i2] >= 0) {
//                long[] jArr = f4058c;
//                if ((jArr[iArr[i2]] & j) != 0) {
//                    j2 |= jArr[i2];
//                }
//            }
//        }
//        return j2;
//    }
//
//
//
//    /* renamed from: a */
//    private static void a(long j, long[] jArr, int i) {
//        long m176771a = a(f4064i, 56, j);
//        for (int i2 = 0; i2 < 16; i2++) {
//            long[] jArr2 = f4067l;
//            int[] iArr = f4066k;
//            m176771a = ((m176771a & (~jArr2[iArr[i2]])) >> iArr[i2]) | ((jArr2[iArr[i2]] & m176771a) << (28 - iArr[i2]));
//            jArr[i2] = a(f4065j, 64, m176771a);
//        }
//        if (i == 1) {
//            for (int i3 = 0; i3 < 8; i3++) {
//                long j2 = jArr[i3];
//                int i4 = 15 - i3;
//                jArr[i3] = jArr[i4];
//                jArr[i4] = j2;
//            }
//        }
//    }
//
//    /* renamed from: a */
//    private static long a(long[] jArr, long j) {
//        f4068m = a(f4059d, 64, j);
//        int[] iArr = f4071p;
//        long j2 = f4068m;
//        iArr[0] = (int) (j2 & 4294967295L);
//        iArr[1] = (int) ((j2 & (-4294967296L)) >> 32);
//        for (int i = 0; i < 16; i++) {
//            f4070o = f4071p[1];
//            f4070o = a(f4060e, 64, f4070o);
//            f4070o ^= jArr[i];
//            for (int i2 = 0; i2 < 8; i2++) {
//                f4072q[i2] = (byte) (255 & (f4070o >> (i2 * 8)));
//            }
//            f4073r = 0;
//            int i3 = 7;
//            while (true) {
//                f4075t = i3;
//                int i4 = f4075t;
//                if (i4 >= 0) {
//                    f4073r <<= 4;
//                    f4073r |= f4061f[i4][f4072q[i4]];
//                    i3 = i4 - 1;
//                }
//                if (i4<0){
//                    break;
//                }
//            }
//            f4070o = f4073r;
//            f4070o = a(f4062g, 32, f4070o);
//            int[] iArr2 = f4071p;
//            f4069n = iArr2[0];
//            iArr2[0] = iArr2[1];
//            iArr2[1] = (int) (f4069n ^ f4070o);
//        }
//        int[] iArr3 = f4071p;
//        f4074s = iArr3[0];
//        iArr3[0] = iArr3[1];
//        iArr3[1] = f4074s;
//        f4068m = ((iArr3[1] << 32) & (-4294967296L)) | (4294967295L & iArr3[0]);
//        f4068m = a(f4063h, 64, f4068m);
//        return f4068m;
//    }
//
//
//
//
//    public static synchronized byte[] b(byte[] bArr, byte[] bArr2) {
//        byte[] bArr3;
//        synchronized (KuwoDES_zj.class) {
//            int length = bArr.length;
//            int length2 = bArr2.length;
//            long[] jArr = new long[16];
//            long j = 0;
//            for (int i = 0; i < 8; i++) {
//                j |= bArr2[i] << (i * 8);
//            }
//            for (int i2 = 0; i2 < 16; i2++) {
//                jArr[i2] = 0;
//            }
//            a(j, jArr, 1);
//            int i3 = length / 8;
//            long[] jArr2 = new long[i3];
//            for (int i4 = 0; i4 < i3; i4++) {
//                for (int i5 = 0; i5 < 8; i5++) {
//                    jArr2[i4] = jArr2[i4] | ((bArr[(i4 * 8) + i5] & 255) << (i5 * 8));
//                }
//            }
//            long[] jArr3 = new long[i3];
//            for (int i6 = 0; i6 < i3; i6++) {
//                jArr3[i6] = a(jArr, jArr2[i6]);
//            }
//            bArr3 = new byte[i3 * 8];
//            for (int i7 = 0; i7 < i3; i7++) {
//                for (int i8 = 0; i8 < 8; i8++) {
//                    bArr3[(i7 * 8) + i8] = (byte) (255 & (jArr3[i7] >> (i8 * 8)));
//                }
//            }
//        }
//        return bArr3;
//    }
//
//
//
//
//
//
//
//
//}
