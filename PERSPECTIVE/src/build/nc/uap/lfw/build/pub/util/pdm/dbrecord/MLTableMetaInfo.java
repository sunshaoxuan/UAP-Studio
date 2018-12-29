package nc.uap.lfw.build.pub.util.pdm.dbrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����ֱ��Ԫ������Ϣ
 * @author syang
 *
 */

public class MLTableMetaInfo {
	private static final String ML_COLUMN_NAME_PATTERN = "\\d$";
	private String tableName;		//���������ֶεı���
	private List<String> columnNames;	//�������ֶε��ֶ���
	public String[] getColumnNames() {
		if(columnNames != null){
			String[] retValue = new String[columnNames.size()];
			columnNames.toArray(retValue);
			return retValue;
		}else {
			return null;
		}
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void addColumnName(String columnName){
		if(columnName != null){
			if(columnNames == null){
				columnNames = new ArrayList<String>();
			}
			columnNames.add(columnName.toLowerCase());
		}
	}
		
	public boolean includeColumn(String columnName){
		String lcColumnName = columnName.toLowerCase();
		if(columnNames.contains(lcColumnName)){
			return true;
		}else{
			for (String column : columnNames) {
				Pattern p = Pattern.compile(column+ML_COLUMN_NAME_PATTERN);
				Matcher m = p.matcher(lcColumnName); 
				if(m.find()){
					return true;
				}
			}
			return false;
		}
	}
}
