package nc.uap.lfw.build.orm.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Լ����
 * 
 * @author PH
 */
public class Constraint implements IConstraint {
	
	/** Լ���� */
	private String name;
	/** ������ */
	private ITable table;
	/** Լ�������м� */
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
