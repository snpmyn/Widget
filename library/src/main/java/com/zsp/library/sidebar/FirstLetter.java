package com.zsp.library.sidebar;

import androidx.annotation.NonNull;

import value.WidgetLibraryMagic;

/**
 * @decs: 首字母
 * @author: 郑少鹏
 * @date: 2019/9/12 16:17
 */
public class FirstLetter {
    private static final int END = 63486;
    /**
     * 按声母表示，该表是GB2312码出现头个汉字（【啊】为首字母头个汉字）
     * i、u、v不做声母（自定规则随前字母）
     */
    private static final char[] CHAR_TABLE = {'啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈', '击', '喀', '垃',
            '妈', '拿', '哦', '啪', '期', '然', '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝',};
    /**
     * 二十六字母区间对应二十七端点
     * GB2312码汉字区间十进制
     */
    private static final int[] TABLE = new int[27];
    /**
     * 对应首字母区间表
     */
    private static final char[] INITIAL_TABLE = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'h', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 't', 't', 'w', 'x', 'y', 'z',};

    // 初始
    static {
        for (int i = 0; i < WidgetLibraryMagic.INT_TWENTY_SIX; i++) {
            // GB2312码首字母区间端点表（十进制）
            TABLE[i] = gbValue(CHAR_TABLE[i]);
        }
        // 区间表结尾
        TABLE[26] = END;
    }

    private FirstLetter() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 据一含汉字字符串返一汉字拼音首字母字符串（字符依次读入、判断、输出）
     */
    @NonNull
    public static String getFirstLetter(@NonNull String sourceStr) {
        String str = sourceStr.toUpperCase();
        char ch = char2initial(str.charAt(0));
        if (isLetter(ch)) {
            return String.valueOf(ch).toUpperCase();
        }
        return "#";
    }

    /**
     * 字母否
     */
    private static boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    /**
     * 指定串首字母
     */
    @NonNull
    public static String getAllFirstLetter(@NonNull String sourceStr) {
        StringBuilder result = new StringBuilder();
        String str = sourceStr.toUpperCase();
        int strLength = str.length();
        int i;
        try {
            for (i = 0; i < strLength; i++) {
                result.append(char2initial(str.charAt(i)));
            }
        } catch (Exception e) {
            result = new StringBuilder();
        }
        return result.toString();
    }

    /**
     * 输字符获声母
     * <p>
     * 英文字母返对应大写字母。
     * 其它非简体汉字返0。
     */
    private static char char2initial(char ch) {
        // 英文字母处理（小写转大写，大写直返）
        if (ch >= WidgetLibraryMagic.CHAR_A_LOWER_CASE && ch <= WidgetLibraryMagic.CHAR_Z_LOWER_CASE) {
            return ch;
        }
        if (ch >= WidgetLibraryMagic.CHAR_A_CAPITAL && ch <= WidgetLibraryMagic.CHAR_Z_CAPITAL) {
            return ch;
        }
        // 非英文字母处理，转首字母后判码表内否（内直返，非内于码表内判）
        // 汉字转首字母
        int gb = gbValue(ch);
        int begin = 45217;
        // 码表区间前直返
        if ((gb < begin) || (gb > END)) {
            return ch;
        }
        int i;
        for (i = 0; i < WidgetLibraryMagic.INT_TWENTY_SIX; i++) {
            // 判匹配码表区间（形如“[,)”），匹配则break
            if ((gb >= TABLE[i]) && (gb < TABLE[i + 1])) {
                break;
            }
        }
        if (gb == END) {
            // 补GB2312区间最右端
            i = 25;
        }
        // 码表区间中返首字母
        return INITIAL_TABLE[i];
    }

    /**
     * 取汉字编码（cn 汉字）
     */
    private static int gbValue(char ch) {
        // 汉字（GB2312码）转十进制
        String str = "";
        str += ch;
        try {
            byte[] bytes = str.getBytes("GB2312");
            if (bytes.length < WidgetLibraryMagic.INT_TWO) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);
        } catch (Exception e) {
            return 0;
        }
    }
}
