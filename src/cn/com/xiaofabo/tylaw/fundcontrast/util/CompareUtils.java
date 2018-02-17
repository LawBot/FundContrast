package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.util.*;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Delta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PartMatch;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;
import cn.com.xiaofabo.tylaw.fundcontrast.textprocessor.DocProcessor;

public class CompareUtils {

    // sort patchDtoList
    List<String> sortIdList = new ArrayList<>();

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
            pdt.setPartIndex(dp.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(dp.getPartIndex().get(0)).getTitle());
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
            pdt.setPartIndex(dp.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(dp.getPartIndex().get(0)).getTitle());
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
            pdt.setPartIndex(samplePart.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(templatePart.getPartIndex().get(0)).getTitle());
            /// Then compare children parts
            compareParts(patchDtoList, templateDoc, sampleDoc, templatePart, samplePart);
        }

        Collections.sort(patchDtoList);
        return patchDtoList;
    }

    private void compareParts(List patchDtoList, 
            FundDoc templateDoc, FundDoc sampleDoc, 
            DocPart templatePart, DocPart samplePart) 
            throws Exception {
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
            PatchDto pdt = new PatchDto();
            pdt.setRevisedDto(revisedDto);
            pdt.setOrignalText(templateText);
            pdt.setIndexType("orginal");
            pdt.setChangeType("change");
            pdt.setPartId(templatePart.getPartCount());
            pdt.setPartIndex(templatePart.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(templatePart.getPartIndex().get(0)).getTitle());
            pdt.setOrignalText(templateText);
            patchDtoList.add(pdt);
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
            pdt.setPartIndex(dp.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(dp.getPartIndex().get(0)).getTitle());
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
            pdt.setPartIndex(dp.getPartIndex());
            pdt.setChapterTitle(templateDoc.getParts().get(dp.getPartIndex().get(0)).getTitle());
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
            pdt.setPartIndex(tPart.getPartIndex());

            /// Then compare children parts
            compareParts(patchDtoList, templateDoc, sampleDoc, tPart, sPart);
        }
    }

    public List<String> getSortIdList() {
        return sortIdList;
    }
}
