package cn.com.xiaofabo.tylaw.fundcontrast.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.com.xiaofabo.tylaw.fundcontrast.entity.CompareDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.PatchDto;
import cn.com.xiaofabo.tylaw.fundcontrast.entity.RevisedDto;

public class CompareUtils2 {
	
	public static PatchDto doCompare(CompareDto orignalCompare, CompareDto revisedCompare) throws Exception{
		try {
			List<String> originalList = new ArrayList<String>();
		    List<String> revisedList = new ArrayList<String>();
			List<PatchDto> patchList = new ArrayList<PatchDto>();
			String orignalText = TextUtils.getPartTitle(orignalCompare.getText());
			String revisedText = TextUtils.getPartTitle(revisedCompare.getText());
			RevisedDto revisedDto = compare(orignalText, revisedText);
			PatchDto patchDto = new PatchDto();
			patchDto.setRevisedDto(revisedDto);
			patchDto.setChapterIndex(orignalCompare.getChapterIndex());
			patchDto.setOrignalText(orignalCompare.getText());
			patchDto.setSectionIndex(orignalCompare.getSectionIndex());
			patchDto.setSubSectionIndex(orignalCompare.getSubSectionIndex());
			patchDto.setSubsubSectionIndex(orignalCompare.getSubsubSectionIndex());
			patchDto.setSubsubsubSectionIndex(orignalCompare.getSubsubsubSectionIndex());
			patchDto.setIndexType("orginal");
			patchDto.setChangeType("change");
//			for (Delta<String> delta : patch.getDeltas()) {
//				Map<String, Object> compareMap = delta.toMap();
//				String originalStr = "";
//				String revisedStr = "";
//				@SuppressWarnings("unchecked")
//				ArrayList<String> originalArr = (ArrayList<String>)compareMap.get("original");
//				ArrayList<String> revisedArr = (ArrayList<String>)compareMap.get("revised");
//				
//				if (originalArr!=null) {
//					int i =0;
//					if (originalArr.size()==revisedArr.size()) {
//						for (String str : originalArr) {
//							CompareDto compareDto = orignalCompareDtoList.get(originalList.indexOf(str));
//							RevisedDto revisedDto = compare(str, revisedArr.get(i));
//							PatchDto patchDto = new PatchDto();
//							patchDto.setRevisedDto(revisedDto);
//							patchDto.setChapterIndex(compareDto.getChapterIndex());
//							patchDto.setOrignalText(str);
//							patchDto.setSectionIndex(compareDto.getSectionIndex());
//							patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//							patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//							patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//							patchDto.setIndexType("orginal");
//							patchList.add(patchDto);
//							i++;
//						}
//					}
//				}
//				if (originalArr!=null&&revisedArr!=null) {
//					if (originalArr.size()>revisedArr.size()) {
//						for (int i = 0; i < revisedArr.size(); i++) {
//							RevisedDto revisedDto = compare(originalArr.get(i), revisedArr.get(i));
//							PatchDto patchDto = new PatchDto();
//							patchDto.setRevisedDto(revisedDto);
//							CompareDto compareDto = orignalCompareDtoList.get(originalList.indexOf(originalArr.get(i)));
//							patchDto.setOrignalText(originalArr.get(i));
//							patchDto.setChapterIndex(compareDto.getChapterIndex());
//							patchDto.setSectionIndex(compareDto.getSectionIndex());
//							patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//							patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//							patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//							patchDto.setIndexType("orginal");
//							patchList.add(patchDto);
//						}
//						for (int i = revisedArr.size(); i < originalArr.size(); i++) {
//							RevisedDto revisedDto = new RevisedDto();
//							Map map = new HashMap<>();
//							for (int j = 0; j < originalArr.get(i).length(); j++) {
//								map.put(j, originalArr.get(i).charAt(j));
//							}
//							CompareDto compareDto = orignalCompareDtoList.get(originalList.indexOf(originalArr.get(i)));
//							revisedDto.setDeleteData(map);
//							PatchDto patchDto = new PatchDto();
//							patchDto.setOrignalText(originalArr.get(i));
//							patchDto.setChapterIndex(compareDto.getChapterIndex());
//							patchDto.setSectionIndex(compareDto.getSectionIndex());
//							patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//							patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//							patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//							patchDto.setIndexType("orginal");
//							patchList.add(patchDto);
//						}
//							
//					}
//					
//					if (originalArr.size()<revisedArr.size()) {
//						for (int i = 0; i < originalArr.size(); i++) {
//							RevisedDto revisedDto = compare(originalArr.get(i), revisedArr.get(i));
//							PatchDto patchDto = new PatchDto();
//							patchDto.setRevisedDto(revisedDto);
//							CompareDto compareDto = orignalCompareDtoList.get(originalList.indexOf(originalArr.get(i)));
//							patchDto.setOrignalText(originalArr.get(i));
//							patchDto.setChapterIndex(compareDto.getChapterIndex());
//							patchDto.setSectionIndex(compareDto.getSectionIndex());
//							patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//							patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//							patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//							patchDto.setIndexType("revised");
//							patchList.add(patchDto);
//						}
//						for (int i = originalArr.size(); i < revisedArr.size(); i++) {
//							RevisedDto revisedDto = new RevisedDto();
//							Map map = new HashMap<>();
//							for (int j = 0; j < revisedArr.get(i).length(); j++) {
//								map.put(j, revisedArr.get(i).charAt(j));
//							}
//							CompareDto compareDto = revisedCompareDtoList.get(revisedList.indexOf(revisedArr.get(i)));
//							revisedDto.setAddData(map);
//							revisedDto.setRevisedText(revisedArr.get(i));
//							PatchDto patchDto = new PatchDto();
//							patchDto.setChapterIndex(compareDto.getChapterIndex());
//							patchDto.setSectionIndex(compareDto.getSectionIndex());
//							patchDto.setSubSectionIndex(compareDto.getSubSectionIndex());
//							patchDto.setSubsubSectionIndex(compareDto.getSubsubSectionIndex());
//							patchDto.setSubsubsubSectionIndex(compareDto.getSubsubsubSectionIndex());
//							patchDto.setIndexType("revised");
//							patchList.add(patchDto);
//						}
//					}
//				}
//				
//	        }
			return patchDto;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
		
	}
	
	public static RevisedDto compare(String original, String revised){
		RevisedDto revisedDto = new RevisedDto();
		revisedDto.setRevisedText(revised);
		String[] a = revised.split("(?![-\\w])");
		List<String> list = new ArrayList<String>();
		for (String s : a) {
			if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
				list.add(s.trim());
			}
		}
		String[] b = original.split("(?![-\\w])");
		List<String> list1 = new ArrayList<String>();
		for (String s : b) {
			if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
				list1.add(s.trim());
			}
		}
		list1.removeAll(list);
		
		if (original.length()>=revised.length()) {
//			System.out.println("revised增加了："+compareStrshort(revised, original, "blue"));
//			System.out.println("revised减少了："+compareStrLong(revised, original, "blue"));
//			revisedDto.setAddData(compareStrshort(revised, original, "blue"));
//			revisedDto.setDeleteData(compareStrLong(revised, original, "blue"));
			//按Key进行排序
			Map<Integer, Character> addDataMap = sortMapByKey(compareStrshort(revised, original, "blue"));    
			Map<Integer, Character> deleteDataMap = sortMapByKey(compareStrLong(revised, original, "blue"));
			revisedDto.setAddData(addDataMap);
			revisedDto.setDeleteData(deleteDataMap);
			 
		}else if(original.length()<revised.length()){
//			System.out.println("revised增加了："+compareStrLong(revised, original, "blue"));
//			System.out.println("revised减少了："+compareStrshort(revised, original, "blue"));
//			revisedDto.setAddData(compareStrLong(revised, original, "blue"));
//			revisedDto.setDeleteData(compareStrshort(revised, original, "blue"));
			//按Key进行排序
			Map<Integer, Character> addDataMap = sortMapByKey(compareStrLong(revised, original, "blue"));    
			Map<Integer, Character> deleteDataMap = sortMapByKey(compareStrshort(revised, original, "blue"));
			revisedDto.setAddData(addDataMap);
			revisedDto.setDeleteData(deleteDataMap);
			
		}
		
		List<Integer> keyList = new ArrayList<Integer>();
		return revisedDto;
		
	}		

	public static Map compareStrLong(String char1, String char2, String colour) {
		String bcolor = "";
		String ecolor = "";
		StringBuffer sb = new StringBuffer();
		char[] a = new char[char1.length()];
		for (int i = 0; i < char1.length(); i++) {
			a[i] = char1.charAt(i);
		}
		char[] b = new char[char2.length()];
		for (int i = 0; i < char2.length(); i++) {
			b[i] = char2.charAt(i);
		}
		// 不同字符集合
		Map map1 = new HashMap();
		// 包含字符集合
		Map map2 = new HashMap();
		if (char1.length() > char2.length()) {
			for (int i = 0; i < a.length; i++) {
				if (i == a.length - 1) {
					if (i > 1) {
						if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
							map2.put(i - 1, a[i - 1]);
							map2.put(i, a[i]);
						}else{
							map1.put(i, a[i]);
						}
					} else {
						map2.put(i, a[i]);
					}
				} else {
					if (String.valueOf(b).contains(String.valueOf(a[i]) + String.valueOf(a[i + 1]))) {
						if (i > 1) {
							if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
								map2.put(i - 1, a[i - 1]);
								map2.put(i, a[i]);
							}
						} else {
							map2.put(i, a[i]);
						}
					} else {
						if (i > 0) {
							if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
								map2.put(i - 1, a[i - 1]);
								map2.put(i, a[i]);
							} else {
								map1.put(i, a[i]);
							}
						} else {
							map1.put(i, a[i]);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < b.length; i++) {
				if (i == b.length - 1) {
					if (i > 1) {
						if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
							map2.put(i - 1, b[i - 1]);
							map2.put(i, b[i]);
						}else{
							map1.put(i, b[i]);
						}
					} else {
						map2.put(i, b[i]);
					}
				} else {
					if (String.valueOf(a).contains(String.valueOf(b[i]) + String.valueOf(b[i + 1]))) {
						if (i > 1) {
							if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
								map2.put(i - 1, b[i - 1]);
								map2.put(i, b[i]);
							}
						} else {
							map2.put(i, b[i]);
						}
					} else {
						if (i > 0) {
							if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
								map2.put(i - 1, b[i - 1]);
								map2.put(i, b[i]);
							} else {
								map1.put(i, b[i]);
							}
						} else {
							map1.put(i, b[i]);
						}
					}
				}
			}
		}
		if (char1.length() > char2.length()) {
			for (int i = 0; i < a.length; i++) {
				if (map1.get(i) != null) {
					sb.append(bcolor).append(map1.get(i)).append(ecolor);
				} else if (map2.get(i) != null) {
					sb.append(map2.get(i));
				}
			}
		} else if (char1.length() <= char2.length()) {
			for (int i = 0; i < b.length; i++) {
				if (map1.get(i) != null) {
					sb.append(bcolor).append(map1.get(i)).append(ecolor);
				} else if (map2.get(i) != null) {
					sb.append(map2.get(i));
				}
			}
		}
//		System.out.println("map1:" + map1);
//		System.out.println("map2:" + map2);
//		return sb.toString();
		return map1;
	}

	public static Map compareStrshort(String char1, String char2, String colour) {
		String bcolor = "";
		String ecolor = "";
		StringBuffer sb = new StringBuffer();
		char[] a = new char[char1.length()];
		for (int i = 0; i < char1.length(); i++) {
			a[i] = char1.charAt(i);
		}
		char[] b = new char[char2.length()];
		for (int i = 0; i < char2.length(); i++) {
			b[i] = char2.charAt(i);
		}
		// 不同字符集合
		Map map1 = new HashMap();
		// 包含字符集合
		Map map2 = new HashMap();
		if (char1.length() > char2.length()) {
			for (int i = 0; i < b.length; i++) {
				if (i == b.length - 1) {
					if (i > 1) {
						if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
							map2.put(i - 1, b[i - 1]);
							map2.put(i, b[i]);
						}else{
							map1.put(i, b[i]);
						}
					} else {
						map2.put(i, b[i]);
					}
				} else {
					if (String.valueOf(a).contains(String.valueOf(b[i]) + String.valueOf(b[i + 1]))) {
						if (i > 1) {
							if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
								map2.put(i - 1, b[i - 1]);
								map2.put(i, b[i]);
							}
						} else {
							map2.put(i, b[i]);
						}
					} else {
						if (i > 0) {
							if (String.valueOf(a).contains(String.valueOf(b[i - 1]) + String.valueOf(b[i]))) {
								map2.put(i - 1, b[i - 1]);
								map2.put(i, b[i]);
							} else {
								map1.put(i, b[i]);
							}
						} else {
							map1.put(i, b[i]);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < a.length; i++) {
				if (i == a.length - 1) {
					if (i > 1) {
						if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
							map2.put(i - 1, a[i - 1]);
							map2.put(i, a[i]);
						}else{
							map1.put(i, a[i]);
						}
					} else {
						map2.put(i, a[i]);
					}
				} else {
					if (String.valueOf(b).contains(String.valueOf(a[i]) + String.valueOf(a[i + 1]))) {
						if (i > 1) {
							if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
								map2.put(i - 1, a[i - 1]);
								map2.put(i, a[i]);
							}else{
								map1.put(i, a[i]);
							}
						} else {
							map2.put(i, a[i]);
						}
					} else {
						if (i > 0) {
							if (String.valueOf(b).contains(String.valueOf(a[i - 1]) + String.valueOf(a[i]))) {
								map2.put(i - 1, a[i - 1]);
								map2.put(i, a[i]);
							} else {
								map1.put(i, a[i]);
							}
						} else {
							map1.put(i, a[i]);
						}
					}
				}
			}
		}

		if (char1.length() > char2.length()) {
			for (int i = 0; i < a.length; i++) {
				if (map1.get(i) != null) {
					sb.append(bcolor).append(map1.get(i)).append(ecolor);
				} else if (map2.get(i) != null) {
					sb.append(map2.get(i));
				}
			}
		} else if (char1.length() <= char2.length()) {
			for (int i = 0; i < b.length; i++) {
				if (map1.get(i) != null) {
					sb.append(bcolor).append(map1.get(i)).append(ecolor);
				} else if (map2.get(i) != null) {
					sb.append(map2.get(i));
				}
			}
		}
//		System.out.println(map1);
//		return sb.toString();
		return map1;
	}

	 /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
	 public static Map<Integer, Character> sortMapByKey(Map<Integer, Character> map) {
	        if (map == null || map.isEmpty()) {
	            return null;
	        }

	        Map<Integer, Character> sortMap = new TreeMap<Integer, Character>(new MapKeyComparator());

	        sortMap.putAll(map);

	        return sortMap;
	    }
    
// @Override            
// public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {         
//	 Double d5 = ((Double) rhs.get(OpenPrice));        
//	 Double d6 = (Double) lhs.get(OpenPrice);           
//	 if (d5 != null && d6 != null) {                     
//		 return d5.compareTo(d6);       
//		 } else {            
//			 return flag;        
//			 }                        // return d1.compareTo(d2);}
//	 }
// }
}
