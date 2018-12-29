package nc.uap.lfw.multilang;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ISNConvert;
import nc.uap.lfw.tool.UTFProperties;

public class MLResSubstitution {
	public static final int EXTERNALIEING = 0;
	public static final int EXTERNALIZED = 1;
	public static final int IGNORED = 2;
	public static final int STATE_COUNT = 3;
	private MLResElement element = null;
	private String langModule = "";
	private String value = null;
	private String englishValue = null;
	private String twValue = null;
	private String key = "";
	private String extKey = "";
	private String extLangModule = "";
	private String prefix = "prefix";
	private int state = 0;

	/** PageNode */
	private PageNode pageNode;
	

	public MLResSubstitution(MLResElement element, String value) {
		this.element = element;
		this.value = value;
		this.englishValue = value;
		this.twValue = ISNConvert.gbToBig5(value);
	}

	public String getValue() {
		return this.value;
	}

	public String getKey() {
		return this.key;
	}

	public String getKeyWithPrefix() {
		return this.prefix + this.key;
	}

	public String getRealKey() {
		if (this.state == 0)
			return getKeyWithPrefix();
		if (this.state == 1)
			return getExtKey();

		return "";
	}

	public int getState() {
		return this.state;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setState(int state) {
		if ((Helper.isEmptyString(this.extKey)) && (state == 1))
			state = 2;

		this.state = state;
	}

	public MLResElement getElement() {
		return this.element;
	}



	public String createKey(MLResSubstitution[] subs, UTFProperties prop) {
		List keyList = new ArrayList();
		if (prop != null)
			keyList.addAll(prop.keySet());
		int count = (subs == null) ? 0 : subs.length;
		for (int i = 0; i < count; ++i) {
			MLResSubstitution substitution = subs[i];
			if (substitution == this)
				continue;
			if (substitution.getState() == 0) {
				keyList.add(substitution.getKeyWithPrefix());
			}
		}
		this.key = "000000";
		int i = 0;
		for (; keyList.contains(getKeyWithPrefix());)
			this.key = createKey(i++);
		return this.key;
	}

	private String createKey(int counter) {
		String temp = "000000";
		String str = String.valueOf(counter);
		if (str.length() > temp.length())
			return str.substring(str.length() - temp.length());

		return temp.substring(0, temp.length() - str.length()) + str;
	}

	public String getLangModule() {
		return this.langModule;
	}

	public String getRealLangModule() {
		if (this.state == 0)
			return getLangModule();
		if (this.state == 1)
			return this.extLangModule;

		return "";
	}

	public void setLangModule(String langModule) {
		this.langModule = langModule;
	}

	public String getExtKey() {
		return this.extKey;
	}

	public void setExtKey(String extKey) {
		this.extKey = extKey;
	}

	public String getExtLangModule() {
		return this.extLangModule;
	}

	public void setExtLangModule(String extLangModule) {
		this.extLangModule = extLangModule;
	}

	public String getEnglishValue() {
		if ((this.state == 2) || (this.state == 1))
			return "";

		return this.englishValue;
	}

	public void setEnglishValue(String englishValue) {
		this.englishValue = englishValue;
	}

	public String getTwValue() {
		if ((this.state == 2) || (this.state == 1))
			return "";

		return this.twValue;
	}

	public void setTwValue(String twValue) {
		this.twValue = twValue;
	}

	public PageNode getPageNode() {
		return pageNode;
	}

	public void setPageNode(PageNode pageNode) {
		this.pageNode = pageNode;
	}


	public String getPrefix(){
		return this.prefix;
	}
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
}