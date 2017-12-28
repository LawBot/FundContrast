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
public class Subsection {

    private String title;
    private String text;
    private List<Subsection> subSubSections;

    public Subsection() {
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

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }

    public List<Subsection> getSubSubSections() {
        return subSubSections;
    }

    public void setSubSubSections(List<Subsection> subSubSections) {
        this.subSubSections = subSubSections;
    }

    public boolean hasSubsection() {
        return !subSubSections.isEmpty();
    }

}
