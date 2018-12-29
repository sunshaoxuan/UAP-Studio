package nc.uap.lfw.build.orm.mapping;

import java.util.ArrayList;
import java.util.List;

public class Index implements IIndex {
	
	private String name;
	
	private ITable table;
	
	private List<IColumn> columns;
	
	private boolean clustered;
	
	private boolean unique;
	
	private String desc;
	
	public Index() {
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

	public boolean isClustered() {
		return clustered;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTable(ITable table) {
		this.table = table;
	}

	public void setClustered(boolean clustered) {
		this.clustered = clustered;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

}
