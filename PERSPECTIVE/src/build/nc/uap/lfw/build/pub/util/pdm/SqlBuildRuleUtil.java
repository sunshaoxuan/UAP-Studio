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
 * SQLBuild建立脚本时的工具方法，将SqlBuildRule中的无用方法拿出来重构获得
 * 
 * @author mazqa
 *
 */
public class SqlBuildRuleUtil {
	
	/**
	 * 数据库对象的权重常量
	 */
	private static final double _TABLE_ = Math.pow(2.0, 0.0);
	private static final double _PK_ = Math.pow(2.0, 1.0);
	private static final double _INDEX_ = Math.pow(2.0, 2.0);
	private static final double _FK_ = Math.pow(2.0, 3.0);
	private static final double _VIEW_ = Math.pow(2.0, 4.0);
	
	/**
	 * 该方法返回脚本构造顺序
	 */
	public static double[] getBuildOrder() {
		double[] build_page = new double[3];
		build_page[0] = 1.0 * _TABLE_ + 1.0 * _PK_ + 0.0 * _INDEX_ + 0.0 * _FK_ + 0.0 * _VIEW_;
		build_page[1] = 0.0 * _TABLE_ + 0.0 * _PK_ + 1.0 * _INDEX_ + 1.0 * _FK_ + 0.0 * _VIEW_;
		build_page[2] = 0.0 * _TABLE_ + 0.0 * _PK_ + 0.0 * _INDEX_ + 0.0 * _FK_ + 1.0 * _VIEW_;
		return build_page;
	}

	/**
	 * 该方法返回脚本是否需序列化处理
	 */
	public static int isScriptSerialized() {
		return ISql.SCRIPT_DESERIALIZED;
	}

	/**
	 * 该方法返回脚本的大小写约定 注: '0':保持原样, '1':小写, '2':大写; 默认为'1'(小写)
	 */
	public static int isScriptSensitive() {
		return ISql.SCRIPT_LOWERCASE;
	}

	/**
	 * 该方法返回脚本构造过程中是否输出调试信息
	 */
	public static boolean isDebug() {
		return true;
	}

	/**
	 * 该方法根据指定的sql页下标返回相应的脚本目录
	 * 
	 * 对应规则为: '0' - '00001' '1' - '00002' '2' - '00003'
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
	 * 该方法根据指定的sql页下标返回相应的脚本文件名称前缀
	 * 
	 * 对应规则为: '0' - 'tb_' '1' - 'fi_' '2' - 'vtp_'
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
	 * 该方法根据指定SQL脚本根目录及PDM名称返回建库脚本相对存储路径(如:
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

		/* 获得数据库类型目录名称(如: SQLSERVER, ORACLE, DB2) */
		if (db_type.equalsIgnoreCase(ISql._SQLSERVER_))
			dbType = "SQLSERVER";
		else if (db_type.equalsIgnoreCase(ISql._ORACLE_))
			dbType = "ORACLE";
		else if (db_type.equalsIgnoreCase(ISql._DB2_))
			dbType = "DB2";
		else {
			MainPlugin.getDefault().logError("指定的数据库类型目前不支持：" + db_type);
			return null;
		}

		/* 获得sql目录(如: 00001, 00002, 00003) */
		String sqlDir = getSqlPageDir(sql_page_index);

		/* 获得sql脚本文件名称(如: tb_nc_fi_gl.sql, fi_nc_fi_gl.sql, vtp_nc_fi_gl.sql) */
		sqlFileName = getSqlFilePrefix(sql_page_index) + pdm_name.substring(0, pdm_name.lastIndexOf(".")).toLowerCase()
				+ ".sql";

		/* 获得sql脚本文件的相对路径 */
		sqlFile = new File(sql_root + File.separator + "script" + File.separator + "dbcreate" + File.separator + dbType
				+ File.separator + sqlDir + File.separator + sqlFileName);
		return sqlFile;
	}


	/**
	 * 该方法解析指定表的元数据结构
	 * 
	 * @param dbType 数据库类型，sql server/db2/oracle #IExport.SQL_EXPORT_DB_TYPE_SQLSERVER
	 * @param schema
	 * @param tableName
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public static Table getTable(String dbType, String schema, String tableName, SDPConnection con) throws SQLException, SDPBuildException {
		/* 解析表的元数据 */
		DatabaseMetaData dbMetaData = con.getPhysicalConnection().getMetaData();
		Table table = new Table(tableName);
		List<String> pkList = new ArrayList<String>();

		/* 1. 解析PK列 */
		ResultSet pkRs = null;
		try {
			pkRs = dbMetaData.getPrimaryKeys(getCatalog(dbType), getSchema(dbType, schema), getTableName(dbType, tableName));
			while (pkRs.next()) {
				pkList.add(pkRs.getString("COLUMN_NAME"));
				MainPlugin.getDefault().logInfo("PK列: " + pkRs.getString("COLUMN_NAME"));
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
			MainPlugin.getDefault().logInfo("PK列数量: " + pkList.size());
			table.setTablePrimaryKey(pkList.toArray(new String[0]));
		} else {
			/* 当前表结构中未定义PK列 */
			MainPlugin.getDefault().logInfo("表[" + tableName + "]未定义PK列");
		}

		/* 2. 解析普通字段 */
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
					//字段默认值
					columnDef = new String(columnDefValue);
					MainPlugin.getDefault().logInfo("字段默认值: " + columnDef);
				}
				// Column Position
				int ordinalPosition = rs.getInt("ORDINAL_POSITION");
				MainPlugin.getDefault().logInfo("字段位置: " + ordinalPosition);
				// Column Nullable
				String isNullable = rs.getString("IS_NULLABLE");
				MainPlugin.getDefault().logInfo("字段是否可空: " + isNullable);
				// 设置TableField
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
			MainPlugin.getDefault().logInfo("列数量: " + columnFieldList.size());
			table.setTableFields(columnFieldList.toArray(new TableField[0]));
		} else {
			MainPlugin.getDefault().logError("表" + tableName + "未定义列或不存在。");
			//throw new SDPBuildException("表" + tableName + "未定义列或不存在。");
		}
		return table;
	}

	/**
	 * 该方法返回转换后的数据库目录(Database Catalog)
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
	 * 该方法返回转换后的数据库方案(Database Schema)
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
	 * 该方法执行表名的大小写转换 注: 通过JDBC取元数据时需要按照不同数据库类型执行表名的大小写转换
	 * 
	 * @param dbType
	 * @param tableName
	 */
	private static String getTableName(String dbType, String tableName) {
		if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_SQLSERVER))
			return tableName; // SQLSERVER表名大小写均可
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_DB2))
			return tableName.toUpperCase(); // ORACLE需将表名大写
		else if (dbType.equals(IExport.SQL_EXPORT_DB_TYPE_ORACLE))
			return tableName.toUpperCase(); // DB2需将表名大写
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
	 * 该方法解析预置脚本表拓补结构，如果是主子表，则返回MainTable；单表则直接返回null
	 * 
	 * @param item_vo
	 * @return
	 */
	public static MainTable getMultiTableHierarchyStructure(Item item_vo, String multiTableRuleMapPath) {
		String hierarchyRuleFileName = item_vo.getItemRule() + ".xml";
		String hierarchyRuleFilePath = multiTableRuleMapPath + File.separator + hierarchyRuleFileName;
		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
			/* 单表结构 */
			return null;
		} else {
			/* 主-子表结构 */
			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, item_vo);
		}
	}

	/**
	 * 该方法解析预置脚本表拓补结构, 同时产生各级表的元数据信息， 检查当前itemVO是否为主子表，如果是的话返回表聚合VO，如果不是返回单表VO
	 * 
	 * @param itemVo 事实上只用到了itemRule和fixWhere两个属性
	 * @param con
	 * @return
	 */
//	public static MainTable getMultiTableHierarchyStructure(Item itemVo, SDPConnection con, String multiTableRuleMapPath)
//			throws SDPBuildException {
//		UnitDataSource uds = con.getDataSource();
//		String hierarchyRuleFileName = itemVo.getItemRule() + ".xml";
//		String hierarchyRuleFilePath = multiTableRuleMapPath + File.separator + hierarchyRuleFileName;
//		// 查找主子表文件，如果不存在则为单表结构，存在则为主子表结构
//		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
//			/* 单表结构 */
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
//			/* 主-子表结构 */
//			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, itemVo, con);
//		}
//	}
	
	/**
	 * 该方法解析预置脚本表拓补结构, 同时产生各级表的元数据信息， 检查当前itemVO是否为主子表，如果是的话返回表聚合VO，如果不是返回单表VO
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
		// 查找主子表文件，如果不存在则为单表结构，存在则为主子表结构
		if (!FileUtil.checkExist(hierarchyRuleFilePath)) {
			/* 单表结构 */
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
			/* 主-子表结构 */
			return XMLUtil.getMultiTableHierarchyStructure(hierarchyRuleFilePath, itemVo, con);
		}
		}
	
	
}
