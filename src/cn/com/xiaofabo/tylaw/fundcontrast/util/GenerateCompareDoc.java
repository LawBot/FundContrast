package cn.com.xiaofabo.tylaw.fundcontrast.util;


import cn.com.xiaofabo.tylaw.fundcontrast.entity.*;
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

import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.TABLE_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.A4_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.A4_LENGTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.TABLE_COLUMN_1_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.TABLE_COLUMN_2_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.TABLE_COLUMN_3_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.TABLE_COLUMN_4_WIDTH;
import static cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils.Color_grey;

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏 email ddl-15 at outlook.com
 */
public class GenerateCompareDoc {

    /**
     * Defined Constants.
     */
    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());

    private static List<List<CompareDto>> getListByGroup(List<CompareDto> list) {
        List<List<CompareDto>> result = new ArrayList<List<CompareDto>>();
        Map<Integer, List<CompareDto>> map = new TreeMap<Integer, List<CompareDto>>();

        for (CompareDto bean : list) {
            if (map.containsKey(bean.getChapterIndex())) {
                List<CompareDto> t = map.get(bean.getChapterIndex());
                t.add(bean);
                new ArrayList<CompareDto>().add(bean);
                map.put(bean.getChapterIndex(), t);
            } else {
                List<CompareDto> t = new ArrayList<CompareDto>();
                t.add(bean);
                map.put(bean.getChapterIndex(), t);
            }
        }
        for (Map.Entry<Integer, List<CompareDto>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public void generate(String title, String leadingText, List<PatchDto> resultDto, String outputPath) throws IOException {
        PropertyConfigurator.configure("log.properties");
        log.info("Create an empty document");
        int row = resultDto.size() + 1;
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File(outputPath + "/条文对照表.docx"));

        /// Document page setup
        pageSetup(document);
        /// Generate title text
        generateTitle(document, title);
        /// Generate leading text
        generateLeadingText(document, leadingText);

        /// Generate contrast table
        XWPFTable table = document.createTable(row, 4);
        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
        width.setType(STTblWidth.DXA);
        width.setW(TABLE_WIDTH);
        CTTblLayoutType type = table.getCTTbl().getTblPr().addNewTblLayout();
        type.setType(STTblLayoutType.FIXED);

        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.setRepeatHeader(true);
        tableRowOne.getTableCells().get(0).getCTTc().addNewTcPr().addNewShd().setFill(Color_grey);
        tableRowOne.getTableCells().get(1).getCTTc().addNewTcPr().addNewShd().setFill(Color_grey);
        tableRowOne.getTableCells().get(2).getCTTc().addNewTcPr().addNewShd().setFill(Color_grey);
        tableRowOne.getTableCells().get(3).getCTTc().addNewTcPr().addNewShd().setFill(Color_grey);
        tableRowOne.getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_1_WIDTH);
        tableRowOne.getCell(0).setText("章节\n");
        tableRowOne.getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_2_WIDTH);
        tableRowOne.getCell(1).setText("《指引》条款\n");
        tableRowOne.getCell(2).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_3_WIDTH);
        tableRowOne.getCell(2).setText("《基金合同》条款\n");
        tableRowOne.getCell(3).getCTTc().addNewTcPr().addNewTcW().setW(TABLE_COLUMN_4_WIDTH);
        tableRowOne.getCell(3).setText("修改理由\n");

        for (int i = 0; i < resultDto.size(); i++) {
            PatchDto p = resultDto.get(i);
            XWPFTableRow tableRow = table.getRow(1 + i);
            //章节
            String column0 = "第" + p.getChapterIndex() + "章";
            tableRow.getCell(0).setText(column0);

            if (p.getChangeType() == "change") {
                System.out.println("INSIDE Change");

                // set delete
                if (p.getRevisedDto() != null && p.getRevisedDto().getDeleteData() != null) {
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
                }

                // set add
                if (p.getRevisedDto() != null && p.getRevisedDto().getRevisedText() != null && p.getRevisedDto().getAddData() != null) {
                    Set set1 = p.getRevisedDto().getAddData().keySet();
                    XWPFTableCell cell = tableRow.getCell(2);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int k = 0; k < p.getRevisedDto().getRevisedText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
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
                System.out.println("INSIDE ADD");
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
                System.out.println("INSIDE delete");

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

        // add header, footer
        String headerContent = "条文对照表测试文本";
        createHeader(document, headerContent);
        createFooter(document);
        document.write(out);
        out.close();
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

    public List<PatchDto> getPatchDtoList(String testPath, String testPath2) throws IOException {
        DocProcessor dp = new DocProcessor(testPath);
        dp.readText(testPath);
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

        DocProcessor dp2 = new DocProcessor(testPath2);
        dp2.readText(testPath2);
        FundDoc fd2 = dp2.process();
        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();

        List<List<CompareDto>> group1List = getListByGroup(orignalCompareDtoList);
        List<List<CompareDto>> group2List = getListByGroup(revisedCompareDtoList);

        List<String> s1 = new LinkedList<String>();
        List<String> s2 = new LinkedList<String>();
        for (int i = 0; i < group1List.size(); i++) {
            s1.add(group1List.get(i).get(0).getText());
        }
        for (int i = 0; i < group2List.size(); i++) {
            s2.add(group2List.get(i).get(0).getText());
        }
        List<MatchDto> matchList = StringSimUtils.findBestMatch(s1, s2, "0");
        List<PatchDto> patchDtoList = new ArrayList<PatchDto>();

        for (int i = 0; i < matchList.size(); i++) {
            List<String> str1List = new LinkedList<String>();
            List<String> str2List = new LinkedList<String>();
            List<Integer> chapterIndexList = new ArrayList<Integer>();
            for (int j = 0; j < matchList.size(); j++) {
                chapterIndexList.add(matchList.get(j).getRevisedIndex());
            }
            //当新条文中间新增chapter的情况
            for (int j = 0; j < matchList.size(); j++) {
                if (!chapterIndexList.contains(j) && chapterIndexList.get(chapterIndexList.size() - 1) > j) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }

            //当chapter能对应的情况
            if (matchList.get(i).getRevisedIndex() != -1) {
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    str1List.add(group1List.get(matchList.get(i).getOrignalIndex()).get(j).getText());
                }
                for (int j = 0; j < group2List.get(matchList.get(i).getRevisedIndex()).size(); j++) {
                    str2List.add(group2List.get(matchList.get(i).getRevisedIndex()).get(j).getText());
                }
                List<MatchDto> compareMatchList = StringSimUtils.findBestMatch(str1List, str2List, "");

                List<Integer> revisedIndexList = new ArrayList<Integer>();
                for (int j = 0; j < compareMatchList.size(); j++) {
                    revisedIndexList.add(compareMatchList.get(j).getRevisedIndex());
                }

                for (int j = 0; j < compareMatchList.size(); j++) {
                    //当新条文中间处新增section的情况
                    if (!revisedIndexList.contains(j) && revisedIndexList.get(revisedIndexList.size() - 1) > j) {
                        CompareDto compareDto = group2List.get(matchList.get(i).getRevisedIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                    //当section一一对应上的情况
                    if (compareMatchList.get(j).getRevisedIndex() != -1) {
                        try {
                            if (compareMatchList.get(j).getBestRatio() != 1) {
                                PatchDto patchDto = CompareUtils2.doCompare(group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex()), group2List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getRevisedIndex()));
                                patchDtoList.add(patchDto);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {//当右边值为-1 新条文删减的情况
                        CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setOrignalText(compareDto.getIndex() + compareDto.getText());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("orginal");
                        patchDto.setChangeType("delete");
                        patchDtoList.add(patchDto);
                    }
                }
                //当新条文section结尾新增的情况
                if (group2List.get(i).size() > compareMatchList.size()) {
                    for (int j = compareMatchList.size(); j < group2List.get(i).size(); j++) {
                        CompareDto compareDto = group2List.get(i).get(j);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            } else {//当chapter右边为-1 删减chapter的情况
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(j);
                    RevisedDto revisedDto = new RevisedDto();
                    PatchDto patchDto = new PatchDto();
                    patchDto.setRevisedDto(revisedDto);
                    patchDto.setChapterIndex(compareDto.getChapterIndex());
                    patchDto.setOrignalText(compareDto.getIndex() + compareDto.getText());
                    patchDto.setSectionIndex(compareDto.getSectionIndex());
                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                    patchDto.setIndexType("orginal");
                    patchDto.setChangeType("delete");
                    patchDtoList.add(patchDto);
                }

            }
            //当新条文chapter结尾处新增的情况
            if (group2List.size() > matchList.size()) {
                for (int j = matchList.size(); j < group2List.size(); j++) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getIndex() + compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }
        }
        return patchDtoList;
    }

}