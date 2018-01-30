package cn.com.xiaofabo.tylaw.fundcontrast.entity;

public class MatchDto {

	private int orignalIndex;
	private int revisedIndex;
	private double bestRatio;
	
	
	public MatchDto(int orignalIndex, int revisedIndex, double bestRatio) {
		super();
		this.orignalIndex = orignalIndex;
		this.revisedIndex = revisedIndex;
		this.bestRatio = bestRatio;
	}
	public int getOrignalIndex() {
		return orignalIndex;
	}
	public void setOrignalIndex(int orignalIndex) {
		this.orignalIndex = orignalIndex;
	}
	public int getRevisedIndex() {
		return revisedIndex;
	}
	public void setRevisedIndex(int revisedIndex) {
		this.revisedIndex = revisedIndex;
	}
	public double getBestRatio() {
		return bestRatio;
	}
	public void setBestRatio(double bestRatio) {
		this.bestRatio = bestRatio;
	}
	
	
}
