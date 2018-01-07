/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.main;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.ChapterIncorrectException;
import cn.com.xiaofabo.tylaw.fundcontrast.exceptionhandler.SectionIncorrectException;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.BondTypeProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.IndexTypeProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.MonetaryTypeProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.StockTypeProcessor;

import java.io.IOException;

/**
 * @author 陈光曦
 */
public class FundContrast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ChapterIncorrectException, SectionIncorrectException {
        String inputPath1 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第1号——股票型（混合型）证券投资基金基金合同填报指引（试行）.doc";
        String inputPath2 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第2号——指数型证券投资基金基金合同填报指引（试行）.doc";
        String inputPath3 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第3号——债券型证券投资基金基金合同填报指引（试行）.doc";
        String inputPath4 = "data/StandardDoc/（2012-12-17）证券投资基金基金合同填报指引第4号——货币市场基金基金合同填报指引（试行）.doc";

        // 工银瑞信
        //String inputPath = "data/Sample/工银瑞信/20161223工银瑞信新动力混合型/工银瑞信新动力灵活配置混合型证券投资基金基金合同（草案）－定稿20161220.docx";
        //String inputPath = "data/Sample/工银瑞信/20161229工银瑞信共享经济混合型/工银瑞信共享经济灵活配置混合型证券投资基金基金合同20161229 - 定稿.docx";
        //String inputPath = "data/Sample/工银瑞信/20170927工银瑞信瑞祥定期开放发债券型发起式/工银瑞信瑞祥定期开放债券型发起式证券投资基金基金合同（草案）-0804.docx";
        //String inputPath = "data/Sample/工银瑞信/20171030工银瑞信瑞景定期开放债券型/工银瑞信瑞景定期开放债券型发起式证券投资基金基金合同（草案）PD1026-clean.docx";

        // 九泰基金
        //String inputPath = "data/Sample/九泰基金/20170413九泰天辰量化新动力混合型/九泰天辰量化新动力混合型证券投资基金合同-0224范-0228TY-0301发托管.doc";
        //String inputPath = "data/Sample/九泰基金/20170419九泰天泽混合型/九泰天泽灵活配置混合型证券投资基金基金合同（草案）-申请用印版0419-修改为灵配混合0505.doc";
        //String inputPath = "data/Sample/九泰基金/20170925九泰生活方式混合型FOF/九泰生活方式稳健配置混合型基金中基金（FOF）基金合同（草案）20170914流动性新规-20170922新调整.doc";
        //String inputPath = "data/Sample/九泰基金/20171010九泰汇利定期开放混合型发起式/九泰汇利定期开放灵活配置混合型发起式证券投资基金基金合同(草案)-流动性新规修订-1010清洁版.docx";
        //String inputPath = "data/Sample/九泰基金/20171103九泰安鑫纯债债券型/九泰安鑫纯债债券型证券投资基金合同（草案）20170725定稿清洁版.doc";

        // 华夏基金:债券 too many problems
        // String inputPath = "data/Sample/华夏基金/债券/华夏鼎丰三个月定期开放债券型发起式证券投资基金基金合同20171107-托管行反馈.docx";
        //String inputPath = "data/Sample/华夏基金/债券/华夏鼎丰三个月定期开放债券型发起式证券投资基金条文对照表 1107.docx";
        // String inputPath = "data/Sample/华夏基金/债券/华夏鼎兴债券型证券投资基金招募说明书修改对照表-20171012.docx";
        // String inputPath = "data/Sample/华夏基金/债券/华夏鼎康六个月定期开放债券型发起式证券投资基金条文对照表 1101.docx";
        // String inputPath = "data/Sample/华夏基金/债券/华夏鼎康六个月定期开放债券型发起式证券投资基金基金合同20171101-托管行反馈 P18.docx";
        //String inputPath = "data/Sample/华夏基金/债券/华夏鼎沛债券型证券投资基金基金合同.docx";
        // String inputPath = "data/Sample/九泰基金/20171103九泰安鑫纯债债券型/九泰安鑫纯债债券型证券投资基金合同（草案）20170725定稿清洁版.doc";
        //华夏基金:指数
        // String inputPath = "data/Sample/华夏基金/指数/华夏上证3-5年期中高评级可质押信用债ETF发起式联接基金基金合同20170112.doc";
        // String inputPath = "data/Sample/华夏基金/指数/华夏中l证全指ETF合同干净版-反馈修订-TY修订 改为指数型.docx";
        // String inputPath = "data/Sample/华夏基金/指数/华夏上证3-5年期中高评级可质押信用债ETF基金合同20170109-clean.doc";
        //华夏基金:股票混合
        //String inputPath = "data/Sample/华夏基金/股票混合/华夏新锦帆灵活配置混合型证券投资基金基金合同V2.docx";
        //String inputPath = "data/Sample/华夏基金/股票混合/华夏新锦远灵活配置混合型证券投资基金基金合同-干净版.docx";
        // String inputPath = "data/Sample/华夏基金/股票混合/华夏节能环保股票型证券投资基金基金合同20161114-clean.doc";
        //String inputPath = "data/Sample/华夏基金/股票混合/华夏行业龙头混合型证券投资基金基金合同-发.docx";
        //华夏基金:货币
        //error String inputPath = "data/Sample/华夏基金/货币/华夏行业龙头混合型证券投资基金基金合同-发.docx";
        //String inputPath = "data/Sample/华夏基金/货币/华夏快线交易型货币市场基金基金合同v6_20161025.doc";
        //String inputPath = "data/Sample/华夏基金/货币/华夏普金宝货币市场基金基金合同（草案）.docx";

        String idxInputPath = "（2012-12-17）证券投资基金基金合同填报指引第2号——指数型证券投资基金基金合同填报指引（试行）.doc";
        IndexTypeProcessor idx = new IndexTypeProcessor();
        idx.readText(inputPath2);
        FundDoc fd = idx.process();

        System.out.println("***************************************************");
        System.out.println("***************************************************");
        System.out.println("***************************************************");
        System.out.println(fd.toString());
        System.out.println("Finished!");

//        StockTypeProcessor proc = new StockTypeProcessor();
//        proc.readText(inputPath1);
//        proc.process();
//        System.out.println("Done");
//
//
//        BondTypeProcessor bond = new BondTypeProcessor();
//        bond.readText(inputPath3);
//        (bond).process();
//        System.out.println("Bond Done");
//
//        MonetaryTypeProcessor mon = new MonetaryTypeProcessor();
//        mon.readText(inputPath4);
//        mon.process();
//        System.out.println("mon Done");
    }
}