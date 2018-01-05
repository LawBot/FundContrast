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
}
