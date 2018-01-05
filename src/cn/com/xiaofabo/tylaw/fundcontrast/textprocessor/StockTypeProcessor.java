/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.Chapter;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.Section;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.SubSection;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.ChapterIncorrectException;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.SectionIncorrectException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class StockTypeProcessor extends TextProcessor {

    FundDoc fundDoc = new FundDoc("（2012-12-17）证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）");
    private List<String> sectionTitles = new ArrayList<String>();

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
    public FundDoc process() throws SectionIncorrectException {
        List textList = super.getLines();
        List textChunkList = new LinkedList<>();
        int startIdx = 0;
        List chunk;
        List<Chapter> chapters = new ArrayList<>();
        int index = 0;

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
        }
        chunk = textList.subList(startIdx, textList.size() - 1);
        textChunkList.add(chunk);

        //process chapter
        for (int i = 0; i < textChunkList.size(); ++i) {
            chunk = (List) textChunkList.get(i);
            try {
                chapters.add(processChapter(chunk));
            } catch (ChapterIncorrectException e) {
                e.printStackTrace();
            }
        }
        fundDoc.setChapters(chapters);

        //printout current structured fundDoc
        System.out.println("FundDoc: " + fundDoc.getTitle());
        for (Chapter c : fundDoc.getChapters()) {
            System.out.println("Chapter: " + c.getTitle() + "\nText: " + c.getText());
//            for (Section s : c.getSections()) {
//                System.out.println("Section: " + s.getText());
//            }
            System.out.println("------------End-------------");
        }
        return fundDoc;
    }

    /**
     * @param chunk
     * @return Chapter entity
     * @throws ChapterIncorrectException
     */
    private Chapter processChapter(List chunk) throws ChapterIncorrectException, SectionIncorrectException {
        String title = "";
        String text = "";
        String currentLine = "";
        Chapter newChapter = new Chapter(title);
        int StartSectionId = 0;
        for (int i = 0, j = 0; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            if (i == 0) {
                String[] tmp = currentLine.split("\\s+");
                if (tmp.length >= 2) {
                    title = tmp[1].trim();
                    newChapter.setTitle(title);
                    continue;
                } else {
                    throw new ChapterIncorrectException();
                }
            }
            if ((!currentLine.startsWith("一、")) && (!currentLine.startsWith("1、")) && (!currentLine.startsWith("(一)"))) {
                text += currentLine.trim();
            } else if (currentLine.startsWith("一、")) {
                StartSectionId = i;
                processSection(chunk, StartSectionId, "一、");
            } else if (currentLine.startsWith("1、")) {
                StartSectionId = i;
                processSection(chunk, StartSectionId, "1、");
            } else if (currentLine.startsWith("(一)")) {
                StartSectionId = i;
                processSection(chunk, StartSectionId, "(一)");
            } else {

            }
            newChapter.setText(text);
        }
        return newChapter;
    }

    /**
     * @param chunk
     * @return Section entity
     * @throws SectionIncorrectException
     */
    private Section processSection(List chunk, int secStartId, String signalOfSection) throws SectionIncorrectException {
        String currentLine;
        String text = "";
        Section newSection = new Section();
        String sectionType = signalOfSection;

        Pattern chinese = Pattern.compile("[\\u4e00-\\u9fa5]、");
        Pattern chineseBraces = Pattern.compile("（[\\u4e00-\\u9fa5]）");
        Pattern arabNumbers = Pattern.compile("^[1-9]\\d*、");

        Matcher mChinese = chinese.matcher(sectionType);
        Matcher mChineseBraces = chineseBraces.matcher(sectionType);
        Matcher mArabNumbers = arabNumbers.matcher(sectionType);


        for (int i = secStartId; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }

            if (mChinese.find()) {
                if ((!currentLine.startsWith("1、")) && (!currentLine.startsWith("(一)")) && (currentLine.split("[\u4e00-\u9fa5]、").length == 2)) {
                    text += currentLine;
                    newSection.setText(text);
                } else if (currentLine.split("[\u4e00-\u9fa5]、").length == 2) {
                    // TODO process sibling section
                } else if (currentLine.startsWith("1、")) {
                    processSubSection(chunk, i, "1、");
                } else if (currentLine.startsWith("(一)")) {
                    processSubSection(chunk, i, "(一)");
                }
            }
            if (mChineseBraces.find()) {
                if ((!currentLine.startsWith("(一)"))) {
                    text += currentLine;
                }
            }
            if (mArabNumbers.find()) {
                if ((!currentLine.startsWith("1、"))) {
                    text += currentLine;
                }
            }
            newSection.setText(text);
        }
        return newSection;
    }

    /**
     * TODO
     *
     * @param chunk
     * @param startSubSectionId
     * @param type
     * @return
     */
    private SubSection processSubSection(List chunk, int startSubSectionId, String type) {
        SubSection newSubSection = new SubSection();
        String currentLine;
        String text = "";
        String subSectionType = type;
        int start = startSubSectionId;

        Pattern chineseBraces = Pattern.compile("（[\\u4e00-\\u9fa5]）");
        Pattern arabNumbers = Pattern.compile("^[1-9]\\d*、");

        Matcher mChineseBraces = chineseBraces.matcher(subSectionType);
        Matcher mArabNumbers = arabNumbers.matcher(subSectionType);

        for (int i = start; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            if (mChineseBraces.find()) {
                if ((!currentLine.startsWith("1、"))) {
                    text += currentLine;
                }
            }
            if (mArabNumbers.find()) {
                if ((!currentLine.startsWith("（1）"))) {
                    text += currentLine;
                }
            }
            newSubSection.setText(text);
        }
        return newSubSection;
    }

    private List<Integer> secLineNumber(List chunk) {
        List<Integer> lineNumberOfSection = new ArrayList();
        int j = 0;
        for (int k = 0; k < chunk.size() && j < this.sectionTitles.size(); k++) {
            if (chunk.get(k).toString().trim().contains(this.sectionTitles.get(j))) {
                lineNumberOfSection.add(k);
                j++;
            }
        }
        return lineNumberOfSection;
    }
}