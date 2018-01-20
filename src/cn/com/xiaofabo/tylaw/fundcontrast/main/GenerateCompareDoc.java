package cn.com.xiaofabo.tylaw.fundcontrast.main;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class GenerateCompareDoc {

    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());

    public static void main(String[] args) throws Exception {
        GenerateCompareDoc test = new GenerateCompareDoc();
        test.generate();
    }

    private void generate() throws IOException {
        PropertyConfigurator.configure("log.properties");
        log.info("Create an empty document");
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File("条文对照表.docx"));
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
        pageSize.setW(BigInteger.valueOf(15840));
        pageSize.setH(BigInteger.valueOf(12240));
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(12);
        run.setBold(true);
        run.setText("《九泰安鑫纯债债券型证券投资基金基金合同（草案）》\n" +
                "修改对照表");
        run.addBreak();
        run.addBreak();
        run.addBreak();
        XWPFParagraph paragraphText = document.createParagraph();
        XWPFRun runText = paragraphText.createRun();
        //　宋体　１１号
        runText.setFontSize(11);
        runText.setText("”募集申证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券");
        runText.addBreak();
        runText.addBreak();
        XWPFTable table = document.createTable(25, 4);
        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
        width.setType(STTblWidth.DXA);
        width.setW(BigInteger.valueOf(11000));
        XWPFTableRow tableRowOne = table.getRow(0);
        //tableRowOne.isRepeatHeader();
        tableRowOne.getTableCells().get(0).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(1).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(2).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(3).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getCell(0).setParagraph(getBoldRowContent(document, "章节"));
        tableRowOne.getCell(1).setParagraph(getBoldRowContent(document, "指引条款"));

        tableRowOne.getCell(2).setParagraph(getBoldRowContent(document, "基金合同条款"));
        tableRowOne.getCell(3).setParagraph(getBoldRowContent(document, "修改理由"));

        XWPFTableRow rowTwo = table.getRow(1);
        rowTwo.getCell(0).setText("章节章节");
        rowTwo.getCell(1).setText("指引条款");
        rowTwo.getCell(2).setText("基金合同条款");
        rowTwo.getCell(3).setText("修改理由");

        XWPFTableRow rowThree = table.getRow(2);
        rowThree.getCell(0).setText("章节章节章节");
        rowThree.getCell(1).setText("指引条款");
        rowThree.getCell(2).setText("基金合同条款");
        rowThree.getCell(3).setText("修改理由");

        XWPFTableRow rowThe = table.getRow(3);
        rowThe.getCell(0).setText("章节章节章节章节");
        rowThe.getCell(1).setText("指引条款");
        rowThe.getCell(2).setText("基金合同条款");
        rowThe.getCell(3).setText("修改理由");

        XWPFTableRow rowFour = table.getRow(4);
        rowFour.getCell(0).setText("章节章节章节章节章节");
        rowFour.getCell(1).setText("指引条款");
        rowFour.getCell(2).setText("基金合同条款");
        rowFour.getCell(3).setText("修改理由");
        // add footer
        createFooter(document);
        document.write(out);
        out.close();
    }

    // TODO
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


    private XWPFParagraph getBoldRowContent(XWPFDocument document, String content) {
        XWPFParagraph pForRowOneC1 = document.createParagraph();
        XWPFRun run = pForRowOneC1.createRun();
        run.setBold(true);
        run.setText(content);
        return pForRowOneC1;
    }
}