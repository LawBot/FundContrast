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
    private List<TextPoint> textPoints;

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


    public List<TextPoint> getTextPoints() {
        return textPoints;
    }

    public void setTextPoints(List<TextPoint> textPoints) {
        this.textPoints = textPoints;
    }

    public boolean hasTextPoint() {
        return !textPoints.isEmpty();
    }

    public boolean hasText() {
        return (text != null) && (!text.isEmpty());
    }

}
