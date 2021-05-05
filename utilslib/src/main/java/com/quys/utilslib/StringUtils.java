package com.quys.utilslib;

import android.text.TextUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    /**
     * 判断一个字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() < 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置实体数据，以防TextView报错
     * @param content
     * @return
     */
    public static String setTextViewText(String content){
        content=isEmpty(content) ? "" : content;
        return content;
    }

    public static List<String> getSplitStr(String str, String symbol){
        if(TextUtils.isEmpty(str) || symbol==null){
            return null;
        }
        if(!str.contains(symbol)){
            return null;
        }
        String[] arr=str.split(symbol);
        return Arrays.asList(arr);
    }

    /**
     * Return whether the string is null or white space.
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static byte[] getUTF8Bytes(final String content) {
        return getBytes(content, StandardCharsets.UTF_8);
    }

    public static byte[] getBytes(final String content, Charset charset) {
        return content.getBytes(charset);
    }

}
