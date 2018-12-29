package nc.uap.lfw.build.orm.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 约束。
 * 
 * @author PH
 */
public class Constraint implements IConstraint {
	
	/** 约束名 */
	private String name;
	/** 所属表 */
	private ITable table;
	/** 约束作用列集 */
	private List<IColumn> columns;
	
	public Constraint() {
		columns = new ArrayList<IColumn>();
	}

	public List<IColumn> getColumns() {
		return columns;
	}

	public String getName() {
		return name;
	}

	public ITable getTable() {
		return table;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public void setColumns(List<IColumn> columns) {
		this.columns = columns;
	}
	
	

}
