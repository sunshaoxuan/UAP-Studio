package nc.uap.lfw.build.dbrecord.itf;

import java.sql.Connection;
import java.util.Map;

import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;

/**
 * 预置脚本业务接口。
 * <ol>
 * 	<li>据主子表配置文件{@link DbRecordConfig}获取{@link IDbRecordScript}。</li>
 * 	<li>据{@link IDbRecordScript}预览导出数据。</li>
 * 	<li>据{@link IDbRecordScript}生成sql脚本文件。</li>
 * </ol>
 * 
 * @author PH
 */
public interface IDbRecordService {
	
	/** 不分组标示 */
	public static final String GROUP_TYPE_UNGROUP = "#unGroup$#";
	
	/**
	 * 据导出配置获取指定表的预置脚本导出条件信息。
	 * 
	 * @param cfg 导出配置
	 * @param conn DB connection
	 * @return 预置脚本导出条件信息
	 * @throws SdpBuildRuntimeException
	 */
	public IDbRecordScript retrieveDBRecordScript(DbRecordConfig cfg, Connection conn) 
			throws SdpBuildRuntimeException;
	
	/**
	 * 预览预置脚本导出结果。<br>
	 * Key：分组名；Value：对应分组的导出数据预览。当不分组时返回值只有一项，Key为IDBRecordService.GROUP_TYPE_UNGROUP。
	 * 
	 * @param dbRecordScript 预置脚本导出条件信息
	 * @param conn DB connection
	 * @return 预置脚本导出结果
	 * @throws SdpBuildRuntimeException
	 */
	public Map<String, String> preViewScriptResult(IDbRecordScript dbRecordScript, Connection conn) 
			throws SdpBuildRuntimeException;
	
	/**
	 * 据预置脚本导出信息生成sql文件。
	 * 
	 * @param script 导出条件信息
	 * @param conn DB connection
	 * @param sqlFileCfg {@link DbRecordSqlFileCfg}
	 * @return 
	 * @throws SdpBuildRuntimeException
	 */
	public ISqlFile[] geneSqlFile(IDbRecordScript script, Connection conn, DbRecordSqlFileCfg sqlFileCfg, Map<String, MLTableMetaInfo> MLTableMetaInfo)
			throws SdpBuildRuntimeException;
	
	/**
	 * 据查询条件查询数据库记录结果。
	 * 
	 * @param queryInfo 查询条件
	 * @param conn DB connection
	 * @return 数据库记录结果
	 * @throws SdpBuildRuntimeException
	 */
//	public SqlQueryResultSet queryResult(IQueryInfo queryInfo, Connection conn) 
//			throws SdpBuildRuntimeException;
	
	/**
	 * 校验预置脚本以下项：
	 * <ol>
	 * 	<li>配置文件正确性。</li>
	 * 	<li>对应数据库信息正确性。</li>
	 * </ol>
	 * 
	 * @param dbRecordScripts
	 */
	public void validateDbRecordScript(DbRecordConfig cfg, DbRecordSqlFileCfg sqlFileCfg, Connection conn);
	
	
	
	
	
}
