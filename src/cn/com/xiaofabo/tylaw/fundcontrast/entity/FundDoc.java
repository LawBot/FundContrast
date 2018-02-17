/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.xiaofabo.tylaw.fundcontrast.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 陈光曦
 */
public class FundDoc {

    private String title;
    private String establisher;
    private String establisherComplete;
    private int type;
    private String contractName;
    private String contractNameComplete;
    private List<DocPart> parts;

    public static int CONTRACT_TYPE_UNKNOWN = -1;
    public static int CONTRACT_TYPE_STOCK = 0;
    public static int CONTRACT_TYPE_INDEX = 1;
    public static int CONTRACT_TYPE_BOND = 2;
    public static int CONTRACT_TYPE_CURRENCY = 3;

    public static String CONTRACT_ESTABLISHER_UNKNOWN = "UNKNOWN";
    public static String CONTRACT_ESTABLISHER_GYRX = "GYRX";
    public static String CONTRACT_ESTABLISHER_HXJJ = "HXJJ";
    public static String CONTRACT_ESTABLISHER_JTJJ = "JTJJ";

    public FundDoc(String title) {
        this.title = title;
        this.establisher = CONTRACT_ESTABLISHER_UNKNOWN;
        this.establisherComplete = "";
        this.type = CONTRACT_TYPE_UNKNOWN;
        this.contractName = "";
        this.contractNameComplete = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DocPart> getParts() {
        return parts;
    }

    public void setParts(List<DocPart> parts) {
        this.parts = parts;
    }

    public boolean addPart(DocPart part) {
        if (parts == null) {
            parts = new LinkedList();
        }
        return parts.add(part);
    }

    public String getEstablisher() {
        return establisher;
    }

    public void setEstablisher(String establisher) {
        this.establisher = establisher;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getEstablisherComplete() {
        return establisherComplete;
    }

    public void setEstablisherComplete(String establisherComplete) {
        this.establisherComplete = establisherComplete;
    }

    public String getContractNameComplete() {
        return contractNameComplete;
    }

    public void setContractNameComplete(String contractNameComplete) {
        this.contractNameComplete = contractNameComplete;
    }

//    @Override
    public List<CompareDto> getFundDoc() {
        List<CompareDto> compareDtoList = new ArrayList<CompareDto>();
        CompareDto compareDto = null;
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("Document Title: ").append(title).append("\n");
        int currentLevel = 0;
        for (int i = 0; i < parts.size(); ++i) {
            DocPart lvl1Part = (DocPart) parts.get(i);
            if (lvl1Part != null) {
                toReturn.append("Index: ").append(i).append("\n");
                toReturn.append("Title: ").append(lvl1Part.getTitle()).append("\n");
                toReturn.append("Text: ").append(lvl1Part.getText()).append("\n");

                compareDto = new CompareDto();
                compareDto.setChapterIndex(i + 1);
                compareDto.setText(lvl1Part.getTitle());
                if (lvl1Part.getIndex() != null && lvl1Part.getIndex().length() != 0) {
                    compareDto.setIndex(lvl1Part.getIndex());
                } else {
                    compareDto.setIndex("");
                }

                if (lvl1Part.getText() != null) {
                    compareDto.setText(lvl1Part.getPoint());
//                	compareDto= new CompareDto();
//                    compareDto.setChapterIndex(i+1);
//                    compareDto.setText(lvl1Part.getText());
//                    compareDto.setIndex("");
//                    compareDtoList.add(compareDto);
                }
                compareDtoList.add(compareDto);
                if (lvl1Part.hasPart()) {
                    for (int j = 0; j < lvl1Part.getChildPart().size(); ++j) {
                        DocPart lvl2Part = (DocPart) lvl1Part.getChildPart().get(j);
                        if (lvl2Part != null) {
                            toReturn.append("\tIndex: ").append(j).append("\n");
                            toReturn.append("\tTitle: ").append(lvl2Part.getTitle()).append("\n");
                            toReturn.append("\tText: ").append(lvl2Part.getText()).append("\n");

                            compareDto = new CompareDto();
                            compareDto.setChapterIndex(i + 1);
                            compareDto.setSectionIndex(j + 1);
                            compareDto.setText(lvl2Part.getTitle());
//                            System.out.println(lvl2Part.getIndex());
                            if (lvl2Part.getIndex() != null && lvl2Part.getIndex().length() != 0) {
                                compareDto.setIndex(lvl2Part.getIndex());
                            } else {
                                compareDto.setIndex("");
                            }

                            if (lvl2Part.getText() != null) {
                                compareDto.setText(lvl2Part.getPoint());
                            }
                            compareDtoList.add(compareDto);
                            if (lvl2Part.hasPart()) {
                                for (int k = 0; k < lvl2Part.getChildPart().size(); ++k) {
                                    DocPart lvl3Part = (DocPart) lvl2Part.getChildPart().get(k);
                                    if (lvl3Part != null) {
                                        toReturn.append("\t\tIndex: ").append(k).append("\n");
                                        toReturn.append("\t\tTitle: ").append(lvl3Part.getTitle()).append("\n");
                                        toReturn.append("\t\tText: ").append(lvl3Part.getText()).append("\n");

                                        compareDto = new CompareDto();
                                        compareDto.setChapterIndex(i + 1);
                                        compareDto.setSectionIndex(j + 1);
                                        compareDto.setSubSectionIndex(k + 1);
                                        compareDto.setText(lvl3Part.getTitle());
                                        if (lvl3Part.getIndex() != null && lvl3Part.getIndex().length() != 0) {
                                            compareDto.setIndex(lvl3Part.getIndex());
                                        } else {
                                            compareDto.setIndex("");
                                        }

                                        if (lvl3Part.getText() != null) {
                                            compareDto.setText(lvl3Part.getPoint());
//                                        	compareDto= new CompareDto();
//                                            compareDto.setChapterIndex(i+1);
//                                            compareDto.setSectionIndex(j+1);
//                                            compareDto.setSubSectionIndex(k+1);
//                                            compareDto.setText(lvl3Part.getText());
//                                            compareDto.setIndex("");
//                                            compareDtoList.add(compareDto);
                                        }
                                        compareDtoList.add(compareDto);
                                        if (lvl3Part.hasPart()) {
                                            for (int l = 0; l < lvl3Part.getChildPart().size(); ++l) {
                                                DocPart lvl4Part = (DocPart) lvl3Part.getChildPart().get(l);
                                                if (lvl4Part != null) {
                                                    toReturn.append("\t\t\tIndex: ").append(l).append("\n");
                                                    toReturn.append("\t\t\tTitle: ").append(lvl4Part.getTitle()).append("\n");
                                                    toReturn.append("\t\t\tText: ").append(lvl4Part.getText()).append("\n");

                                                    compareDto = new CompareDto();
                                                    compareDto.setChapterIndex(i + 1);
                                                    compareDto.setSectionIndex(j + 1);
                                                    compareDto.setSubSectionIndex(k + 1);
                                                    compareDto.setSubsubSectionIndex(l + 1);
                                                    compareDto.setText(lvl4Part.getTitle());
                                                    if (lvl4Part.getIndex() != null && lvl4Part.getIndex().length() != 0) {
                                                        compareDto.setIndex(lvl4Part.getIndex());
                                                    } else {
                                                        compareDto.setIndex("");
                                                    }

                                                    if (lvl4Part.getText() != null) {
                                                        compareDto.setText(lvl4Part.getPoint());
//                                                    	compareDto= new CompareDto();
//                                                        compareDto.setChapterIndex(i+1);
//                                                        compareDto.setSectionIndex(j+1);
//                                                        compareDto.setSubSectionIndex(k+1);
//                                                        compareDto.setSubsubSectionIndex(l+1);
//                                                        compareDto.setText(lvl4Part.getText());
//                                                        compareDto.setIndex("");
//                                                        compareDtoList.add(compareDto);
                                                    }
                                                    compareDtoList.add(compareDto);
                                                    if (lvl4Part.hasPart()) {
                                                        for (int m = 0; m < lvl4Part.getChildPart().size(); ++m) {
                                                            DocPart lvl5Part = (DocPart) lvl4Part.getChildPart().get(m);
                                                            if (lvl5Part != null) {
                                                                toReturn.append("\t\t\t\tIndex: ").append(m).append("\n");
                                                                toReturn.append("\t\t\t\tTitle: ").append(lvl5Part.getTitle()).append("\n");
                                                                toReturn.append("\t\t\t\tText: ").append(lvl5Part.getText()).append("\n");

                                                                compareDto = new CompareDto();
                                                                compareDto.setChapterIndex(i + 1);
                                                                compareDto.setSectionIndex(j + 1);
                                                                compareDto.setSubSectionIndex(k + 1);
                                                                compareDto.setSubsubSectionIndex(l + 1);
                                                                compareDto.setSubsubsubSectionIndex(m + 1);
                                                                compareDto.setText(lvl5Part.getTitle());
                                                                if (lvl5Part.getIndex() != null && lvl5Part.getIndex().length() != 0) {
                                                                    compareDto.setIndex(lvl5Part.getIndex());
                                                                } else {
                                                                    compareDto.setIndex("");
                                                                }
//                                                                compareDtoList.add(compareDto);
                                                                if (lvl5Part.getText() != null) {
                                                                    compareDto.setText(lvl5Part.getPoint());
//                                                                	compareDto= new CompareDto();
//                                                                    compareDto.setChapterIndex(i+1);
//                                                                    compareDto.setSectionIndex(j+1);
//                                                                    compareDto.setSubSectionIndex(k+1);
//                                                                    compareDto.setSubsubSectionIndex(l+1);
//                                                                    compareDto.setSubsubsubSectionIndex(m+1);
//                                                                    compareDto.setText(lvl5Part.getText());
//                                                                    compareDto.setIndex("");
//                                                                    compareDtoList.add(compareDto);
                                                                }
                                                                compareDtoList.add(compareDto);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        return toReturn.toString();
        return compareDtoList;
    }

}
