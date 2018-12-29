package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * 实表、虚表(视图)基类接口。
 * 
 * @author PH
 */
public interface ITableBase {
	
	/**
	 * 获取表名。
	 * 
	 * @return 表名
	 */
	public String getName();
	
	/**
	 * 获取所有列。
	 * 
	 * @return 所有列
	 */
	public List<IColumn> getAllColumns();
	
//	public Map<String, IColumn> getAllColumns();// for performance and convenience
	
	/**
	 * 据列名获取列。
	 * 
	 * @param colName 列名
	 * @return 对应列
	 */
	public IColumn getColumnByName(String colName);
	
	/**
	 * 表描述信息。
	 * 
	 * @return 表描述信息
	 */
	public String getDesc();

}
