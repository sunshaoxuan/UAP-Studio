package nc.uap.lfw.design.itf;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwView;

public interface ILfwDesignDataProvider {
	
	
	public List getAllComponents();
	
	public List getAllClasses();
	
	public List getAllClassByComId(String componentId) throws LfwBusinessException; 
	
	
	public String[] getAllNcRefNode();
	
	public List getAllModulse();
	
//	public List getAllComponentByModuleId(String moduleid);
	
//	public List getAllClassByComponentId(String componentID);
	
	public void clearAllMDCache();
	
	public void clearMDCacheById(String componentID);
	
	public MdDataset getMdDataset(MdDataset ds);
	
//	public String getAggVO(String fullClassName);
	
	public List  getNCRefMdDataset(MdDataset mdds);
	
	public List  getNCFieldRelations(MdDataset mdds);
	
	public List  getAllNCRefNode(MdDataset mdds);
	
	public List  getAllNcComboData(MdDataset mdds);
	
	/**
	 * 生成参照类
	 * @param refType
	 * @param modelClass
	 * @param refPk
	 * @param refCode
	 * @param refName
	 * @param visibleFields
	 * @param childfield
	 * @param childfield2
	 * @return
	 */
	public String generateRefNodeClass(String refType, String modelClass, String tableName, String refPk, String refCode, String refName, String visibleFields, String childfield, String childfield2);
	
//	public String generatorClass(String fullPath, String extendClass, Map<String, Object> param);
	
	public String generatorVO(String fullPath, String tableName, String primaryKey, Dataset ds);
	
	
	public Map<String, String>[] getPageNames(String[] projPaths);
	
	public LfwView getMdDsFromComponent(LfwView widget, String componetId);
	
	public List getEntityByComponetId(String componetId);
	
	public Map<String,String> getIBDObjMap(String componetId);
	
	public String getQtyTempConditionSqlByEntity(String entityFullName);
	
	public Map getBusinessEntity(String entityFullName);
	
}
