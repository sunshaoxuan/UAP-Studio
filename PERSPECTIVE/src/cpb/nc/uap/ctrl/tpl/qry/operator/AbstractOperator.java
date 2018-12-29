package nc.uap.ctrl.tpl.qry.operator;

import nc.uap.ctrl.tpl.qry.base.DateInterval;
import nc.uap.ctrl.tpl.qry.meta.FilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IAliasableFilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IFieldValue;
import nc.uap.ctrl.tpl.qry.meta.IFieldValueElement;
import nc.uap.ctrl.tpl.qry.meta.IFilterMeta;
import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;
import nc.uap.ctrl.tpl.qry.meta.RefResultVO;
import nc.uap.ctrl.tpl.qry.sysfunc.SystemFunction;
import nc.uap.ctrl.tpl.qry.value.Attribute;
import nc.vo.pub.lang.ICalendar;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFLiteralDate;

public abstract class AbstractOperator implements IOperator {
	
	private static final long serialVersionUID = -5191289628896157070L;
	
	public int getParameterNumber() {
		return 0;
	}

	public String getSQLString(FilterMeta meta, IFieldValue value) {
		return null;
	}

	public abstract String getOperatorCode();
	
	/**
	 * 判断meta是否是数值类型
	 */
	protected boolean isNumberType(IFilterMeta meta)
	{
		return meta.isNumberType();
	}
	
	/**
	 * 判断meta是否是日期或时间类型
	 */
	protected boolean isDateType(FilterMeta meta) {
		return IQueryConstants.DATE == meta.getDataType() || IQueryConstants.LITERALDATE == meta.getDataType() || IQueryConstants.TIME == meta.getDataType();
	}
	
	/**
	 * 判断meta是否是字面日期类型
	 */
	protected boolean isLiteralDate(FilterMeta meta){
		return IQueryConstants.LITERALDATE == meta.getDataType();
	}
	
	protected boolean isBooleanType(FilterMeta meta){
		return IQueryConstants.BOOLEAN == meta.getDataType();
	}
	
	/**
	 * IFieldValueElement包含的对象是否是系统函数SystemFunction
	 */
	protected boolean isSystemFunction(IFieldValueElement value) {
		return value == null ? false : value.getValueObject() instanceof SystemFunction;
	}
	
	/**
	 * IFieldValueElement包含的对象是否是库表属性Attribute
	 */
	protected boolean isAttribute(IFieldValueElement value) {
		return value == null ? false : value.getValueObject() instanceof Attribute;
	}
	
	/**
	 * 返回FilterMeta对应的表别名，主要为库表属性生成表连接式SQL使用
	 * 使得库表属性作为查询值时仍然能够获取表别名以便拼接上以保证SQL正确性
	 */
	protected String getTableAlias(FilterMeta meta) {
		return ((IAliasableFilterMeta) meta).getTableAlias();
	}
	
	/**
	 * 返回带表别名的库表属性
	 * <p>
	 * 库表属性作为条件值时要注意表连接式SQL的生成
     * <p>
	 * 算法描述如下:
	 * 1，先根据FilterMeta(实际上是fieldCode)得到表别名
     * fieldCode只有两种情况：有表别名的和无表别名的
	 * 2，如果fieldCode没有表别名，则库表属性也不用表别名
	 * 3，如果fieldCode有表别名，则将其表别名也加到库表属性上
	 * 因为fieldCode对应的属性和库表属性肯定都属于是同一个实体
	 * 
	 * @param meta
	 *            FilterMeta
	 * @param attr
	 *            库表属性
	 */
	protected String getAttrWithTableAlias(FilterMeta meta, String attr) {
		String alias = getTableAlias(meta);
		return alias == null ? attr : alias + "." + attr;
	}
	
	public boolean isFuzzy()
	{
		return false;
	}
	
	protected ICalendar getCalendarBegin(IFieldValueElement fve) {
		Object valueObject = fve.getValueObject();
		ICalendar calendar = null;
		if (valueObject instanceof SystemFunction) {
			RefResultVO functionValue = ((SystemFunction) valueObject).getFunctionValue();
			DateInterval dateInterval = (DateInterval) functionValue.getRefObj();
			calendar = dateInterval.getBeginDate();
		} else if (valueObject instanceof ICalendar){
			calendar = (ICalendar) valueObject;
		}
		return calendar;
	}
	
	protected ICalendar getCalendarEnd(IFieldValueElement fve) {
		Object valueObject = fve.getValueObject();
		ICalendar calendar = null;
		if (valueObject instanceof SystemFunction) {
			RefResultVO functionValue = ((SystemFunction) valueObject).getFunctionValue();
			DateInterval dateInterval = (DateInterval) functionValue.getRefObj();
			calendar = dateInterval.getEndDate();
		} else if (valueObject instanceof ICalendar){
			calendar = (ICalendar) valueObject;
		}
		return calendar;
	}
	
	/**
	 * 返回UFDate对应的基准时区开始时间，如 "yyyy-MM-dd 00:00:00"
	 */
	protected String getDateBegin(UFDate date) {
		return date.asLocalBegin().toString();
	} 
	
	/**
	 * 返回UFDate对应的基准时区结束时间，如 "yyyy-MM-dd 00:00:00"
	 */
    protected String getDateEnd(UFDate date){
		return date.asLocalEnd().toString();
	} 
    
    /**
	 * 创建大于等于指定日期的SQL，如"date >= 'yyyy-MM-dd 00:00:00'"
	 * 
	 * @param fieldCode
	 *            字段编码
	 * @param calendar
	 *            日期或时间
	 */
	protected String buildGETDateSQL(String fieldCode, ICalendar calendar) {
		String value = null;
		if (calendar instanceof UFDate) {
			value = getDateBegin((UFDate) calendar);
		} else if (calendar instanceof UFDateTime) {
			value = getDateBegin(((UFDateTime)calendar).getDate());
		}else if (calendar instanceof UFLiteralDate){
			value = ((UFLiteralDate)calendar).toStdString();
		}
		return fieldCode + " " + IOperatorConstants.GE + " '" + value + "'";
	}

	/**
	 * 创建大于指定日期的SQL，如"date > 'yyyy-MM-dd 23:59:59'"
	 * 
	 * @param fieldCode
	 *            字段编码
	 * @param calendar
	 *            日期或时间           
	 */
	protected String buildGTDateSQL(String fieldCode, ICalendar calendar) {
		String value = null;
		if (calendar instanceof UFDate) {
			value = getDateEnd((UFDate) calendar);
		} else if (calendar instanceof UFDateTime) {
			value = ((UFDateTime) calendar).toStdString();
		} else if (calendar instanceof UFLiteralDate){
			value = ((UFLiteralDate)calendar).toStdString();
		}
		return fieldCode + " " + IOperatorConstants.GREATE + " '" + value + "'";
	}

	/**
	 * 创建小于等于指定日期的SQL，如"date <= 'yyyy-MM-dd 23:59:59'"
	 * 
	 * @param fieldCode
	 *            字段编码
	 * @param calendar
	 *            日期或时间   
	 */
	protected String buildLETDateSQL(String fieldCode, ICalendar calendar) {
		String value = null;
		if (calendar instanceof UFDate) {
			value = getDateEnd((UFDate) calendar);
		} else if (calendar instanceof UFDateTime) {
			value = getDateEnd(((UFDateTime)calendar).getDate());
		} else if (calendar instanceof UFLiteralDate){
			value = ((UFLiteralDate)calendar).toStdString();
		}
		return fieldCode + " " + IOperatorConstants.LE + " '" + value + "'";
	}

	/**
	 * 创建小于指定日期的SQL，如"date < 'yyyy-MM-dd 00:00:00'"
	 * 
	 * @param fieldCode
	 *            字段编码
	 * @param calendar
	 *            日期或时间
	 */
	protected String buildLTDateSQL(String fieldCode, ICalendar calendar) {
		String value = null;
		if (calendar instanceof UFDate) {
			value = getDateBegin((UFDate) calendar);
		} else if (calendar instanceof UFDateTime) {
			value = ((UFDateTime) calendar).toStdString();
		} else if (calendar instanceof UFLiteralDate){
			value = ((UFLiteralDate)calendar).toStdString();
		}
		return fieldCode + " " + IOperatorConstants.LESS + " '" + value + "'";
	}
}