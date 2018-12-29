package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * ʵ�����(��ͼ)����ӿڡ�
 * 
 * @author PH
 */
public interface ITableBase {
	
	/**
	 * ��ȡ������
	 * 
	 * @return ����
	 */
	public String getName();
	
	/**
	 * ��ȡ�����С�
	 * 
	 * @return ������
	 */
	public List<IColumn> getAllColumns();
	
//	public Map<String, IColumn> getAllColumns();// for performance and convenience
	
	/**
	 * ��������ȡ�С�
	 * 
	 * @param colName ����
	 * @return ��Ӧ��
	 */
	public IColumn getColumnByName(String colName);
	
	/**
	 * ��������Ϣ��
	 * 
	 * @return ��������Ϣ
	 */
	public String getDesc();

}
