package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;

public class LikeOperator extends OneParaOperator {

	private static final long serialVersionUID = -3312909946740685672L;
	
	private static final LikeOperator INSTANCE = new LikeOperator();

	public static LikeOperator getInstance() {
		return INSTANCE;
	}
	@Override
	public String toString() {
		return IOperatori18n.getLIKE_Desc();//"相似于";
	}
	@Override
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if(value==null||value.getFieldValues()==null||value.getFieldValues().size()<1||value.getFieldValues().get(0)==null)
			return null;
		IFieldValueElement fieldValueElement = value.getFieldValues().get(0);
		String code = getOperatorCode();
		String valueString = fieldValueElement.getSqlString();
		// 库表属性作为条件值时要注意表连接式SQL的生成
		if (isAttribute(fieldValueElement)) {
			valueString = getAttrWithTableAlias(meta, valueString);
		}else if(!isNumberType(meta))
		{
			valueString ="'%"+valueString+"%'";
		}
		return meta.getSQLFieldCode()+" "+code+" "+valueString;
	}
	@Override
	public String getOperatorCode() {
		return IOperatorConstants.LIKE;
	}
	public boolean isFuzzy()
	{
		return true;
	}
	
	@Override
	protected String getCalendarSQLString(IFieldValueElement fieldValueElement,int dataType) {
		throw new RuntimeException("Why do you use like operator with Date type?");
	}
	
	protected String getDescription(IFilterMeta meta, String opname,
			IFieldValue value) {
		if (opname == null) {
			return super.getDescription(meta, value);
		}
		if (value == null || value.getFieldValues() == null
				|| value.getFieldValues().size() < 1
				|| value.getFieldValues().get(0) == null)
			return meta.toString();
		String valueString = value.getFieldValues().get(0).getShowString();
		return meta.toString() + " " + opname + " " + valueString;
	}
}