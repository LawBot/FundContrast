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
public class Chapter {

    private String title;
    private String text;
    private List<Section> sections;


    public Chapter(String title) {
        this.title = title;
        this.text = "";
        this.sections = new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sectionList) {
        this.sections = sectionList;
    }

    public boolean hasSection() {
        return !sections.isEmpty();
    }

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }

}
