package nc.uap.lfw.template.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.wfm.vo.WfmFlwTypeVO;
import nc.uap.wfm.vo.WfmProdefVO;
import nc.vo.pub.lang.UFBoolean;

public class LfwTemplateTool {

	/***
	 * 生成查询模板文件
	 * 
	 * @param filePath
	 * @param modelCode
	 * @param modelName
	 * @param mataClass
	 * @param businessEntityFullName
	 */
	public static void GenQryTemplate(String appid, String modelCode, String modelName, String mataClass, String businessEntityFullName){
		//插入查询配置主表sql
		String qryTempSql = "INSERT INTO cp_query_template" +
				"(metaclass,modelcode,modelname,nodecode,pk_query_template,parentid,pkcorp) " +
				"VALUES('%s','%s','%s','%s','%s','~','~')";
		String nodecode = "$tempcode_" + appid;
		//删除原有以该app为nodecode的数据
		LFWWfmConnector.executeSql("DELETE FROM cp_query_condition WHERE pk_query_template =" +
				" (select pk_query_template from  cp_query_template where nodecode =  '"+nodecode+"')");
		LFWWfmConnector.executeSql("DELETE FROM cp_query_template WHERE nodecode = '"+nodecode+"'");
		
		String pkqty = LFWWfmConnector.generatePK();
		//生成查询模板配置文件 
 		String sql = String.format(qryTempSql, mataClass, modelCode, modelName, nodecode, pkqty);
 		LFWWfmConnector.executeSql(sql);
// 		try {
//			FileUtilities.saveFile(filePath + "/" + "0001.qt", sql.toString().getBytes("GBK"));
//		} catch (UnsupportedEncodingException e) {
//			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
//		} catch (IOException e) {
//			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
//		}
 		
 		//生成查询模板配置条件文件 
 		String sqlCon = NCConnector.getQtyTempConditionSqlByEntity(businessEntityFullName);
 		sqlCon = sqlCon.replace("${pk_qty}", pkqty);
 		while (sqlCon.indexOf("$pk_query_con") > 0){
 			sqlCon = sqlCon.replaceFirst("\\$pk_query_con", LFWWfmConnector.generatePK());
 		}  
 		LFWWfmConnector.executeSql(sqlCon);
// 		try {
//			FileUtilities.saveFile(filePath + "/" + "0001.qtc", sqlCon.toString().getBytes("GBK"));
//		} catch (UnsupportedEncodingException e) {
//			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
//		} catch (IOException e) {
//			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
//		}
	}
	
	/**
	 * 注册流程分类
	 * @param flowCatPk 流程大类Pk
	 * @param typeCode	流程分类Code
	 * @param typeName	流程分类Name
	 * @param serverClass	流程分类对应处理类
	 * @return  流程分类Pk
	 */
	public static String registerFlowType(String flowCatPk, String typeCode, String typeName, String serverClass){
		WfmFlwTypeVO flwType = new WfmFlwTypeVO();
		flwType.setPk_flwcat(flowCatPk);
		flwType.setTypecode(typeCode);
		flwType.setTypename(typeName);
//		flwType.setServerclass("nc.bs." + windowId + ".wfm.DefaultFormOper");
		flwType.setServerclass(serverClass);
		String groupPk = LFWWfmConnector.getGroupPk();
		flwType.setPk_group(groupPk);
		flwType.setPk_org(groupPk);
		LFWWfmConnector.saveFlwType(flwType);
		WfmFlwTypeVO flwTypeVo = LFWWfmConnector.getFlwTypeByCode(typeCode);
		String flowTypePk = flwTypeVo.getPk_flwtype();		
		
		WfmProdefVO proDef = new WfmProdefVO();
		proDef.setId("DefId");
		proDef.setName("wfmProDef");
		proDef.setFlwtype(flowTypePk);
		proDef.setIsnotstartup(UFBoolean.TRUE);
		String PK = LFWWfmConnector.generatePK();
		proDef.setPk_original(PK);
		proDef.setPk_prodef(PK);
		proDef.setPk_group(groupPk);
		proDef.setPk_org(groupPk);
		proDef.setVersion("1.0");
		
		LFWWfmConnector.insertProdef(proDef);
/*
//		String pk_org = Pk_prodef;
//		proDef.setPk_original(pk_org);
		LFWWfmConnector.executeSql("UPDATE wfm_prodef SET pk_original = '" + Pk_prodef + "' WHERE pk_prodef = '" + Pk_prodef + "'");
*/		
		return flowTypePk;
	}
	
	public static void deleteFlw(String flwTypePk){
		WfmProdefVO[] proVOs = LFWWfmConnector.getProDef(flwTypePk);
		for(WfmProdefVO vo:proVOs){
			String pk = vo.getPk_prodef();
			LFWWfmConnector.delProdef(pk);
		}
		LFWWfmConnector.delFlwType(flwTypePk);
	}
	public static void deleteTemplate(String nodeCode){
		String pk_query = LFWWfmConnector.getQueryTemplateByNodeCode(nodeCode);
		if(pk_query!=null){
			LFWWfmConnector.executeSql("delete from cp_query_condition where pk_query_template='"+pk_query+"'");
			LFWWfmConnector.delQryTemplate(pk_query);
		}
		
		String pk_print = LFWWfmConnector.getPrintTemplateByNodeCode(nodeCode);
		if(pk_print!=null){
			LFWWfmConnector.executeSql("delete from cp_print_condition where pk_print_template='"+pk_print+"'");				
			LFWWfmConnector.delPrintTemplate(pk_print);
		}
	}
	
}
