package nc.uap.portal.plugins.model;

/**
 * 动态插件
 * <pre>PluginManager在插件初始化的时候,发现插件实现了IDynamicalPlugin接口,则调用init方法,将Plugin里配置的名称,id,标题信息动态注入</pre>
 * @author licza
 *
 */
public interface IDynamicalPlugin {
	
	/**
	 * 初始化插件
	 * @param id
	 * @param i18nname
	 * @param title
	 */
	void init(String id,String i18nname,String title);
	String getId();
	String getI18nname();
	String getTitle();
}
