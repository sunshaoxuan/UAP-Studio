package nc.uap.ctrl.tpl.qry.meta;

public interface IFilterMeta {

	/**
	 * �ֶα���
	 */
	public String getFieldCode(); 

	/**
	 * �ֶ�����
	 */
	public String getFieldName();

	/**
	 * �Ƿ�̶�
	 */
	public boolean isFixCondition();

	/**
	 * �Ƿ����
	 */
	public boolean isRequired();

	/**
	 * �Ƿ�Ϊ��ֵ
	 */
	public boolean isNumberType();
}