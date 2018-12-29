package uap.lfw.smartmw;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class SmartMwDataSourcePool {
	private Map<String, DataSource> dsPool = new HashMap<String, DataSource>(2);
	private static SmartMwDataSourcePool instance = new SmartMwDataSourcePool();
	private SmartMwDataSourcePool() {
		
	}
	
	public static SmartMwDataSourcePool getInstance() {
		return instance;
	}
	
	public void regester(String name, DataSource ds){
		dsPool.put(name, ds);
	}
	
	public DataSource getDataSource(String name) {
		return dsPool.get(name);
	}
}
