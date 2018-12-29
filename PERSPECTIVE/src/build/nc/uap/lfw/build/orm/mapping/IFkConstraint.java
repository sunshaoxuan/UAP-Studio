package nc.uap.lfw.build.orm.mapping;

import java.util.List;


/**
 * ���Լ����
 * 
 * @author PH
 */
public interface IFkConstraint extends IConstraint {
	
	/**
	 * ��ȡ����������
	 * 
	 * @return ����������
	 */
	public ITable getRefTable();
	
	/**
	 * ��ȡ������Ķ�Ӧ�ֶΡ�
	 * 
	 * @return ������Ķ�Ӧ�ֶ�
	 */
	public List<IColumn> getRefColumns();
	
	/**
	 * ��ȡ���������ӳ���ϵ��<br>
	 * Key: �ӱ�����; Value: �����Ӧ�ӱ�������ֶΡ�
	 * 
	 * @return ���ӳ���ϵ
	 */
//	public Map<IColumn, IColumn> getRefCols();
	

}
