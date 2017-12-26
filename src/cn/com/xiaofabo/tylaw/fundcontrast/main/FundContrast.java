/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.main;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


/**
 *
 * @author 陈光曦
 */
public class FundContrast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String inputPath = "D:\\OneDrive\\小法博科技\\合作\\天元律所\\天元需求\\"
                + "1、基金条文对照表（华夏、九泰、工银）\\工银瑞信\\20161223工银瑞信新动力混合型"
                + "\\工银瑞信新动力灵活配置混合型证券投资基金基金合同（草案）－定稿20161220.docx";
        
        String docText = "";
        try {
            FileInputStream fis = new FileInputStream(inputPath);
            XWPFDocument docx = new XWPFDocument(fis);
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            docText = we.getText();
        } /// Old version word documents
        catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
            HWPFDocument doc = new HWPFDocument(new FileInputStream(inputPath));
            WordExtractor we = new WordExtractor(doc);
            docText = we.getText();
        }
        
        System.out.println(docText);
    }

}
