package nc.uap.lfw.build.dbscript.ext;

import java.sql.Connection;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbrecord.itf.DbRecordConfig;
import nc.uap.lfw.build.dbrecord.itf.DbRecordSqlFileCfg;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordScript;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordService;
import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;
import nc.uap.lfw.build.transport.ITransporter;

public class DbRecordWithTransportServiceImpl implements IDbRecordService {
	
	private IDbRecordService dbRecordService;
	
	private ITransporter transporter;
	
	public DbRecordWithTransportServiceImpl(IDbRecordService dbRecordService, ITransporter transporter){
		this.dbRecordService = dbRecordService;
		this.transporter = transporter;
	}

	public ISqlFile[] geneSqlFile(IDbRecordScript script, Connection conn, DbRecordSqlFileCfg sqlFileCfg, Map<String, MLTableMetaInfo> mlTableMetaInfo) throws SdpBuildRuntimeException {
		ISqlFile[] sqlFile = dbRecordService.geneSqlFile(script, conn, sqlFileCfg, mlTableMetaInfo);
		if(transporter != null){
			try{
				transporter.handle(sqlFile);
				MainPlugin.getDefault().logInfo("预置脚本表" + script.getTableName() + transporter.getOperateDesc() + "成功。");
				OperateLogger.getInstance().addLog(LogLevel.INFO, "表" + script.getTableName() + transporter.getOperateDesc() + "成功。");
			} catch (SdpBuildRuntimeException e) {
				MainPlugin.getDefault().logInfo("预置脚本表" + script.getTableName() + transporter.getOperateDesc() + "失败：" + e.getMessage());
				OperateLogger.getInstance().addLog(LogLevel.ERROR, "表" + script.getTableName() + transporter.getOperateDesc() + "失败，" + e.getMessage());
				throw e;
			}
		}
		return sqlFile;
	}

	public Map<String, String> preViewScriptResult(IDbRecordScript dbRecordScript, Connection conn)
			throws SdpBuildRuntimeException {
		return dbRecordService.preViewScriptResult(dbRecordScript, conn);
	}

	public IDbRecordScript retrieveDBRecordScript(DbRecordConfig cfg, Connection conn) throws SdpBuildRuntimeException {
		return dbRecordService.retrieveDBRecordScript(cfg, conn);
	}

	public void validateDbRecordScript(DbRecordConfig cfg, DbRecordSqlFileCfg sqlFileCfg, Connection conn) {
		dbRecordService.validateDbRecordScript(cfg, sqlFileCfg, conn);
	}

}
