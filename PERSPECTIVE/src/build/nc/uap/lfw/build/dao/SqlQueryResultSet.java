package nc.uap.lfw.build.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * ���ӱ��ѯ�����
 * 
 * @author PH
 */
public class SqlQueryResultSet {
	
	/** ����Ԫ���� */
	private ITable table;
	/** �������� */
	private List<Map<String, Object>> results;
	/** �ӱ��ѯ��� */
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
