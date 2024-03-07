package com.sqmusicplus.plug.utils;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Classname QSignHelper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/1/18 17:40
 * @Created AffectionParadise 的项目内容
 */

public class QSignHelper {


    private  static String v(String b) {
        int[] p = {21, 4, 9, 26, 16, 20, 27, 30};
        StringBuilder sb = new StringBuilder();
        for (int x : p) sb.append(b.charAt(x));
        return sb.toString();
    }

    private static  String c(String b) {
        int[] p = {18, 11, 3, 2, 1, 7, 6, 25};
        StringBuilder sb = new StringBuilder();
        for (int x : p) sb.append(b.charAt(x));
        return sb.toString();
    }

    private static List<Integer> y(Integer a, Integer b, Integer c) {
        List<Integer> e = new LinkedList<>();
        int r25 = a >> 2;
        if (b != null && c != null) {
            int r26 = a & 3;
            int r26_2 = r26 << 4;
            int r26_3 = b >> 4;
            int r26_4 = r26_2 | r26_3;
            int r27 = b & 15;
            int r27_2 = r27 << 2;
            int r27_3 = r27_2 | c >> 6;
            int r28 = c & 63;
            e.add(r25);
            e.add(r26_4);
            e.add(r27_3);
            e.add(r28);
        } else {
            int r10 = a >> 2;
            int r11 = a & 3;
            int r11_2 = r11 << 4;
            e.add(r10);
            e.add(r11_2);
        }
        return e;
    }

    private static String n(List<Integer> ls) {
        List<Integer> e = new LinkedList<>();
        for (int i = 0, s = ls.size(); i < s; i += 3) {
            if (i < s - 2) e.addAll(y(ls.get(i), ls.get(i + 1), ls.get(i + 2)));
            else e.addAll(y(ls.get(i), null, null));
        }
        StringBuilder sb = new StringBuilder();
        String base64All = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        for (Integer i : e) sb.append(base64All.charAt(i));
        return sb.toString();
    }

    private static List<Integer> t(String b) {
        Map<Character, Integer> zd = new HashMap<>();
        zd.put('0', 0);
        zd.put('1', 1);
        zd.put('2', 2);
        zd.put('3', 3);
        zd.put('4', 4);
        zd.put('5', 5);
        zd.put('6', 6);
        zd.put('7', 7);
        zd.put('8', 8);
        zd.put('9', 9);
        zd.put('A', 10);
        zd.put('B', 11);
        zd.put('C', 12);
        zd.put('D', 13);
        zd.put('E', 14);
        zd.put('F', 15);
        int[] ol = {212, 45, 80, 68, 195, 163, 163, 203, 157, 220, 254, 91, 204, 79, 104, 6};
        LinkedList<Integer> res = new LinkedList<>();
        for (int i = 0, j = 0, s = b.length(); i < s; i += 2, j++) {
            int one = zd.get(b.charAt(i));
            int two = zd.get(b.charAt(i + 1));
            int r = one * 16 ^ two;
            res.add(r ^ ol[j]);
        }
        return res;
    }

    public static String sign(String data) {
        String md5 = DigestUtil.md5Hex(data).toUpperCase();
        String h = v(md5);
        String e = c(md5);
        List<Integer> ls = t(md5);
        String m = n(ls);
        String res = ("zzb" + h + m + e).toLowerCase().replaceAll("[\\\\/+]", "");
        return res;
    }

}
