/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.textprocessor;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.DocPart;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.FundDoc;
import cn.com.xiaofabo.tylaw.fundcontrast.util.TextUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈光曦
 */
public class DocProcessor extends TextProcessor {

    public FundDoc fundDoc;
    public String titleOfGenDoc = "";
    public String textOfGenDoc = "";
    private List partIdentifiers;

    public DocProcessor(String docName) {
        this.fundDoc = new FundDoc(docName);
        partIdentifiers = new LinkedList();
        partIdentifiers.add(TextUtils.REGEX_IDENTIFIER_LEVEL_0_TP);
        partIdentifiers.add(TextUtils.REGEX_IDENTIFIER_LEVEL_1);
        partIdentifiers.add(TextUtils.REGEX_IDENTIFIER_LEVEL_2);
        partIdentifiers.add(TextUtils.REGEX_IDENTIFIER_LEVEL_3);
        partIdentifiers.add(TextUtils.REGEX_IDENTIFIER_LEVEL_4);
    }

    // 0--3
    public String getNameForGenDoc() {
        return this.titleOfGenDoc;
    }

    public String getTextForGenDoc() {
        return this.textOfGenDoc;
    }

    public FundDoc process() {
        int currentLevel = -1;
        int lastPartLevel = -1;
        int lineIdx = 0;
        int partIndex = 0;
        List textList = TextUtils.removeAllEmptyLines(super.getLines());
        StringBuilder partText = new StringBuilder();
        List<DocPart> tmpPartList = new LinkedList();
        List<Integer> levelMatchList = new LinkedList();
        DocPart currentPart = new DocPart();
        boolean foundFirstIdentifier = false;
        StringBuilder contractNameComplete = new StringBuilder();
        boolean contractNameFilled = false;
        while (lineIdx < textList.size()) {
            String currentLine = ((String) textList.get(lineIdx)).trim();

            if (lineIdx < 4) {
                this.titleOfGenDoc += currentLine;
            }
            if (lineIdx < 2) {
                this.textOfGenDoc += currentLine;
            }

            if (lineIdx < 20 && !currentLine.startsWith("基金管理人：") && currentLine.endsWith("有限公司")) {
                ++lineIdx;
                continue;
            }

            if (lineIdx < 20 && !contractNameFilled) {
                contractNameComplete.append(currentLine);
                String nextLine = ((String) textList.get(lineIdx + 1)).trim();

                if (currentLine.endsWith("基金合同") || currentLine.endsWith("（草案）")) {
                    if (nextLine.endsWith("（草案）")) {
                        ++lineIdx;
                        continue;
                    }
                    String contractNameCompleteStr = contractNameComplete.toString();
                    String contractNameStr = contractNameCompleteStr.substring(0, contractNameComplete.indexOf("基金合同"));
                    fundDoc.setContractNameComplete(contractNameCompleteStr);
                    fundDoc.setContractName(contractNameStr);;
                    if (contractNameStr.contains("混合") || contractNameStr.contains("股票")) {
                        fundDoc.setType(FundDoc.CONTRACT_TYPE_STOCK);
                    } else if (contractNameStr.contains("指数")) {
                        fundDoc.setType(FundDoc.CONTRACT_TYPE_INDEX);
                    } else if (contractNameStr.contains("债务")) {
                        fundDoc.setType(FundDoc.CONTRACT_TYPE_DEBT);
                    } else if (contractNameStr.contains("货币")) {
                        fundDoc.setType(FundDoc.CONTRACT_TYPE_CURRENCY);
                    } else {
                        fundDoc.setType(FundDoc.CONTRACT_TYPE_UNKNOWN);
                    }
                    contractNameFilled = true;
                }
                ++lineIdx;
                continue;
            }

            if (lineIdx < 20 && currentLine.startsWith("基金管理人：")) {
                String establisherCompleteName = currentLine.substring(currentLine.indexOf("基金管理人："));
                fundDoc.setEstablisherComplete(establisherCompleteName);
                if (establisherCompleteName.contains("工银")) {
                    fundDoc.setEstablisher(FundDoc.CONTRACT_ESTABLISHER_GYRX);
                } else if (establisherCompleteName.contains("华夏")) {
                    fundDoc.setEstablisher(FundDoc.CONTRACT_ESTABLISHER_HXJJ);
                } else if (establisherCompleteName.contains("九泰")) {
                    fundDoc.setEstablisher(FundDoc.CONTRACT_ESTABLISHER_JTJJ);
                } else {
                    fundDoc.setEstablisher(FundDoc.CONTRACT_ESTABLISHER_UNKNOWN);
                }
                ++lineIdx;
                continue;
            }

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
                    String index = currentLine.substring(0, currentLine.indexOf(title)).trim();

                    currentPart.setIndex(index);
                    currentPart.setTitle(title);

                    List<Integer> idxList = new LinkedList();
                    if (!tmpPartList.isEmpty()) {
                        idxList.addAll(tmpPartList.get(tmpPartList.size() - 1).getPartIndex());
                    }
                    idxList.add(0);
                    currentPart.setPartIndex(idxList);

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
                        partIndex = tmpPartList.get(0).getPartIndex().get(0) + 1;
                        tmpPartList = new LinkedList();
                    }
                    currentPart = new DocPart();
                    String title = TextUtils.getPartTitle(currentLine);
                    String index = currentLine.substring(0, currentLine.indexOf(title)).trim();

                    currentPart.setIndex(index);
                    currentPart.setTitle(title);

                    List<Integer> idxList = new LinkedList();
                    if (actualLevel == 0) {
                        idxList.add(partIndex);
                    } else {
                        List childParts = tmpPartList.get(tmpPartList.size() - 1).getChildPart();
                        DocPart part = (DocPart) childParts.get(childParts.size() - 1);
                        idxList.addAll(part.getPartIndex());
                        partIndex = idxList.get(idxList.size() - 1) + 1;
                        idxList.set(idxList.size() - 1, partIndex);
                    }
                    currentPart.setPartIndex(idxList);

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
                    String index = currentLine.substring(0, currentLine.indexOf(title)).trim();

                    currentPart.setIndex(index);
                    currentPart.setTitle(title);

                    List<Integer> idxList = new LinkedList();
                    if (!tmpPartList.isEmpty()) {
                        idxList.addAll(tmpPartList.get(tmpPartList.size() - 1).getPartIndex());
                        partIndex = idxList.get(idxList.size() - 1) + 1;
                        idxList.set(idxList.size() - 1, partIndex);
                    } else {
                        idxList.add(++partIndex);
                    }

                    currentPart.setPartIndex(idxList);

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
        // sortPartIds
//        fundDoc = setPartIdsForEachNode(fundDoc);
        return fundDoc;
    }
}
