package nc.uap.lfw.multilang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.internal.corext.refactoring.nls.KeyValuePair;
import org.eclipse.jdt.internal.corext.refactoring.nls.SimpleLineReader;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;

public class MyPropertyFileDocumentModel
{
  private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  private List fKeyValuePairs;
  private String fLineDelimiter;

  public MyPropertyFileDocumentModel(IDocument document)
  {
    parsePropertyDocument(document);
    this.fLineDelimiter = TextUtilities.getDefaultLineDelimiter(document);
  }

  public int getIndex(String key) {
    for (int i = 0; i < this.fKeyValuePairs.size(); ++i) {
      KeyValuePairModell keyValuePair = (KeyValuePairModell)this.fKeyValuePairs.get(i);
      if (keyValuePair.getKey().equals(key))
        return i;
    }

    return -1;
  }

  public InsertEdit insert(String key, String value)
  {
    return insert(new KeyValuePair(key, value));
  }

  public InsertEdit insert(KeyValuePair keyValuePair) {
    KeyValuePairModell keyValuePairModell = new KeyValuePairModell(keyValuePair);
    int index = findInsertPosition(keyValuePairModell);
    KeyValuePairModell insertHere = (KeyValuePairModell)this.fKeyValuePairs.get(index);
    int offset = insertHere.fOffset - insertHere.fLeadingWhiteSpaces;

    String extra = "";
    if ((insertHere instanceof LastKeyValuePair) && (((LastKeyValuePair)insertHere).needsNewLine())) {
      extra = this.fLineDelimiter;
      ((LastKeyValuePair)insertHere).resetNeedsNewLine();
    }
    return new InsertEdit(offset, extra + keyValuePairModell.getText(this.fLineDelimiter));
  }

  public InsertEdit[] insert(KeyValuePair[] keyValuePairs) {
    InsertEdit[] inserts = new InsertEdit[keyValuePairs.length];
    for (int i = 0; i < keyValuePairs.length; ++i)
      inserts[i] = insert(keyValuePairs[i]);

    return inserts;
  }

  public DeleteEdit remove(String key) {
    for (Iterator iter = this.fKeyValuePairs.iterator(); iter.hasNext(); ) {
      KeyValuePairModell keyValuePair = (KeyValuePairModell)iter.next();
      if (!(keyValuePair.fKey.equals(key))) break;
      KeyValuePairModell next = (KeyValuePairModell)iter.next();
      label67: return new DeleteEdit(keyValuePair.fOffset, next.fOffset - keyValuePair.fOffset);
    }

    return null;
  }

  public ReplaceEdit replace(KeyValuePair toReplace, KeyValuePair replaceWith) {
    for (Iterator iter = this.fKeyValuePairs.iterator(); iter.hasNext(); ) {
      KeyValuePairModell keyValuePair = (KeyValuePairModell)iter.next();
      if (!(keyValuePair.fKey.equals(toReplace.getKey()))) break;
      String newText = new KeyValuePairModell(replaceWith).getText(this.fLineDelimiter);
      KeyValuePairModell next = (KeyValuePairModell)iter.next();
      int range = next.fOffset - keyValuePair.fOffset;
      label97: return new ReplaceEdit(keyValuePair.fOffset, range, newText);
    }

    return null;
  }

  private int findInsertPosition(KeyValuePairModell keyValuePair) {
    int insertIndex = 0;
    int maxMatch = -2147483648;
    for (int i = 0; i < this.fKeyValuePairs.size(); ++i) {
      KeyValuePairModell element = (KeyValuePairModell)this.fKeyValuePairs.get(i);
      int match = element.compareTo(keyValuePair);
      if (match >= maxMatch) {
        insertIndex = i;
        maxMatch = match;
      }
    }

    if (insertIndex < this.fKeyValuePairs.size() - 1) {
      ++insertIndex;
    }

    return insertIndex;
  }

  private void parsePropertyDocument(IDocument document) {
    this.fKeyValuePairs = new ArrayList();

    SimpleLineReader reader = new SimpleLineReader(document);
    int offset = 0;
    String line = reader.readLine();
    int leadingWhiteSpaces = 0;
    while (line != null) {
      if (!(SimpleLineReader.isCommentOrWhiteSpace(line))) {
        int idx = getIndexOfSeparationCharacter(line);
        if (idx != -1) {
          String key = line.substring(0, idx);
          String value = line.substring(idx + 1);
          this.fKeyValuePairs.add(new KeyValuePairModell(key, value, offset, leadingWhiteSpaces));
          leadingWhiteSpaces = 0;
        }
      } else {
        leadingWhiteSpaces += line.length();
      }
      offset += line.length();
      line = reader.readLine();
    }
    int lastLine = document.getNumberOfLines() - 1;
    boolean needsNewLine = false;
    try {
      needsNewLine = document.getLineLength(lastLine) != 0;
    }
    catch (BadLocationException localBadLocationException) {
    }
    LastKeyValuePair lastKeyValuePair = new LastKeyValuePair(offset, needsNewLine);
    this.fKeyValuePairs.add(lastKeyValuePair);
  }

  private int getIndexOfSeparationCharacter(String line) {
    int minIndex = -1;
    int indexOfEven = line.indexOf(61);
    int indexOfColumn = line.indexOf(58);
    int indexOfBlank = line.indexOf(32);

    if ((indexOfEven != -1) && (indexOfColumn != -1))
      minIndex = Math.min(indexOfEven, indexOfColumn);
    else {
      minIndex = Math.max(indexOfEven, indexOfColumn);
    }

    if ((minIndex != -1) && (indexOfBlank != -1))
      minIndex = Math.min(minIndex, indexOfBlank);
    else {
      minIndex = Math.max(minIndex, indexOfBlank);
    }

    return minIndex;
  }

  public static String unwindEscapeChars(String s) {
    StringBuffer sb = new StringBuffer(s.length());
    int length = s.length();
    for (int i = 0; i < length; ++i) {
      char c = s.charAt(i);
      sb.append(getUnwoundString(c));
    }
    return sb.toString();
  }

  public static String unwindValue(String value) {
    return escapeLeadingWhiteSpaces(escapeCommentChars(unwindEscapeChars(value)));
  }

  private static String getUnwoundString(char c) {
    switch (c)
    {
    case '\b':
      return "\\b";
    case '\t':
      return "\\t";
    case '\n':
      return "\\n";
    case '\f':
      return "\\f";
    case '\r':
      return "\\r";
    case '\\':
      return "\\\\";
    }

    if ((c < ' ') || (c > '~')) {
      return 
        "\\" + 
        'u' + 
        toHex(c >> '\f' & 0xF) + 
        toHex(c >> '\b' & 0xF) + 
        toHex(c >> '\4' & 0xF) + 
        toHex(c & 0xF);
    }

    return String.valueOf(c);
  }

  private static char toHex(int halfByte)
  {
    return HEX_DIGITS[(halfByte & 0xF)];
  }

  private static String escapeCommentChars(String string) {
    char c;
    StringBuffer sb = new StringBuffer(string.length() + 5);
    for (int i = 0; i < string.length(); ++i) {
      c = string.charAt(i);
      switch (c)
      {
      case '!':
        sb.append("\\!");
        break;
      case '#':
        sb.append("\\#");
        break;
      case '"':
      default:
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private static String escapeLeadingWhiteSpaces(String str) {
    int firstNonWhiteSpace = findFirstNonWhiteSpace(str);
    StringBuffer buf = new StringBuffer(firstNonWhiteSpace);
    for (int i = 0; i < firstNonWhiteSpace; ++i) {
      buf.append('\\');
      buf.append(str.charAt(i));
    }
    buf.append(str.substring(firstNonWhiteSpace));
    return buf.toString();
  }

  private static int findFirstNonWhiteSpace(String s)
  {
    for (int i = 0; i < s.length(); ++i)
      if (!(Character.isWhitespace(s.charAt(i))))
        return i;

    return s.length();
  }

  private static class KeyValuePairModell extends KeyValuePair
  implements Comparable
  {
    int fOffset;
    int fLeadingWhiteSpaces;

    public KeyValuePairModell(String key, String value, int offset, int leadingWhiteSpaces) {
      super(key, value);
      this.fOffset = offset;
      this.fLeadingWhiteSpaces = leadingWhiteSpaces;
    }

    public KeyValuePairModell(KeyValuePair keyValuePair) {
      super(keyValuePair.fKey, keyValuePair.fValue);
    }

    public String getText(String lineDelimiter)
    {
      return this.fKey + '=' + this.fValue + lineDelimiter; }

    public int compareTo(Object o) {
      int counter = 0;
      String key = ((KeyValuePair)o).fKey;
      int minLen = Math.min(key.length(), this.fKey.length());
      int diffLen = Math.abs(key.length() - this.fKey.length());
      for (int i = 0; i < minLen; ++i) {
        if (key.charAt(i) != this.fKey.charAt(i)) break;
        ++counter;
      }

      return (counter - diffLen);
    }
  }

  private static class LastKeyValuePair extends MyPropertyFileDocumentModel.KeyValuePairModell
  {
    private boolean fNeedsNewLine;

    public LastKeyValuePair(int offset, boolean needsNewLine)
    {
      super("last", "key", offset, 0);
      this.fNeedsNewLine = needsNewLine; }

    public int compareTo(Object o) {
      return 1; }

    public boolean needsNewLine() {
      return this.fNeedsNewLine; }

    public void resetNeedsNewLine() {
      this.fNeedsNewLine = false;
    }
  }
}
