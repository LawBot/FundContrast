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
public class Section {

    private String title;
    private String text;
    private List<SubSection> subSections;

    public Section() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubSection> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<SubSection> subSections) {
        this.subSections = subSections;
    }

    public boolean hasSubsection() {
        return !subSections.isEmpty();
    }

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }
}
