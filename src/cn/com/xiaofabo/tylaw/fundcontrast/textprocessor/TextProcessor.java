/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author 陈光曦
 */
public class TextProcessor {
    
    private String docText;
    private List<String> textList;
    private static final String LINE_BREAK = "\\r?\\n";
    
    public TextProcessor() {
    }
    
    public boolean readText(String filePath) throws IOException {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XWPFDocument docx = new XWPFDocument(fis);
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            docText = we.getText();
        } /// Old version word documents
        catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException e) {
            HWPFDocument doc = new HWPFDocument(new FileInputStream(filePath));
            WordExtractor we = new WordExtractor(doc);
            docText = we.getText();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public String getText() {
        return docText;
    }
    
    public List getLines() {
        String lines[] = docText.split(LINE_BREAK);
        textList = new LinkedList(Arrays.asList(lines));
        return textList;
    }
}
