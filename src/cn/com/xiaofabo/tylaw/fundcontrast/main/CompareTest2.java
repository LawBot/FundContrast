package cn.com.xiaofabo.tylaw.fundcontrast.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.MatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;
import cn.com.xiaofabo.tylaw.fundcontrast.util.CompareUtils2;
import cn.com.xiaofabo.tylaw.fundcontrast.util.DataUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.StringSimUtils;
import cn.com.xiaofabo.tylaw.fundcontrast.util.TextUtils;

public class CompareTest2 {

    public static void main(String[] args) throws IOException {
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

        List<List<CompareDto>> group1List = getListByGroup(orignalCompareDtoList);
        List<List<CompareDto>> group2List = getListByGroup(revisedCompareDtoList);

        List<String> s1 = new LinkedList<String>();
        List<String> s2 = new LinkedList<String>();
        for (int i = 0; i < group1List.size(); i++) {
            s1.add(group1List.get(i).get(0).getText());
        }
        for (int i = 0; i < group2List.size(); i++) {
            s2.add(group2List.get(i).get(0).getText());
        }
        List<MatchDto> matchList = StringSimUtils.findBestMatch(s1, s2, "0");
        List<PatchDto> patchDtoList = new ArrayList<PatchDto>();

        for (int i = 0; i < matchList.size(); i++) {
            List<String> str1List = new LinkedList<String>();
            List<String> str2List = new LinkedList<String>();
            List<Integer> chapterIndexList = new ArrayList<Integer>();
            for (int j = 0; j < matchList.size(); j++) {
                chapterIndexList.add(matchList.get(j).getRevisedIndex());
            }
            //当新条文中间新增chapter的情况
            for (int j = 0; j < matchList.size(); j++) {
                if (!chapterIndexList.contains(j) && chapterIndexList.get(chapterIndexList.size() - 1) > j) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }

            //当chapter能对应的情况
            if (matchList.get(i).getRevisedIndex() != -1) {
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    str1List.add(group1List.get(matchList.get(i).getOrignalIndex()).get(j).getText());
                }
                for (int j = 0; j < group2List.get(matchList.get(i).getRevisedIndex()).size(); j++) {
                    str2List.add(group2List.get(matchList.get(i).getRevisedIndex()).get(j).getText());
                }
                List<MatchDto> compareMatchList = StringSimUtils.findBestMatch(str1List, str2List, "");

                List<Integer> revisedIndexList = new ArrayList<Integer>();
                for (int j = 0; j < compareMatchList.size(); j++) {
                    revisedIndexList.add(compareMatchList.get(j).getRevisedIndex());
                }

                for (int j = 0; j < compareMatchList.size(); j++) {
                    //当新条文中间处新增section的情况
                    if (!revisedIndexList.contains(j) && revisedIndexList.get(revisedIndexList.size() - 1) > j) {
                        CompareDto compareDto = group2List.get(matchList.get(i).getRevisedIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                    //当section一一对应上的情况
                    if (compareMatchList.get(j).getRevisedIndex() != -1) {
                        try {
                            if (compareMatchList.get(j).getBestRatio() != 1) {
                                PatchDto patchDto = CompareUtils2.doCompare(group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex()), group2List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getRevisedIndex()));
                                patchDtoList.add(patchDto);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {//当右边值为-1 新条文删减的情况
                        CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setOrignalText(compareDto.getText());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("orginal");
                        patchDto.setChangeType("delete");
                        patchDtoList.add(patchDto);
                    }

                }
                //当新条文section结尾新增的情况
                if (group2List.get(i).size() > compareMatchList.size()) {
                    for (int j = compareMatchList.size(); j < group2List.get(i).size(); j++) {
                        CompareDto compareDto = group2List.get(i).get(j);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            } else {//当chapter右边为-1 删减chapter的情况
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(j);
                    RevisedDto revisedDto = new RevisedDto();
                    PatchDto patchDto = new PatchDto();
                    patchDto.setRevisedDto(revisedDto);
                    patchDto.setChapterIndex(compareDto.getChapterIndex());
                    patchDto.setOrignalText(compareDto.getText());
                    patchDto.setSectionIndex(compareDto.getSectionIndex());
                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                    patchDto.setIndexType("orginal");
                    patchDto.setChangeType("delete");
                    patchDtoList.add(patchDto);
                }

            }
            //当新条文chapter结尾处新增的情况
            if (group2List.size() > matchList.size()) {
                for (int j = matchList.size(); j < group2List.size(); j++) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }
        }
        System.out.println(patchDtoList);//patchDtoList 为最终值 请参考


    }

    private static List<List<CompareDto>> getListByGroup(List<CompareDto> list) {
        List<List<CompareDto>> result = new ArrayList<List<CompareDto>>();
        Map<Integer, List<CompareDto>> map = new TreeMap<Integer, List<CompareDto>>();

        for (CompareDto bean : list) {
            if (map.containsKey(bean.getChapterIndex())) {
                List<CompareDto> t = map.get(bean.getChapterIndex());
                t.add(bean);
                new ArrayList<CompareDto>().add(bean);
                map.put(bean.getChapterIndex(), t);
            } else {
                List<CompareDto> t = new ArrayList<CompareDto>();
                t.add(bean);
                map.put(bean.getChapterIndex(), t);
            }
        }
        for (Entry<Integer, List<CompareDto>> entry : map.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    public List<PatchDto> getPatchDtoList(String testPath, String testPath2) {
        DocProcessor dp = new DocProcessor(testPath);
        try {
            dp.readText(testPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FundDoc fd = dp.process();
        List<CompareDto> orignalCompareDtoList = fd.getFundDoc();

        DocProcessor dp2 = new DocProcessor(testPath2);
        try {
            dp2.readText(testPath2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FundDoc fd2 = dp2.process();
        List<CompareDto> revisedCompareDtoList = fd2.getFundDoc();

        List<List<CompareDto>> group1List = getListByGroup(orignalCompareDtoList);
        List<List<CompareDto>> group2List = getListByGroup(revisedCompareDtoList);

        List<String> s1 = new LinkedList<String>();
        List<String> s2 = new LinkedList<String>();
        for (int i = 0; i < group1List.size(); i++) {
            s1.add(group1List.get(i).get(0).getText());
        }
        for (int i = 0; i < group2List.size(); i++) {
            s2.add(group2List.get(i).get(0).getText());
        }
        List<MatchDto> matchList = StringSimUtils.findBestMatch(s1, s2, "0");
        List<PatchDto> patchDtoList = new ArrayList<PatchDto>();

        for (int i = 0; i < matchList.size(); i++) {
            List<String> str1List = new LinkedList<String>();
            List<String> str2List = new LinkedList<String>();
            List<Integer> chapterIndexList = new ArrayList<Integer>();
            for (int j = 0; j < matchList.size(); j++) {
                chapterIndexList.add(matchList.get(j).getRevisedIndex());
            }
            //当新条文中间新增chapter的情况
            for (int j = 0; j < matchList.size(); j++) {
                if (!chapterIndexList.contains(j) && chapterIndexList.get(chapterIndexList.size() - 1) > j) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }

            //当chapter能对应的情况
            if (matchList.get(i).getRevisedIndex() != -1) {
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    str1List.add(group1List.get(matchList.get(i).getOrignalIndex()).get(j).getText());
                }
                for (int j = 0; j < group2List.get(matchList.get(i).getRevisedIndex()).size(); j++) {
                    str2List.add(group2List.get(matchList.get(i).getRevisedIndex()).get(j).getText());
                }
                List<MatchDto> compareMatchList = StringSimUtils.findBestMatch(str1List, str2List, "");

                List<Integer> revisedIndexList = new ArrayList<Integer>();
                for (int j = 0; j < compareMatchList.size(); j++) {
                    revisedIndexList.add(compareMatchList.get(j).getRevisedIndex());
                }

                for (int j = 0; j < compareMatchList.size(); j++) {
                    //当新条文中间处新增section的情况
                    if (!revisedIndexList.contains(j) && revisedIndexList.get(revisedIndexList.size() - 1) > j) {
                        CompareDto compareDto = group2List.get(matchList.get(i).getRevisedIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                    //当section一一对应上的情况
                    if (compareMatchList.get(j).getRevisedIndex() != -1) {
                        try {
                            if (compareMatchList.get(j).getBestRatio() != 1) {
                                PatchDto patchDto = CompareUtils2.doCompare(group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex()), group2List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getRevisedIndex()));
                                patchDtoList.add(patchDto);
                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {//当右边值为-1 新条文删减的情况
                        CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(compareMatchList.get(j).getOrignalIndex());
                        RevisedDto revisedDto = new RevisedDto();
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setOrignalText(compareDto.getText());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("orginal");
                        patchDto.setChangeType("delete");
                        patchDtoList.add(patchDto);
                    }

                }
                //当新条文section结尾新增的情况
                if (group2List.get(i).size() > compareMatchList.size()) {
                    for (int j = compareMatchList.size(); j < group2List.get(i).size(); j++) {
                        CompareDto compareDto = group2List.get(i).get(j);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            } else {//当chapter右边为-1 删减chapter的情况
                for (int j = 0; j < group1List.get(matchList.get(i).getOrignalIndex()).size(); j++) {
                    CompareDto compareDto = group1List.get(matchList.get(i).getOrignalIndex()).get(j);
                    RevisedDto revisedDto = new RevisedDto();
                    PatchDto patchDto = new PatchDto();
                    patchDto.setRevisedDto(revisedDto);
                    patchDto.setChapterIndex(compareDto.getChapterIndex());
                    patchDto.setOrignalText(compareDto.getText());
                    patchDto.setSectionIndex(compareDto.getSectionIndex());
                    patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                    patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                    patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                    patchDto.setIndexType("orginal");
                    patchDto.setChangeType("delete");
                    patchDtoList.add(patchDto);
                }

            }
            //当新条文chapter结尾处新增的情况
            if (group2List.size() > matchList.size()) {
                for (int j = matchList.size(); j < group2List.size(); j++) {
                    for (int k = 0; k < group2List.get(j).size(); k++) {
                        CompareDto compareDto = group2List.get(j).get(k);
                        RevisedDto revisedDto = new RevisedDto();
                        revisedDto.setRevisedText(compareDto.getText());
                        PatchDto patchDto = new PatchDto();
                        patchDto.setRevisedDto(revisedDto);
                        patchDto.setChapterIndex(compareDto.getChapterIndex());
                        patchDto.setSectionIndex(compareDto.getSectionIndex());
                        patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
                        patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
                        patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
                        patchDto.setIndexType("revised");
                        patchDto.setChangeType("add");
                        patchDtoList.add(patchDto);
                    }
                }
            }
        }
        return patchDtoList;
    }
}
