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
 * Modified on 2018-02-14 by G. Chen
 */
public class GenerateCompareDoc {

    public static final BigInteger A4_WIDTH = BigInteger.valueOf(16840L);
    public static final BigInteger A4_LENGTH = BigInteger.valueOf(11900L);
    public static final BigInteger TABLE_WIDTH = BigInteger.valueOf(13040L);

    public static final String TABLE_HEADER_BGCOLOR = "808080";

    public static final BigInteger TABLE_COLUMN_1_WIDTH = BigInteger.valueOf(1133L);    /// ~2.0cm
    public static final BigInteger TABLE_COLUMN_2_WIDTH = BigInteger.valueOf(4820L);    /// ~8.5cm
    public static final BigInteger TABLE_COLUMN_3_WIDTH = BigInteger.valueOf(4820L);    /// ~7.5cm
    public static final BigInteger TABLE_COLUMN_4_WIDTH = BigInteger.valueOf(1985L);    /// ~4.5cm

    public static final String TABLE_COLUMN_1_TEXT = "章节";
    public static final String TABLE_COLUMN_2_TEXT = "《指引》条款";
    public static final String TABLE_COLUMN_3_TEXT = "《基金合同》条款";
    public static final String TABLE_COLUMN_4_TEXT = "修改理由";

    /**
     * Defined Constants.
     */
    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());
    String headerContent = "条文对照表测试文本";

    public int generate(String title, String leadingText, List<PatchDto> contrastList, String outputPath) throws IOException {
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

        for (int i = 0; i < contrastList.size(); ++i) {
            PatchDto contrastItem = contrastList.get(i);
            List<Integer> partIndex = contrastItem.getPartIndex();
            int chapterIndex = partIndex.get(0);
            String changeType = contrastItem.getChangeType();

            XWPFTableRow tableRow = table.getRow(i + 1);
            String column0Str = "第" + chapterIndex + "章";
            tableRow.getCell(0).setText(column0Str);

            if (changeType.equalsIgnoreCase("add")) {
                XWPFTableCell cell = tableRow.getCell(2);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();

                String addText = contrastItem.getRevisedDto().getRevisedText();
                XWPFRun run = paragraph.createRun();
                run.setBold(true);
                run.setText(addText);
            }

            if (changeType.equalsIgnoreCase("delete")) {
                XWPFTableCell cell = tableRow.getCell(1);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();

                String deleteText = contrastItem.getOrignalText();
                XWPFRun run = paragraph.createRun();
                run.setStrikeThrough(true);
                run.setText(deleteText);
            }

            if (changeType.equalsIgnoreCase("change")) {
                RevisedDto rdt = contrastItem.getRevisedDto();
                if (rdt == null) {
                    continue;
                }

                if (rdt.getDeleteData() != null && rdt.getAddData() == null) {
                    Set set = rdt.getDeleteData().keySet();
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int j = 0; j < contrastItem.getOrignalText().length(); j++) {
                        XWPFRun letterRun = paragraph.createRun();
                        String currentLetter = Character.toString(contrastItem.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            letterRun.setStrike(true);
                            letterRun.setText(currentLetter);
                        } else {
                            letterRun.setText(currentLetter);
                        }
                    }
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int j = 0; j < rdt.getRevisedText().length(); j++) {
                        XWPFRun letterRun = paragraph1.createRun();
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(j));
                        if (set.contains(j)) {
                            letterRun.setStrike(true);
                            letterRun.setText(currentLetter);
                        } else {
                            letterRun.setText(currentLetter);
                        }
                    }
                }
                // change: add
                if (rdt.getRevisedText() != null && rdt.getAddData() != null && rdt.getDeleteData() == null) {
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int k = 0; k < contrastItem.getOrignalText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
                        String currentLetter = Character.toString(contrastItem.getOrignalText().charAt(k));
                        runForEachLetter.setText(currentLetter);
                    }
                    Set set1 = rdt.getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int k = 0; k < rdt.getRevisedText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph1.createRun();
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(k));
                        if (set1.contains(k)) {
                            runForEachLetter.setBold(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                }
                // change: add + delete
                if (rdt.getDeleteData() != null && rdt.getAddData() != null) {
                    Set set = rdt.getDeleteData().keySet();
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    for (int j = 0; j < contrastItem.getOrignalText().length(); j++) {
                        XWPFRun runForEachLetter = paragraph.createRun();
                        String currentLetter = Character.toString(contrastItem.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            runForEachLetter.setStrike(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
                    Set set1 = rdt.getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    for (int k = 0; k < rdt.getRevisedText().length(); k++) {
                        XWPFRun runForEachLetter = paragraph1.createRun();
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(k));
                        if (set1.contains(k)) {
                            runForEachLetter.setBold(true);
                            runForEachLetter.setText(currentLetter);
                        } else {
                            runForEachLetter.setText(currentLetter);
                        }
                    }
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
}
