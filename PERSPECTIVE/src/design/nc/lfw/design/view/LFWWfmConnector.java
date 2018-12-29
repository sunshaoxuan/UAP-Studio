package nc.lfw.design.view;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateVO;
import nc.uap.lfw.common.ClassTool;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.core.vo.CpDeviceVO;
import nc.uap.lfw.design.itf.ILfwDesignDataProvider;
import nc.uap.lfw.design.view.ClassPathProvider;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.wfm.itf.ILfwWfmDesignDataProvider;
import nc.uap.wfm.vo.WfmFlwCatVO;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;

import org.eclipse.core.resources.IProject;

public class LFWWfmConnector {
	
	public static WfmFlwCatVO[] getWfmFlowCateQry(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmFlwCatVO[] flwcats = getWfmDataProvider().getFlowCateQry();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return flwcats;
	}
	public static WfmFlwCatVO[] getWfmFlowCateByModule(String module, String component){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmFlwCatVO[] flwcats = getWfmDataProvider().getWfmFlowCateByModule(module,component);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return flwcats;
	}
	public static void insertFlwCate(WfmFlwCatVO flwCate){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().insertFlwCate(flwCate);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delFlwCate(String flwCatePk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delFlwCate(flwCatePk);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void editFlwCate(WfmFlwCatVO flwCate){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().editFlwCate(flwCate);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static WfmFlwTypeVO[] getFlwTypeQry(String pk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmFlwTypeVO[] flwTypes = getWfmDataProvider().getFlwTypeQry(pk);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return flwTypes;
		
	}
	public static void saveFlwType(WfmFlwTypeVO flwType){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().saveFlwType(flwType);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void editFlwType(WfmFlwTypeVO flwType){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().editFlwType(flwType);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delFlwType(String flwTypePk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delFlwType(flwTypePk);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static String insertProdef(WfmProdefVO proDef){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().insertProdef(proDef);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static void delProdef(String defPk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delProdef(defPk);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static WfmProdefVO[] getProDef(String flwTypePk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmProdefVO[] proDefs = getWfmDataProvider().getProDef(flwTypePk);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return proDefs;
	}
	public static WfmFlwTypeVO getFlwType(String flwTypePk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmFlwTypeVO flwType = getWfmDataProvider().getFlwType(flwTypePk);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return flwType;
	}
	public static WfmFlwTypeVO getFlwTypeByCode(String code){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		WfmFlwTypeVO flwType = getWfmDataProvider().getFlwTypeByCode(code);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return flwType;
	}
	
	public static CpAppsCategoryVO[] getAppsCategory(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsCategoryVO[] appsCates = getWfmDataProvider().getAppsCategory();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsCates;
	}
	public static CpAppsCategoryVO[] getAppsCategoryByDevModule(String devmodulename){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsCategoryVO[] appsCates = getWfmDataProvider().getAppsCategoryByDevModule(devmodulename);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsCates;
	}
	public static CpAppsCategoryVO getAppsCategoryById(String appsCateId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsCategoryVO appscate = getWfmDataProvider().getAppsCategoryById(appsCateId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appscate;
	}
	public static CpAppsNodeVO[] getAppsNodeByCategory(String appsCategory){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO[] appsNodes = getWfmDataProvider().getAppsNodeByCategory(appsCategory);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNodes;
	}
	public static CpAppsNodeVO[] getAllAppsNodes(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO[] appsNodes = getWfmDataProvider().getAllAppsNodes();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNodes;
	}
	public static CpAppsNodeVO[] getAppsNodeVOsByCondition(String condition){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO[] appsNodes = getWfmDataProvider().getAppsNodeVOsByCondition(condition);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNodes;
	}
	public static CpAppsNodeVO[] getNodeByModuleandComponent(String devmodule,String component){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO[] appsNodes = getWfmDataProvider().getNodeByModuleandComponent(devmodule,component);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNodes;
	}
	public static CpAppsNodeVO getAppsNodeById(String appsNodeId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO appsNode = getWfmDataProvider().getAppsNodeById(appsNodeId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNode;
	}
	public static CpAppsNodeVO getAppsNodeByPk(String pk_funnode){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpAppsNodeVO appsNode = getWfmDataProvider().getAppsNodeByPk(pk_funnode);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return appsNode;
	}
	public static String saveAppsCategory(CpAppsCategoryVO appsCateVO){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().saveAppsCategory(appsCateVO);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static String saveAppsNode(CpAppsNodeVO appsNodeVo){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().saveAppsNode(appsNodeVo);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static void updateAppsNode(CpAppsNodeVO appsNodeVo){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().updateAppsNode(appsNodeVo);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delAppsNode(String pk_funnode){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delAppsNode(pk_funnode);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static CpMenuCategoryVO[] getMenuCategory(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpMenuCategoryVO[] menuCates = getWfmDataProvider().getMenuCategory();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return menuCates;
	}
	public static CpMenuItemVO getMenuItemById(String menuId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpMenuItemVO menuItem = getWfmDataProvider().getMenuItemById(menuId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return menuItem;
	}
	public static CpMenuItemVO[] getMenuItemsByCondition(String condition){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpMenuItemVO[] menuItems = getWfmDataProvider().getMenuItemsByCondition(condition);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return menuItems;
	}
	public static CpMenuCategoryVO getMenuCategoryById(String menuCateId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpMenuCategoryVO menuCate = getWfmDataProvider().getMenuCategoryById(menuCateId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return menuCate;
	}
	public static String saveMenuCategory(CpMenuCategoryVO menuCate){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().saveMenuCategory(menuCate);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static String saveMenuItem(CpMenuItemVO menuItem){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().saveMenuItem(menuItem);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static void updateMenuItem(CpMenuItemVO menuItem){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().updateMenuItem(menuItem);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delMenuItem(String pk_menuitem){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delMenuItem(pk_menuitem);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static CpModuleVO getModuleById(String moduleId){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpModuleVO module = getWfmDataProvider().getModuleById(moduleId);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return module;
	}
	public static CpModuleVO[] getModules(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpModuleVO[] modules = getWfmDataProvider().getModules();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return modules;
	}
	public static String saveModule(CpModuleVO module){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().saveModule(module);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static String getGroupPk(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().getGroupPk();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static void savePrintTemplate(CpPrintTemplateVO template){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().initPrintTemplate(template);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void saveQryTemplate(CpQueryTemplateVO template){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().initQueryTemplate(template);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delPrintTemplate(String pk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delPrintTemplate(pk);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static void delQryTemplate(String pk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().delQueryTemplate(pk);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	public static String getQueryTemplateByNodeCode(String nodeCode){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().getQueryTemplateByNodeCode(nodeCode);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static String getPrintTemplateByNodeCode(String nodeCode){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().getPrintTemplateByNodeCode(nodeCode);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static CpQueryTemplateVO[] getQueryTplByCondition(String condition){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpQueryTemplateVO[] queryTemp = getWfmDataProvider().getQueryTplByCondition(condition);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return queryTemp;
	}
	public static CpPrintTemplateVO[] getPrintTplByCondition(String condition){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpPrintTemplateVO[] printTemps = getWfmDataProvider().getPrintTplByCondition(condition);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return printTemps;
	}
	public static LfwRefInfoVO[] getRefInfoByCondition(String condition){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		LfwRefInfoVO[] refInfos = getWfmDataProvider().getRefInfoByCondition(condition);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return refInfos;
	}
	public static String addRefInfo(LfwRefInfoVO refInfo){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().addRefInfo(refInfo);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static String deleteRefInfoByPk(String pk){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String delPk = getWfmDataProvider().deleteRefInfoByPk(pk);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return delPk;
	}
	public static CpDeviceVO[] getAllDevices(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpDeviceVO[] devices = getWfmDataProvider().getAllDevices();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return devices;
	}
	public static String generatePK(){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().generatePK();
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	public static void genQryTemplate(String nodecode, String mataClass, String modelCode, String modelName, String businessEntityFullName){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().genQryTemplate(nodecode, mataClass, modelCode, modelName, businessEntityFullName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static void genPrintTemplate(String nodecode, String mataClass, String modelCode, String modelName, String businessEntityFullName){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().genPrintTemplate(nodecode, mataClass, modelCode, modelName, businessEntityFullName);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static String getOnlinePrintUrl(String pktemplate, String nodeCode, String filepk, String realPath){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().getOnlinePrintUrl(pktemplate, nodeCode, filepk, realPath);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	
	public static String insertLfwfile(LfwFileVO vo){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		String pk = getWfmDataProvider().insertLfwfile(vo);
		Thread.currentThread().setContextClassLoader(currentLoader);
		return pk;
	}
	
	public static void updateNodeMenuItems(String pk_appsnode,LfwWindow[] windows){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().updateNodeMenuItems(pk_appsnode, windows);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	
	public static void executeSql(String sql){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		getWfmDataProvider().executeSql(sql);
		Thread.currentThread().setContextClassLoader(currentLoader);
	}
	

	/**
	 * 连接UAPWEB服务
	 * @return
	 */
	private static ILfwWfmDesignDataProvider getWfmDataProvider() {
		try {
			Thread.currentThread().setContextClassLoader(ClassPathProvider.getClassLoader());
			Class<?> c = Class.forName("nc.uap.wfm.impl.LfwWfmDesignDataProviderImpl", true, ClassPathProvider.getClassLoader());
			return (ILfwWfmDesignDataProvider) c.newInstance();
		} 
		catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		return null;
	}
}
