/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 陈光曦
 */
public class FundDoc {

    private String title;
    private List<DocPart> parts;

    public FundDoc(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DocPart> getParts() {
        return parts;
    }

    public void setParts(List<DocPart> parts) {
        this.parts = parts;
    }

    public boolean addPart(DocPart part) {
        if (parts == null) {
            parts = new LinkedList();
        }
        return parts.add(part);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Document Title: ").append(title).append("\n");
        int currentLevel = 0;
        for (int i = 0; i < parts.size(); ++i) {
            DocPart lvl1Part = (DocPart) parts.get(i);
            if (lvl1Part != null) {
                toReturn.append("Index: ").append(i).append("\n");
                toReturn.append("Title: ").append(lvl1Part.getTitle()).append("\n");
                toReturn.append("Text: ").append(lvl1Part.getText()).append("\n");
                if (lvl1Part.hasPart()) {
                    for (int j = 0; j < lvl1Part.getChildPart().size(); ++j) {
                        DocPart lvl2Part = (DocPart) lvl1Part.getChildPart().get(j);
                        if (lvl2Part != null) {
                            toReturn.append("\tIndex: ").append(j).append("\n");
                            toReturn.append("\tTitle: ").append(lvl2Part.getTitle()).append("\n");
                            toReturn.append("\tText: ").append(lvl2Part.getText()).append("\n");
                            if (lvl2Part.hasPart()) {
                                for (int k = 0; k < lvl2Part.getChildPart().size(); ++k) {
                                    DocPart lvl3Part = (DocPart) lvl2Part.getChildPart().get(k);
                                    if (lvl3Part != null) {
                                        toReturn.append("\t\tIndex: ").append(k).append("\n");
                                        toReturn.append("\t\tTitle: ").append(lvl3Part.getTitle()).append("\n");
                                        toReturn.append("\t\tText: ").append(lvl3Part.getText()).append("\n");
                                        if (lvl3Part.hasPart()) {
                                            for (int l = 0; l < lvl3Part.getChildPart().size(); ++l) {
                                                DocPart lvl4Part = (DocPart) lvl3Part.getChildPart().get(l);
                                                if (lvl4Part != null) {
                                                    toReturn.append("\t\t\tIndex: ").append(l).append("\n");
                                                    toReturn.append("\t\t\tTitle: ").append(lvl4Part.getTitle()).append("\n");
                                                    toReturn.append("\t\t\tText: ").append(lvl4Part.getText()).append("\n");
                                                    if (lvl4Part.hasPart()) {
                                                        for (int m = 0; m < lvl4Part.getChildPart().size(); ++m) {
                                                            DocPart lvl5Part = (DocPart) lvl4Part.getChildPart().get(m);
                                                            if (lvl5Part != null) {
                                                                toReturn.append("\t\t\t\tIndex: ").append(m).append("\n");
                                                                toReturn.append("\t\t\t\tTitle: ").append(lvl5Part.getTitle()).append("\n");
                                                                toReturn.append("\t\t\t\tText: ").append(lvl5Part.getText()).append("\n");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return toReturn.toString();
    }

}
