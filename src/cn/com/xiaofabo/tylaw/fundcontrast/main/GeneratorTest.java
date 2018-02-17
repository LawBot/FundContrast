/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocGenerator;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 陈光曦
 */
public class GeneratorTest {

    public static void main(String argv[]) {
        List<String> outputPath = new LinkedList();
        for (int i = 0; i < 50; ++i) {
            outputPath.add("data/output/output_" + (i + 1) + ".docx");
        }
        int index = 0;
        String docPath;
        int errorCode = 0;
        
        docPath= DataUtils.SAMPLE_GYRX_STOCK_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_GYRX_STOCK_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
//        docPath= DataUtils.SAMPLE_GYRX_BOND_1;
//        result = DocGenerator.generate(docPath, outputPath.get(index++));
//        System.out.println("Processing: " + docPath);
//        System.out.println("Result: " + result);
        
        docPath= DataUtils.SAMPLE_GYRX_BOND_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_STOCK_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_STOCK_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_STOCK_3;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_STOCK_4;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_INDEX_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_INDEX_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_BOND_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_BOND_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_BOND_3;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_BOND_4;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_MONETARY_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_MONETARY_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_HXJJ_MONETARY_3;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_JTJJ_STOCK_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_JTJJ_STOCK_2;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_JTJJ_STOCK_3;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_JTJJ_STOCK_4;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
        
        docPath= DataUtils.SAMPLE_JTJJ_BOND_1;
        System.out.println("Processing: " + docPath);
        errorCode = DocGenerator.generate(docPath, outputPath.get(index++));
        System.out.println("Result: " + errorCode);
    }
}
