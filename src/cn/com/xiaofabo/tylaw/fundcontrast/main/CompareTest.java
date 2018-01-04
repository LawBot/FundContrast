package com.mindpin.rsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Delta;
import com.github.difflib.patch.Patch;


public class CompareTest {

	public static void main(String[] args) {
		List<String> original = Arrays.asList("解放军","测试仪","测试而基德");
    	List<String> patched = Arrays.asList("解放军","测试仪","测试而t");
		try {
			Patch<String> patch = DiffUtils.diff(original,patched);
			for (Delta<String> delta : patch.getDeltas()) {
				Map<String, Object> compareMap = delta.toMap();
				
//				String.Join(",", (String[])(ArrayList)compareMap.get("original").ToArray(typeof( String)));
				String originalStr = "";
				String revisedStr = "";
				@SuppressWarnings("unchecked")
				ArrayList<String> originalArr = (ArrayList<String>)compareMap.get("original");
				for (String str : originalArr) {
					originalStr +=str;
				}
				@SuppressWarnings("unchecked")
				ArrayList<String> revisedArr = (ArrayList<String>)compareMap.get("revised");
				for (String str : revisedArr) {
					revisedStr +=str;
				}
				System.out.println("original:"+originalStr+",revised:"+revisedStr);
				compare(originalStr, revisedStr);
	            
	        }
		} catch (DiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		String str1 = "基金管理人、基金托管人在本基金合同之外披露涉及本基金的信息qq，其内容涉及界定基金合同当事人之间权利义务关系的，如与基金合同有冲突，以基金合同为准。";
//		String str = "基金托管人在本基金合同之外披露涉及本基金额的信息，其内容涉及界定基金合同当事人之间权利义务关系的，如与基金合同rty有冲突，以基金合同为准。";
	}
		public static void compare(String original, String revised){
			String[] a = revised.split("(?![-\\w])");
			List<String> list = new ArrayList<String>();
			for (String s : a) {
				if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
					list.add(s.trim());
				}
			}
			// System.out.println(list);

			String[] b = original.split("(?![-\\w])");
			List<String> list1 = new ArrayList<String>();
			for (String s : b) {
				if (!s.equals("") && !s.equals(".") && !s.equals(",")) {
					list1.add(s.trim());
				}
			}
			list1.removeAll(list);
			// System.out.println(list1);
			if (original.length()>revised.length()) {
				System.out.println("revised增加了："+compareStrshort(revised, original, "blue"));
				System.out.println("revised减少了："+compareStrLong(revised, original, "blue"));
			}else if(original.length()<original.length()){
				System.out.println("revised增加了："+compareStrLong(revised, original, "blue"));
				System.out.println("revised减少了："+compareStrshort(revised, original, "blue"));
			}
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
//			System.out.println("map1:" + map1);
//			System.out.println("map2:" + map2);
//			return sb.toString();
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
//			System.out.println(map1);
//			return sb.toString();
			return map1;
		}
}
