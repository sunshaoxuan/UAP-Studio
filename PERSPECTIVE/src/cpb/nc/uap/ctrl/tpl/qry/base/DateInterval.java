package nc.uap.ctrl.tpl.qry.base;

import nc.vo.pub.lang.UFDate;

/**
 * 日期范围
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2011-6-1
 */
public class DateInterval {

	private UFDate beginDate;
	private UFDate endDate;

	public DateInterval() {
		super();
	}

	/**
	 * @param beginDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 */
	public DateInterval(UFDate beginDate, UFDate endDate) {
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	/**
	 * 起始日期
	 */
	public UFDate getBeginDate() {
		return beginDate;
	}

	/**
	 * 起始日期
	 */
	public void setBeginDate(UFDate beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * 结束日期
	 */
	public UFDate getEndDate() {
		return endDate;
	}

	/**
	 * 结束日期
	 */
	public void setEndDate(UFDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "[" + getDateString(beginDate) + "," + getDateString(endDate)
				+ "]";
	}

	/**
	 * 返回起始日期和结束日期之间的天数
	 */
	public int getDays() {
		return UFDate.getDaysBetween(beginDate, endDate);
	}
	
	private String getDateString(UFDate date) {
		return date == null ? null : date.toLocalString();
	}
}