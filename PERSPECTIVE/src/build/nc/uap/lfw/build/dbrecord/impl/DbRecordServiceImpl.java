package nc.uap.lfw.build.dbrecord.impl;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dao.IQueryInfo;
import nc.uap.lfw.build.dao.SqlQueryResultSet;
import nc.uap.lfw.build.dao.SqlUtil;
import nc.uap.lfw.build.dbrecord.impl.OidMarkValidator.PkInfo;
import nc.uap.lfw.build.dbrecord.itf.DbRecordConfig;
import nc.uap.lfw.build.dbrecord.itf.DbRecordSqlFileCfg;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordScript;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordService;
import nc.uap.lfw.build.dbrecord.itf.MainTableRecordItem;
import nc.uap.lfw.build.dbscript.itf.ISqlFile;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.orm.mapping.IColumn;
import nc.uap.lfw.build.orm.mapping.IFkConstraint;
import nc.uap.lfw.build.orm.mapping.IPkConstraint;
import nc.uap.lfw.build.orm.mapping.ITable;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;
import nc.uap.lfw.build.pub.util.pdm.itf.ISDP;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 预置脚本业务实现。
 * 
 * @author PH
 */
public class DbRecordServiceImpl implements IDbRecordService {
	
	/** insert语句分隔符 */
	private static final String SQL_DELIMITER = IOUtils.LINE_SEPARATOR + "go" + IOUtils.LINE_SEPARATOR;
	
		
	public IDbRecordScript retrieveDBRecordScript(DbRecordConfig cfg, Connection conn) throws SdpBuildRuntimeException {
		MainTableRecordItem mainTableItem = cfg.getMainTableItem();
		if(mainTableItem == null){
			throw new IllegalArgumentException("未指定预置脚本主表导出参数。");
		}
		OperateLogger.getInstance().addLog(LogLevel.INFO, new StringBuilder("开始解析表").append(mainTableItem.getTableName()).append("。").toString());
		DbRecordCfgParser cfgParser = new DbRecordCfgParser(cfg, conn);
		return cfgParser.getDBRecordScript();
	}
	
	public SqlQueryResultSet queryResult(IQueryInfo queryInfo, Connection conn) throws SdpBuildRuntimeException{
		return SqlUtil.queryResults(queryInfo, conn);
	}
	
	public ISqlFile[] geneSqlFile(IDbRecordScript script, Connection conn, DbRecordSqlFileCfg sqlFileCfg, Map<String, MLTableMetaInfo> mlTableMetaInfo) 
			throws SdpBuildRuntimeException{
		try{
			//1.获取生成目录路径，删除该路径下所有目录文件。
			String rootDirName = sqlFileCfg.getAbsolutePath(script.getTable().getName());
			if(StringUtils.isBlank(rootDirName)){
				throw new SdpBuildRuntimeException("表" + script.getTable().getName() + "在.map文件中未配置。");
			}
			File rootDir = new File(rootDirName);
			if(rootDir.exists()){
				try {
					FileUtils.forceDelete(rootDir);
				} catch (IOException e) {
					MainPlugin.getDefault().logError("删除目录" + rootDirName + "失败", e);
					throw new SdpBuildRuntimeException("删除目录" + rootDirName + "失败。");
				}
			}
			//为方便CC上传处理。
			try {
				FileUtils.forceMkdir(rootDir);
			} catch (IOException e) {
				MainPlugin.getDefault().logError("创建目录" + rootDirName + "失败", e);
				throw new SdpBuildRuntimeException("创建目录" + rootDirName + "失败。");
			}
			
			//2.查询数据记录，生成文件。
			//2.1 处理自定义查询二进制数据。
			if(DbRecordSqlFileCfg.CUSTOM_QUERY.equalsIgnoreCase(script.getTableDesc())){
//				exportBinary(sqlFileCfg, rootDirName, script, conn);
				return new ISqlFile[]{new DbRecordSqlFile(rootDir)};
			}
			
//			if(DbRecordSqlFileCfg.FREE_REPORT.equalsIgnoreCase(script.getTableDesc())){
//				exportFreeReport(sqlFileCfg, rootDirName, script, conn);
//				return new ISqlFile[]{new DbRecordSqlFile(rootDir)};
//			}
			
			
			List<File> mlScriptDir = new ArrayList<File>(); 
			//2.2 不分组
			if(StringUtils.isBlank(script.getGrpField())){
				SqlQueryResultSet resultSet = queryResult(script, conn);
				if(resultSet != null){
					//校验OIDMark
					OidMarkValidator validator = new OidMarkValidator(sqlFileCfg.getOidMarkRuleFilePath());
					checkOidMark(resultSet, validator, sqlFileCfg.getDepartment());
					handleOidValidateResult(validator);
					
					geneSqlFile(resultSet, rootDirName, SQL_DELIMITER, script, mlTableMetaInfo, mlScriptDir, null);
				}
			}else{
				//2.3 分组
				geneSqlFilesByGroup(script, rootDirName, conn, sqlFileCfg.getOidMarkRuleFilePath(), sqlFileCfg.getDepartment(), mlTableMetaInfo, mlScriptDir);
			}
			MainPlugin.getDefault().logInfo("预置脚本表" + script.getTableName() + "生成成功。");
			OperateLogger.getInstance().addLog(LogLevel.INFO, "表" + script.getTableName() + "生成成功。");
			if(mlScriptDir.size()>0){
				ISqlFile[] aryScriptDir = new DbRecordSqlFile[mlScriptDir.size()+1];
				aryScriptDir[0] = new DbRecordSqlFile(rootDir);
				for(int k=0; k<mlScriptDir.size(); k++){
					aryScriptDir[k+1] = new DbRecordSqlFile(mlScriptDir.get(k));
				}
				return aryScriptDir;
			}else{
				return new ISqlFile[]{new DbRecordSqlFile(rootDir)};
			}
		}catch (SdpBuildRuntimeException e) {
			MainPlugin.getDefault().logError("预置脚本表" + script.getTableName() + "生成失败: " + e.getMessage());
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "表" + script.getTableName() + "生成失败，" + e.getMessage());
			throw e;
		}
	}

	/**
	 * 自由报表导出
	 * @param sqlFileCfg
	 * @param rootDirName
	 * @param script
	 * @param conn
	 */
//	private void exportFreeReport(DbRecordSqlFileCfg sqlFileCfg, String rootDirName, IDbRecordScript script, Connection conn) {
//		if(sqlFileCfg.isFmdEnabledOfFR()){
//			String fid = sqlFileCfg.getFidOfFR();
//			if(StringUtils.isNotBlank(fid)){
//				if(!ExportUtil.onExportBinary(fid, "", true, rootDirName, conn)){
//					String errorMsg = "FMD导出，自由报表" + script.getTable().getName() + "发生错误。";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "启用了格式目录导出，但未设置格式目录对象ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		if(sqlFileCfg.isQmdEnabledOfFR()){
//			String qid = sqlFileCfg.getQidOfFR();
//			if(qid != null && !(qid = qid.trim()).equals("")){
//				if(!ExportUtil.onExportBinary(qid, "", false, rootDirName, conn)){
//					String errorMsg = "QMD导出，自由报表" + script.getTable().getName() + "发生错误。";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "启用了查询目录导出，但未设置格式目录对象ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		
//	}

	public Map<String, String> preViewScriptResult(IDbRecordScript dbRecordScript, Connection conn)
			throws SdpBuildRuntimeException {//限制最大显示字数?
//		if(StringUtils.isBlank(dbRecordScript.getGrpField())){
//			
//		}
		//目前只查询主表数据
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.table = dbRecordScript.getTable();
		queryInfo.whereCondition = dbRecordScript.getWhereCondition();
		SqlQueryResultSet queryResult = queryResult(queryInfo, conn);
		Map<String, String> unGrpResult = new HashMap<String, String>();
		Writer writer = new StringWriter();
		if(queryResult != null){
			try {
				geneInsertSqlWithMainAndSub(queryResult, SQL_DELIMITER, writer);
			} catch (IOException e) {
				throw new SdpBuildRuntimeException("表" + dbRecordScript.getTable().getName() + "生成sql失败。");
			}
		}
		unGrpResult.put(IDbRecordService.GROUP_TYPE_UNGROUP, writer.toString());
		return unGrpResult;
	}
	
	public void validateDbRecordScript(DbRecordConfig cfg, DbRecordSqlFileCfg sqlFileCfg, Connection conn) {
		try{
			IDbRecordScript script = retrieveDBRecordScript(cfg, conn);
			String rootDirName = sqlFileCfg.getAbsolutePath(script.getTable().getName());
			if(StringUtils.isBlank(rootDirName)){
				throw new SdpBuildRuntimeException("表" + script.getTable().getName() + "在.map文件中未配置。");
			}
			OperateLogger.getInstance().addLog(LogLevel.INFO, cfg.getMainTableItem().getTableName() + "校验成功。");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, cfg.getMainTableItem().getTableName() + "校验失败，" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 导出二进制数据。
	 * 
	 * @param sqlFileCfg DBRecordSqlFileCfg
	 * @param rootDirName root directory name
	 * @param script IDBRecordScript
	 * @param conn Connection
	 */
//	private void exportBinary(DbRecordSqlFileCfg sqlFileCfg, String rootDirName, IDbRecordScript script,
//			Connection conn){
//		if(sqlFileCfg.isFmdEnabledOfQE()){
//			String fid = sqlFileCfg.getFidOfQE();
//			if(StringUtils.isNotBlank(fid)){
//				if(!nc.vo.pub.oyx.ExportUtil.onExportBinary(fid, "", true, rootDirName, conn)){
//					String errorMsg = "FMD导出，自定义查询" + script.getTable().getName() + "发生错误。";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "启用了格式目录导出，但未设置格式目录对象ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		if(sqlFileCfg.isQmdEnabledOfQE()){
//			String qid = sqlFileCfg.getQidOfQE();
//			if(qid != null && !(qid = qid.trim()).equals("")){
//				if(!nc.vo.pub.oyx.ExportUtil.onExportBinary(qid, "", false, rootDirName, conn)){
//					String errorMsg = "QMD导出，自定义查询" + script.getTable().getName() + "发生错误。";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "启用了查询目录导出，但未设置格式目录对象ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//	}

	/**
	 * 根据结果集生成(insert)sql文件。
	 * 
	 * @param resultSet
	 * @param dir
	 * @param separator
	 * @param dbRecordScript
	 */
	private void geneSqlFile(SqlQueryResultSet resultSet, String dir, String separator, IDbRecordScript dbRecordScript, Map<String, MLTableMetaInfo> mlTableMetaInfo, List<File> mlScriptDir, String grpFlag){
		if(resultSet == null){
			return;
		}
		File file = new File(dir);
		if(!file.exists() && !file.mkdirs()){
			throw new SdpBuildRuntimeException("创建目录" + dir + "失败。");
		}
		String sqlFileName = dir + File.separator + dbRecordScript.getSqlNo() + ".sql";
		ITable table = resultSet.getTable();
		if(resultSet.getResults().isEmpty()){
			MainPlugin.getDefault().logInfo("There is no data in table(" + table.getName()+").");
			return;
		}

		MLTableMetaInfo tableMLMetaInfo = null;
		if(mlTableMetaInfo != null){
			tableMLMetaInfo = mlTableMetaInfo.get(table.getName().toLowerCase());
		}
		
		//1. 主表
		Writer writer = null;
		try {
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFileName), "UTF-8"));
			geneInsertSql(resultSet, separator, writer, tableMLMetaInfo);
		} catch (IOException e) {
			MainPlugin.getDefault().logError("写入文件" + sqlFileName + "失败。",e);
			throw new SdpBuildRuntimeException("写入文件" + sqlFileName + "失败。");
		} finally{
			IOUtils.closeQuietly(writer);
		}
		
		List<IColumn> lstBlobColumn = getBlobColumns(table);
		if(lstBlobColumn != null && lstBlobColumn.size()>0){
			
			IPkConstraint pkConstraint = table.getPkConstraint();
			List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
			if(pkCols == null || pkCols.isEmpty()){
				throw new SdpBuildRuntimeException("表:"+ table.getName()+"没有主键，无法生成数据库blob字段存储文件");
			}
			if(pkCols.size() != 1){
				throw new SdpBuildRuntimeException("表:"+ table.getName() + "为复合主键，无法生成数据库blob字段存储文件");
			}
			
			String pkColumnName = pkCols.get(0).getName();
			if(pkColumnName != null){
				pkColumnName = pkColumnName.toLowerCase();
			}
			
			geneBlobFile(resultSet, pkColumnName, lstBlobColumn, dir, dbRecordScript);
			
		}
		
		if(tableMLMetaInfo != null ){
			
			File mlRecordRootDir = file.getParentFile();
			while(!mlRecordRootDir.getName().equalsIgnoreCase(ISDP._SCRIPT_INIT_)){
				mlRecordRootDir = mlRecordRootDir.getParentFile();
			}
			
			String mlRecordFilePath = mlRecordRootDir.getAbsolutePath() + File.separator + ISDP._SCRIPT_INIT_DBML + File.separator +  ISDP._SCRIPT_INIT_DBML_SIMPCHN + File.separator + table.getName();
			File mlRecordFile = new File(mlRecordFilePath);
			if(!mlRecordFile.exists() && !mlRecordFile.mkdirs()){
				throw new SdpBuildRuntimeException("创建目录" + mlRecordFilePath + "失败。");
			}
			
			
			String mlRecordFileName = mlRecordFilePath + File.separator + (grpFlag == null ? "001.csv" : convertToWinSystemFileName(grpFlag)+".csv");
			try{
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mlRecordFileName), "utf-8"));
				geneMLUpdateSql(resultSet, writer, tableMLMetaInfo);
				mlScriptDir.add(new File(mlRecordFileName).getParentFile());
			} catch (IOException e) {
				MainPlugin.getDefault().logError("写入文件" + sqlFileName + "失败。",e);
				throw new SdpBuildRuntimeException("写入文件" + sqlFileName + "失败。");
			} finally{
				IOUtils.closeQuietly(writer);
			}

		}
		
		//2. 子表
		if(!resultSet.getSubResultSets().isEmpty()){
			Map<String, IDbRecordScript> subSqlNoMap = new HashMap<String, IDbRecordScript>();
			for(IDbRecordScript subDbRecordScript : dbRecordScript.getChilds()){
				subSqlNoMap.put(subDbRecordScript.getTable().getName(), subDbRecordScript);
			}
			for(SqlQueryResultSet subResultSet : resultSet.getSubResultSets()){
				geneSqlFile(subResultSet, dir, separator, subSqlNoMap.get(subResultSet.getTable().getName()), mlTableMetaInfo, mlScriptDir, grpFlag);
			}
		}
	}
	
	/**
	 * 检查字符串中是否包含windows系统文件名不允许包含的字符，如果，有转换成'&'
	 * @param beforeConvert
	 * @return
	 */
	private String convertToWinSystemFileName(String beforeConvert) {
		if(beforeConvert == null)
			return null;
		String afterConvert = beforeConvert.replace('\\', '&');
		afterConvert = afterConvert.replace('/', '&');
		afterConvert = afterConvert.replace(':', '&');
		afterConvert = afterConvert.replace('*', '&');
		afterConvert = afterConvert.replace('?', '&');
		afterConvert = afterConvert.replace('"', '&');
		afterConvert = afterConvert.replace('<', '&');
		afterConvert = afterConvert.replace('>', '&');
		afterConvert = afterConvert.replace('|', '&');
		
		return afterConvert;
	}

	/**
	 * 生成保存Blob字段内容的二进制文件
	 * @param resultSet
	 * @param pkColumnName
	 * @param lstBlobColumn
	 * @param dir
	 * @param dbRecordScript
	 */
	private void geneBlobFile(SqlQueryResultSet resultSet, String pkColumnName, List<IColumn> lstBlobColumn, String dir, IDbRecordScript dbRecordScript){
		String tableName = resultSet.getTable().getName();
		byte[] btTableName = null;
		try {
			btTableName = tableName.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			MainPlugin.getDefault().logError("生成Blob文件时发生错误：转换表["+tableName+"]名称为字节数组时发生错误。", e);
			throw new SdpBuildRuntimeException("生成Blob文件时发生错误：转换表["+tableName+"]名称为字节数组时发生错误。", e);
		}
		if(btTableName.length>40){
			throw new SdpBuildRuntimeException("表名" + tableName + "长度超过40个字节");
		}
		byte[] outputTableName = new byte[40];
		System.arraycopy(btTableName, 0, outputTableName, 0, btTableName.length);
		byte[] btPKColumnName = null;
		try {
			btPKColumnName = pkColumnName.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			MainPlugin.getDefault().logError("生成Blob文件时发生错误：转换表["+tableName+"]的主键["+pkColumnName+"]名称为字节数组时发生错误。", e);
			throw new SdpBuildRuntimeException("生成Blob文件时发生错误：转换表["+tableName+"]的主键["+pkColumnName+"]名称为字节数组时发生错误。", e);
		}
		if(btPKColumnName.length>40){
			throw new SdpBuildRuntimeException("表名" + tableName + "中的主键名"+pkColumnName + "长度超过40个字节");
		}
		byte[] outputPKColumnName = new byte[40];
		System.arraycopy(btPKColumnName, 0, outputPKColumnName, 0, btPKColumnName.length);
		byte btBlobColumnCount = (byte)lstBlobColumn.size();
		
		String[] aryBlobColumnName = new String[lstBlobColumn.size()];
		for(int i=0; i<lstBlobColumn.size(); i++){
			aryBlobColumnName[i] = lstBlobColumn.get(i).getName();
		}
		
		byte[][] outputBlobColumnNames = new byte[aryBlobColumnName.length][40];
		for(int i=0; i<aryBlobColumnName.length; i++){
			byte[] outputBlobColumnName = new byte[40];
			byte[] btBlobColumnName = null;
			try {
				btBlobColumnName = aryBlobColumnName[i].getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				MainPlugin.getDefault().logError("生成Blob文件时发生错误：转换表["+tableName+"]的Blob字段["+aryBlobColumnName[i]+"]名称为字节数组时发生错误。", e);
				throw new SdpBuildRuntimeException("生成Blob文件时发生错误：转换表["+tableName+"]的Blob字段["+aryBlobColumnName[i]+"]名称为字节数组时发生错误。", e);
			}
			System.arraycopy(btBlobColumnName, 0, outputBlobColumnName, 0, btBlobColumnName.length);
			outputBlobColumnNames[i] = outputBlobColumnName;
		}
		
//		int count = 0;
		for(Map<String, Object> result : resultSet.getResults()){
//			if(count == 2){
//				break;
//			}
			for(Map.Entry<String, Object> entry : result.entrySet()){
				String columnName = entry.getKey().toLowerCase();
				String pkValue = null;
				if(columnName.equals(pkColumnName)){
					pkValue = entry.getValue().toString().trim();
					String blobFileName = dir + File.separator + dbRecordScript.getSqlNo() + "-"+ pkValue + ".blob";
					DataOutputStream out = null;
					FileOutputStream fout = null;
					try{
						fout = new FileOutputStream(new File(blobFileName));
						out = new DataOutputStream(fout);
						
						//输出表名--40个字节
						out.write(outputTableName);
						//输出主键名--40个字节
						out.write(outputPKColumnName);
						//输出Blob字段个数字节--1个字节
						out.write(new byte[]{btBlobColumnCount});
						//输出Blob字段名--每个字段名40个字节
						for(int ii = 0; ii<outputBlobColumnNames.length; ii++){
							out.write(outputBlobColumnNames[ii]);
						}
						
						//输出主键值的长度--1个字节
						byte[] outputPKValue = pkValue.getBytes("utf-8");
						byte pkValueLen = (byte)outputPKValue.length;
						out.write(new byte[]{pkValueLen});
						
						//输出主键值
						out.write(outputPKValue);
						
						//逐个输出blob字段的值
						for(int jj = 0; jj<aryBlobColumnName.length; jj++){
							IColumn blobColumn = resultSet.getTable().getColumnByName(aryBlobColumnName[jj]);
							Object obj = result.get(aryBlobColumnName[jj]);
							if(obj == null){
								//输出blob字段值的长度--4字节 （此时为0）
								out.write(0);								
							}else{
								if(blobColumn.getTypeName().equalsIgnoreCase("image")){
									//SQL Server的image类型
									if(obj instanceof byte[]) {
										byte[] blobByte = (byte[])obj;
										if(blobByte == null){							
											//输出blob字段值的长度--4字节 （此时为0）
											out.write(0);
										}else{
											//输出blob字段值的长度--4字节
											out.writeInt(blobByte.length);
											//输出blob字段值
											out.write(blobByte);								
										}									
									}else{
										String errorInfo = "获取表["+tableName+"]的Image字段["+ aryBlobColumnName[jj]+"]的值时发生类型不是Iamge错误";
										MainPlugin.getDefault().logError(errorInfo);
										throw new SdpBuildRuntimeException(errorInfo);
									}															
								}else if(blobColumn.getTypeName().equalsIgnoreCase("blob")){								
									if (obj instanceof Blob) {
										Blob blobValue = (Blob) obj;
										if(blobValue == null){
											//输出blob字段值的长度--4字节 （此时为0）
											out.write(0);
										}
										else{
											byte[] blobByte = null;
											try{
												blobByte= blobValue.getBytes(1, (int)blobValue.length());
											}catch(SQLException e){
												String errorInfo = "获取表["+tableName+"]的Blob字段["+ aryBlobColumnName[jj]+"]的值时发生错误："+e.getMessage();
												MainPlugin.getDefault().logError(errorInfo, e);
												throw new SdpBuildRuntimeException(errorInfo);
											}
											//输出blob字段值的长度--4字节
											out.writeInt(blobByte.length);
											//输出blob字段值
											out.write(blobByte);
										}															
									}else{
										String errorInfo = "获取表["+tableName+"]的Blob字段["+ aryBlobColumnName[jj]+"]的值时发生类型不是Blob错误";
										MainPlugin.getDefault().logError(errorInfo);
										throw new SdpBuildRuntimeException(errorInfo);
									}
								}else if(blobColumn.getTypeName().equalsIgnoreCase("blob(128m)")){
									String errorInfo = "系统暂不支持对DB2数据库表["+tableName+"]的二进制字段["+ aryBlobColumnName[jj]+"]数据导出";
									MainPlugin.getDefault().logInfo(errorInfo);
									throw new SdpBuildRuntimeException(errorInfo);
								}
							}
						}
						out.flush();
					}catch (IOException e){
						MainPlugin.getDefault().logError("写入文件" + blobFileName + "失败。",e);
						throw new SdpBuildRuntimeException("写入文件" + blobFileName + "失败。");
					}finally{
						IOUtils.closeQuietly(fout);
						IOUtils.closeQuietly(out);
					}
				}
			}
//			count++;
		}
	}

				

	/**
	 * 	获取表中Blob字段
	 * @param table
	 * @return
	 */
	private List<IColumn> getBlobColumns(ITable table) {
		
		List<IColumn> retValue = new ArrayList<IColumn>();
		List<IColumn> allColumns = table.getAllColumns();
		for (IColumn column : allColumns) {
			if(isBlobColumn(column)){
				retValue.add(column);
			}
		}
		return retValue;
	}

	/**
	 * 生成表的多语字段对应更新脚本需要的数据文件
	 * @param resultSet
	 * @param writer
	 * @param tableMLMetaInfo
	 * @throws IOException
	 */
	private void geneMLUpdateSql(SqlQueryResultSet resultSet, Writer writer, MLTableMetaInfo tableMLMetaInfo) throws IOException {
		ITable table = resultSet.getTable();
		IPkConstraint pkConstraint = table.getPkConstraint();
		List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
		if(pkCols == null || pkCols.isEmpty()){
			throw new SdpBuildRuntimeException("表:"+ table.getName()+"没有主键，无法生成数据库脚本多语文件");
		}		
		if(pkCols.size() != 1){
			throw new SdpBuildRuntimeException("表:"+ table.getName() + "为复合主键，无法生成数据库脚本多语文件");
		}
		
		String pkColumnName = pkCols.get(0).getName();
		if(pkColumnName != null){
			pkColumnName = pkColumnName.toLowerCase();
		}
				

		StringBuilder cols = new StringBuilder();
		cols.append(pkColumnName).append(",");
		List<String> lstMLColumnName = new ArrayList<String>();
		Map<String, Integer> MLColumnNameIndex = new HashMap<String, Integer>();
		for (int i = 0; i < tableMLMetaInfo.getColumnNames().length; i++) {
			String mlColumnName = tableMLMetaInfo.getColumnNames()[i];
			lstMLColumnName.add(mlColumnName);
			int nPos = lstMLColumnName.size() - 1;
			MLColumnNameIndex.put(mlColumnName, Integer.valueOf(nPos));
			cols.append(csvEncode(mlColumnName)).append(",");				
		}
		if(cols.charAt(cols.length()-1) == ','){
			cols.deleteCharAt(cols.length() - 1);
		}
		
		if(lstMLColumnName.size() == 0){
			throw new SdpBuildRuntimeException("表：" + table.getName() +"未找到多语字段，无法生成多语预置脚本");
		}
		//第一行为表名
		writer.write(csvEncode(table.getName())+IOUtils.LINE_SEPARATOR);
		//第二行为主键和多语字段名
		writer.write(cols.toString()+IOUtils.LINE_SEPARATOR);
		
		//之后是具体的记录信息
		for(Map<String, Object> result : resultSet.getResults()){
			StringBuilder values = new StringBuilder();
			String[] recordValue = new String[lstMLColumnName.size() +1];
			for(Map.Entry<String, Object> entry : result.entrySet()){

				String columnName = entry.getKey().toLowerCase();
				String pkValue = null;
				if(columnName.equals(pkColumnName)){
					pkValue = entry.getValue().toString().trim();
					recordValue[0] = pkValue;
				}else if(MLColumnNameIndex.containsKey(columnName)){
					int nArrayPos = MLColumnNameIndex.get(columnName).intValue();
					if(entry.getValue() != null){
						String mlColumnValue = entry.getValue().toString().trim();		
						recordValue[nArrayPos + 1] = mlColumnValue;
					}
					
				}
			}
			
			for(int i=0; i<recordValue.length; i++){
				String value = recordValue[i];
				if(value != null){
					values.append(csvEncode(value));
				}
				if(i <recordValue.length -1){
					values.append(",");
				}
			}
			writer.write(values.toString()+IOUtils.LINE_SEPARATOR);			
		}		
	}

	
	private String csvEncode(String entity)
	{
		if(entity==null) return entity;
		boolean needQuote = false;
		if(entity.indexOf('"')!=-1)
		{
			entity = entity.replace("\"", "\"\"");
			
			needQuote = true;
		}
		if(entity.indexOf(',')!=-1)
		{
			needQuote = true;
		}
		if(needQuote)
		{
			entity = "\""+entity+"\"";
		}
		return entity;	
			
		
	}
	/**
	 * 根据单表结果信息生成sql语句。
	 * 
	 * @param resultSet
	 * @param separator
	 * @return
	 */
//	private String geneInsertSql(SqlQueryResultSet resultSet, String separator){
//		StringBuilder sql = new StringBuilder();
//		for(Map<String, Object> result : resultSet.getResults()){
//			ITable table = resultSet.getTable();
//			StringBuilder cols = new StringBuilder(), values = new StringBuilder(), 
//				singleSql = new StringBuilder("insert into ").append(resultSet.getTable().getName())
//					.append("(");
//			for(Map.Entry<String, Object> entry : result.entrySet()){
//				cols.append(entry.getKey()).append(",");
//				IColumn col = table.getColumnByName(entry.getKey());
//				values.append(SqlUtil.formatSql(entry.getValue(), col.getDataType())).append(",");
//			}
//			cols.deleteCharAt(cols.length() - 1);
//			values.deleteCharAt(values.length() - 1);
//			singleSql.append(cols.toString()).append(") values")
//				.append("(").append(values.toString()).append(")")
//				.append(separator);
//			sql.append(singleSql);
//		}
//		return sql.toString();
//	}
	
	/**
	 * 根据单表结果信息生成sql语句写入Writer。
	 * 
	 * @param resultSet
	 * @param separator
	 * @param writer
	 * @throws IOException
	 */
	private void geneInsertSql(SqlQueryResultSet resultSet, String separator, Writer writer, MLTableMetaInfo tableMLMetaInfo) throws IOException{
		ITable table = resultSet.getTable();		
		List<IColumn> pkColumns = table.getPkConstraint().getColumns();
		boolean bHasPK = pkColumns!=null && pkColumns.size()>0 ? true :false;
		List<String> lstPKColumnName = null;
		StringBuilder sbPKColumns = new StringBuilder();
		if(bHasPK){
			lstPKColumnName = new ArrayList<String>(pkColumns.size());
			for (Iterator iter = pkColumns.iterator(); iter.hasNext();) {
				IColumn column = (IColumn) iter.next();
				String columnName = column.getName();
				lstPKColumnName.add(columnName);
				sbPKColumns.append(columnName).append(",");			
			}
		}

		for(Map<String, Object> result : resultSet.getResults()){
			StringBuilder cols = new StringBuilder(), values = new StringBuilder(), 
				singleSql = new StringBuilder("insert into ").append(table.getName())
					.append("(");
			StringBuilder sbPKValues = new StringBuilder();
			if(bHasPK){
				for (Iterator iter = lstPKColumnName.iterator(); iter.hasNext();) {
					String pkColumnName = (String) iter.next();
					IColumn col = table.getColumnByName(pkColumnName);
					sbPKValues.append(SqlUtil.formatSql(result.get(pkColumnName), col.getDataType())).append(",");
				}
			}
			
			for(Map.Entry<String, Object> entry : result.entrySet()){				
				String columnName = entry.getKey();
				if(bHasPK && lstPKColumnName.contains(columnName)){
					continue;
				}
				IColumn col = table.getColumnByName(columnName);
				if(isBlobColumn(col)){
					cols.append(columnName).append(",");
					values.append("null,");
					continue;
				}
				if(tableMLMetaInfo != null && tableMLMetaInfo.includeColumn(columnName)){
					char endChar = columnName.charAt(columnName.length()-1);
					if(!Character.isDigit(endChar)){
						cols.append(columnName).append(",");
						values.append("'~'").append(",");
					}
					continue;
				}
				cols.append(columnName).append(",");

				values.append(SqlUtil.formatSql(entry.getValue(), col.getDataType())).append(",");
			}
			cols.deleteCharAt(cols.length() - 1);
			values.deleteCharAt(values.length() - 1);
			if(bHasPK){
				singleSql.append(sbPKColumns);
			}
			singleSql.append(cols.toString()).append(") values").append("(");
			if(bHasPK){
				singleSql.append(sbPKValues.toString());
			}
			singleSql.append(values.toString()).append(")").append(separator);
			writer.write(singleSql.toString());
		}
	}

	
	/**
	 * 判断某个字段是否为blob字段
	 * @param col
	 * @return
	 */
	private boolean isBlobColumn(IColumn col) {
		return col.getTypeName().equalsIgnoreCase("image") || col.getTypeName().equalsIgnoreCase("blob") || col.getTypeName().equalsIgnoreCase("blob(128m)");
	}
	
	/**
	 * 将主子表结果生成sql语句写入Writer中。
	 * 
	 * @param resultSet
	 * @param separator
	 * @param writer
	 * @throws IOException
	 */
	private void geneInsertSqlWithMainAndSub(SqlQueryResultSet resultSet, String separator, Writer writer) 
				throws IOException {
		if(!resultSet.getResults().isEmpty()){
			geneInsertSql(resultSet, separator, writer, null);
			//...
//			if(!resultSet.getSubResults().isEmpty()){
//				for(SqlQueryResultSet subResultSet : resultSet.getSubResults()){
//					geneInsertSqlWithMainAndSub(subResultSet, separator, writer);
//				}
//			}
		}
	}
	
	
	/**
	 * 
	 */
	private void geneSqlFilesByGroup(IDbRecordScript script, String rootDirName, Connection conn, String oidMarkRuleFilePath, String department, Map<String, MLTableMetaInfo> mlTableMetaInfo, List<File> mlScriptDir){
		//2.3.1 获取主表数据
		SqlQueryResultSet singleResultSet = SqlUtil.queryResults(script.getTable(), script.getWhereCondition(), conn);
		if(singleResultSet == null){
			return;
		}
		
		OidMarkValidator validator = new OidMarkValidator(oidMarkRuleFilePath);
		try{
			//2.3.2 主表数据进行分组
			List<Map<String, Object>> mainTableResult = singleResultSet.getResults();
			Map<Object, List<Map<String, Object>>> grpMapResults = new HashMap<Object, List<Map<String,Object>>>();
			for(Map<String, Object> temp : mainTableResult){
				Object grpValue = temp.get(script.getGrpField());
				List<Map<String, Object>> grpValues = null;
				if(!grpMapResults.containsKey(grpValue)){
					grpValues = new ArrayList<Map<String,Object>>();
					grpMapResults.put(grpValue, grpValues);
				}else{
					grpValues = grpMapResults.get(grpValue);
				}
				grpValues.add(temp);
			}
			//2.3.3 对每一组获取子表数据。
			for(Map.Entry<Object, List<Map<String, Object>>> entry : grpMapResults.entrySet()){
				SqlQueryResultSet grpResultSet = new SqlQueryResultSet(script.getTable());
				grpResultSet.getResults().addAll(entry.getValue());
				Object grpFlag = entry.getKey();
				String grpSqlFileDirName = new StringBuilder(rootDirName).append(File.separator)
					.append(script.getTable().getName()).append("_").append(grpFlag).toString().trim();
				if(!script.getChilds().isEmpty()){
					//2.3.3.1特殊处理pub_print_template
					if("pub_print_template".equalsIgnoreCase(script.getTable().getName())){
						IColumn grpCol = script.getTable().getColumnByName(script.getGrpField());
						QueryInfo printItemQry = new QueryInfo();
						ITable table = SqlUtil.retrieveTable("pub_print_dataitem", null, conn);
						printItemQry.table = table;
						printItemQry.whereCondition = "vnodecode = " + SqlUtil.formatSql(grpFlag, grpCol.getDataType());
						SqlQueryResultSet printItemQryResult = queryResult(printItemQry, conn);
						if(printItemQryResult != null){
							File dir = new File(grpSqlFileDirName);
							if(!dir.exists() && !dir.mkdirs()){
								throw new SdpBuildRuntimeException("创建目录" + grpSqlFileDirName + "失败。");
							}
							Writer writer = null;
							String sqlFileName = grpSqlFileDirName + File.separator + "006.sql";
							try {
								writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFileName), "UTF-8"));
								geneInsertSql(printItemQryResult, SQL_DELIMITER, writer, null);
								writer.flush();
							} catch (IOException e) {
								MainPlugin.getDefault().logError("写入文件" + sqlFileName + "失败。",e);
								throw new SdpBuildRuntimeException("写入文件" + sqlFileName + "失败。");
							} finally{
								IOUtils.closeQuietly(writer);
							}
						}
					}
					
					//2.3.3.2 正常处理子表。
					List<String> pkValues = new ArrayList<String>();
					IPkConstraint pkConstraint = script.getTable().getPkConstraint();
					List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
					if(pkCols == null || pkCols.isEmpty()){
						throw new SdpBuildRuntimeException("表"+script.getTable().getName() + "不存在主键。");
					}else if(pkCols.size() != 1){
						throw new SdpBuildRuntimeException("表"+script.getTable().getName() + "为复合主键，不支持子表查询。");
					}
					IColumn pkCol = pkCols.get(0);
					for(Map<String, Object> colNameValue : entry.getValue()){
						String pkValue = SqlUtil.formatSql(colNameValue.get(pkCol.getName()), pkCol.getDataType());
						pkValues.add(pkValue);
					}
					for(IDbRecordScript subRecordScript : script.getChilds()){
						List<IFkConstraint> fkConstraints = subRecordScript.getTable().getFkConstraints();
						if(fkConstraints == null || fkConstraints.size() != 1 || fkConstraints.get(0).getColumns().size() != 1){
							throw new SdpBuildRuntimeException("子表"+subRecordScript.getTable().getName() + "为复合外键，不支持。");
						}
						QueryInfo temp = new QueryInfo();
						temp.table = subRecordScript.getTable();
						temp.whereCondition = SqlUtil.geneInClause(fkConstraints.get(0).getColumns().get(0).getName(), pkValues);
						temp.childs = subRecordScript.getChilds();
						SqlQueryResultSet subResult = queryResult(temp, conn);
						if(subResult != null){
							grpResultSet.getSubResultSets().add(subResult);
						}
					}
				}
				//check
				checkOidMark(grpResultSet, validator, department);
				
				geneSqlFile(grpResultSet, grpSqlFileDirName, SQL_DELIMITER, script, mlTableMetaInfo, mlScriptDir, grpFlag == null ? null: grpFlag.toString());
			}
		}finally{
			handleOidValidateResult(validator);
		}
	}
	
	/**
	 * 校验OIDMark
	 * 
	 * @param resultSet
	 * @param validator
	 * @param department
	 */
	private void checkOidMark(SqlQueryResultSet resultSet,OidMarkValidator validator, String department){
		ITable table = resultSet.getTable();
		if(validator.isValidationNeed(table.getName(), department)){
			if(table.getPkConstraint() != null && !table.getPkConstraint().getColumns().isEmpty()){
				List<Map<String, Object>> results = resultSet.getResults();
				for(IColumn pkCol : table.getPkConstraint().getColumns()){
					List<String> pkValues = new ArrayList<String>();
					//retrieve pk values.
					for(Map<String, Object> result :results){
						pkValues.add(result.get(pkCol.getName()).toString());
					}
					validator.validate(table.getName(), department, pkCol.getName(), pkValues);
				}
			}
		}
	}
	
	/**
	 * 处理OIDMark校验结果：为处理分组导出的特殊性。
	 * 
	 * @param validator
	 */
	private void handleOidValidateResult(OidMarkValidator validator){
		for(Map.Entry<String, PkInfo> entry : validator.getResults().entrySet()){
			PkInfo pkInfo = entry.getValue();
			for(Map.Entry<String, List<String>> pkNameValuesEntry : pkInfo.getPkNameMapIllegalValues().entrySet()){
				StringBuilder oidWarnMsg = new StringBuilder("表").append(entry.getKey()).append("的主键(")
					.append(pkNameValuesEntry.getKey())
					.append(")不符合OIDMark规则").append(pkInfo.getOidMarks()).append("。以下值不符合：");
				for(String illegalPk : pkNameValuesEntry.getValue()){
					oidWarnMsg.append(IOUtils.LINE_SEPARATOR).append("       ").append(illegalPk);
				}
				String str = oidWarnMsg.toString();
				OperateLogger.getInstance().addLog(LogLevel.WARN, str);
				MainPlugin.getDefault().logInfo(str);
			}
		}
	}
	
	/**
	 * 分组导出的另一种实现方式。先查询全部数据，在内存中进行分组。
	 * 
	 */
/*	private void geneSqlFilesByGroup_1(IDBRecordScript script, String rootDirName, Connection conn){
		//1.查询主子表结果。
		SqlQueryResultSet result = queryResult(script, conn);
		if(result == null || result.getResults().isEmpty()){
			return;
		}
		//2.据主表分组字段进行分组
		String pkColName = null;
		boolean hasChilds = !script.getChilds().isEmpty();
		if(hasChilds){
			pkColName = script.getTable().getPkConstraint().getColumns().get(0).getName();
		}
		List<Map<String, Object>> mainTableResult = result.getResults();
		Map<Object, List<Map<String, Object>>> grpMapResults = new HashMap<Object, List<Map<String,Object>>>();
		Map<Object, List<Object>> grpMapPkValues = new HashMap<Object, List<Object>>();
		for(Map<String, Object> temp : mainTableResult){
			Object grpValue = temp.get(script.getGrpField());
			List<Map<String, Object>> grpValues = null;
			List<Object> pkValues = null;
			if(!grpMapResults.containsKey(grpValue)){
				grpValues = new ArrayList<Map<String,Object>>();
				grpMapResults.put(grpValue, grpValues);
				if(hasChilds){
					pkValues = new ArrayList<Object>();
					grpMapPkValues.put(grpValue, pkValues);
				}
				
			}else{
				grpValues = grpMapResults.get(grpValue);
				if(hasChilds){
					pkValues = grpMapPkValues.get(grpValue);
				}
			}
			grpValues.add(temp);
			if(hasChilds){
				pkValues.add(temp.get(pkColName));
			}
		}
		
		for(Map.Entry<Object, List<Map<String, Object>>> entry : grpMapResults.entrySet()){
			SqlQueryResultSet grpResultSet = new SqlQueryResultSet(script.getTable());
			grpResultSet.getResults().addAll(entry.getValue());
			String grpSqlFileDirName = new StringBuilder(rootDirName).append(File.separator)
				.append(script.getTable().getName()).append("_").append(entry.getKey()).toString();
			if(hasChilds){
				//2.3.3.1特殊处理pub_print_template
				if("pub_print_template".equalsIgnoreCase(script.getTable().getName())){
					IColumn grpCol = script.getTable().getColumnByName(script.getGrpField());
					QueryInfo printItemQry = new QueryInfo();
					ITable table = SqlUtil.retrieveTable("pub_print_dataitem", null, conn);
					printItemQry.table = table;
					printItemQry.whereCondition = "vnodecode = " + SqlUtil.formatSql(entry.getKey(), grpCol.getDataType());
					SqlQueryResultSet printItemQryResult = queryResult(printItemQry, conn);
					Writer writer = null;
					String sqlFileName = grpSqlFileDirName + File.separator + "006.sql";
					try {
						writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFileName), "gbk"));
						geneInsertSql(printItemQryResult, SQL_DELIMITER, writer);
					} catch (IOException e) {
						MainPlugin.getDefault().logError("写入文件" + sqlFileName + "失败。",e);
						throw new SdpBuildRuntimeException("写入文件" + sqlFileName + "失败。");
					} finally{
						IOUtils.closeQuietly(writer);
					}
				}
				
				//2.3.3.2 正常处理子表。
				grpResultSet.getSubResultSets().addAll(filterSubResults(result.getSubResultSets(), grpMapPkValues.get(entry.getKey())));
			}
			geneSqlFile(grpResultSet, grpSqlFileDirName, SQL_DELIMITER, script);
		}
	}*/
	
	
/*	private List<SqlQueryResultSet> filterSubResults(List<SqlQueryResultSet> subResultSets, List<Object> parentPkValues){
		List<SqlQueryResultSet> filteredResultSets = new ArrayList<SqlQueryResultSet>();
		for(SqlQueryResultSet resultSet : subResultSets){
			ITable table = resultSet.getTable();
			//have been checked when querying.
			String fkColName = table.getFkConstraints().get(0).getColumns().get(0).getName();
			String pkColName = table.getPkConstraint().getColumns().get(0).getName();
			List<Map<String, Object>> filteredResults = new ArrayList<Map<String,Object>>();
			List<Object> pkValues = new ArrayList<Object>();
			List<Map<String, Object>> results = resultSet.getResults();
			for(Map<String, Object> result : results){
				if(parentPkValues.contains(result.get(fkColName))){
					filteredResults.add(result);
					pkValues.add(result.get(pkColName));
				}
			}
			if(!filteredResults.isEmpty()){
				SqlQueryResultSet currQryResultSet = new SqlQueryResultSet(resultSet.getTable());
				filteredResultSets.add(currQryResultSet);
				currQryResultSet.getResults().addAll(filteredResults);
				if(!resultSet.getSubResultSets().isEmpty()){
					currQryResultSet.getSubResultSets().addAll(filterSubResults(resultSet.getSubResultSets(), pkValues));
				}
			}
		}
		
		return filteredResultSets;
	}*/
	
	/**
	 * 查询条件。
	 */
	static class QueryInfo implements IQueryInfo{
		
		private ITable table;
		
		private String whereCondition;
		
		private List<? extends IQueryInfo> childs;

		public List<? extends IQueryInfo> getChilds() {
			return childs;
		}

		public ITable getTable() {
			return table;
		}

		public String getWhereCondition() {
			return whereCondition;
		}
	}


}
