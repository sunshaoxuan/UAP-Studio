package nc.uap.lfw.build.orm.mapping;

import java.util.List;


/**
 * 外键约束。
 * 
 * @author PH
 */
public interface IFkConstraint extends IConstraint {
	
	/**
	 * 获取关联的主表。
	 * 
	 * @return 关联的主表
	 */
	public ITable getRefTable();
	
	/**
	 * 获取关联表的对应字段。
	 * 
	 * @return 关联表的对应字段
	 */
	public List<IColumn> getRefColumns();
	
	/**
	 * 获取关联的外键映射关系。<br>
	 * Key: 子表的外键; Value: 主表对应子表外键的字段。
	 * 
	 * @return 外键映射关系
	 */
//	public Map<IColumn, IColumn> getRefCols();
	

}
