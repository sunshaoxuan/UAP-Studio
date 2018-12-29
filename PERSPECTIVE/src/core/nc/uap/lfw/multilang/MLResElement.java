package nc.uap.lfw.multilang;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.Region;

public class MLResElement {
	  public static final String MLR_TAG_PREFIX = "/*$MLRes-";
	  public static final int MLR_TAG_PREFIX_LENGTH = "/*$MLRes-".length();
	  public static final String MLR_TAG_POSTFIX = "$*/";
	  public static final int MLR_TAG_POSTFIX_LENGTH = "$*/".length();
	  private String fValue;
	  private Region fPosition;
	  private Region fTagPosition;
	  private String argStr = null;
	  
	  public MLResElement(String value, int start, int length)
	  {
	    this.fValue = value;
	    Assert.isNotNull(this.fValue);
	    this.fPosition = new Region(start, length);
	  }
	  public MLResElement(String value, int start, int length, String argStr)
	  {
	    this.fValue = value;
	    Assert.isNotNull(this.fValue);
	    this.fPosition = new Region(start, length);
	    this.argStr = argStr;
	  }
	  public Region getPosition()
	  {
	    return this.fPosition;
	  }

	  public String getValue()
	  {
	    return this.fValue;
	  }

	  public void setValue(String value)
	  {
	    this.fValue = value;
	  }

	  public void setTagPosition(int start, int length)
	  {
	    this.fTagPosition = new Region(start, length);
	  }

	  public Region getTagPosition()
	  {
	    return this.fTagPosition;
	  }

	  public boolean hasTag()
	  {
	    return ((this.fTagPosition != null) && (this.fTagPosition.getLength() > 0));
	  }

	  public static String createTagText(int index) {
	    return "/*$MLRes-" + index + "$*/";
	  }

	  public String toString()
	  {
	    return this.fPosition + ": " + this.fValue + "    Tag position: " + 
	      ((hasTag()) ? this.fTagPosition.toString() : "no tag found"); }

	  public String getArgStr() {
	    return this.argStr;
	  }

}
