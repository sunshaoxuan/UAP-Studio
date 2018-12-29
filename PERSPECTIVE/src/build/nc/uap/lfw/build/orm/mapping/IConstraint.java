package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * 约束抽象接口。有以下子约束：
 * <ol>
 * 	<li>主键约束</li>
 * 	<li>外键约束</li>
 * 	<li>唯一性约束</li>
 * </ol>
 * 
 * @author PH
 */
public interface IConstraint {
	
	public String getName();
	
	public ITable getTable();
	
	public List<IColumn> getColumns();

}
