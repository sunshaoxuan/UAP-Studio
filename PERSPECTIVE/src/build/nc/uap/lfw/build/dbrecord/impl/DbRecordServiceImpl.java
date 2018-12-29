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
 * Ԥ�ýű�ҵ��ʵ�֡�
 * 
 * @author PH
 */
public class DbRecordServiceImpl implements IDbRecordService {
	
	/** insert���ָ��� */
	private static final String SQL_DELIMITER = IOUtils.LINE_SEPARATOR + "go" + IOUtils.LINE_SEPARATOR;
	
		
	public IDbRecordScript retrieveDBRecordScript(DbRecordConfig cfg, Connection conn) throws SdpBuildRuntimeException {
		MainTableRecordItem mainTableItem = cfg.getMainTableItem();
		if(mainTableItem == null){
			throw new IllegalArgumentException("δָ��Ԥ�ýű�������������");
		}
		OperateLogger.getInstance().addLog(LogLevel.INFO, new StringBuilder("��ʼ������").append(mainTableItem.getTableName()).append("��").toString());
		DbRecordCfgParser cfgParser = new DbRecordCfgParser(cfg, conn);
		return cfgParser.getDBRecordScript();
	}
	
	public SqlQueryResultSet queryResult(IQueryInfo queryInfo, Connection conn) throws SdpBuildRuntimeException{
		return SqlUtil.queryResults(queryInfo, conn);
	}
	
	public ISqlFile[] geneSqlFile(IDbRecordScript script, Connection conn, DbRecordSqlFileCfg sqlFileCfg, Map<String, MLTableMetaInfo> mlTableMetaInfo) 
			throws SdpBuildRuntimeException{
		try{
			//1.��ȡ����Ŀ¼·����ɾ����·��������Ŀ¼�ļ���
			String rootDirName = sqlFileCfg.getAbsolutePath(script.getTable().getName());
			if(StringUtils.isBlank(rootDirName)){
				throw new SdpBuildRuntimeException("��" + script.getTable().getName() + "��.map�ļ���δ���á�");
			}
			File rootDir = new File(rootDirName);
			if(rootDir.exists()){
				try {
					FileUtils.forceDelete(rootDir);
				} catch (IOException e) {
					MainPlugin.getDefault().logError("ɾ��Ŀ¼" + rootDirName + "ʧ��", e);
					throw new SdpBuildRuntimeException("ɾ��Ŀ¼" + rootDirName + "ʧ�ܡ�");
				}
			}
			//Ϊ����CC�ϴ�����
			try {
				FileUtils.forceMkdir(rootDir);
			} catch (IOException e) {
				MainPlugin.getDefault().logError("����Ŀ¼" + rootDirName + "ʧ��", e);
				throw new SdpBuildRuntimeException("����Ŀ¼" + rootDirName + "ʧ�ܡ�");
			}
			
			//2.��ѯ���ݼ�¼�������ļ���
			//2.1 �����Զ����ѯ���������ݡ�
			if(DbRecordSqlFileCfg.CUSTOM_QUERY.equalsIgnoreCase(script.getTableDesc())){
//				exportBinary(sqlFileCfg, rootDirName, script, conn);
				return new ISqlFile[]{new DbRecordSqlFile(rootDir)};
			}
			
//			if(DbRecordSqlFileCfg.FREE_REPORT.equalsIgnoreCase(script.getTableDesc())){
//				exportFreeReport(sqlFileCfg, rootDirName, script, conn);
//				return new ISqlFile[]{new DbRecordSqlFile(rootDir)};
//			}
			
			
			List<File> mlScriptDir = new ArrayList<File>(); 
			//2.2 ������
			if(StringUtils.isBlank(script.getGrpField())){
				SqlQueryResultSet resultSet = queryResult(script, conn);
				if(resultSet != null){
					//У��OIDMark
					OidMarkValidator validator = new OidMarkValidator(sqlFileCfg.getOidMarkRuleFilePath());
					checkOidMark(resultSet, validator, sqlFileCfg.getDepartment());
					handleOidValidateResult(validator);
					
					geneSqlFile(resultSet, rootDirName, SQL_DELIMITER, script, mlTableMetaInfo, mlScriptDir, null);
				}
			}else{
				//2.3 ����
				geneSqlFilesByGroup(script, rootDirName, conn, sqlFileCfg.getOidMarkRuleFilePath(), sqlFileCfg.getDepartment(), mlTableMetaInfo, mlScriptDir);
			}
			MainPlugin.getDefault().logInfo("Ԥ�ýű���" + script.getTableName() + "���ɳɹ���");
			OperateLogger.getInstance().addLog(LogLevel.INFO, "��" + script.getTableName() + "���ɳɹ���");
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
			MainPlugin.getDefault().logError("Ԥ�ýű���" + script.getTableName() + "����ʧ��: " + e.getMessage());
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "��" + script.getTableName() + "����ʧ�ܣ�" + e.getMessage());
			throw e;
		}
	}

	/**
	 * ���ɱ�����
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
//					String errorMsg = "FMD���������ɱ���" + script.getTable().getName() + "��������";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "�����˸�ʽĿ¼��������δ���ø�ʽĿ¼����ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		if(sqlFileCfg.isQmdEnabledOfFR()){
//			String qid = sqlFileCfg.getQidOfFR();
//			if(qid != null && !(qid = qid.trim()).equals("")){
//				if(!ExportUtil.onExportBinary(qid, "", false, rootDirName, conn)){
//					String errorMsg = "QMD���������ɱ���" + script.getTable().getName() + "��������";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "�����˲�ѯĿ¼��������δ���ø�ʽĿ¼����ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		
//	}

	public Map<String, String> preViewScriptResult(IDbRecordScript dbRecordScript, Connection conn)
			throws SdpBuildRuntimeException {//���������ʾ����?
//		if(StringUtils.isBlank(dbRecordScript.getGrpField())){
//			
//		}
		//Ŀǰֻ��ѯ��������
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
				throw new SdpBuildRuntimeException("��" + dbRecordScript.getTable().getName() + "����sqlʧ�ܡ�");
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
				throw new SdpBuildRuntimeException("��" + script.getTable().getName() + "��.map�ļ���δ���á�");
			}
			OperateLogger.getInstance().addLog(LogLevel.INFO, cfg.getMainTableItem().getTableName() + "У��ɹ���");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, cfg.getMainTableItem().getTableName() + "У��ʧ�ܣ�" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * �������������ݡ�
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
//					String errorMsg = "FMD�������Զ����ѯ" + script.getTable().getName() + "��������";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "�����˸�ʽĿ¼��������δ���ø�ʽĿ¼����ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//		if(sqlFileCfg.isQmdEnabledOfQE()){
//			String qid = sqlFileCfg.getQidOfQE();
//			if(qid != null && !(qid = qid.trim()).equals("")){
//				if(!nc.vo.pub.oyx.ExportUtil.onExportBinary(qid, "", false, rootDirName, conn)){
//					String errorMsg = "QMD�������Զ����ѯ" + script.getTable().getName() + "��������";
//					MainPlugin.getDefault().logError(errorMsg);
//					throw new SdpBuildRuntimeException(errorMsg);
//				}
//			}else{
//				String warnMsg = script.getTable().getName() + "�����˲�ѯĿ¼��������δ���ø�ʽĿ¼����ID";
//				MainPlugin.getDefault().logInfo(warnMsg);
//				OperateLogger.getInstance().addLog(LogLevel.WARN, warnMsg);
//			}
//		}
//	}

	/**
	 * ���ݽ��������(insert)sql�ļ���
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
			throw new SdpBuildRuntimeException("����Ŀ¼" + dir + "ʧ�ܡ�");
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
		
		//1. ����
		Writer writer = null;
		try {
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFileName), "UTF-8"));
			geneInsertSql(resultSet, separator, writer, tableMLMetaInfo);
		} catch (IOException e) {
			MainPlugin.getDefault().logError("д���ļ�" + sqlFileName + "ʧ�ܡ�",e);
			throw new SdpBuildRuntimeException("д���ļ�" + sqlFileName + "ʧ�ܡ�");
		} finally{
			IOUtils.closeQuietly(writer);
		}
		
		List<IColumn> lstBlobColumn = getBlobColumns(table);
		if(lstBlobColumn != null && lstBlobColumn.size()>0){
			
			IPkConstraint pkConstraint = table.getPkConstraint();
			List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
			if(pkCols == null || pkCols.isEmpty()){
				throw new SdpBuildRuntimeException("��:"+ table.getName()+"û���������޷��������ݿ�blob�ֶδ洢�ļ�");
			}
			if(pkCols.size() != 1){
				throw new SdpBuildRuntimeException("��:"+ table.getName() + "Ϊ�����������޷��������ݿ�blob�ֶδ洢�ļ�");
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
				throw new SdpBuildRuntimeException("����Ŀ¼" + mlRecordFilePath + "ʧ�ܡ�");
			}
			
			
			String mlRecordFileName = mlRecordFilePath + File.separator + (grpFlag == null ? "001.csv" : convertToWinSystemFileName(grpFlag)+".csv");
			try{
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mlRecordFileName), "utf-8"));
				geneMLUpdateSql(resultSet, writer, tableMLMetaInfo);
				mlScriptDir.add(new File(mlRecordFileName).getParentFile());
			} catch (IOException e) {
				MainPlugin.getDefault().logError("д���ļ�" + sqlFileName + "ʧ�ܡ�",e);
				throw new SdpBuildRuntimeException("д���ļ�" + sqlFileName + "ʧ�ܡ�");
			} finally{
				IOUtils.closeQuietly(writer);
			}

		}
		
		//2. �ӱ�
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
	 * ����ַ������Ƿ����windowsϵͳ�ļ���������������ַ����������ת����'&'
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
	 * ���ɱ���Blob�ֶ����ݵĶ������ļ�
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
			MainPlugin.getDefault().logError("����Blob�ļ�ʱ��������ת����["+tableName+"]����Ϊ�ֽ�����ʱ��������", e);
			throw new SdpBuildRuntimeException("����Blob�ļ�ʱ��������ת����["+tableName+"]����Ϊ�ֽ�����ʱ��������", e);
		}
		if(btTableName.length>40){
			throw new SdpBuildRuntimeException("����" + tableName + "���ȳ���40���ֽ�");
		}
		byte[] outputTableName = new byte[40];
		System.arraycopy(btTableName, 0, outputTableName, 0, btTableName.length);
		byte[] btPKColumnName = null;
		try {
			btPKColumnName = pkColumnName.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			MainPlugin.getDefault().logError("����Blob�ļ�ʱ��������ת����["+tableName+"]������["+pkColumnName+"]����Ϊ�ֽ�����ʱ��������", e);
			throw new SdpBuildRuntimeException("����Blob�ļ�ʱ��������ת����["+tableName+"]������["+pkColumnName+"]����Ϊ�ֽ�����ʱ��������", e);
		}
		if(btPKColumnName.length>40){
			throw new SdpBuildRuntimeException("����" + tableName + "�е�������"+pkColumnName + "���ȳ���40���ֽ�");
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
				MainPlugin.getDefault().logError("����Blob�ļ�ʱ��������ת����["+tableName+"]��Blob�ֶ�["+aryBlobColumnName[i]+"]����Ϊ�ֽ�����ʱ��������", e);
				throw new SdpBuildRuntimeException("����Blob�ļ�ʱ��������ת����["+tableName+"]��Blob�ֶ�["+aryBlobColumnName[i]+"]����Ϊ�ֽ�����ʱ��������", e);
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
						
						//�������--40���ֽ�
						out.write(outputTableName);
						//���������--40���ֽ�
						out.write(outputPKColumnName);
						//���Blob�ֶθ����ֽ�--1���ֽ�
						out.write(new byte[]{btBlobColumnCount});
						//���Blob�ֶ���--ÿ���ֶ���40���ֽ�
						for(int ii = 0; ii<outputBlobColumnNames.length; ii++){
							out.write(outputBlobColumnNames[ii]);
						}
						
						//�������ֵ�ĳ���--1���ֽ�
						byte[] outputPKValue = pkValue.getBytes("utf-8");
						byte pkValueLen = (byte)outputPKValue.length;
						out.write(new byte[]{pkValueLen});
						
						//�������ֵ
						out.write(outputPKValue);
						
						//������blob�ֶε�ֵ
						for(int jj = 0; jj<aryBlobColumnName.length; jj++){
							IColumn blobColumn = resultSet.getTable().getColumnByName(aryBlobColumnName[jj]);
							Object obj = result.get(aryBlobColumnName[jj]);
							if(obj == null){
								//���blob�ֶ�ֵ�ĳ���--4�ֽ� ����ʱΪ0��
								out.write(0);								
							}else{
								if(blobColumn.getTypeName().equalsIgnoreCase("image")){
									//SQL Server��image����
									if(obj instanceof byte[]) {
										byte[] blobByte = (byte[])obj;
										if(blobByte == null){							
											//���blob�ֶ�ֵ�ĳ���--4�ֽ� ����ʱΪ0��
											out.write(0);
										}else{
											//���blob�ֶ�ֵ�ĳ���--4�ֽ�
											out.writeInt(blobByte.length);
											//���blob�ֶ�ֵ
											out.write(blobByte);								
										}									
									}else{
										String errorInfo = "��ȡ��["+tableName+"]��Image�ֶ�["+ aryBlobColumnName[jj]+"]��ֵʱ�������Ͳ���Iamge����";
										MainPlugin.getDefault().logError(errorInfo);
										throw new SdpBuildRuntimeException(errorInfo);
									}															
								}else if(blobColumn.getTypeName().equalsIgnoreCase("blob")){								
									if (obj instanceof Blob) {
										Blob blobValue = (Blob) obj;
										if(blobValue == null){
											//���blob�ֶ�ֵ�ĳ���--4�ֽ� ����ʱΪ0��
											out.write(0);
										}
										else{
											byte[] blobByte = null;
											try{
												blobByte= blobValue.getBytes(1, (int)blobValue.length());
											}catch(SQLException e){
												String errorInfo = "��ȡ��["+tableName+"]��Blob�ֶ�["+ aryBlobColumnName[jj]+"]��ֵʱ��������"+e.getMessage();
												MainPlugin.getDefault().logError(errorInfo, e);
												throw new SdpBuildRuntimeException(errorInfo);
											}
											//���blob�ֶ�ֵ�ĳ���--4�ֽ�
											out.writeInt(blobByte.length);
											//���blob�ֶ�ֵ
											out.write(blobByte);
										}															
									}else{
										String errorInfo = "��ȡ��["+tableName+"]��Blob�ֶ�["+ aryBlobColumnName[jj]+"]��ֵʱ�������Ͳ���Blob����";
										MainPlugin.getDefault().logError(errorInfo);
										throw new SdpBuildRuntimeException(errorInfo);
									}
								}else if(blobColumn.getTypeName().equalsIgnoreCase("blob(128m)")){
									String errorInfo = "ϵͳ�ݲ�֧�ֶ�DB2���ݿ��["+tableName+"]�Ķ������ֶ�["+ aryBlobColumnName[jj]+"]���ݵ���";
									MainPlugin.getDefault().logInfo(errorInfo);
									throw new SdpBuildRuntimeException(errorInfo);
								}
							}
						}
						out.flush();
					}catch (IOException e){
						MainPlugin.getDefault().logError("д���ļ�" + blobFileName + "ʧ�ܡ�",e);
						throw new SdpBuildRuntimeException("д���ļ�" + blobFileName + "ʧ�ܡ�");
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
	 * 	��ȡ����Blob�ֶ�
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
	 * ���ɱ�Ķ����ֶζ�Ӧ���½ű���Ҫ�������ļ�
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
			throw new SdpBuildRuntimeException("��:"+ table.getName()+"û���������޷��������ݿ�ű������ļ�");
		}		
		if(pkCols.size() != 1){
			throw new SdpBuildRuntimeException("��:"+ table.getName() + "Ϊ�����������޷��������ݿ�ű������ļ�");
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
			throw new SdpBuildRuntimeException("��" + table.getName() +"δ�ҵ������ֶΣ��޷����ɶ���Ԥ�ýű�");
		}
		//��һ��Ϊ����
		writer.write(csvEncode(table.getName())+IOUtils.LINE_SEPARATOR);
		//�ڶ���Ϊ�����Ͷ����ֶ���
		writer.write(cols.toString()+IOUtils.LINE_SEPARATOR);
		
		//֮���Ǿ���ļ�¼��Ϣ
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
	 * ���ݵ�������Ϣ����sql��䡣
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
	 * ���ݵ�������Ϣ����sql���д��Writer��
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
	 * �ж�ĳ���ֶ��Ƿ�Ϊblob�ֶ�
	 * @param col
	 * @return
	 */
	private boolean isBlobColumn(IColumn col) {
		return col.getTypeName().equalsIgnoreCase("image") || col.getTypeName().equalsIgnoreCase("blob") || col.getTypeName().equalsIgnoreCase("blob(128m)");
	}
	
	/**
	 * �����ӱ�������sql���д��Writer�С�
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
		//2.3.1 ��ȡ��������
		SqlQueryResultSet singleResultSet = SqlUtil.queryResults(script.getTable(), script.getWhereCondition(), conn);
		if(singleResultSet == null){
			return;
		}
		
		OidMarkValidator validator = new OidMarkValidator(oidMarkRuleFilePath);
		try{
			//2.3.2 �������ݽ��з���
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
			//2.3.3 ��ÿһ���ȡ�ӱ����ݡ�
			for(Map.Entry<Object, List<Map<String, Object>>> entry : grpMapResults.entrySet()){
				SqlQueryResultSet grpResultSet = new SqlQueryResultSet(script.getTable());
				grpResultSet.getResults().addAll(entry.getValue());
				Object grpFlag = entry.getKey();
				String grpSqlFileDirName = new StringBuilder(rootDirName).append(File.separator)
					.append(script.getTable().getName()).append("_").append(grpFlag).toString().trim();
				if(!script.getChilds().isEmpty()){
					//2.3.3.1���⴦��pub_print_template
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
								throw new SdpBuildRuntimeException("����Ŀ¼" + grpSqlFileDirName + "ʧ�ܡ�");
							}
							Writer writer = null;
							String sqlFileName = grpSqlFileDirName + File.separator + "006.sql";
							try {
								writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sqlFileName), "UTF-8"));
								geneInsertSql(printItemQryResult, SQL_DELIMITER, writer, null);
								writer.flush();
							} catch (IOException e) {
								MainPlugin.getDefault().logError("д���ļ�" + sqlFileName + "ʧ�ܡ�",e);
								throw new SdpBuildRuntimeException("д���ļ�" + sqlFileName + "ʧ�ܡ�");
							} finally{
								IOUtils.closeQuietly(writer);
							}
						}
					}
					
					//2.3.3.2 ���������ӱ�
					List<String> pkValues = new ArrayList<String>();
					IPkConstraint pkConstraint = script.getTable().getPkConstraint();
					List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
					if(pkCols == null || pkCols.isEmpty()){
						throw new SdpBuildRuntimeException("��"+script.getTable().getName() + "������������");
					}else if(pkCols.size() != 1){
						throw new SdpBuildRuntimeException("��"+script.getTable().getName() + "Ϊ������������֧���ӱ��ѯ��");
					}
					IColumn pkCol = pkCols.get(0);
					for(Map<String, Object> colNameValue : entry.getValue()){
						String pkValue = SqlUtil.formatSql(colNameValue.get(pkCol.getName()), pkCol.getDataType());
						pkValues.add(pkValue);
					}
					for(IDbRecordScript subRecordScript : script.getChilds()){
						List<IFkConstraint> fkConstraints = subRecordScript.getTable().getFkConstraints();
						if(fkConstraints == null || fkConstraints.size() != 1 || fkConstraints.get(0).getColumns().size() != 1){
							throw new SdpBuildRuntimeException("�ӱ�"+subRecordScript.getTable().getName() + "Ϊ�����������֧�֡�");
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
	 * У��OIDMark
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
	 * ����OIDMarkУ������Ϊ������鵼���������ԡ�
	 * 
	 * @param validator
	 */
	private void handleOidValidateResult(OidMarkValidator validator){
		for(Map.Entry<String, PkInfo> entry : validator.getResults().entrySet()){
			PkInfo pkInfo = entry.getValue();
			for(Map.Entry<String, List<String>> pkNameValuesEntry : pkInfo.getPkNameMapIllegalValues().entrySet()){
				StringBuilder oidWarnMsg = new StringBuilder("��").append(entry.getKey()).append("������(")
					.append(pkNameValuesEntry.getKey())
					.append(")������OIDMark����").append(pkInfo.getOidMarks()).append("������ֵ�����ϣ�");
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
	 * ���鵼������һ��ʵ�ַ�ʽ���Ȳ�ѯȫ�����ݣ����ڴ��н��з��顣
	 * 
	 */
/*	private void geneSqlFilesByGroup_1(IDBRecordScript script, String rootDirName, Connection conn){
		//1.��ѯ���ӱ�����
		SqlQueryResultSet result = queryResult(script, conn);
		if(result == null || result.getResults().isEmpty()){
			return;
		}
		//2.����������ֶν��з���
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
				//2.3.3.1���⴦��pub_print_template
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
						MainPlugin.getDefault().logError("д���ļ�" + sqlFileName + "ʧ�ܡ�",e);
						throw new SdpBuildRuntimeException("д���ļ�" + sqlFileName + "ʧ�ܡ�");
					} finally{
						IOUtils.closeQuietly(writer);
					}
				}
				
				//2.3.3.2 ���������ӱ�
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
	 * ��ѯ������
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
