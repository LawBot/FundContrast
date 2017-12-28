package cn.com.xiaofabo.tylaw.fundcontrast.entity;


public class SubSubsection {
    private String title;
    private String text;


    public SubSubsection(String text) {
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
