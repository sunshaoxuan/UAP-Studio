package nc.uap.lfw.build.transport;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;

/**
 * �ļ����ͽӿڡ���Դ�ļ����д����磺
 * <ul>
 * 	<li>�ϴ����汾���ƹ���</li>
 * 	<li>���͵��ļ�ϵͳ����·��������</li>
 * </ul>
 * 
 * @author PH
 */
public interface ITransporter {
	
	/**
	 * ��Դ�ļ����д���
	 * 
	 * @param srcFile
	 */
	public void handle(ISqlFile[] srcFile);
	
	/**
	 * ��ȡ�������������
	 * 
	 * @return
	 */
	public String getOperateDesc();

}
