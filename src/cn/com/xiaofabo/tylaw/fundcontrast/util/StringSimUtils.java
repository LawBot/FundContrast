/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.MatchDto;

/**
 *
 * @author 陈光曦
 */
public class StringSimUtils {

    private static int compare(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        d = new int[n + 1][m + 1];

        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }

        return d[n][m];
    }

    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     *
     * @param str
     * @param target
     *
     * @return
     */
    public static float getSimilarityRatio(String str, String target) {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());

    }

    //--------------------------------------------------------------------------

    /* 
     * 计算相似度 
     * */
    public static double SimilarDegree(String strA, String strB) {
        if (strA.length() < strB.length()) {
            String tmp = strA;
            strA = strB;
            strB = tmp;
        }
        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);
        //用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度  
        int temp = Math.max(newStrA.length(), newStrB.length());
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();
        return temp2 * 1.0 / temp;
    }

    /* 
     * 将字符串的所有数据依次写成一行 
     * */
    public static String removeSign(String str) {
        StringBuffer sb = new StringBuffer();
        //遍历字符串str,如果是汉字数字或字母，则追加到ab上面  
        for (char item : str.toCharArray()) {
            if (charReg(item)) {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /* 
     * 判断字符是否为汉字，数字和字母， 
     * 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。 
     * */
    public static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
                || (charValue >= 'A' && charValue <= 'Z') || (charValue >= '0' && charValue <= '9');
    }

    /* 
     * 求公共子串，采用动态规划算法。 
     * 其不要求所求得的字符在所给的字符串中是连续的。 
     *  
     * */
    public static String longestCommonSubstring(String strA, String strB) {
        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray();
        int m = chars_strA.length;
        int n = chars_strB.length;

        /* 
         * 初始化矩阵数据,matrix[0][0]的值为0， 
         * 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1， 
         * 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 
         * 矩阵中其余各点的值为0. 
         */
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars_strA[i - 1] == chars_strB[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
                }
            }
        }
        /* 
         * 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值， 
         * 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。 
         *  
         */
        char[] result = new char[matrix[m][n]];
        int currentIndex = result.length - 1;
        while (matrix[m][n] != 0) {
            if (matrix[n] == matrix[n - 1]) {
                n--;
            } else if (matrix[m][n] == matrix[m - 1][n]) {
                m--;
            } else {
                result[currentIndex] = chars_strA[m - 1];
                currentIndex--;
                n--;
                m--;
            }
        }
        return new String(result);
    }

    /* 
     * 结果转换成百分比形式  
     * */
    public static String similarityResult(double resule) {
        return NumberFormat.getPercentInstance(new Locale("en ", "US ")).format(resule);
    }

    public static List<MatchDto> findBestMatch(List<String> s1, List<String> s2, String type) {
        List<MatchDto> matchList = new LinkedList<>();

        for (int i = 0; i < s1.size(); ++i) {
            int bestMatch = -1;
            double bestRatio = 0.0;
            String sampleStr = null;
            String str1 = s1.get(i);
            for (int j = 0; j < s2.size(); ++j) {
                String str2 = s2.get(j);
                double r = getSimilarityRatio(str1, str2);
                if (r > bestRatio) {
                    bestRatio = r;
                    bestMatch = j;
                    sampleStr = str2;
                    if (i != j) {
                        if (i < s2.size()) {
                            String str3 = s2.get(i);
                            double tr = getSimilarityRatio(str1, str3);
                            if (Math.abs(r - tr) < 0.05) {
                                bestRatio = tr;
                                bestMatch = i;
                                sampleStr = str3;
                            }
                        }
                    }
                }
            }
            if ("0".equals(type)) {
            	if (bestMatch != i && bestRatio <1) {
                    bestMatch = -1;
                }
//            	MatchDto matchDto = new MatchDto(i, bestMatch);
//            	matchList.add(matchDto);
			}else {
				if (bestMatch != i && bestRatio < 0.5) {
	                bestMatch = -1;
	            }
				if (bestRatio<1) {
//					MatchDto matchDto = new MatchDto(i, bestMatch);
//	            	matchList.add(matchDto);
				}
			}
            
            System.out.println(i + " -- " + bestMatch);
            if (bestMatch != i) {
                System.out.println("MATCH RATIO: " + bestRatio);
                System.out.println("ORIGINAL STR: " + str1);
                System.out.println("SAMPLE STR: " + sampleStr);
            }
            MatchDto matchDto = new MatchDto(i, bestMatch, bestRatio);
        	matchList.add(matchDto);
            
            
        }
        return matchList;
    }
}
