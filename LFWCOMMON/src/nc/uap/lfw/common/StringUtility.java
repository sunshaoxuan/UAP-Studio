/* 
 * @(#)StringUtil.java 1.0 2004-10-11
 *
 * Copyright 2005 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.uap.lfw.common;

import java.awt.FontMetrics;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类，提供一些字符串相关的操作。
 * 
 */
public class StringUtility {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    /** 编码与名称之间的连字符。 目前为&quot;&nbsp;&quot; */
    public static final String HYPHEN = " ";

    /**
     * 从原字符串中截取从开始字符串到结束字符串之间的字符串。结果不包含开始字符串与开始字符串。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * source = &quot;this is a red car&quot;;
     * strBegin = &quot;hi&quot;;
     * setEnd = &quot;ar&quot;;
     * result = &quot;s is a red c&quot;;
     * </pre>
     * 
     * @param source
     *            源字符串
     * @param strBegin
     *            开始字符串
     * @param strEnd
     *            结束字符串
     * @return 字符串
     */
    public static String substringBetween(String source, String strBegin, String strEnd) {
        if (null == source)
            return null;
        int index = source.indexOf(strBegin);
        int indexEnd = source.indexOf(strEnd);
        if (index < 0)
            index = 0 - strBegin.length();
        if (indexEnd < 0)
            indexEnd = source.length();
        return source.substring(index + strBegin.length(), indexEnd);
    }

    /**
     * 从原字符串中去掉从开始字符串到结束字符串之间的字符串。结果不包含开始字符串与开始字符串。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * source = &quot;this is a red car&quot;;
     * strBegin = &quot;hi&quot;;
     * setEnd = &quot;ar&quot;;
     * result = &quot;t&quot;;
     * </pre>
     * 
     * @param source
     *            源字符串
     * @param strBegin
     *            开始字符串
     * @param strEnd
     *            结束字符串
     * @return 替换之后的字符串
     */
    public static String removeStringBetween(String source, String strBegin, String strEnd) {
        int index = source.indexOf(strBegin);
        int indexEnd = source.indexOf(strEnd);
        return source.substring(0, index) + source.substring(indexEnd + strEnd.length());
    }

    /**
     * 用strReplaced替换字符串source中所有的strReplace。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * StringUtil.replaceAll(null, &quot;*&quot;, &quot;*&quot;) = null;
     * StringUtil.replaceAll(&quot;&quot;, &quot;*&quot;, &quot;*&quot;) = &quot;&quot;;
     * StringUtil.replaceAll(&quot;any&quot;, null, &quot;*&quot;) = &quot;any&quot;;
     * StringUtil.replaceAll(&quot;any&quot;, &quot;*&quot;, null) = &quot;any&quot;;
     * StringUtil.replaceAll(&quot;any&quot;, &quot;&quot;, &quot;*&quot;) = &quot;any&quot;;
     * StringUtil.replaceAll(&quot;abaa&quot;, &quot;a&quot;, null) = &quot;abaa&quot;;
     * StringUtil.replaceAll(&quot;abaa&quot;, &quot;a&quot;, &quot;&quot;) = &quot;b&quot;;
     * StringUtil.replaceAll(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;) = &quot;zbzz&quot;;
     * </pre>
     * 
     * @param source
     *            源字符串，可为<tt>null</tt>
     * @param strReplaced
     *            需要被替换的字符串，可为<tt>null</tt>
     * @param strReplace
     *            替换的字符串，可为<tt>null</tt>
     * @return 所有替换处理后的字符串，如果源字符串为<tt>null</tt>，则返回也为<tt>null</tt>。
     * @see #replaceIgnoreCase(String, String, String)
     * 
     */
    public static String replaceAllString(String source, String strReplaced, String strReplace) {
        if (isEmpty(source) || isEmpty(strReplaced) || strReplace == null)
            return source;

        StringBuffer buf = new StringBuffer(source.length());
        int start = 0, end = 0;
        while ((end = source.indexOf(strReplaced, start)) != -1) {
            buf.append(source.substring(start, end)).append(strReplace);
            start = end + strReplaced.length();
        }
        buf.append(source.substring(start));
        return buf.toString();
    }

    /**
     * 忽略大小写，用strReplaced替换字符串source中所有的strBeReplace。
     * 
     * @param source
     *            源字符串，可为<tt>null</tt>
     * @param strReplace
     *            需要被替换的字符串，可为<tt>null</tt>
     * @param strReplaced
     *            替换的字符串，可为<tt>null</tt>
     * @return 所有替换处理后的字符串，如果源字符串为<tt>null</tt>，则返回也为<code>null</code>。
     * @see #replaceAllString(String, String, String)
     */
    public static String replaceIgnoreCase(String source, String strBeReplace, String strReplaced) {
        if (isEmpty(source) || isEmpty(strBeReplace) || strReplaced == null)
            return source;

        StringBuffer buf = new StringBuffer(source.length());
        int start = 0, end = 0;
        String strReplacedCopy = strBeReplace.toUpperCase();
        String sourceCopy = source.toUpperCase();
        while ((end = sourceCopy.indexOf(strReplacedCopy, start)) != -1) {
            buf.append(source.substring(start, end)).append(strReplaced);
            start = end + strReplacedCopy.length();
        }
        buf.append(source.substring(start));
        return buf.toString();
    }

    /**
     * 将原字符串中从字符串strBegin开始到strEnd之间的字符串替换为replaced字符串。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * source = &quot;this is a red car&quot;;
     * strBegin = &quot;is&quot;;
     * setEnd = &quot;ed&quot;;
     * repalced = &quot;abc&quot;;
     * result = &quot;thabc car&quot;;
     * </pre>
     * 
     * @param source
     *            源字符串，可为<tt>null</tt>
     * @param strBegin
     *            定位开始的字符串，不可为<tt>null</tt>
     * @param strEnd
     *            定位结束的字符串，不可为<tt>null</tt>
     * @param replaced
     *            替换的字符串，不可为<tt>null</tt>
     * 
     * @return 所有替换处理后的字符串，如果源字符串为<tt>null</tt>，则返回也为<code>null</code>。
     * 
     */
    public static String replaceFromTo(String source, String strBegin, String strEnd, String replaced) {
        if (null == source)
            return null;
        int index = source.indexOf(strBegin);
        int index1 = source.indexOf(strEnd);
        return source.substring(0, index) + replaced + source.substring(index1 + strEnd.length());
    }

    /**
     * 将gb编码的字符串数组转换为unicode编码的字符串数组。
     * 
     * @param srcAry
     *            gb编码的字符串数组
     * @return unicode编码的字符串数组
     */
    public static String[] gb2Unicode(String[] srcAry) {
        String[] strOut = new String[srcAry.length];
        for (int i = 0; i < srcAry.length; i++) {
            strOut[i] = gb2Unicode(srcAry[i]);
        }
        return strOut;
    }

    /**
     * 将gb编码的字符串转换为unicode编码的字符串。
     * 
     * @param src
     *            gb编码的字符串
     * @return unicode编码的字符串
     */
    public static String gb2Unicode(String src) {
        src = spaceToNull(src);
        if (src == null) {
            return null;
        }
        char[] c = src.toCharArray();
        int n = c.length;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++)
            b[i] = (byte) c[i];
        return new String(b);
    }

    /**
     * 将unicode编码的字符串数组转换为gb编码的字符串数组。
     * 
     * @param srcAry
     *            以unicode编码的字符串数组
     * @return gb编码的字符串数组。
     */
    public static String[] unicode2Gb(String[] srcAry) {
        String[] strOut = new String[srcAry.length];
        for (int i = 0; i < srcAry.length; i++) {
            strOut[i] = uniCode2Gb(srcAry[i]);
        }
        return strOut;
    }

    /**
     * 将unicode编码转换为gb。
     * 
     * @param src
     *            以unicode编码的字符串
     * @return gb编码的字符串。
     */
    public static String uniCode2Gb(String src) {
        src = spaceToNull(src);
        if (src == null) {
            return null;
        }
        byte[] b = src.getBytes();
        int n = b.length;
        char[] c = new char[n];
        for (int i = 0; i < n; i++)
            c[i] = (char) ((short) b[i] & 0xff);
        return new String(c);
    }

    /**
     * 根据reg给定的规则（通配符"*"和单字匹配符"?"），判断str是否符合此规则，符合的话返回true。 简单的正则表式的实现。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     *                      match(&quot;a*&quot;,&quot;abc&quot;) = true;
     *                      match(&quot;a*&quot;,&quot;abcd&quot;) = true;
     *                      match(&quot;a*&quot;,&quot;ba&quot;)  = false;
     *                      match(&quot;a?&quot;,&quot;ab&quot;) = true;
     *                      match(&quot;a?&quot;,&quot;abc&quot;) = false;
     *                      
     *                      其中&quot;*&quot;为通配符，而&quot;?&quot;表示匹配一个字符，要求在指定的位置上必须有字符。
     * </pre>
     * 
     * @param reg
     *            包含匹配符的字符串
     * @param str
     *            源字符串
     * @return 如果源字符串符合匹配规则，则返回<tt>true</tt>。
     * @see #matchIgnoreCase(String, String)
     */
//    public static boolean match(String reg, String str) {
//        return WildcardMatcher.match(reg, str);
//    }

    /**
     * 根据reg给定的规则（通配符"*"和单字匹配符"?"），判断str是否符合此规则，符合的话返回true。简单的正则表式的实现。比较时忽略大小写。
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     *                match(&quot;A*&quot;,&quot;abc&quot;)= true;
     *                match(&quot;a*&quot;,&quot;abcD&quot;) = true;
     *                match(&quot;a*&quot;,&quot;ba&quot;) = true;
     *                match(&quot;a?&quot;,&quot;ab&quot;) = true;
     *                match(&quot;a?&quot;,&quot;abc&quot;) = true;
     *                      
     *                其中&quot;*&quot;为通配符，而&quot;?&quot;表示匹配一个字符，要求在指定的位置上必须有字符。
     * </pre> *
     * 
     * @param reg
     *            包含匹配符的字符串
     * @param str
     *            源字符串
     * @return 如果源字符串符合匹配规则，则返回<tt>true</tt>。
     * @see #match(String, String)
     */
//    public static boolean matchIgnoreCase(String reg, String str) {
//        return WildcardMatcher.match(reg, str);
//    }

    /**
     * 用特定的间隔符对输入的字符串进行拆分，返回的串放在List里。
     * 
     * @param inputstring
     *            以指字间隔符拼成的字符串。
     * @param splitstr
     *            分隔符
     * @return 拆分后的字符串列表
     */
    public static List<String> toList(String inputstring, String splitstr) {
        StringTokenizer st = new StringTokenizer(inputstring, splitstr, false);
        List<String> reslist = new ArrayList<String>(st.countTokens());
        while (st.hasMoreTokens()) {
            reslist.add(st.nextToken().trim());
        }
        return reslist;
    }

    /**
     * 将字符串数组的元素以指定连接符unionChar拼接成字符串，每个数组元素前后都将添加appendChar。
     * 
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * getUnionStr(new String[] { &quot;A&quot;, &quot;B&quot;, &quot;C&quot; }, &quot;,&quot;, &quot;&quot;) = &quot;A,B,C&quot;;
     * getUnionStr(new String[] { &quot;A&quot;, &quot;B&quot;, &quot;C&quot; }, &quot;,&quot;, &quot;*&quot;) = &quot;*A*,*B*,*C*&quot;;
     * </pre>
     * 
     * @params strAry 用于连接的字符串数组
     * @params unionChar 用来连接的中间字符
     * @params appendChar 用来在每个字符中追加
     */
    public static String getUnionStr(String[] strAry, String unionChar, String appendChar) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i != 0)
                ret.append(unionChar);
            ret.append(appendChar + strAry[i] + appendChar);
        }
        return ret.toString();
    }

    /**
     * 获取多个汉字的拼音首字符组成的字符串。
     * 
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * getPYIndexStr(&quot;程&quot;, true) = &quot;C&quot;;
     * getPYIndexStr(&quot;程序&quot;, ture) = &quot;CX&quot;;
     * </pre>
     * 
     * @param strChinese
     *            要处理的汉字字符串
     * @param bUpCase
     *            决定返回的首拼字符是否大写，true为大写。
     * @return 拼音首字符组成的字符串
     * @throws UnsupportedEncodingException 
     */
    public static String getPYIndexStr(String strChinese, boolean bUpCase) throws UnsupportedEncodingException {
            StringBuffer buffer = new StringBuffer();

            byte b[] = strChinese.getBytes("GBK");

            int i = 0;
            while (i < b.length) {
                if ((int) (b[i] & 0xff) > 0x80) {
                    // 如果是中文就连续处理
                    int char1 = b[i++] & 0xff;
                    char1 = char1 << 8;
                    int chart = char1 + (int) (b[i] & 0xff);
                    buffer.append(getPYIndexChar((char) chart, bUpCase));
                } else {
                    char c = (char) b[i];
                    if (!Character.isJavaIdentifierPart(c))
                        c = 'A';
                    buffer.append(c);
                }
                i++;
            }
            return buffer.toString();
    }

    /**
     * 获取单个汉字的拼音首字符。
     * 
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     * getPYIndexChar(&quot;程&quot;, true) = &quot;C&quot;;
     * </pre>
     * 
     * @param strChinese
     *            要处理的汉字字符串
     * @param bUpCase
     *            决定返回的首拚字符是否大写,true(大写)
     * @return 返回单个汉字的拼音首字符
     */
    private static char getPYIndexChar(char strChinese, boolean bUpCase) {
        // 根据汉字表中拼音首字符分别为“A”至“Z”的汉字内码范围，
        // 要检索的汉字只需要检查它的内码位于哪一个首字符的范围内，
        // 就可以判断出它的拼音首字符。
        int charGBK = (int) strChinese;
        char result;
        if ((charGBK >= 0xB0A1) && (charGBK <= 0xB0C4))
            result = 'A';
        else if ((charGBK >= 0xB0C5) && (charGBK <= 0xB2C0))
            result = 'B';
        else if ((charGBK >= 0xB2C1) && (charGBK <= 0xB4ED))
            result = 'C';
        else if ((charGBK >= 0xB4EE) && (charGBK <= 0xB6E9))
            result = 'D';
        else if ((charGBK >= 0xB6EA) && (charGBK <= 0xB7A1))
            result = 'E';
        else if ((charGBK >= 0xB7A2) && (charGBK <= 0xB8C0))
            result = 'F';
        else if ((charGBK >= 0xB8C1) && (charGBK <= 0xB9FD))
            result = 'G';
        else if ((charGBK >= 0xB9FE) && (charGBK <= 0xBBF6))
            result = 'H';
        else if ((charGBK >= 0xBBF7) && (charGBK <= 0xBFA5))
            result = 'J';
        else if ((charGBK >= 0xBFA6) && (charGBK <= 0xC0AB))
            result = 'K';
        else if ((charGBK >= 0xC0AC) && (charGBK <= 0xC2E7))
            result = 'L';
        else if ((charGBK >= 0xC2E8) && (charGBK <= 0xC4C2))
            result = 'M';
        else if ((charGBK >= 0xC4C3) && (charGBK <= 0xC5B5))
            result = 'N';
        else if ((charGBK >= 0xC5B6) && (charGBK <= 0xC5BD))
            result = 'O';
        else if ((charGBK >= 0xC5BE) && (charGBK <= 0xC6D9))
            result = 'P';
        else if ((charGBK >= 0xC6DA) && (charGBK <= 0xC8BA))
            result = 'Q';
        else if ((charGBK >= 0xC8BB) && (charGBK <= 0xC8F5))
            result = 'R';
        else if ((charGBK >= 0xC8F6) && (charGBK <= 0xCBF9))
            result = 'S';
        else if ((charGBK >= 0xCBFA) && (charGBK <= 0xCDD9))
            result = 'T';
        else if ((charGBK >= 0xCDDA) && (charGBK <= 0xCEF3))
            result = 'W';
        else if ((charGBK >= 0xCEF4) && (charGBK <= 0xD1B8))
            result = 'X';
        else if ((charGBK >= 0xD1B9) && (charGBK <= 0xD4D0))
            result = 'Y';
        else if ((charGBK >= 0xD4D1) && (charGBK <= 0xD7F9))
            result = 'Z';
        else
            result = (char) ('A' + new Random().nextInt(25));
        if (!bUpCase) // 转换为小写
            result = Character.toLowerCase(result);
        return result;
    }

    /**
     * 用特定的间隔符对输入的字符串进行拆分，返回拆分后的数组。
     * 
     * <p>
     * <strong>用例描述：</strong>
     * 
     * <pre>
     *       toArray(&quot;boo:and:foo&quot;,&quot;:&quot;) = {&quot;boo&quot;, &quot;and&quot;, &quot;foo&quot;};
     *       toArray(&quot;boo:and:foo&quot;,&quot;o&quot;) = { &quot;b&quot;, &quot;:and:f&quot; };
     * </pre>
     * 
     * <strong>注意：</strong> 这个方法类似于{@link java.lang.String#split(java.lang.String)，
     * 但是区别在于对于连续与分割符相同的字符处理不一样。 如 new String("boo:and:foo").split("o")
     * ={"b","",":and:f"};
     * 
     * @param str
     *            以指字间隔符拼成的字符串。
     * @param token
     *            分割的标记
     * @return 拆分后的字符串数组
     * @see #split(String, String)
     * 
     */
    public static String[] toArray(String str, String delim) {
        return toArray(str, delim, false, false);
    }

    public static String[] toArray(String s) {

        return toArray(s, ",", false, false);
    }

    /**
     * 用特定的间隔符对输入的字符串进行拆分，返回拆分后的数组。实现与{@link #toArray(String, String)}同样的功能。
     * 
     * @see #toArray(String, String)
     */
    public static String[] split(String str, String token) {
        return toArray(str, token);
    }

    /**
     * 将字符串中的单引号替换为双引号。
     * 
     * @param strValue
     *            要替换的字符串
     * @return 替换为双引号的字符串。
     */
    public static String replaceQuotMark(String strValue) {

        String oldMark = "'"; // 将'替换成''
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

    /**
     * 将空格或空字符串转化为<tt>null</tt>。
     * 
     * @param str
     *            源字符串。
     * @return 转换后的字符串。
     */
    public static String spaceToNull(String str) {
        if (str == null || str.trim().length() == 0)
            return null;
        return str.trim();
    }

    /**
     * 将字符串之间的特定符号清除。
     * 
     * @param value
     *            字符串
     * @param removeChar
     *            需要清除的字符
     * @return 清除指定字符后的字符串。
     * @see #addCharToString(String, char)
     */
    public static String removeCharFromString(String value, char removeChar) {
        if (value == null)
            return null;
        String regular = value.trim();
        String removestr = String.valueOf(removeChar);
        int index = regular.indexOf(removestr);
        while (index > 0) {
            String temp = regular.substring(0, index);
            regular = temp + regular.substring(index + 1);
            index = regular.indexOf(removestr);
        }
        return regular;
    }

    /**
     * 对字符串加入间隔符。
     * 
     * @param value
     *            字符串
     * @param addChar
     *            间隔符
     * @return 加入间隔符后的字符串。
     */
    public static String addCharToString(String value, char addChar) {
        if (value == null)
            return null;
        String regular = value;
        // 正负数
        String sign = "";
        if (regular.substring(0, 1).equals("-")) {
            sign = "-";
            regular = regular.substring(1, regular.length());
        }
        int index = regular.indexOf(".");
        String fracTemp = "";
        if (index > 0) {
            fracTemp = "." + regular.substring(index + 1);
            regular = regular.substring(0, index);
        }
        //
        String after = null;
        String strAdd = String.valueOf(addChar);
        while (regular.length() > 3) {
            String temp = regular.substring(regular.length() - 3, regular.length());
            regular = regular.substring(0, regular.length() - 3);
            if (after == null)
                after = temp;
            else
                after = temp + strAdd + after;
        }
        if (after == null)
            regular = sign + regular + fracTemp;
        else
            regular = sign + regular + strAdd + after + fracTemp;
        //
        return regular;
    }

    /**
     * 计算字符宽度 create date:(2001-7-25 18:31:19)
     * 
     * @since V1.00
     * @return int
     * @param fontMetrics
     *            java.awt.FontMetrics
     * @param str
     *            java.lang.String
     */
    public static int computeStringWidth(FontMetrics fontMetrics, String str) {
        if (str == null || str.length() <= 0)
            return 0;
        int width = 10 + javax.swing.SwingUtilities.computeStringWidth(fontMetrics, str);
        int bytesLen = str.getBytes().length;
        if (bytesLen >= 10)
            width += (bytesLen - 10) * 2 + 5;
        return width;
    }

    /**
     * 将科学计量法表示的数据还原成正常表示的数据
     * 
     * @createDate:(2001-1-8 14:44:49)
     * @see:
     * @since:v1.0
     * @return java.lang.String
     * @param value
     *            java.lang.String
     */
    public static String convExpoToRegular(String value) {
        if (value == null)
            return "0";
        String regular = value.toUpperCase();
        // 正负数
        String sign = "";
        if (regular.substring(0, 1).equals("-")) {
            sign = "-";
            regular = regular.substring(1, regular.length());
        }
        int index1 = regular.indexOf("E");
        if (index1 > 0) {
            String temp = regular.substring(0, index1);
            String strExep = regular.substring(index1 + 1);
            int exep = Integer.parseInt(strExep);
            int index2 = temp.indexOf(".");
            if (exep >= 0) {
                // 如果指数大于等于0
                if (index2 > 0) {
                    // 如果是小数
                    String inteTemp = temp.substring(0, index2);
                    String fracTemp = temp.substring(index2 + 1);
                    if (fracTemp.length() > exep) {
                        regular = inteTemp + fracTemp.substring(0, exep) + "." + fracTemp.substring(exep);
                    } else {
                        int diff = exep - fracTemp.length();
                        for (int l = 0; l < diff; l++)
                            fracTemp += "0";
                        regular = inteTemp + fracTemp + ".0";
                    }
                } else {
                    // 如果是整数
                    for (int l = 0; l < exep; l++)
                        temp += "0";
                    regular = temp;
                }
            } else {
                // 如果指数是负数
                String inteTemp = temp;
                String fracTemp = "";
                exep = -exep;
                if (index2 > 0) {
                    inteTemp = temp.substring(0, index2);
                    fracTemp = temp.substring(index2 + 1);
                }
                if (inteTemp.length() > exep) {
                    int diff = inteTemp.length() - exep;
                    regular = inteTemp.substring(0, diff) + "." + inteTemp.substring(diff) + fracTemp;
                } else {
                    int diff = exep - inteTemp.length();
                    for (int l = 0; l < diff; l++)
                        inteTemp = "0" + inteTemp;
                    regular = "0." + inteTemp + fracTemp;
                }
            }
        }
        return sign + regular;
    }

    /**
     * 对小数的精度控制
     * 
     * @createDate:(2000-12-22 10:29:02)
     * @see:
     * @since:v1.0
     * @return java.lang.String
     * @param str
     *            java.lang.String 数字串
     * @param precision
     *            int 精度(小数点后的位数)
     */
    public static String formatFloat(String str, int precision) {
        if (str == null)
            return "0";
        // 如果是科学计数法,则先转换
        if (str.indexOf("E") > -1)
            str = convExpoToRegular(str);
        // 整数部分
        String preStr = "";
        // 小数部分
        String numStr = "";
        if (precision < 0)
            precision = 0;
        int index = str.indexOf(".");
        if (index > -1) {
            // 有小数点
            if (index == 0) {
                preStr = "0";
            } else {
                preStr = str.substring(0, index);
                if (preStr.equals("-"))
                    preStr += "0";
            }
            numStr = str.substring(index + 1);
        } else {
            preStr = str;
        }
        if (precision > 0) {
            preStr += ".";
            int len = numStr.length();
            if (len < precision) {
                // 小数点后精度小于所需的精度,则补0
                for (int i = 0; i < precision - len; i++)
                    numStr += "0";
                preStr += numStr;
            } else if (len > precision) {
                String s = numStr.substring(precision, precision + 1);
                String temp = numStr.substring(0, precision);
                if (Integer.parseInt(s) >= 5) {
                    // 进行加1操作
                    preStr = addOne(preStr, temp, "");
                } else {
                    preStr += temp;
                }
            } else
                preStr += numStr;
        }
        return preStr;
    }

    /**
     * 将字符"Y","N"转换为boolean值 逆函数为booleanToString(boolean)
     * 
     * @createDate:(2001-2-12 19:25:36)
     * @see:
     * @since:v1.0
     * @return boolean
     * @param str
     *            java.lang.String
     */
    public static boolean stringToBoolean(String str) {
        if (str == null)
            return false;
        if (str.equalsIgnoreCase("Y"))
            return true;
        else
            return false;
    }

    /**
     * 对用连字符连接起来的字符串进行解码，返回其中的编码。如果不能成功解析，则返回<code>null</code>。
     * 
     * @param obj
     *            编码+连字符+名称的字符串。
     * @return 编码
     */
    public static String getObjectCode(String obj) {
        String code = null;
        if (obj != null) {
            int index = obj.indexOf(HYPHEN);
            if (index > -1) {
                code = obj.substring(0, index);
            }
        }
        return code;
    }

    /**
     * 对用连字符连接起来的字符串进行解码，返回其中的名称。如果不能成功解析，则返回<code>null</code>。
     * 
     * <p>
     * (2001-2-12 19:28:51)
     * 
     * @param obj
     *            编码+连字符+名称的字符串。
     * @return 名称
     */
    public static String getObjectName(String obj) {
        String name = null;
        if (obj != null) {
            int index = obj.indexOf(HYPHEN);
            if (index > -1) {
                name = obj.substring(index + 1);
            }
        }
        return name;
    }

    /**
     * 返回保留字数组。保留字包含：&quot;&nbsp;&quot;,&quot;`&quot;,&quot;#&quot;,&quot;&&quot;,&quot;\&quot;,
     * &quot;'&quot;,&quot;?&quot;,&quot;+&quot;,&quot;-&quot;,&quot;!&quot;。
     * 
     * @return
     */
    public static String[] getReservedWords() {
        return new String[] { " ", "`", "#", "&", "*", "\"", "'", "?", "+", "-", "!" };
    }

    /**
     * 进行加1操作
     * 
     * @createDate:(2000-12-22 11:32:09)
     * @see:
     * @since:v1.0
     * @param preStr
     *            java.lang.String 整数部分
     * @param numStr
     *            java.lang.String 当前小数部分
     * @param afterStr
     *            java.lang.String 后缀
     */
    private static String addOne(String preStr, String numStr, String afterStr) {
        String result = "";
        if (numStr.length() > 0) {
            String s = numStr.substring(numStr.length() - 1);
            numStr = numStr.substring(0, numStr.length() - 1);
            int value = Integer.parseInt(s);
            if (value == 9) {
                afterStr = "0" + afterStr;
                result = addOne(preStr, numStr, afterStr);
            } else {
                result = preStr + numStr + Integer.toString(value + 1) + afterStr;
            }
        } else if (preStr.length() > 0) {
            String s = preStr.substring(preStr.length() - 1);
            preStr = preStr.substring(0, preStr.length() - 1);
            if (s.equals(".")) {
                afterStr = s + afterStr;
                result = addOne(preStr, numStr, afterStr);
            } else {
                int value = Integer.parseInt(s);
                if (value == 9) {
                    afterStr = "0" + afterStr;
                    result = addOne(preStr, numStr, afterStr);
                } else {
                    result = preStr + numStr + Integer.toString(value + 1) + afterStr;
                }
            }
        } else {
            result = preStr + "1" + numStr + afterStr;
        }
        return result;
    }

    /**
     * 根据字节数组比较两对象。<tt>null</tt>定义为最小。
     * 
     * @param o1
     *            对象1
     * @param o2
     *            对象2
     * @return <tt>1</tt> - 对象1比对象2大；<br>
     *         <tt>0</tt> - 对象1与对象2相等；<br>
     *         <tt>-1</tt> - 对象1比对象2小。
     */
    public static int compareByByte(Object o1, Object o2) {
        if (null == o1) {
            return (null == o2) ? 0 : -1;
        }
        if (null == o2) {
            return 1;
        }

        boolean isBytes = o1 instanceof byte[];
        byte[] bAry1 = isBytes ? (byte[]) o1 : o1.toString().getBytes();
        byte[] bAry2 = isBytes ? (byte[]) o2 : o2.toString().getBytes();

        int len1 = bAry1.length;
        int len2 = bAry2.length;
        int n = Math.min(len1, len2);
        int i = 0;
        int j = 0;
        int r = 0;
        while (n-- != 0) {
            if ((r = bAry1[i++] - bAry2[j++]) != 0)
                break;
        }
        if (r == 0)
            r = len1 - len2;
        if (r == 0)
            return 0;
        if (r > 0)
            return 1;
        return -1;
    }

    /**
     * 检查字符串是否为空串("")或<tt>null</tt>。不会trim给定字符串。
     * 
     * @param str
     *            the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 检查字符串是否为空串("   ")或<tt>null</tt>。会trim给定字符串
     * @param str
     * @return
     */
    public static boolean isEmptyWithTrim(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断一个字符串是否包含中文
     * 
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        if (isEmpty(str))
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+"))
                return true;
        }
        return false;
    }

    public static int lenOfChinesString(String str) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '\u4e00' && c <= '\u9fa5') {
                len += 2;
            } else {
                len++;
            }
        }
        return len;
    }

    public static String subChineseString(String str, int from, int len) {
        if (str == null || from < 0 || len <= 0) {
            throw new IllegalArgumentException();
        }
        int splitedLen = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = from; i < str.length() && (i < from + len); i++) {
            char c = str.charAt(i);
            if (c >= '\u4e00' && c <= '\u9fa5') {
                splitedLen += 2;
            } else {
                splitedLen++;
            }
            if (splitedLen > len) {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 对多语言里的换行符号(转义符号)进行复原
     * 
     * @param msg
     * @return
     */
    public static String recoverWrapLineChar(String msg) {
        if (msg == null)
            return "";
        StringBuffer dest = new StringBuffer();
        for (int i = 0; i < msg.length(); i++) {
            char aChar = msg.charAt(i);
            if (aChar == '\\' && i < msg.length() - 1) {
                char aCharNext = msg.charAt(++i);
                if (aCharNext == 't')
                    aCharNext = '\t';
                else if (aCharNext == 'r')
                    aCharNext = '\r';
                else if (aCharNext == 'n')
                    aCharNext = '\n';
                else if (aCharNext == 'f')
                    aCharNext = '\f';
                else
                    dest.append(aChar);
                dest.append(aCharNext);
            } else
                dest.append(aChar);
        }
        return dest.toString();
    }

    /**
     * 转化成十六进制的字符数组。
     * @param bArray
     * @return
     */
    public static char[] toHexChar(byte[] bArray) {
        char[] digitChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] charDigest = new char[bArray.length * 2];

        for (int i = 0; i < bArray.length; i++) {
            charDigest[i * 2] = digitChars[(bArray[i] >>> 4) & 0X0F];
            charDigest[i * 2 + 1] = digitChars[bArray[i] & 0X0F];
        }
        return charDigest;
    }

    public static int compare(String str1, String str2) throws UnsupportedEncodingException {
        String t1 = "";
        String t2 = "";
            if (str1 != null)
                t1 = new String(str1.getBytes(), "ISO-8859-1");
            if (str2 != null)
                t2 = new String(str2.getBytes(), "ISO-8859-1");
        return t1.compareTo(t2);
    }

    /**
     * convert a string to a string array
     * 
     * @param s - the string
     * @param delimiters - the seperator for array element
     * @param trimTokens - whether trim the array element
     * @param ignoreEmptyTokens - whether ignore empty string token
     * @return
     */
    public static String[] toArray(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (s == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(s, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!(ignoreEmptyTokens && token.length() == 0)) {
                tokens.add(token);
            }
        }
        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    /**
     * not starts with
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean nstartsWith(String str1, String str2) {
        if (str2.length() > str1.length())
            return true;
        for (int i = 0; i < str2.length(); i++) {
            if (str2.charAt(i) != str1.charAt(i))
                return true;
        }
        return false;
    }

    /**
     * not ends with
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static boolean nendsWith(String str1, String str2) {
        if (str2.length() > str1.length())
            return true;
        int indexSrc = str1.length() - 1;
        for (int i = str2.length() - 1; i > 0; i--) {
            if (str2.charAt(i) != str1.charAt(indexSrc--))
                return true;
        }
        return false;
    }

    public static boolean nequals(String str1, String str2) {
        if (str2.length() != str1.length())
            return true;
        for (int i = 0; i < str2.length(); i++) {
            if (str2.charAt(i) != str1.charAt(i))
                return true;
        }
        return false;
    }

    public static boolean hasText(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String digest(String strSource) throws java.security.NoSuchAlgorithmException{
        StringBuffer digest = new StringBuffer();
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA");
            byte[] sourBytes = strSource.getBytes();
            byte[] digestBytes = md.digest(sourBytes);
            if (digestBytes != null) {
                for (int i = 0, n = digestBytes.length; i < n; i++) {
                    digest.append(digestBytes[i]);
                }
            }
        return digest.toString();
    }

    public static String digest(URL[] urls) throws NoSuchAlgorithmException, IOException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < urls.length; i++) {
            long lastModified = -1;
            if (i > 1)
                buffer.append(";");
            buffer.append(urls[i]);
            if ("file".equals(urls[i].getProtocol())) {
                File file = new File(urls[i].getPath().replace('/', File.separatorChar).replace('|', ':'));
                lastModified = file.lastModified();
            } else {
                URLConnection connection;
                connection = urls[i].openConnection();
                lastModified = connection.getLastModified();
            }

            buffer.append('!').append(lastModified);

        }
        return digest(buffer.toString());
    }

    public static String toString(Object[] arr) {
        return toString(arr, ",");
    }

    @SuppressWarnings("unchecked")
	public static String toString(Object value, String delim) {
        if (value instanceof String) {
            return (String) value;
        } else {
            if (value.getClass().isArray()) {
                return toString((Object[]) value, delim);
            } else if (value instanceof Iterable) {
                return toString((Iterable) value, delim);
            } else {
                return value.toString();
            }
        }
    }

    @SuppressWarnings("unchecked")
	public static String toString(Iterable c, String delim, String prefix, String suffix) {
        if (c == null) {
            return "null";
        }
        StringBuffer sb = new StringBuffer();
        Iterator it = c.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (i++ > 0) {
                sb.append(delim);
            }
            sb.append(prefix + it.next() + suffix);
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
	public static String toString(Iterable c, String delim) {
        return toString(c, delim, "", "");
    }

    public static String toString(Object value) {
        return toString(value, ",");
    }

    public static String toString(Object[] arr, String delim) {
        if (arr == null) {
            return "null";
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                if (i > 0)
                    sb.append(delim);
                sb.append(arr[i]);
            }
            return sb.toString();
        }
    }

    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(true, str);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(false, str);
    }

    private static String changeFirstCharacterCase(boolean capitalize, String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        StringBuffer buf = new StringBuffer(strLen);
        if (capitalize) {
            buf.append(Character.toUpperCase(str.charAt(0)));
        } else {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }

    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    public static String getFilename(String path) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    public static String normalizePath(String path) {
        String p = path.replace(WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
        String[] pArray = toArray(p, FOLDER_SEPARATOR);
        List<String> pList = new LinkedList<String>();
        int tops = 0;
        for (int i = pArray.length - 1; i >= 0; i--) {
            if (CURRENT_PATH.equals(pArray[i])) {
                // Do nothing
            } else if (TOP_PATH.equals(pArray[i])) {
                tops++;
            } else {
                if (tops > 0) {
                    tops--;
                } else {
                    pList.add(0, pArray[i]);
                }
            }
        }
        return toString(pList, FOLDER_SEPARATOR);
    }

    public static boolean pathEquals(String path1, String path2) {
        return normalizePath(path1).equals(normalizePath(path2));
    }

    public static URL[] pathToURLs(String path) throws MalformedURLException {
        StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
        URL[] urls = new URL[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens()) {
            File file = new File(st.nextToken());
            URL url = null;
            url = file.toURI().toURL();
            if (url != null) {
                urls[count++] = url;
            }
        }
        if (urls.length != count) {
            URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }

    public static String removeLastFileSeperator(String path) {
        if (path == null)
            return null;
        while (path.endsWith("\\") || path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return StringUtility.spaceToNull(path.trim());
    }

    public static String getTimeStampString(long l) {
        java.util.Calendar cl = java.util.Calendar.getInstance();
        cl.setTimeInMillis(l);
        int ia[] = new int[5];
        int year = cl.get(Calendar.YEAR);
        ia[0] = cl.get(Calendar.MONTH) + 1;
        ia[1] = cl.get(Calendar.DAY_OF_MONTH);
        ia[2] = cl.get(Calendar.HOUR_OF_DAY);
        ia[3] = cl.get(Calendar.MINUTE);

        ia[4] = cl.get(Calendar.SECOND);
        byte ba[] = new byte[19];
        ba[4] = ba[7] = (byte) '-';
        ba[10] = (byte) ' ';
        ba[13] = ba[16] = (byte) ':';
        ba[0] = (byte) (year / 1000 + '0');
        ba[1] = (byte) ((year / 100) % 10 + '0');
        ba[2] = (byte) ((year / 10) % 10 + '0');
        ba[3] = (byte) (year % 10 + '0');
        for (int i = 0; i < 5; i++) {
            ba[i * 3 + 5] = (byte) (ia[i] / 10 + '0');
            ba[i * 3 + 6] = (byte) (ia[i] % 10 + '0');
        }
        return new String(ba);
    }

    public static String replace(String s, Properties p) {
        String regex = "\\$\\w+\\W|\\$\\{[^}]+\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(s);

        while (m.find()) {
            String temp = m.group();
            String key = null;
            if (temp.indexOf("{") != -1) {
                key = temp.substring(temp.indexOf("{") + 1, temp.length() - 1);

            } else {
                key = temp.substring(1, temp.length() - 1);
            }
            String value = p.getProperty(key);

            if (value != null) {
                s = s.replace(temp, value);
                m = pattern.matcher(s);
            }
        }

        return s;
    }
}
