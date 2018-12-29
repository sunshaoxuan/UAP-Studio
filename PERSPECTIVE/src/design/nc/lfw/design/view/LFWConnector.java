package nc.lfw.design.view;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.itf.ILfwDesignDataProvider;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.design.view.LFWAMCConnector;

public class LFWConnector {

//	public static Map<String, IRefNode> getRefNode(String projectPath){
//		return getProvider().getRefNode(projectPath);
//	}
	
	//String ctx =ResourcesPlugin.getWorkspace().getRoot().getc.getWorkspace().getRoot().getProjects() 
//	public static Map<String,Dataset> getDataset(String projectPath){
//		return getProvider().getDataset(projectPath);
//	}
	
//	public static Map<String, Map<String, LfwView>>  getAllPoolWidgets(String ctx){
//		long start = System.currentTimeMillis();
//		Map<String, Map<String, LfwView>> result = getProvider().getAllPoolWidgets(ctx);
//		long end = System.currentTimeMillis();
//		MainPlugin.getDefault().logInfo("调用getAllPoolWidgets,耗时:" + (end - start) + "毫秒");
//		return result;
//	}
	
	
	public static LfwWindow getPageMeta(String pageId){
		return LFWAMCConnector.getWindowById(pageId);
	}
	
	
//	public static void removeWidgetFromPool(String rootPath, LfwView widget){
//		getProvider().removeWidgetFromPool(rootPath, widget);
//		LFWPersTool.refreshCurrentPorject();
//	}
	
	public static void savePagemetaToXml(String filePath, String fileName, String projectPath, LfwWindow meta) {
		//checkout文件
		String path = filePath + "/" + fileName;
		LFWPersTool.checkOutFile(path);
		LFWAMCConnector.updateWindowToXml(filePath, fileName, projectPath, meta);
		LFWPersTool.refreshCurrentPorject();
	}
	
	public static String[] getAllNcRefNode() {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String[] refnodes = getProvider().getAllNcRefNode();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return refnodes;
		
	}
	
	public static List getAllModulse() {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List modules = getProvider().getAllModulse();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return modules;
	}
	public static List getEntity(String componentId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List entity = getProvider().getEntityByComponetId(componentId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return entity;
	}
	
//	public static List getAllComponentByModuleId(String moduleid) {
//		return getProvider().getAllComponentByModuleId(moduleid);
//	}
	
	public static List getAllComponents(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List components = getProvider().getAllComponents();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return components;
	}
	
	public static List getAllClasses(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List classes = getProvider().getAllClasses();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return classes;
	}
	
//	public static List getAllClassByComponentId(String componentID) {
//		return getProvider().getAllClassByComponentId(componentID);
//	}
	
	public static void clearAllMDCache(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().clearAllMDCache();
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static void clearMDCacheById(String componentID){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getProvider().clearMDCacheById(componentID);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static MdDataset getMdDataset(MdDataset ds) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		MdDataset mddataset = getProvider().getMdDataset(ds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return mddataset;
	}
	
//	public static String getAggVO(String fullClassName){
//		return getProvider().getAggVO(fullClassName);
//	}
	
	public static List  getNCRefMdDataset(MdDataset mdds) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List refmddatasets = getProvider().getNCRefMdDataset(mdds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return refmddatasets;
	}
	
	public static List  getNCFieldRelations(MdDataset mdds) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List fieldRelations = getProvider().getNCFieldRelations(mdds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return fieldRelations;
	}
	
	public static List  getAllNCRefNode(MdDataset mdds) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List refNodes = getProvider().getAllNCRefNode(mdds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return refNodes;
	}
	
	public static List getAllNcComboData(MdDataset mdds) {
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		List comboData = getProvider().getAllNcComboData(mdds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return comboData;
	}

//	public static String generatorClass(String fullPath, String extendClass,Map<String, Object> param){
//		return getProvider().generatorClass(fullPath, extendClass, param);
//	}
	
	public static String generateRefNodeClass(String refType, String modelClass, String tableName, String refPk, String refCode, String refName, String visibleFields, String childfield, String childfield2){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String refnode= getProvider().generateRefNodeClass(refType, modelClass, tableName, refPk, refCode, refName, visibleFields, childfield, childfield2);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return refnode;
	}
	public static String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String vo = getProvider().generatorVO(fullPath, tableName, primaryKey, ds);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return vo;
	}
	
	private static ILfwDesignDataProvider getProvider() {
//		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			Class<?> c = Class.forName("nc.uap.lfw.design.impl.LfwDesignDataProvider", true, ClassPathProvider.getClassLoader());
			return (ILfwDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MessageDialog.openWarning(null, "", "Web Service is not loaded currectly,please restart studio");
			MainPlugin.getDefault().logError(e);
		}
		return null;
	}
	
}
