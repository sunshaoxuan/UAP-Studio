package uap.lfw.smartmw;


public interface ISmartServiceProvider {
	
	public Object getService(String name);
	
	public void regesterService(String name, SmartMetaVO meta);
}
