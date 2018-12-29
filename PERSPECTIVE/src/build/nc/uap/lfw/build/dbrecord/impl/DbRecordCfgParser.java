package nc.uap.lfw.build.dbrecord.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dao.SqlUtil;
import nc.uap.lfw.build.dbrecord.itf.DbRecordConfig;
import nc.uap.lfw.build.dbrecord.itf.IDbRecordScript;
import nc.uap.lfw.build.dbrecord.itf.MainTableRecordItem;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;
import nc.uap.lfw.build.orm.mapping.IColumn;
import nc.uap.lfw.build.orm.mapping.ITable;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 预置脚本主子表配置文件{@link DbRecordConfig}解析器，并可获取相关表的元数据信息{@link ITable}。
 * 
 * @author PH
 */
class DbRecordCfgParser{
	/** XML字符编码 */
	private static final String XML_ENCODING = "gb2312" ;
	/** 预置脚本导出项配置 */
	private DbRecordConfig recordCfg;
	/** DB connection */
	private Connection conn;
	
	/**
	 * Constructor.
	 * 
	 * @param recordCfg DBRecordConfig
	 * @param conn Connection
	 */
	DbRecordCfgParser(DbRecordConfig recordCfg, Connection conn){
		this.recordCfg = recordCfg;
		this.conn = conn;
	}
	
	/**
	 * 获取预置脚本主子表导出条件。
	 * 
	 * @return 主子表导出条件
	 * @throws SdpBuildRuntimeException 当
	 * 	1.配置文件解析有误;2.数据库中对应的主表不存在;3.分组字段不存在;4.外键字段不存在。
	 */
	IDbRecordScript getDBRecordScript() throws SdpBuildRuntimeException {
		MainTableRecordItem mainTableItem = recordCfg.getMainTableItem();
		DbRecordItem mainTableRecordItem = new DbRecordItem();
		//item.itemKey = mainTableItem.getItemKey();
		//item.tableDesc = mainTableItem.getTableDesc();
		mainTableRecordItem.tableName = mainTableItem.getTableName();
		mainTableRecordItem.grpField = mainTableItem.getGrpField();
		mainTableRecordItem.whereCondition = mainTableItem.getWhereCondition();
		mainTableRecordItem.tableDesc = mainTableItem.getTableDesc();
		mainTableRecordItem.sqlNo = "001";
		
		//首先从公共主子表配置目录下找子表配置文件，若找不到则从模块主子表配置目录下找。
		File subFile = findSubCfgFile(recordCfg.getCommonMultiTableFileName() ,mainTableRecordItem.tableName + ".xml");
		if(subFile == null){
			subFile = findSubCfgFile(recordCfg.getModuleMultiTableFileName() ,mainTableRecordItem.tableName + ".xml");
		}
		if(subFile != null){
			XStream xStream = new XStream();
			List<Class<?>> classList = new ArrayList<Class<?>>();
			classList.add(DbRecordMainSubItems.class);
			classList.add(DbRecordItem.class);
			Annotations.configureAliases(xStream, classList.toArray(new Class[0]));
			Reader subReader = null;
			DbRecordMainSubItems mainSubItems = null;
			try {
				subReader = new InputStreamReader(new FileInputStream(subFile), XML_ENCODING);
				mainSubItems = (DbRecordMainSubItems)xStream.fromXML(subReader);
			}catch (Exception e) {
				MainPlugin.getDefault().logError("解析" + subFile + "出错。", e);
				throw new SdpBuildRuntimeException("解析" + subFile + "出错。");
			} finally{
				IOUtils.closeQuietly(subReader);
			}
			if(mainSubItems != null && mainSubItems.subDbRecordItems != null 
					&& !mainSubItems.subDbRecordItems.isEmpty()){
				//校验子表是否配置了外键列
				for(DbRecordItem dBRecordSubItem : mainSubItems.subDbRecordItems){
					if(dBRecordSubItem.fkColumn == null 
							|| (dBRecordSubItem.fkColumn = dBRecordSubItem.fkColumn.trim()).equals("")){
						throw new SdpBuildRuntimeException(
								new StringBuilder("解析配置").append(subFile)
								.append("出错。表").append(dBRecordSubItem.tableName)
								.append("未配置外键列。").toString());
					}
				}
				mainTableRecordItem.sqlNo = mainSubItems.sqlNo;
				mainTableRecordItem.subDbRecordItems = mainSubItems.subDbRecordItems;
			}
		}
		fillMetaData(mainTableRecordItem);
		return mainTableRecordItem;
	}
	
	/**
	 * 填充表元数据信息。
	 * 
	 * @param dbRecordItem
	 * @return
	 */
	private IDBRecordItem fillMetaData(IDBRecordItem dbRecordItem){
		DatabaseMetaData metaData = null;
		String dbType = null, userName = null;
		try {
			metaData = conn.getMetaData();
			dbType = metaData.getDatabaseProductName();
			userName = metaData.getUserName();
		}catch (SQLException e) {
			MainPlugin.getDefault().logError("获取数据库元数据出错。",e);
			throw new SdpBuildRuntimeException("获取数据库元数据出错。");
		}
		List<IDBRecordItem> dbRecordItems = new ArrayList<IDBRecordItem>();
		dbRecordItems.add(dbRecordItem);
		fillMetaDataRecursive(dbRecordItems, metaData, dbType, userName, true);

		return dbRecordItem;
	}
	
	/**
	 * 递归填充元数据信息。<br>
	 * 主表不存在时抛出异常；子表不存在时忽略,提示警告信息。
	 * 
	 * @param dbRecordItems
	 * @param metaData
	 * @param dbType
	 * @param userName
	 * @param isMainItem
	 */
	private void fillMetaDataRecursive(List<? extends IDBRecordItem> dbRecordItems, DatabaseMetaData metaData, 
			String dbType, String userName, boolean isMainItem){
		for(Iterator<? extends IDBRecordItem> iter = dbRecordItems.iterator(); iter.hasNext();){
			IDBRecordItem dbRecordItem = iter.next();
			//1.主表
			List<String> fkColNames = null;
			if(StringUtils.isNotBlank(dbRecordItem.getFkColName())){
				fkColNames = Arrays.asList(dbRecordItem.getFkColName().trim().toUpperCase().split(",\\s"));
			}
			ITable table = SqlUtil.retrieveTable(dbRecordItem.getTableName(), fkColNames, metaData, dbType, userName);
			if(table == null){
				String errorMsg = "表" + dbRecordItem.getTableName() + "不存在或未定义列。";
				if(isMainItem){
					throw new SdpBuildRuntimeException(errorMsg);
				}else{
					MainPlugin.getDefault().logError(errorMsg);
					OperateLogger.getInstance().addLog(LogLevel.WARN, errorMsg);
					iter.remove();
					continue;
				}
			}else if(isMainItem && StringUtils.isNotBlank(dbRecordItem.getGrpField())){
				//分组字段不存在
				IColumn grpCol = table.getColumnByName(dbRecordItem.getGrpField());
				if(grpCol == null){
					throw new SdpBuildRuntimeException("表" + dbRecordItem.getTableName() + "的分组字段" 
							+ dbRecordItem.getGrpField() + "不存在");
				}else{//case sensitive for grouping
					if(dbRecordItem instanceof DbRecordItem){
						((DbRecordItem)dbRecordItem).grpField = grpCol.getName();
					}
				}
			}
			dbRecordItem.fillDataMeta(table);
			
			//2.子表
			if(!dbRecordItem.getChilds().isEmpty()){
				fillMetaDataRecursive(dbRecordItem.getChilds(), metaData, dbType, userName, false);
			}
			if(isMainItem){
				OperateLogger.getInstance().addLog(LogLevel.INFO, new StringBuilder("表")
					.append(dbRecordItem.getTableName()).append("解析为")
					.append(dbRecordItem.getChilds().isEmpty() ? "单表结构。" : "主子表结构。").toString());
			}
		}
	}
	
	/**
	 * 查找子表配置文件。
	 * 
	 * @param path 子表配置文件父目录
	 * @param fileName 子表文件名
	 * @return 子表配置文件
	 */
	private File findSubCfgFile(String path, String fileName){
		File commonMultiTableFile = new File(path);
		if(commonMultiTableFile.exists() && commonMultiTableFile.isDirectory()){
			File[] files = commonMultiTableFile.listFiles(new SubItemFileFilter(fileName));
			if(files != null && files.length >= 1){
				return files[0];
			}
		}
		return null;
	}
	
	/**
	 * 子表配置文件过滤器。
	 * 
	 * @author PH
	 */
	static class SubItemFileFilter implements FileFilter{
		/** 子表文件名 */
		private String fileName;
		
		SubItemFileFilter(String fileName){
			this.fileName = fileName;
		}

		public boolean accept(File file) {
			//case insensitive.
			return file.isFile() && fileName.equalsIgnoreCase(file.getName());
		}
	}
	
	//以下为配置文件解析相关实体。
	
	static interface IDBRecordItem extends IDbRecordScript{
		//String getTableName();
		
		String getFkColName();
		
		void fillDataMeta(ITable table);
		
		public List<? extends IDBRecordItem> getChilds();
	}
	
	/**
	 * 主子表配置，对应XML标签。
	 */
	@XStreamAlias("hierarchy")
	static class DbRecordMainSubItems{
		
		String tableName;
		
		String sqlNo;
		
		String substitutionGroup;
		
		@XStreamAlias("subTableGroup")
		List<DbRecordItem> subDbRecordItems;
		
	}
	
	/**
	 * 预置脚本导出配置项：树结构。
	 * 
	 * @author PH
	 */
	@XStreamAlias("subTable")
	static class DbRecordItem implements IDBRecordItem{
		/** 表名 */
		String tableName;
		/** 外键名 */
		@XStreamAlias("foreignKeyColumn")
		String fkColumn;
		/** where条件 */
		String whereCondition;
		/** sql文件序号 */
		String sqlNo;
		/** unused */
		String substitutionGroup;
		/** 分组字段 */
		String grpField;
		/** 表描述 */
		String tableDesc;
		/** 子表项 */
		@XStreamAlias("subTableGroup")
		List<DbRecordItem> subDbRecordItems;
		
		@XStreamOmitField
		private ITable table;

		public ITable getTable() {
			return table;
		}

		@SuppressWarnings("unchecked")
		public List<? extends IDBRecordItem> getChilds() {
			return subDbRecordItems == null ? Collections.EMPTY_LIST : subDbRecordItems;
		}

		public String getWhereCondition() {
			return whereCondition;
		}

		public void fillDataMeta(ITable table) {
			this.table = table;
		}

		public String getFkColName() {
			return fkColumn;
		}

		public String getTableName() {
			return tableName;
		}

		public String getGrpField() {
			return grpField;
		}

		public String getSqlNo() {
			return sqlNo;
		}
		
		public String getTableDesc() {
			return tableDesc;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("talbeName:").append(tableName).append(";").append("sqlNo:").append(sqlNo).append(";")
			.append("fkColumn:").append(fkColumn).append("; Table MetaData:").append(IOUtils.LINE_SEPARATOR)
			.append(table).append(IOUtils.LINE_SEPARATOR);
			if(subDbRecordItems != null && !subDbRecordItems.isEmpty()){
				for(DbRecordItem temp : subDbRecordItems){
					sb.append("		").append(temp.toString());
				}
			}
			return sb.toString();
		}
	}

}
