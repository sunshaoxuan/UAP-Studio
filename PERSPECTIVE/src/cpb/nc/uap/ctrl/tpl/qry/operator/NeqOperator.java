package nc.uap.ctrl.tpl.qry.operator;

import java.util.List;

import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFLiteralDate;

import org.apache.commons.lang.StringUtils;


public class NeqOperator extends EqOperator {

	private static final long serialVersionUID = -1976570665432108547L;
	
	private static final NeqOperator INSTANCE = new NeqOperator();
	
	
	public static NeqOperator getInstance() {
		return INSTANCE;
	}
	

	@Override
	public int getParameterNumber() {
		return -1;
	}
	
	public String getMultiValueOperator() {
		return IOperatorConstants.NIN;
	}
	
	protected String getSingleValueOperatorCode() {
		return IOperatorConstants.NEQ;
	}
	
	@Override
	public String toString() {	
		return IOperatori18n.getNEQ_Desc();
	}

	@Override
	public String getOperatorCode() {
		return getSingleValueOperatorCode();
	}
	
	public String getSQLString(FilterMeta meta, IFieldValue value) {
		if (value == null || value.getFieldValues() == null
				|| value.getFieldValues().size() == 0)
			return null;
		String fieldCode = meta.getSQLFieldCode();
		if (value.getFieldValues().size() == 1) {
			IFieldValueElement fieldValueElement = value.getFieldValues().get(0);
			String valueString = fieldValueElement.getSqlString();
			if(StringUtils.isBlank(valueString)){
				return null;
			}
			if (isNumberType(meta) || isAttribute(fieldValueElement)) {
				// 库表属性作为条件值时要注意表连接式SQL的生成
				if(isAttribute(fieldValueElement)) {
					valueString = getAttrWithTableAlias(meta, valueString);
				}
				return "("+fieldCode + " "
						+ getSingleValueOperatorCode() + " " + valueString+" or "+"cast(" + fieldCode + " as char) = '~')";
			} 
			// 增加对日期类型的i18n处理
			else if (isDateType(meta)) {
				ICalendar beginDate = getCalendarBegin(fieldValueElement);
				ICalendar endDate = getCalendarEnd(fieldValueElement);
				// SQL形如：date < '2010-01-01 00:00:00' or date > '2010-01-01 23:59:59'
				String ltSql = buildLTDateSQL(fieldCode, beginDate);
				String gtSql = buildGTDateSQL(fieldCode, endDate);
				return "(" + ltSql + " or " + gtSql + ")";
			}
			else if (isLiteralDate(meta)){
				ICalendar beginDate = getCalendarBegin(fieldValueElement);
				ICalendar endDate = getCalendarEnd(fieldValueElement);
				UFLiteralDate beginLiteralDate = UFLiteralDate.getDate(beginDate.toStdString());
				UFLiteralDate endLiteralDate = UFLiteralDate.getDate(endDate.toStdString());
				// SQL形如：date < '2010-01-01' or date > '2010-01-01'
				String ltSql = buildLTDateSQL(fieldCode, beginLiteralDate);
				String gtSql = buildGTDateSQL(fieldCode, endLiteralDate);
				return "(" + ltSql + " or " + gtSql + ")";
			}
			else if (isMultiSelectionSql(valueString)) {
				return "("+fieldCode + " " + getMultiValueOperator()
						+ " (" + valueString + ")"+" or "+fieldCode+"='~')";
			} else {
				return "("+fieldCode + " "
						+ getSingleValueOperatorCode() + " '" + valueString
						+ "'"+" or "+fieldCode+"='~')";
			}

		} else {
			StringBuffer buf = new StringBuffer();
			List<IFieldValueElement> l = value.getFieldValues();
			for (IFieldValueElement element : l) {
				if(element==null)
					continue;
				if (isNumberType(meta) || isAttribute(element)) {
					buf.append(element.getSqlString()).append(",");
				} else {
					buf.append("'").append(element.getSqlString()).append("',");
				}
			}
			String isnullsql = " "+fieldCode+"='~'";
			if(isNumberType(meta)) {
				isnullsql = " cast(" + fieldCode + " as char) = '~'";
			}
			return "("+fieldCode + " " + getMultiValueOperator()
					+ " (" + buf.substring(0, buf.length() - 1) + ")"+" or "+isnullsql+")";
		}
	}
}
