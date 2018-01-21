package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
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
import java.util.Set;

/**
 * Created on @ 17.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class GenerateCompareDoc {

    private static Logger log = Logger.getLogger(GenerateCompareDoc.class.getName());

    public static void main(String[] args) throws Exception {
        String testPath = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第4号——货币市场基金基金合同填报指引（试行）.doc";
        String testPath2 = "data/Sample/华夏基金/货币/华夏兴金宝货币市场基金基金合同（草案） 1026.docx";
        DocProcessor dp = new DocProcessor(testPath);
        dp.readText(testPath);
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

        DocProcessor dp2 = new DocProcessor(testPath2);
        dp2.readText(testPath2);
        FundDoc fd2 = dp2.process();
        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();
        List<PatchDto> patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList);

        GenerateCompareDoc test = new GenerateCompareDoc();
        String title = "《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》\n";
        String txt = "九泰天辰量化新动力混合型证券投资基金募集申请材料之《九泰天辰量化新动力混合型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引(试行)》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
        test.generate(title, txt, patchDtoList);
    }

    private void generate(String title, String text, List<PatchDto> resultDto) throws IOException {
        PropertyConfigurator.configure("log.properties");
        log.info("Create an empty document");
        String tit = title;
        String txt = text;
        int row = resultDto.size() + 1;
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
        run.setText(tit + "修改对照表");
        run.addBreak();
        run.addBreak();
        run.addBreak();
        XWPFParagraph paragraphText = document.createParagraph();
        XWPFRun runText = paragraphText.createRun();
        //　宋体　１１号
        runText.setFontSize(11);
        runText.setText(txt);
        runText.addBreak();
        runText.addBreak();

        XWPFTable table = document.createTable(row, 4);
        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
        width.setType(STTblWidth.DXA);
        width.setW(BigInteger.valueOf(11000));
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.setRepeatHeader(true);
        tableRowOne.getTableCells().get(0).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(1).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(2).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getTableCells().get(3).getCTTc().addNewTcPr().addNewShd().setFill("808080");
        tableRowOne.getCell(0).setText("章节\n");
        tableRowOne.getCell(1).setText("《指引》条款\n");
        tableRowOne.getCell(2).setText("《基金合同》条款\n");
        tableRowOne.getCell(3).setText("修改理由\n");

        for (int i = 0; i < resultDto.size(); i++) {
            PatchDto p = resultDto.get(i);
            XWPFTableRow s = table.getRow(1 + i);
            //章节
            String column0 = p.getChapterIndex() + "";
            s.getCell(0).setText(column0);
            String column1;
            String column2;

            XWPFParagraph par = document.createParagraph();
            if (p.getRevisedDto() != null && p.getRevisedDto().getDeleteData() != null) {
                Set set = p.getRevisedDto().getDeleteData().keySet();
                for (int j = 0; j < p.getOrignalText().length(); j++) {
                    XWPFRun runForEachLetter = par.createRun();
                    String currentLetter = Character.toString(p.getOrignalText().charAt(j));
                    if (set.contains(j)) {
                        runForEachLetter.setStrike(true);
                        runForEachLetter.setText(currentLetter);
                    } else {
                        runForEachLetter.setText(currentLetter);
                    }
                }
                s.getCell(1).setParagraph(par);
            }

            XWPFParagraph par1 = document.createParagraph();
            if (p.getRevisedDto() != null && p.getRevisedDto().getRevisedText() != null && p.getRevisedDto().getAddData() != null) {
                Set set1 = p.getRevisedDto().getAddData().keySet();
                for (int k = 0; k < p.getRevisedDto().getRevisedText().length(); k++) {
                    XWPFRun runForEachLetter = par1.createRun();
                    String currentLetter = Character.toString(p.getRevisedDto().getRevisedText().charAt(k));
                    if (set1.contains(k)) {
                        runForEachLetter.setBold(true);
                        runForEachLetter.setText(currentLetter);
                    } else {
                        runForEachLetter.setText(currentLetter);
                    }
                }
                s.getCell(2).setParagraph(par1);
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
}