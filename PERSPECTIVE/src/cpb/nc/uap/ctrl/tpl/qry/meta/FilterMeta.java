package nc.uap.ctrl.tpl.qry.meta;

import java.io.Serializable;
import java.util.Comparator;

import nc.uap.ctrl.tpl.qry.operator.IOperator;
import nc.uap.ctrl.tpl.qry.operator.OperatorFactory;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.jcom.lang.StringUtil;
import uap.lfw.core.ml.LfwResBundle;

public class FilterMeta implements IFilterMeta,Serializable ,Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2713896428412550665L;

	private String tableCode; // 表编码

	private String tableName; // 表名称

	private String fieldCode; // 字段编码

	private String fieldName; // 字段名称

	private String defaultValue = null;

	private int dispType;

	private int returnType; // 返回类型,具体定义见 IQueryConstants

	private boolean ifAutoCheck;

	private String valueEditorDescription; // 值编辑器的描述串.如参照编码,或-99表示非参照

	private boolean isEditable; // 是否可编辑

	private int dataType; // 数据类型
	
	private IOperator operator;
	
	private String defaultCondition;

	private String varType; //变量类型
	
	private boolean ifContainOrg = false; //是否包含组织属性
	
	private String resId; //资源ID
	
	private String langdir; //多语所在模板

	private String trueFieldCode;
	
	private boolean ifCheckBox;
	
	private IOperator[] operators;

	private boolean isFixCondition = false;

	private boolean isRequired = false;

	private boolean isDefault = false;
	
	private boolean isUserDef = false; //是否用户自定义项
	
	private boolean isDataPower = false;//是否数据权限控制
	
	private boolean isCondition = true; //是否为查询条件,默认为true,反之为逻辑条件.@since v5.3
	
	private String instrumentedsql ;
	
	private boolean isOrder = false;
	
//	private boolean isMultiLangName = false;// 是否是多语名称
	
	private transient int langSeq = 0;// 多语名称的语种号
	
	// 参照类型的where条件，只适用于代码使用，不会持久化到数据库
	private String where;
	
	private String prerestrict;// 预置限制条件
	private String guideline;// 指标条件,为双引擎服务
	    
    private boolean isSubIncluded;// 参照是否包含下级
    
    // fields below add for V6 composite ref
    private boolean isSysFuncRefUsed;// 是否使用系统函数参照
    private boolean isAttrRefUsed;// 是否使用库表属性参照
    
    private int maxlength;// 最大长度
    private int dispSequence;// 显示序号
    
    private int limit = 9999;// 数目限制

	public boolean isIfCheckBox() {
		return ifCheckBox;
	}

	public void setIfCheckBox(boolean ifCheckBox) {
		this.ifCheckBox = ifCheckBox;
	}

	public String getTrueFieldCode() {
		return trueFieldCode;
	}

	public void setTrueFieldCode(String trueFieldCode) {
		this.trueFieldCode = trueFieldCode;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}
	
	public String getLangdir() {
		return langdir;
	}

	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}

	public String getDefaultCondition() {
		return defaultCondition;
	}

	public void setDefaultCondition(String defaultCondition) {
		this.defaultCondition = defaultCondition;
	}
	
	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public IOperator getOperator() {
		return operator;
	}

	public void setOperator(IOperator operator) {
		this.operator = operator;
	}
	
	public boolean isIfContainOrg() {
		return ifContainOrg;
	}

	public void setIfContainOrg(boolean ifContainOrg) {
		this.ifContainOrg = ifContainOrg;
	}
    
	public FilterMeta() {
		super();
	}

	public int getDataType() {
		return dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public int getLangSeq() {
		return langSeq;
	}
	
	public void setLangSeq(int langSeq) {
		this.langSeq = langSeq;
	}
	
	public String getFieldCode() {
		// 特殊处理多语名称属性
		return isMultiLangName() ? fieldCode + getLangSeq() : fieldCode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public boolean getIsEditable() {
		return isEditable;
	}

	public String getTableCode() {
		return tableCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setDataType(int newDataType) {
		dataType = newDataType;
	}

	public void setDefaultValue(String newValue) {
		defaultValue = newValue;
	}

	public void setFieldCode(String newCode) {
		fieldCode = newCode;
	}

	public void setFieldName(String newName) {
		fieldName = newName;
	}

	public void setIsEditable(boolean flag) {
		isEditable = flag;
	}

	public void setTableCode(String newCode) {
		tableCode = newCode;
	}

	public void setTableName(String newName) {
		tableName = newName;
	}

	public IOperator[] getOperators() {
		return operators;
	}

	public void setOperators(IOperator[] operators) {
		this.operators = operators;
	}

	public String toString() {
		if(getTableName()!=null&&getTableName().length()>0){
			return getTableName()+"."+getFieldName();
		}
		return getFieldName();
	}

	public String getValueEditorDescription() {
		return valueEditorDescription;
	}

	public void setValueEditorDescription(String valueEditorDescription) {
		this.valueEditorDescription = valueEditorDescription;
	}

	public int getDispType() {
		return dispType;
	}

	public void setDispType(int dispType) {
		this.dispType = dispType;
	}

	public boolean getIfAutoCheck() {
		return ifAutoCheck;
	}

	public void setIfAutoCheck(boolean ifAutoCheck) {
		this.ifAutoCheck = ifAutoCheck;
	}

	public int getReturnType() {
		return returnType;
	}

	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	
	public boolean isFixCondition() {
		return isFixCondition;
	}

	public void setFixCondition(boolean isFixCondition) {
		this.isFixCondition = isFixCondition;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public boolean isDataPower() {
		return isDataPower;
	}

	public void setDataPower(boolean isDataPower) {
		this.isDataPower = isDataPower;
	}

	public boolean isUserDef() {
		return isUserDef;
	}

	public void setUserDef(boolean isUserDef) {
		this.isUserDef = isUserDef;
	}
	
	
	
	public boolean isCondition() {
		return isCondition;
	}

	public void setCondition(boolean isCondition) {
		this.isCondition = isCondition;
	}

	public String getSQLFieldCode() {
		if (getTableCode() == null || getFieldCode().indexOf(".") != -1||getTableCode().length()==0) {
			return getFieldCode();
		} else {
			return getTableCode() + "." + getFieldCode();
		}
	}

	public boolean isNumberType() {
		boolean result = false;
		if (IQueryConstants.INTEGER == getDataType()
				|| IQueryConstants.DECIMAL == getDataType()) {
			result = true;
		} else if (IQueryConstants.USERCOMBO == getDataType()) {
			if (getValueEditorDescription() != null
					&& getValueEditorDescription().startsWith(IQueryConstants.COMBO_INDEX))
				result = true;
		}

		return result;
	}

	
	@Override
	public Object clone() {
		try{
			return super.clone();
		} 
		catch (CloneNotSupportedException e){
			LfwLogger.error(e);
		}
		return null;
	}

	public String getInstrumentedsql() {
		return instrumentedsql;
	}

	public void setInstrumentedsql(String instrumentedsql) {
		this.instrumentedsql = instrumentedsql;
	}
	
	public String getInstrumentedSql(String basicSql)
	{
		if(basicSql==null||basicSql.trim().length()==0) return basicSql;
		if(getInstrumentedsql()==null||getInstrumentedsql().trim().length()==0)
		{
			return basicSql;
		}
		else
		{
			return StringUtil.replaceAllString(getInstrumentedsql(), "???", basicSql);
		}
	}

	public boolean isOrder() {
		return isOrder;
	}

	public void setOrder(boolean isOrder) {
		this.isOrder = isOrder;
	}

	public boolean isMultiLangName() {
		return (getLangSeq() != 0 && getLangSeq() != 1);
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getPrerestrict() {
		return prerestrict;
	}
	
	public void setPrerestrict(String prerestrict) {
		this.prerestrict = prerestrict;
	}
	
	public String getGuideline() {
		return guideline;
	}
	
	public void setGuideline(String guideline) {
		this.guideline = guideline;
	}
	
	public boolean isSubIncluded() {
		return isSubIncluded;
	}

	public void setSubIncluded(boolean isSubIncluded) {
		this.isSubIncluded = isSubIncluded;
	}
	
	public boolean isSysFuncRefUsed() {
		return isSysFuncRefUsed;
	}
	
	public void setSysFuncRefUsed(boolean isSysFuncRefUsed) {
		this.isSysFuncRefUsed = isSysFuncRefUsed;
	}
	
	public boolean isAttrRefUsed() {
		return isAttrRefUsed;
	}
	
	public void setAttrRefUsed(boolean isAttrRefUsed) {
		this.isAttrRefUsed = isAttrRefUsed;
	}
	
	public int getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}
	
	public int getDispSequence() {
		return dispSequence;
	}

	public void setDispSequence(int dispSequence) {
		this.dispSequence = dispSequence;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 按显示顺序排序器
	 */
	public static Comparator<FilterMeta> byDispSequence() {
		return new Comparator<FilterMeta>() {

			@Override
			public int compare(FilterMeta o1, FilterMeta o2) {
				return o1.getDispSequence() - o2.getDispSequence();
			}
		};
	}
	
	/**
	 * 返回数据权限operation编码，没有则返回null
	 */
	public String getDataPowerOperation() {
		return null;
	}

	/**
	 * 适配QueryConditionVO的构造方法
	 */
	public FilterMeta(QueryConditionVO vo,boolean isMultiTable) {
		super();
		init(vo,isMultiTable);
	}
	
	protected void init(QueryConditionVO vo,boolean isMultiTable) {
		this.setFieldCode(vo.getFieldCode());
		this.setFieldName(vo.getFieldName());
		this.setOperators(OperatorFactory.getInstance().getOperators(vo.getOperaCode()));
		this.setTableCode(vo.getTableCode());
		// 单表的情况不再处理表名称，没有什么意义
		if(isMultiTable) {
			// 特殊处理表名的多语翻译
			if (isEmpty(vo.getTableCode())) {
				this.setTableName(vo.getTableName());
			} else {
				String tableName = getTableNameByCode(vo.getTableCode(), vo.getTableName());
				this.setTableName(tableName);
			}
		}
		this.setIfAutoCheck(vo.getIfAutoCheck()==null?false:vo.getIfAutoCheck().booleanValue());
		this.setIsEditable(vo.getIfImmobility()==null?true:!vo.getIfImmobility().booleanValue());
		this.setDispType(vo.getDispType());
		this.setReturnType(vo.getReturnType());
		this.setValueEditorDescription(vo.getConsultCode());
		this.setDataType(vo.getDataType());
		this.setDefaultValue(vo.getValue());
		this.setFixCondition(vo.getIfImmobility()==null?false:vo.getIfImmobility().booleanValue());
		this.setRequired(vo.getIfMust()==null?false:vo.getIfMust().booleanValue());
		this.setDefault(vo.getIfDefault()==null?false:vo.getIfDefault().booleanValue());
		this.setDataPower(vo.getIfDataPower().booleanValue());
		this.setUserDef(vo.getUserDef());
		this.setCondition(vo.getIsCondition()==null?true:vo.getIsCondition().booleanValue());
		this.setInstrumentedsql(vo.getInstrumentedsql());
		this.setOrder(vo.getIfOrder().booleanValue());
		this.setWhere(vo.getWhere());
		this.setPrerestrict(vo.getPrerestrict());
		this.setGuideline(vo.getGuideline());
		this.setSubIncluded(vo.getIsSubIncluded()==null?true:vo.getIsSubIncluded().booleanValue());
		this.setSysFuncRefUsed(vo.getIsSysFuncRefUsed()==null?false:vo.getIsSysFuncRefUsed().booleanValue());
		this.setAttrRefUsed(vo.getIsAttrRefUsed()==null?false:vo.getIsAttrRefUsed().booleanValue());
		this.setMaxlength(vo.getMaxLength()==null?1000:vo.getMaxLength());
		this.setDispSequence(vo.getDispSequence()==null?1000:vo.getDispSequence());
		this.setLimit(vo.getLimit() == null ? 9999 : vo.getLimit());
	}
	
	/**
	 * 根据表编码得到对应的表名称(多语)，如果没有进行多语注册则返回参数tableName
	 */
	private static String getTableNameByCode(String tableCode,String tableName) {
		// 根据数字字典的多语文件进行翻译
		return LfwResBundle.getInstance().getString("datadict", tableName,"D" + tableCode);
	}
	
	private static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}
}