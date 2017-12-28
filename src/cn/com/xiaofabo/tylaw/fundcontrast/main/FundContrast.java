/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.StockTypeProcessor;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 陈光曦
 */
public class FundContrast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String inputPath = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）.doc";
        StockTypeProcessor proc = new StockTypeProcessor();
        proc.readText(inputPath);
        proc.process();
    }
}
