package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * ���ݿ���Ԫ���������Ϣ�ӿڡ�
 * 
 * @author PH
 */
public interface ITable extends ITableBase{
	
	/**
	 * ��ȡ����Լ����
	 * 
	 * @return ����Լ��
	 */
	public IPkConstraint getPkConstraint();
	
	/**
	 * ��ȡ���Լ������
	 * 
	 * @return ���Լ����
	 */
	public List<IFkConstraint> getFkConstraints();
	
	/**
	 * ��ȡ������ָ��������Լ����
	 * 
	 * @param refTableName ����
	 * @return ������ָ��������Լ��
	 */
	public IFkConstraint getFkConstraintByRefTableName(String refTableName);
	
}
