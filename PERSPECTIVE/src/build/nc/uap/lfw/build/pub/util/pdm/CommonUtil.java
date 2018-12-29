package nc.uap.lfw.build.pub.util.pdm;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 通用工具类
 * 
 * 可访问方法: convertVectorToArray, isRedundentItem, spaceToNull, gb2Unicode,
 * unicode2Gb, getStr, removeStr, replaceAllString, replaceFromTo,
 * replaceQuotMark
 * 
 * @author fanp
 */
public class CommonUtil {
	public static String[] getValueIndexListBySpecifiedValue(String[] value_list, String value) {
		if (value_list == null || value_list.length == 0)
			return null;

		Vector v = new Vector();
		for (int i = 0; i < value_list.length; i++) {
			if (value.equalsIgnoreCase(value_list[i]))
				v.addElement((Integer.valueOf(i)).toString());
		}

		if (v.size() == 0)
			return null;

		String[] result = new String[v.size()];
		v.copyInto(result);
		return result;
	}

	/**
	 * 该方法返回指定字符串中的不重复子集
	 * 
	 * @param value_list
	 * @return
	 */
	public static String[] getUniqueValueList(String[] value_list) {
		if (value_list == null || value_list.length == 0)
			return null;

		Hashtable ht = new Hashtable();
		for (int i = 0; i < value_list.length; i++) {
			String value = value_list[i];
			if (!ht.containsKey(value))
				ht.put(value, i);
		}// for

		Vector v = new Vector();
		Enumeration e = ht.keys();
		while (e.hasMoreElements()) {
			v.addElement(e.nextElement());
		}

		if (v.size() == 0)
			return null;

		String[] result = new String[v.size()];
		v.copyInto(result);
		return result;
	}

	/**
	 * 该方法将指定Vector集合转换为Object数组, 且确保同一元素在Vector集合与Object数组中顺序一致
	 * 
	 * @param v
	 * @return
	 */
	public static String[] convertVectorToArray(Vector v) {
		if (v == null || v.size() == 0)
			return null;

		String[] strArray = new String[v.size()];

		// v.copyInto(objArray); 该方法未必保证顺序一致, 故不采用
		for (int i = 0; i < v.size(); i++) {
			strArray[i] = (String) v.elementAt(i);
		}// for

		return strArray;
	}

	/**
	 * 该方法过滤掉指定集合中的重复项
	 * 
	 * @param al
	 * @param s
	 * @return
	 */
	public static boolean isRedundentItem(ArrayList al, String s) {
		Object[] items = al.toArray();

		if (items == null || items.length == 0)
			return false;

		for (int i = 0; i < items.length; i++) {
			String item = (String) items[i];
			if (item.equals(s))
				return true;
		}// for
		return false;
	}

	/**
	 * 该方法将空格或空字符串转化为NULL
	 * 
	 * @param str
	 * @return
	 */
	public static String spaceToNull(String str) {
		if (str == null)
			return null;
		/*
		 * str = str.trim(); String strNull = str; if(str.equals("") ||
		 * str.length() == 0) strNull = null; return strNull;
		 */
		String s = str.trim();
		if (s.equals("") || s.length() == 0)
			return null;
		else
			return s;
	}

	/**
	 * 该方法将GB编码转换为UNICODE 创建日期:(2001-8-11 15:46:09)
	 * 
	 * @return java.lang.String[]
	 * @param src
	 *            java.lang.String[]
	 */
	public static String[] gb2Unicode(String[] srcAry) {
		String[] strOut = new String[srcAry.length];
		for (int i = 0; i < srcAry.length; i++) {
			strOut[i] = gb2Unicode(srcAry[i]);
		}
		return strOut;
	}

	public static String gb2Unicode(String src) {
		char[] c = src.toCharArray();
		int n = c.length;
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) c[i];
		return new String(b);
	}

	/**
	 * 该方法将UNICODE编码转换为GB编码
	 * 
	 * @return java.lang.String[]
	 * @param src
	 *            java.lang.String[]
	 */
	public static String[] unicode2Gb(String[] srcAry) {
		String[] strOut = new String[srcAry.length];
		for (int i = 0; i < srcAry.length; i++) {
			strOut[i] = uniCode2Gb(srcAry[i]);
		}
		return strOut;
	}

	public static String uniCode2Gb(String src) {
		byte[] b = src.getBytes();
		int n = b.length;
		char[] c = new char[n];
		for (int i = 0; i < n; i++)
			c[i] = (char) ((short) b[i] & 0xff);
		return new String(c);
	}

	public static String getStr(String source, String strBegin, String strEnd) {
		int index = source.indexOf(strBegin);
		int index1 = source.indexOf(strEnd);
		return source.substring(index + strBegin.length(), index1);
	}

	public static String removeStr(String source, String strBegin, String strEnd) {
		int index = source.indexOf(strBegin);
		int index1 = source.indexOf(strEnd);
		return source.substring(0, index) + source.substring(index1 + strEnd.length());
	}

	/**
	 * 该方法将指定字符串中包含的原有字符串(strBeReplace)替换为新字符串(strReplaced)
	 * 
	 * @param source
	 * @param strBeReplace
	 * @param strReplaced
	 * @return
	 */
	public static String replaceAllString(String source, String strBeReplace, String strReplaced) {
		if (strReplaced == null)
			return null;

		int loc = 0;
		int indexLoc = source.indexOf(strBeReplace);
		while (indexLoc >= 0) {
			source = source.substring(0, indexLoc) + strReplaced + source.substring(indexLoc + strBeReplace.length());
			loc = indexLoc + strReplaced.length();
			indexLoc = source.indexOf(strBeReplace, loc);
		}
		return source;
	}

	/**
	 * 该方法将指定字符串中包含的原有字符串(strBeReplace)替换为新字符串(strReplaced)
	 * 
	 * @param source
	 * @param strBeReplace
	 * @param strReplaced
	 * @return
	 */
	public static String replaceFromTo(String source, String strBegin, String strEnd, String replaced) {
		int index = source.indexOf(strBegin);
		int index1 = source.indexOf(strEnd);
		return source.substring(0, index) + replaced + source.substring(index1 + strEnd.length());
	}

	public static String replace(String source, String strBeReplace, String strReplaced) {
		if (source == null || source.length() == 0)
			return null;

		if (strReplaced == null)
			return null;

		String s = source;
		return s.replaceAll(strBeReplace, strReplaced);
	}

	/**
	 * 该方法将指定字符串中包含的引用符号("'")替换为"''"
	 * 
	 * @return java.lang.String
	 * @param strValue
	 *            java.lang.String
	 */
	public static String replaceQuotMark(String strValue) {

		String oldMark = "'";
		String strResult = strValue;

		if (strValue != null && strValue.length() > 0 && strValue.indexOf(oldMark) >= 0) {
			boolean hasOneQuoMard = true;
			int pos = 0;
			while (hasOneQuoMard) {
				pos = strResult.indexOf(oldMark, pos);
				if (pos < 0)
					break;
				if (pos >= strResult.length() - 1) {
					strResult = strResult.substring(0, strResult.length()) + oldMark;
					hasOneQuoMard = false;
				} else {
					if (strResult.substring(pos + 1, pos + 2).equals(oldMark)) {
						pos += 2;
					} else {
						strResult = strResult.substring(0, pos + 1) + oldMark + strResult.substring(pos + 1);
						pos += 2;
					}
				}
			}
		}
		return strResult;
	}
}
