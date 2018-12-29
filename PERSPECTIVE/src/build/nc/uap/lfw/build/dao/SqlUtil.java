package nc.uap.lfw.build.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.orm.mapping.Column;
import nc.uap.lfw.build.orm.mapping.FkConstraint;
import nc.uap.lfw.build.orm.mapping.IColumn;
import nc.uap.lfw.build.orm.mapping.IFkConstraint;
import nc.uap.lfw.build.orm.mapping.IPkConstraint;
import nc.uap.lfw.build.orm.mapping.ITable;
import nc.uap.lfw.build.orm.mapping.PkConstraint;
import nc.uap.lfw.build.orm.mapping.Table;

import org.apache.commons.lang.StringUtils;

/**
 * 据查询条件进行DB查询工具。
 * 
 * @author PH
 */
public class SqlUtil {
	private static final String DB_TYPE_ORACLE = "Oracle";
	
	private static final String DB_TYPE_SQLSERVER = "Microsoft SQL Server";//SQLSERVER
	
	private static final String DB_TYPE_DB2 = "DB2";
	
	/**
	 * 通过表名等信息获取表元数据信息。对同一Connection的多表获取用
	 * {@link #retrieveTable(String, List, DatabaseMetaData, String, String)}更高效。
	 * 
	 * @param tableName
	 * @param fkColNames
	 * @param conn
	 * @return
	 * @throws SdpBuildRuntimeException 当
	 * 1.数据库表不存在或未定义列。2.指定的外键列在表中不存在。3.数据库错误。
	 */
	public static ITable retrieveTable(String tableName, List<String> fkColNames, Connection conn) 
			throws SdpBuildRuntimeException{
		DatabaseMetaData metaData = null;
		String dbType = null, userName = null;
		try {
			metaData = conn.getMetaData();
			dbType = metaData.getDatabaseProductName();
			userName = metaData.getUserName();
		}catch (SQLException e) {
			MainPlugin.getDefault().logError("获取表" + tableName + "元数据出错。",e);
			throw new SdpBuildRuntimeException("获取表" + tableName + "元数据出错。");
		}
		return retrieveTable(tableName, fkColNames, metaData, dbType, userName);
	}
	
	/**
	 * 通过表名等信息获取表元数据信息。
	 * 
	 * @param tableName
	 * @param fkColNames
	 * @param metaData
	 * @param dbType
	 * @param userName
	 * @return
	 * @throws SdpBuildRuntimeException 当
	 * 1.数据库错误。2.数据库表不存在或未定义列。3.指定的外键列在表中不存在。
	 */
	public static ITable retrieveTable(String tableName, List<String> fkColNames, 
			DatabaseMetaData metaData, String dbType, String userName) throws SdpBuildRuntimeException{
		Table table = new Table();
		table.setName(tableName);
		
		//1.获取主键列名。（复合主键需要按构成主键的字段顺序排列）

		String[] pkColNamesBySeq = new String[10];
		int nPos = 0;		
		ResultSet pkRs = null;
		try {
			pkRs = metaData.getPrimaryKeys(retriveCatelog(dbType), 
					retriveSchema(dbType, userName), 
					formatTableName(tableName, dbType));
			while(pkRs.next()){
				String colName = pkRs.getString("COLUMN_NAME");
				short seq = pkRs.getShort("KEY_SEQ");
				pkColNamesBySeq[seq-1] = colName;
				nPos++;
			}
		} catch(SQLException e){
			MainPlugin.getDefault().logError("获取表" + tableName + "主键信息出错。",e);
			throw new SdpBuildRuntimeException("获取表" + tableName + "主键信息出错。");
		} finally{
			if(pkRs != null){
				try {
					pkRs.close();
				} catch (SQLException e) {
					MainPlugin.getDefault().logError("Close result set error.",e);
				}
			}
		}
		
		List<String> pkColNames = new ArrayList<String>(nPos);
		for(int i=0; i<nPos; i++){
			pkColNames.add(pkColNamesBySeq[i]);
		}
		
		List<IColumn> allCols = table.getAllColumns(); 
		PkConstraint pkConstraint = new PkConstraint();
		table.setPkConstraint(pkConstraint);
		IColumn[] pkCols = new IColumn[pkColNames.size()];

		List<IFkConstraint> fkConstraints = table.getFkConstraints();
		boolean hasFkCols = false;
		List<String> upperFkColNames = null;
		FkConstraint fkConstraint = null;
		if(fkColNames != null && !fkColNames.isEmpty()){
			hasFkCols = true;
			upperFkColNames = new ArrayList<String>();
			for(String str : fkColNames){
				upperFkColNames.add(str.toUpperCase());
			}
			fkConstraint = new FkConstraint();
			fkConstraints.add(fkConstraint);
		}
		
		//2.获取表所有列。
		ResultSet colRs = null;
		try {
			colRs = metaData.getColumns(retriveCatelog(dbType), retriveSchema(dbType, userName), 
					formatTableName(tableName, dbType), "%");
			while(colRs.next()){
				String colName = colRs.getString("COLUMN_NAME");
				short dataType = colRs.getShort("DATA_TYPE");
				String typeName = colRs.getString("TYPE_NAME");
				Column col = new Column();
				col.setName(colName);
				col.setDataType(dataType);
				col.setTypeName(typeName);
				allCols.add(col);
				
				if(hasFkCols && upperFkColNames.contains(colName.toUpperCase())){
					fkConstraint.getColumns().add(col);
				}else{
					if(pkColNames.contains(colName)){
						int pos = pkColNames.indexOf(colName);						
						pkCols[pos] = col;
					}
				}
			}
			if(allCols.isEmpty()){
//				throw new SdpBuildRuntimeException("表" + tableName + "不存在或未定义列。");
				return null;
			}else if(hasFkCols && fkConstraint.getColumns().isEmpty()){//校验外键列在表中是否存在。
				throw new SdpBuildRuntimeException(new StringBuilder("表")
					.append(tableName).append("的外键列")
					.append(StringUtils.join(fkColNames.iterator(), ",")).append("在表中不存在。").toString());
			}
			pkConstraint.getColumns().addAll(Arrays.asList(pkCols));
			return table;
		} catch(SQLException e){
			MainPlugin.getDefault().logError(String.format("获取表%s列信息出错。", tableName));
			throw new SdpBuildRuntimeException(String.format("获取表%s列信息出错。", tableName));
		} finally{
			if(colRs != null){
				try {
					colRs.close();
				} catch (SQLException e) {
					MainPlugin.getDefault().logError("Close result set error.",e);
				}
			}
		}
	}
	
	
	/**
	 * 据QueryInfo查询主子表数据。无结果时返回null。
	 * 
	 * @param queryInfo
	 * @param conn
	 * @return
	 * @throws SdpBuildRuntimeException
	 */
	public static SqlQueryResultSet queryResults(IQueryInfo queryInfo, Connection conn) 
			throws SdpBuildRuntimeException{
		return queryResults(queryInfo, queryInfo.getWhereCondition(), conn);
	}
	
	/**
	 * 据表元数据及查询条件查询单表数据。
	 * 
	 * @param table
	 * @param whereCondition
	 * @param conn
	 * @return
	 * @throws SdpBuildRuntimeException
	 */
	public static SqlQueryResultSet queryResults(ITable table, String whereCondition, Connection conn) 
			throws SdpBuildRuntimeException{
		if(table != null){
			checkTable(table);
			String sql = getSql(table, whereCondition);
			if(MainPlugin.getDefault().isDebugging()){
				MainPlugin.getDefault().logInfo("Query: " + sql);
			}
			
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				boolean exist = false;
				SqlQueryResultSet sqlQueryResultSet = null;
				while(rs.next()){
					if(!exist){
						exist = true;
						sqlQueryResultSet = new SqlQueryResultSet(table);
					}
					Map<String, Object> colNameValueMap = new LinkedHashMap<String, Object>();
					for(IColumn col : table.getAllColumns()){
						colNameValueMap.put(col.getName(), rs.getObject(col.getName()));
					}
					sqlQueryResultSet.getResults().add(colNameValueMap);
				}
				return sqlQueryResultSet;
			} catch (SQLException e) {
				MainPlugin.getDefault().logError("数据库查询异常:",e);
				throw new SdpBuildRuntimeException("数据库查询异常，请检查查询条件是否有效。");
			}finally{
				if(rs != null){
					try {
						rs.close();
					} catch (SQLException e) {
						MainPlugin.getDefault().logError("Failed to close ResultSet.",e);
					}
				}
				if(stmt != null){
					try {
						stmt.close();
					} catch (SQLException e) {
						MainPlugin.getDefault().logError("Failed to close Statement.",e);
					}
				}
			}
			
		}else{
			throw new SdpBuildRuntimeException("查询条件参数有误。");
		}
	}
	
	public static String formatSql(Object obj, int dataType){
		//1.'' ; 2.特殊字符...
		if(obj != null){
			String str = obj.toString().trim();
			if(isTypeString(dataType)){
				str = str.replace("'", "''");
				str = "'" + str + "'";
			}
			return str;
		}
		return "null";
	}
	
	public static String geneInClause(String col, Collection<String> values){
		StringBuilder sql = new StringBuilder(col).append(" in(");
		int i = 0, remainedSize = values.size();
		for(String str : values){
			i++;
			remainedSize--;
			sql.append(str).append(",");
			if(i >= 100 && remainedSize > 0){
				sql.deleteCharAt(sql.length()-1);
				sql.append(")");
				sql.append(" or ").append(col).append(" in(");
				i = 0;
			}
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		return sql.toString();
	}
	
	private static SqlQueryResultSet queryResults(IQueryInfo queryInfo, String whereCondition, Connection conn) throws SdpBuildRuntimeException{
		//1.获取主表结果
		ITable mainTable = queryInfo.getTable();
		SqlQueryResultSet sqlQueryResultSet = queryResults(mainTable, whereCondition, conn);
		
		//2.处理子表结果。
		if(sqlQueryResultSet != null && !sqlQueryResultSet.getResults().isEmpty()){
			if(queryInfo.getChilds() != null && !queryInfo.getChilds().isEmpty()){
				IPkConstraint pkConstraint = queryInfo.getTable().getPkConstraint();
				List<IColumn> pkCols = pkConstraint != null ? pkConstraint.getColumns() : null;
				if(pkCols == null || pkCols.isEmpty()){
					throw new SdpBuildRuntimeException("表"+mainTable.getName() + "不存在主键。");
				}else if(pkCols.size() != 1){
					throw new SdpBuildRuntimeException("表"+mainTable.getName() + "为复合主键，不支持子表查询。");
				}
				List<Map<String, Object>> results = sqlQueryResultSet.getResults();
				
				String pkName = pkCols.get(0).getName();
				int dataType = pkCols.get(0).getDataType();
				Set<String> pks = new HashSet<String>();
				for(Map<String, Object> colNameValue : results){
					Object obj = colNameValue.get(pkName);
					pks.add(formatSql(obj, dataType));
				}
				
				for(IQueryInfo subQryInfo : queryInfo.getChilds()){
					ITable subTable = subQryInfo.getTable();
					IFkConstraint fkConstraint = null;
					if(subTable.getFkConstraints() != null){
						if(subTable.getFkConstraints().size() == 1){
							fkConstraint = subTable.getFkConstraints().get(0);
						}else{
							fkConstraint = subTable.getFkConstraintByRefTableName(mainTable.getName());
						}
					}
					if(fkConstraint == null){
						throw new SdpBuildRuntimeException("子表" + subTable.getName() + "没有外键列。");
					}else if(fkConstraint.getColumns().size() != 1){//...
						throw new SdpBuildRuntimeException("子表" + subTable.getName() + "为多外键列，不支持。");
					}else{
						SqlQueryResultSet subResultSet = queryResults(subQryInfo, 
								geneInClause(fkConstraint.getColumns().get(0).getName(), pks), conn);
						if(subResultSet != null){
							sqlQueryResultSet.getSubResultSets().add(subResultSet);
						}
					}
				}
			}
		}
		return sqlQueryResultSet;
	}

	private static void checkTable(ITable table) throws SdpBuildRuntimeException{
		if(table != null && StringUtils.isNotBlank(table.getName()) 
				&& table.getAllColumns() != null && !table.getAllColumns().isEmpty()){
			return;
		}
		throw new SdpBuildRuntimeException("查询条件参数有误。");
	}
	
	private static String getSql(ITable table, String whereCondition){
		StringBuilder sql = new StringBuilder("select ");
		for(IColumn col : table.getAllColumns()){
			sql.append(col.getName()).append(", ");
		}
		sql.delete(sql.length() - 2, sql.length());
		sql.append(" from ").append(table.getName());
		if(StringUtils.isNotBlank(whereCondition)){
			sql.append(" where ").append(whereCondition);
		}/*else{
			sql.append(" where 1 != 1");
		}*/
		return sql.toString();
	}
	
	private static boolean isTypeString(int dataType){
		return Types.CHAR == dataType || Types.VARCHAR == dataType 
				|| Types.LONGVARCHAR == dataType;//|| Types.LONGNVARCHAR == dataType
	}
	
	private static String retriveCatelog(String dataBaseType){
		if(DB_TYPE_ORACLE.equalsIgnoreCase(dataBaseType)){
			return "";
		}
		return null;
	}
	
	private static String retriveSchema(String dbType, String schema){
		if(DB_TYPE_SQLSERVER.equalsIgnoreCase(dbType)){
			return null;
		}else if(DB_TYPE_ORACLE.equalsIgnoreCase(dbType)){
			return schema.toUpperCase();
		}else if(DB_TYPE_DB2.equalsIgnoreCase(dbType)){
			return schema;
		}else{
			return null;
		}
	}
	
	private static String formatTableName(String tableName, String dbType){
		if(DB_TYPE_SQLSERVER.equalsIgnoreCase(dbType)){ //SQLSERVER表名大小写均可
			return tableName;
		}else if (DB_TYPE_ORACLE.equalsIgnoreCase(dbType)){//ORACLE需将表名大写
			return tableName.toUpperCase(); 
		}else if (DB_TYPE_DB2.equalsIgnoreCase(dbType)){//DB2需将表名大写
			return tableName.toUpperCase(); 
		}else{
			return null;
		}
	}

}
