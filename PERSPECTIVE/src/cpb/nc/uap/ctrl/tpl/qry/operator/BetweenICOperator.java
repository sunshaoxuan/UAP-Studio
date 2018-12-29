package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;


public class BetweenICOperator extends BetweenOperator{
	private static final long serialVersionUID = 1L;
	
	private static final BetweenICOperator INSTANCE = new BetweenICOperator();

	public static BetweenICOperator getInstance() {
		return INSTANCE;
	}
	
	protected String oneSideString(FilterMeta meta, IFieldValueElement value, IOperator operator) {
		String result = null;
		String valueString = value.getSqlString();
		// 库表属性作为条件值时要注意表连接式SQL的生成
		if (isAttribute(value)) {
			valueString = getAttrWithTableAlias(meta, valueString);
		} else {
			String fieldCode = meta.getSQLFieldCode();
			String operatorCode = operator.getOperatorCode();
			if(!isNumberType(meta)){
				// 增加对日期类型的i18n处理
				if (isDateType(meta) || isLiteralDate(meta)) {
					int dataType = meta.getDataType();
					OneParaOperator opOperator = (OneParaOperator)operator;
//				ICalendar date = getCalendar(value);
					valueString = "'" + opOperator.getCalendarSQLString(value,dataType) + "'";
				} else {
					valueString = "'" + valueString + "'";
				}
				result = "upper("+fieldCode+") "+operatorCode+" "+valueString.toUpperCase();
			}else if(isNumberType(meta)){
				if(meta.isUserDef()){
					int dataType = meta.getDataType();
					if(dataType == IQueryConstants.DECIMAL){
						result = fieldCode + " != '~' and cast(" + fieldCode + " as float)" + operatorCode + " " + valueString;  
					}else{
						result = fieldCode + " != '~' and cast(" + fieldCode + " as int)" + operatorCode + " " + valueString;
					}
				}else{
					result = fieldCode+" "+operatorCode+" "+valueString;
				}
				
			}else{
				result = fieldCode+" "+operatorCode+" "+valueString;
			}
		}
		return result;
	}

}
