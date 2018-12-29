package nc.uap.lfw.build.dbcreate.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbcreate.itf.IDbCreateService;
import nc.uap.lfw.build.dbcreate.pdm.Pdm;
import nc.uap.lfw.build.dbcreate.pdm.Pdm.ViewInfo;
import nc.uap.lfw.build.dbcreate.pdm.PdmUtil;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.orm.mapping.Column;
import nc.uap.lfw.build.orm.mapping.IColumn;
import nc.uap.lfw.build.orm.mapping.ITable;
import nc.uap.lfw.build.orm.mapping.Table;

import org.apache.commons.io.IOUtils;

/**
 * ����ű�ҵ��ʵ�֡�
 * 
 * @author PH
 */
public class DbCreateServiceImpl implements IDbCreateService {
	/** �� */
	private static final int DDL_TYPE_TABLE = 1;
	/** ��������� */
	private static final int DDL_TYPE_INDEX_AND_REFERENCE = 2;
	/** ��ͼ */
	private static final int DDL_TYPE_VIEW = 3;
	/** sql�ļ����� */
	private static final String SQL_FILE_ENCODING = "gb2312";
	
	private static final int MAX_PK_NAME_LENGTH_SQLSERVER = 30;
	private static final int MAX_PK_NAME_LENGTH_DB2 = 18;
	private static final int MAX_PK_NAME_LENGTH_ORACLE = 30;
	
	public void validatePdm(File pdmFile, boolean parseReference) {
		try{
			PdmUtil.validatePdm(pdmFile);
			Pdm pdm = PdmUtil.parsePdm(pdmFile, parseReference);
			String pdmFileName = pdmFile.getName();
			int index = -1;
			if((index = pdmFileName.lastIndexOf(".")) != -1){
				pdmFileName = pdmFileName.substring(0, index).toLowerCase();
			}
			pdm.setPdmName(pdmFileName);
			int origTableSize = pdm.getTables().size();
			checkPkConstaintNameLength(pdm);
			if(pdm.getTables().size() != origTableSize){
				throw new SdpBuildRuntimeException("����Լ�������ȳ��ޡ�");
			}
			OperateLogger.getInstance().addLog(LogLevel.INFO, "PDM(" + pdmFile.getName() + ")У��ɹ���");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "PDM(" + pdmFile.getName() + ")У��ʧ�ܣ�" + e.getMessage());
			throw e;
		}
	}

	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot) {
		List<DatabaseType> dbTypes = new ArrayList<DatabaseType>();
		dbTypes.add(DatabaseType.SQLSERVER);
		dbTypes.add(DatabaseType.ORACLE);
		dbTypes.add(DatabaseType.DB2);
		
		geneSqlFileInner(pdmFile, geneReference, sqlRoot, dbTypes);
	}
	
	public void geneSqlFile(File pdmFile, boolean geneReference, File sqlRoot, DatabaseType dbType) {
		List<DatabaseType> dbTypes = new ArrayList<DatabaseType>();
		dbTypes.add(dbType);
		
		geneSqlFileInner(pdmFile, geneReference, sqlRoot, dbTypes);
	}
	
	public void geneDataDictionary(File pdmFile, boolean geneReference, File ddRoot){
		
	}
	
	
	/**
	 * ��PDM����ָ�����ݿ�Ľ���ű���
	 * 
	 * @param pdmFile
	 * @param geneReference
	 * @param sqlRoot
	 * @param dbTypes
	 */
	private void geneSqlFileInner(File pdmFile, boolean geneReference, File sqlRoot, Collection<DatabaseType> dbTypes) {
		try{
			PdmUtil.validatePdm(pdmFile);
			Pdm pdm = PdmUtil.parsePdm(pdmFile, geneReference);
			String pdmFileName = pdmFile.getName();
			int index = -1;
			if((index = pdmFileName.lastIndexOf(".")) != -1){
				pdmFileName = pdmFileName.substring(0, index).toLowerCase();
			}
			pdm.setPdmName(pdmFileName);
			
			//У������Լ��������
			for(DatabaseType dbType : dbTypes){
				checkPkConstaintNameLength(pdm.getTables(), dbType, pdm.getPdmName());
			}
			
			for(DatabaseType dbType : dbTypes){
				geneSqlFileInner(pdm, sqlRoot, dbType);
			}
			
			OperateLogger.getInstance().addLog(LogLevel.INFO, "PDM(" + pdmFile.getName() + ")���ɳɹ���");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "PDM(" + pdmFile.getName() + ")����ʧ�ܣ�" + e.getMessage());
			throw e;
		}
	}

	/**
	 * ��Ԫ������Ϣ����SQL�ļ���
	 * 
	 * @param pdm Pdm
	 * @param sqlRoot ����Ŀ¼
	 * @param dbType ���ݿ�����
	 */
	private void geneSqlFileInner(Pdm pdm, File sqlRoot, DatabaseType dbType){
		//1. convertion
		pdm = getCvtedPdm(pdm, dbType);
		
		File tableSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_TABLE), 
			 indexAndRefSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_INDEX_AND_REFERENCE),
			 viewSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_VIEW);
		
		//2.1 ����Ŀ¼��ɾ���ļ���
		if(pdm.getTables().isEmpty()){
			tableSqlFile.delete();
		}else{
			if(!tableSqlFile.getParentFile().exists() && !tableSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("����Ŀ¼" + tableSqlFile.getParentFile().getAbsolutePath() + "ʧ�ܡ�");
			}
		}
		
		if(pdm.getIndexs().isEmpty() && pdm.getFkConstraints().isEmpty()){
			indexAndRefSqlFile.delete();
		}else{
			if(!indexAndRefSqlFile.getParentFile().exists() && !indexAndRefSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("����Ŀ¼" + indexAndRefSqlFile.getParentFile().getAbsolutePath() + "ʧ�ܡ�");
			}
		}
		
		if(pdm.getViews().isEmpty()){
			viewSqlFile.delete();
		}else{
			if(!viewSqlFile.getParentFile().exists() && !viewSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("����Ŀ¼" + viewSqlFile.getParentFile().getAbsolutePath() + "ʧ�ܡ�");
			}
		}
		
		//2.2  ����Sql�ļ���
		IDdlGenerator generator = DdlGeneratorFactory.getInstance(dbType);
		Writer tableWriter = null, indexAndReferWriter = null, viewWriter = null;
		try {
			if(!pdm.getTables().isEmpty()){
				tableWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(tableSqlFile), SQL_FILE_ENCODING));
				generator.geneCreateTableDdl(pdm.getTables(), tableWriter);
			}
			
			if(indexAndRefSqlFile.exists() && !indexAndRefSqlFile.delete()){
				throw new SdpBuildRuntimeException("ɾ���ļ�" + indexAndRefSqlFile.getAbsolutePath() + "ʧ�ܡ�");
			}
			if(!pdm.getIndexs().isEmpty() || !pdm.getFkConstraints().isEmpty()){
				indexAndReferWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(indexAndRefSqlFile, true), SQL_FILE_ENCODING));
				generator.geneCreateIndexDdl(pdm.getIndexs(), indexAndReferWriter);
				generator.geneAddConstraintDdl(pdm.getFkConstraints(), indexAndReferWriter);
			}
			
			if(!pdm.getViews().isEmpty()){
				viewWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(viewSqlFile), SQL_FILE_ENCODING));
				generator.geneCreateViewDdl(pdm.getViews(), viewWriter);
			}
		} catch (FileNotFoundException e) {
			MainPlugin.getDefault().logError("�ļ������ڡ�", e);
			throw new SdpBuildRuntimeException("�ļ������ڡ�");
		} catch (UnsupportedEncodingException e) {
			MainPlugin.getDefault().logError("�ļ���֧��" + SQL_FILE_ENCODING + "���롣", e);
			throw new SdpBuildRuntimeException("�ļ���֧��" + SQL_FILE_ENCODING + "���롣");
		} finally{
			IOUtils.closeQuietly(tableWriter);
			IOUtils.closeQuietly(indexAndReferWriter);
			IOUtils.closeQuietly(viewWriter);
		}
	}
	
	/**
	 * ��ȡPDM��Ӧ��sql�ļ�·����
	 * 
	 * @param pdmName
	 * @param sqlRoot
	 * @param dbType
	 * @param ddlType
	 * @return
	 */
	private File getSqlFile(String pdmName, File sqlRoot, DatabaseType dbType, int ddlType){
		StringBuilder sb = new StringBuilder();
		if(DatabaseType.SQLSERVER == dbType){
			sb.append("SQLSERVER");
		}else if(DatabaseType.ORACLE == dbType){
			sb.append("ORACLE");
		}else if(DatabaseType.DB2 == dbType){
			sb.append("DB2");
		}else{
			throw new IllegalArgumentException("Unsupported dbType: " + dbType);
		}
		
		sb.append(File.separator);
		switch (ddlType) {
			case DDL_TYPE_TABLE:
				sb.append("00001").append(File.separator).append("tb_").append(pdmName);
				break;
			case DDL_TYPE_INDEX_AND_REFERENCE:
				sb.append("00002").append(File.separator).append("fi_").append(pdmName);
				break;
			case DDL_TYPE_VIEW:
				sb.append("00003").append(File.separator).append("vtp_").append(pdmName);
				break;
			default:
				throw new IllegalArgumentException("Unsupported dbType: " + dbType);
		}
		sb.append(".sql");
		File sqlFile = new File(sqlRoot, sb.toString());
		return sqlFile;
	}
	
	/**
	 * ��SqlServerת����Oracle��DB2��Ӧ����Ϣ��
	 * 
	 * @param pdm
	 * @param dbType
	 * @return
	 */
	private Pdm getCvtedPdm(Pdm pdm, DatabaseType dbType){
		Pdm cvtedPdm = new Pdm();
		List<ITable> origTables = pdm.getTables();
		
		//Ϊ֧�ֶ����ֶεĴ洢��varchar���������ֶ���Ҫ�ж��Ƿ�ָ����Stereotype���ԣ������locale����Ҫ����1.5��
		List<ITable> cvtTables = new ArrayList<ITable>();
		for(ITable origTable : origTables){
			Table table = SqlConvertor.cloneTable(origTable);
			for(IColumn col : table.getAllColumns()){
				Column column = (Column) col;
				String columnType = column.getTypeName().toLowerCase();
				if(columnType.startsWith("varchar") && "locale".equals(column.getStereotype())){
					int newColumnLen = Math.round((float) (column.getLength()* 1.5));
					if(newColumnLen>4000){
						//�ֶγ��ȳ������ݿ��������󳤶�4000ʱ����4000����
						newColumnLen = 4000;
					}
					column.setLength(newColumnLen);
					column.setTypeName("varchar("+ newColumnLen + ")");
				}
			}
			cvtTables.add(table);
		}
		origTables = cvtTables;
		List<ViewInfo> origViews = pdm.getViews();
		if(DatabaseType.ORACLE == dbType){
			for(ITable origTable : origTables){
				cvtedPdm.getTables().add(SqlConvertor.cvtSqlServer2Oracle(origTable));
			}
			for(ViewInfo origView : origViews){
				cvtedPdm.getViews().add(SqlConvertor.cvtViewFromSqlServer2Oracle(origView));
			}
		}else if(DatabaseType.DB2 == dbType){
			for(ITable origTable : origTables){
				cvtedPdm.getTables().add(SqlConvertor.cvtSqlServer2Db2(origTable));
			}
			for(ViewInfo origView : origViews){
				cvtedPdm.getViews().add(SqlConvertor.cvtViewFromSqlServer2Db2(origView));
			}
		}else if(DatabaseType.SQLSERVER == dbType){
			for(ITable origTable : origTables){
				cvtedPdm.getTables().add(origTable);
			}
			for(ViewInfo origView : origViews){
				cvtedPdm.getViews().add(origView);
			}
		}
		cvtedPdm.setPdmName(pdm.getPdmName());
		cvtedPdm.getFkConstraints().addAll(pdm.getFkConstraints());
		cvtedPdm.getIndexs().addAll(pdm.getIndexs());
		return cvtedPdm;
	}
	
	private void checkPkConstaintNameLength(Pdm pdm){
		checkPkConstaintNameLength(pdm.getTables(), DatabaseType.ORACLE, pdm.getPdmName());
		checkPkConstaintNameLength(pdm.getTables(), DatabaseType.DB2, pdm.getPdmName());
		checkPkConstaintNameLength(pdm.getTables(), DatabaseType.SQLSERVER, pdm.getPdmName());
	}
	
	private void checkPkConstaintNameLength(List<ITable> tables, DatabaseType dbType, String pdmName){
		int length = MAX_PK_NAME_LENGTH_SQLSERVER;
		if(DatabaseType.ORACLE == dbType){
			length = MAX_PK_NAME_LENGTH_ORACLE;
		}else if(DatabaseType.DB2 == dbType){
			length = MAX_PK_NAME_LENGTH_DB2;
		}
		
		for(Iterator<ITable> iter = tables.iterator(); iter.hasNext(); ){
			ITable table = iter.next();
			if(table.getPkConstraint() != null){
				String pkConstaintName = table.getPkConstraint().getName();
				if(pkConstaintName != null && pkConstaintName.length() > length){
					String errorMsg = new StringBuilder("PDM(").append(pdmName).append(")�б�(")
						.append(table.getName()).append(")����Լ������������").append(length).append("��").toString();
					MainPlugin.getDefault().logError(errorMsg);
					OperateLogger.getInstance().addLog(LogLevel.ERROR, errorMsg);
					iter.remove();
				}
			}
		}
	}

}
