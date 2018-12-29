package nc.uap.lfw.mlr;

import java.util.*;
import org.eclipse.jdt.internal.corext.refactoring.nls.KeyValuePair;
import org.eclipse.jdt.internal.corext.refactoring.nls.SimpleLineReader;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

/**
 * 资源文件生成类
 * 
 * @author dingrf
 *
 */
public class MyPropertyFileDocumentModel{
	
	/**
	 *键值对
	 */
	private static class KeyValuePairModell extends KeyValuePair implements Comparable<Object>{

		int fOffset;
		int fLeadingWhiteSpaces;

		public String getText(String lineDelimiter){
			return (new StringBuilder(fKey)).append('=').append(fValue).append(lineDelimiter).toString();
		}

		@Override
		public int compareTo(Object o){
			int counter = 0;
			String key = ((KeyValuePair)o).fKey;
			int minLen = Math.min(key.length(), fKey.length());
			int diffLen = Math.abs(key.length() - fKey.length());
			for (int i = 0; i < minLen; i++){
				if (key.charAt(i) != fKey.charAt(i))
					break;
				counter++;
			}

			return counter - diffLen;
		}

		public KeyValuePairModell(String key, String value, int offset, int leadingWhiteSpaces){
			super(key, value);
			fOffset = offset;
			fLeadingWhiteSpaces = leadingWhiteSpaces;
		}

		public KeyValuePairModell(KeyValuePair keyValuePair){
			super(keyValuePair.fKey, keyValuePair.fValue);
		}
	}

	/**
	 *最后键值对 
	 */
	private static class LastKeyValuePair extends KeyValuePairModell{

		private boolean fNeedsNewLine;

		@Override
		public int compareTo(Object o){
			return 1;
		}

		public boolean needsNewLine(){
			return fNeedsNewLine;
		}

		public void resetNeedsNewLine(){
			fNeedsNewLine = false;
		}

		public LastKeyValuePair(int offset, boolean needsNewLine){
			super("last", "key", offset, 0);
			fNeedsNewLine = needsNewLine;
		}
	}


	private static final char HEX_DIGITS[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F'
	};
	
	/**文件键值对列表*/
	private List<KeyValuePairModell> fKeyValuePairs;
	
	/**换行符*/
	private String fLineDelimiter;

	public MyPropertyFileDocumentModel(IDocument document){
		parsePropertyDocument(document);
		fLineDelimiter = TextUtilities.getDefaultLineDelimiter(document);
	}

	public int getIndex(String key){
		for (int i = 0; i < fKeyValuePairs.size(); i++){
			KeyValuePairModell keyValuePair = (KeyValuePairModell)fKeyValuePairs.get(i);
			if (keyValuePair.getKey().equals(key))
				return i;
		}
		return -1;
	}

	/**
	 * 插入键值对
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public InsertEdit insert(String key, String value){
		return insert(new KeyValuePair(key, value));
	}

	/**
	 *插入键值对
	 * 
	 * @param keyValuePair
	 * @return
	 */
	public InsertEdit insert(KeyValuePair keyValuePair){
		KeyValuePairModell keyValuePairModell = new KeyValuePairModell(keyValuePair);
		int index = findInsertPosition(keyValuePairModell);
		KeyValuePairModell insertHere = (KeyValuePairModell)fKeyValuePairs.get(index);
		int offset = insertHere.fOffset - insertHere.fLeadingWhiteSpaces;
		String extra = "";
		if ((insertHere instanceof LastKeyValuePair) && ((LastKeyValuePair)insertHere).needsNewLine()){
			extra = fLineDelimiter;
			((LastKeyValuePair)insertHere).resetNeedsNewLine();
		}
		return new InsertEdit(offset, (new StringBuilder(extra)).append(keyValuePairModell.getText(fLineDelimiter)).toString());
	}

	/**
	 * 插入键值对数组
	 * 
	 * @param keyValuePairs
	 * @return
	 */
	public InsertEdit[] insert(KeyValuePair keyValuePairs[]){
		InsertEdit inserts[] = new InsertEdit[keyValuePairs.length];
		for (int i = 0; i < keyValuePairs.length; i++)
			inserts[i] = insert(keyValuePairs[i]);

		return inserts;
	}

	/**
	 * 删除键值对
	 * 
	 * @param key
	 * @return
	 */
	public DeleteEdit remove(String key){
		for (Iterator<KeyValuePairModell> iter = fKeyValuePairs.iterator(); iter.hasNext();){
			KeyValuePairModell keyValuePair = (KeyValuePairModell)iter.next();
			if (keyValuePair.fKey.equals(key)){
				KeyValuePairModell next = (KeyValuePairModell)iter.next();
				return new DeleteEdit(keyValuePair.fOffset, next.fOffset - keyValuePair.fOffset);
			}
		}
		return null;
	}

	/**
	 * 替换键值对
	 * 
	 * @param toReplace
	 * @param replaceWith
	 * @return
	 */
	public ReplaceEdit replace(KeyValuePair toReplace, KeyValuePair replaceWith){
		for (Iterator<KeyValuePairModell> iter = fKeyValuePairs.iterator(); iter.hasNext();){
			KeyValuePairModell keyValuePair = (KeyValuePairModell)iter.next();
			if (keyValuePair.fKey.equals(toReplace.getKey())){
				String newText = (new KeyValuePairModell(replaceWith)).getText(fLineDelimiter);
				KeyValuePairModell next = (KeyValuePairModell)iter.next();
				int range = next.fOffset - keyValuePair.fOffset;
				return new ReplaceEdit(keyValuePair.fOffset, range, newText);
			}
		}

		return null;
	}

	/**
	 * 找到插入位置
	 * @param keyValuePair
	 * @return
	 */
	private int findInsertPosition(KeyValuePairModell keyValuePair){
		int insertIndex = 0;
		int maxMatch = 0x80000000;
		for (int i = 0; i < fKeyValuePairs.size(); i++){
			KeyValuePairModell element = (KeyValuePairModell)fKeyValuePairs.get(i);
			int match = element.compareTo(keyValuePair);
			if (match >= maxMatch){
				insertIndex = i;
				maxMatch = match;
			}
		}

		if (insertIndex < fKeyValuePairs.size() - 1)
			insertIndex++;
		return insertIndex;
	}

	/**
	 * 解析document中的键值对
	 * 
	 * @param document
	 */
	private void parsePropertyDocument(IDocument document){
		fKeyValuePairs = new ArrayList<KeyValuePairModell>();
		SimpleLineReader reader = new SimpleLineReader(document);
		int offset = 0;
		String line = reader.readLine();
		int leadingWhiteSpaces = 0;
		for (; line != null; line = reader.readLine()){
			if (!SimpleLineReader.isCommentOrWhiteSpace(line)){
				int idx = getIndexOfSeparationCharacter(line);
				if (idx != -1){
					String key = line.substring(0, idx);
					String value = line.substring(idx + 1);
					fKeyValuePairs.add(new KeyValuePairModell(key, value, offset, leadingWhiteSpaces));
					leadingWhiteSpaces = 0;
				}
			} 
			else{
				leadingWhiteSpaces += line.length();
			}
			offset += line.length();
		}

		int lastLine = document.getNumberOfLines() - 1;
		boolean needsNewLine = false;
		try{
			needsNewLine = document.getLineLength(lastLine) != 0;
		}
		catch (BadLocationException e) { }
		LastKeyValuePair lastKeyValuePair = new LastKeyValuePair(offset, needsNewLine);
		fKeyValuePairs.add(lastKeyValuePair);
	}

	/**
	 *取键与值分割字符位置
	 * 
	 * @param line
	 * @return
	 */
	private int getIndexOfSeparationCharacter(String line){
		int minIndex = -1;
		int indexOfEven = line.indexOf('=');
		int indexOfColumn = line.indexOf(':');
		int indexOfBlank = line.indexOf(' ');
		if (indexOfEven != -1 && indexOfColumn != -1)
			minIndex = Math.min(indexOfEven, indexOfColumn);
		else
			minIndex = Math.max(indexOfEven, indexOfColumn);
		if (minIndex != -1 && indexOfBlank != -1)
			minIndex = Math.min(minIndex, indexOfBlank);
		else
			minIndex = Math.max(minIndex, indexOfBlank);
		return minIndex;
	}

	public static String unwindEscapeChars(String s){
		StringBuffer sb = new StringBuffer(s.length());
		int length = s.length();
		for (int i = 0; i < length; i++){
			char c = s.charAt(i);
			sb.append(getUnwoundString(c));
		}

		return sb.toString();
	}

	public static String unwindValue(String value){
		return escapeLeadingWhiteSpaces(escapeCommentChars(unwindEscapeChars(value)));
	}

	private static String getUnwoundString(char c){
		switch (c){
		case 8: // '\b'
			return "\\b";

		case 9: // '\t'
			return "\\t";

		case 10: // '\n'
			return "\\n";

		case 12: // '\f'
			return "\\f";

		case 13: // '\r'
			return "\\r";

		case 92: // '\\'
			return "\\\\";
		}
		if (c < ' ' || c > '~')
			return "" + '\\' + 'u' + toHex(c >> 12 & 0xf) + toHex(c >> 8 & 0xf) + toHex(c >> 4 & 0xf) + toHex(c & 0xf);
		else
			return String.valueOf(c);
	}

	private static char toHex(int halfByte){
		return HEX_DIGITS[halfByte & 0xf];
	}

	private static String escapeCommentChars(String string){
		StringBuffer sb = new StringBuffer(string.length() + 5);
		for (int i = 0; i < string.length(); i++){
			char c = string.charAt(i);
			switch (c){
			case 33: // '!'
				sb.append("\\!");
				break;

			case 35: // '#'
				sb.append("\\#");
				break;

			case 34: // '"'
			default:
				sb.append(c);
				break;
			}
		}

		return sb.toString();
	}

	private static String escapeLeadingWhiteSpaces(String str){
		int firstNonWhiteSpace = findFirstNonWhiteSpace(str);
		StringBuffer buf = new StringBuffer(firstNonWhiteSpace);
		for (int i = 0; i < firstNonWhiteSpace; i++)
		{
			buf.append('\\');
			buf.append(str.charAt(i));
		}

		buf.append(str.substring(firstNonWhiteSpace));
		return buf.toString();
	}

	private static int findFirstNonWhiteSpace(String s){
		for (int i = 0; i < s.length(); i++)
			if (!Character.isWhitespace(s.charAt(i)))
				return i;

		return s.length();
	}

}
