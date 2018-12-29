package nc.uap.portal.service.itf;

import java.io.File;
import java.util.List;
import java.util.Map;

import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.plugins.model.PtExtension;
import nc.uap.portal.plugins.model.PtExtensionPoint;

/**
 * 
 * Portal��������ṩ����
 * 
 *@since 1.6
 */
public interface IPortalDesignDataProvider {

	/**
	 * ��ȡ��Ŀ�����е�Portlet
	 * 
	 * @param projectPath		��Ŀ·��
	 * @param projectModuleName		��Ŀmodule���� 
	 * @return	��Ŀ�е�����portlet�б�
	 */
	public List<PortletDefinition> getAllPortlets(String projectPath,String projectModuleName);
	
 
	/**
	 * ɾ��ManagerApps
	 * 
	 * @param projectPath	��Ŀ·��
	 * @param projectModuleName	��Ŀmodule����
	 * @param id	ManagerApps��id
	 */
	public void deleteManagerApps(String projectPath,String projectModuleName,String id);
	
	public PortalDeployDefinition getPortalModule(String projectPath);
	
	public PortalModule getPortalModule(File file);
	
	/**
	 * ��ȡ��Ŀ�е� PortletApplicationDefinition
	 * 
	 * @param projectPath 	��Ŀ·��
	 * @param projectModuleName ��Ŀmodule����
	 * @return  ��Ŀ��PortletApplicationDefinition
	 */
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName);
	
	/**
	 * ��ȡ��Ŀ�е� PortletApplicationDefinition
	 * 
	 * @param projectPath 	��Ŀ·��
	 * @param projectModuleName ��Ŀmodule����
	 * @param portletAppText	PortletApplicationDefinition�ַ���
	 * @return  ��Ŀ��PortletApplicationDefinition
	 */
	public PortletApplicationDefinition getPortletApp(String projectPath,String projectModuleName,String portletAppText);
	
	/**
	 * ����PortletApplicationDefinition
	 * 
	 * @param projectPath	��Ŀ·��
	 * @param projectModuleName	��Ŀmodule����
	 * @param portletApp PortletApplicationDefinition����
	 */
	public void savePortletAppToXml(String projectPath,String projectModuleName, PortletApplicationDefinition portletApp);

	/**
	 * ��ȡ��Ŀ�е� PortalModule ����
	 * 
	 * @param projectPath  	��Ŀ·��
	 * @param projectModuleName 	��Ŀmodule����
	 * @return	��ĿPortalModule����
	 */
    public PortalModule getPortal(String projectPath,String projectModuleName);

    /**
     * ������ĿPortalModule
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param portalModule	��ĿPortalModule����
     */
    public void savePortalToXml(String projectPath,String projectModuleName,PortalModule portalModule);
    
 
    /**
     * ��ȡ��Ŀportlet����
     * 
     * @param projectPath ��Ŀ·��
     * @param projectModuleName ��Ŀmodule����
     * @return	��Ŀportlet����
     */
    public Display getDisplay(String projectPath,String projectModuleName);
    
    /**
     * ��ȡ��Ŀportlet���࣬��������ģ���
     * 
     * @param projectPath ��Ŀ·��
     * @param projectModuleName ��Ŀmodule����
     * @return	��Ŀportlet����
     */
    public List<Display> getAllDisplays(String projectPath,String projectModuleName);
    
    /**
     * ������Ŀportlet����
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName ��Ŀmodule����
     * @param display	Display����
     */
    public void saveDisplayToXml(String projectPath,String projectModuleName,Display display);
    
    /**
     * ��ȡ��ĿPage�����б�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @return	��ĿPage�����б�
     */
    public Page[] getAllPages(String projectPath,String projectModuleName);

    /**
     * ��ȡ��ĿPage����
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param PageName	Page����
     * @return	Page����
     */
    public Page getPage(String projectPath,String projectModuleName,String PageName);

    /**
     * Page����ת����XML�ַ���
     * 
     * @param page	Page����
     * @return	Page����ת���ɵ�XML�ַ���
     */
    public String  pageToString(Page page);

    /**
     * XML�ַ���ת����Page����
     * 
     * @param xml	
     * @return	Page����
     */
    public Page  stringToPage(String xml);

    /**
     * ����Page����
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param fileName	Page����
     * @param xml	Page��XML�ַ���
     */
    public void  savePageToXml(String projectPath,String projectModuleName,String fileName,String xml);

    /**
     * ����Page����
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param page	Page����
     */
    public void  savePageToXml(String projectPath,String projectModuleName,Page page);
    
    /**
     * ɾ����Ŀ�е�Page
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param pageName	Page����
     */
    public void deletePage(String projectPath,String projectModuleName,String pageName);
    
    /**
     * ��ȡ��ĿLookAndFeel����
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @return	LookAndFeel����
     */
    public LookAndFeel getLookAndFeel(String projectPath,String projectModuleName);

    /**
     * ����LookAndFeel
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName ��Ŀmodule����
     * @param lookAndFeel	LookAndFeel����
     */
    public void saveLookAndFeelToXml(String projectPath,String projectModuleName,LookAndFeel lookAndFeel);
    
    /**
     * ����LookAndFeel
     * 
     * @param projectPath	��Ŀ·��
     * @param theme �������
     * @param actionType	��������
     */
    public void updateLookAndFeelToXml(String projectPath,Theme theme,int actionType);
    
    /**
     * ��ȡ��ʽ�б�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @return
     */
    public List<Skin> getSkins(String projectPath,String projectModuleName,String type);

    /**
     * ��ȡ��ʽ�б�,��������ģ���е�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @return
     */
    public List<Skin> getAllSkins(String projectPath,String projectModuleName,String type);
    
    /**
     * ��ȡ��ʽ�����ļ�
     * 
     * @param projectPath ��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @param themeId	����id
     * @return	��ʽ�����ļ�
     */
    public SkinDescription getSkinDescription(String projectPath,String projectModuleName,String type,String themeId);
    
    /**
     * ������ʽ�����ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @param themeId	����id
     * @param skinDescription	��ʽ�����ļ�
     */
    public void saveSkinDescription(String projectPath,String projectModuleName,String type,String themeId,SkinDescription skinDescription);

    /**
     * ������ʽ�����ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param skin	��ʽ����
     * @param actionType ��������
     */
    public void updateSkinDescription(String projectPath,Skin skin,int actionType);

    /**
     * ������ʽ�ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @param themeId	����id
     * @param fileName	��ʽ����
     * @param fileText	��ʽXML�ַ���
     */
    public void createSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName,String fileText);
    
    /**
     * ������ʽ�ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param skin	��ʽ����
     */
    public void createSkinFile(String projectPath,Skin skin);

    /**
     * ɾ����ʽ�ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param type	��ʽ����
     * @param themeId	����id
     * @param fileName	��ʽ����
     */
    public void deleteSkinFile(String projectPath,String projectModuleName,String type,String themeId,String fileName);
    
    /**
     * ɾ����ʽ�ļ�
     * 
     * @param projectPath	��Ŀ·��
     * @param skin	��ʽ����
     */
    public void deleteSkinFile(String projectPath,Skin skin);
    
    /**
     * ���������ļ���
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param theme	����
     */
    public void createThemeFolder(String projectPath,String projectModuleName,Theme theme);
    
    /**
     * ɾ�������ļ���
     * 
     * @param projectPath	��Ŀ·��
     * @param projectModuleName	��Ŀmodule����
     * @param themeId	����id
     */
    public void deleteThemeFolder(String projectPath,String projectModuleName,String themeId);
    
    /**
     * ��ȡ����ҳ��Map 
     * 
     * @param projPaths	��Ŀ·��
     * @return ����ҳ��Map	
     */
    public Map<String, String>[] getPageNames(String[] projectPath);
    
    /**
     * ����PortalModule
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
    public void deployPortal(String projectModuleName);
    /**
     * ����ManagerApps
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deployManagerApps(String projectModuleName);
	
    /**
     * ����PortletApp
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deployPortletApp(String projectModuleName);
	
    /**
     * ����PtPlugin
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deployPtPlugin(String projectModuleName);
	
    /**
     * ����Display
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deployDisplay(String projectModuleName);
	
    /**
     * ����Page
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deployPage(String projectModuleName, String pageName);
	
    /**
     * ��������
     * 
     */
	public void deployLookAndFeel();
	
    /**
     * ������ʽ 
     * 
     * @param projectModuleName   Ҫ�����ģ������
     */
	public void deploySkin(String projectModuleName);
	

	
    /**
     * ��ȡ������չ�� 
     * 
     */
	public PtExtensionPoint[] getAllPtExtPoints();

    /**
     * ��ȡĳ��չ���������չ
     *
     * @param point   Ҫ��ȡ��չ����չ��
     */
	public PtExtension[] getPtExtensionsByPoint(String point);

}
