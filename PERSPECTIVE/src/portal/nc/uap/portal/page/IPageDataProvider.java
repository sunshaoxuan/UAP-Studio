package nc.uap.portal.page;

import java.io.UnsupportedEncodingException;


/**
 *与Flex数据交互
 * 
 * @author dingrf
 */
public interface IPageDataProvider {
	/**
	 *读取Portlet分类 
	 */
	public void loadPortletCates(PortalPageEditor portalPageEditor,String callBack);
	
	/**
	 *读取Portlet
	 */
	public void loadPortlet(PortalPageEditor portalPageEditor,String callBack, String cateId);

	/**
	 *读取theme
	 */
	public void loadTheam(PortalPageEditor portalPageEditor, String callBack,String groupId);

	/**
	 *读取skin
	 */
	public void loadSkin(PortalPageEditor portalPageEditor, String callBack,String type);

	/**
	 *读取page
	 */
	public void loadXML(PortalPageEditor portalPageEditor, String callBack);

	/**
	 *保存page
	 * @throws UnsupportedEncodingException 
	 */
	public void saveXml(PortalPageEditor portalPageEditor, String callBack,String xml) throws UnsupportedEncodingException;
	
	public void getLinkgroups(PortalPageEditor portalPageEditor, String callBack);
	
	public void getLangRes(PortalPageEditor portalPageEditor, String callBack);
	
	public void getDevices(PortalPageEditor portalPageEditor, String callBack);
	
	
	
}
