package nc.uap.lfw.build.dao;

import java.util.List;

import nc.uap.lfw.build.orm.mapping.ITable;

/**
 * 数据查询信息接口。
 * 
 * @author PH
 */
public interface IQueryInfo {
	
	/**
	 * 获取表元数据信息。
	 * 
	 * @return 表元数据信息
	 */
	public ITable getTable();
	
	/**
	 * 获取查询条件。
	 * 
	 * @return 查询条件
	 */
	public String getWhereCondition();
	
	/**
	 * 获取子表查询信息。
	 * 
	 * @return 子表查询信息
	 */
	public List<? extends IQueryInfo> getChilds();

	//public LinkedHashMap<String, Boolean> getSortField();
}
