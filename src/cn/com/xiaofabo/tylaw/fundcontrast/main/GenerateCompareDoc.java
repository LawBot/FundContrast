package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;
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

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class GenerateCompareDoc {

    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());

    public static void main(String[] args) throws Exception {
        String testPath = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）.doc";
        String testPath2 = "data/Sample/华夏基金/债券/华夏鼎康六个月定期开放债券型发起式证券投资基金基金合同20171101-托管行反馈 P18.docx";
        String path3 = "data/Sample/九泰基金/20170419九泰天泽混合型/九泰天泽灵活配置混合型证券投资基金基金合同（草案）-申请用印版0419-修改为灵配混合0505.doc";

        DocProcessor dp = new DocProcessor(testPath);
        dp.readText(testPath);
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();
        List<String> originalList = new ArrayList<String>();
        List<String> revisedList = new ArrayList<String>();
        DocProcessor dp2 = new DocProcessor(testPath2);
        FundDoc fd2 = dp2.process();

        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();
        for (CompareDto compareDto : revisedCompareDtoList) {
            revisedList.add(compareDto.getText());
        }
        List<PatchDto> patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList, originalList, revisedList);
        GenerateCompareDoc test = new GenerateCompareDoc();
        test.generate(patchDtoList);
    }

    private void generate(List<PatchDto> resultDto) throws IOException {
        PropertyConfigurator.configure("log.properties");
        for (PatchDto p : resultDto) {
            log.info("MINMIN" + p.getIndexType());
            log.info("MINMIN" + p.getOrignalText());
            log.info("MINMIN" + p.getRevisedDto());
            log.info("MINMIN" + p.getChapterIndex());
            log.info("MINMIN" + p.getSectionIndex());

        }
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
        tableRowOne.setRepeatHeader(true);
        tableRowOne.getTableCells().get(0).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(1).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(2).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(3).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getCell(0).setText("章节章节");
        tableRowOne.getCell(1).setText("指引条款");
        tableRowOne.getCell(2).setText("基金合同条款");
        tableRowOne.getCell(3).setText("修改理由");
//        tableRowOne.getCell(0).setParagraph(getBoldRowContent(document, "章节"));
//        tableRowOne.getCell(1).setParagraph(getBoldRowContent(document, "指引条款"));
//        tableRowOne.getCell(2).setParagraph(getBoldRowContent(document, "基金合同条款"));
//        tableRowOne.getCell(3).setParagraph(getBoldRowContent(document, "修改理由"));

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
        String headerContent = "条文对照表测试文本";
        createHeader(document, headerContent);
        // add footer
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
//    private XWPFParagraph getBoldRowContent(XWPFDocument document, String content) {
//        XWPFParagraph pForRowOneC1 = document.createParagraph();
//        XWPFRun run = pForRowOneC1.createRun();
//        run.setBold(true);
//        run.setText(content);
//        return pForRowOneC1;
//    }
}