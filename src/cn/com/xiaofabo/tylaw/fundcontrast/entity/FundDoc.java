/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 陈光曦
 */
public class FundDoc {

    private String title;
    private List<Chapter> chapters;

    public FundDoc(String title) {
        this.title = title;
        chapters = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Document Title: ").append(title).append("\n");
        if (chapters != null) {
            for (int cIdx = 0; cIdx < chapters.size(); ++cIdx) {
                Chapter c = chapters.get(cIdx);
                if (c != null) {    /// Only tmp implementation
                    toReturn.append("====================================================").append("\n");
                    toReturn.append("Chapter Index: ").append(cIdx + 1).append("\n");
                    toReturn.append("Chapter Title: ").append(c.getTitle()).append("\n");
                    toReturn.append("Chapter Text: ").append(c.getText()).append("\n");

                    List sections = c.getSections();
                    if (sections != null) {
                        for (int sIdx = 0; sIdx < sections.size(); ++sIdx) {
                            Section s = (Section) sections.get(sIdx);
                            toReturn.append("\tSection Index: ").append(sIdx + 1).append("\n");
                            toReturn.append("\tSection Title: ").append(s.getTitle()).append("\n");
                            toReturn.append("\tSection Text: ").append(s.getText()).append("\n");

                            List subSections = s.getSubSections();
                            if (subSections != null) {
                                for (int ssIdx = 0; ssIdx < subSections.size(); ++ssIdx) {
                                    SubSection ss = (SubSection) subSections.get(ssIdx);
                                    toReturn.append("\t\tSub-Section Title: ").append(ss.getTitle()).append("\n");
                                    toReturn.append("\t\tSub-Section Text: ").append(ss.getText()).append("\n");

                                    List subSubSections = ss.getSubSubSections();
                                    if (subSubSections != null) {
                                        for (int sssIdx = 0; sssIdx < subSubSections.size(); ++sssIdx) {
                                            SubSubSection sss = (SubSubSection) subSubSections.get(sssIdx);
                                            toReturn.append("\t\t\tSub-Sub-Section Text: ").append(sss.getText()).append("\n");
                                        }
                                    }
                                }
                            }
                            toReturn.append("\t-----------------------------------------------------").append("\n");
                        }
                    }
                    toReturn.append("====================================================").append("\n");
                }
            }
        }
        return toReturn.toString();
    }

}
