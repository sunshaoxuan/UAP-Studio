package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;

public class LtOperator extends OneParaOperator {

	private static final long serialVersionUID = -8248033557547107067L;
	
	private static final LtOperator INSTANCE = new LtOperator();

	public static LtOperator getInstance() {
		return INSTANCE;
	}
	@Override
	public String toString() {
		return IOperatori18n.getLT_Desc();
	}
	@Override
	public String getOperatorCode() {
		return IOperatorConstants.LESS;
	}
	
	/**
	 * 根据数据类型，返回小于某个日期应该返回形如"yyyy-MM-dd 00:00:00"或"yyyy-MM-dd"的SQL
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
