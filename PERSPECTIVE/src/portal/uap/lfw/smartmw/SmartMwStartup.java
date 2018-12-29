package uap.lfw.smartmw;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.util.LfwClassUtil;
import nc.uap.plugin.studio.database.meta.DataSourceChangeManager;
import nc.uap.plugin.studio.database.meta.IDataSourceChangeListener;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import uap.lfw.smartmw.mw.EJBDataSource;
import uap.lfw.smartmw.mw.MiddleProperty;


public class SmartMwStartup implements IDataSourceChangeListener{
  
    private  Object center = null;
  
	private static final String DESIGN_DS = "design";
	
	public SmartMwStartup() {
      DataSourceChangeManager.registerListener(this);
    }

	public void startup() {

	  
		String uapHome = LfwCommonTool.getUapHome();
		
		String propXml = uapHome + "/ierp/bin/prop.xml";
		try {
			MiddleProperty props = new MiddleProperty(propXml);
			EJBDataSource ds = props.getMiddleParameter().getDataSource(DESIGN_DS);
			
			BasicDataSource bds = new BasicDataSource();
			bds.setUrl(ds.getDatabaseUrl());
			bds.setDefaultAutoCommit(true);
			bds.setUsername(ds.getUser());
			bds.setMaxActive(ds.getMaxCon());
			bds.setInitialSize(ds.getMinCon());
			bds.setPassword(ds.getPassword());
			bds.setDriverClassName(ds.getDriverClassName());
			SmartMwDataSourcePool.getInstance().regester(DESIGN_DS, bds);
//			bds.getConnection();
			MainPlugin.getDefault().logInfo("uaphome:"+uapHome);
			MainPlugin.getDefault().logInfo("BasicDataSource getConnection");
			initSimpleServices();
		} 
		catch (Exception e) {
			MwLogger.log(e);
		}
		
	      clearCache();
	      		
	}

  private synchronized void clearCache() {
    try {
      if (center == null){
          String className = "nc.jdbc.framework.DataSourceCenter";
          Class clazz = LfwClassUtil.forName(className);
          Field field = clazz.getDeclaredField("center");
          field.setAccessible(true);
          center = field.get(clazz);
        
      }
    
      center.getClass().getDeclaredMethod("clearCache").invoke(center);
      
    } catch (Exception e1) {
      MainPlugin.getDefault().logError(e1);
    }
  }
	
	

	private void initSimpleServices() {
		Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream("services.properties"));
			Iterator<Entry<Object, Object>> it = props.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object, Object> entry = it.next();
				SmartMetaVO meta = wrapMeta((String)entry.getKey(), (String)entry.getValue());
				SmartServiceProvider.getInstance().regesterService(meta.getKey(), meta);
//				MwLogger.logInfo("===register service:" + meta.getKey() + "," + meta.getValue());
			}
			MainPlugin.getDefault().logInfo("initSimpleServices finish");
			String uapHome = System.getProperty("nc.lfw.location");
//			MainPlugin.getDefault().logInfo("nchome:"+uapHome);
			
		} 
		catch (IOException e) {
			MwLogger.log(e);
		}
		
	}

	private SmartMetaVO wrapMeta(String key, String value) {
		SmartMetaVO meta = new SmartMetaVO();
		meta.setKey(key);
		meta.setValue(value);
		return meta;
	}



  @Override
  public void dataSourceChange() {
    this.startup();
  }



  @Override
  public void designDataSourceChange() {
    this.startup();
  }
	
}

class ServicePair{
	String itf;
	String imp;
	ServicePair(String itf, String imp){
		this.itf = itf;
		this.imp = imp;
	}
}
