package cn.com.xiaofabo.tylaw.fundcontrast.main;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

/**TODO
 * Created on @ 17.01.18
 *
 * @author 杨敏
 * email ddl-15 at outlook.com
 **/
public class GenerateCompareDoc {

    public static void main(String[] args) throws Exception {

        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));


        //create title Paragraph
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("《九泰安鑫纯债债券型证券投资基金基金合同（草案）》\n" +
                "修改对照表");
        // document.write(out);

        //  create text　Paragraph
        XWPFParagraph paragraphText = document.createParagraph();
        XWPFRun runText = paragraph.createRun();
        runText.setText("九泰安鑫纯债债券型证券投资基金募集申请材料之《九泰安鑫纯债债券型证券投资基金基金合同（草案）》（以下简称“《基金合同》”）系按照中国证监会基金监管部发布的《证券投资基金基金合同填报指引第3号——债券型证券投资基金基金合同填报指引(试行)》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。");
        // document.write(out);


        // create table
        // ensure title on each page for table TODO
        XWPFTable table = document.createTable();

        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setText("col one, row one");
        tableRowOne.addNewTableCell().setText("col two, row one");
        tableRowOne.addNewTableCell().setText("col three, row one");

        //create second row
        XWPFTableRow tableRowTwo = table.createRow();
        tableRowTwo.getCell(0).setText("col one, row two");
        tableRowTwo.getCell(1).setText("col two, row two");
        tableRowTwo.getCell(2).setText("col three, row two");

        //create third row
        XWPFTableRow tableRowThree = table.createRow();
        tableRowThree.getCell(0).setText("col one, row three");
        tableRowThree.getCell(1).setText("col two, row three");
        tableRowThree.getCell(2).setText("col three, row three");

        document.write(out);
        out.close();
        System.out.println("create_table.docx written successully");
    }
}
