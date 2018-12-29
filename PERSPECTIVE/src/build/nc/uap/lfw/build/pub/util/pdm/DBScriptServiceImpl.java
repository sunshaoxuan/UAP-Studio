package nc.uap.lfw.build.pub.util.pdm;

import java.io.File;

import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.pub.util.pdm.itf.IDB2;
import nc.uap.lfw.build.pub.util.pdm.itf.IDBScriptService;
import nc.uap.lfw.build.pub.util.pdm.itf.IOracle;
import nc.uap.lfw.build.pub.util.pdm.itf.ISQLServer;

/**
 * ����ű�������
 * 
 * ������ʽ: ��ʵ��, �̰߳�ȫ
 * �ɷ��ʷ���: validatePDM
 * 
 * @author fanp
 */
public class DBScriptServiceImpl implements IDBScriptService, ISQLServer, IOracle, IDB2
{
//	private String taskId;
	private File pdmFile;

	/**
	 * ���캯��(��ɳ�ʼPDM�Ϸ���У��)
	 * 
	 * @param pdm_path
	 * @return
	 */
	public DBScriptServiceImpl(String task_id, String pdm_path) throws SDPBuildException
	{
//		this.taskId = task_id;
		this.pdmFile = new File(pdm_path);
		try
		{
			/* PDM�Ϸ���У�� */
		    validatePDM(this.pdmFile);
		}
		catch(SDPBuildException sdp_build_exception)
		{
			throw sdp_build_exception;
		}
	}

	/**
	 * �÷���ʵ�ֶ�ָ��PDM�ļ���У��
	 * У�����:
	 * A. PDM�ļ���ʽ�Ƿ�Ϊ"XML"��ʽ
	 * B. ����PDM�ļ���ʹ�õ�Sybase PowerDesigner�İ汾�Ƿ�Ϊ12.0.0.1700
	 * C. PDM�ļ��Ƿ���SQL Server 2005���ݿ�汾����
	 * 
	 * @param pdm_file
	 * @return ��У��ʧ�����׳�SDPBuildException�쳣; ��У��ɹ�����������
	 */
	public void validatePDM(File pdm_file) throws SDPBuildException
	{
		try
		{
			/* PDM�Ϸ���У�� */
			PDMUtil.validate(pdm_file);
		}
		catch(SDPBuildException sdp_build_exception)
		{
			/* ����ʧ�� */
			throw sdp_build_exception;
		}
		return;
	}
	
	/**
	 * �÷���Ϊָ��PDM�ļ�����ָ�����ݿ����͵Ľ���ű�
	 * Ŀǰ֧�ֵ����ݿ�ű�����:
	 * A. SQL Server 2005
	 * B. Oracle 10g
	 * C. DB2 8.x
	 * 
	 * @param db_type
	 * @return
	 */
    public void genDBCreateScript(int db_type)
    {
    	
    }
    
//    private String getTaskId()
//    {
//    	return this.taskId;
//    }
//    
//    private File getPdmFile()
//    {
//    	return this.pdmFile;
//    }
}
