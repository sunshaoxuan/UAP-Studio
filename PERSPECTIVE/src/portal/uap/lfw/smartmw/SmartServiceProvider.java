package uap.lfw.smartmw;

import java.util.HashMap;
import java.util.Map;

import nc.uap.lfw.core.exception.LfwRuntimeException;

public class SmartServiceProvider implements ISmartServiceProvider{
	

	private Map<String, SmartMwServiceWrapper> serviceMap = new HashMap<String, SmartMwServiceWrapper>();
	
	private static SmartServiceProvider instance;
	
	private SmartServiceProvider() {
	}
	
	public static SmartServiceProvider getInstance() {
		return instance;
	}
	
	public static void createInstance() {
		instance = new SmartServiceProvider();
	}
	
	public Object getService(String name){
		if(name.equals("design"))
			return SmartMwDataSourcePool.getInstance().getDataSource(name);
//		if(name.equals(NC_BS_FRAMEWORK_CORE_SERVICE_REQUEST_ATTRIBUTE_MANAGER))
//			return getService(NC_BS_FRAMEWORK_COMMON_I_ATTRIBUTE_MANAGER);
//		if(name.equals("nc.bs.framework.core.service.TimeService")){
//			Object obj = LfwClassUtil.newInstance("uap.lfw.smartmw.SmartLocator");
//			Method m;
//			try {
//				m = obj.getClass().getMethod("getService", new Class[]{String.class});
//				return m.invoke(null, new Object[]{name});
//			} 
//			catch (Exception e) {
//				throw new LfwRuntimeException("not found:" + name);
//			}
//		}
		SmartMwServiceWrapper wrapper = serviceMap.get(name);
		if(wrapper != null){
			return wrapper;
		}
		throw new LfwRuntimeException("not found:" + name);
	}


	@Override
	public void regesterService(String name, SmartMetaVO meta) {
		SmartMwServiceWrapper wrapper = new SmartMwServiceWrapper();
		wrapper.setComponent(meta);
		serviceMap.put(name, wrapper);
	}
}
