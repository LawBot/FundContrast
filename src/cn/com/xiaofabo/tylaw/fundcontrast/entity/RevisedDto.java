package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.Map;

public class RevisedDto {
	private String revisedText;
	private Map<Integer, String> addData;
	private Map<Integer, String> deleteData;
	
	public String getRevisedText() {
		return revisedText;
	}
	public void setRevisedText(String revisedText) {
		this.revisedText = revisedText;
	}
	public Map<Integer, String> getAddData() {
		return addData;
	}
	public void setAddData(Map<Integer, String> addData) {
		this.addData = addData;
	}
	public Map<Integer, String> getDeleteData() {
		return deleteData;
	}
	public void setDeleteData(Map<Integer, String> deleteData) {
		this.deleteData = deleteData;
	}
	
}
