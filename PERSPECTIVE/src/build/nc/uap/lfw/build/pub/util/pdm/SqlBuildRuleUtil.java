package nc.uap.lfw.build.pub.util.pdm;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SDPBuildException;
import nc.uap.lfw.build.exception.SDPSystemMessage;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.UnitDataSource;
import nc.uap.lfw.build.pub.util.pdm.itf.IExport;
import nc.uap.lfw.build.pub.util.pdm.itf.ISql;
import nc.uap.lfw.build.pub.util.pdm.vo.Item;
import nc.uap.lfw.build.pub.util.pdm.vo.MainTable;
import nc.uap.lfw.build.pub.util.pdm.vo.Table;
import nc.uap.lfw.build.pub.util.pdm.vo.TableField;

/**
 * SQLBuild�����ű�ʱ�Ĺ��߷�������SqlBuildRule�е����÷����ó����ع����
 * 
 * @author mazqa
 *
 */
public class SqlBuildRuleUtil {
	
	/**
	 * ���ݿ�����Ȩ�س���
	 */
	private static final double _TABLE_ = Math.pow(2.0, 0.0);
	private static final double _PK_ = Math.pow(2.0, 1.0);
	private static final double _INDEX_ = Math.pow(2.0, 2.0);
	private static final double _FK_ = Math.pow(2.0, 3.0);
	private static final double _VIEW_ = Math.pow(2.0, 4.0);
	
	/**
	 * �÷������ؽű�����˳��
	 */
	public static double[] getBuildOrder() {
		double[] build_page = new double[3];
		build_page[0] = 1.0 * _TABLE_ + 1.0 * _PK_ + 0.0 * _INDEX_ + 0.0 * _FK_ + 0.0 * _VIEW_;
		build_page[1] = 0.0 * _TABLE_ + 0.0 * _PK_ + 1.0 * _INDEX_ + 1.0 * _FK_ + 0.0 * _VIEW_;
		build_page[2] = 0.0 * _TABLE_ + 0.0 * _PK_ + 0.0 * _INDEX_ + 0.0 * _FK_ + 1.0 * _VIEW_;
		return build_page;
	}

	/**
	 * �÷������ؽű��Ƿ������л�����
	 */
	public static int isScriptSerialized() {
		return ISql.SCRIPT_DESERIALIZED;
	}

	/**
	 * �÷������ؽű��Ĵ�СдԼ�� ע: '0':����ԭ��, '1':Сд, '2':��д; Ĭ��Ϊ'1'(Сд)
	 */
	public static int isScriptSensitive() {
		return ISql.SCRIPT_LOWERCASE;
	}

	/**
	 * �÷������ؽű�����������Ƿ����������Ϣ
	 */
	public static boolean isDebug() {
		return true;
	}

	/**
	 * �÷�������ָ����sqlҳ�±귵����Ӧ�Ľű�Ŀ¼
	 * 
	 * ��Ӧ����Ϊ: '0' - '00001' '1' - '00002' '2' - '00003'
	 */
	private static String getSqlPageDir(int sql_page_index) {
		switch (sql_page_index) {
		case 0:
			return "00001";
		case 1:
			return "00002";
		case 2:
			return "00003";
		default:
			return null;
		}
	}

	/**
	 * �÷�������ָ����sqlҳ�±귵����Ӧ�Ľű��ļ�����ǰ׺
	 * 
	 * ��Ӧ����Ϊ: '0' - 'tb_' '1' - 'fi_' '2' - 'vtp_'
	 */
	private static String getSqlFilePrefix(int sql_page_index) {
		switch (sql_page_index) {
		case 0:
			return "tb_";
		case 1:
			return "fi_";
		case 2:
			return "vtp_";
		default:
			return null;
		}
	}
	
	/**
	 * �÷�������ָ��SQL�ű���Ŀ¼��PDM���Ʒ��ؽ���ű���Դ洢·��(��:
	 * script\dbcreate\SQLSERVER\00001\tb_nc_fi_gl.sql)
	 * 
	 * @param sql_root
	 * @param pdm_name
	 * @param db_type
	 * @param sql_page_index
	 */
	public static File getSQLFileByPDM(String sql_root, String pdm_name, String db_type, int sql_page_index) {
		File sqlFile = null;
		String dbType = null;
		String sqlFileName = null;

		/* ������ݿ�����Ŀ¼����(��: SQLSERVER, ORACLE, DB2) */
		if (db_type.equalsIgnoreCase(ISql._SQLSERVER_))
			dbType = "SQLSERVER";
		else if (db_type.equalsIgnoreCase(ISql._ORACLE_))
			dbType = "ORACLE";
		else if (db_type.equalsIgnoreCase(ISql._DB2_))
			dbType = "DB2";
		else {
			MainPlugin.getDefault().logError("ָ�������ݿ�����Ŀǰ��֧�֣�" + db_type);
			return null;
		}

		/* ���sqlĿ¼(��: 00001, 00002, 00003) */
		String sqlDir = getSqlPageDir(sql_page_index);

		/* ���sql�ű��ļ�����(��: tb_nc_fi_gl.sql, fi_nc_fi_gl.sql, vtp_nc_fi_gl.sql) */
		sqlFileName = getSqlFilePrefix(sql_page_index) + pdm_name.substring(0, pdm_name.lastIndexOf(".")).toLowerCase()
				+ ".sql";

		/* ���sql�ű��ļ������·�� */
		sqlFile = new File(sql_root + File.separator + "script" + File.separator + "dbcreate" + File.separator + dbType
				+ File.separator + sqlDir + File.separator + sqlFileName);
		return sqlFile;
	}


	/**
	 * �÷�������ָ�����Ԫ���ݽṹ
	 * 
	 * @param dbType ���ݿ����ͣ�sql server/db2/oracle #IExport.SQL_EXPORT_DB_TYPE_SQLSERVER
	 * @param schema
	 * @param tableName
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public static Table getTable(String dbType, String schema, String tableName, SDPConnection con) throws SQLException, SDPBuildException {
		/* �������Ԫ���� */
		DatabaseMetaData dbMetaData = con.getPhysicalConnection().getMetaData();
		Table table = new Table(tableName);
		List<String> pkList = new ArrayList<String>();

		/* 1. ����PK�� */
		ResultSet pkRs = null;
		try {
			pkRs = dbMetaData.getPrimaryKeys(getCatalog(dbType), getSchema(dbType, schema), getTableName(dbType, tableName));
			while (pkRs.next()) {
				pkList.add(pkRs.getString("COLUMN_NAME"));
				MainPlugin.getDefault().logInfo("PK��: " + pkRs.getString("COLUMN_NAME"));
			}// while
		} finally {
			if (pkRs != null) {
				try {
					pkRs.close();
				} catch (SQLException ignore) {
					MainPlugin.getDefault().logError(ignore.getMessage(), ignore);
				}
			}
		}

		if (pkList.size() > 0) {
			MainPlugin.getDefault().logInfo("PK������: " + pkList.size());
			table.setTablePrimaryKey(pkList.toArray(new String[0]));
		} else {
			/* ��ǰ��ṹ��δ����PK�� */
			MainPlugin.getDefault().logInfo("��[" + tableName + "]δ����PK��");
		}

		/* 2. ������ͨ�ֶ� */
		List<TableField> columnFieldList = new ArrayList<TableField>();
		ResultSet rs = null;
		try {
			rs = dbMetaData.getColumns(getCatalog(dbType), getSchema(dbType, schema), getTableName(dbType, tableName), "%");
			while (rs.next()) {
				// Column Name
				String columnName = rs.getString("COLUMN_NAME");
				String typeName = rs.getString("TYPE_NAME");
				// Column Size
				int columnSize = rs.getInt("COLUMN_SIZE");
				// Column Comment
				String remarks = rs.getString("REMARKS");
				// Column Type
				short dataType = rs.getShort("DATA_TYPE");
				// Column Default Value
				String columnDef = null;
				byte[] columnDefValue = rs.getBytes("COLUMN_DEF");
				if (columnDefValue != null) {
					//�ֶ�Ĭ��ֵ
					columnDef = new String(columnDefValue);
					MainPlugin.getDefault().logInfo("�ֶ�Ĭ��ֵ: " + columnDef);
				}
				// Column Position
				int ordinalPosition = rs.getInt("ORDINAL_POSITION");
				MainPlugin.getDefault().logInfo("�ֶ�λ��: " + ordinalPosition);
				// Column Nullable
				String isNullable = rs.getString("IS_NULLABLE");
				MainPlugin.getDefault().logInfo("�ֶ��Ƿ�ɿ�: " + isNullable);
				// ����TableField
				TableField field = new TableField();
				field.setFieldName(columnName);
				field.setMaxLength(columnSize);
				field.setDataType(dataType);
				field.setDataTypeName(typeName);
				field.setNote(remarks);
				field.setDefaultValue(columnDef);
				field.setIndex(ordinalPosition);
				field.setNullAllowed(isNullable.equals("YES"));
				columnFieldList.add(field);
			}// while

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					MainPlugin.getDefault().logError(e.getMessage(), e);
				}
			}
		}
		if (columnFieldList.size() > 0) {
			MainPlugin.getDefault().logInfo("������: " + columnFieldList.size());
			table.setTableFields(columnFieldList.toArray(new TableField[0]));
		} else {
			MainPlugin.getDefault().logError("��" + tableName + "δ�����л򲻴��ڡ�");
			//throw new SDPBuildException("��" + tableName + "δ�����л򲻴��ڡ�");
		}
		return table;
	}

	/**
	 * �÷�������ת��������ݿ�Ŀ¼(Database Catalog)
	 * 
	 * @param dbType
	 * @return
	 */
	private static String getCatalog(String dbType) {
		String catalog = null;
//		if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_SQLSERVER) || dbType.equals(IExport.SQL_EXPORT_DB_TYPE_DB2))
//			catalog = null;
		if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_ORACLE))
			catalog = "";
		return catalog;
	}

	/**
	 * �÷�������ת��������ݿⷽ��(Database Schema)
	 * 
	 * @param dbType
	 * @param schema
	 * @return
	 */
	private static String getSchema(String dbType, String schema) {
		if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_SQLSERVER))
			return null;
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_DB2))
			return schema;
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_ORACLE))
			return schema.toUpperCase();
		else
			return null;
	}

	/**
	 * �÷���ִ�б����Ĵ�Сдת�� ע: ͨ��JDBCȡԪ����ʱ��Ҫ���ղ�ͬ���ݿ�����ִ�б����Ĵ�Сдת��
	 * 
	 * @param dbType
	 * @param tableName
	 */
	private static String getTableName(String dbType, String tableName) {
		if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_SQLSERVER))
			return tableName; // SQLSERVER������Сд����
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_DB2))
			return tableName.toUpperCase(); // ORACLE�轫������д
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_ORACLE))
			return tableName.toUpperCase(); // DB2�轫������д
		else
			return null;
	}
	
	public static double getTablePower() {
		return _TABLE_;
	}

	public static double getPKPower() {
		return _PK_;
	}

	public static double getFKPower() {
		return _FK_;
	}

	public static double getIndexPower() {
		return _INDEX_;
	}

	public static double getViewPower() {
		return _VIEW_;
	}
	
	
	/**
	 * �÷�������Ԥ�ýű����ز��ṹ����������ӱ��򷵻�MainTable��������ֱ�ӷ���null
	 * 
	 * @param item_vo
	 * @return
	 */
	public static MainTable getMultiTableHierarchyStructure(Item item_vo, String multiTableRuleMapPath) {
		String hierarchyRuleFileName = item_vo.getItemRule() + ".xml";
		String hierarchyRuleFilePath = multiTableRuleMapPath + File.separator + hierarchyRuleFileName;
		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
			/* ����ṹ */
			return null;
		} else {
			/* ��-�ӱ�ṹ */
			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, item_vo);
		}
	}

	/**
	 * �÷�������Ԥ�ýű����ز��ṹ, ͬʱ�����������Ԫ������Ϣ�� ��鵱ǰitemVO�Ƿ�Ϊ���ӱ�����ǵĻ����ر�ۺ�VO��������Ƿ��ص���VO
	 * 
	 * @param itemVo ��ʵ��ֻ�õ���itemRule��fixWhere��������
	 * @param con
	 * @return
	 */
//	public static MainTable getMultiTableHierarchyStructure(Item itemVo, SDPConnection con, String multiTableRuleMapPath)
//			throws SDPBuildException {
//		UnitDataSource uds = con.getDataSource();
//		String hierarchyRuleFileName = itemVo.getItemRule() + ".xml";
//		String hierarchyRuleFilePath = multiTableRuleMapPath + File.separator + hierarchyRuleFileName;
//		// �������ӱ��ļ��������������Ϊ����ṹ��������Ϊ���ӱ�ṹ
//		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
//			/* ����ṹ */
//			MainTable mainTable = new MainTable(itemVo);
//			// sqlNo
//			mainTable.setSqlNo("001");
//			// isAssociatedTables
//			mainTable.setAssociated(false);
//			// subTableSet
//			mainTable.setSubTableSet(null);
//			// metaData
//			try {
//				Table tableVO = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(), itemVo.getItemRule(),
//						con);
//				mainTable.setTableMetaData(tableVO);
//			} catch (SQLException sdp_exception) {
//				MainPlugin.getDefault().logError(sdp_exception.getMessage(), sdp_exception);
//				throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4014), sdp_exception);
//			}
//			return mainTable;
//		} else {
//			/* ��-�ӱ�ṹ */
//			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, itemVo, con);
//		}
//	}
	
	/**
	 * �÷�������Ԥ�ýű����ز��ṹ, ͬʱ�����������Ԫ������Ϣ�� ��鵱ǰitemVO�Ƿ�Ϊ���ӱ�����ǵĻ����ر�ۺ�VO��������Ƿ��ص���VO
	 * 
	 * @param itemVo
	 * @param con
	 * @param commonMultiTableRuleMapPath
	 * @param moduleMultiTablePath
	 * @return
	 * @throws SDPBuildException
	 */
	public static MainTable getMultiTableHierarchyStructure(Item itemVo, SDPConnection con, 
			String commonMultiTableRuleMapPath, String moduleMultiTablePath)
			throws SDPBuildException {
		UnitDataSource uds = con.getDataSource();
		String hierarchyRuleFileName = itemVo.getItemRule() + ".xml";
		String hierarchyRuleFilePath = commonMultiTableRuleMapPath + File.separator + hierarchyRuleFileName;
		if(!FileUtil.checkExist(hierarchyRuleFilePath)){
			hierarchyRuleFilePath = moduleMultiTablePath + File.separator + hierarchyRuleFileName;
		}
		// �������ӱ��ļ��������������Ϊ����ṹ��������Ϊ���ӱ�ṹ
		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
			/* ����ṹ */
			MainTable mainTable = new MainTable(itemVo);
			// sqlNo
			mainTable.setSqlNo("001");
			// isAssociatedTables
			mainTable.setAssociated(false);
			// subTableSet
			mainTable.setSubTableSet(null);
			// metaData
			try {
				Table tableVO = SqlBuildRuleUtil.getTable(uds.getDatabaseType(), uds.getUser(), itemVo.getItemRule(),
						con);
				mainTable.setTableMetaData(tableVO);
			} catch (SQLException sdp_exception) {
				MainPlugin.getDefault().logError(sdp_exception.getMessage(), sdp_exception);
				throw new SDPBuildException(SDPSystemMessage.getMsg(SDPSystemMessage.SDP_BUILD_4014), sdp_exception);
			}
			return mainTable;
		} else {
			/* ��-�ӱ�ṹ */
			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, itemVo, con);
		}
		}
	
	
}
