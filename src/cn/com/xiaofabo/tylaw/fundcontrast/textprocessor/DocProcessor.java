/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.util.TextUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 陈光曦
 */
public class DocProcessor extends TextProcessor {

    public FundDoc fundDoc;
    private List partIdentifiers;

    public DocProcessor(String docName) {
        this.fundDoc = new FundDoc(docName);
        partIdentifiers = new LinkedList();
        partIdentifiers.add("^第.*?部分.*?[^0-9]$");
        partIdentifiers.add("^[一|二|三|四|五|六|七|八|九|十].*?、");
        partIdentifiers.add("^[（|(][一|二|三|四|五|六|七|八|九|十].*?[）|)]");
        partIdentifiers.add("^\\d+[、|\\.|．]");
        partIdentifiers.add("^[（|(]\\d+[）|)]");
    }

    public FundDoc process() {
        int currentLevel = -1;
        int lastPartLevel = -1;
        int lineIdx = 0;
        List textList = TextUtils.removeAllEmptyLines(super.getLines());
        StringBuilder partText = new StringBuilder();
        List<DocPart> tmpPartList = new LinkedList();
        List<Integer> levelMatchList = new LinkedList();
        DocPart currentPart = new DocPart();
        boolean foundFirstIdentifier = false;
        while (lineIdx < textList.size()) {
            String currentLine = ((String) textList.get(lineIdx)).trim();
//            System.out.println("Processing line number: " + lineIdx);
//            System.out.println(currentLine);
            int currentPartLevel = -1;
            boolean foundIdentifier = false;
            for (int i = 0; i < partIdentifiers.size(); ++i) {
                String identifierStr = (String) partIdentifiers.get(i);
                Pattern pattern = Pattern.compile(identifierStr);
                Matcher matcher = pattern.matcher(currentLine);
                if (matcher.find()) {
                    currentPartLevel = i;
                    foundIdentifier = true;
                    foundFirstIdentifier = true;
                    break;
                }
            }

            if (!foundFirstIdentifier) {
                ++lineIdx;
                continue;
            }

            if (currentPartLevel == 0) {
                /// Every time a new chapter starts, set a new matching scheme
                levelMatchList = new LinkedList();
                /// Actual level 0 is always matching part level 0
                levelMatchList.add(0);
            } else {
                if (currentPartLevel >= 0 && !levelMatchList.contains(currentPartLevel)) {
                    levelMatchList.add(currentPartLevel);
                }
            }

            if (!foundIdentifier) {
                partText.append(currentLine);
            } else {
                if (lastPartLevel < currentPartLevel) {
                    if (!partText.toString().isEmpty()) {
                        currentPart.setText(partText.toString());
                        partText = new StringBuilder();
                    }
                    currentPart = new DocPart();
                    String title = TextUtils.getPartTitle(currentLine);
                    currentPart.setTitle(title);
                    tmpPartList.add(currentPart);
                    ++currentLevel;
                } else if (lastPartLevel > currentPartLevel) {
                    if (!partText.toString().isEmpty()) {
                        currentPart.setText(partText.toString());
                        partText = new StringBuilder();
                    }
                    DocPart parentDp = (DocPart) tmpPartList.get(currentLevel - 1);
                    parentDp.addPart(currentPart);
                    tmpPartList.remove(currentLevel);

                    int actualLevel = levelMatchList.indexOf(currentPartLevel);
                    int endLevel = actualLevel == 0 ? 0 : actualLevel - 1;
                    for (int i = tmpPartList.size() - 1; i > endLevel; --i) {
                        DocPart dp = tmpPartList.get(i);
                        parentDp = tmpPartList.get(i - 1);
                        parentDp.addPart(dp);
                        tmpPartList.remove(i);
                    }
                    if (actualLevel == 0) {
                        fundDoc.addPart(tmpPartList.get(0));
                        tmpPartList = new LinkedList();
                    }
                    currentPart = new DocPart();
                    String title = TextUtils.getPartTitle(currentLine);
                    currentPart.setTitle(title);
                    tmpPartList.add(currentPart);
                    currentLevel = actualLevel;
                } else {  //(currentPartLevel == tmpPartLevel)
                    if (!partText.toString().isEmpty()) {
                        currentPart.setText(partText.toString());
                        partText = new StringBuilder();
                    }
                    if (currentPartLevel == 0) {
                        fundDoc.addPart(tmpPartList.get(0));
                        tmpPartList = new LinkedList();
                    } else {
                        DocPart parentDp = (DocPart) tmpPartList.get(currentLevel - 1);
                        parentDp.addPart(currentPart);
                    }
                    currentPart = new DocPart();
                    String title = TextUtils.getPartTitle(currentLine);
                    currentPart.setTitle(title);
                    if (currentPartLevel == 0) {
                        tmpPartList.add(currentPart);
                    } else {
                        tmpPartList.set(currentLevel, currentPart);
                    }
                }
                lastPartLevel = currentPartLevel;
            }

            if (lineIdx == textList.size() - 1) {
                if (!partText.toString().isEmpty()) {
                    currentPart.setText(partText.toString());
                }
                for (int i = tmpPartList.size() - 1; i > 0; --i) {
                    DocPart dp = tmpPartList.get(i);

                    DocPart parentDp = tmpPartList.get(i - 1);
                    parentDp.addPart(dp);
                    tmpPartList.remove(i);
                }
                fundDoc.addPart(tmpPartList.get(0));
            }
            ++lineIdx;
        }
        return fundDoc;
    }
}
