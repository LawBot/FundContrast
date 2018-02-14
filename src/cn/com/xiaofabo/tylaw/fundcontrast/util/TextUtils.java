/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class TextUtils {
    
    public static String REGEX_IDENTIFIER_LEVEL_0 = "^第.*?部分";
    public static String REGEX_IDENTIFIER_LEVEL_0_TP = "^第.*?部分.*?[^0-9]$";
    public static String REGEX_IDENTIFIER_LEVEL_1 = "^[一|二|三|四|五|六|七|八|九|十]*?、";
    public static String REGEX_IDENTIFIER_LEVEL_2 = "^[（|(][一|二|三|四|五|六|七|八|九|十]*[）|)]";
    public static String REGEX_IDENTIFIER_LEVEL_3 = "^\\d+[、|\\.|．]";
    public static String REGEX_IDENTIFIER_LEVEL_4 = "^[（|(]\\d+[）|)]";
    

    public static List removeAllEmptyLines(List strList) {
        List toReturn = new LinkedList();
        for (int i = 0; i < strList.size(); ++i) {
            String str = (String) strList.get(i);
            if (str != null && !str.trim().isEmpty()) {
                toReturn.add(str);
            }
        }
        return toReturn;
    }

    public static int getChapterIndex(String titleLine) {
        int start = titleLine.indexOf("第") + 1;
        int end = titleLine.indexOf("部分");

        if (start == 0 || end == -1 || end < start) {
            return -1;  // error
        }

        String idxStr = titleLine.substring(start, end);
        int idx = zhNum2Int(idxStr);
        return idx;
    }

    public static String getChapterTitle(String titleLine) {
        int start = titleLine.indexOf("部分") + "部分".length();
        if (start == 0) {
            return null;
        }

        String title = titleLine.substring(start).trim();
//        System.out.println(title);
        return title;
    }

    public static String getSectionTitle(String titleLine) {
        int start = titleLine.indexOf("、") + "、".length();
        if (start == 0) {
            return null;
        }

        String title = titleLine.substring(start).trim();
        return title;
    }

    public static String getSubSectionTitle(String titleLine) {
        int start = titleLine.indexOf("、") + "、".length();
        if (start == 0) {
            return null;
        }

        String title = titleLine.substring(start).trim();
        return title;
    }

    public static String getType3SubSectionTitle(String titleLine) {
        int start = titleLine.indexOf("）") + "）".length();
        if (start == 0) {
            return null;
        }
        String title = titleLine.substring(start).trim();
        return title;
    }

    public static String getSubSubSectionTitle(String titleLine) {
        int start = titleLine.indexOf("）") + "）".length();
        if (start == 0) {
            return null;
        }

        String title = titleLine.substring(start).trim();
        return title;
    }

    public static String getPartTitle(String titleLine) {
        String title = null;
        List partIdentifiers = new LinkedList();
        partIdentifiers.add(REGEX_IDENTIFIER_LEVEL_0);
        partIdentifiers.add(REGEX_IDENTIFIER_LEVEL_1);
        partIdentifiers.add(REGEX_IDENTIFIER_LEVEL_2);
        partIdentifiers.add(REGEX_IDENTIFIER_LEVEL_3);
        partIdentifiers.add(REGEX_IDENTIFIER_LEVEL_4);

        int identifierLevel = -1;
        for (int i = 0; i < partIdentifiers.size(); ++i) {
            String identifierStr = (String) partIdentifiers.get(i);
            Pattern pattern = Pattern.compile(identifierStr);
            Matcher matcher = pattern.matcher(titleLine);
            if (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
//                System.out.println(titleLine); 
                title = titleLine.substring(end).trim();
                break;
            }
        }
        return title;
    }

    private static int zhNum2Int(String s) {
        String x = " 一二三四五六七八九十百";
        int l = s.length();
        int i = x.indexOf(s.charAt(l - 1));
        int j = x.indexOf(s.charAt(0));
        int q = j * 100;
        return l < 2
                ? i : l < 3
                        ? i == 10
                                ? j * 10 : i > 10
                                        ? q : 10 + i : l < 4
                                ? j * 10 + i : l < 5
                                        ? q + i : q + i + x.indexOf(s.charAt(2)) * 10;
    }
}
