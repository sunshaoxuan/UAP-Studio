package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;

public class GtOperator extends OneParaOperator {

	private static final long serialVersionUID = 5619782160617034857L;

	private static final GtOperator INSTANCE = new GtOperator();

	public static GtOperator getInstance() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return IOperatori18n.getGT_Desc();
	}

	@Override
	public String getOperatorCode() {
		return IOperatorConstants.GREATE;
	}

	/**
	 * �����������ͣ����ش���ĳ������Ӧ�÷�������"yyyy-MM-dd 23:59:59"��"yyyy-MM-dd"��SQL
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