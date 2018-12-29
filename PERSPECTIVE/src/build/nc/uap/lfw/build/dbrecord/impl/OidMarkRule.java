package nc.uap.lfw.build.dbrecord.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * OIDMark配置规则工厂类。
 * 
 * @author PH
 *
 */
@XStreamAlias("oidMarkRule")
public class OidMarkRule {
	
	/** OIDMarkMap集合 */
	@XStreamImplicit
	private List<OidMarkMap> oidMarkMaps;
	
	/** 需校验的表集合 */
	@XStreamAlias("table")
	@XStreamImplicit
	private List<String> tables;
	
	private File file;
	
	private long lastModified;
	
	/** key:部门; value:OIDMark*/
	private transient Map<String, String> deptOidMarkMap;
	
	private static Map<String, OidMarkRule> instanceMap = new HashMap<String, OidMarkRule>(); 
	
	private OidMarkRule(){

	}
	
	/**
	 * 据版本获取对应的OIDMark规则。
	 * 
	 * @param oidMarkRuleFileName 版本
	 * @return 对应的OIDMark规则
	 */
	public synchronized static OidMarkRule getInstance(String oidMarkRuleFileName){
		if(StringUtils.isBlank(oidMarkRuleFileName)){
			return null;
		}
		File file = new File(oidMarkRuleFileName);
		oidMarkRuleFileName = file.getPath();
		OidMarkRule rule = instanceMap.get(oidMarkRuleFileName);
		if(rule == null){
			rule = parseOIDMarkRule(file);
			if(rule != null){
				instanceMap.put(oidMarkRuleFileName, rule);
			}
		}else{
			long lastModified = rule.file.lastModified();
			if(lastModified == 0){
				instanceMap.remove(oidMarkRuleFileName);
				return null;
			}else if(lastModified > rule.lastModified){
				instanceMap.put(oidMarkRuleFileName, parseOIDMarkRule(file));
			}
		}
		return instanceMap.get(oidMarkRuleFileName);
	}
	
	public List<String> getTables() {
		return tables;
	}

	public Map<String, String> getDeptOidMarkMap() {
		return deptOidMarkMap;
	}
	
	/**
	 * 解析OIDMark规则。
	 * 
	 * @param ruleCfgfile OIDMark规则配置文件
	 * @return OIDMark规则
	 */
	private static OidMarkRule parseOIDMarkRule(File ruleCfgfile){
		//version : nc6.0
		XStream xstream = new XStream();
		List<Class<?>> clazzs = new ArrayList<Class<?>>();
		clazzs.add(OidMarkRule.class);
		clazzs.add(OidMarkMap.class);
		Annotations.configureAliases(xstream, clazzs.toArray(new Class[0]));
		
		Reader reader = null;
		OidMarkRule rule = null;
		long lastModified = ruleCfgfile.lastModified();
		try{
			reader = new InputStreamReader(new FileInputStream(ruleCfgfile), "UTF-8");
			rule = (OidMarkRule)xstream.fromXML(reader);
		}catch (Exception e) {
			MainPlugin.getDefault().logError("Failed to parse oidmark with version path: (" + ruleCfgfile.getPath() +")",e);
		}finally{
			IOUtils.closeQuietly(reader);
		}
		if(rule != null){
			rule.file = ruleCfgfile;
			rule.lastModified = lastModified;
			cvtOIDMarkRule(rule);
		}
		
		return rule;
	}
	
	@SuppressWarnings("unchecked")
	private static void cvtOIDMarkRule(OidMarkRule rule){
		if(rule.tables != null){
			rule.tables = Collections.unmodifiableList(rule.tables);
		}else{
			rule.tables = Collections.unmodifiableList(Collections.EMPTY_LIST);
		}
		Map<String, String> deptOidMapTemp = new HashMap<String, String>();
		if(rule.oidMarkMaps != null && !rule.oidMarkMaps.isEmpty()){
			for(OidMarkMap oidMarkMap : rule.oidMarkMaps){
				deptOidMapTemp.put(oidMarkMap.getDepartment(), oidMarkMap.getOidMark());
			}
		}
		rule.deptOidMarkMap = Collections.unmodifiableMap(deptOidMapTemp);
	}
	
//	private static File getCfgFileByVersion(String version){
//		String ruleFilePath = new StringBuilder().append(RuntimeEnv.getInstance().getNCHome())
//			.append(File.separator).append(BuildContextContainer.getInstance().getConfigPathByVersion(version))
//			.append(File.separator).append(OID_RULE_FILENAME).toString();
//		return new File(version);
//	}
	
	@XStreamAlias("oidMarkMap")
	static class OidMarkMap {
		
		private String department;
		
		private String oidMark;

		public String getDepartment() {
			return department;
		}

		public String getOidMark() {
			return oidMark;
		}

		@Override
		public String toString() {
			return department + ":" + oidMark;
		}

	}

}
