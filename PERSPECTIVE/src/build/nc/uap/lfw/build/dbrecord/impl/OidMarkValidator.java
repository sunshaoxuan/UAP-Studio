package nc.uap.lfw.build.dbrecord.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.build.optlog.OperateLogger;
import nc.uap.lfw.build.optlog.OperateLogger.LogLevel;

import org.apache.commons.lang.StringUtils;

/**
 * OIDMark У������
 * 
 * @author PH
 */
class OidMarkValidator {
	/** OIDMark�����ļ�·�� */
	private String oidMarkRuleFilePath;
	
	private Map<String, PkInfo> illegalPkMap;
	
	OidMarkValidator(String oidMarkRuleFilePath){
		this.oidMarkRuleFilePath = oidMarkRuleFilePath;
		illegalPkMap = new LinkedHashMap<String, PkInfo>();
	}
	
	boolean isValidationNeed(String tableName, String department){
		OidMarkRule oidMarkRule = OidMarkRule.getInstance(oidMarkRuleFilePath);
		if(oidMarkRule == null){
			String msg = "OIDMark�����ļ�" + oidMarkRuleFilePath + "�����ڻ���������";
			MainPlugin.getDefault().logInfo(msg);
			OperateLogger.getInstance().addLog(LogLevel.WARN, msg);
		}else{
			if(oidMarkRule.getTables().contains(tableName)){
				if(StringUtils.isNotBlank(oidMarkRule.getDeptOidMarkMap().get(department))){
					return true;
				}else{
					OperateLogger.getInstance().addLog(LogLevel.WARN, new StringBuilder("OIDMark�����ļ�(")
						.append(oidMarkRuleFilePath).append(")�в���(").append(department).append(")δ����OIDMark����").toString());
				}
			}
		}
		return false;
	}
	
	void validate(String tableName, String department, String pkColName, List<String> pkValues){
		OidMarkRule oidMarkRule = OidMarkRule.getInstance(oidMarkRuleFilePath);
		if(oidMarkRule != null){
			String oidMark = oidMarkRule.getDeptOidMarkMap().get(department);
			if(oidMark != null && !(oidMark = oidMark.trim()).equals("")){
				List<String> oidMarks = Arrays.asList(oidMark.split(",\\s*"));
				for(String pkValue : pkValues){
					if(pkValue == null || (pkValue = pkValue.trim()).length() < 6 
							|| !oidMarks.contains(pkValue.substring(4, 6))){
//						pkValue = pkValue.substring(1, pkValue.length()-1);
						addIllegalPk(tableName, pkColName, oidMarks, pkValue);
					}
				}
			}
		}
	}
	
	Map<String, PkInfo> getResults(){
		return illegalPkMap;
	}
	
	private void addIllegalPk(String tableName, String pkName, List<String> oidMarks, String pkValue){
		PkInfo pkInfo = illegalPkMap.get(tableName);
		if(pkInfo == null){
			pkInfo = new PkInfo();
			pkInfo.oidMarks =oidMarks;
			illegalPkMap.put(tableName, pkInfo);
		}
		Map<String, List<String>> pkNameValuesMap = pkInfo.pkNameMapIllegalValues;
		List<String> illegalValues = pkNameValuesMap.get(pkName);
		if(illegalValues == null){
			illegalValues = new ArrayList<String>();
			pkNameValuesMap.put(pkName, illegalValues);
		}
		illegalValues.add(pkValue);
	}
	
	/**
	 * �����Ϲ淶��������Ϣ��
	 * 
	 * @author PH
	 */
	static class PkInfo{
		private List<String> oidMarks;
		
		private Map<String, List<String>> pkNameMapIllegalValues;
		
		PkInfo() {
			pkNameMapIllegalValues = new LinkedHashMap<String, List<String>>();
			oidMarks = new ArrayList<String>();
		}

		List<String> getOidMarks() {
			return oidMarks;
		}

		Map<String, List<String>> getPkNameMapIllegalValues() {
			return pkNameMapIllegalValues;
		}
		
		
	}

}
