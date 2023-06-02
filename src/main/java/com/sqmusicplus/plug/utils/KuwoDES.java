package com.sqmusicplus.plug.utils;

public final class KuwoDES {

    /* renamed from: a  reason: collision with root package name */
    public static final byte[] SECRET_KEY  = "ylzsxkwm".getBytes();

    /* renamed from: kuwo.b  reason: collision with root package name */
    public static final int SECRET_KEY_LENG = SECRET_KEY.length;

    /* renamed from: c  reason: collision with root package name */
    public static final byte[] f4896c = "kwks&@69".getBytes();

    /* renamed from: kuwo.d  reason: collision with root package name */
    private static final long[] f4897d = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, 2147483648L, 4294967296L, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f4898e = {57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7, 56, 48, 40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6};

    /* renamed from: f  reason: collision with root package name */
    private static final int[] f4899f = {31, 0, 1, 2, 3, 4, -1, -1, 3, 4, 5, 6, 7, 8, -1, -1, 7, 8, 9, 10, 11, 12, -1, -1, 11, 12, 13, 14, 15, 16, -1, -1, 15, 16, 17, 18, 19, 20, -1, -1, 19, 20, 21, 22, 23, 24, -1, -1, 23, 24, 25, 26, 27, 28, -1, -1, 27, 28, 29, 30, 31, 30, -1, -1};

    /* renamed from: g  reason: collision with root package name */
    private static final char[][] f4900g = {new char[]{14, 4, 3, 15, 2, '\r', 5, 3, '\r', 14, 6, '\t', 11, 2, 0, 5, 4, 1, '\n', '\f', 15, 6, '\t', '\n', 1, '\b', '\f', 7, '\b', 11, 7, 0, 0, 15, '\n', 5, 14, 4, '\t', '\n', 7, '\b', '\f', 3, '\r', 1, 3, 6, 15, '\f', 6, 11, 2, '\t', 5, 0, 4, 2, 11, 14, 1, 7, '\b', '\r'}, new char[]{15, 0, '\t', 5, 6, '\n', '\f', '\t', '\b', 7, 2, '\f', 3, '\r', 5, 2, 1, 14, 7, '\b', 11, 4, 0, 3, 14, 11, '\r', 6, 4, 1, '\n', 15, 3, '\r', '\f', 11, 15, 3, 6, 0, 4, '\n', 1, 7, '\b', 4, 11, 14, '\r', '\b', 0, 6, 2, 15, '\t', 5, 7, 1, '\n', '\f', 14, 2, 5, '\t'}, new char[]{'\n', '\r', 1, 11, 6, '\b', 11, 5, '\t', 4, '\f', 2, 15, 3, 2, 14, 0, 6, '\r', 1, 3, 15, 4, '\n', 14, '\t', 7, '\f', 5, 0, '\b', 7, '\r', 1, 2, 4, 3, 6, '\f', 11, 0, '\r', 5, 14, 6, '\b', 15, 2, 7, '\n', '\b', 15, 4, '\t', 11, 5, '\t', 0, 14, 3, '\n', 7, 1, '\f'}, new char[]{7, '\n', 1, 15, 0, '\f', 11, 5, 14, '\t', '\b', 3, '\t', 7, 4, '\b', '\r', 6, 2, 1, 6, 11, '\f', 2, 3, 0, 5, 14, '\n', '\r', 15, 4, '\r', 3, 4, '\t', 6, '\n', 1, '\f', 11, 0, 2, 5, 0, '\r', 14, 2, '\b', 15, 7, 4, 15, 1, '\n', 7, 5, 6, '\f', 11, 3, '\b', '\t', 14}, new char[]{2, 4, '\b', 15, 7, '\n', '\r', 6, 4, 1, 3, '\f', 11, 7, 14, 0, '\f', 2, 5, '\t', '\n', '\r', 0, 3, 1, 11, 15, 5, 6, '\b', '\t', 14, 14, 11, 5, 6, 4, 1, 3, '\n', 2, '\f', 15, 0, '\r', 2, '\b', 5, 11, '\b', 0, 15, 7, 14, '\t', 4, '\f', 7, '\n', '\t', 1, '\r', 6, 3}, new char[]{'\f', '\t', 0, 7, '\t', 2, 14, 1, '\n', 15, 3, 4, 6, '\f', 5, 11, 1, 14, '\r', 0, 2, '\b', 7, '\r', 15, 5, 4, '\n', '\b', 3, 11, 6, '\n', 4, 6, 11, 7, '\t', 0, 6, 4, 2, '\r', 1, '\t', 15, 3, '\b', 15, 3, 1, 14, '\f', 5, 11, 0, 2, '\f', 14, 7, 5, '\n', '\b', '\r'}, new char[]{4, 1, 3, '\n', 15, '\f', 5, 0, 2, 11, '\t', 6, '\b', 7, 6, '\t', 11, 4, '\f', 15, 0, 3, '\n', 5, 14, '\r', 7, '\b', '\r', 14, 1, 2, '\r', 6, 14, '\t', 4, 1, 2, 14, 11, '\r', 5, 0, 1, '\n', '\b', 3, 0, 11, 3, 5, '\t', 4, 15, 2, 7, '\b', '\f', 15, '\n', 7, 6, '\f'}, new char[]{'\r', 7, '\n', 0, 6, '\t', 5, 15, '\b', 4, 3, '\n', 11, 14, '\f', 5, 2, 11, '\t', 6, 15, '\f', 0, 3, 4, 1, 14, '\r', 1, 2, 7, '\b', 1, 2, '\f', 15, '\n', 4, 0, 3, '\r', 14, 6, '\t', 7, '\b', '\t', 6, 15, 1, 5, '\f', 3, '\n', 14, 5, '\b', 7, 11, 0, 4, '\r', 2, 11}};

    /* renamed from: h  reason: collision with root package name */
    private static final int[] f4901h = {15, 6, 19, 20, 28, 11, 27, 16, 0, 14, 22, 25, 4, 17, 30, 9, 1, 7, 23, 13, 31, 26, 2, 8, 18, 12, 29, 5, 21, 10, 3, 24};

    /* renamed from: i  reason: collision with root package name */
    private static final int[] f4902i = {39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25, 32, 0, 40, 8, 48, 16, 56, 24};
    private static final int[] j = {56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19, 11, 3};
    private static final int[] k = {13, 16, 10, 23, 0, 4, -1, -1, 2, 27, 14, 5, 20, 9, -1, -1, 22, 18, 11, 3, 25, 7, -1, -1, 15, 6, 26, 19, 12, 1, -1, -1, 40, 51, 30, 36, 46, 54, -1, -1, 29, 39, 50, 44, 32, 47, -1, -1, 43, 48, 38, 55, 33, 52, -1, -1, 45, 41, 49, 35, 28, 31, -1, -1};
    private static final int[] l = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private static final long[] m = {0, 1048577, 3145731};
    private static final int n = 0;
    private static final int o = 1;
    private static long p = 0;
    private static long q;
    private static long r;
    private static int[] s = new int[2];
    private static byte[] t = new byte[8];
    private static int u;
    private static int v;
    private static int w;

    private static long a(int[] iArr, int i2, long j2) {
        long j3 = 0;
        for (int i3 = 0; i3 < i2; i3++) {
            if (iArr[i3] >= 0 && (f4897d[iArr[i3]] & j2) != 0) {
                j3 |= f4897d[i3];
            }
        }
        return j3;
    }

    private static void a(long j2, long[] jArr, int i2) {
        long a2 = a(j, 56, j2);
        for (int i3 = 0; i3 < 16; i3++) {
            a2 = ((a2 & (m[l[i3]] ^ -1)) >> l[i3]) | ((m[l[i3]] & a2) << (28 - l[i3]));
            jArr[i3] = a(k, 64, a2);
        }
        if (i2 == 1) {
            for (int i4 = 0; i4 < 8; i4++) {
                long j3 = jArr[i4];
                int i5 = 15 - i4;
                jArr[i4] = jArr[i5];
                jArr[i5] = j3;
            }
        }
    }

    private static long a(long[] jArr, long j2) {
        p = a(f4898e, 64, j2);
        s[0] = (int) (p & 4294967295L);
        s[1] = (int) ((p & -4294967296L) >> 32);
        for (int i2 = 0; i2 < 16; i2++) {
            r = (long) s[1];
            r = a(f4899f, 64, r);
            r ^= jArr[i2];
            for (int i3 = 0; i3 < 8; i3++) {
                t[i3] = (byte) ((int) (255 & (r >> (i3 * 8))));
            }
            u = 0;
            int i4 = 7;
            while (true) {
                w = i4;
                if (w < 0) {
                    break;
                }
                u <<= 4;
                u |= f4900g[w][t[w]];
                i4 = w - 1;
            }
            r = (long) u;
            r = a(f4901h, 32, r);
            q = (long) s[0];
            s[0] = s[1];
            s[1] = (int) (q ^ r);
        }
        v = s[0];
        s[0] = s[1];
        s[1] = v;
        p = (((long) s[0]) & 4294967295L) | ((((long) s[1]) << 32) & -4294967296L);
        p = a(f4902i, 64, p);
        return p;
    }

    public static synchronized byte[] a(byte[] bArr, int i2, byte[] bArr2, int i3) {
        byte[] bArr3;
//        synchronized (d.class) {
            long j2 = 0;
            for (int i4 = 0; i4 < 8; i4++) {
                try {
                    j2 |= ((long) bArr2[i4]) << (i4 * 8);
                } catch (Throwable th) {
                    throw th;
                }
            }
            int i5 = i2 / 8;
            long[] jArr = new long[16];
            for (int i6 = 0; i6 < 16; i6++) {
                jArr[i6] = 0;
            }
            long[] jArr2 = new long[i5];
            for (int i7 = 0; i7 < i5; i7++) {
                for (int i8 = 0; i8 < 8; i8++) {
                    jArr2[i7] = (((long) bArr[(i7 * 8) + i8]) << (i8 * 8)) | jArr2[i7];
                }
            }
            long[] jArr3 = new long[((((i5 + 1) * 8) + 1) / 8)];
            a(j2, jArr, 0);
            for (int i9 = 0; i9 < i5; i9++) {
                jArr3[i9] = a(jArr, jArr2[i9]);
            }
            int i10 = i2 % 8;
            int i11 = i5 * 8;
            int i12 = i2 - i11;
            byte[] bArr4 = new byte[i12];
            System.arraycopy(bArr, i11, bArr4, 0, i12);
            long j3 = 0;
            for (int i13 = 0; i13 < i10; i13++) {
                j3 |= ((long) bArr4[i13]) << (i13 * 8);
            }
            jArr3[i5] = a(jArr, j3);
            bArr3 = new byte[(jArr3.length * 8)];
            int i14 = 0;
            int i15 = 0;
            while (i14 < jArr3.length) {
                int i16 = i15;
                for (int i17 = 0; i17 < 8; i17++) {
                    bArr3[i16] = (byte) ((int) (255 & (jArr3[i14] >> (i17 * 8))));
                    i16++;
                }
                i14++;
                i15 = i16;
            }
//        }
        return bArr3;
    }

    public static synchronized byte[] encrypt2(byte[] bArr, int i2, byte[] bArr2, int i3) {
        byte[] bArr3;
        synchronized (KuwoDES.class) {
            long j2 = 0;
            for (int i4 = 0; i4 < 8; i4++) {
                try {
                    j2 |= ((long) bArr2[i4]) << (i4 * 8);
                } catch (Throwable th) {
                    throw th;
                }
            }
            int i5 = i2 / 8;
            long[] jArr = new long[16];
            for (int i6 = 0; i6 < 16; i6++) {
                jArr[i6] = 0;
            }
            long[] jArr2 = new long[i5];
            for (int i7 = 0; i7 < i5; i7++) {
                for (int i8 = 0; i8 < 8; i8++) {
                    jArr2[i7] = (((long) (bArr[(i7 * 8) + i8] & 255)) << (i8 * 8)) | jArr2[i7];
                }
            }
            long[] jArr3 = new long[((((i5 + 1) * 8) + 1) / 8)];
            a(j2, jArr, 0);
            for (int i9 = 0; i9 < i5; i9++) {
                jArr3[i9] = a(jArr, jArr2[i9]);
            }
            int i10 = i2 % 8;
            int i11 = i5 * 8;
            int i12 = i2 - i11;
            byte[] bArr4 = new byte[i12];
            System.arraycopy(bArr, i11, bArr4, 0, i12);
            long j3 = 0;
            for (int i13 = 0; i13 < i10; i13++) {
                j3 |= ((long) (bArr4[i13] & 255)) << (i13 * 8);
            }
            jArr3[i5] = a(jArr, j3);
            bArr3 = new byte[(jArr3.length * 8)];
            int i14 = 0;
            int i15 = 0;
            while (i14 < jArr3.length) {
                int i16 = i15;
                for (int i17 = 0; i17 < 8; i17++) {
                    bArr3[i16] = (byte) ((int) (255 & (jArr3[i14] >> (i17 * 8))));
                    i16++;
                }
                i14++;
                i15 = i16;
            }
        }
        return bArr3;
    }

    public static synchronized byte[] a(byte[] bArr, byte[] bArr2) {
        long j2;
        byte[] bArr3;
        synchronized (KuwoDES.class) {
            int length = bArr.length;
            int length2 = bArr2.length;
            long[] jArr = new long[16];
            int i2 = 0;
            while (true) {
                j2 = 0;
                if (i2 >= 16) {
                    break;
                }
                jArr[i2] = 0;
                i2++;
            }
            long j3 = 0;
            for (int i3 = 0; i3 < 8; i3++) {
                j3 |= ((long) bArr2[i3]) << (i3 * 8);
            }
            a(j3, jArr, 0);
            int i4 = length / 8;
            long[] jArr2 = new long[i4];
            for (int i5 = 0; i5 < i4; i5++) {
                for (int i6 = 0; i6 < 8; i6++) {
                    jArr2[i5] = jArr2[i5] | (((long) (bArr[(i5 * 8) + i6] & 255)) << (i6 * 8));
                }
            }
            long[] jArr3 = new long[((((i4 + 1) * 8) + 1) / 8)];
            for (int i7 = 0; i7 < i4; i7++) {
                jArr3[i7] = a(jArr, jArr2[i7]);
            }
            int i8 = length % 8;
            int i9 = i4 * 8;
            int i10 = length - i9;
            byte[] bArr4 = new byte[i10];
            System.arraycopy(bArr, i9, bArr4, 0, i10);
            for (int i11 = 0; i11 < i8; i11++) {
                j2 |= ((long) (bArr4[i11] & 255)) << (i11 * 8);
            }
            jArr3[i4] = a(jArr, j2);
            bArr3 = new byte[(jArr3.length * 8)];
            int i12 = 0;
            int i13 = 0;
            while (i12 < jArr3.length) {
                int i14 = i13;
                for (int i15 = 0; i15 < 8; i15++) {
                    bArr3[i14] = (byte) ((int) (255 & (jArr3[i12] >> (i15 * 8))));
                    i14++;
                }
                i12++;
                i13 = i14;
            }
        }
        return bArr3;
    }

    public static synchronized byte[] b(byte[] bArr, byte[] bArr2) {
        byte[] bArr3;
        synchronized (KuwoDES.class) {
            int length = bArr.length;
            int length2 = bArr2.length;
            long[] jArr = new long[16];
            long j2 = 0;
            for (int i2 = 0; i2 < 8; i2++) {
                j2 |= ((long) bArr2[i2]) << (i2 * 8);
            }
            for (int i3 = 0; i3 < 16; i3++) {
                jArr[i3] = 0;
            }
            a(j2, jArr, 1);
            int i4 = length / 8;
            long[] jArr2 = new long[i4];
            for (int i5 = 0; i5 < i4; i5++) {
                for (int i6 = 0; i6 < 8; i6++) {
                    jArr2[i5] = jArr2[i5] | (((long) (bArr[(i5 * 8) + i6] & 255)) << (i6 * 8));
                }
            }
            long[] jArr3 = new long[i4];
            for (int i7 = 0; i7 < i4; i7++) {
                jArr3[i7] = a(jArr, jArr2[i7]);
            }
            bArr3 = new byte[(i4 * 8)];
            for (int i8 = 0; i8 < i4; i8++) {
                for (int i9 = 0; i9 < 8; i9++) {
                    bArr3[(i8 * 8) + i9] = (byte) ((int) (255 & (jArr3[i8] >> (i9 * 8))));
                }
            }
        }
        return bArr3;
    }
}
