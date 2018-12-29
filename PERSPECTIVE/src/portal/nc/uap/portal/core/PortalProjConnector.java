package nc.uap.portal.core;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.portal.service.itf.IPortalDesignDataProvider;

/**
 * 与service交互
 * 
 * @author dingrf
 *
 */
public class PortalProjConnector {

	private static IPortalDesignDataProvider getProvider() {
		
		try {
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			Class<?> c = Class.forName("nc.uap.portal.service.impl.PortalDesignDataProviderImpl", true, ClassPathProvider.getClassLoader());			
			return (IPortalDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		return null;
	}

	/**
	 * 部署portal.xml
	 * 
	 * @param projectModuleName
	 */
	public static void deployPortal(String projectModuleName){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployPortal(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	/**
	 * 部署display
	 * 
	 * @param projectModuleName
	 */
	public static void deployDisplay(String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployDisplay(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 部署主题
	 * 
	 */
//	public static void deployLookAndFeel() {
//		getProvider().deployLookAndFeel();
//	}

	/**
	 * 部署功能管理 
	 * 
	 * @param projectModuleName
	 */
	public static void deployManagerApps(String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployManagerApps(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 部署page页
	 * 
	 * @param projectModuleName
	 * @param pageName
	 */
	public static void deployPage(String projectModuleName, String pageName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployPage(projectModuleName,pageName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 部署portletapp
	 * 
	 * @param projectModuleName
	 */
	public static void deployPortletApp(String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployPortletApp(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 部署插件
	 * 
	 * @param projectModuleName
	 */
	public static void deployPtPlugin(String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deployPtPlugin(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}

	/**
	 * 部署皮肤
	 * 
	 * @param projectModuleName
	 */
	public static void deploySkin(String projectModuleName) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().deploySkin(projectModuleName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	
}
