package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;

public class BetweenOperator extends AbstractOperator {

	private static final long serialVersionUID = 750489721195625824L;
	
	private static final BetweenOperator INSTANCE = new BetweenOperator();

	public static BetweenOperator getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String toString() {
		return IOperatori18n.getBetween_Desc();
	}
	public int getParameterNumber() {
		return 2;
	}
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if(value==null||value.getFieldValues()==null||value.getFieldValues().size()<1)
			return null;
		String result = null;
		if(value.getFieldValues().size()==1 && value.getFieldValues().get(0)!=null)
		{
			result = leftSideString(meta, value.getFieldValues().get(0));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)!=null
				&&value.getFieldValues().get(1)==null)
		{
			result = leftSideString(meta,value.getFieldValues().get(0));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)==null
				&&value.getFieldValues().get(1)!=null)
		{
			result = rightSideString(meta,value.getFieldValues().get(1));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)!=null
				&&value.getFieldValues().get(1)!=null)
		{
			result = "("+leftSideString(meta, value.getFieldValues().get(0)) +
			" and " + rightSideString(meta,value.getFieldValues().get(1))
			+")";
		}
		return result;
	}
	private String rightSideString(FilterMeta meta, IFieldValueElement value) {
		return oneSideString(meta, value,  LetOperator.getInstance());
	}
	private String leftSideString(FilterMeta meta, IFieldValueElement value) {
		return oneSideString(meta, value, GetOperator.getInstance());
	}
	protected String oneSideString(FilterMeta meta, IFieldValueElement value, IOperator operator) {
		String result = null;
		String valueString = value.getSqlString();
		String fieldCode = meta.getSQLFieldCode();
		// 库表属性作为条件值时要注意表连接式SQL的生成
		if (isAttribute(value)) {
			valueString = getAttrWithTableAlias(meta, valueString);
		} else if(!isNumberType(meta)){
			// 增加对日期类型的i18n处理
			if (isDateType(meta) || isLiteralDate(meta)) {
				int dataType = meta.getDataType();
				OneParaOperator opOperator = (OneParaOperator)operator;
//				ICalendar date = getCalendar(value);
				valueString = "'" + opOperator.getCalendarSQLString(value,dataType) + "'";
				if(meta.isUserDef()){
					return fieldCode + " " + operator.getOperatorCode() + " " + valueString + " and " + fieldCode + " != '~'";
				}
			} else if(isBooleanType(meta) && meta.isUserDef()){
				if("N".equals(valueString) || "n".equals(valueString)){
					return fieldCode+" "+operator.getOperatorCode()+" "+valueString + " and " + fieldCode + " = '~'";
				}
			}
			else {
				valueString = "'" + valueString + "'";
			}
		} else if(isNumberType(meta)){
			if(meta.isUserDef()){
				int dataType = meta.getDataType();
				if(dataType == IQueryConstants.DECIMAL){
					return fieldCode + " != '~' and cast(" + fieldCode + " as float)" + operator.getOperatorCode() + " " + valueString;  
				}else{
					return fieldCode + " != '~' and cast(" + fieldCode + " as int)" + operator.getOperatorCode() + " " + valueString;
				}
			}
		}
		result = fieldCode+" "+operator.getOperatorCode()+" "+valueString;
		return result;
	}
	public String getOperatorCode() {
		
		return IOperatorConstants.BETWEEN;
	}

	public String getDescription(IFilterMeta meta, IFieldValue value) {
		if(value==null||value.getFieldValues()==null||value.getFieldValues().size()<1)
			return meta.toString();
		String result = null;
		if(value.getFieldValues().size()==1&&value.getFieldValues().get(0)!=null)
		{
			result = leftSideDescription(meta, value.getFieldValues().get(0));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)!=null
				&&value.getFieldValues().get(1)==null)
		{
			result = leftSideDescription(meta,value.getFieldValues().get(0));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)==null
				&&value.getFieldValues().get(1)!=null)
		{
			result = rightSideDescription(meta,value.getFieldValues().get(1));
		}
		else if(value.getFieldValues().size()>1
				&&value.getFieldValues().get(0)!=null
				&&value.getFieldValues().get(1)!=null)
		{
			result = meta.toString() + " " + toString() + " [" + value.getFieldValues().get(0).getShowString() + " ,"
					+ value.getFieldValues().get(1).getShowString() + "]";
		}
		return result;		
	}
	
	private String leftSideDescription(IFilterMeta meta, IFieldValueElement value) {
		return oneSideDescription(meta, value, new GetOperator());
	}
	private String rightSideDescription(IFilterMeta meta, IFieldValueElement value) {
		return oneSideDescription(meta, value, new LetOperator());
	}
	private String oneSideDescription(IFilterMeta meta, IFieldValueElement value, IOperator operator) {
		String result = null;
		String valueString = value.getShowString();
		// 库表属性作为条件值时要注意表连接式SQL的生成
		if (isAttribute(value)) {
			valueString = getAttrWithTableAlias((FilterMeta) meta, valueString);
		} else if(!isNumberType(meta))
		{
			valueString ="'"+valueString+"'";
		}
		result = meta.toString()+" "+operator.toString()+" "+valueString;
		return result;
	}
}
