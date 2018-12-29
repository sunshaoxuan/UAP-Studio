package nc.uap.lfw.build.orm.mapping;

/**
 * ���ݿ�����Ϣ�ӿڡ�
 * 
 * @author PH
 */
public interface IColumn{
	
	/**
	 * ��ȡ������
	 * 
	 * @return ����
	 */
	public String getName();
	
	/**
	 * ��ȡ������
	 * 
	 * @return ������
	 */
	public ITableBase getTableBase();
	
	/**
	 * ��ȡ�е��������͡�
	 * @see java.sql.Types
	 * 
	 * @return ��������
	 */
	public int getDataType();
	
	/**
	 * ��ȡ�е������������ơ�
	 * 
	 * @return ������������
	 */
	public String getTypeName();
	
	/**
	 * ��ȡ�ֶγ��ȡ�
	 * 
	 * @return �ֶγ���
	 */
	public int getLength();
	
	/**
	 * ��ȡ�ֶξ��ȡ�
	 * 
	 * @return �ֶξ���
	 */
	public int getPrecise();
	
	/**
	 * �ֶ��Ƿ��Ϊnull��
	 * 
	 * @return true if null enabled.
	 */
	public boolean isNullable();
	
	/**
	 * ��ȡ�ֶε�Ĭ��ֵ��
	 * 
	 * @return �ֶε�Ĭ��ֵ
	 */
	public String getDefaultValue(); 
	
	/**
	 * ��ȡ���Ĭ���ַ���ֵ��
	 * 
	 * @return Ĭ���ַ���ֵ
	 */
//	public String getDefaultStringValue();
	
	/**
	 * ��ȡ�ֶε�������Ϣ��
	 * 
	 * @return �ֶε�������Ϣ
	 */
	public String getDesc();
	
	/**
	 * ��ȡ�ֶεİ�����Ϣ����ʾ�����ֶΣ�
	 * @return
	 */
	public String getStereotype();

}
