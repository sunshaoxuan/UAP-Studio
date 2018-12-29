package nc.uap.lfw.build.dbrecord.itf;

import java.sql.Connection;
import java.util.Map;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;

/**
 * Ԥ�ýű�ҵ��ӿڡ�
 * <ol>
 * 	<li>�����ӱ������ļ�{@link DbRecordConfig}��ȡ{@link IDbRecordScript}��</li>
 * 	<li>��{@link IDbRecordScript}Ԥ���������ݡ�</li>
 * 	<li>��{@link IDbRecordScript}����sql�ű��ļ���</li>
 * </ol>
 * 
 * @author PH
 */
public interface IDbRecordService {
	
	/** �������ʾ */
	public static final String GROUP_TYPE_UNGROUP = "#unGroup$#";
	
	/**
	 * �ݵ������û�ȡָ�����Ԥ�ýű�����������Ϣ��
	 * 
	 * @param cfg ��������
	 * @param conn DB connection
	 * @return Ԥ�ýű�����������Ϣ
	 * @throws SdpBuildRuntimeException
	 */
	public IDbRecordScript retrieveDBRecordScript(DbRecordConfig cfg, Connection conn) 
			throws SdpBuildRuntimeException;
	
	/**
	 * Ԥ��Ԥ�ýű����������<br>
	 * Key����������Value����Ӧ����ĵ�������Ԥ������������ʱ����ֵֻ��һ�KeyΪIDBRecordService.GROUP_TYPE_UNGROUP��
	 * 
	 * @param dbRecordScript Ԥ�ýű�����������Ϣ
	 * @param conn DB connection
	 * @return Ԥ�ýű��������
	 * @throws SdpBuildRuntimeException
	 */
	public Map<String, String> preViewScriptResult(IDbRecordScript dbRecordScript, Connection conn) 
			throws SdpBuildRuntimeException;
	
	/**
	 * ��Ԥ�ýű�������Ϣ����sql�ļ���
	 * 
	 * @param script ����������Ϣ
	 * @param conn DB connection
	 * @param sqlFileCfg {@link DbRecordSqlFileCfg}
	 * @return 
	 * @throws SdpBuildRuntimeException
	 */
	public ISqlFile[] geneSqlFile(IDbRecordScript script, Connection conn, DbRecordSqlFileCfg sqlFileCfg, Map<String, MLTableMetaInfo> MLTableMetaInfo)
			throws SdpBuildRuntimeException;
	
	/**
	 * �ݲ�ѯ������ѯ���ݿ��¼�����
	 * 
	 * @param queryInfo ��ѯ����
	 * @param conn DB connection
	 * @return ���ݿ��¼���
	 * @throws SdpBuildRuntimeException
	 */
//	public SqlQueryResultSet queryResult(IQueryInfo queryInfo, Connection conn) 
//			throws SdpBuildRuntimeException;
	
	/**
	 * У��Ԥ�ýű������
	 * <ol>
	 * 	<li>�����ļ���ȷ�ԡ�</li>
	 * 	<li>��Ӧ���ݿ���Ϣ��ȷ�ԡ�</li>
	 * </ol>
	 * 
	 * @param dbRecordScripts
	 */
	public void validateDbRecordScript(DbRecordConfig cfg, DbRecordSqlFileCfg sqlFileCfg, Connection conn);
	
	
	
	
	
}
