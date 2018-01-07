/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.*;
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
    Pattern chinese = Pattern.compile("[\\u4e00-\\u9fa5]、");
    Pattern chineseBraces = Pattern.compile("（[\\u4e00-\\u9fa5]）");
    Pattern arabNumbers = Pattern.compile("^[1-9]\\d*、");
    Pattern arabNumberBraces = Pattern.compile("（[1-9]\\d*）");

    private List<String> sectionTitles = new ArrayList<String>();

    private List<String> subSectionTitles = new ArrayList<String>();

    private List<String> subSubSectionTitles = new ArrayList<String>();

    private List<String> textPointTitles = new ArrayList<String>();


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

        this.subSectionTitles.add("（一）");
        this.subSectionTitles.add("（二）");
        this.subSectionTitles.add("（三）");
        this.subSectionTitles.add("（四）");
        this.subSectionTitles.add("（五）");
        this.subSectionTitles.add("（六）");
        this.subSectionTitles.add("（七）");
        this.subSectionTitles.add("（八）");
        this.subSectionTitles.add("（九）");
        this.subSectionTitles.add("（十）");

        this.subSubSectionTitles.add("1、");
        this.subSubSectionTitles.add("2、");
        this.subSubSectionTitles.add("3、");
        this.subSubSectionTitles.add("4、");
        this.subSubSectionTitles.add("5、");
        this.subSubSectionTitles.add("6、");
        this.subSubSectionTitles.add("7、");
        this.subSubSectionTitles.add("8、");
        this.subSubSectionTitles.add("9、");
        this.subSubSectionTitles.add("10、");
        this.subSubSectionTitles.add("11、");
        this.subSubSectionTitles.add("12、");
        this.subSubSectionTitles.add("13、");
        this.subSubSectionTitles.add("14、");
        this.subSubSectionTitles.add("15、");
        this.subSubSectionTitles.add("16、");
        this.subSubSectionTitles.add("17、");
        this.subSubSectionTitles.add("18、");
        this.subSubSectionTitles.add("19、");
        this.subSubSectionTitles.add("20、");
        this.subSubSectionTitles.add("21、");
        this.subSubSectionTitles.add("22、");
        this.subSubSectionTitles.add("23、");
        this.subSubSectionTitles.add("24、");
        this.subSubSectionTitles.add("25、");
        this.subSubSectionTitles.add("26、");
        this.subSubSectionTitles.add("27、");
        this.subSubSectionTitles.add("28、");
        this.subSubSectionTitles.add("29、");
        this.subSubSectionTitles.add("30、");
        this.subSubSectionTitles.add("31、");
        this.subSubSectionTitles.add("32、");
        this.subSubSectionTitles.add("33、");
        this.subSubSectionTitles.add("34、");
        this.subSubSectionTitles.add("35、");
        this.subSubSectionTitles.add("36、");
        this.subSubSectionTitles.add("37、");
        this.subSubSectionTitles.add("38、");
        this.subSubSectionTitles.add("39、");
        this.subSubSectionTitles.add("40、");
        this.subSubSectionTitles.add("41、");
        this.subSubSectionTitles.add("42、");
        this.subSubSectionTitles.add("43、");
        this.subSubSectionTitles.add("44、");
        this.subSubSectionTitles.add("45、");
        this.subSubSectionTitles.add("46、");
        this.subSubSectionTitles.add("47、");
        this.subSubSectionTitles.add("48、");
        this.subSubSectionTitles.add("49、");
        this.subSubSectionTitles.add("50、");
        this.subSubSectionTitles.add("51、");
        this.subSubSectionTitles.add("52、");

        this.textPointTitles.add("（1）");
        this.textPointTitles.add("（2）");
        this.textPointTitles.add("（3）");
        this.textPointTitles.add("（4）");
        this.textPointTitles.add("（5）");
        this.textPointTitles.add("（6）");
        this.textPointTitles.add("（7）");
        this.textPointTitles.add("（8）");
        this.textPointTitles.add("（9）");
        this.textPointTitles.add("（10）");
        this.textPointTitles.add("（11）");
        this.textPointTitles.add("（12）");
        this.textPointTitles.add("（13）");
        this.textPointTitles.add("（14）");
        this.textPointTitles.add("（15）");
        this.textPointTitles.add("（16）");
        this.textPointTitles.add("（17）");
        this.textPointTitles.add("（18）");
        this.textPointTitles.add("（19）");
        this.textPointTitles.add("（20）");
        this.textPointTitles.add("（21）");
        this.textPointTitles.add("（22）");
        this.textPointTitles.add("（23）");
        this.textPointTitles.add("（24）");
        this.textPointTitles.add("（25）");
        this.textPointTitles.add("（26）");
        this.textPointTitles.add("（27）");
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

        //printout current structured fundDoc
        System.out.println("FundDoc: " + fundDoc.getTitle());
        for (Chapter c : fundDoc.getChapters()) {
            System.out.println("Chapter: " + c.getTitle() + "\nText: " + c.getText());
            for (Section s : c.getSections()) {
                System.out.println("Section: " + s.getText());
//                for (SubSection x : s.getSubSections()) {
//                    System.out.println("SubSection: " + x.getText());
//                    for (SubSubSection y : x.getSubSubSections()) {
//                        System.out.println("SubSubSection: " + y.getText());
//                        for (TextPoint p : y.getTextPoints()) {
//                            System.out.println("TextPoint: " + p.getText());
//                        }
//                    }
//                }
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
    private Chapter processChapter(List chunk) throws ChapterIncorrectException, SectionIncorrectException {
        String title = "";
        String text = "";
        Chapter newChapter = new Chapter(title);
        List<Section> secList = new ArrayList<>();
        Matcher matcher;

        String currentLine = "";
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
            if ((!currentLine.startsWith("一、")) && (!currentLine.startsWith("（一）") && (!currentLine.startsWith("1、")))) {
                text += currentLine.trim();
                newChapter.setText(text);
            } else if (currentLine.startsWith("一、")) {
                StartSectionId = i;
                List<Integer> sectionIds = countSiblings(chunk, StartSectionId, "一、");
                for (int id : sectionIds) {
                    secList.add(processSection(chunk, id, "一、"));
                }
                newChapter.setSections(secList);
                break;
            } else if (currentLine.startsWith("1、")) {
                StartSectionId = i;
                List<Integer> sectionIds = countSiblings(chunk, StartSectionId, "1");
                for (int id : sectionIds) {
                    secList.add(processSection(chunk, id, "1、"));
                }
                newChapter.setSections(secList);
                break;
            } else if (currentLine.startsWith("(一)")) {
                StartSectionId = i;
                List<Integer> sectionIds = countSiblings(chunk, StartSectionId, "(一)");
                for (int id : sectionIds) {
                    secList.add(processSection(chunk, id, "(一)"));
                }
                newChapter.setSections(secList);
                break;
            }
        }
        return newChapter;
    }

    private List<Integer> countSiblings(List chunk, int startId, String type) {
        List<Integer> lineNumberOfSection = new ArrayList();
        int j = 0;
        Matcher mC = chinese.matcher(type);
        Matcher mCB = chineseBraces.matcher(type);
        Matcher mA = arabNumbers.matcher(type);
        Matcher mAB = arabNumberBraces.matcher(type);

        if (mC.find()) {
            for (int k = 0; k < chunk.size() && j < this.sectionTitles.size(); k++) {
                if (chunk.get(k).toString().trim().contains(this.sectionTitles.get(j))) {
                    lineNumberOfSection.add(k);
                    j++;
                }
            }
        } else if (mCB.find()) {
            for (int k = 0; k < chunk.size() && j < this.subSectionTitles.size(); k++) {
                if (chunk.get(k).toString().trim().contains(this.subSectionTitles.get(j))) {
                    lineNumberOfSection.add(k);
                    j++;
                }
            }
        } else if (mA.find()) {
            for (int k = 0; k < chunk.size() && j < this.subSubSectionTitles.size(); k++) {
                if (chunk.get(k).toString().trim().contains(this.subSubSectionTitles.get(j))) {
                    lineNumberOfSection.add(k);
                    j++;
                }
            }
        } else if (mAB.find()) {
            for (int k = 0; k < chunk.size() && j < this.textPointTitles.size(); k++) {
                if (chunk.get(k).toString().trim().contains(this.textPointTitles.get(j))) {
                    lineNumberOfSection.add(k);
                    j++;
                }
            }
        }
        return lineNumberOfSection;
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
        int StartSubSectionId = 0;
        List<SubSection> subSecList = new ArrayList<>();

        Matcher mChinese = chinese.matcher(sectionType);
        Matcher mChineseBraces = chineseBraces.matcher(sectionType);
        Matcher mArabNumbers = arabNumbers.matcher(sectionType);

        for (int i = secStartId; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            //　中文数字ｓｅｃｔｉｏｎ处理
            if (mChinese.find()) {
                if (i == secStartId) {
                    text = currentLine.split("[\\u4e00-\\u9fa5]、")[1];
                    newSection.setText(text);
                } else if ((!currentLine.startsWith("1、")) && (!currentLine.startsWith("(一)")) && (currentLine.split("[\u4e00-\u9fa5]、").length != 2)) {
                    text += currentLine;
                    newSection.setText(text);
                } else if (currentLine.startsWith("1、")) {
                    StartSubSectionId = i;
                    List<Integer> sectionIds = countSiblings(chunk, StartSubSectionId, "1、");
                    for (int id : sectionIds) {
                        subSecList.add(processSubSection(chunk, id, "一、"));
                    }
                    newSection.setSubSections(subSecList);
                } else if (currentLine.startsWith("(一)")) {
                    StartSubSectionId = i;
                    List<Integer> sectionIds = countSiblings(chunk, StartSubSectionId, "(一)");
                    for (int id : sectionIds) {
                        subSecList.add(processSubSection(chunk, id, "(一)"));
                    }
                    newSection.setSubSections(subSecList);
                }
            }
            //TODO
            if (mChineseBraces.find()) {
                if ((!currentLine.startsWith("(一)"))) {
                    text += currentLine;
                }
            }
            //TODO
            if (mArabNumbers.find()) {
                if ((!currentLine.startsWith("1、"))) {
                    text += currentLine;
                }
            }
        }
        return newSection;
    }

//    private List<Section> processSectionSibling(List chunk, int startId, String titleOfSibling) {
//        String text = "";
//        String siblingStartStr = titleOfSibling;
//        List currentChunk = chunk;
//        Section[] newSection = new Section[50];
//        int start = startId;
//        String currentLine = "";
//        List<Section> siblingSections = new ArrayList<>();
//        List<Integer> siblingsIndex = countSiblings();
//        int index = 0;
//        int j = 0;
//        for (int i = start; i < siblingsIndex.size(); i++, j++) {
//            index = siblingsIndex.get(i);
//            currentLine = ((String) chunk.get(index)).trim();
//            text = currentLine.split("")[1];
//            newSection[j].setText(text);
//        }
//        for (int i = 0; i < j; i++) {
//            siblingSections.add(newSection[i]);
//        }
//        return siblingSections;
//    }

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
        List<SubSubSection> subSubSectionList = new ArrayList<>();
        int StartSubSubSectionId = 0;


        Matcher mChineseBraces = chineseBraces.matcher(subSectionType);
        Matcher mArabNumbers = arabNumbers.matcher(subSectionType);

        for (int i = start; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            if (mChineseBraces.find()) {
                if ((!currentLine.startsWith("1、")) && (currentLine.split("（[\u4e00-\u9fa5]）").length == 2)) {
                    text += currentLine;
                    newSubSection.setText(text);
                } else if (currentLine.startsWith("1、")) {
                    StartSubSubSectionId = i;
                    List<Integer> sectionIds = countSiblings(chunk, StartSubSubSectionId, "1、");
                    for (int id : sectionIds) {
                        subSubSectionList.add(processSubSubSection(chunk, id, "1、"));
                    }
                    newSubSection.setSubSubSections(subSubSectionList);
                }
            }
            //TODO
            else if (mArabNumbers.find()) {
                if ((!currentLine.startsWith("（1）"))) {
                    text += currentLine;
                }
            }
        }
        return newSubSection;
    }

    private SubSubSection processSubSubSection(List chunk, int subSubSectionStartId, String type) {
        SubSubSection newSubSubSection = new SubSubSection();
        String text = "";
        int start = subSubSectionStartId;
        String currentLine;
        List<TextPoint> textPointList = new ArrayList<>();
        int textPointStartId = 0;

        Matcher mArabNumbers = arabNumbers.matcher(type);
        Matcher mArabNumberBraces = arabNumberBraces.matcher(type);

        for (int i = start; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            if (mArabNumbers.find()) {
                if ((!currentLine.startsWith("1、")) && (currentLine.split("（[\u4e00-\u9fa5]）").length == 2)) {
                    text += currentLine;
                    newSubSubSection.setText(text);
                } else if (currentLine.startsWith("（1）")) {
                    textPointStartId = i;
                    List<Integer> sectionIds = countSiblings(chunk, textPointStartId, "（1）");
                    for (int id : sectionIds) {
                        textPointList.add(processTextPoint(chunk, id));
                    }
                    newSubSubSection.setTextPoints(textPointList);
                }

            } else if (mArabNumberBraces.find()) {
                //TODO
            }
        }
        return newSubSubSection;
    }

    private TextPoint processTextPoint(List chunk, int textPointStartId) {
        TextPoint newText = new TextPoint();
        String text = "";
        int start = textPointStartId;
        String currentLine = "";
        for (int i = start; i < chunk.size(); i++) {
            currentLine = ((String) chunk.get(i)).trim();
            if (currentLine == "" || currentLine == null) {
                continue;
            }
            if (currentLine.split("[\u4e00-\u9fa5]、").length != 2) {
                text += currentLine;
                newText.setText(text);
            } else if (currentLine.split("[\u4e00-\u9fa5]、").length == 2) {
                processTextPoint(chunk, i);
            }
        }
        return newText;
    }
}