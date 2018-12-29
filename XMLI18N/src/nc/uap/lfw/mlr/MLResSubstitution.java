package nc.uap.lfw.mlr;

import nc.uap.lfw.lang.M_mlr;
import nc.uap.lfw.tool.*;
import nc.uap.lfw.wizard.PageNode;

/**
 * 多语资源替换类
 * 
 * @author dingrf
 *
 */
public class MLResSubstitution{

	/**多语元素*/
	private MLResElement element;
	
	/**多语模块*/
	private String langModule;
	
	/**显示值*/
	private String value;
	
	/**中文值*/
	private String simpValue;
	
	/**英文值*/
	private String englishValue;
	
	/**中文繁体*/
	private String twValue;
	
	/**多语key*/
	private String key;
	
	/**选中内容*/
	private String selectKey;
	
	/**原始内容*/
	private String oldKey;
	
	/**已存在key*/
	private String extKey;
	
	/**使用公共资源key*/
	private String commKey;
	
	/**已存在多语模块*/
	private String extLangModule;
	
	/**key值前缀*/
	private String prefix;
	
	/**状态  0：新增，1：更新，2：删除，3：公共资源,4：错误,5:警告,6:修复*/
	private int state;
	
	/**资源文件*/
	private MLRes simpchnRes;
	private MLRes tradchnRes;
	private MLRes englishRes;
	
	/**文件id*/
	private String filePath;
	
	/** 资源文件名称*/
	private String resFileName;
	
	/**PageNode*/
	private PageNode pageNode;
	
	public MLResSubstitution(MLResElement element, String value){
		langModule = ""; //$NON-NLS-1$
		key = ""; //$NON-NLS-1$
		extKey = ""; //$NON-NLS-1$
		extLangModule = ""; //$NON-NLS-1$
		prefix = M_mlr.MLResSubstitution_4;
		state = 0;
		this.element = element;
		this.value = value;
		this.simpValue = value;
		twValue = ISNConvert.gbToBig5(value);
		englishValue = value;
	}

	public String getValue(){
		return value;
	}

	public String getKey(){
		return key;
	}

	public String getKeyWithPrefix(){
		return prefix + key;
	}
	
	/**
	 * 根据状态取MLResSubstitution的key值
	 * @return
	 */
	public String getRealKey(){
		if (state == 0 || state == 6||state == 7)
			return getKeyWithPrefix();
		if (state == 1)
			return getExtKey();
		if (state == 3)
			return getCommKey();
		else
			return ""; //$NON-NLS-1$
	}

	public int getState(){
		return state;
	}

	public void setValue(String value){
		this.value = value;
	}

	public void setKey(String key){
		this.key = key;
	}

	public void setState(int state){
		if (Helper.isEmptyString(extKey) && state == 1)
			state = 2;
		this.state = state;
	}

	public MLResElement getElement(){
		return element;
	}

	public void setPrefix(String prefix){
		this.prefix = prefix;
	}

	public String getPrefix(){
		return this.prefix;
	}
	
	public String getLangModule(){
		return langModule;
	}

	public String getRealLangModule(){
		if (state == 0 || state == 5 || state == 6)
			return getLangModule();
		if (state == 1)
			return extLangModule;
		else
			return ""; //$NON-NLS-1$
	}

	public void setLangModule(String langModule){
		this.langModule = langModule;
	}

	public String getExtKey(){
		return extKey;
	}

	public void setExtKey(String extKey){
		this.extKey = extKey;
	}

	public String getCommKey() {
		return commKey;
	}

	public void setCommKey(String commKey) {
		this.commKey = commKey;
	}

	public String getExtLangModule(){
		return extLangModule;
	}

	public void setExtLangModule(String extLangModule){
		this.extLangModule = extLangModule;
	}
	

	public String getSimpValue() {
		return simpValue;
	}

	public void setSimpValue(String simpValue) {
		this.simpValue = simpValue;
	}

	public String getEnglishValue(){
			return englishValue;
	}

	public void setEnglishValue(String englishValue){
		this.englishValue = englishValue;
	}

	public String getTwValue(){
			return twValue;
	}

	public void setTwValue(String twValue){
		this.twValue = twValue;
	}

	public MLRes getSimpchnRes() {
		return simpchnRes;
	}

	public void setSimpchnRes(MLRes simpchnRes) {
		this.simpchnRes = simpchnRes;
//		this.value = simpchnRes.getValue(); 
		this.simpValue = simpchnRes.getValue();
	}

	public MLRes getTradchnRes() {
		return tradchnRes;
	}

	public void setTradchnRes(MLRes tradchnRes) {
		this.tradchnRes = tradchnRes;
		this.twValue = tradchnRes.getValue();
	}

	public MLRes getEnglishRes() {
		return englishRes;
	}

	public void setEnglishRes(MLRes englishRes) {
		this.englishRes = englishRes;
		this.englishValue = englishRes.getValue();
	}
	
	public MLRes getNotNullResByLangCode(String langCode){
		MLRes res = null;
		if (M_mlr.MLResSubstitution_7.equals(langCode) && simpchnRes != null)
			res = simpchnRes;
		else if (M_mlr.MLResSubstitution_0.equals(langCode) && tradchnRes != null)
			res = tradchnRes;
		else if (M_mlr.MLResSubstitution_9.equals(langCode) && englishRes != null)
			res = englishRes;
		
		if (res == null)
			if (simpchnRes != null)
				res = simpchnRes;
			else if (tradchnRes != null)
				res = tradchnRes;
			else if (englishRes != null)
				res = englishRes;
		return res;
	}
	
	/**
	 * 是否已修改
	 * 
	 * @return
	 */
	public boolean hasModifyed(){
		return simpchnUpdated() || tradchnUpdated() || englishUpdated();
	}
	
	/**
	 * 是否更新简体中文
	 * 
	 * @return
	 */
	public boolean simpchnUpdated(){
		return simpchnRes == null || !simpchnRes.getValue().equals(value);
	}

	/**
	 * 是否更新繁体中文
	 * 
	 * @return
	 */
	public boolean tradchnUpdated(){
		return tradchnRes == null || !tradchnRes.getValue().equals(this.twValue);
	}

	/**
	 * 是否更新英文
	 * 
	 * @return
	 */
	public boolean englishUpdated(){
		return englishRes == null || !englishRes.getValue().equals(this.englishValue);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public PageNode getPageNode() {
		return pageNode;
	}

	public void setPageNode(PageNode pageNode) {
		this.pageNode = pageNode;
	}

	public String getSelectKey() {
		return selectKey;
	}

	public void setSelectKey(String selectKey) {
		this.selectKey = selectKey;
	}

	public String getOldKey() {
		return oldKey;
	}

	public void setOldKey(String oldKey) {
		this.oldKey = oldKey;
	}
	
}
