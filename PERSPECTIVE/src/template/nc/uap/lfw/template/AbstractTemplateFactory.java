package nc.uap.lfw.template;


public abstract class AbstractTemplateFactory implements ITemplatePageFactory {
	private String appId;
	public String getAppId() {
		return appId;
	}
	@Override
	public void setAppId(String appid) {
		this.appId = appid;
	}

}