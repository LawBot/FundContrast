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
public class DocPart {

    String title;
    String text;
    List<DocPart> childPart;

    public DocPart() {
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

    public boolean addPart(DocPart part) {
        if (childPart == null) {
            childPart = new LinkedList();
        }
        return childPart.add(part);
    }

    public List<DocPart> getChildPart() {
        return childPart;
    }

    public void setChildPart(List<DocPart> childPart) {
        this.childPart = childPart;
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Title: ").append(title).append("\n");
        toReturn.append("Text: ").append(text).append("\n");
        if (childPart != null) {
            childPart.forEach((part) -> {
                toReturn.append(part.toString());
            });
        }
        return toReturn.toString();
    }
}
