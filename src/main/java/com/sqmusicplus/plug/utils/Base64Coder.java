package com.sqmusicplus.plug.utils;

import java.io.UnsupportedEncodingException;

public final class Base64Coder {

    /* renamed from: a  reason: collision with root package name */
    public static final String f4891a = "mobile";

    /* renamed from: kuwo.b  reason: collision with root package name */
    private static char[] f4892b = new char[64];

    /* renamed from: c  reason: collision with root package name */
    private static byte[] f4893c = new byte[128];

    static {
        char c2 = 'A';
        int i2 = 0;
        while (c2 <= 'Z') {
            f4892b[i2] = c2;
            c2 = (char) (c2 + 1);
            i2++;
        }
        char c3 = 'a';
        while (c3 <= 'z') {
            f4892b[i2] = c3;
            c3 = (char) (c3 + 1);
            i2++;
        }
        char c4 = '0';
        while (c4 <= '9') {
            f4892b[i2] = c4;
            c4 = (char) (c4 + 1);
            i2++;
        }
        f4892b[i2] = '+';
        f4892b[i2 + 1] = '/';
        for (int i3 = 0; i3 < f4893c.length; i3++) {
            f4893c[i3] = -1;
        }
        for (int i4 = 0; i4 < 64; i4++) {
            f4893c[f4892b[i4]] = (byte) i4;
        }
    }

    public static String a(String str) {
        return a(str.getBytes());
    }

    public static String a(byte[] bArr) {
        return new String(encode(bArr, bArr.length));
    }

    public static String a(String str, String str2, String str3) {
        try {
            return new String(encode(str.getBytes(str2), str.getBytes(str2).length, str3));
        } catch (Exception unused) {
            return "";
        }
    }

    public static char[] encode(byte[] bArr, int i2) {
        return encode(bArr, i2, (String) null);
    }

    public static char[] encode(byte[] bArr, int i2, String str) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        if (str != null && !str.equals("")) {
            byte[] bytes = str.getBytes();
            for (int i8 = 0; i8 < bArr.length; i8 = i7) {
                i7 = i8;
                int i9 = 0;
                while (i9 < bytes.length && i7 < bArr.length) {
                    bArr[i7] = (byte) (bArr[i7] ^ bytes[i9]);
                    i9++;
                    i7++;
                }
            }
        }
        int i10 = ((i2 * 4) + 2) / 3;
        char[] cArr = new char[(((i2 + 2) / 3) * 4)];
        int i11 = 0;
        int i12 = 0;
        while (i11 < i2) {
            int i13 = i11 + 1;
            int i14 = bArr[i11] & 255;
            if (i13 < i2) {
                i3 = i13 + 1;
                i4 = bArr[i13] & 255;
            } else {
                i3 = i13;
                i4 = 0;
            }
            if (i3 < i2) {
                i5 = i3 + 1;
                i6 = bArr[i3] & 255;
            } else {
                i5 = i3;
                i6 = 0;
            }
            int i15 = i14 >>> 2;
            int i16 = ((i14 & 3) << 4) | (i4 >>> 4);
            int i17 = ((i4 & 15) << 2) | (i6 >>> 6);
            int i18 = i6 & 63;
            int i19 = i12 + 1;
            cArr[i12] = f4892b[i15];
            int i20 = i19 + 1;
            cArr[i19] = f4892b[i16];
            char c2 = '=';
            cArr[i20] = i20 < i10 ? f4892b[i17] : '=';
            int i21 = i20 + 1;
            if (i21 < i10) {
                c2 = f4892b[i18];
            }
            cArr[i21] = c2;
            i12 = i21 + 1;
            i11 = i5;
        }
        return cArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0037, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[ExcHandler: IllegalArgumentException | OutOfMemoryError (unused java.lang.Throwable), SYNTHETIC, Splitter:B:11:0x002b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String b(String r6, String r7, String r8) {
        /*
            char[] r6 = r6.toCharArray()
            byte[] r6 = a(r6)
            byte[] r8 = r8.getBytes()
            r0 = 0
            r1 = 0
        L_0x000e:
            int r2 = r6.length
            if (r1 >= r2) goto L_0x0029
            r2 = r1
            r1 = 0
        L_0x0013:
            int r3 = r8.length
            if (r1 >= r3) goto L_0x0027
            int r3 = r6.length
            if (r2 >= r3) goto L_0x0027
            int r3 = r2 + 1
            byte r4 = r6[r2]
            byte r5 = r8[r1]
            r4 = r4 ^ r5
            byte r4 = (byte) r4
            r6[r2] = r4
            int r1 = r1 + 1
            r2 = r3
            goto L_0x0013
        L_0x0027:
            r1 = r2
            goto L_0x000e
        L_0x0029:
            if (r7 != 0) goto L_0x0031
            java.lang.String r7 = new java.lang.String     // Catch:{ IllegalArgumentException | OutOfMemoryError -> 0x0037, IllegalArgumentException | OutOfMemoryError -> 0x0037 }
            r7.<init>(r6)     // Catch:{ IllegalArgumentException | OutOfMemoryError -> 0x0037, IllegalArgumentException | OutOfMemoryError -> 0x0037 }
            return r7
        L_0x0031:
            java.lang.String r8 = new java.lang.String     // Catch:{ IllegalArgumentException | OutOfMemoryError -> 0x0037, IllegalArgumentException | OutOfMemoryError -> 0x0037 }
            r8.<init>(r6, r7)     // Catch:{ IllegalArgumentException | OutOfMemoryError -> 0x0037, IllegalArgumentException | OutOfMemoryError -> 0x0037 }
            return r8
        L_0x0037:
            r6 = 0
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.base.utils.kuwo.b.kuwo.b.kuwo.b(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public static String a(String str, String str2) {
        try {
            return new String(b(str), str2);
        } catch (OutOfMemoryError | UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static byte[] b(String str) {
        return a(str.toCharArray());
    }

    public static byte[] a(char[] cArr) {
        int i2;
        char c2;
        int i3;
        int length = cArr.length;
        if (length % 4 == 0) {
            while (length > 0 && cArr[length - 1] == '=') {
                length--;
            }
            int i4 = (length * 3) / 4;
            byte[] bArr = new byte[i4];
            int i5 = 0;
            for (int i6 = 0; i6 < length; i6 = i2) {
                int i7 = i6 + 1;
                char c3 = cArr[i6];
                int i8 = i7 + 1;
                char c4 = cArr[i7];
                char c5 = 'A';
                if (i8 < length) {
                    i2 = i8 + 1;
                    c2 = cArr[i8];
                } else {
                    i2 = i8;
                    c2 = 'A';
                }
                if (i2 < length) {
                    int i9 = i2 + 1;
                    char c6 = cArr[i2];
                    i2 = i9;
                    c5 = c6;
                }
                if (c3 > 127 || c4 > 127 || c2 > 127 || c5 > 127) {
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                }
                byte b2 = f4893c[c3];
                byte b3 = f4893c[c4];
                byte b4 = f4893c[c2];
                byte b5 = f4893c[c5];
                if (b2 < 0 || b3 < 0 || b4 < 0 || b5 < 0) {
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                }
                int i10 = (b2 << 2) | (b3 >>> 4);
                int i11 = ((b3 & 15) << 4) | (b4 >>> 2);
                int i12 = ((b4 & 3) << 6) | b5;
                int i13 = i5 + 1;
                bArr[i5] = (byte) i10;
                if (i13 < i4) {
                    i3 = i13 + 1;
                    bArr[i13] = (byte) i11;
                } else {
                    i3 = i13;
                }
                if (i3 < i4) {
                    i5 = i3 + 1;
                    bArr[i3] = (byte) i12;
                } else {
                    i5 = i3;
                }
            }
            return bArr;
        }
        throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
    }

    private Base64Coder() {
    }

    public static char[] encode(byte[] bArr){
       return encode(bArr,bArr.length,"");
    }
}