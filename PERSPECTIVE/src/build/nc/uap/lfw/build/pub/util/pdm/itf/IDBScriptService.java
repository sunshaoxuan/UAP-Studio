package nc.uap.lfw.build.pub.util.pdm.itf;

import java.io.File;

import nc.uap.lfw.build.exception.SDPBuildException;

/**
 * ���ݿ�ű�����ӿ�
 * 
 * ��ʵ�ַ���: validatePDM, genDBCreateScript
 * 
 * @author fanp
 */
public interface IDBScriptService extends ISql {
	/**
	 * �÷���ʵ�ֶ�ָ��PDM�ļ���У�� У�����: A. PDM�ļ���ʽ�Ƿ�Ϊ"XML"��ʽ B. ����PDM�ļ���ʹ�õ�Sybase
	 * PowerDesigner�İ汾�Ƿ�Ϊ12.0.0.1700 C. PDM�ļ��Ƿ���SQL Server 2005���ݿ�汾����
	 * 
	 * @param pdm_path
	 * @return ��У��ʧ�����׳�SDPBuildException�쳣; ��У��ɹ�����������
	 */
	public void validatePDM(File pdm_path) throws SDPBuildException;

	/**
	 * �÷���Ϊָ��PDM�ļ�����ָ�����ݿ����͵Ľ���ű� Ŀǰ֧�ֵ����ݿ�ű�����: A. SQL Server 2005 B. Oracle 10g
	 * C. DB2 8.x
	 * 
	 * @param db_type
	 * @return
	 */
	public void genDBCreateScript(int db_type);
}
