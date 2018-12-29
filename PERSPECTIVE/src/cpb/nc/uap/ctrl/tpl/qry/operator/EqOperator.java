package nc.uap.ctrl.tpl.qry.operator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFLiteralDate;

public class EqOperator extends AbstractOperator {

	private static final long serialVersionUID = 79122814953707611L;

	private static final EqOperator INSTANCE = new EqOperator();

	public static EqOperator getInstance() {
		return INSTANCE;
	}

	@Override
	public int getParameterNumber() {
		return -1;
	}

	@Override
	public String getOperatorCode() {
		return getSingleValueOperatorCode();
	}

	@Override
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if (value == null || value.getFieldValues() == null
				|| value.getFieldValues().size() == 0)
			return null;
		String fieldCode = meta.getSQLFieldCode();
		if (value.getFieldValues().size() == 1) {
			IFieldValueElement fieldValueElement = value.getFieldValues()
					.get(0);
			String valueString = fieldValueElement.getSqlString();
			if(valueString == null) return null;

			if (isNumberType(meta)||isAttribute(fieldValueElement)) {
				// 库表属性作为条件值时要注意表连接式SQL的生成
				if(isAttribute(fieldValueElement)) {
					valueString = getAttrWithTableAlias(meta, valueString);
				}
				if(meta.isUserDef()){
					int dataType = meta.getDataType();
					if(dataType == IQueryConstants.DECIMAL){
						return fieldCode + " != '~' and cast(" + fieldCode + " as float)" + getSingleValueOperatorCode() + " " + valueString;  
					}else{
						return fieldCode + " != '~' and cast(" + fieldCode + " as int)" + getSingleValueOperatorCode() + " " + valueString;
					}
				}
				return fieldCode + " " + getSingleValueOperatorCode() + " " + valueString;
			}
			// 增加对日期类型的i18n处理
			else if (isDateType(meta)) {
				ICalendar beginCalendar = getCalendarBegin(fieldValueElement);
				ICalendar endCalendar = getCalendarEnd(fieldValueElement);
				ICalendar beginDate = null;
				ICalendar endDate = null;
				if(isLiteralDate(meta)){
					beginDate = UFLiteralDate.getDate(beginCalendar.toStdString());
					endDate = UFLiteralDate.getDate(endCalendar.toStdString());
				}else{
					beginDate = beginCalendar;
					endDate = endCalendar;
				}
				//UFDate或UFDateTime类型 SQL形如：date >= '2010-01-01 00:00:00' and date <= '2010-01-01 23:59:59'
				//UFLiteralDate类型 SQL形如：date >= '2010-01-01' and date <= '2010-01-01' 
				String getSql = buildGETDateSQL(fieldCode, beginDate);
				String letSql = buildLETDateSQL(fieldCode, endDate);
				String conStr = "(" + getSql + " and " + letSql;
				if(meta.isUserDef()){
					conStr += " and " + fieldCode + " != '~'";
				}
				conStr += ")";
				return conStr;
			} 
			else if (isMultiSelectionSql(valueString)) {
				return fieldCode + " " + getMultiValueOperator() + " ("
						+ valueString + ")";
			} else {
				if(isBooleanType(meta) && meta.isUserDef()){
					if("N".equals(valueString) || "n".equals(valueString)){
						return fieldCode + " " + getSingleValueOperatorCode() + " '" + valueString + "' and " +
						 	fieldCode + "= '~'";
					}
				}
				return fieldCode + " " + getSingleValueOperatorCode() + " '"
						+ valueString + "'";
			}
		} else {
			StringBuffer buf = new StringBuffer();
			List<IFieldValueElement> l = value.getFieldValues();
			for (IFieldValueElement element : l) {
				if(element==null)
					continue;
				if (isNumberType(meta)||isAttribute(element)) {
					
					buf.append(element.getSqlString()).append(",");
				} else {
					buf.append("'").append(element.getSqlString()).append("',");
				}
			}
			return fieldCode + " " + getMultiValueOperator()
					+ " (" + buf.substring(0, buf.length() - 1) + ")";
		}
	}

	public String getMultiValueOperator() {
		return IOperatorConstants.IN;
	}

	protected String getSingleValueOperatorCode() {
		return IOperatorConstants.EQ;
	}

	public boolean isMultiSelectionSql(String valueString) {

		if(valueString == null) return false;
		Pattern p = Pattern.compile("select\\s+.*from");
		Matcher m = p.matcher(valueString.toLowerCase());
		return m.find();
	}

	@Override
	public String toString() {
		return IOperatori18n.getEQ_Desc();
	}

	public String getDescription(IFilterMeta meta, IFieldValue value) {
		if (value == null || value.getFieldValues() == null
				|| value.getFieldValues().size() == 0) {
			return meta.toString();
		}

		if (value.getFieldValues().size() == 1) {
			IFieldValueElement fieldValueElement = value.getFieldValues().get(0);
			String sqlString = fieldValueElement.getSqlString();
			String showString = fieldValueElement.getShowString();
			if (isNumberType(meta)||isAttribute(fieldValueElement)||isSystemFunction(fieldValueElement)) {
				return meta.toString() + " " + toString() + " "	+ showString;
			} else if (isMultiSelectionSql(sqlString)) {
				return meta.toString() + " " + toString() + " "	+ showString;
			} else {
				return meta.toString() + " " + toString() + " '" + showString + "'";
			}

		} else {
			StringBuffer buf = new StringBuffer();
			List<IFieldValueElement> l = value.getFieldValues();
			for (IFieldValueElement element : l) {
				if(element==null)
					continue;
				if (isNumberType(meta)||isAttribute(element)) {
					buf.append(element.getShowString()).append(",");
				} else {
					buf.append("'").append(element.getShowString()).append("',");
				}
			}
			if(buf.length()==0)
				return meta.toString();
			return meta.toString() + " " + toString() + " (" + buf.substring(0, buf.length() - 1) + ")";
		}
	}
	
	public boolean isFuzzy()
	{
		return true;
	}
}
