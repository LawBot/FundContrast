/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.List;

/**
 * @author 陈光曦
 */
public class SubSection {

    private String text;
    private List<SubSubSection> subSubSections;

    public SubSection() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<SubSubSection> getSubSubSections() {
        return subSubSections;
    }

    public void setSubSubSections(List<SubSubSection> subSubSections) {
        this.subSubSections = subSubSections;
    }

    public boolean hasSubSubsection() {
        return !subSubSections.isEmpty();
    }

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }

}
