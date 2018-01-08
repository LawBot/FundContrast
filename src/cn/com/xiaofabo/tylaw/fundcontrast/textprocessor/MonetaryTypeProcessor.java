/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.*;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.ChapterIncorrectException;
import cn.com.xiaofabo.tylaw.fundcontrast.util.TextUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class MonetaryTypeProcessor extends TextProcessor {
    FundDoc fundDoc;


    public MonetaryTypeProcessor(String titleOfDoc) {
        this.fundDoc = new FundDoc(titleOfDoc);
    }

    public FundDoc process() {
        List textList = super.getLines();
        List textChunkList = new LinkedList<>();
        int startIdx = 0;
        List chunk;
        List<Chapter> chapters = new LinkedList<>();
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
        chunk = textList.subList(startIdx, textList.size());
        textChunkList.add(chunk);

        // process chapter
        for (int i = 0; i < textChunkList.size(); ++i) {
            chunk = (List) textChunkList.get(i);
            try {
                chapters.add(processChapter(chunk));
            } catch (ChapterIncorrectException e) {
                e.printStackTrace();
            }
        }
        fundDoc.setChapters(chapters);
        return fundDoc;
    }

    private Chapter processChapter(List chunk) throws ChapterIncorrectException {
        List chapterStrChunk = TextUtils.removeAllEmptyLines(chunk);
        String firstLine = (String) chapterStrChunk.get(0);

        int chapterIdx = TextUtils.getChapterIndex(firstLine);

        int type = 0;
        switch (chapterIdx) {
            case 2:
            case 22: {
                type = 2;
            }
            break;
            case 7:
            case 9:
            case 18: {
                type = 3;
            }
            break;
            default: {
                type = 1;
            }
            break;
        }
        return processChapter(chapterStrChunk, type);
    }

    private Chapter processChapter(List chapterStrChunk, int type) {
        Chapter c = null;
        switch (type) {
            case 1:
                c = chapterStrType1(chapterStrChunk);
                break;
            case 2:
                c = chapterStrType2(chapterStrChunk);
                break;
            case 3:
                c = chapterStrType3(chapterStrChunk);
                break;
            default:
                c = null;
                break;
        }
        return c;
    }

    private Chapter chapterStrType1(List chapterStrChunk) {
        String title = TextUtils.getChapterTitle((String) chapterStrChunk.get(0));
        Chapter c = new Chapter(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != chapterStrChunk.size()) {
            String currentLine = ((String) chapterStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("一、")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String chapterText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        c.setText(chapterText);

        int chapterIdx = TextUtils.getChapterIndex((String) chapterStrChunk.get(0));

        List<String> txtChunk;
        List<List> sectionStrChunkList = new LinkedList<>();
        int startIdx = 0;
        for (int i = lineIdx; i < chapterStrChunk.size(); ++i) {
            Pattern pattern = Pattern.compile("^[一|二|三|四|五|六|七|八|九|十|"
                    + "十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|"
                    + "二十一|二十二|二十三|二十四|二十五|二十六|二十七|二十八|二十九|]、");
            Matcher matcher = pattern.matcher((String) chapterStrChunk.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = chapterStrChunk.subList(startIdx, i);
                    startIdx = i;
                    sectionStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = chapterStrChunk.subList(startIdx, chapterStrChunk.size());
            sectionStrChunkList.add(txtChunk);
        }

        List<Section> sectionList = new LinkedList<>();

        for (int i = 0; i < sectionStrChunkList.size(); ++i) {
            List sectionStrList = (List) sectionStrChunkList.get(i);
            Section s = processSection(sectionStrList);
            sectionList.add(s);
        }
        c.setSections(sectionList);
        return c;
    }

    private Chapter chapterStrType2(List chapterStrChunk) {
        String title = TextUtils.getChapterTitle((String) chapterStrChunk.get(0));
        Chapter c = new Chapter(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != chapterStrChunk.size()) {
            String currentLine = ((String) chapterStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("1、")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String chapterText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        c.setText(chapterText);

        List<Section> secList = new ArrayList<>();
        List<List> secChunkList = new ArrayList<>();

        Section sec = new Section();
        String currentLine = "";
        int startIdx = 0;
        List<String> txtChunk;
        for (int i = lineIdx; i < chapterStrChunk.size(); ++i) {
            Pattern pattern = Pattern.compile("^\\d+、");
            Matcher matcher = pattern.matcher((String) chapterStrChunk.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = chapterStrChunk.subList(startIdx, i);
                    startIdx = i;
                    secChunkList.add(txtChunk);
                }
            }
        }
        for (List<String> s : secChunkList) {
            secList.add(processType2Section(s));
        }
        c.setSections(secList);
        return c;
    }

    private Chapter chapterStrType3(List chapterStrChunk) {
        String title = TextUtils.getChapterTitle((String) chapterStrChunk.get(0));
        Chapter c = new Chapter(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != chapterStrChunk.size()) {
            String currentLine = ((String) chapterStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("一、")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String chapterText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        c.setText(chapterText);


        List<String> txtChunk;
        List<List> sectionStrChunkList = new LinkedList<>();
        int startIdx = 0;

        // split section chunks into list
        for (int i = lineIdx; i < chapterStrChunk.size(); ++i) {
            Pattern pattern = Pattern.compile("^[一|二|三|四|五|六|七|八|九|十|"
                    + "十一|十二|十三|十四|十五|十六|十七|十八|十九|二十|"
                    + "二十一|二十二|二十三|二十四|二十五|二十六|二十七|二十八|二十九|]、");
            Matcher matcher = pattern.matcher((String) chapterStrChunk.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = chapterStrChunk.subList(startIdx, i);
                    startIdx = i;
                    sectionStrChunkList.add(txtChunk);
                }
            }
        }

        if (startIdx != 0) {
            txtChunk = chapterStrChunk.subList(startIdx, chapterStrChunk.size());
            sectionStrChunkList.add(txtChunk);
        }
        List<Section> sectionList = new LinkedList<>();
        for (int i = 0; i < sectionStrChunkList.size(); ++i) {
            List sectionStrList = (List) sectionStrChunkList.get(i);
            // process sections seperately
            Section s = processType3Section(sectionStrList);
            sectionList.add(s);
        }

        c.setSections(sectionList);
        return c;
    }


    private Section processSection(List sectionStrChunk) {
        Section s = new Section();

        String title = TextUtils.getSectionTitle((String) sectionStrChunk.get(0));
//        System.out.println("\tSection Title: " + title);
        s.setTitle(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != sectionStrChunk.size()) {
            String currentLine = ((String) sectionStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("1、")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String sectionText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        s.setText(sectionText);
//        System.out.println("\tSection Text: " + sectionText);

        List<String> txtChunk;
        List<List> subsecStrChunkList = new LinkedList();

        int startIdx = 0;
        for (int i = lineIdx; i < sectionStrChunk.size(); ++i) {
//            System.out.println(chapterStrChunk.get(i));
            Pattern pattern = Pattern.compile("^\\d+、");
            Matcher matcher = pattern.matcher((String) sectionStrChunk.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = sectionStrChunk.subList(startIdx, i);
                    startIdx = i;
                    subsecStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = sectionStrChunk.subList(startIdx, sectionStrChunk.size());
            subsecStrChunkList.add(txtChunk);
        }

        List<SubSection> subsecList = new LinkedList<>();

        for (int i = 0; i < subsecStrChunkList.size(); ++i) {
            List subsecStrList = (List) subsecStrChunkList.get(i);
            SubSection ss = processSubSection(subsecStrList);
            subsecList.add(ss);
        }
        s.setSubSections(subsecList);
        return s;
    }

    private Section processType2Section(List<String> secChunk) {
        String tmpTitle = "";
        String currentLine = "";
        String tmpText = "";
        Section secS = new Section();
        for (int m = 0; m < secChunk.size(); m++) {
            currentLine = secChunk.get(m).trim();
            if (m == 0) {
                tmpTitle = currentLine.split("^\\d+、")[1];
                secS.setTitle(tmpTitle);
            } else {
                tmpText += currentLine;
            }
        }
        secS.setText(tmpText);
        return secS;
    }

    private Section processType3Section(List<String> sectionStrChunk) {
        Section s = new Section();
        String title = TextUtils.getSectionTitle((String) sectionStrChunk.get(0));
        s.setTitle(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != sectionStrChunk.size()) {
            String currentLine = ((String) sectionStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("（一）")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String sectionText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        s.setText(sectionText);

        List<String> txtChunk;
        List<List> subsecStrChunkList = new LinkedList();

        int startIdx = 0;
        for (int i = lineIdx; i < sectionStrChunk.size(); ++i) {
            Pattern pattern = Pattern.compile("^（[一|二|三|四|五|六|七|八|九|十|]）");
            Matcher matcher = pattern.matcher((String) sectionStrChunk.get(i));

            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = sectionStrChunk.subList(startIdx, i);
                    startIdx = i;
                    subsecStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = sectionStrChunk.subList(startIdx, sectionStrChunk.size());
            subsecStrChunkList.add(txtChunk);
        }
        List<SubSection> subsecList = new LinkedList<>();

        for (int i = 0; i < subsecStrChunkList.size(); ++i) {
            List subsecStrList = (List) subsecStrChunkList.get(i);
            SubSection ss = processType3SubSection(subsecStrList);
            subsecList.add(ss);
        }
        s.setSubSections(subsecList);
        return s;
    }

    private SubSection processSubSection(List subsecStrList) {
        SubSection ss = new SubSection();

//        System.out.println("\t\tSub-Section Text:");
//        for(int i = 0; i < subsecStrList.size(); ++i){
//            System.out.println("\t\t" + subsecStrList.get(i));
//        }
        String title = TextUtils.getSubSectionTitle((String) subsecStrList.get(0));
//        System.out.println("\t\tSub-Section Title: " + title);
        ss.setTitle(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != subsecStrList.size()) {
            String currentLine = ((String) subsecStrList.get(lineIdx)).trim();
            if (!currentLine.startsWith("（1）")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String subsecText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        ss.setText(subsecText);
//        System.out.println("\t\tSub-Section Text: " + subsecText);

        List<String> txtChunk;
        List<List> subsubsecStrChunkList = new LinkedList();

        int startIdx = 0;
        for (int i = lineIdx; i < subsecStrList.size(); ++i) {
//            System.out.println(chapterStrChunk.get(i));
            Pattern pattern = Pattern.compile("^（\\d+）");
            Matcher matcher = pattern.matcher((String) subsecStrList.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = subsecStrList.subList(startIdx, i);
                    startIdx = i;
                    subsubsecStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = subsecStrList.subList(startIdx, subsecStrList.size());
            subsubsecStrChunkList.add(txtChunk);
        }

        List<SubSubSection> subsubsecList = new LinkedList<>();

        for (int i = 0; i < subsubsecStrChunkList.size(); ++i) {
            List subsubsecStrList = (List) subsubsecStrChunkList.get(i);
            SubSubSection sss = processSubSubSection(subsubsecStrList);
            subsubsecList.add(sss);
            /// --------------------------------------------------------------------
//            for (int j = 0; j < subsubsecStrList.size(); ++j) {
//                System.out.println((String) subsubsecStrList.get(j));
//            }
//            System.out.println("\t\t\t***********************************************");
            /// --------------------------------------------------------------------
        }
        ss.setSubSubSections(subsubsecList);
        return ss;
    }

    private SubSection processType3SubSection(List sectionStrChunk) {
        SubSection s = new SubSection();

        String title = TextUtils.getType3SubSectionTitle((String) sectionStrChunk.get(0));
        s.setTitle(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != sectionStrChunk.size()) {
            String currentLine = ((String) sectionStrChunk.get(lineIdx)).trim();
            if (!currentLine.startsWith("1、")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String sectionText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        s.setText(sectionText);
        List<String> txtChunk;
        List<List> subsecStrChunkList = new LinkedList();

        int startIdx = 0;
        for (int i = lineIdx; i < sectionStrChunk.size(); ++i) {
//            System.out.println(chapterStrChunk.get(i));
            Pattern pattern = Pattern.compile("^\\d+、");
            Matcher matcher = pattern.matcher((String) sectionStrChunk.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = sectionStrChunk.subList(startIdx, i);
                    startIdx = i;
                    subsecStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = sectionStrChunk.subList(startIdx, sectionStrChunk.size());
            subsecStrChunkList.add(txtChunk);
        }

        List<SubSubSection> subsecList = new LinkedList<>();

        for (int i = 0; i < subsecStrChunkList.size(); ++i) {
            List subsecStrList = (List) subsecStrChunkList.get(i);
            SubSubSection ss = processType3SubSubSection(subsecStrList);
            subsecList.add(ss);
        }
        s.setSubSubSections(subsecList);
        return s;
    }

    private SubSubSection processSubSubSection(List subsubsecStrList) {
        StringBuilder tmpText = new StringBuilder();

        SubSubSection sss = new SubSubSection();
        String firstLine = (String) subsubsecStrList.get(0);
        int start = firstLine.indexOf("）");
        String tmpLine = firstLine.substring(start + 1).trim();
        tmpText.append(tmpLine);

        int lineIdx = 1;
        while (lineIdx != subsubsecStrList.size()) {
            String currentLine = ((String) subsubsecStrList.get(lineIdx)).trim();
            tmpText.append(currentLine);
            ++lineIdx;
        }
        String subsecText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        sss.setText(subsecText);
        return sss;
    }

    private SubSubSection processType3SubSubSection(List subsecStrList) {
        SubSubSection ss = new SubSubSection();

        String title = TextUtils.getSubSectionTitle((String) subsecStrList.get(0));
        ss.setTitle(title);

        int lineIdx = 1;
        StringBuilder tmpText = new StringBuilder();
        while (lineIdx != subsecStrList.size()) {
            String currentLine = ((String) subsecStrList.get(lineIdx)).trim();
            if (!currentLine.startsWith("（1）")) {
                tmpText.append(currentLine);
                ++lineIdx;
            } else {
                break;
            }
        }
        String subsecText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        ss.setText(subsecText);

        List<String> txtChunk;
        List<List> subsubsecStrChunkList = new LinkedList();

        int startIdx = 0;
        for (int i = lineIdx; i < subsecStrList.size(); ++i) {
            Pattern pattern = Pattern.compile("^（\\d+）");
            Matcher matcher = pattern.matcher((String) subsecStrList.get(i));
            while (matcher.find()) {
                if (startIdx == 0) {
                    startIdx = i;
                } else {
                    txtChunk = subsecStrList.subList(startIdx, i);
                    startIdx = i;
                    subsubsecStrChunkList.add(txtChunk);
                }
            }
        }
        if (startIdx != 0) {
            txtChunk = subsecStrList.subList(startIdx, subsecStrList.size());
            subsubsecStrChunkList.add(txtChunk);
        }

        List<TextPoint> subsubsecList = new LinkedList<>();

        for (int i = 0; i < subsubsecStrChunkList.size(); ++i) {
            List subsubsecStrList = (List) subsubsecStrChunkList.get(i);
            TextPoint sss = processTextPoint(subsubsecStrList);
            subsubsecList.add(sss);
        }
        ss.setTextPoints(subsubsecList);
        return ss;

    }

    private TextPoint processTextPoint(List subsubsecStrList) {
        StringBuilder tmpText = new StringBuilder();

        TextPoint sss = new TextPoint();
        String firstLine = (String) subsubsecStrList.get(0);
        int start = firstLine.indexOf("）");
        String tmpLine = firstLine.substring(start + 1).trim();
        tmpText.append(tmpLine);

        int lineIdx = 1;
        while (lineIdx != subsubsecStrList.size()) {
            String currentLine = ((String) subsubsecStrList.get(lineIdx)).trim();
            tmpText.append(currentLine);
            ++lineIdx;
        }
        String subsecText = tmpText.toString().isEmpty() ? null : tmpText.toString();
        sss.setText(subsecText);
        return sss;
    }

}
