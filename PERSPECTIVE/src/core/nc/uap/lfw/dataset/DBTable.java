package nc.uap.lfw.dataset;

import java.util.ArrayList;
import java.util.Arrays;

public class DBTable {
	private String name = "";
	private String displayName = "";
	private ArrayList<DBField> fields = new ArrayList<DBField>();
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<DBField> getFields() {
		return fields;
	}
	public void addField(DBField[] fields){
		if(fields != null)
			getFields().addAll(Arrays.asList(fields));
	}
	public void addField(DBField field){
		getFields().add(field);
	}
	public String toString(){
		return getDisplayName();
	}
}
