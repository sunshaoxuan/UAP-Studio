package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;


public class LetOperator extends OneParaOperator {

	private static final long serialVersionUID = -5191289628896157070L;
	
	private static final LetOperator INSTANCE = new LetOperator();

	public static LetOperator getInstance() {
		return INSTANCE;
	}
	
	@Override
	public String toString() {
		return IOperatori18n.getLE_Desc();//小于等于
	}
	@Override
	public String getOperatorCode() {
		return IOperatorConstants.LE;
	}
	
	/**
	 * 根据数据类型，返回小于等于某个日期应该返回形如"yyyy-MM-dd 23:59:59"或"yyyy-MM-dd"的SQL
	 */
	protected String getCalendarSQLString(IFieldValueElement fieldValueElement,int dataType) {
		ICalendar date = getCalendarEnd(fieldValueElement);
		UFDate ufDate = null;
		if(date instanceof UFDate){
			ufDate = (UFDate)date;
		}else if(date instanceof UFDateTime){
			ufDate = ((UFDateTime)date).getDate();
		}
		if(ufDate!=null){
			String dateStr = getDateEnd(ufDate);
			if(dataType == IQueryConstants.LITERALDATE){
				return UFLiteralDate.getDate(dateStr).toStdString();
			}else{
				return dateStr;
			}
		}
		return null;
	}
}
