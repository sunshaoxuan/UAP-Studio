package nc.uap.portal.core;

import java.io.File;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.portal.container.om.PortletApplicationDefinition;
import nc.uap.portal.container.om.PortletDefinition;
import nc.uap.portal.deploy.vo.PortalDeployDefinition;
import nc.uap.portal.deploy.vo.PortalModule;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.SkinDescription;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalPlugin;
import nc.uap.portal.service.itf.IPortalDesignDataProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * service服务连接
 * 
 * @author dingrf
 * 
 */
public class PortalConnector {

	private static IPortalDesignDataProvider getProvider() {
		try {
			Class<?> c = Class.forName("nc.uap.portal.service.impl.PortalDesignDataProviderImpl", true, ClassPathProvider.getClassLoader());
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			return (IPortalDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * 检查文件
	 * 
	 * @param filePath
	 * @param fileName
	 */
	public static void checkFile(String filePath, String fileName) {
		String path = filePath + "/" + fileName;
		MainPlugin.getDefault().logInfo("文件路径：" + path);
		LFWAMCPersTool.checkOutFile(path);
	}

	/**
	 * 获取文件路径
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	private static String getPortalSpecPath(String projectPath, String projectModuleName) {
		return projectPath + "/src/portalspec/" + projectModuleName + "/portalspec/";
	}

	/**
	 * 获取项目中所有的Portlet
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static List<PortletDefinition> getAllPortlet(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List<PortletDefinition> portlets = getProvider().getAllPortlets(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return portlets;
	}

	/**
	 * 获取项目中的所有ManagerApps
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	// public static List<ManagerApps> getAllManagerApps(String
	// projectPath,String projectModuleName){
	// return getProvider().getManagerApps(projectPath,projectModuleName);
	// }

	/**
	 * 获取项目中的ManagerApps
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param managerId
	 * @return
	 */
	// public static ManagerApps getManagerApps(String projectPath,String
	// projectModuleName,String managerId){
	// return
	// getProvider().getManagerApps(projectPath,projectModuleName,managerId);
	// }

	/**
	 * 删除ManagerApps
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param id
	 */
	public static void deleteManagerApps(String projectPath, String projectModuleName, String id) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deleteManagerApps(projectPath, projectModuleName, id);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 保存ManagerApps
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param managerApps
	 */
	// public static void saveManagerAppsToXml(String projectPath,String
	// projectModuleName,ManagerApps managerApps){
	// // getProvider().saveManagerApps(projectPath, projectModuleName,
	// managerApps);
	// // refreshCurrentPorject();
	// }

	/**
	 * 取portalModule
	 * 
	 * @param projectPath
	 * @return
	 */
	public static PortalDeployDefinition getPortalDefinition(String projectPath) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		PortalDeployDefinition definition = getProvider().getPortalModule(projectPath);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return definition;
	}

	/**
	 * 取portalModule
	 * 
	 * @param file
	 * @return
	 */
	public static PortalModule getPortalModule(File file) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		PortalModule module = getProvider().getPortalModule(file);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return module;
	}

	/**
	 * 获取项目中的 PortletApplicationDefinition
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static PortletApplicationDefinition getPortletApp(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		PortletApplicationDefinition definition = getProvider().getPortletApp(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return definition;
	}

	/**
	 * 通过字符串得到 PortletApplicationDefinition
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param portletAppText
	 * @return
	 */
	public static PortletApplicationDefinition getPortletApp(String projectPath, String projectModuleName, String portletAppText) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		PortletApplicationDefinition definition = getProvider().getPortletApp(projectPath, projectModuleName, portletAppText);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return definition;
	}

	/**
	 * 保存PortletApplicationDefinition
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param portletApp
	 */
	public static void savePortletAppToXml(String projectPath, String projectModuleName, PortletApplicationDefinition portletApp) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName);
		String fileName = "portlet.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().savePortletAppToXml(projectPath, projectModuleName, portletApp);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 保存portlet
	 * 
	 */
	public static void savePortletToXml(String projectPath, String projectModuleName, PortletDefinition portlet, String categoryId) {
		PortletApplicationDefinition portletApp = PortalConnector.getPortletApp(projectPath, projectModuleName);
		if (portletApp == null) {
			portletApp = new PortletApplicationDefinition();
		}
		PortletDefinition pd = null;
		if (portletApp.getPortlet(portlet.getPortletName()) == null) {
			portletApp.getPortlets().add(portlet);
		} else {
			pd = portletApp.getPortlet(portlet.getPortletName());
			portletApp.getPortlets().remove(pd);
			portletApp.getPortlets().add(portlet);
		}
		savePortletAppToXml(projectPath, projectModuleName, portletApp);

		// 写portlet分类
		if (categoryId != null) {
			Display display = PortalConnector.getDisplay(projectPath, projectModuleName);
			for (PortletDisplayCategory pdc : display.getCategory()) {
				if (pdc.getId().equals(categoryId)) {
					Boolean isEdit = false;
					for (PortletDisplay portletDisplay : pdc.getPortletDisplayList()) {
						if (portletDisplay.getId().equals(portlet.getPortletName())) {
							portletDisplay.setTitle(portlet.getDisplayName().get(0).getDisplayName());
							isEdit = true;
							break;
						}
					}
					if (!isEdit) {
						PortletDisplay portletDisplay = new PortletDisplay();
						portletDisplay.setId(portlet.getPortletName());
						portletDisplay.setDynamic(true);
						portletDisplay.setTitle(portlet.getDisplayName().size() > 0 ? portlet.getDisplayName().get(0).getDisplayName() : "");
						pdc.addPortletDisplayList(portletDisplay);
					}
					break;
				}
			}
			saveDisplayToXml(projectPath, projectModuleName, display);
		}

		refreshCurrentPorject();
	}

	/**
	 * 获取项目中的 PortalModule 配置
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static PortalModule getPortal(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		PortalModule portalModule = getProvider().getPortal(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return portalModule;
	}

	/**
	 * 保存项目PortalModule
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param portalModule
	 */
	public static void savePortalToXml(String projectPath, String projectModuleName, PortalModule portalModule) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName);
		String fileName = "portal.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().savePortalToXml(projectPath, projectModuleName, portalModule);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 获取项目插件
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	// public static PtPlugin getPtPlugin(String projectPath,String
	// projectModuleName){
	// PtPlugin p = getProvider().getPtPlugin(projectPath, projectModuleName);
	// return p;
	// //return getProvider().getPtPlugin(projectPath, projectModuleName);
	// }
	/**
	 * 获取所有拓展点
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	// public static PtExtensionPoint[] getAllExtensionPoint(){
	// PtExtensionPoint[] ep = null;
	// ep = getProvider().getAllPtExtPoints();
	// return ep;
	// //return getProvider().getPtPlugin(projectPath, projectModuleName);
	// }
	// public static PtExtension[] getExtensions(String point){
	// PtExtension[] exts = null;
	// exts = getProvider().getPtExtensionsByPoint(point);
	// return exts;
	// }

	/**
	 * 保存项目插件
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param ptPlugin
	 */
	// public static void savePtPluginToXml(String projectPath,String
	// projectModuleName, PtPlugin ptPlugin){
	// String filePath = getPortalSpecPath(projectPath, projectModuleName);
	// String fileName = "plugin.xml";
	// checkFile(filePath, fileName);
	// getProvider().savePtPluginToXml(projectPath,projectModuleName, ptPlugin);
	// refreshCurrentPorject();
	// }

	/**
	 * 获取项目portlet分类,包括依赖的项目
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static List<Display> getAllDisplays(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List<Display> displays = getProvider().getAllDisplays(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return displays;
	}

	/**
	 * 获取项目portlet分类
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static Display getDisplay(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Display d = getProvider().getDisplay(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return d;
	}

	/**
	 * 保存项目portlet分类
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param display
	 */
	public static void saveDisplayToXml(String projectPath, String projectModuleName, Display display) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName);
		String fileName = "display.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().saveDisplayToXml(projectPath, projectModuleName, display);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 获取项目Page对象列表
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static Page[] getAllPages(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Page[] pages = getProvider().getAllPages(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pages;
	}

	/**
	 * 获取项目Page对象
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param pageName
	 * @return
	 */
	public static Page getPage(String projectPath, String projectModuleName, String pageName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Page page = getProvider().getPage(projectPath, projectModuleName, pageName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return page;
	}

	/**
	 * Page对象转换成XML字符串
	 * 
	 * @param page
	 * @return
	 */
	public static String pageToString(Page page) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pageStr = getProvider().pageToString(page);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pageStr;
	}

	/**
	 * XML字符串转换成Page对象
	 * 
	 * @param xml
	 * @return
	 */
	public static Page stringToPage(String xml) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();		
		Page page = getProvider().stringToPage(xml);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return page;
	}

	/**
	 * 保存Page对象
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param fileName
	 * @param xml
	 */
	public static void savePageToXml(String projectPath, String projectModuleName, String fileName, String xml) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName) + "pml/";
		if (fileName.endsWith(".pml")) {
			checkFile(filePath, fileName);
		} else {
			checkFile(filePath, fileName + ".pml");
		}
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().savePageToXml(projectPath, projectModuleName, fileName, xml);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 保存Page对象
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param page
	 */
	public static void savePageToXml(String projectPath, String projectModuleName, Page page) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName) + "pml/";
		String fileName = page.getPagename() + ".pml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().savePageToXml(projectPath, projectModuleName, page);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 删除项目中的Page
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param pageName
	 */
	public static void deletePage(String projectPath, String projectModuleName, String pageName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().deletePage(projectPath, projectModuleName, pageName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 获取项目LookAndFeel对象
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static LookAndFeel getLookAndFeel(String projectPath, String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		LookAndFeel lookFeel = getProvider().getLookAndFeel(projectPath, projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return lookFeel;
	}

	/**
	 * 保存LookAndFeel
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @return
	 */
	public static void saveLookAndFeelToXml(String projectPath, String projectModuleName, LookAndFeel lookAndFeel) {
		String filePath = projectPath + "/web/WEB-INF/conf/";
		String fileName = "look-and-feel.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().saveLookAndFeelToXml(projectPath, projectModuleName, lookAndFeel);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 更新LookAndFeel
	 * 
	 * @param projectPath
	 * @param theme
	 * @param actionType
	 * @return
	 */
	public static void updateLookAndFeelToXml(String projectPath, Theme theme, int actionType) {
		String filePath = projectPath + "/web/WEB-INF/conf/";
		String fileName = "look-and-feel.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().updateLookAndFeelToXml(projectPath, theme, actionType);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 获取样式列表
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @return
	 */
	public static List<Skin> getSkins(String projectPath, String projectModuleName, String type) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		List<Skin> skins = getProvider().getSkins(projectPath, projectModuleName, type);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return skins;
	}

	/**
	 * 获取样式列表,包括依赖模块
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @return
	 */
	public static List<Skin> getAllSkins(String projectPath, String projectModuleName, String type) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		List<Skin> skins = getProvider().getAllSkins(projectPath, projectModuleName, type);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return skins;
	}

	/**
	 * 获取样式列表
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @param themeId
	 * @return
	 */
	public static SkinDescription getSkinDescription(String projectPath, String projectModuleName, String type, String themeId) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		SkinDescription description = getProvider().getSkinDescription(projectPath, projectModuleName, type, themeId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return description;
	}

	/**
	 * 保存样式描述文件
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @param themeId
	 * @param skinDescription
	 */
	public static void saveSkinDescription(String projectPath, String projectModuleName, String type, String themeId, SkinDescription skinDescription) {
		String filePath = getPortalSpecPath(projectPath, projectModuleName) + "/ftl/portaldefine/skin/" + themeId + "/" + type + "/";
		String fileName = "description.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().saveSkinDescription(projectPath, projectModuleName, type, themeId, skinDescription);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 更新样式描述文件
	 * 
	 * @param projectPath
	 * @param skin
	 * @param actionType
	 */
	public static void updateSkinDescription(String projectPath, Skin skin, int actionType) {
		String filePath = projectPath + "/web/tpl/" + skin.getThemeid() + "/" + skin.getStype() + "/";
		String fileName = "description.xml";
		checkFile(filePath, fileName);
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().updateSkinDescription(projectPath, skin, actionType);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 创建样式文件
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @param themeId
	 * @param fileName
	 * @param fileText
	 */
	public static void createSkinFile(String projectPath, String projectModuleName, String type, String themeId, String fileName, String fileText) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().createSkinFile(projectPath, projectModuleName, type, themeId, fileName, fileText);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 创建样式文件
	 * 
	 * @param projectPath
	 * @param skin
	 */
	public static void createSkinFile(String projectPath, Skin skin) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().createSkinFile(projectPath, skin);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 删除样式文件
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param type
	 * @param themeId
	 * @param fileName
	 */
	public static void deleteSkinFile(String projectPath, String projectModuleName, String type, String themeId, String fileName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().deleteSkinFile(projectPath, projectModuleName, type, themeId, fileName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 删除样式文件
	 * 
	 * @param projectPath
	 * @param skin
	 */
	public static void deleteSkinFile(String projectPath, Skin skin) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().deleteSkinFile(projectPath, skin);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 创建样式文件夹
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param themeId
	 */
	public static void createThemeFolder(String projectPath, String projectModuleName, Theme theme) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().createThemeFolder(projectPath, projectModuleName, theme);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 删除样式文件夹
	 * 
	 * @param projectPath
	 * @param projectModuleName
	 * @param themeId
	 */
	public static void deleteThemeFolder(String projectPath, String projectModuleName, String themeId) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		getProvider().deleteThemeFolder(projectPath, projectModuleName, themeId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		refreshCurrentPorject();
	}

	/**
	 * 获取功能页面Map
	 * 
	 * @param projPaths
	 * @return
	 */
	public static Map<String, String>[] getPageNames(String[] projPaths) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();	
		Map<String, String>[] pageNames = getProvider().getPageNames(projPaths);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pageNames;
	}

	/**
	 * 刷新当前porject
	 * 
	 * @throws CoreException
	 */
	private static void refreshCurrentPorject() {
		IProject project = LFWPersTool.getCurrentProject();
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			PortalPlugin.getDefault().logError(e.getMessage(), e);
		}
	}
}
