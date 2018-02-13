package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.io.IOException;
import java.util.*;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Delta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PartMatch;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;

public class CompareUtils3 {


    // sort patchDtoList
    List<String> sortIdList = new ArrayList<>();

    public static PatchDto doCompare(String orignalCompare, String revisedCompare) throws Exception {
        try {
            RevisedDto revisedDto = compare(orignalCompare, revisedCompare);
            revisedDto.setRevisedText(revisedCompare);
            PatchDto patchDto = new PatchDto();
            patchDto.setRevisedDto(revisedDto);
            patchDto.setOrignalText(orignalCompare);
            patchDto.setIndexType("orginal");
            patchDto.setChangeType("change");
            return patchDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }

    }

    public static RevisedDto compare(String original, String revised) {
        RevisedDto revisedDto = new RevisedDto();
        revisedDto.setRevisedText(revised);
        String[] a = revised.split("(?![-\\w])");
        List<String> list = new ArrayList<String>();
        for (String s : a) {
            if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
                list.add(s.trim());
            }
        }
        String[] b = original.split("(?![-\\w])");
        List<String> list1 = new ArrayList<String>();
        for (String s : b) {
            if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
                list1.add(s.trim());
            }
        }
        list1.removeAll(list);

        if (original.length() >= revised.length()) {
//			System.out.println("revised增加了："+compareStrshort(revised, original, "blue"));
//			System.out.println("revised减少了："+compareStrLong(revised, original, "blue"));
//			revisedDto.setAddData(compareStrshort(revised, original, "blue"));
//			revisedDto.setDeleteData(compareStrLong(revised, original, "blue"));
            //按Key进行排序
            Map<Integer, Character> addDataMap = sortMapByKey(compareStrshort(revised, original, "blue"));
            Map<Integer, Character> deleteDataMap = sortMapByKey(compareStrLong(revised, original, "blue"));
            revisedDto.setAddData(addDataMap);
            revisedDto.setDeleteData(deleteDataMap);

        } else if (original.length() < revised.length()) {
//			System.out.println("revised增加了："+compareStrLong(revised, original, "blue"));
//			System.out.println("revised减少了："+compareStrshort(revised, original, "blue"));
//			revisedDto.setAddData(compareStrLong(revised, original, "blue"));
//			revisedDto.setDeleteData(compareStrshort(revised, original, "blue"));
            //按Key进行排序
            Map<Integer, Character> addDataMap = sortMapByKey(compareStrLong(revised, original, "blue"));
            Map<Integer, Character> deleteDataMap = sortMapByKey(compareStrshort(revised, original, "blue"));
            revisedDto.setAddData(addDataMap);
            revisedDto.setDeleteData(deleteDataMap);

        }

        List<Integer> keyList = new ArrayList<Integer>();
        return revisedDto;

    }

    public static Map compareStrLong(String char1, String char2, String colour) {
        String bcolor = "";
        String ecolor = "";
        StringBuffer sb = new StringBuffer();
        char[] a = new char[char1.length()];
        for (int i = 0; i < char1.length(); i++) {
            a[i] = char1.charAt(i);
        }
        char[] b = new char[char2.length()];
        for (int i = 0; i < char2.length(); i++) {
            b[i] = char2.charAt(i);
        }
        // 不同字符集合
        Map map1 = new HashMap();
        // 包含字符集合
        Map map2 = new HashMap();
        if (char1.length() > char2.length()) {
            for (int i = 0; i < a.length; i++) {
                if (i == a.length - 1) {
                    if (i > 1) {
                        if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                            map2.put(i - 1, a[i - 1]);
                            map2.put(i, a[i]);
                        } else {
                            map1.put(i, a[i]);
                        }
                    } else {
                        map2.put(i, a[i]);
                    }
                } else {
                    if (String.valueOf(b).contains(String.valueOf(a[i]) + String.valueOf(a[i + 1]))) {
                        if (i > 1) {
                            if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                                map2.put(i - 1, a[i - 1]);
                                map2.put(i, a[i]);
                            }
                        } else {
                            map2.put(i, a[i]);
                        }
                    } else {
                        if (i > 0) {
                            if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                                map2.put(i - 1, a[i - 1]);
                                map2.put(i, a[i]);
                            } else {
                                map1.put(i, a[i]);
                            }
                        } else {
                            map1.put(i, a[i]);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < b.length; i++) {
                if (i == b.length - 1) {
                    if (i > 1) {
                        if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                            map2.put(i - 1, b[i - 1]);
                            map2.put(i, b[i]);
                        } else {
                            map1.put(i, b[i]);
                        }
                    } else {
                        map2.put(i, b[i]);
                    }
                } else {
                    if (String.valueOf(a).contains(String.valueOf(b[i]) + String.valueOf(b[i + 1]))) {
                        if (i > 1) {
                            if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                                map2.put(i - 1, b[i - 1]);
                                map2.put(i, b[i]);
                            }
                        } else {
                            map2.put(i, b[i]);
                        }
                    } else {
                        if (i > 0) {
                            if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                                map2.put(i - 1, b[i - 1]);
                                map2.put(i, b[i]);
                            } else {
                                map1.put(i, b[i]);
                            }
                        } else {
                            map1.put(i, b[i]);
                        }
                    }
                }
            }
        }
        if (char1.length() > char2.length()) {
            for (int i = 0; i < a.length; i++) {
                if (map1.get(i) != null) {
                    sb.append(bcolor).append(map1.get(i)).append(ecolor);
                } else if (map2.get(i) != null) {
                    sb.append(map2.get(i));
                }
            }
        } else if (char1.length() <= char2.length()) {
            for (int i = 0; i < b.length; i++) {
                if (map1.get(i) != null) {
                    sb.append(bcolor).append(map1.get(i)).append(ecolor);
                } else if (map2.get(i) != null) {
                    sb.append(map2.get(i));
                }
            }
        }
//		System.out.println("map1:" + map1);
//		System.out.println("map2:" + map2);
//		return sb.toString();
        return map1;
    }

    public static Map compareStrshort(String char1, String char2, String colour) {
        String bcolor = "";
        String ecolor = "";
        StringBuffer sb = new StringBuffer();
        char[] a = new char[char1.length()];
        for (int i = 0; i < char1.length(); i++) {
            a[i] = char1.charAt(i);
        }
        char[] b = new char[char2.length()];
        for (int i = 0; i < char2.length(); i++) {
            b[i] = char2.charAt(i);
        }
        // 不同字符集合
        Map map1 = new HashMap();
        // 包含字符集合
        Map map2 = new HashMap();
        if (char1.length() > char2.length()) {
            for (int i = 0; i < b.length; i++) {
                if (i == b.length - 1) {
                    if (i > 1) {
                        if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                            map2.put(i - 1, b[i - 1]);
                            map2.put(i, b[i]);
                        } else {
                            map1.put(i, b[i]);
                        }
                    } else {
                        map2.put(i, b[i]);
                    }
                } else {
                    if (String.valueOf(a).contains(String.valueOf(b[i]) + String.valueOf(b[i + 1]))) {
                        if (i > 1) {
                            if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                                map2.put(i - 1, b[i - 1]);
                                map2.put(i, b[i]);
                            }
                        } else {
                            map2.put(i, b[i]);
                        }
                    } else {
                        if (i > 0) {
                            if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
                                map2.put(i - 1, b[i - 1]);
                                map2.put(i, b[i]);
                            } else {
                                map1.put(i, b[i]);
                            }
                        } else {
                            map1.put(i, b[i]);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < a.length; i++) {
                if (i == a.length - 1) {
                    if (i > 1) {
                        if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                            map2.put(i - 1, a[i - 1]);
                            map2.put(i, a[i]);
                        } else {
                            map1.put(i, a[i]);
                        }
                    } else {
                        map2.put(i, a[i]);
                    }
                } else {
                    if (String.valueOf(b).contains(String.valueOf(a[i]) + String.valueOf(a[i + 1]))) {
                        if (i > 1) {
                            if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                                map2.put(i - 1, a[i - 1]);
                                map2.put(i, a[i]);
                            } else {
                                map1.put(i, a[i]);
                            }
                        } else {
                            map2.put(i, a[i]);
                        }
                    } else {
                        if (i > 0) {
                            if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
                                map2.put(i - 1, a[i - 1]);
                                map2.put(i, a[i]);
                            } else {
                                map1.put(i, a[i]);
                            }
                        } else {
                            map1.put(i, a[i]);
                        }
                    }
                }
            }
        }

        if (char1.length() > char2.length()) {
            for (int i = 0; i < a.length; i++) {
                if (map1.get(i) != null) {
                    sb.append(bcolor).append(map1.get(i)).append(ecolor);
                } else if (map2.get(i) != null) {
                    sb.append(map2.get(i));
                }
            }
        } else if (char1.length() <= char2.length()) {
            for (int i = 0; i < b.length; i++) {
                if (map1.get(i) != null) {
                    sb.append(bcolor).append(map1.get(i)).append(ecolor);
                } else if (map2.get(i) != null) {
                    sb.append(map2.get(i));
                }
            }
        }
//		System.out.println(map1);
//		return sb.toString();
        return map1;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<Integer, Character> sortMapByKey(Map<Integer, Character> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<Integer, Character> sortMap = new TreeMap<Integer, Character>(new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    public void compareParts(List patchDtoList, DocPart templatePart, DocPart samplePart) throws Exception {
        String templateText = templatePart.getIndex() + templatePart.getPoint();
        String sampleText = samplePart.getIndex() + samplePart.getPoint();
        /// Compare templateText and sampleText
        /// In case they are different, patchDtoList.add
        if (!templateText.equalsIgnoreCase(sampleText)) {

            Patch<String> patch = DiffUtils.diffInline(templateText, sampleText);
            List<Delta<String>> deltaList = patch.getDeltas();
            Map<Integer, Character> deleteMap = new HashMap();
            Map<Integer, Character> addMap = new HashMap();
            for (Delta<String> delta : deltaList) {
                if (delta.getType().equals(DeltaType.CHANGE)) {
                    for (int i = delta.getOriginal().getPosition(); i < delta.getOriginal().getPosition() + delta.getOriginal().getLines().get(0).length(); i++) {
                        deleteMap.put(i, templateText.charAt(i));
                    }
                    for (int i = delta.getRevised().getPosition(); i < delta.getRevised().getPosition() + delta.getRevised().getLines().get(0).length(); i++) {
                        addMap.put(i, sampleText.charAt(i));
                    }
                }
                if (delta.getType().equals(DeltaType.DELETE)) {
                    for (int i = delta.getOriginal().getPosition(); i < delta.getOriginal().getPosition() + delta.getOriginal().getLines().get(0).length(); i++) {
                        deleteMap.put(i, templateText.charAt(i));
                    }
                }
                if (delta.getType().equals(DeltaType.INSERT)) {
                    for (int i = delta.getRevised().getPosition(); i < delta.getRevised().getPosition() + delta.getRevised().getLines().get(0).length(); i++) {
                        addMap.put(i, sampleText.charAt(i));
                    }
                }
            }
            RevisedDto revisedDto = new RevisedDto();
            revisedDto.setAddData(addMap);
            revisedDto.setDeleteData(deleteMap);
            revisedDto.setRevisedText(sampleText);
            PatchDto patchDto = new PatchDto();
            patchDto.setRevisedDto(revisedDto);
            patchDto.setOrignalText(templateText);
            patchDto.setIndexType("orginal");
            patchDto.setChangeType("change");
            patchDto.setPartId(templatePart.getPartCount());
            patchDto.setOrignalText(templateText);
            patchDtoList.add(patchDto);

//        	PatchDto patchDto = doCompare(templateText, sampleText);
//                patchDto.setOrignalText(templatePart.getIndex() + patchDto.getOrignalText());
//                RevisedDto tmpDto = patchDto.getRevisedDto();
//                tmpDto.setRevisedText(samplePart.getIndex() + tmpDto.getRevisedText());
//                patchDto.setRevisedDto(tmpDto);
//            patchDtoList.add(patchDto);
//            PatchDto p = new PatchDto();
//            p.setChapterIndex(0);
//            p.setChangeType("change");
//            p.setOrignalText(templateText);
//            RevisedDto r = new RevisedDto();
//            r.setRevisedText(sampleText);
//            p.setRevisedDto(r);
//            patchDtoList.add(p);
        }

        if (!templatePart.hasPart() && !samplePart.hasPart()) {
            return;
        }

        List templateTitles = new LinkedList();
        List sampleTitles = new LinkedList();

        for (int i = 0; templatePart.hasPart() && i < templatePart.getChildPart().size(); ++i) {
            String title = ((DocPart) templatePart.getChildPart().get(i)).getTitle();
            templateTitles.add(title);
        }
        for (int i = 0; samplePart.hasPart() && i < samplePart.getChildPart().size(); ++i) {
            String title = ((DocPart) samplePart.getChildPart().get(i)).getTitle();
            sampleTitles.add(title);
        }

        PartMatch partMatch = StringSimUtils.findBestMatch(templateTitles, sampleTitles);
        List addList = partMatch.getAddList();
        List deleteList = partMatch.getDeleteList();
        Map matchList = partMatch.getMatchList();

        for (int i = 0; i < deleteList.size(); ++i) {
            int chapterIndex = (int) deleteList.get(i);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(chapterIndex);
            pdt.setChangeType("delete");
            /// TODO: should be recursive
            /// Delete means exists in template but not in sample
            DocPart dp = templatePart.getChildPart().get(chapterIndex);
            String pointText = dp.getWholePoint();
            pdt.setOrignalText(pointText);
            RevisedDto rdt = new RevisedDto();
            for (int j = 0; j < pointText.length(); ++j) {
                Character c = pointText.charAt(j);
                rdt.deleteData(j, c);
            }
            pdt.setPartId(dp.getPartCount());
            pdt.setRevisedDto(rdt);
            patchDtoList.add(pdt);
        }

        for (int i = 0; i < addList.size(); ++i) {
            int chapterIndex = (int) addList.get(i);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(chapterIndex);
            pdt.setChangeType("add");
            /// TODO: should be recursive
            /// Add means exists in sample but not in template
            DocPart dp = samplePart.getChildPart().get(chapterIndex);
            String pointText = dp.getWholePoint();
            RevisedDto rdt = new RevisedDto();
            rdt.setRevisedText(pointText);
            for (int j = 0; j < pointText.length(); ++j) {
                Character c = pointText.charAt(j);
                rdt.addData(j, c);
            }
            pdt.setRevisedDto(rdt);
            pdt.setPartId(dp.getPartCount());
            patchDtoList.add(pdt);
        }

        Iterator it = matchList.keySet().iterator();
        while (it.hasNext()) {
            int templateIndex = (int) it.next();
            int sampleIndex = (int) matchList.get(templateIndex);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(sampleIndex);
            pdt.setChangeType("change");
            DocPart tPart = templatePart.getChildPart().get(templateIndex);
            DocPart sPart = samplePart.getChildPart().get(sampleIndex);
            pdt.setPartId(tPart.getPartCount());

            /// Then compare children parts
            compareParts(patchDtoList, tPart, sPart);
        }
    }

    public List<PatchDto> getPatchDtoList(String templatePath, String samplePath) throws Exception {
        List<PatchDto> patchDtoList = new LinkedList();

        DocProcessor templateProcessor = new DocProcessor(templatePath);
        templateProcessor.readText(templatePath);
        FundDoc templateDoc = templateProcessor.process();

        DocProcessor sampleProcessor = new DocProcessor(samplePath);
        sampleProcessor.readText(samplePath);
        FundDoc sampleDoc = sampleProcessor.process();

        /// Compare first level part
        List templateTitles = new LinkedList();
        List sampleTitles = new LinkedList();

        for (int i = 0; i < templateDoc.getParts().size(); ++i) {
            String title = ((DocPart) templateDoc.getParts().get(i)).getTitle();
            templateTitles.add(title);
        }
        for (int i = 0; i < sampleDoc.getParts().size(); ++i) {
            String title = ((DocPart) sampleDoc.getParts().get(i)).getTitle();
            sampleTitles.add(title);
        }

        PartMatch partMatch = StringSimUtils.findBestMatch(templateTitles, sampleTitles);
        List addList = partMatch.getAddList();
        List deleteList = partMatch.getDeleteList();
        Map matchList = partMatch.getMatchList();

        for (int i = 0; i < deleteList.size(); ++i) {
            int chapterIndex = (int) deleteList.get(i);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(chapterIndex);
            pdt.setChangeType("delete");

            /// TODO: should be recursive
            /// Delete means exists in template but not in sample
            DocPart dp = templateDoc.getParts().get(chapterIndex);
            String pointText = dp.getWholePoint();
            pdt.setOrignalText(pointText);
            RevisedDto rdt = new RevisedDto();
            for (int j = 0; j < pointText.length(); ++j) {
                Character c = pointText.charAt(j);
                rdt.deleteData(j, c);
            }
            pdt.setPartId(dp.getPartCount());
            pdt.setRevisedDto(rdt);
            patchDtoList.add(pdt);
        }

        for (int i = 0; i < addList.size(); ++i) {
            int chapterIndex = (int) addList.get(i);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(chapterIndex);
            pdt.setChangeType("add");
            /// TODO: should be recursive
            /// Add means exists in sample but not in template
            DocPart dp = sampleDoc.getParts().get(chapterIndex);
            String pointText = dp.getWholePoint();
            RevisedDto rdt = new RevisedDto();
            rdt.setRevisedText(pointText);
            for (int j = 0; j < pointText.length(); ++j) {
                Character c = pointText.charAt(j);
                rdt.addData(j, c);
            }
            pdt.setPartId(dp.getPartCount());
            pdt.setRevisedDto(rdt);
            patchDtoList.add(pdt);
        }

        Iterator it = matchList.keySet().iterator();
        while (it.hasNext()) {
            int templateIndex = (int) it.next();
            int sampleIndex = (int) matchList.get(templateIndex);
            PatchDto pdt = new PatchDto();
            pdt.setChapterIndex(sampleIndex);
            pdt.setChangeType("change");
            DocPart templatePart = templateDoc.getParts().get(templateIndex);
            DocPart samplePart = sampleDoc.getParts().get(sampleIndex);
            pdt.setPartId(samplePart.getPartCount());
            /// Then compare children parts
            compareParts(patchDtoList, templatePart, samplePart);
        }


        for (PatchDto p : patchDtoList) {
            sortIdList.add(p.getPartId());
        }


        Collections.sort(sortIdList);
        return patchDtoList;
    }

    public List<String> getSortIdList() {
        return sortIdList;
    }
}
