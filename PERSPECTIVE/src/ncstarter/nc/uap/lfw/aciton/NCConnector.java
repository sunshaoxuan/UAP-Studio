package nc.uap.lfw.aciton;

import java.util.List;
import java.util.Map;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.design.itf.ILfwDesignDataProvider;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.wfm.vo.WfmFlwTypeVO;

public class NCConnector {
	
//	public static UIMeta getUIMeta(String folderPath, LfwWindow pm,  String widgetID) {
//		return getProvider().getUIMeta(folderPath, pm, widgetID);
//	}
	
	public static void saveUIMeta(UIMeta meta, String pmPath, String folderPath){
		String filePath = folderPath + "/uimeta.um";
		LFWPersTool.checkOutFile(filePath);
		LFWAMCConnector.createUIMeta(folderPath, "uimeta.um", meta);
		LFWPersTool.refreshCurrentPorject();
	}
	
	public static  Map<String, String>[] getPageNames(String[] projPaths){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String, String>[] pageNames = getProvider().getPageNames(projPaths);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pageNames;
	}
	
//	public static BCPManifest getBCPManifest(String filePath){
//		return getProvider().getMenifest(filePath);
//	}
	
	public static LfwView getMdDsFromComponent(LfwView widget, String componetId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LfwView mdView = getProvider().getMdDsFromComponent(widget, componetId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return mdView;
	}
	public static Map<String,String> getIBDObjMap(String componetId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map<String,String> ibdobjMap = getProvider().getIBDObjMap(componetId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return ibdobjMap;
	}
	
//	public static String getAggVOByComponent(String componetId){
//		try {
//			return getProvider().getAggVOByComponent(componetId);
//		} catch (LfwBusinessException e) {
//			MainPlugin.getDefault().logError(e);
//			return null;
//		}
//	}
	
	public static List getAllClassByComId(String componentId){
		try {
			ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
			List classes = getProvider().getAllClassByComId(componentId);
			Thread.currentThread().setContextClassLoader(currentLoader);
			return classes;		
		} catch (LfwBusinessException e) {
			MainPlugin.getDefault().logError(e);
			return null;
		}
	}
	
	public static String getQtyTempConditionSqlByEntity(String entityFullName){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String conditionSql = getProvider().getQtyTempConditionSqlByEntity(entityFullName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return conditionSql;
	}
	
	public static Map getBusinessEntity(String entityFullName){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		Map businessEntity = getProvider().getBusinessEntity(entityFullName);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return businessEntity;
	}
	
	public static void saveWidgettoXml(String filePath, String fileName, String projectPath, LfwView widget, String rootPath) {
		String path = filePath + "/" + fileName;
		LFWPersTool.checkOutFile(path);
		LFWAMCConnector.updateViewToXml(filePath, fileName, projectPath, widget);
		LFWPersTool.refreshCurrentPorject();
	}
	
//	/*
//	 * 查询流程大类
//	 */
//	public static WfmFlwCatVO[] getWfmFLowQry(){
//		return LFWWfmConnector.getWfmFlowCateQry();
//	}
	/*
	 * 查询流程大类下的小类
	 */
	public static WfmFlwTypeVO[] getFlwType(String cataPk){
		return LFWWfmConnector.getFlwTypeQry(cataPk);
	}
	
	/*
	 * 保存业务类型
	 */
	public static void saveFlwType(WfmFlwTypeVO flwType){
		LFWWfmConnector.saveFlwType(flwType);
	}
	
	private static ILfwDesignDataProvider getProvider() {
		try {
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			Class<?> c = Class.forName("nc.uap.lfw.design.impl.LfwDesignDataProvider", true, ClassPathProvider.getClassLoader());			
			return (ILfwDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		return null;
//		return new LfwDesignDataProvider();
//		IProject[] projects = LFWPersTool.getLFwProjects();
//		for (int i = 0; i < projects.length; i++) {
//			try {
//			} 
//			catch (Exception e) {
//				MainPlugin.getDefault().logError(e);
//			}
//		}
//		return null;
	}
	
}