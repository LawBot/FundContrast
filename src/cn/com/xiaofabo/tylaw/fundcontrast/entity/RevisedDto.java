package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.HashMap;
import java.util.Map;

public class RevisedDto {

    private String revisedText;
    private Map<Integer, Character> addData;
    private Map<Integer, Character> deleteData;

    public RevisedDto() {
        addData = new HashMap<>();
        deleteData = new HashMap<>();
    }

    public String getRevisedText() {
        return revisedText;
    }

    public void setRevisedText(String revisedText) {
        this.revisedText = revisedText;
    }

    public Map<Integer, Character> getAddData() {
        return addData;
    }

    public Character addData(Integer k, Character v) {
        return addData.put(k, v);
    }

    public void setAddData(Map<Integer, Character> addData) {
        this.addData = addData;
    }

    public Map<Integer, Character> getDeleteData() {
        return deleteData;
    }

    public Character deleteData(Integer k, Character v) {
        return deleteData.put(k, v);
    }

    public void setDeleteData(Map<Integer, Character> deleteData) {
        this.deleteData = deleteData;
    }

}
