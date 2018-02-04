package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils2;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏 email ddl-15 at outlook.com
 *
 */
public class GenerateCompareDoc {

    /**
     * Defined Constants.
     */
    public static final BigInteger A4_WIDTH = BigInteger.valueOf(16840L);
    public static final BigInteger A4_LENGTH = BigInteger.valueOf(11900L);
    public static final BigInteger TABLE_WIDTH = BigInteger.valueOf(13040L);
    
    private static final BigInteger TABLE_COLUMN_1_WIDTH = BigInteger.valueOf(1133L);    /// ~2.0cm
    private static final BigInteger TABLE_COLUMN_2_WIDTH = BigInteger.valueOf(4820L);    /// ~8.5cm
    private static final BigInteger TABLE_COLUMN_3_WIDTH = BigInteger.valueOf(4253L);    /// ~7.5cm
    private static final BigInteger TABLE_COLUMN_4_WIDTH = BigInteger.valueOf(2551L);    /// ~4.5cm
    
    public static final String Color_grey = "808080";

    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());

    public static void main(String[] args) throws Exception {
        String standardPath = DataUtils.STANDARD_TYPE_STOCK;
        String samplePath = DataUtils.SAMPLE_GYRX_STOCK_1;
        DocProcessor dp = new DocProcessor(standardPath);
        dp.readText(standardPath);
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

        DocProcessor dp2 = new DocProcessor(samplePath);
        dp2.readText(samplePath);
        FundDoc fd2 = dp2.process();
        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();
        List<PatchDto> patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList);

        GenerateCompareDoc test = new GenerateCompareDoc();
        String title = "《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》\n";
        String txt = "九泰天辰量化新动力混合型证券投资基金募集申请材料之《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引(试行)》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
        test.generate(title, txt, patchDtoList, "data");
    }

    public void generate(String title, String leadingText, List<PatchDto> resultDto, String outputPath) throws IOException {
        PropertyConfigurator.configure("log.properties");
        log.info("Create an empty document");
        int row = resultDto.size() + 1;
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File(outputPath + "/条文对照表.docx"));
//        FileOutputStream out = new FileOutputStream(new File(outputPath));

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
            String column1;
            String column2;

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

    private void generateTitle(XWPFDocument document, String title) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setText(title + "\n修改对照表");
        run.addBreak();
        run.addBreak();
        run.addBreak();
    }

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
