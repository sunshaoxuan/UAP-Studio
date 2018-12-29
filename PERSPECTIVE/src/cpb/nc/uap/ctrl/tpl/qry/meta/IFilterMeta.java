package nc.uap.ctrl.tpl.qry.meta;

public interface IFilterMeta {

	/**
	 * 字段编码
	 */
	public String getFieldCode(); 

	/**
	 * 字段名称
	 */
	public String getFieldName();

	/**
	 * 是否固定
	 */
	public boolean isFixCondition();

	/**
	 * 是否必需
	 */
	public boolean isRequired();

	/**
	 * 是否为数值
	 */
	public boolean isNumberType();
}