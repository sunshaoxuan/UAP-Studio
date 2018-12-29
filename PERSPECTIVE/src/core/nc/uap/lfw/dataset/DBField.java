package nc.uap.lfw.dataset;

import java.util.ArrayList;

//import ncmdp.model.Type;
//import ncmdp.tool.NCMDPTool;

public class DBField {
	private String name = "";
	private String displayName = "";
	private String type = "";
	private String defValue = "";
	private boolean isSel = true;
	private String length = "";
	private boolean isKey =false;
	private String precision = "";
	private FieldType moduleType = null;
	public String getDefValue() {
		return defValue;
	}
	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSel() {
		return isSel;
	}
	public void setSel(boolean isSel) {
		this.isSel = isSel;
	}
	public FieldType getModuleType() {
		if(moduleType==null){
			FieldType []types = DataBaseTool.getBaseTypes();
			ArrayList<FieldType> al = new ArrayList<FieldType>();
			for (int i = 0; i < types.length; i++) {
				if(types[i].getDbType() != null && types[i].getDbType().equalsIgnoreCase(getType())){
					al.add(types[i]);
				}
			}
			if(al.size() == 0){
				return null;
			}else if(al.size() == 1){
				moduleType = al.get(0);
			}else{
				types = al.toArray(new FieldType[0]);
				for (int i = 0; i < types.length; i++) {
					if(!(types[i].getLength() != null && types[i].getLength().equalsIgnoreCase(getLength()))){
						al.remove(types[i]);
					}
				}
			}
			if(al.size() == 0){
				moduleType = types[0];
			}else if(al.size() == 1){
				moduleType = al.get(0);
			}else{
				types = al.toArray(new FieldType[0]);
				for (int i = 0; i < types.length; i++) {
					if(!(types[i].getPrecise() != null && types[i].getPrecise().equalsIgnoreCase(getPrecision()))){
						al.remove(types[i]);
					}
				}
			}
			if(al.size() == 0){
				moduleType = types[0];
			}else {
				moduleType = al.get(0);
			}
		}
		return moduleType;
	}
	
	public void setModuleType(FieldType moduleType) {
		this.moduleType = moduleType;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getTypeDisplayString(){
		StringBuilder sb = new StringBuilder(getType());
		if(getLength() != null && getLength().trim().length() > 0){
			sb.append("(").append(getLength());
			if(getPrecision() != null && getPrecision().trim().length() > 0){
				sb.append(",").append(getPrecision());
			}
			sb.append(")");
		}
		return sb.toString();
		
	}
	public boolean isKey() {
		return isKey;
	}
	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}
}
