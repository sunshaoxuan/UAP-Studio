package nc.uap.lfw.build.dbrecord.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.dbrecord.itf.IMLTableMetaInfoFinder;
import nc.uap.lfw.build.exception.SdpBuildRuntimeException;
import nc.uap.lfw.build.pub.util.pdm.SDPConnection;
import nc.uap.lfw.build.pub.util.pdm.dbrecord.MLTableMetaInfo;

/**
 * 数据库中含多语字段的表元数据查询类
 * @author syang
 *
 */

public class DbMLTableMetaInfoFinder implements IMLTableMetaInfoFinder {
	
	private SDPConnection con;
	
	public DbMLTableMetaInfoFinder(SDPConnection con){
		this.con = con;
		
	}

	public MLTableMetaInfo[] getMLTableMetaInfo() throws SdpBuildRuntimeException{
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select distinct m.tableid as tableName, c.name as columnName from md_column c inner join md_ormap m on c.id = m.columnid and m.attributeid in (select id from md_property where datatype='BS000010000100001058') order by m.tableid, c.name";
		try {
			conn = con.getPhysicalConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			Map<String, MLTableMetaInfo> mlTableMetaInfos = new HashMap<String, MLTableMetaInfo>();
			while(rs.next()){
				String tableName = rs.getString(1);
				String columnName = rs.getString(2);
				
				MLTableMetaInfo metaInfo = null;
				if(mlTableMetaInfos.containsKey(tableName)){
					metaInfo = mlTableMetaInfos.get(tableName);
				}else{
					metaInfo = new MLTableMetaInfo();
					metaInfo.setTableName(tableName);
					mlTableMetaInfos.put(tableName, metaInfo);
				}
				metaInfo.addColumnName(columnName);
				
			}
			if(mlTableMetaInfos != null){
				MLTableMetaInfo[] retValue = new MLTableMetaInfo[mlTableMetaInfos.size()];
				mlTableMetaInfos.values().toArray(retValue);
				return retValue;
				
			}else{
				return null;
			}
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

	}

}
