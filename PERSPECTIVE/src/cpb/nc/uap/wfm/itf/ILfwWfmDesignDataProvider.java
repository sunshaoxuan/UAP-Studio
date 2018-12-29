package nc.uap.wfm.itf;

import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.ctrl.tpl.print.base.CpPrintTemplateVO;
import nc.uap.ctrl.tpl.qry.base.CpQueryTemplateVO;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.core.vo.CpDeviceVO;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.wfm.vo.WfmFlwCatVO;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;


public interface ILfwWfmDesignDataProvider {

	public WfmFlwCatVO[] getFlowCateQry();
	
	public WfmFlwCatVO[] getWfmFlowCateByModule(String module, String component);
	
	public void insertFlwCate(WfmFlwCatVO flwCate);
	
	public void delFlwCate(String flwCatePk);
	
	public void editFlwCate(WfmFlwCatVO flwCate);
	
	public WfmFlwTypeVO[] getFlwTypeQry(String pk) ;
	
	public WfmFlwTypeVO getFlwTypeByCode(String code);
	
	public void saveFlwType(WfmFlwTypeVO flwType);
	
	public void editFlwType(WfmFlwTypeVO flwType);
	
	public void delFlwType(String flwTypePk);
	
	public String insertProdef(WfmProdefVO proDef);
	
	public void delProdef(String defPk);
	
	public WfmProdefVO[] getProDef(String flwTypePk);
	
	public WfmFlwTypeVO getFlwType(String flwTypePk);
	
	public CpAppsCategoryVO[] getAppsCategory();
	
	public CpAppsCategoryVO[] getAppsCategoryByDevModule(String devmodulename);
	
	public CpAppsCategoryVO getAppsCategoryById(String appsCateId);
	
	public CpAppsNodeVO[] getAppsNodeByCategory(String appsCategory);
	
	public CpAppsNodeVO[] getAllAppsNodes();
	
	public CpAppsNodeVO[] getAppsNodeVOsByCondition(String condition);
	
	public CpAppsNodeVO[] getNodeByModuleandComponent(String devmodule,String component);
	
	public CpAppsNodeVO getAppsNodeById(String appsNodeId);
	
	public CpAppsNodeVO getAppsNodeByPk(String pk_funnode);
	
	public CpMenuCategoryVO[] getMenuCategory();
	
	public CpMenuItemVO getMenuItemById(String menuId);
	
	public CpMenuItemVO[] getMenuItemsByCondition(String condition);
	
	public CpMenuCategoryVO getMenuCategoryById(String menuCateId);
	
	public String saveAppsCategory(CpAppsCategoryVO appsCateVO);
	
	public String saveAppsNode(CpAppsNodeVO appsNodeVo);
	
	public void updateAppsNode(CpAppsNodeVO appsNodeVo);
	
	public void delAppsNode(String pk_funnode);	
	
	public String saveMenuCategory(CpMenuCategoryVO menuCate);
	
	public void delMenuCategory(CpMenuCategoryVO menuCate);
	
	public String saveMenuItem(CpMenuItemVO menuItem);
	
	public void updateMenuItem(CpMenuItemVO menuItem);
	
	public void delMenuItem(String pk_menuitem);
	
	public CpModuleVO[] getModules();
	
	public CpModuleVO getModuleById(String moduleId);
	
	public String saveModule(CpModuleVO module);
	
	public String getGroupPk();
	
	public void initQueryTemplate(CpQueryTemplateVO queryVO);
	
	public void initPrintTemplate(CpPrintTemplateVO printVO);
	
	public void delQueryTemplate(String pk);
	
	public void delPrintTemplate(String pk);
	
	public String getQueryTemplateByNodeCode(String nodeCode);
	
	public String getPrintTemplateByNodeCode(String nodeCode);
	
	public CpQueryTemplateVO[] getQueryTplByCondition(String condition);
	
	public CpPrintTemplateVO[] getPrintTplByCondition(String condition);
	
	public LfwRefInfoVO[] getRefInfoByCondition(String condition);
	
	public String addRefInfo(LfwRefInfoVO refInfo);
	
	public String deleteRefInfoByPk(String pk);
	
	public CpDeviceVO[] getAllDevices();
	
	public void genQryTemplate(String nodecode, String mataClass, String modelCode, String modelName, String businessEntityFullName);
	
	public void genPrintTemplate(String nodecode, String mataClass, String modelCode, String modelName, String businessEntityFullName);
	
	public String getOnlinePrintUrl(String pktemplate, String nodeCode, String filepk, String realPath);
	
	public String insertLfwfile(LfwFileVO vo);

	public String generatePK();
	
	public void updateNodeMenuItems(String pk_appsnode,LfwWindow[] windows);
	
	public void executeSql(String sql);
}
