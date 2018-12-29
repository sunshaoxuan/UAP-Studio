package nc.uap.lfw.dataset;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;

import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class DataBaseTool {
	// 数据库类型常量
	public static final int DBTYPE_UNKNOWN = -1;

	public static final int DBTYPE_SQLSEVER = 0; // SQLSERVER数据库

	public static final int DBTYPE_ORACLE = 1; // ORACLE数据库

	public static final int DBTYPE_DB2 = 2; // DB2数据库

	public static final int DBTYPE_INFORMIX = 3;// informix数据库

	public static Connection getConnection(Driver driver, String url, String user, String password) throws Exception {
		Connection con = null;
		try {
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
			throw e;
		}
		return con;
	}

	public static DBTable[] getAllTables(Connection con) throws Exception{
		ArrayList<DBTable> al = new ArrayList<DBTable>();
		try {
			String[] tableNames = getTableNames(con);
			int count = tableNames == null ? 0 : tableNames.length;
			for (int i = 0; i < count; i++) {
				DBTable table = new DBTable();
				table.setDisplayName(tableNames[i]);
				table.setName(tableNames[i]);
				DBField[] fields = getTableColumns(con, tableNames[i]);
				table.addField(fields);
				al.add(table);
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
			throw e;
		} 
		return al.toArray(new DBTable[0]);
	}

	public static int getDBType(Connection con) throws java.sql.SQLException {
		java.sql.DatabaseMetaData dmd = con.getMetaData();
		String dbname = dmd.getDatabaseProductName();
		int dbType = DBTYPE_UNKNOWN;
		dbname = dbname.toLowerCase();
		if (-1 != dbname.indexOf("sql server")) {
			dbType = DBTYPE_SQLSEVER;
		} else if (-1 != dbname.indexOf("oracle")) {
			dbType = DBTYPE_ORACLE;
		} else if (dbname.indexOf("db2") != -1) {
			dbType = DBTYPE_DB2;
		} else if (dbname.indexOf("informix") != -1) {
			dbType = DBTYPE_INFORMIX;
		}
		return dbType;
	}

	public static String[] getTableNames(Connection con) throws java.sql.SQLException {
		java.sql.Statement stmt = null;
		java.sql.ResultSet dbmdrs = null;
		java.util.Vector<String> v = new java.util.Vector<String>();
		try {
			int dbType = getDBType(con);
			stmt = con.createStatement();
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				dbmdrs = dbmd.getTables(null, null, null, new String[] { "TABLE" });
			else if (dbType == DBTYPE_ORACLE || dbType == DBTYPE_DB2 || dbType == DBTYPE_INFORMIX)
				dbmdrs = dbmd.getTables(null, dbmd.getUserName().toUpperCase(), null, new String[] { "TABLE" });
			while (dbmdrs.next()) {
				// 获得表名
				String tableName = dbmdrs.getString("TABLE_NAME");
				if ((dbType == DBTYPE_SQLSEVER && tableName.equalsIgnoreCase("dtproperties")) || (dbType == DBTYPE_ORACLE && tableName.equalsIgnoreCase("plan_table")))
					continue;
				//
				if (!v.contains(tableName))
					v.add(tableName);
			}
		} finally {
			try {
				if (dbmdrs != null)
					dbmdrs.close();
				if (stmt != null) {
					stmt.close();
				}

			} catch (Exception e) {
			}
		}

		String[] retrs = new String[v.size()];
		if (v.size() > 0)
			v.copyInto(retrs);
		return retrs;

	}

	public static DBField[] getTableColumns(java.sql.Connection con, String tableName) throws java.sql.SQLException {
		java.sql.ResultSet rs = null;
		// java.util.Vector v = new java.util.Vector();
		// java.util.Vector v1 = new java.util.Vector();
		ArrayList<String> v1 = new ArrayList<String>();
		ArrayList<DBField> v = new ArrayList<DBField>();
		ArrayList<String> pks = null;
		try {
			int dbType = getDBType(con);
			pks = getTablePKColNames(con, tableName);
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				rs = dbmd.getColumns(null, null, tableName.toUpperCase(), "%");
			else
				rs = dbmd.getColumns(null, dbmd.getUserName().toUpperCase(), tableName.toUpperCase(), "%");
			while (rs.next()) {
				String colName = rs.getString("COLUMN_NAME");
				if (!v1.contains(colName)) {
					v1.add(colName);
				} else
					continue;
				// int colType = rs.getInt("DATA_TYPE");
				String strColType = rs.getString("TYPE_NAME");
				int size = rs.getInt("COLUMN_SIZE");
				int digits = rs.getInt("DECIMAL_DIGITS");
				// String strIsNullAble = rs.getString("IS_NULLABLE");

				DBField field = new DBField();
				field.setKey(pks.contains(colName));
				field.setName(colName);
				field.setDisplayName(colName);
				field.setType(strColType);
				field.setLength(size + "");
				field.setPrecision(digits + "");
				v.add(field);

			}
		} finally {
			if (rs != null)
				rs.close();
		}

		return v.toArray(new DBField[0]);

	}

	public static ArrayList<String> getTablePKColNames(Connection con, String tableName) throws java.sql.SQLException {
		java.sql.ResultSet rs = null;
		ArrayList<String> v = new ArrayList<String>();
		try {
			int dbType = getDBType(con);
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			if (dbType == DBTYPE_SQLSEVER)
				rs = dbmd.getPrimaryKeys(null, null, tableName.toUpperCase());
			else if (dbType == DBTYPE_ORACLE || dbType == DBTYPE_DB2 || dbType == DBTYPE_INFORMIX)
				rs = dbmd.getPrimaryKeys(null, dbmd.getUserName().toUpperCase(), tableName.toUpperCase());
			while (rs.next()) {
				String pkStr = rs.getString("COLUMN_NAME");
				if (!v.contains(pkStr))
					v.add(pkStr);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {

			}
		}

		return v;
	}
	
	
	private static FieldType[] baseTypes = null;

	public static FieldType[] getBaseTypes() {
		if (baseTypes == null) {
			baseTypes = loadBaseTypes();
		}
		return baseTypes;
	}
	
	public static FieldType getBaseTypeById(String typeId) {
		FieldType[] types = getBaseTypes();
		FieldType type = null;
		int count = types == null ? 0 : types.length;
		for (int i = 0; i < count; i++) {
			if (types[i].getTypeId().equals(typeId)) {
				type = types[i];
			}
		}
		return type;

	}
	
	public static String getNCHome() {
		IStringVariableManager vvManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable ncHome = vvManager.getValueVariable(WEBPersConstants.FIELD_NC_HOME);
		String ncHomeStr = ncHome == null ? "" : ncHome.getValue();
		return ncHomeStr;
	}
	
	public static FieldType[] loadBaseTypes(){
		ArrayList<FieldType> al = new ArrayList<FieldType>();
		String ncHome = getNCHome();
		String baseTypeConfigFileStr = ncHome+"/ierp/metadata/baseType.xml";
		FileInputStream fis = null;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			fis = new FileInputStream(baseTypeConfigFileStr);
			Document doc = db.parse(fis);
			Node root = doc.getDocumentElement();
			if("baseTypes".equals(root.getNodeName())){
				NodeList nl = root.getChildNodes();
				int len = nl.getLength();
				for (int i = 0; i < len; i++) {
					Node node = nl.item(i);
					String nodeName = node.getNodeName();
					if(nodeName.equals("baseType")){
						FieldType type= new FieldType();
						NodeList nl1 = node.getChildNodes();
						for (int j = 0; j < nl1.getLength(); j++) {
							Node child = nl1.item(j);
							if("id".equals(child.getNodeName())){
								type.setTypeId(child.getFirstChild().getNodeValue());
							}else if("name".equals(child.getNodeName())){
								type.setName(child.getFirstChild().getNodeValue());
							}else if("displayName".equals(child.getNodeName())){
								type.setDisplayName(child.getFirstChild().getNodeValue());
							}else if("dbType".equalsIgnoreCase(child.getNodeName())){
								type.setDbType(child.getFirstChild().getNodeValue());
//								String dbTypelist = child.getFirstChild().getNodeValue();
//								if(dbTypelist != null && dbTypelist.trim().length() > 0){
//									String[] dbTypes = dbTypelist.split(",");
//									type.getDbTypeList().addAll(Arrays.asList(dbTypes));
//								}
							}else if("length".equalsIgnoreCase(child.getNodeName())){
								type.setLength(child.getFirstChild().getNodeValue());
							}else if("precise".equalsIgnoreCase(child.getNodeName())){
								type.setPrecise(child.getFirstChild().getNodeValue());
							}
						}
						al.add(type);
					}
				}
			}
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		} finally{
			if(fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
		}

		return al.toArray(new FieldType[0]);
	}

}
