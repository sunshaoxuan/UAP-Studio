package uap.lfw.smartmw;


public class SmartMwServiceWrapper {
	private SmartMetaVO component;
	private Object instance;
	public SmartMetaVO getComponent() {
		return component;
	}
	public void setComponent(SmartMetaVO component) {
		this.component = component;
	}
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object instance) {
		this.instance = instance;
	}
}
