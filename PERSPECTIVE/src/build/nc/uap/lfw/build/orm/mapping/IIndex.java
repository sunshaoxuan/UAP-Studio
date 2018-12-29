package nc.uap.lfw.build.orm.mapping;

import java.util.List;

/**
 * �����ӿڡ�
 * 
 * @author PH
 */
public interface IIndex {
	
	public String getName();
	
	public ITable getTable();
	
	public List<IColumn> getColumns();
	
	public boolean isClustered();
	
	public boolean isUnique();
	
	public String getDesc();
}
