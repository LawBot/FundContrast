package cn.com.xiaofabo.tylaw.fundcontrast.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Delta;
import com.github.difflib.patch.Patch;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils;


public class CompareTest {

	public static void main(String[] args) throws IOException {
			String testPath = "data/Sample/华夏基金/债券/华夏鼎沛债券型证券投资基金基金合同.docx";
	        String testPath2 = "data/Sample/华夏基金/货币/华夏兴金宝货币市场基金基金合同（草案） 1026.docx";
	        String path3="data/Sample/九泰基金/20170419九泰天泽混合型/九泰天泽灵活配置混合型证券投资基金基金合同（草案）-申请用印版0419-修改为灵配混合0505.doc";
	        
	        DocProcessor dp = new DocProcessor(testPath);
	        dp.readText(testPath);
	        FundDoc fd = dp.process();
	        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();
	        
	        DocProcessor dp2 = new DocProcessor(testPath2);
	        dp2.readText(testPath2);
	        FundDoc fd2 = dp2.process();
	        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();
	        try {
	        	List<PatchDto> patchDtoList = CompareUtils.doCompare(orignalCompareDtoList, revisedCompareDtoList);
	        	System.out.println(patchDtoList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		
	}
	
	


}
