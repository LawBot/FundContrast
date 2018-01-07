/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 陈光曦
 */
public class TextUtils {

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
    
    public static String getChapterTitle(String titleLine){
        int start = titleLine.indexOf("部分") + "部分".length();
        if(start == 0){
            return null;
        }
        
        String title = titleLine.substring(start).trim();
//        System.out.println(title);
        return title;
    }

    private static int zhNum2Int(String s) {
        String x = " 一二三四五六七八九十百";
        int l = s.length();
        int i = x.indexOf(s.charAt(l - 1));
        int j = x.indexOf(s.charAt(0));
        int q = j * 100;
        return l < 2 ? 
                i : l < 3 ? 
                i == 10 ? 
                j * 10 : i > 10 ? 
                q : 10 + i : l < 4 ? 
                j * 10 + i : l < 5 ? 
                q + i : q + i + x.indexOf(s.charAt(2)) * 10;
    }
}
