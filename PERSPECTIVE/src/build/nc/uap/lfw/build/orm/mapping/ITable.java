package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * 数据库表的元数据相关信息接口。
 * 
 * @author PH
 */
public interface ITable extends ITableBase{
	
	/**
	 * 获取主键约束。
	 * 
	 * @return 主键约束
	 */
	public IPkConstraint getPkConstraint();
	
	/**
	 * 获取外键约束集。
	 * 
	 * @return 外键约束集
	 */
	public List<IFkConstraint> getFkConstraints();
	
	/**
	 * 获取关联到指定表的外键约束。
	 * 
	 * @param refTableName 主表
	 * @return 关联到指定表的外键约束
	 */
	public IFkConstraint getFkConstraintByRefTableName(String refTableName);
	
}
