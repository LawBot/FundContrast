/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.Chapter;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class StockTypeProcessor extends TextProcessor {

    public FundDoc process() {
        List textList = super.getLines();
        List textChunkList = new LinkedList<>();
        int startIdx = 0;
        int chapterStartId = 0;
        int sectionStartId = 0;
        int subSectionStartId = 0;
        int subSubSectionStartId = 0;
        int textPointStartId = 0;

        List chunk;

        for (int i = 0; i < textList.size(); ++i) {
            Pattern pattern = Pattern.compile("^第.*?部分.*?[^0-9]$");
            Matcher matcher = pattern.matcher((String) textList.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    chunk = textList.subList(startIdx, i);
                    startIdx = i;
                    textChunkList.add(chunk);
                }
            }
            //System.out.println(i + ": " + textList.get(i));
        }
        chunk = textList.subList(startIdx, textList.size() - 1);
        textChunkList.add(chunk);


        for (int i = 0; i < textChunkList.size(); ++i) {
            chunk = (List) textChunkList.get(i);
            //System.out.println("Chapter " + (i + 1) + ":");
            for (int j = 0; j < chunk.size(); ++j) {
                if (j == 0) {
                    chapterStartId = j;
                }
                // process chapter 1
                if (i == 0) {
                    //  System.out.println(j + ": " + chunk.get(j));
                    String readLine = (String) chunk.get(j);
                    if (readLine.startsWith("第")) {
                        String[] contentChapter = readLine.split("\\s+");
                        Chapter newChapter = new Chapter(readLine);
                        newChapter.setTitle(contentChapter[0]);
                        newChapter.setText(contentChapter[1]);
                        System.out.println("newChapter title: " + newChapter.getTitle() + ". text is : " + newChapter.getText());
                    }
                }
                // process chapter 2
                if (i == 1) {
                    //System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 3
                if (i == 2) {
                    //System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 4
                if (i == 3) {
                    //System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 5
                if (i == 4) {
                    //System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 6
                if (i == 5) {
                    // System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 7
                if (i == 6) {
                    /// System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 8
                if (i == 7) {
                    // System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 9
                if (i == 8) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 10
                if (i == 9) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 11
                if (i == 10) {
                    // System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 12
                if (i == 11) {
                    // System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 13
                if (i == 12) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 11
                if (i == 13) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 15
                if (i == 14) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 16
                if (i == 15) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 17
                if (i == 16) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 18
                if (i == 17) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 19
                if (i == 18) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 20
                if (i == 19) {
                    //   System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 21
                if (i == 20) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 22
                if (i == 21) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 23
                if (i == 22) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                // process chapter 24
                if (i == 23) {
                    //  System.out.println(j + ": " + chunk.get(j));
                }
                //System.out.println(chunk.get(j));
            }
        }
        FundDoc fundDoc = new FundDoc("");
        return fundDoc;
    }
}