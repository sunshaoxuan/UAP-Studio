package nc.uap.lfw.build.dbscript.ext;

import java.io.File;

import nc.uap.lfw.build.dbcreate.impl.DbCreateSqlFile;
import nc.uap.lfw.build.dbcreate.itf.IDbCreateService;
import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.transport.ITransporter;

/**
 * 有脚本传输功能的建库脚本业务实现。
 * 
 * @author PH
 */
public class DbCreateWithTransportServiceImpl implements IDbCreateService {
	
	private IDbCreateService dbCreateService;
	
	private ITransporter transporter;
	

	public DbCreateWithTransportServiceImpl(IDbCreateService dbCreateService, ITransporter transporter) {
		this.dbCreateService = dbCreateService;
		this.transporter = transporter;
	}

	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot) throws SdpBuildRuntimeException {
		dbCreateService.geneSqlFile(pdmFile, geneReference, sqlRoot);
		transportFile(sqlRoot, pdmFile.getName());
	}
	
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot, DatabaseType dbType) throws SdpBuildRuntimeException {
		dbCreateService.geneSqlFile(pdmFile, geneReference, sqlRoot, dbType);
		transportFile(sqlRoot, pdmFile.getName());
	}

	public void validatePdm(File pdmFile, boolean parseReference) throws SdpBuildRuntimeException {
		dbCreateService.validatePdm(pdmFile, parseReference);
	}

	public void geneDataDictionary(File pdmFile, boolean geneReference, File ddRoot) throws SdpBuildRuntimeException {
		dbCreateService.geneDataDictionary(pdmFile, geneReference, ddRoot);
	}

	/**
	 * 文件传输。
	 * 
	 * @param sqlRoot
	 * @param pdmFileName
	 */
	private void transportFile(File sqlRoot, String pdmFileName){
		if(transporter != null){
			try {
				ISqlFile file = new DbCreateSqlFile(sqlRoot);
				
				transporter.handle(new ISqlFile[]{file});
				OperateLogger.getInstance().addLog(LogLevel.INFO, "PDM(" + pdmFileName + ")" + transporter.getOperateDesc() + "成功。");
			} catch (SdpBuildRuntimeException e) {
				OperateLogger.getInstance().addLog(LogLevel.ERROR, "PDM(" + pdmFileName + ")" + transporter.getOperateDesc() + "失败。");
				throw e;
			}
		}
	}

}
