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
 * 建库脚本业务实现。
 * 
 * @author PH
 */
public class DbCreateServiceImpl implements IDbCreateService {
	/** 表 */
	private static final int DDL_TYPE_TABLE = 1;
	/** 索引、外键 */
	private static final int DDL_TYPE_INDEX_AND_REFERENCE = 2;
	/** 视图 */
	private static final int DDL_TYPE_VIEW = 3;
	/** sql文件编码 */
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
				throw new SdpBuildRuntimeException("主键约束名长度超限。");
			}
			OperateLogger.getInstance().addLog(LogLevel.INFO, "PDM(" + pdmFile.getName() + ")校验成功。");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "PDM(" + pdmFile.getName() + ")校验失败，" + e.getMessage());
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
	 * 据PDM生成指定数据库的建库脚本。
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
			
			//校验主键约束名长度
			for(DatabaseType dbType : dbTypes){
				checkPkConstaintNameLength(pdm.getTables(), dbType, pdm.getPdmName());
			}
			
			for(DatabaseType dbType : dbTypes){
				geneSqlFileInner(pdm, sqlRoot, dbType);
			}
			
			OperateLogger.getInstance().addLog(LogLevel.INFO, "PDM(" + pdmFile.getName() + ")生成成功。");
		} catch (SdpBuildRuntimeException e) {
			OperateLogger.getInstance().addLog(LogLevel.ERROR, "PDM(" + pdmFile.getName() + ")生成失败，" + e.getMessage());
			throw e;
		}
	}

	/**
	 * 据元数据信息生成SQL文件。
	 * 
	 * @param pdm Pdm
	 * @param sqlRoot 生成目录
	 * @param dbType 数据库类型
	 */
	private void geneSqlFileInner(Pdm pdm, File sqlRoot, DatabaseType dbType){
		//1. convertion
		pdm = getCvtedPdm(pdm, dbType);
		
		File tableSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_TABLE), 
			 indexAndRefSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_INDEX_AND_REFERENCE),
			 viewSqlFile = getSqlFile(pdm.getPdmName(), sqlRoot, dbType, DDL_TYPE_VIEW);
		
		//2.1 创建目录或删除文件。
		if(pdm.getTables().isEmpty()){
			tableSqlFile.delete();
		}else{
			if(!tableSqlFile.getParentFile().exists() && !tableSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("创建目录" + tableSqlFile.getParentFile().getAbsolutePath() + "失败。");
			}
		}
		
		if(pdm.getIndexs().isEmpty() && pdm.getFkConstraints().isEmpty()){
			indexAndRefSqlFile.delete();
		}else{
			if(!indexAndRefSqlFile.getParentFile().exists() && !indexAndRefSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("创建目录" + indexAndRefSqlFile.getParentFile().getAbsolutePath() + "失败。");
			}
		}
		
		if(pdm.getViews().isEmpty()){
			viewSqlFile.delete();
		}else{
			if(!viewSqlFile.getParentFile().exists() && !viewSqlFile.getParentFile().mkdirs()){
				throw new SdpBuildRuntimeException("创建目录" + viewSqlFile.getParentFile().getAbsolutePath() + "失败。");
			}
		}
		
		//2.2  生成Sql文件。
		IDdlGenerator generator = DdlGeneratorFactory.getInstance(dbType);
		Writer tableWriter = null, indexAndReferWriter = null, viewWriter = null;
		try {
			if(!pdm.getTables().isEmpty()){
				tableWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(tableSqlFile), SQL_FILE_ENCODING));
				generator.geneCreateTableDdl(pdm.getTables(), tableWriter);
			}
			
			if(indexAndRefSqlFile.exists() && !indexAndRefSqlFile.delete()){
				throw new SdpBuildRuntimeException("删除文件" + indexAndRefSqlFile.getAbsolutePath() + "失败。");
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
			MainPlugin.getDefault().logError("文件不存在。", e);
			throw new SdpBuildRuntimeException("文件不存在。");
		} catch (UnsupportedEncodingException e) {
			MainPlugin.getDefault().logError("文件不支持" + SQL_FILE_ENCODING + "编码。", e);
			throw new SdpBuildRuntimeException("文件不支持" + SQL_FILE_ENCODING + "编码。");
		} finally{
			IOUtils.closeQuietly(tableWriter);
			IOUtils.closeQuietly(indexAndReferWriter);
			IOUtils.closeQuietly(viewWriter);
		}
	}
	
	/**
	 * 获取PDM对应的sql文件路径。
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
	 * 据SqlServer转换成Oracle、DB2对应的信息。
	 * 
	 * @param pdm
	 * @param dbType
	 * @return
	 */
	private Pdm getCvtedPdm(Pdm pdm, DatabaseType dbType){
		Pdm cvtedPdm = new Pdm();
		List<ITable> origTables = pdm.getTables();
		
		//为支持多语字段的存储，varchar数据类型字段需要判断是否指定了Stereotype属性，如果是locale，需要乘以1.5倍
		List<ITable> cvtTables = new ArrayList<ITable>();
		for(ITable origTable : origTables){
			Table table = SqlConvertor.cloneTable(origTable);
			for(IColumn col : table.getAllColumns()){
				Column column = (Column) col;
				String columnType = column.getTypeName().toLowerCase();
				if(columnType.startsWith("varchar") && "locale".equals(column.getStereotype())){
					int newColumnLen = Math.round((float) (column.getLength()* 1.5));
					if(newColumnLen>4000){
						//字段长度超过数据库允许的最大长度4000时，按4000处理
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
					String errorMsg = new StringBuilder("PDM(").append(pdmName).append(")中表(")
						.append(table.getName()).append(")主键约束名超出长度").append(length).append("。").toString();
					MainPlugin.getDefault().logError(errorMsg);
					OperateLogger.getInstance().addLog(LogLevel.ERROR, errorMsg);
					iter.remove();
				}
			}
		}
	}

}
