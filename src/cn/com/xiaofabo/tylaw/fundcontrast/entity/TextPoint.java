package cn.com.xiaofabo.tylaw.fundcontrast.entity;

/**
 * Created @ 29.12.17
 * by 杨敏
 * email ddl-15 at outlook.com
 **/
public class TextPoint {
    private String title;
    private String text;

    public TextPoint(String text) {
        this.text = text;
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

}
