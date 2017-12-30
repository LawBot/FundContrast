/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.Chapter;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.Section;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.ChapterNotCorrectException;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.SectionNotCorrectException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class StockTypeProcessor extends TextProcessor {

    FundDoc fundDoc = new FundDoc("");
    private int sectionStartId = 0;
    private List<String> sectionTitles = new ArrayList<String>();
    private int sectionCounter = 0;

    /**
     * Constructor
     */
    public StockTypeProcessor() {
        this.sectionTitles.add("一、");
        this.sectionTitles.add("二、");
        this.sectionTitles.add("三、");
        this.sectionTitles.add("四、");
        this.sectionTitles.add("五、");
        this.sectionTitles.add("六、");
        this.sectionTitles.add("七、");
        this.sectionTitles.add("八、");
    }

    /**
     * @return FundDoc entity
     */
    public FundDoc process() {
        List textList = super.getLines();
        List textChunkList = new LinkedList<>();
        int startIdx = 0;
        int chapterStartId = 0;

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
        String currentLine = "";
        //  for (int i = 0; i < textChunkList.size(); ++i) {

        for (int i = 0; i < 1; ++i) {
            chunk = (List) textChunkList.get(i);
            //System.out.println("Processing Chapter " + (i + 1) + ": " + chunk);
            //System.out.println("SOOOO");
            for (int j = 0; j < chunk.size(); ++j) {
                if (j == 0) {
                    chapterStartId = j;
                }
                currentLine = (String) chunk.get(j);
                if (j == 0) {
                    try {
                        processChapter(chunk);
                    } catch (ChapterNotCorrectException e) {
                        //TODO
                    }
                }
                // process sections
                if (currentLine.startsWith("一、")) {
                    List<Integer> lineNumbers = sectionCount(chunk);
                    try {
                        for (int k = 0; k < lineNumbers.size(); k++) {
                            System.out.println(lineNumbers.get(k) + "  " + k);
                            Section tmp = processSection(chunk, lineNumbers.get(k), k);
                            System.out.println(tmp.getTitle() + " the text is: " + tmp.getText());
                        }
                    } catch (SectionNotCorrectException e) {
                        //TODO
                    }
                }
                //process sub-sections
                if (currentLine.startsWith("(一)")) {

                }
            }
            //System.out.println("Processing Finish");
        }
        return fundDoc;
    }

    /**
     * @param chunk
     * @return Chapter entity
     * @throws ChapterNotCorrectException
     */
    private Chapter processChapter(List chunk) throws ChapterNotCorrectException {
        String title = "";
        String text = "";
        String currentLine = "";
        Chapter newChapter = new Chapter(title);
        for (int i = 0, j = 0; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (i == 0) {
                String[] tmp = currentLine.split("\\s+");
                if (tmp.length >= 2) {
                    newChapter.setText(text);
                    title = tmp[1];
                    newChapter.setTitle(title);
                    //System.out.println("Chapter:" + title);
                    continue;
                } else {
                    throw new ChapterNotCorrectException();
                }
            }
            if ((!currentLine.startsWith("一、")) && (!currentLine.startsWith("1、")) && (!currentLine.startsWith("（一）"))) {
                text += currentLine.trim();
            } else {
                sectionStartId = i;
                newChapter.setText(text);
                //System.out.println(" Chapter text: " + text + " ,id is :" + sectionStartId + " Get this: " + chunk.get(sectionStartId));
                break;
            }
        }
        return newChapter;
    }

    /**
     * @param chunk
     * @param lineNumber
     * @param sectionNo
     * @return Section entity
     * @throws SectionNotCorrectException
     */
    private Section processSection(List chunk, int lineNumber, int sectionNo) throws SectionNotCorrectException {
        String title = "";
        String text = "";
        String currentLine = "";
        Section newSection = new Section();
        for (int i = lineNumber; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            String[] tmp = currentLine.split(this.sectionTitles.get(sectionNo));
            if (tmp.length >= 2) {
                title = tmp[0] + this.sectionTitles.get(sectionNo);
                this.sectionCounter++;
                newSection.setTitle(title);
                text = tmp[1];
            } else if ((!currentLine.startsWith("^[0-9]*$、")) && (!currentLine.startsWith("（一）"))) {
                text += currentLine.trim();
                newSection.setText(text);
                break;
            }
        }
        return newSection;
    }

    /**
     * @param chunk
     * @return List of section number
     */
    private List<Integer> sectionCount(List chunk) {
        List<Integer> lineNumberOfSection = new ArrayList<Integer>();
        this.sectionCounter = 0;
        for (int k = 0; k < chunk.size(); k++) {
            if (chunk.get(k).toString().trim().contains(this.sectionTitles.get(this.sectionCounter))) {
                lineNumberOfSection.add(k);
                this.sectionCounter++;
            }
        }
        return lineNumberOfSection;
    }
}