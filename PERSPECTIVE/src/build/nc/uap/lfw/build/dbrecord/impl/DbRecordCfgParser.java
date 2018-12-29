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
 * Ԥ�ýű����ӱ������ļ�{@link DbRecordConfig}�����������ɻ�ȡ��ر��Ԫ������Ϣ{@link ITable}��
 * 
 * @author PH
 */
class DbRecordCfgParser{
	/** XML�ַ����� */
	private static final String XML_ENCODING = "gb2312" ;
	/** Ԥ�ýű����������� */
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
	 * ��ȡԤ�ýű����ӱ���������
	 * 
	 * @return ���ӱ�������
	 * @throws SdpBuildRuntimeException ��
	 * 	1.�����ļ���������;2.���ݿ��ж�Ӧ����������;3.�����ֶβ�����;4.����ֶβ����ڡ�
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
		
		//���ȴӹ������ӱ�����Ŀ¼�����ӱ������ļ������Ҳ������ģ�����ӱ�����Ŀ¼���ҡ�
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
				MainPlugin.getDefault().logError("����" + subFile + "����", e);
				throw new SdpBuildRuntimeException("����" + subFile + "����");
			} finally{
				IOUtils.closeQuietly(subReader);
			}
			if(mainSubItems != null && mainSubItems.subDbRecordItems != null 
					&& !mainSubItems.subDbRecordItems.isEmpty()){
				//У���ӱ��Ƿ������������
				for(DbRecordItem dBRecordSubItem : mainSubItems.subDbRecordItems){
					if(dBRecordSubItem.fkColumn == null 
							|| (dBRecordSubItem.fkColumn = dBRecordSubItem.fkColumn.trim()).equals("")){
						throw new SdpBuildRuntimeException(
								new StringBuilder("��������").append(subFile)
								.append("������").append(dBRecordSubItem.tableName)
								.append("δ��������С�").toString());
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
	 * ����Ԫ������Ϣ��
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
			MainPlugin.getDefault().logError("��ȡ���ݿ�Ԫ���ݳ���",e);
			throw new SdpBuildRuntimeException("��ȡ���ݿ�Ԫ���ݳ���");
		}
		List<IDBRecordItem> dbRecordItems = new ArrayList<IDBRecordItem>();
		dbRecordItems.add(dbRecordItem);
		fillMetaDataRecursive(dbRecordItems, metaData, dbType, userName, true);

		return dbRecordItem;
	}
	
	/**
	 * �ݹ����Ԫ������Ϣ��<br>
	 * ��������ʱ�׳��쳣���ӱ�����ʱ����,��ʾ������Ϣ��
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
			//1.����
			List<String> fkColNames = null;
			if(StringUtils.isNotBlank(dbRecordItem.getFkColName())){
				fkColNames = Arrays.asList(dbRecordItem.getFkColName().trim().toUpperCase().split(",\\s"));
			}
			ITable table = SqlUtil.retrieveTable(dbRecordItem.getTableName(), fkColNames, metaData, dbType, userName);
			if(table == null){
				String errorMsg = "��" + dbRecordItem.getTableName() + "�����ڻ�δ�����С�";
				if(isMainItem){
					throw new SdpBuildRuntimeException(errorMsg);
				}else{
					MainPlugin.getDefault().logError(errorMsg);
					OperateLogger.getInstance().addLog(LogLevel.WARN, errorMsg);
					iter.remove();
					continue;
				}
			}else if(isMainItem && StringUtils.isNotBlank(dbRecordItem.getGrpField())){
				//�����ֶβ�����
				IColumn grpCol = table.getColumnByName(dbRecordItem.getGrpField());
				if(grpCol == null){
					throw new SdpBuildRuntimeException("��" + dbRecordItem.getTableName() + "�ķ����ֶ�" 
							+ dbRecordItem.getGrpField() + "������");
				}else{//case sensitive for grouping
					if(dbRecordItem instanceof DbRecordItem){
						((DbRecordItem)dbRecordItem).grpField = grpCol.getName();
					}
				}
			}
			dbRecordItem.fillDataMeta(table);
			
			//2.�ӱ�
			if(!dbRecordItem.getChilds().isEmpty()){
				fillMetaDataRecursive(dbRecordItem.getChilds(), metaData, dbType, userName, false);
			}
			if(isMainItem){
				OperateLogger.getInstance().addLog(LogLevel.INFO, new StringBuilder("��")
					.append(dbRecordItem.getTableName()).append("����Ϊ")
					.append(dbRecordItem.getChilds().isEmpty() ? "����ṹ��" : "���ӱ�ṹ��").toString());
			}
		}
	}
	
	/**
	 * �����ӱ������ļ���
	 * 
	 * @param path �ӱ������ļ���Ŀ¼
	 * @param fileName �ӱ��ļ���
	 * @return �ӱ������ļ�
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
	 * �ӱ������ļ���������
	 * 
	 * @author PH
	 */
	static class SubItemFileFilter implements FileFilter{
		/** �ӱ��ļ��� */
		private String fileName;
		
		SubItemFileFilter(String fileName){
			this.fileName = fileName;
		}

		public boolean accept(File file) {
			//case insensitive.
			return file.isFile() && fileName.equalsIgnoreCase(file.getName());
		}
	}
	
	//����Ϊ�����ļ��������ʵ�塣
	
	static interface IDBRecordItem extends IDbRecordScript{
		//String getTableName();
		
		String getFkColName();
		
		void fillDataMeta(ITable table);
		
		public List<? extends IDBRecordItem> getChilds();
	}
	
	/**
	 * ���ӱ����ã���ӦXML��ǩ��
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
	 * Ԥ�ýű�������������ṹ��
	 * 
	 * @author PH
	 */
	@XStreamAlias("subTable")
	static class DbRecordItem implements IDBRecordItem{
		/** ���� */
		String tableName;
		/** ����� */
		@XStreamAlias("foreignKeyColumn")
		String fkColumn;
		/** where���� */
		String whereCondition;
		/** sql�ļ���� */
		String sqlNo;
		/** unused */
		String substitutionGroup;
		/** �����ֶ� */
		String grpField;
		/** ������ */
		String tableDesc;
		/** �ӱ��� */
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
