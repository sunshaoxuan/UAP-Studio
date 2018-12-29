package nc.uap.ctrl.tpl.qry.operator;

import java.util.List;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFLiteralDate;

public class EqICOperator extends EqOperator{

	private static final long serialVersionUID = 1L;
	
	private static final EqICOperator INSTANCE = new EqICOperator();
	
	public static EqICOperator getInstance(){
		return INSTANCE;
	}
	
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if (value == null || value.getFieldValues() == null
				|| value.getFieldValues().size() == 0)
			return null;
		String fieldCode = meta.getSQLFieldCode();
		if (value.getFieldValues().size() == 1) {
			IFieldValueElement fieldValueElement = value.getFieldValues().get(0);
			String valueString = fieldValueElement.getSqlString();
			if (isNumberType(meta) || isAttribute(fieldValueElement)) {
				// 库表属性作为条件值时要注意表连接式SQL的生成
				if(isAttribute(fieldValueElement)) {
					valueString = getAttrWithTableAlias(meta, valueString);
				}
				return fieldCode + " " + getSingleValueOperatorCode() + " " + valueString;
			} 
			// 库表属性作为条件值时要注意表连接式SQL的生成
			else if (isAttribute(fieldValueElement)) {
				return fieldCode + " " + getSingleValueOperatorCode() + " "
						+ getAttrWithTableAlias(meta, valueString);
			}
			// 增加对日期类型的i18n处理
			else if (isDateType(meta)) {
				ICalendar beginDate = getCalendarBegin(fieldValueElement);
				ICalendar endDate = getCalendarEnd(fieldValueElement);
				// SQL形如：date >= '2010-01-01 00:00:00' and date <= '2010-01-01 23:59:59'
				String getSql = buildGETDateSQL(fieldCode, beginDate);
				String letSql = buildLETDateSQL(fieldCode, endDate);
				return "(" + getSql + " and " + letSql + ")";
			}
			else if (isLiteralDate(meta)){
				ICalendar beginDate = getCalendarBegin(fieldValueElement);
				ICalendar endDate = getCalendarEnd(fieldValueElement);
				UFLiteralDate beginLiteralDate = UFLiteralDate.getDate(beginDate.toStdString());
				UFLiteralDate endLiteralDate = UFLiteralDate.getDate(endDate.toStdString());
				// SQL形如：date >= '2010-01-01' and date <= '2010-01-01'
				String getSql = buildGETDateSQL(fieldCode, beginLiteralDate);
				String letSql = buildLETDateSQL(fieldCode, endLiteralDate);
				return "(" + getSql + " and " + letSql + ")";
			}
			else if (isMultiSelectionSql(valueString)) {
				return fieldCode + " " + getMultiValueOperator()
						+ " (" + valueString + ")";
			} else {
				return "upper("+fieldCode + ") "
						+ getSingleValueOperatorCode() + " '" + valueString.toUpperCase()
						+ "'";
			}

		} else {
			StringBuffer buf = new StringBuffer();
			List<IFieldValueElement> l = value.getFieldValues();
			for (IFieldValueElement element : l) {
				if (isNumberType(meta) || isAttribute(element)) {
					buf.append(element.getSqlString()).append(",");
				} else {
					buf.append("'").append(element.getSqlString()).append("',");
				}
			}
			if(isNumberType(meta))
				return fieldCode + " " + getMultiValueOperator()
					+ " (" + buf.substring(0, buf.length() - 1) + ")";
			else
				return "upper("+fieldCode + ") " + getMultiValueOperator()
				+ " (" + buf.substring(0, buf.length() - 1).toUpperCase() + ")";
		}
	}
}
