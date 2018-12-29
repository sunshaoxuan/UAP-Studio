package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;

public class GetOperator extends OneParaOperator {

	private static final long serialVersionUID = 5330194410363033160L;
	
	private static final GetOperator INSTANCE = new GetOperator();

	public static GetOperator getInstance() {
		return INSTANCE;
	}
	
	
	@Override
	public String toString() {
		return IOperatori18n.getGE_Desc();
	}
	@Override
	public String getOperatorCode() {
		return IOperatorConstants.GE;
	}
	
	/**
	 * 根据数据类型返回，大于等于某个日期应该返回形如"yyyy-MM-dd 00:00:00"或"yyyy-MM-dd"的SQL
	 */
	protected String getCalendarSQLString(IFieldValueElement fieldValueElement,int dataType) {
		ICalendar date = getCalendarBegin(fieldValueElement);
		UFDate ufDate = null;
		if(date instanceof UFDate){
			ufDate = (UFDate)date;
		}else if(date instanceof UFDateTime){
			ufDate = ((UFDateTime)date).getDate();
		}
		if(ufDate!=null){
			String dateStr = getDateBegin(ufDate);
			if(dataType == IQueryConstants.LITERALDATE){
				return UFLiteralDate.getDate(dateStr).toStdString();
			}else{
				return dateStr;
			}
		}
		return null;
	}
}
