/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.GenerateCompareDoc;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author 陈光曦
 */
public class DocGenerator {

    public static String LEADING_TEXT_STOCK = "（以下简称“《基金合同》”）系按照中国证监"
            + "会基金监管部发布的《证券投资基金基金合同填报指引第1号——股票型（混合型）"
            + "证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。"
            + "根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对"
            + "《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    public static String LEADING_TEXT_INDEX = "（以下简称“《基金合同》”）系按照中国证监"
            + "会基金监管部发布的《证券投资基金基金合同填报指引第2号——"
            + "指数型证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。"
            + "根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对"
            + "《指引》部分条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    public static String LEADING_TEXT_BOND = "（以下简称“《基金合同》”）系按照中国证监"
            + "会基金监管部发布的《证券投资基金基金合同填报指引第3号——"
            + "证券投资基金基金合同填报指引（试行）》（以下简称“《指引》”）撰写。"
            + "根据基金托管人和律师事务所的意见，我公司在撰写《基金合同》时对《指引》部分"
            + "条款进行了增加、删除或修改，现将具体情况详细说明如下。";
    public static String LEADING_TEXT_CURRENCY = "（以下简称“《基金合同》”）系按照中国"
            + "证监会基金监管部发布的《证券投资基金基金合同填报指引第4号——货币市场基金基金合同"
            + "填报指引（试行）》（以下简称“《指引》”）撰写。根据基金托管人和律师事务所的意见，"
            + "我公司在撰写《基金合同》时对《指引》部分条款进行了增加、删除或修改，"
            + "现将具体情况详细说明如下。";

    /**
     * @return error code for generation process 0: no error; -1: unknown error;
     * 1: Fund type not recognized; 2: Fund establisher not recognized; 10:
     * Input file cannot be read (unknown reason); 11: Input file cannot be
     * read: IO error; 20: Output file cannot be generated (unknown reason);
     * 21: Output file cannot be generated: IO error;
     * -10: Cannot find template file; -11: Input template file IO error;
     * @param inputSampleDocPath
     * @param outputDocPath
     * @return
     */
    public static int generate(String inputSampleDocPath, String outputDocPath) {
        DocProcessor sampleDocProcessor = new DocProcessor(inputSampleDocPath);
        try {
            sampleDocProcessor.readText(inputSampleDocPath);
        } catch (IOException e) {
            /// Read input sample document IO error
            return 11;
        }

        FundDoc sampleDoc = sampleDocProcessor.process();

        if (sampleDoc.getType() == FundDoc.CONTRACT_TYPE_UNKNOWN) {
            return 1;
        }
        if (sampleDoc.getEstablisher().equals(FundDoc.CONTRACT_ESTABLISHER_UNKNOWN)) {
            return 2;
        }

        String templateDocPath;
        switch (sampleDoc.getType()) {
            case 0:
                templateDocPath = DataUtils.STANDARD_TYPE_STOCK_C;
                break;
            case 1:
                templateDocPath = DataUtils.STANDARD_TYPE_INDEX;
                break;
            case 2:
                templateDocPath = DataUtils.STANDARD_TYPE_BOND;
                break;
            case 3:
                templateDocPath = DataUtils.STANDARD_TYPE_MONETARY;
                break;
            default:
                templateDocPath = "";
        }

        if (templateDocPath.isEmpty()) {
            return -10;
        }

        DocProcessor templateDocProcessor = new DocProcessor(templateDocPath);
        try {
            templateDocProcessor.readText(templateDocPath);
        } catch (IOException e) {
            /// Read input sample document IO error
            return -11;
        }
        FundDoc templateDoc = templateDocProcessor.process();

        String outputFileTitle = "《" + sampleDoc.getContractNameComplete() + "》";
        StringBuilder leadingTextSB = new StringBuilder();

        leadingTextSB.append(sampleDoc.getContractName());
        leadingTextSB.append("募集申请材料之");
        leadingTextSB.append(outputFileTitle);
        switch (sampleDoc.getType()) {
            case 0:
                leadingTextSB.append(LEADING_TEXT_STOCK);
                break;
            case 1:
                leadingTextSB.append(LEADING_TEXT_INDEX);
                break;
            case 2:
                leadingTextSB.append(LEADING_TEXT_BOND);
                break;
            case 3:
                leadingTextSB.append(LEADING_TEXT_CURRENCY);
                break;
            default:
                break;
        }
        String leadingText = leadingTextSB.toString();

        GenerateCompareDoc genDoc = new GenerateCompareDoc();
        List<PatchDto> patchDtoList;
        CompareUtils compareUtils = new CompareUtils();
        try {
            patchDtoList = compareUtils.getPatchDtoList(templateDocPath, inputSampleDocPath);
        } catch (Exception e1) {
            return -1;
        }

        try {
            genDoc.generate(outputFileTitle, leadingText, patchDtoList, outputDocPath);
        } catch (IOException e) {
            e.printStackTrace();
            return 21;
        }

        return 0;
    }
}
