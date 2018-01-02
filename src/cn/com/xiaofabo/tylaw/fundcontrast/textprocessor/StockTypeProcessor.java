/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.Chapter;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.Section;
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
    public FundDoc process() {
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

        //process section
        for (Chapter c : fundDoc.getChapters()) {
            chunk = (List) textChunkList.get(index);
            List<Integer> secStatus = secLineNumber(chunk);
            if (!secStatus.isEmpty()) {
                List<Section> sectionList = new ArrayList<Section>();
                int lineNumber = 0;
                for (int i = 0; i < secStatus.size(); i++) {
                    lineNumber = secStatus.get(i);
                    try {
                        sectionList.add(processSection(chunk, lineNumber, i));
                    } catch (SectionIncorrectException e) {
                        e.printStackTrace();
                    }
                }
                c.setSections(sectionList);
            }
            index++;
        }

        //printout current structured fundDoc
        System.out.println("FundDoc: " + fundDoc.getTitle());
        for (Chapter c : fundDoc.getChapters()) {
            //System.out.println("Chapter: " + c.getTitle() + "\nText: " + c.getText());
            for (Section s : c.getSections()) {
                System.out.println("Section: " + s.getText());
            }
            System.out.println("------------End-------------");
        }
        return fundDoc;
    }

    /**
     * @param chunk
     * @return Chapter entity
     * @throws ChapterIncorrectException
     */
    private Chapter processChapter(List chunk) throws ChapterIncorrectException {
        String title = "";
        String text = "";
        String currentLine = "";
        Chapter newChapter = new Chapter(title);
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
            } else {
                break;
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
    private Section processSection(List chunk, int secStartId, int index) throws SectionIncorrectException {
        String currentLine;
        String text = "";
        Section newSection = new Section();

        for (int i = secStartId; i < chunk.size() && index < this.sectionTitles.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            // currentLine = "1、订立本基金合同的目的是保护投资人合法权益，明确基金合同当事人的权利义务，规范基金运作。";
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            String[] tmp = currentLine.split(this.sectionTitles.get(index));
            if (tmp.length >= 2) {
                text = tmp[1];
            } else if ((!currentLine.startsWith("1、")) && (!currentLine.contains("^[1-9]\\d*、")) && (!currentLine.contains("（[\\u4e00-\\u9fa5]）"))) {
                text += currentLine.trim();
                newSection.setText(text);
                break;
            }
        }
        return newSection;
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