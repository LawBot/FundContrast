/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocGenerator;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;

/**
 *
 * @author 陈光曦
 */
public class GeneratorTest {
    public static void main(String argv[]){
        String outputPath = "data/output.docx";
        int result = DocGenerator.generate(DataUtils.SAMPLE_GYRX_STOCK_1, outputPath);
        System.out.println(result);
    }
}
