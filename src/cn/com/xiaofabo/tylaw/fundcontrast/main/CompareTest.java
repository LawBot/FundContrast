package cn.com.xiaofabo.tylaw.fundcontrast.main;

import java.io.IOException;
import java.util.List;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.StringSimUtils;
import com.github.difflib.algorithm.DiffException;
import java.util.LinkedList;

public class CompareTest {

    public static void main(String[] args) throws IOException, DiffException {
        String testPath = DataUtils.STANDARD_TYPE_STOCK_C;
        String testPath2 = DataUtils.SAMPLE_GYRX_STOCK_1;

        DocProcessor dp = new DocProcessor(testPath);
        dp.readText(testPath);
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

        DocProcessor dp2 = new DocProcessor(testPath2);
        dp2.readText(testPath2);
        FundDoc fd2 = dp2.process();
        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();

        for (int c = 0; c < 22; ++c) {
            if (!fd.getParts().get(c).hasPart()) {
                continue;
            }
            System.out.println("Chapter " + (c + 1) + "=======================");
            List<String> s1 = new LinkedList<>();
            List<DocPart> pList = fd.getParts().get(c).getChildPart();
            for (int i = 0; i < pList.size(); ++i) {
                DocPart dPart = pList.get(i);
                s1.add(dPart.getTitle());
            }
            List<String> s2 = new LinkedList();
            List<DocPart> pList2 = fd2.getParts().get(c).getChildPart();
            for (int i = 0; i < pList2.size(); ++i) {
                DocPart dPart = pList2.get(i);
                s2.add(dPart.getTitle());
            }

            List matchList = StringSimUtils.findBestMatch(s1, s2);
        }
//        for (int i = 0; i < matchList.size(); ++i) {
//            System.out.print(i + " = " + matchList.get(i));
//            if (i != (int)matchList.get(i)) {
//                System.out.println("  <===");
//            }else{
//                System.out.println("");
//            }
//        }

//        
//        try {
//            List<PatchDto> patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList);
//            System.out.println(patchDtoList);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

}
