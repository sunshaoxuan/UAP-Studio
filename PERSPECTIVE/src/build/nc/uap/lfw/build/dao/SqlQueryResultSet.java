package nc.uap.lfw.build.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * 主子表查询结果。
 * 
 * @author PH
 */
public class SqlQueryResultSet {
	
	/** 主表元数据 */
	private ITable table;
	/** 主表结果集 */
	private List<Map<String, Object>> results;
	/** 子表查询结果 */
	private List<SqlQueryResultSet> subResultSets;
	
	public SqlQueryResultSet(ITable table){
		this.table = table;
		results = new ArrayList<Map<String,Object>>();
		subResultSets = new ArrayList<SqlQueryResultSet>();
	}

	public ITable getTable() {
		return table;
	}

	public List<Map<String, Object>> getResults() {
		return results;
	}

	public List<SqlQueryResultSet> getSubResultSets() {
		return subResultSets;
	}

}
