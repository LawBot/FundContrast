package cn.com.xiaofabo.tylaw.fundcontrast.util;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.*;
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
    
    public static final String FONT_FAMILY_TIME_NEW_ROMAN = "Times New Roman";
    public static final String FONT_FAMILY_SONG = "宋体";
    
    public static final int EFFECT_ADD_BOLD = 0;
    public static final int EFFECT_ADD_UNDERLINE = 1;
    public static final int EFFECT_ADD_BOLD_UNDERLINE = 2;
    public static final int EFFECT_DELETE_STRIKE = 0;

    public static final String TABLE_HEADER_BGCOLOR = "808080";

    public static final BigInteger TABLE_COLUMN_1_WIDTH = BigInteger.valueOf(1133L);    /// ~2.0cm
    public static final BigInteger TABLE_COLUMN_2_WIDTH = BigInteger.valueOf(4820L);    /// ~8.5cm
    public static final BigInteger TABLE_COLUMN_3_WIDTH = BigInteger.valueOf(4253L);    /// ~7.5cm
    public static final BigInteger TABLE_COLUMN_4_WIDTH = BigInteger.valueOf(2551L);    /// ~4.5cm

    public static final String TABLE_COLUMN_1_TEXT = "章节";
    public static final String TABLE_COLUMN_2_TEXT = "《指引》条款";
    public static final String TABLE_COLUMN_3_TEXT = "《基金合同》条款";
    public static final String TABLE_COLUMN_4_TEXT = "修改理由";

    public static final int FONT_SIZE_LEADING_TEXT = 11;

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
        FileOutputStream out = new FileOutputStream(new File(outputPath));

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

        for (int i = 0; i < 4; ++i) {
            XWPFTableCell cell = tableRowOne.getCell(i);
            cell.getCTTc().addNewTcPr().addNewShd().setFill(TABLE_HEADER_BGCOLOR);
            BigInteger columnWidth = null;
            String columnText = null;
            switch (i) {
                case 0:
                    columnWidth = TABLE_COLUMN_1_WIDTH;
                    columnText = TABLE_COLUMN_1_TEXT;
                    break;
                case 1:
                    columnWidth = TABLE_COLUMN_2_WIDTH;
                    columnText = TABLE_COLUMN_2_TEXT;
                    break;
                case 2:
                    columnWidth = TABLE_COLUMN_3_WIDTH;
                    columnText = TABLE_COLUMN_3_TEXT;
                    break;
                case 3:
                    columnWidth = TABLE_COLUMN_4_WIDTH;
                    columnText = TABLE_COLUMN_4_TEXT;
                    break;
                default:
                    break;
            }
            cell.getCTTc().addNewTcPr().addNewTcW().setW(columnWidth);
            cell.removeParagraph(0);
            XWPFParagraph paragraph = cell.addParagraph();
            XWPFRun run = paragraph.createRun();
            addEffect(run, EFFECT_ADD_BOLD);
            run.setText(columnText);
        }

        for (int i = 0; i < contrastList.size(); ++i) {
            PatchDto contrastItem = contrastList.get(i);
            List<Integer> partIndex = contrastItem.getPartIndex();
            int chapterIndex = partIndex.get(0);
            String changeType = contrastItem.getChangeType();

            XWPFTableRow tableRow = table.getRow(i + 1);
            String chapterTitle = contrastItem.getChapterTitle();
            tableRow.getCell(0).removeParagraph(0);
            XWPFParagraph p = tableRow.getCell(0).addParagraph();
            XWPFRun r = p.createRun();
            addEffect(r, EFFECT_ADD_BOLD);
            r.setText(chapterTitle);

            if (changeType.equalsIgnoreCase("add")) {
                XWPFTableCell cell = tableRow.getCell(2);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);

                String addText = contrastItem.getRevisedDto().getRevisedText();
                XWPFRun run = paragraph.createRun();
                addEffect(run, EFFECT_ADD_BOLD_UNDERLINE);
                run.setText(addText);
            }

            if (changeType.equalsIgnoreCase("delete")) {
                XWPFTableCell cell = tableRow.getCell(1);
                cell.removeParagraph(0);
                XWPFParagraph paragraph = cell.addParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);

                String deleteText = contrastItem.getOrignalText();
                XWPFRun run = paragraph.createRun();
                deleteEffect(run, EFFECT_DELETE_STRIKE);
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
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    for (int j = 0; j < contrastItem.getOrignalText().length(); j++) {
                        XWPFRun letterRun = paragraph.createRun();
                        if(contrastItem.getOrignalText().charAt(j) == '\n'){
                            letterRun.addBreak();
                            continue;
                        }
                        
                        String currentLetter = Character.toString(contrastItem.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            deleteEffect(letterRun, EFFECT_DELETE_STRIKE);
                        }
                        letterRun.setText(currentLetter);

                    }
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    for (int j = 0; j < rdt.getRevisedText().length(); j++) {
                        XWPFRun letterRun = paragraph1.createRun();
                        if(rdt.getRevisedText().charAt(j) == '\n'){
                            letterRun.addBreak();
                            continue;
                        }
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(j));
                        if (set.contains(j)) {
                            deleteEffect(letterRun, EFFECT_DELETE_STRIKE);
                        }
                        letterRun.setText(currentLetter);
                    }
                }
                // change: add
                if (rdt.getRevisedText() != null && rdt.getAddData() != null && rdt.getDeleteData() == null) {
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun run = paragraph.createRun();
                    run.setText(contrastItem.getOrignalText());
                    
                    Set set1 = rdt.getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    paragraph1.setAlignment(ParagraphAlignment.LEFT);
                    for (int j = 0; j < rdt.getRevisedText().length(); j++) {
                        XWPFRun letterRun = paragraph1.createRun();
                        if(rdt.getRevisedText().charAt(j) == '\n'){
                            letterRun.addBreak();
                            continue;
                        }
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(j));
                        if (set1.contains(j)) {
                            addEffect(letterRun, EFFECT_ADD_BOLD_UNDERLINE);
                        }
                        letterRun.setText(currentLetter);
                    }
                }
                // change: add + delete
                if (rdt.getDeleteData() != null && rdt.getAddData() != null) {
                    Set set = rdt.getDeleteData().keySet();
                    XWPFTableCell cell = tableRow.getCell(1);
                    cell.removeParagraph(0);
                    XWPFParagraph paragraph = cell.addParagraph();
                    paragraph.setAlignment(ParagraphAlignment.LEFT);
                    for (int j = 0; j < contrastItem.getOrignalText().length(); j++) {
                        XWPFRun letterRun = paragraph.createRun();
                        if(contrastItem.getOrignalText().charAt(j) == '\n'){
                            letterRun.addBreak();
                            continue;
                        }
                        String currentLetter = Character.toString(contrastItem.getOrignalText().charAt(j));
                        if (set.contains(j)) {
                            deleteEffect(letterRun, EFFECT_DELETE_STRIKE);
                        }
                        letterRun.setText(currentLetter);
                    }
                    Set set1 = rdt.getAddData().keySet();
                    XWPFTableCell cell1 = tableRow.getCell(2);
                    cell1.removeParagraph(0);
                    XWPFParagraph paragraph1 = cell1.addParagraph();
                    paragraph1.setAlignment(ParagraphAlignment.LEFT);
                    for (int j = 0; j < rdt.getRevisedText().length(); j++) {
                        XWPFRun letterRun = paragraph1.createRun();
                        if(rdt.getRevisedText().charAt(j) == '\n'){
                            letterRun.addBreak();
                            continue;
                        }
                        String currentLetter = Character.toString(rdt.getRevisedText().charAt(j));
                        if (set1.contains(j)) {
                            addEffect(letterRun, EFFECT_ADD_BOLD_UNDERLINE);
                        }
                        letterRun.setText(currentLetter);

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
        
        XWPFStyles styles = document.createStyles();
        CTFonts fonts = CTFonts.Factory.newInstance();
        fonts.setEastAsia(FONT_FAMILY_SONG);
        fonts.setAscii(FONT_FAMILY_TIME_NEW_ROMAN);
        styles.setDefaultFonts(fonts);
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
        runText.setFontSize(FONT_SIZE_LEADING_TEXT);
        runText.setText(leadingText);
        runText.addBreak();
        runText.addBreak();
    }
    
    /// style:
    /// 0: strickthrough
    private void deleteEffect(XWPFRun run, int style){
        switch(style){
            case EFFECT_DELETE_STRIKE:
                run.setStrikeThrough(true);
                break;
            default:
                break;
        }
    }
    
    /// style:
    /// 0: bold
    /// 1: underline
    /// 2: bold + underline
    private void addEffect(XWPFRun run, int style){
        switch(style){
            case EFFECT_ADD_BOLD:
                run.setBold(true);
                break;
            case EFFECT_ADD_UNDERLINE:
                run.setUnderline(UnderlinePatterns.SINGLE);
                break;
            case EFFECT_ADD_BOLD_UNDERLINE:
                run.setUnderline(UnderlinePatterns.SINGLE);
                run.setBold(true);
                break;
            default:
                break;
        }
    }
}
