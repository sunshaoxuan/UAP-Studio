package nc.uap.lfw.build.dao;

import java.util.List;

import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * ���ݲ�ѯ��Ϣ�ӿڡ�
 * 
 * @author PH
 */
public interface IQueryInfo {
	
	/**
	 * ��ȡ��Ԫ������Ϣ��
	 * 
	 * @return ��Ԫ������Ϣ
	 */
	public ITable getTable();
	
	/**
	 * ��ȡ��ѯ������
	 * 
	 * @return ��ѯ����
	 */
	public String getWhereCondition();
	
	/**
	 * ��ȡ�ӱ��ѯ��Ϣ��
	 * 
	 * @return �ӱ��ѯ��Ϣ
	 */
	public List<? extends IQueryInfo> getChilds();

	//public LinkedHashMap<String, Boolean> getSortField();
}
