package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.List;

public class PatchDto implements Comparable<PatchDto>{

    private String orignalText;
    private int chapterIndex;
    private int sectionIndex;
    private int subSectionIndex;
    private int subsubSectionIndex;
    private int subsubsubSectionIndex;
    private RevisedDto revisedDto;
    private String indexType;//(代表原文的序号则为“orginal”,代表新条文的序号则为"revised"，当新增加原文没有的条文，则类型为"revised")
    private String changeType;//(add:新增 delete:删减 change:更改)
    private String partId;
    private List<Integer> partIndex;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getOrignalText() {
        return orignalText;
    }

    public void setOrignalText(String orignalText) {
        this.orignalText = orignalText;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public int getSectionIndex() {
        return sectionIndex;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public int getSubSectionIndex() {
        return subSectionIndex;
    }

    public void setSubSectionIndex(int subSectionIndex) {
        this.subSectionIndex = subSectionIndex;
    }

    public int getSubsubSectionIndex() {
        return subsubSectionIndex;
    }

    public void setSubsubSectionIndex(int subsubSectionIndex) {
        this.subsubSectionIndex = subsubSectionIndex;
    }

    public int getSubsubsubSectionIndex() {
        return subsubsubSectionIndex;
    }

    public void setSubsubsubSectionIndex(int subsubsubSectionIndex) {
        this.subsubsubSectionIndex = subsubsubSectionIndex;
    }

    public RevisedDto getRevisedDto() {
        return revisedDto;
    }

    public void setRevisedDto(RevisedDto revisedDto) {
        this.revisedDto = revisedDto;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public List<Integer> getPartIndex() {
        return partIndex;
    }

    public void setPartIndex(List<Integer> partIndex) {
        this.partIndex = partIndex;
    }

    @Override
    public int compareTo(PatchDto pdt) {
        for (int i = 0; i < partIndex.size(); ++i) {
            if (i > pdt.getPartIndex().size() - 1) {
                return 1;
            }
            int thisIndex = partIndex.get(i);
            int compareIndex = pdt.getPartIndex().get(i);
            int diff = thisIndex - compareIndex;
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

}
