/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.List;

/**
 *
 * @author 陈光曦
 */
public class Section {

    private String text;
    private List<Subsection> subsections;

    public Section() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }

    public List<Subsection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<Subsection> subsections) {
        this.subsections = subsections;
    }

    public boolean hasSubsection() {
        return !subsections.isEmpty();
    }

}
