package cn.com.xiaofabo.tylaw.fundcontrast.util;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.*;
import cn.com.xiaofabo.tylaw.fundcontrast.main.CompareTest2;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏 email ddl-15 at outlook.com
 * 
 * Modified on 2018-02-14
 */
public class GenerateCompareDoc {
    
    public static final BigInteger A4_WIDTH = BigInteger.valueOf(16840L);
    public static final BigInteger A4_LENGTH = BigInteger.valueOf(11900L);
    public static final BigInteger TABLE_WIDTH = BigInteger.valueOf(13040L);
    
    public static final String TABLE_HEADER_BGCOLOR = "808080";
    
    public static final BigInteger TABLE_COLUMN_1_WIDTH = BigInteger.valueOf(1133L);    /// ~2.0cm
    public static final BigInteger TABLE_COLUMN_2_WIDTH = BigInteger.valueOf(4820L);    /// ~8.5cm
    public static final BigInteger TABLE_COLUMN_3_WIDTH = BigInteger.valueOf(4253L);    /// ~7.5cm
    public static final BigInteger TABLE_COLUMN_4_WIDTH = BigInteger.valueOf(2551L);    /// ~4.5cm
    
    public static final String TABLE_COLUMN_1_TEXT = "章节";
    public static final String TABLE_COLUMN_2_TEXT = "《指引》条款";
    public static final String TABLE_COLUMN_3_TEXT = "《基金合同》条款";
    public static final String TABLE_COLUMN_4_TEXT = "修改理由";
    

    /**
     * Defined Constants.
     */
    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());
    String headerContent = "条文对照表测试文本";

    public int generate(String title, String leadingText, List<PatchDto> contrastList, String outputPath, List<String> listOfId) throws IOException {
        PropertyConfigurator.configure("log.properties");
        log.info("Create an empty document");
        int nRow = contrastList.size() + 1;
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File(outputPath + "/条文对照表.docx"));

        /// Document page setup
        pageSetup(document);
        /// Generate title text
        generateTitle(document, title);
        /// Generate leading text
        generateLeadingText(document, leadingText);

        /// Generate contrast table
        XWPFTable table = document.createTable(nRow, 4);
        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
        width.setType(STTblWidth.DXA);
        width.setW(TABLE_WIDTH);
        CTTblLayoutType type = table.getCTTbl().getTblPr().addNewTblLayout();
        type.setType(STTblLayoutType.FIXED);


        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.setRepeatHeader(true);
        tableRowOne.getTableCells().get(0).getCTTc().addNewTcPr().addNewShd().setFill(TABLE_HEADER_BGCOLOR);
        tableRowOne.getTableCells().get(1).getCTTc().addNewTcPr().addNewShd().setFill(TABLE_HEADER_BGCOLOR);
        tableRowOne.getTableCells().get(2).getCTTc().addNewTcPr().addNewShd().setFill(TABLE_HEADER_BGCOLOR);
        tableRowOne.getTableCells().get(3).getCTTc().addNewTcPr().addNewShd().setFill(TABLE_HEADER_BGCOLOR);
        tableRowOne.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_1_WIDTH);
        tableRowOne.getCell(0).setText(TABLE_COLUMN_1_TEXT);
        tableRowOne.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_2_WIDTH);
        tableRowOne.getCell(1).setText(TABLE_COLUMN_2_TEXT);
        tableRowOne.getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_3_WIDTH);
        tableRowOne.getCell(2).setText(TABLE_COLUMN_3_TEXT);
        tableRowOne.getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_4_WIDTH);
        tableRowOne.getCell(3).setText(TABLE_COLUMN_4_TEXT);

        for (int i = 0; i < listOfId.size(); i++) {
            String targetId = listOfId.get(i);

            PatchDto p = new PatchDto();
            for (PatchDto tmp : contrastList) {
                if (tmp.getPartId() == (targetId)) {
                    p = tmp;
                    break;
                }
            }

            XWPFTableRow tableRow = table.getRow(1 + i);
            //章节
            String column0 = "第" + p.getChapterIndex() + "章";
            tableRow.getCell(0).setText(column0);
            //type==change
            if (p.getChangeType() == "change") {
                // change: delete
                if (p.getRevisedDto() != null && p.getRevisedDto().getDeleteData() != null && p.getRevisedDto().getAddData() == null) {
                    Set set = p.getRevisedDto().getDeleteData().keySet();
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int j = 0; j < p.getOrignalText().length(); j++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
                        String currentLetter = Character.toString(p.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            runForEachLetter.setStrike(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int j = 0; j < p.getRevisedDto().getRevisedText().length(); j++) {
                        XWPFRun runForEachLetter = paragraph1.createRun();
                        String currentLetter = Character.toString(p.getRevisedDto().getRevisedText().charAt(j));
                        if (set.contains(j)) {
                            runForEachLetter.setStrike(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                }
                // change: add
                if (p.getRevisedDto() != null && p.getRevisedDto().getRevisedText() != null && p.getRevisedDto().getAddData() != null && p.getRevisedDto().getDeleteData() == null) {
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int k = 0; k < p.getOrignalText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
                        String currentLetter = Character.toString(p.getOrignalText().charAt(k));
                        runForEachLetter.setText(currentLetter);
                    }
                    Set set1 = p.getRevisedDto().getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int k = 0; k < p.getRevisedDto().getRevisedText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph1.createRun();
                        String currentLetter = Character.toString(p.getRevisedDto().getRevisedText().charAt(k));
                        if (set1.contains(k)) {
                            runForEachLetter.setBold(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                }
                // change: add + delete
                if (p.getRevisedDto() != null && p.getRevisedDto().getDeleteData() != null && p.getRevisedDto().getAddData() != null) {
                    Set set = p.getRevisedDto().getDeleteData().keySet();
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int j = 0; j < p.getOrignalText().length(); j++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
                        String currentLetter = Character.toString(p.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            runForEachLetter.setStrike(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                    Set set1 = p.getRevisedDto().getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int k = 0; k < p.getRevisedDto().getRevisedText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph1.createRun();
                        String currentLetter = Character.toString(p.getRevisedDto().getRevisedText().charAt(k));
                        if (set1.contains(k)) {
                            runForEachLetter.setBold(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                }
            }
            // type==add
            if (p.getChangeType() == "add") {
                XWPFTableCell cell = tableRow.getCell(2);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();
                for (int k = 0; k < p.getRevisedDto().getRevisedText().length(); k++) {
                    XWPFRun runForEachLetter = paragraph.createRun();
                    String currentLetter = Character.toString(p.getRevisedDto().getRevisedText().charAt(k));
                    runForEachLetter.setBold(true);
                    runForEachLetter.setText(currentLetter);
                }
            }

            // type==delete
            if (p.getChangeType() == "delete") {
                XWPFTableCell cell = tableRow.getCell(1);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();
                for (int k = 0; k < p.getOrignalText().length(); k++) {
                    XWPFRun runForEachLetter = paragraph.createRun();
                    String currentLetter = Character.toString(p.getOrignalText().charAt(k));
                    runForEachLetter.setStrike(true);
                    runForEachLetter.setText(currentLetter);
                }
            }
        }
        createHeader(document, headerContent);
        createFooter(document);
        document.write(out);
        out.close();
        
        return 0;
    }

    /**
     * @param doc
     */
    private void createFooter(XWPFDocument doc) {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        XWPFFooter footer;
        footer = doc.createFooter(HeaderFooterType.FIRST);
        paragraph = footer.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        footer = doc.createFooter(HeaderFooterType.DEFAULT);
        paragraph = footer.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run = paragraph.createRun();
        paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
        CTDocument1 document = doc.getDocument();
        CTBody body = document.getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageNumber pageNumber = section.getPgNumType();
        if (pageNumber == null) {
            pageNumber = section.addNewPgNumType();
        }
        pageNumber.setStart(BigInteger.valueOf(0L));
        XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
        if (headerFooterPolicy == null) {
            headerFooterPolicy = doc.createHeaderFooterPolicy();
        }
    }

    /**
     * @param doc
     * @param headerContent
     */
    private void createHeader(XWPFDocument doc, String headerContent) {
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT);
        XWPFParagraph paragraph = header.createParagraph();
        XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        run.setText(headerContent);
    }

    /**
     * @param document
     * @param content
     * @return
     */
    private XWPFParagraph getBoldRowContent(XWPFDocument document, String content) {
        XWPFParagraph pForRowOneC1 = document.createParagraph();
        XWPFRun run = pForRowOneC1.createRun();
        run.setBold(true);
        run.setText(content);
        return pForRowOneC1;
    }

    /**
     * @param document
     */
    private void pageSetup(XWPFDocument document) {
        CTDocument1 doc = document.getDocument();
        CTBody body = doc.getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pageSize = section.getPgSz();
        pageSize.setW(A4_WIDTH);
        pageSize.setH(A4_LENGTH);
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
    }

    /**
     * @param document
     * @param title
     */
    private void generateTitle(XWPFDocument document, String title) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setText(title + "\n" + "修改对照表");
        run.addBreak();
        run.addBreak();
        run.addBreak();
    }

    /**
     * @param document
     * @param leadingText
     */
    private void generateLeadingText(XWPFDocument document, String leadingText) {
        XWPFParagraph paragraphText = document.createParagraph();
        paragraphText.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runText = paragraphText.createRun();
        //　宋体　１１号
        runText.setFontSize(11);
        runText.setText(leadingText);
        runText.addBreak();
        runText.addBreak();
    }

//    public void compareParts(List patchDtoList, DocPart templatePart, DocPart samplePart) {
//        String templateText = templatePart.getPoint();
//        String sampleText = samplePart.getPoint();
//        /// Compare templateText and sampleText
//        /// In case they are different, patchDtoList.add
//
//
//        if (!templateText.equalsIgnoreCase(sampleText)) {
//            PatchDto p = new PatchDto();
//            p.setChapterIndex(0);
//            p.setChangeType("change");
//            p.setOrignalText(templateText);
//            RevisedDto r = new RevisedDto();
//            r.setRevisedText(sampleText);
//            p.setRevisedDto(r);
//            patchDtoList.add(p);
//        }
//
//        if (!templatePart.hasPart() && !samplePart.hasPart()) {
//            return;
//        }
//
//        List templateTitles = new LinkedList();
//        List sampleTitles = new LinkedList();
//
//        for (int i = 0; templatePart.hasPart() && i < templatePart.getChildPart().size(); ++i) {
//            String title = ((DocPart) templatePart.getChildPart().get(i)).getTitle();
//            templateTitles.add(title);
//        }
//        for (int i = 0; samplePart.hasPart() && i < samplePart.getChildPart().size(); ++i) {
//            String title = ((DocPart) samplePart.getChildPart().get(i)).getTitle();
//            sampleTitles.add(title);
//        }
//
//        PartMatch partMatch = StringSimUtils.findBestMatch(templateTitles, sampleTitles);
//        List addList = partMatch.getAddList();
//        List deleteList = partMatch.getDeleteList();
//        Map matchList = partMatch.getMatchList();
//
//        for (int i = 0; i < deleteList.size(); ++i) {
//            int chapterIndex = (int) deleteList.get(i);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(chapterIndex);
//            pdt.setChangeType("delete");
//            /// TODO: should be recursive
//            /// Delete means exists in template but not in sample
//            DocPart dp = templatePart.getChildPart().get(chapterIndex);
//            String pointText = dp.getPoint();
//            pdt.setOrignalText(pointText);
//            RevisedDto rdt = new RevisedDto();
//            for (int j = 0; j < pointText.length(); ++j) {
//                Character c = pointText.charAt(j);
//                rdt.deleteData(j, c);
//            }
//            pdt.setRevisedDto(rdt);
//            patchDtoList.add(pdt);
//        }
//
//        for (int i = 0; i < addList.size(); ++i) {
//            int chapterIndex = (int) addList.get(i);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(chapterIndex);
//            pdt.setChangeType("add");
//            /// TODO: should be recursive
//            /// Add means exists in sample but not in template
//            DocPart dp = samplePart.getChildPart().get(chapterIndex);
//            String pointText = dp.getPoint();
//            RevisedDto rdt = new RevisedDto();
//            rdt.setRevisedText(pointText);
//            for (int j = 0; j < pointText.length(); ++j) {
//                Character c = pointText.charAt(j);
//                rdt.addData(j, c);
//            }
//            pdt.setRevisedDto(rdt);
//            patchDtoList.add(pdt);
//        }
//
//        Iterator it = matchList.keySet().iterator();
//        while (it.hasNext()) {
//            int templateIndex = (int) it.next();
//            int sampleIndex = (int) matchList.get(templateIndex);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(sampleIndex);
//            pdt.setChangeType("change");
//            DocPart tPart = templatePart.getChildPart().get(templateIndex);
//            DocPart sPart = samplePart.getChildPart().get(sampleIndex);
//
//            /// Then compare children parts
//            compareParts(patchDtoList, tPart, sPart);
//        }
//    }

//    public List<PatchDto> getPatchDtoList(String templatePath, String samplePath) throws IOException {
//        List<PatchDto> patchDtoList = new LinkedList();
//
//        DocProcessor templateProcessor = new DocProcessor(templatePath);
//        templateProcessor.readText(templatePath);
//        FundDoc templateDoc = templateProcessor.process();
//
//        DocProcessor sampleProcessor = new DocProcessor(samplePath);
//        sampleProcessor.readText(samplePath);
//        FundDoc sampleDoc = sampleProcessor.process();
//
//        /// Compare first level part
//        List templateTitles = new LinkedList();
//        List sampleTitles = new LinkedList();
//
//        for (int i = 0; i < templateDoc.getParts().size(); ++i) {
//            String title = ((DocPart) templateDoc.getParts().get(i)).getTitle();
//            templateTitles.add(title);
//        }
//        for (int i = 0; i < sampleDoc.getParts().size(); ++i) {
//            String title = ((DocPart) sampleDoc.getParts().get(i)).getTitle();
//            sampleTitles.add(title);
//        }
//
//        PartMatch partMatch = StringSimUtils.findBestMatch(templateTitles, sampleTitles);
//        List addList = partMatch.getAddList();
//        List deleteList = partMatch.getDeleteList();
//        Map matchList = partMatch.getMatchList();
//
//        for (int i = 0; i < deleteList.size(); ++i) {
//            int chapterIndex = (int) deleteList.get(i);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(chapterIndex);
//            pdt.setChangeType("delete");
//            /// TODO: should be recursive
//            /// Delete means exists in template but not in sample
//            DocPart dp = templateDoc.getParts().get(chapterIndex);
//            String pointText = dp.getPoint();
//            pdt.setOrignalText(pointText);
//            RevisedDto rdt = new RevisedDto();
//            for (int j = 0; j < pointText.length(); ++j) {
//                Character c = pointText.charAt(j);
//                rdt.deleteData(j, c);
//            }
//            pdt.setRevisedDto(rdt);
//            patchDtoList.add(pdt);
//        }
//
//        for (int i = 0; i < addList.size(); ++i) {
//            int chapterIndex = (int) addList.get(i);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(chapterIndex);
//            pdt.setChangeType("add");
//            /// TODO: should be recursive
//            /// Add means exists in sample but not in template
//            DocPart dp = sampleDoc.getParts().get(chapterIndex);
//            String pointText = dp.getPoint();
//            RevisedDto rdt = new RevisedDto();
//            rdt.setRevisedText(pointText);
//            for (int j = 0; j < pointText.length(); ++j) {
//                Character c = pointText.charAt(j);
//                rdt.addData(j, c);
//            }
//            pdt.setRevisedDto(rdt);
//            patchDtoList.add(pdt);
//        }
//
//        Iterator it = matchList.keySet().iterator();
//        while (it.hasNext()) {
//            int templateIndex = (int) it.next();
//            int sampleIndex = (int) matchList.get(templateIndex);
//            PatchDto pdt = new PatchDto();
//            pdt.setChapterIndex(sampleIndex);
//            pdt.setChangeType("change");
//            DocPart templatePart = templateDoc.getParts().get(templateIndex);
//            DocPart samplePart = sampleDoc.getParts().get(sampleIndex);
//            /// Then compare children parts
//            compareParts(patchDtoList, templatePart, samplePart);
//        }
//        return patchDtoList;
//    }


//    CompareTest2 test = new CompareTest2();
//    List<List<CompareDto>> group1List = test.getListByGroup(orignalCompareDtoList);
//    List<List<CompareDto>> group2List = test.getListByGroup(revisedCompareDtoList);
//
//    List<String> s1 = new LinkedList<String>();
//    List<String> s2 = new LinkedList<String>();
//            for(
//    int i = 0; i<group1List.size();i++)
//int i = 0;
//            for(
//        List<MatchDto> matchList = StringSimUtils.findBestMatch(s1, s2, "0"); i<group2List.size();i++)
//List<PatchDto> patchDtoList = new ArrayList<PatchDto>();
//int i = 0;
//
//    {
//        +
//                s1.add(group1List.get(i).get(0).getText());
//    } StringSimUtils.findBestMatch(s1,s2,"0");
//
//            for(
//
//    {
//        s2.add(group2List.get(i).get(0).getText());
//    } i<matchList.size();i++)
//
//    {
//        List<String> str1List = new LinkedList<String>();
//        List<String> str2List = new LinkedList<String>();
//        List<Integer> chapterIndexList = new ArrayList<Integer>();
//        for (int j = 0; j < matchList.size(); j++) {
//            chapterIndexList.add(matchList.get(j).getRevisedIndex());
//        }
//        //当新条文中间新增chapter的情况
//        for (int j = 0; j < matchList.size(); j++) {
//            if (!chapterIndexList.contains(j) && chapterIndexList.get(chapterIndexList.size() - 1) > j) {
//                for (int k = 0; k < group2List.get(j).size(); k++) {
//                    CompareDto compareDto = group2List.get(j).get(k);
//                    RevisedDto revisedDto = new RevisedDto();
//                    revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
//                    PatchDto patchDto = new PatchDto();
//                    patchDto.setRevisedDto(revisedDto);
//                    patchDto.setChapterIndex(compareDto.getChapterIndex());
//                    patchDto.setSectionIndex(compareDto.getSectionIndex());
//                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                    +
//                            patchDto.setIndexType("revised");
//                    patchDto.setChangeType("add");
//                    patchDtoList.add(patchDto);
//                }
//            }
//        }
//
//        //当chapter能对应的情况
//        if (matchList.get(i).getRevisedIndex() != -1) {
//            for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
//                str1List.add(group1List.get(matchList.get(i).getOrignalIndex()).get(j).getText());
//            }
//            for (int j = 0; j < group2List.get(matchList.get(i).getRevisedIndex()).size(); j++) {
//                str2List.add(group2List.get(matchList.get(i).getRevisedIndex()).get(j).getText());
//            }
//            List<MatchDto> compareMatchList = StringSimUtils.findBestMatch(str1List, str2List, "");
//
//            List<Integer> revisedIndexList = new ArrayList<Integer>();
//            for (int j = 0; j < compareMatchList.size(); j++) {
//                revisedIndexList.add(compareMatchList.get(j).getRevisedIndex());
//            }
//
//            for (int j = 0; j < compareMatchList.size(); j++) {
//                //当新条文中间处新增section的情况
//                if (!revisedIndexList.contains(j) && revisedIndexList.get(revisedIndexList.size() - 1) > j) {
//                    CompareDto compareDto = group2List.get(matchList.get(i).getRevisedIndex()).get(compareMatchList.get(j).getOrignalIndex());
//                    RevisedDto revisedDto = new RevisedDto();
//                    revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
//                    PatchDto patchDto = new PatchDto();
//                    patchDto.setRevisedDto(revisedDto);
//                    patchDto.setChapterIndex(compareDto.getChapterIndex());
//                    patchDto.setSectionIndex(compareDto.getSectionIndex());
//                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                    patchDto.setIndexType("revised");
//                    patchDto.setChangeType("add");
//                    patchDtoList.add(patchDto);
//                }
//                //当section一一对应上的情况
//                if (compareMatchList.get(j).getRevisedIndex() != -1) {
//                    try {
//                        if (compareMatchList.get(j).getBestRatio() != 1) {
//                            PatchDto patchDto = CompareUtils2.doCompare(group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex()), group2List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getRevisedIndex()));
//                            patchDtoList.add(patchDto);
//                        }
//
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                } else {//当右边值为-1 新条文删减的情况
//                    CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex());
//                    RevisedDto revisedDto = new RevisedDto();
//                    PatchDto patchDto = new PatchDto();
//                    patchDto.setRevisedDto(revisedDto);
//                    patchDto.setChapterIndex(compareDto.getChapterIndex());
//                    patchDto.setOrignalText(compareDto.getIndex() + compareDto.getText());
//                    patchDto.setSectionIndex(compareDto.getSectionIndex());
//                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                    patchDto.setIndexType("orginal");
//                    patchDto.setChangeType("delete");
//                    patchDtoList.add(patchDto);
//                }
//            }
//            //当新条文section结尾新增的情况
//            if (group2List.get(i).size() > compareMatchList.size()) {
//                for (int j = compareMatchList.size(); j < group2List.get(i).size(); j++) {
//                    CompareDto compareDto = group2List.get(i).get(j);
//                    RevisedDto revisedDto = new RevisedDto();
//                    revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
//                    PatchDto patchDto = new PatchDto();
//                    patchDto.setRevisedDto(revisedDto);
//                    patchDto.setChapterIndex(compareDto.getChapterIndex());
//                    patchDto.setSectionIndex(compareDto.getSectionIndex());
//                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                    patchDto.setIndexType("revised");
//                    patchDto.setChangeType("add");
//                    patchDtoList.add(patchDto);
//                }
//            }
//        } else {//当chapter右边为-1 删减chapter的情况
//            for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
//                CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(j);
//                RevisedDto revisedDto = new RevisedDto();
//                PatchDto patchDto = new PatchDto();
//                patchDto.setRevisedDto(revisedDto);
//                patchDto.setChapterIndex(compareDto.getChapterIndex());
//                patchDto.setOrignalText(compareDto.getIndex() + compareDto.getText());
//                patchDto.setSectionIndex(compareDto.getSectionIndex());
//                patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                patchDto.setIndexType("orginal");
//                patchDto.setChangeType("delete");
//                patchDtoList.add(patchDto);
//            }
//
//        }
//        //当新条文chapter结尾处新增的情况
//        if (group2List.size() > matchList.size()) {
//            for (int j = matchList.size(); j < group2List.size(); j++) {
//                for (int k = 0; k < group2List.get(j).size(); k++) {
//                    CompareDto compareDto = group2List.get(j).get(k);
//                    RevisedDto revisedDto = new RevisedDto();
//                    revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
//                    PatchDto patchDto = new PatchDto();
//                    patchDto.setRevisedDto(revisedDto);
//                    patchDto.setChapterIndex(compareDto.getChapterIndex());
//                    patchDto.setSectionIndex(compareDto.getSectionIndex());
//                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//                    patchDto.setIndexType("revised");
//                    patchDto.setChangeType("add");
//                    patchDtoList.add(patchDto);
//                }
//            }
//        }
//    }
//     return null;
//}	     }
}
