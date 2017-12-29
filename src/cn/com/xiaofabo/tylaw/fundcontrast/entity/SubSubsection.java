package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.List;

/**
 * Created @ 28.12.17
 * by 杨敏
 * email ddl-15 at outlook.com
 **/
public class SubSubsection {
    private String title;
    private String text;
    private List<Subsection> subSubSections;

    public SubSubsection() {
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
