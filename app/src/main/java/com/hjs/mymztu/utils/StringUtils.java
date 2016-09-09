package com.hjs.mymztu.utils;

/**
 * Created by Administrator on 2016/3/29.
 */
public class StringUtils {

    /**
     * 去除内容中的html代码
     *
     * @param input
     * @param length
     * @return
     */
    public static final String splitAndFilterString(String input) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        // 去掉所有html元素,
        String str = input.replaceAll("//&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        // int len = str.length();
        // if (len <= length) {
        // return str;
        // } else {
        // str = str.substring(0, length);
        // str += "......";
        // }
        return str;
    }
}
