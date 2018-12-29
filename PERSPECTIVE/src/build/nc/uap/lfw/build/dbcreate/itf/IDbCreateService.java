package nc.uap.lfw.build.dbcreate.itf;

import java.io.File;

import nc.uap.lfw.build.exception.SdpBuildRuntimeException;

/**
 * ����ű�ҵ��ӿڡ�
 * 
 * @author PH
 */
public interface IDbCreateService {
	
	/**
	 * У��PDM�ļ��Ƿ�Ϸ���
	 * 
	 * @param pdmFile ��У��PDM�ļ�
	 * @param parseReference �Ƿ�������
	 * @throws SdpBuildRuntimeException ��У��ʧ��ʱ�׳���
	 */
	public void validatePdm(File pdmFile, boolean parseReference) throws SdpBuildRuntimeException;
	
	/**
	 * ��PDM����(oracle��sqlserver��db2)��sql�ű��ļ���
	 * 
	 * @param pdmFile PDM�ļ�
	 * @param geneReference �Ƿ��������
	 * @param sqlRoot ����Sql�ļ�Ŀ¼
	 * @throws SdpBuildRuntimeException ��PDM�ļ�У��ʧ�ܻ������ļ�ʧ��ʱ�׳���
	 */
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot) throws SdpBuildRuntimeException;
	
	/**
	 * ��PDM����ָ�����ݿ��sql�ű��ļ���
	 * 
	 * @param pdmFile PDM�ļ�
	 * @param geneReference �Ƿ��������
	 * @param sqlRoot ����Sql�ļ�Ŀ¼
	 * @param dbType ���������ݿ�����͡�
	 * @throws SdpBuildRuntimeException ��PDM�ļ�У��ʧ�ܻ������ļ�ʧ��ʱ�׳���
	 */
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot, DatabaseType dbType) throws SdpBuildRuntimeException;
	
	/**
	 * ��PDM���������ֵ䡣
	 * 
	 * @param pdmFile PDM�ļ�
	 * @param geneReference �Ƿ��������
	 * @param ddRoot �ֵ��ļ���Ŀ¼
	 * @throws SdpBuildRuntimeException ��PDM�ļ�У��ʧ�ܻ������ļ�ʧ��ʱ�׳���
	 */
	public void geneDataDictionary(File pdmFile, boolean geneReference, File ddRoot) throws SdpBuildRuntimeException;
	
	
	/**
	 * ���ݿ����͡�
	 * 
	 * @author PH
	 */
	public enum DatabaseType{
		ORACLE, SQLSERVER, DB2
	}
}
