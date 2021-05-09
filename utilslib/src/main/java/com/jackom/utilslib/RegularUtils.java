package com.jackom.utilslib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : zhengminxin
 * @date : 1/13/2021 11:00 AM
 * @desc : 正则相关的工具类
 */
public class RegularUtils {

    /**
     * 判断手机号的书写是否有误
     *
     * @param phone
     * @return bool
     */
    public static boolean isPhone(String phone) {
        String regexMobile = "^[1][3-8]\\d{9}$";

        Pattern moblie = Pattern.compile(regexMobile);
        Matcher matcherMoblie = moblie.matcher(phone);
        if (matcherMoblie.matches()) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否为int型数
     * @param number
     * @return
     */
    public static boolean isInteger(String number){
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    // 判断一个字符是否是中文字符
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5; // 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isContainChinese(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c))
                return true; // 有一个中文字符就返回
        }
        return false;
    }

}
