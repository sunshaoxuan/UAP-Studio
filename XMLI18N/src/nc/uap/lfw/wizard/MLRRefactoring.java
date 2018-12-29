package nc.uap.lfw.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.mlr.CreateResFileChange;
import nc.uap.lfw.mlr.MLRPropertyCache;
import nc.uap.lfw.mlr.MLRes;
import nc.uap.lfw.mlr.MLResElement;
import nc.uap.lfw.mlr.MLResSubstitution;
import nc.uap.lfw.mlr.MyPropertyFileDocumentModel;
import nc.uap.lfw.plugin.Activator;
import nc.uap.lfw.plugin.common.CommonPlugin;
import nc.uap.lfw.tool.Helper;
import nc.uap.lfw.tool.ProjConstants;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.internal.corext.refactoring.changes.TextChangeCompatibility;
import org.eclipse.jdt.internal.corext.refactoring.nls.KeyValuePair;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;

/**
 * xml多语 Refactoring
 * 
 * @author dingrf
 *
 */
public class MLRRefactoring extends Refactoring{

	/** project*/
	private IProject project;
//	/** 资源文件模块名称*/
	private IFolder folder;
//	/** 资源文件夹名称*/
	
//	private String resModuleName;
	/** 资源文件名称*/
//	private String resFileName;
//	/** key值前缀*/
//	private String prefix;
	private List<MLResSubstitution> substitutionsList;
//	private MLResSubstitution rawSubstitutions[];
	private MLResSubstitution substitutions[];
	
	private String expType = "module";
	
	/**当前PageNode*/
	private PageNode currentPageNode;
	
	private String resourceHomePath;
	/** xml文件内容*/
	private Map<String,String> contents;
	/** xml文件原始内容*/
	private Map<String,String> oldContents;
	/**已有多语资源Cache*/
	private MLRPropertyCache propCache;
	/**page2*/
	private ExternalizeMLRWizardPage2 page2;
	/**源文件发生改变*/
	private boolean sourceFileChanged=false;
	
	/**生成的key列表*/
	private List<String> newKeyList;
	
	/**所有需要修改的key列表*/
	private List<String> allKeyList;
	
	private List<MLRes> simpchnResList;
	
	/**所有key列表*/
	private List<String> resKeyList;
	
	private boolean flag;
	
	private boolean cleanRes;
	
	private Map<String,List<String>> noChinValueMap;

	
	public MLRRefactoring(IProject project){
		
		this.project = project;
//		propCache = new MLRPropertyCache(project, getResouceHomePath());
//		String eleName = "xxxxxxxxxxxxx";
//		prefix = eleName + "-";
//		resModuleName = project.getName()+"_nodes";
//		resFileName = eleName + "res.properties";
//		rawSubstitutions = createRawSubstitution();
//		substitutions = rawSubstitutions;
	}
	public MLRRefactoring(IFolder folder){
		this.folder = folder;
	}

	/**
	 * 取资源文件路径
	 * 
	 * @return
	 */
	public String getResouceHomePath(){
//		if (resourceHomePath == null){
			String prefix = "";
//			String elePath = xmlFile.getLocation().toString();
			String projPath = project.getLocation().toOSString();
//			int index = elePath.indexOf(projPath);
//			if (index != -1)
//				index += projPath.length();
//			if (index != -1 && index < elePath.length())
//				prefix = elePath.substring(index);
			if(LfwCommonTool.isBCPProject(project)&& getExpType().equals("module")){
				resourceHomePath = new StringBuilder(folder.getFullPath().segments()[1]).append("/resources").toString();
			}
			else resourceHomePath = "/resources";
//		}
		return resourceHomePath;
	}

	/**
	 * 找到所有i18nName属性的节点
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void createRawSubstitution(List<PageNode> pageNodes){
		
		noChinValueMap = new HashMap<String,List<String>>();
		List<String> noChinList = null;
		for (PageNode p : pageNodes){
			if (!p.isFile())
				continue;
			File file = new File(p.getPath());
			try {
				if (this.contents == null)
					this.contents = new HashMap<String,String>();
				if(simpchnResList == null){
					propCache = new MLRPropertyCache(project, "/"+folder.getFullPath().segments()[1]+"/resources");
					simpchnResList = (List<MLRes>) propCache.findLocalMLResList(p.getBcpName(),ProjConstants.LANGCODE_SIMPCHN, p.getResModuleName());
				}							
				if(allKeyList == null){
					allKeyList = new ArrayList<String>();
					allKeyList = getModuleKeyList(p.getBcpName(),p.getResModuleName());
					resKeyList = allKeyList;					
				}
				Map<String,String> keyMap = new HashMap<String,String>();
				String cx = getModifiedContent(file,p,keyMap);
				this.contents.put(p.getPath(), cx);
				Document doc = FileUtilities.parseText(this.contents.get(p.getPath())) ;
				Document oldDoc = FileUtilities.parseText(this.oldContents.get(p.getPath())) ;
				List<? extends Node> nodes =  doc.selectNodes("//*[@"+ ProjConstants.I18NNAME +"]");
				noChinList = new ArrayList<String>();
				//找i18nName对应的属性，顺序：text,title,caption
				for (Node n : nodes){
					String value;
					Element e = (Element) n;
					if (e.attribute("text") !=null){
						value = e.attribute("text").getText();
					}
					else if(e.attribute("title") !=null){
						value = e.attribute("title").getText();
					}
					else if (e.attribute("caption") !=null){
						value = e.attribute("caption").getText();
					}
					else
						value ="";
					
					if("".equals(value)||value.getBytes().length==1) continue;
					if(value.getBytes().length == value.length()&&!noChinList.contains(value)){
						noChinList.add(value);
					}
					//创建 MLResSubstitution
					String resID = e.attribute(ProjConstants.I18NNAME).getText();
					String i18n = ProjConstants.I18NNAME + "=\"" + e.attribute(ProjConstants.I18NNAME).getText() + "\"";
//					int start = this.contents.get(p.getPath()).indexOf(i18n); 
//					int length = i18n.length();
					MLResElement ele = new MLResElement(value,0,0);
				    MLResSubstitution substitution = new MLResSubstitution(ele, ele.getValue());
				    substitution.setPrefix(p.getPrefix());
				    String langDir ="";
				    if (e.attribute(ProjConstants.LANG_DIR) != null)
				    	langDir = e.attribute(ProjConstants.LANG_DIR).getText();
				    substitution.setLangModule(langDir);
				    substitution.setFilePath(p.getPath());
				    substitution.setSelectKey(i18n);
				    substitution.setExtKey(resID);
				   
//				    substitution.setResFileName(p.getPrefix().substring(0,p.getPrefix().length()-1)+"res.properties");
				    substitution.setResFileName(p.getPrefix()+"res.properties");
				    substitution.setPageNode(p);
				    
				    //是否为新增
				    boolean isNew = true;	
				    
				    //已多语化非公共资源
				    if (this.oldContents.get(p.getPath()).indexOf(resID)!= -1 && !langDir.equals(ProjConstants.COMM)){
				    	List<? extends Node> tempNodes = oldDoc.selectNodes("//*[@"+ ProjConstants.I18NNAME +"='"+resID+"']");
				    	for(Node nl : tempNodes){
				    		Element el = (Element) nl;
				    		//根据i18nName 与 langDir判读是否已存在
//				    		if (el.attribute(ProjConstants.LANG_DIR)!= null && el.attribute(ProjConstants.LANG_DIR).getText().equals(langDir)&&langDir.equals(p.getBcpName()+"_nodes")){
				    		if (el.attribute(ProjConstants.LANG_DIR)!= null && el.attribute(ProjConstants.LANG_DIR).getText().equals(langDir)){
//				    			substitution.setExtKey(resID);
				    			substitution.setExtLangModule(e.attribute(ProjConstants.LANG_DIR).getText());
				    			substitution.setState(1);
//				    			substitution.setPrefix(resID.substring(0,resID.indexOf("-")+1));
//				    			String prefix = substitution.getPrefix();
				    			String prefix = resID.substring(0,resID.indexOf("-")+1);
				    			String projectName = project.getName()+"-";
//				    			if(projectName.equalsIgnoreCase(prefix)){
//				    				propCache = new MLRPropertyCache(project, "/resources");
//				    			}
//				    			else{
//				    				if(p.getBcpName()==null||"".equals(p.getBcpName()))
//				    					propCache = new MLRPropertyCache(project, "/resources");
//				    				else
//				    					propCache = new MLRPropertyCache(project, "/"+folder.getFullPath().segments()[1]+"/resources");
//				    			}
				    			
				    			MLRes simpchnRes = propCache.findLocalMLRes(p.getBcpName(), ProjConstants.LANGCODE_SIMPCHN, substitution.getExtLangModule(), resID);		    			
				    			MLRes tradchnRes = propCache.findLocalMLRes(p.getBcpName(),ProjConstants.LANGCODE_TRADCHN, substitution.getExtLangModule(), resID);
				    			MLRes englishRes = propCache.findLocalMLRes(p.getBcpName(),ProjConstants.LANGCODE_ENGLISH, substitution.getExtLangModule(), resID);
				    			if (simpchnRes != null){
				    				substitution.setSimpchnRes(simpchnRes);
				    				if(!resKeyList.contains(simpchnRes.getResID())){
				    					simpchnResList.add(simpchnRes);
				    					resKeyList.add(simpchnRes.getResID());
				    				}
				    			}
				    			else{
					    			if(!propCache.findLangDir(p.getBcpName(), ProjConstants.LANGCODE_SIMPCHN, substitution.getExtLangModule())){
					    				substitution.setState(4);
						    			isNew = false;
						    			continue;
					    			}
				    			}
//				    			else{
//				    				substitution.setState(4);
//					    			isNew = false;
//					    			continue;
//				    			}
//NO_EN				    			
				    			if (tradchnRes != null)
				    				substitution.setTradchnRes(tradchnRes);
				    			if (englishRes != null)
				    				substitution.setEnglishRes(englishRes);
				    			isNew = false;
//				    			if(projectName.equalsIgnoreCase(prefix)||prefix.equals(p.getBcpName()+"-")){
				    			if(!prefix.equalsIgnoreCase(p.getPrefix())){
				    				String k = getKey(cx,p);
				    				cx = cx.replace(ProjConstants.LANG_DIR +  "=\""+ substitution.getLangModule() +"\"", ProjConstants.LANG_DIR +  "=\""+p.getResModuleName() +"\"");
				    				cx = cx.replace(ProjConstants.I18NNAME +"=\""+ resID +"\"", ProjConstants.I18NNAME +"=\""+ k +"\"");
				    				this.contents.put(p.getPath(), cx);
				    				substitution.setExtKey(k);
				    				substitution.setKey(k.replace(p.getPrefix() , ""));
				    				substitution.setLangModule(p.getResModuleName());
				    				substitution.setSelectKey(ProjConstants.I18NNAME + "=\"" + k + "\"");
				    				substitution.setState(6);
				    				p.setChanged(true);
				    				isNew = true;
				    			}
				    			if(!substitution.getValue().equals(substitution.getSimpValue())){
				    				String k = getKey(cx,p);
				    				cx = cx.replace(ProjConstants.I18NNAME +"=\""+ resID +"\"", ProjConstants.I18NNAME +"=\""+ k +"\"");
				    				this.contents.put(p.getPath(), cx);
				    				substitution.setExtKey(k);
				    				substitution.setKey(k.replace(p.getPrefix() , ""));
				    				substitution.setSelectKey(ProjConstants.I18NNAME + "=\"" + k + "\"");
				    				substitution.setState(7);
				    				p.setChanged(true);
				    				isNew = true;
				    			}
				    			break;
				    		}
				    		else{
				    			//错误
				    			substitution.setState(4);
				    			isNew = false;
				    		}
				    	}
				    }
				  //公共资源
				    else if (this.oldContents.get(p.getPath()).indexOf(resID)!= -1 && langDir.equals(ProjConstants.COMM)){
				    	substitution.setCommKey(resID);
				    	substitution.setState(3);
				
						MLRes simpchnRes = propCache.findLocalMLRes(p.getBcpName(),ProjConstants.LANGCODE_SIMPCHN, substitution.getLangModule(), resID);
						MLRes tradchnRes = propCache.findLocalMLRes(p.getBcpName(),ProjConstants.LANGCODE_TRADCHN, substitution.getLangModule(), resID);
						MLRes englishRes = propCache.findLocalMLRes(p.getBcpName(),ProjConstants.LANGCODE_ENGLISH, substitution.getLangModule(), resID);
						if (simpchnRes != null)
							substitution.setSimpchnRes(simpchnRes);
						if (tradchnRes != null)
							substitution.setTradchnRes(tradchnRes);
						if (englishRes != null)
							substitution.setEnglishRes(englishRes);
						isNew = false;
				    }
				    
				    if (isNew){
					    //去除重复id
				    	String k = null;
					    if (substitution.getValue() != null && !substitution.getValue().equals("")){
					    	if (keyMap.containsKey(substitution.getValue())){
					    		k = keyMap.get(substitution.getValue());
					    		String s = ProjConstants.I18NNAME + "=\"" + p.getPrefix() + k + "\"";
					    		String  c = this.contents.get(p.getPath());
					    		c = c.replace(substitution.getSelectKey(), s);
					    		this.contents.put(p.getPath(), c);
					    		substitution.setSelectKey(s);
					    	}
					    	else{
					    		k = substitution.getExtKey().replace(substitution.getPrefix() , "");
					    		keyMap.put(substitution.getValue(), k);
					    	}
					    } 
					    substitution.setKey(k);
				    }
				    if (substitutionsList == null)
				    	substitutionsList = new ArrayList(); 
				    substitutionsList.add(substitution);
				}
				//找到有汉字但是没有i18nName的行
				while (!cx.equals("")){
					this.flag = false;
					String s = cx.substring(0,cx.indexOf("\n")+1);
					if("".equals(s)) s = cx;
//					if(s.indexOf("<")!=-1&&s.indexOf("<")!=s.indexOf("<!")) flag = true;
//					if(s.indexOf(">")==0||(s.indexOf(">")>0&&s.indexOf(">")!=s.indexOf("]>")+1&&s.indexOf(">")!=s.indexOf("->")+1)) flag = false;
//					String test = getSimpchn(s);
					String s1 = s.trim();
//					String value = getChin(s1, flag);
					Map<String,String> valueMap = getChin(s1, flag);
					Iterator<String> iter = valueMap.keySet().iterator();
					while(iter.hasNext()){
						String key =iter.next();
						String i18Name = "";
						if(key.equals("text")){
							i18Name = ProjConstants.I18NNAME;
						}
						else if(key.equals("description")){
							i18Name = ProjConstants.DESC_I18NNAME;
						}
						else i18Name = ProjConstants.TIP_I18NNAME;
						if (!"".equals(valueMap.get(key)) && s.indexOf(i18Name) == -1){
							
							p.setChanged(true);
//							int start = this.contents.get(p.getPath()).indexOf(s); 
//							int length = s.length();
							
							MLResElement ele = new MLResElement(valueMap.get(key),0,0);
						    MLResSubstitution substitution = new MLResSubstitution(ele, ele.getValue());
						    substitution.setPrefix(p.getPrefix());
						    substitution.setFilePath(p.getPath());
						    substitution.setResFileName(p.getPrefix()+"res.properties");
						    substitution.setPageNode(p);
						    substitution.setLangModule(p.getResModuleName());
						    substitution.setState(6);
							String c = this.getContents().get(p.getPath());
							
						    //去除重复id
							String k =null;
						    if (substitution.getValue() != null && !substitution.getValue().equals("")){
						    	if (keyMap.containsKey(substitution.getValue())){
						    		k = keyMap.get(substitution.getValue());
						    		String select = i18Name + "=\"" + p.getPrefix() + k + "\"";
//						    		c = c.replace(substitution.getSelectKey(), select);
						    		substitution.setSelectKey(select);
						    	}
						    	else{
						    		k = getKey(cx, p).replace(p.getPrefix(), "");
						    		keyMap.put(substitution.getValue(), k);
						    	}
						    } 
						    substitution.setKey(k);
						    substitution.setExtKey(substitution.getRealKey());
						    String s2 = null;
						    if(s1.indexOf(ProjConstants.LANG_DIR)>-1)
						    	s2 = s1.replace(">", " "+ i18Name + "=\"" + substitution.getRealKey() + "\">");
						    else
						    	s2 = s1.replace(">", " "+ i18Name + "=\"" + substitution.getRealKey() + "\" " + ProjConstants.LANG_DIR + "=\"" + p.getResModuleName() +"\">");
							c = c.replace(s1, s2);
							this.contents.put(p.getPath(), c);
							substitution.setOldKey(s1);
							substitution.setSelectKey(s2);
							substitution.getElement().setFPosition(c.indexOf(s2),s2.length());
							s1 = s2;
						    if (substitutionsList == null)
						    	substitutionsList = new ArrayList(); 
						    substitutionsList.add(substitution);
						    String value = valueMap.get(key);
						    if(value.getBytes().length == value.length()&&!noChinList.contains(value)){
						    	noChinList.add(value);
						    }
							
						}
					}
					
					cx = cx.substring(cx.indexOf(s)+s.length()).trim();
//					cx = cx.replace(s, "");
				}
			} catch (Exception e) {				
				Activator.getDefault().logError(e.getMessage(), e);
			}
			if(noChinList!=null)
				noChinValueMap.put(p.getPath(), noChinList);
		}
		if (substitutionsList != null){
//			rawSubstitutions = (MLResSubstitution[])substitutionsList.toArray(new MLResSubstitution[0]);
			substitutions = (MLResSubstitution[])substitutionsList.toArray(new MLResSubstitution[0]);
		}
		//更新位置信息
		for (PageNode p : pageNodes){
			updateSubPostion(p);
		}
		
	}
	
	/**
	 * 修改xml文件，对空的i18nName添入ID,不存在langDir时，添入langDir
	 * 
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	private String getModifiedContent(File file,PageNode p,Map<String,String> keyMap) throws Exception {
		String cxOld = FileUtilities.fetchFileContent(file,"UTF-8").trim();
		String cx = cxOld;
		if (this.oldContents == null)
			this.oldContents = new HashMap<String,String>();
		this.oldContents.put(p.getPath(), cxOld);
		
		//对i18nName赋值
		List<String> newKeys = new ArrayList<String>();
		while (cx.indexOf(ProjConstants.I18NNAME_ISNULL) != -1){
			String k = getKey(cx,p);
			newKeys.add(k);
			cx = cx.replaceFirst(ProjConstants.I18NNAME_ISNULL, ProjConstants.I18NNAME +"=\""+ k +"\"");
		}
		// 替换langDir="" 为 langDir="moduleName"
		cx = cx.replace(ProjConstants.LANG_ISNULL, ProjConstants.LANG_DIR + "=\""+ p.getResModuleName() +"\"");
		
		//对有i18nName但没有langDir属性的节点添加langDir属性
		Document doc = null;
//		Thread.currentThread().setContextClassLoader(DocumentHelper.class.getClassLoader());
		try{
//			Thread.currentThread().getContextClassLoader();
//			Thread.currentThread().setContextClassLoader(DocumentHelper.class.getClassLoader());
//			doc = DocumentHelper.parseText(cx);
			doc = FileUtilities.parseText(cx);
		}
		catch (Exception e) {
			byte[] b = cx.getBytes("UTF-8");
			cx = new String(b,3,b.length-3,"UTF-8");
			doc = FileUtilities.parseText(cx);
//			Thread.currentThread().getContextClassLoader()
			String cxold = oldContents.get(p.getPath());
			b = cxold.getBytes("UTF-8");
			cxold = new String(b,3,b.length-3,"UTF-8");
			oldContents.put(p.getPath(), cxold);
			Activator.getDefault().logError("解析文件出现问题，请用编辑工具将文件保存成无BOM格式,文件为："+p.getName(), e);
		}
		List<? extends Node> nodes =  doc.selectNodes("//*[@" + ProjConstants.I18NNAME + "]");
		for (Node n : nodes){
			Element e = (Element) n;
			String k = e.attribute(ProjConstants.I18NNAME).getText();
			if (newKeys.contains(k)){
				String i18nName = ProjConstants.I18NNAME + "=\"" + k + "\""; 
				if (e.attribute(ProjConstants.LANG_DIR)==null){
					cx = cx.replaceFirst(i18nName, i18nName + " " + ProjConstants.LANG_DIR +  "=\""+ p.getResModuleName() +"\"");
				}
			}
		}
		
		if (cx!= cxOld)
			p.setChanged(true);
		return cx;
	}
	
	/**
	 * 取模块分类中的所有资源ID列表
	 * 
	 * @param resModuleName
	 * @return
	 */
	private List<String> getModuleKeyList(String bcpName,String resModuleName) {
		if("".equals(bcpName)||bcpName==null){
			propCache = new MLRPropertyCache(project, "/resources");
		}
		else{
			propCache = new MLRPropertyCache(project, "/"+folder.getFullPath().segments()[1]+"/resources");
		}
		
//		List<MLRes> simpchnResList = (List<MLRes>) propCache.findLocalMLResList(bcpName,ProjConstants.LANGCODE_SIMPCHN, resModuleName);
	
//		List<MLRes> tradchnResList = (List<MLRes>) propCache.findLocalMLResList(bcpName,ProjConstants.LANGCODE_TRADCHN, resModuleName);
//		List<MLRes> englishResList = (List<MLRes>) propCache.findLocalMLResList(bcpName,ProjConstants.LANGCODE_ENGLISH, resModuleName);
		List<String> keyList = new ArrayList<String>();
		for (MLRes m : simpchnResList){
			if (!keyList.contains(m.getResID()))
				keyList.add(m.getResID());
		}
//		for (MLRes m : tradchnResList){
//			if (!keyList.contains(m.getResID()))
//				keyList.add(m.getResID());
//		}
//		for (MLRes m : englishResList){
//			if (!keyList.contains(m.getResID()))
//				keyList.add(m.getResID());
//		}
		return keyList;
	}

	/**
	 * 生成新key
	 * 
	 * @param content
	 * @return
	 */
	public String getKey(String content,PageNode p){
		int i = 1;
		String key = createKey(i);
		String keyWithPrefix = p.getPrefix() + key;
		
		//模块下已存在的key
		List<String> keyList = getModuleKeyList(p.getBcpName(),p.getResModuleName());
		
		if (newKeyList == null)
			newKeyList = new ArrayList<String>();
		//新key不能为模块资源中已有的key，也不能与当前文件内容重复
		while (newKeyList.contains(keyWithPrefix) || keyList.contains(keyWithPrefix) || content.indexOf(keyWithPrefix)!= -1){
			key = createKey(++i);
			keyWithPrefix = p.getPrefix() + key;
		}
		keyList.add(keyWithPrefix);
		newKeyList.add(keyWithPrefix);
//		allKeyList.add(keyWithPrefix);
		return keyWithPrefix;
	}
	
	/**
	 * 创建key
	 * 
	 * @param counter
	 * @return
	 */
	private String createKey(int counter){
		String temp = "000000";
		String str = String.valueOf(counter);
		if (str.length() > temp.length())
			return str.substring(str.length() - temp.length());
		else
			return (new StringBuilder(String.valueOf(temp.substring(0, temp.length() - str.length())))).append(str).toString();
	}

	/**
	 * 更新前缀
	 * 
	 */
	private void refreshPrefix(){
		if (currentPageNode == null)
			return;
		int count = substitutions != null ? substitutions.length : 0;
		for (int i = 0; i < count; i++){
			if (substitutions[i].getPageNode() != currentPageNode)
				continue;
			if (substitutions[i].getState()==0){
				String oldKey = substitutions[i].getPrefix() + substitutions[i].getKey(); 
				substitutions[i].setPrefix(currentPageNode.getPrefix());
				String contents = this.contents.get(currentPageNode.getPath());
				contents = contents.replace(oldKey, substitutions[i].getPrefix() + substitutions[i].getKey());
				this.contents.put(currentPageNode.getPath(), contents);
			}
		}
	}

	/**
	 * 更新多语模块
	 * 
	 */
	//TODO   可能不能删除 
//	private void refreshLangModule(){
//		int count = substitutions != null ? substitutions.length : 0;
//		List<String> keyList = getModuleKeyList(resModuleName);
//		for (int i = 0; i < count; i++){
//			if (substitutions[i].getState()==0){
//				//更新langDir
//				String oldLangModule = substitutions[i].getLangModule(); 
//				substitutions[i].setLangModule(resModuleName);
//				String contents = this.contents.get(currentPageNode.getPath());
//				contents = contents.replace(ProjConstants.LANG_DIR + "=\"" + oldLangModule + "\"", ProjConstants.LANG_DIR+ "=\"" + resModuleName + "\"");
//				this.contents.put(currentPageNode.getPath(), contents);
//				//更新i18nName
//				String oldkey = substitutions[i].getRealKey();
//				//在模块资源文件中存在前在key
//				if (keyList.contains(oldkey)){
//					String newKey = getKey(contents,keyList);
//					substitutions[i].setKey(newKey.replace(prefix, ""));
//					contents = this.contents.get(currentPageNode.getPath());
//					contents = contents.replaceFirst(ProjConstants.I18NNAME+ "=\"" + oldkey + "\"", ProjConstants.I18NNAME +"=\""+ newKey +"\"");
//					this.contents.put(currentPageNode.getPath(), contents);
//					
//				}
//			}
//		}
//	}
	
	public void updateSubPostion(PageNode pageNode){
		PageNode p = null;
		if (pageNode != null)
			p = pageNode;
		else if (currentPageNode != null)
			p = currentPageNode;
		if (p == null)
			return;
		int count = substitutions != null ? substitutions.length : 0;
		//更新位置信息
		for (int i = 0; i < count; i++){
			if (substitutions[i].getPageNode() != p)
				continue;
			if (substitutions[i].getState() == 4){
//				String value = ProjConstants.I18NNAME + "=\"" + substitutions[i].getSelectKey() + "\"";;
				String value = substitutions[i].getSelectKey();
				substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(value), value.length());
			}
			else if (substitutions[i].getState() == 5){
				String value = substitutions[i].getSelectKey();
				substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(value), value.length());
			}
			else{
//				String newKey = ProjConstants.I18NNAME + "=\"" + substitutions[i].getRealKey() + "\"";
				String newKey = substitutions[i].getSelectKey();
				substitutions[i].getElement().setFPosition(this.contents.get(p.getPath()).indexOf(newKey), newKey.length());
			}
		}
	}
	
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException{
		RefactoringStatus refactoringstatus;
		pm.beginTask("检查可执行外部化的条件", 3);
		RefactoringStatus result = new RefactoringStatus();
//		result.merge(Checks.validateModifiesFiles(getAllFilesToModify(), getValidationContext()));
		if (!needModifyPropFile() && !needModifySourceFile())
			result.addFatalError("没有任何多语资源外部化。");
		try{
			int count = substitutions != null ? substitutions.length : 0;
			for (int i = 0; i < count; i++){
				IFile fsimpchn = getResBoundleFile(ProjConstants.LANGCODE_SIMPCHN,substitutions[i].getRealLangModule(),substitutions[i].getResFileName());
				if (fsimpchn.exists() && !fsimpchn.getCharset().equalsIgnoreCase("UTF-16"))
					fsimpchn.setCharset("UTF-16", pm);
//ON_EN				
//				IFile ftradchn = getResBoundleFile(ProjConstants.LANGCODE_TRADCHN,substitutions[i].getResFileName());
//				if (ftradchn.exists())
//					ftradchn.setCharset("UTF-16", pm);
//				IFile fenglis = getResBoundleFile(ProjConstants.LANGCODE_ENGLISH,substitutions[i].getResFileName());
//				if (fenglis.exists() && !fenglis.getCharset().equalsIgnoreCase("GBK"))
//					fenglis.setCharset("GBK", pm);
			}	
		}
		catch (Exception e){
			result.addFatalError((new StringBuilder()).append(e.getClass()).append(":").append(e.getMessage()).toString());
		}
		refactoringstatus = result;
		pm.done();
		return refactoringstatus;
	}

	/**
	 * 得到所有需要修改的file
	 * 
	 * @return
	 */
	public IFile[] getAllFilesToModify(){
		List<IFile> list = new ArrayList();
		if (needModifySourceFile()){
			for (PageNode p: page2.getPageNodes()){
				if (p.isFile() && p.isChanged()){
					list.add(p.getFile());
				}			
			}
		}
		
		if (needModifyPropFile()){
			int count = substitutions != null ? substitutions.length : 0;
			for (int i = 0; i < count; i++){
				IFile fsimpchn = getResBoundleFile(ProjConstants.LANGCODE_SIMPCHN,substitutions[i].getRealLangModule(),substitutions[i].getResFileName());
				if (fsimpchn.exists())
					list.add(fsimpchn);
				
//NO_EN				
//				IFile ftradchn = getResBoundleFile(ProjConstants.LANGCODE_TRADCHN,substitutions[i].getResFileName());
//				if (ftradchn.exists())
//					list.add(ftradchn);
//				IFile fenglis = getResBoundleFile(ProjConstants.LANGCODE_ENGLISH,substitutions[i].getResFileName());
//				if (fenglis.exists())
//					list.add(fenglis);
			}
			if(allKeyList!=null&&allKeyList.size()>0){
				for(String key:allKeyList){
					IFile file = null;
					String fileName = null;					
					for(MLRes ml:simpchnResList){
						if(key.equals(ml.getResID())){
							fileName = ml.getResFileName();
							break;
						}
					}
					if(fileName!=null){
						file = project.getFile((new StringBuilder(folder.getFullPath().segment(1))).append("/resources/" + ProjConstants.LANG + "/").append(ProjConstants.LANGCODE_SIMPCHN).append("/").append(folder.getFullPath().segment(1)+"_nodes/").append(fileName).toString());
						if (file.exists())
							list.add(file);
					}
				}
			}
		}
		return (IFile[])list.toArray(new IFile[0]);
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException{
//		if (substitutions == null || substitutions.length == 0)
//			return RefactoringStatus.createFatalErrorStatus("没有需要外部化的多语资源。");
//		else
			return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException{
		CompositeChange compositechange;
		pm.beginTask("", 3);
		CompositeChange change = new CompositeChange("外部化NC多语资源");
		if (needModifyPropFile()){
			createUpdateResFileModifyChange(change);
//			addResFileModifyChange(change, ProjConstants.LANGCODE_SIMPCHN);
//			addResFileModifyChange(change, ProjConstants.LANGCODE_ENGLISH);
//			addResFileModifyChange(change, ProjConstants.LANGCODE_TRADCHN);
		}
		pm.worked(1);
		if (needModifySourceFile()){
				createModifySourceChange(change, pm);
//				Change c = createModifySourceChange();
//				change.add(c);
		}
		
		pm.worked(1);
		compositechange = change;
		pm.done();
		return compositechange;
	}

	private boolean needModifySourceFile(){
		boolean b = false;
		if (sourceFileChanged)
			return true;
		int count = substitutions != null ? substitutions.length : 0;
		for (int i = 0; i < count; i++){
			MLResSubstitution sub = substitutions[i];
			if (sub.getState() == 0||sub.getState() == 6||sub.getState()==7){
				b = true;
				break;
			}
		}
		return b;
	}

	private boolean needModifyPropFile(){
		boolean b = false;
		int count = substitutions != null ? substitutions.length : 0;
		for (int i = 0; i < count; i++){
			MLResSubstitution sub = substitutions[i];
			if (sub.getState() == 0 || sub.getState()==6 ||sub.hasModifyed()){
				b = true;
				break;
			}			
		}
		for (int i = 0; i < count; i++){
			MLResSubstitution sub = substitutions[i];
			if(allKeyList.contains(sub.getExtKey())){
				allKeyList.remove(sub.getExtKey());
			}
		}
		if(allKeyList.size()>0) 
			b = true;
		return b;
	}

	/**
	 * 资源文件改变
	 * 
	 * @param change
	 * @param langCode
	 * @throws CoreException
	 */
//	private void addResFileModifyChange(CompositeChange change, String langCode)throws CoreException{
//		//创建新资源文件
//		CreateResFileChange cfc = createFileChange(langCode);
//		if (cfc != null){
//			change.add(cfc);
//		} 
//		//存在资源文件
//		else{
//			TextFileChange tfc = createTextFileChange(langCode);
//			change.add(tfc);
//		}
//	}
	

	/**
	 * 修改源文件
	 * 
	 * @return
	 * @throws CoreException
	 */
	private void createModifySourceChange(CompositeChange change, IProgressMonitor pm)throws CoreException{
		for (PageNode p: page2.getPageNodes()){
			if (p.isFile() && p.isChanged()){
				if (!p.getFile().getCharset().equals("UTF-8"))
					p.getFile().setCharset("UTF-8", pm);
				TextChange c =  new TextFileChange("修改源文件", p.getFile());
				int l = c.getCurrentDocument(null).getLength();
				TextChangeCompatibility.addTextEdit(c, "修改源码", new ReplaceEdit(0, l, this.getContents().get(p.getPath())));
				change.add(c);
			}
		}
	}
	
	/**
	 * 修改资源文件
	 * 
	 * @param change
	 * @throws CoreException
	 */
	
	private void createUpdateResFileModifyChange(CompositeChange change)throws CoreException{
		HashMap<String,TextFileChange> map = new HashMap<String,TextFileChange>();
		HashMap<IFile,MyPropertyFileDocumentModel> pfdmHM = new HashMap<IFile,MyPropertyFileDocumentModel>();
		HashMap<IFile,StringBuilder> simpchnHM = new HashMap<IFile,StringBuilder>();
//NO_EN		
		HashMap<IFile,StringBuilder> tradchnHM = new HashMap<IFile,StringBuilder>();
		HashMap<IFile,StringBuilder> englishHM = new HashMap<IFile,StringBuilder>();
		int count = substitutions != null ? substitutions.length : 0;
		Map<String,List<String>> changedMaps = new HashMap<String, List<String>>();
		List<String> changedKeys = null;
		for (int i = 0; i < count; i++){
			MLResSubstitution sub = substitutions[i];
//			if (sub.getState() == 2){
//				createTextFileDelResChange(sub, sub.getRealKey(), ProjConstants.LANGCODE_SIMPCHN, map);
//				createTextFileDelResChange(sub, sub.getRealKey(), ProjConstants.LANGCODE_TRADCHN, map);
//				createTextFileDelResChange(sub, sub.getRealKey(), ProjConstants.LANGCODE_ENGLISH, map);
//			} else 
			//新增或修改或修复
			if (sub.getState() == 1 || sub.getState() == 0 || sub.getState() == 6|| sub.getState() == 7){
				String resId = sub.getRealKey();
				//去除重复
				String langdir = sub.getLangModule();
				if(langdir ==null) continue;
				if(changedMaps.get(langdir)==null){
					changedKeys = new ArrayList<String>();
					changedMaps.put(langdir, changedKeys);
				}
				if (changedMaps.get(langdir).contains(resId))
					continue;
				changedMaps.get(langdir).add(resId);
				if (sub.simpchnUpdated()||sub.getState()==6){
					String newValue = sub.getValue();
					MLRes res = sub.getSimpchnRes();
					String oldValue = res != null ? res.getValue() : null;
					createTextFileChange(sub, resId, oldValue, newValue, ProjConstants.LANGCODE_SIMPCHN, map, simpchnHM, pfdmHM);
				}
//NO_EN				
				if (sub.tradchnUpdated()||sub.getState()==6){
					String newValue = sub.getTwValue();
					MLRes res = sub.getTradchnRes();
					String oldValue = res != null ? res.getValue() : null;
					createTextFileChange(sub, resId, oldValue, newValue, ProjConstants.LANGCODE_TRADCHN, map, tradchnHM, pfdmHM);
				}
				if (sub.englishUpdated()||sub.getState()==6){
					String newValue = sub.getEnglishValue();
					MLRes res = sub.getEnglishRes();
					String oldValue = res != null ? res.getValue() : null;
					createTextFileChange(sub, resId, oldValue, newValue, ProjConstants.LANGCODE_ENGLISH, map, englishHM, pfdmHM);
				}
			}
		}	
		createNewFileChange(change, simpchnHM, "UTF-16","创建简体中文资源文件:");

//		createNewFileChange(change, tradchnHM, "UTF-16","创建繁体中文资源文件:");
//		createNewFileChange(change, englishHM, "UTF-16","创建英文资源文件:");		
		if(cleanRes&& allKeyList!=null&&allKeyList.size()>0){
			CommonPlugin.getPlugin().logInfo(""+allKeyList.size());
			for(String key:allKeyList){
				createTextFileChange(null, key, null, null, ProjConstants.LANGCODE_SIMPCHN, map, simpchnHM, pfdmHM);
			}
			
		}
		TextFileChange changes[] = (TextFileChange[])map.values().toArray(new TextFileChange[0]);
		for (int i = 0; i < changes.length; i++)
			change.add(changes[i]);
	}
	
	private void createNewFileChange(CompositeChange change, HashMap<IFile,StringBuilder> hm, String charSet,String name){
		CreateResFileChange cfc;
		for (Iterator<IFile> iter = hm.keySet().iterator(); iter.hasNext(); change.add(cfc)){
			IFile file = iter.next();
			StringBuilder sb = (StringBuilder)hm.get(file);
			cfc = new CreateResFileChange(file.getFullPath(), sb.toString(), charSet);
			cfc.setName((new StringBuilder(name)).append(file.getFullPath().toOSString()).toString());
		}

	}

	private void createTextFileChange(MLResSubstitution sub, String resId, String oldValue, String newValue, String langCode, HashMap<String,TextFileChange> map, HashMap<IFile,StringBuilder> hm,HashMap<IFile,MyPropertyFileDocumentModel> pfdmHM)throws CoreException{
		if(sub==null){			
			IFile file = null;
			String fileName = null;
			
			for(MLRes ml:simpchnResList){
				if(resId.equals(ml.getResID())){
					fileName = ml.getResFileName();
					break;
				}
			}
			if(fileName!=null){
				file = project.getFile((new StringBuilder(folder.getFullPath().segment(1))).append("/resources/" + ProjConstants.LANG + "/").append(langCode).append("/").append(folder.getFullPath().segment(1)+"_nodes/").append(fileName).toString());
				String key = file.getLocation().toOSString();
				TextFileChange tfChange = (TextFileChange)map.get(key);
				if (tfChange == null){
					tfChange = new TextFileChange("修改多语资源", file);
					map.put(key, tfChange);
				}
				MyPropertyFileDocumentModel model = (MyPropertyFileDocumentModel)pfdmHM.get(file);
				if (model == null){
					model = new MyPropertyFileDocumentModel(tfChange.getCurrentDocument(new NullProgressMonitor()));
					pfdmHM.put(file, model);
				}
				DeleteEdit edit = model.remove(resId);
				TextChangeCompatibility.addTextEdit(tfChange, (new StringBuilder("修改多语资源")).append(resId).toString(), edit);
			}
		}
		else{
		IFile file = getMLResfile(langCode, sub);
		if (oldValue != null && !oldValue.equals(newValue)){
			String key = file.getLocation().toOSString();
			TextFileChange tfChange = (TextFileChange)map.get(key);
			if (tfChange == null){
				tfChange = new TextFileChange("修改多语资源", file);
				map.put(key, tfChange);
			}
			MyPropertyFileDocumentModel model = (MyPropertyFileDocumentModel)pfdmHM.get(file);
			if (model == null){
				model = new MyPropertyFileDocumentModel(tfChange.getCurrentDocument(new NullProgressMonitor()));
				pfdmHM.put(file, model);
			}
			KeyValuePair oldKVP = new KeyValuePair(resId, Helper.unwindEscapeChars(oldValue));
			KeyValuePair newKVP = new KeyValuePair(resId, Helper.unwindEscapeChars(newValue));
			if(sub.getState()==7){
				InsertEdit edit = model.insert(newKVP);
				TextChangeCompatibility.addTextEdit(tfChange, (new StringBuilder("修改多语资源")).append(resId).toString(), edit);
			}
				
			else{
				ReplaceEdit edit = model.replace(oldKVP, newKVP);
				TextChangeCompatibility.addTextEdit(tfChange, (new StringBuilder("修改多语资源")).append(resId).toString(), edit);
			}
		} 
		else if (file.exists()){
			String key = file.getLocation().toOSString();
			TextFileChange tfChange = (TextFileChange)map.get(key);
			if (tfChange == null){
				tfChange = new TextFileChange("修改多语资源", file);
				map.put(key, tfChange);
			}
			MyPropertyFileDocumentModel model = (MyPropertyFileDocumentModel)pfdmHM.get(file);
			if (model == null){
				model = new MyPropertyFileDocumentModel(tfChange.getCurrentDocument(new NullProgressMonitor()));
				pfdmHM.put(file, model);
			}
			KeyValuePair newKVP = new KeyValuePair(resId, Helper.unwindEscapeChars(newValue));
			org.eclipse.text.edits.InsertEdit edit = model.insert(newKVP);
			TextChangeCompatibility.addTextEdit(tfChange, (new StringBuilder("插入多语资源")).append(resId).toString(), edit);
		} 
		else{
			String LineDelimiter = Helper.getLineDelimiterPreference(project);
			StringBuilder sb = (StringBuilder)hm.get(file);
			if (sb == null){
				sb = new StringBuilder();
				hm.put(file, sb);
			}
			sb.append(resId).append("=").append(Helper.unwindEscapeChars(newValue)).append(LineDelimiter);
		}
		}
	}

	/**
	 * 取资源文件
	 * 
	 * @param langCode
	 * @param sub
	 * @return
	 */
	private IFile getMLResfile(String langCode, MLResSubstitution sub){
		IFile file = null;
		MLRes res = sub.getNotNullResByLangCode(langCode);
//		if (res != null)
//			file = project.getFile((new StringBuilder(getResouceHomePath())).append("/"+ProjConstants.LANG+"/").append(langCode).append("/").append(sub.getLangModule()).append("/").append(res.getResFileName()).toString());
//		else{
//			file =project.getFile((new StringBuilder(getResouceHomePath())).append("/"+ProjConstants.LANG+"/").append(langCode).append("/").append(sub.getLangModule()).append("/").append(sub.getResFileName()).toString());
//		}
		file =project.getFile((new StringBuilder(getResouceHomePath())).append("/"+ProjConstants.LANG+"/").append(langCode).append("/").append(sub.getLangModule()).append("/").append(sub.getResFileName()).toString());
		if (file != null && file.exists())
			try{
				if ("simpchn".equals(langCode) && !file.getCharset().equalsIgnoreCase("UTF-16"))
					file.setCharset("UTF-16", new NullProgressMonitor());
//NO_EN				
				else
				if ("tradchn".equals(langCode)&& !file.getCharset().equalsIgnoreCase("UTF-16"))
					file.setCharset("UTF-16", new NullProgressMonitor());
				else
				if ("english".equals(langCode) && !file.getCharset().equalsIgnoreCase("UTF-16"))
					file.setCharset("UTF-16", new NullProgressMonitor());
			}
			catch (Exception e){
				Activator.getDefault().logError(e.getMessage(), e);
			}
		return file;
	}
	
	/***
	 * 返回s中的汉字
	 * @param s
	 * @return
	 */
	private String getSimpchn(String s){
		StringBuffer sb = new StringBuffer();
		String temp;
		boolean begin = false;
		int count = 0;
		for (int i = 0 ; i<s.length(); i++){
			temp = String.valueOf(s.charAt(i));
			if (temp.getBytes().length == 2){
				sb.append(temp);
				if (!begin)
					begin=true;
			}
			else if (begin){
				break;
			}
		}
		return sb.toString();
	}
	/***
	 * 返回s中的汉字并标识是否可以修复
	 * @param s
	 * @return
	 */
	private Map<String,String> getChin(String s,boolean flag){
		StringBuffer sb = new StringBuffer();
		Map<String,String> valueMap = new HashMap<String, String>();
//		String temp;
//		int count = 0;
//		boolean sr = false;
//		boolean fl = false;
//		boolean descFlag = false;
//		boolean tipFlag = false;
		int descIndex = -1;
		int tipIndex = -1;
		int textIndex = -1;
		String value = null;
		if(s.indexOf("description")>-1){
//			descIndex = s.indexOf("description")+13;
			descIndex = s.indexOf("description")+11;
			value = getNeedTextValue(descIndex, s);
			if(!value.startsWith("&")&&value.getBytes().length!=1)
				valueMap.put("description", value);
		}
		if(s.indexOf("tip")>-1){
//			tipIndex = s.indexOf("tip")+5;
			tipIndex = s.indexOf("tip")+3;
			value = getNeedTextValue(tipIndex, s);
			if(!value.startsWith("&")&&value.getBytes().length!=1)
				valueMap.put("tip", value);
			
		}
		if(s.indexOf("caption")>-1){
			textIndex = s.indexOf("caption")+7;
		}
		if(s.indexOf("text")>-1){
			textIndex = s.indexOf("text")+4;
		}
		if(s.indexOf("title")>-1){
			textIndex = s.indexOf("title")+5;
		}
		if(textIndex>-1){
			value = getNeedTextValue(textIndex, s);
			if(!value.startsWith("&")&&value.getBytes().length!=1)
				valueMap.put("text", value);
		}
			
//		if(s.indexOf("]>")>-1||s.indexOf("->")>-1) sr = true;
		
		
//		for (int i = 0 ; i<s.length(); i++){
//			temp = String.valueOf(s.charAt(i));
//			if(i==descIndex)
//				descFlag = true;
//			if(i==tipIndex)
//				tipFlag = true;
//			if (temp.getBytes().length == 2&&flag){
//				sb.append(temp);
//				fl = true;
//			}
//			if(temp.equals("\"")&&fl){
//				fl = false;
//				if(descFlag){
//					valueMap.put("description", sb.toString());
//					descFlag = false;
//				}
//				else if(tipFlag){
//					valueMap.put("tip", sb.toString());
//					tipFlag = false;
//				}
//				else
//					valueMap.put("text", sb.toString());
//				sb = new StringBuffer();
//			}
//			if(temp.equals("<")&&s.indexOf("<!")!=i) flag = true;
//			if((temp.equals(">")&& !sr)||(temp.equals(">")&&sr&&s.indexOf("]>")+1!=i&&s.indexOf("->")!=i)) flag = false;
//		}
//		this.flag = flag;
		
		return valueMap;
	}
	/**
//	 * 返回指定位置之后双引号内的字符串
	 * @param index
	 * @param content
	 * @return
	 */
	private String getNeedTextValue(int index, String content){
		String value = null;
		String s1 = content.substring(index);
		if(s1.replace(" ", "").startsWith("=\"")){
			String s2 = s1.substring(s1.indexOf("\"")+1);
			value = s2.substring(0,s2.indexOf("\""));
		}
		else value = "";
		return value;
	}
	
//	private String getSubValue(MLResSubstitution sub, String langCode){
//		String value = sub.getValue();
//		if (langCode.equals("english"))
//			value = sub.getEnglishValue();
//		else
//		if (langCode.equals("tradchn"))
//			value = sub.getTwValue();
//		return value;
//	}

	public IFile getResBoundleFile(String langCode,String resModuleName,String resFileName){
		return Helper.getResBoundleFile(project, getResouceHomePath(), langCode, resModuleName, resFileName);
	}

	public String getName(){
			return (new StringBuilder("LFW多语资源外部化:")).toString();
	}

//	public String getResFileName(){
//		return resFileName;
//	}
//
//	public void setResFileName(String resFileName){
//		this.resFileName = resFileName;
//	}

//	public String getResModuleName(){
//		return resModuleName;
//	}

//	public void setResModuleName(String resModuleName){
//		this.resModuleName = resModuleName;
//		//TODO 可能不能删除
//		refreshLangModule();
//		updateSubPostion();
//		page2.refreshSoruseView();
//	}

	public static MLRRefactoring create(IProject project){
			return new MLRRefactoring(project);
	}
	public static MLRRefactoring create(IFolder folder){
		return new MLRRefactoring(folder);
}
	public MLResSubstitution[] getSubstitutions(){
		return substitutions;
	}

	public String getPrefix(){
		if (currentPageNode != null)
			return currentPageNode.getPrefix();
		return null;
	}

	public void setPrefix(String prefix){
		if (currentPageNode != null){
			String old = currentPageNode.getPrefix();
			currentPageNode.setPrefix(prefix);
			if (!old.equals(prefix)){
				refreshPrefix();
				updateSubPostion(null);
				page2.refreshSoruseView();
			}
		}
			
		
	}

	public boolean isSourceFileChanged() {
		return sourceFileChanged;
	}

	public void setSourceFileChanged(boolean sourceFileChanged) {
		this.sourceFileChanged = sourceFileChanged;
	}

	public void setPage2(ExternalizeMLRWizardPage2 page2) {
		this.page2 = page2;
	}
	
	public IProject getProject(){
		return project;
	}
	public IFolder getFolder(){
		return folder;
	}
	public void setFolder(IFolder folder){
		this.folder = folder;
	}

	public PageNode getCurrentPageNode() {
		return currentPageNode;
	}

	public void setCurrentPageNode(PageNode currentPageNode) {
		this.currentPageNode = currentPageNode;
	}

	public Map<String, String> getContents() {
		return contents;
	}

	public void setContents(Map<String, String> contents) {
		this.contents = contents;
	}

	public Map<String, String> getOldContents() {
		return oldContents;
	}

	public void setOldContents(Map<String, String> oldContents) {
		this.oldContents = oldContents;
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public boolean isCleanRes() {
		return cleanRes;
	}
	public void setCleanRes(boolean cleanRes) {
		this.cleanRes = cleanRes;
	}
	public Map<String, List<String>> getNoChinValueMap() {
		return noChinValueMap;
	}
	public void setNoChinValueMap(Map<String, List<String>> noChinValueMap) {
		this.noChinValueMap = noChinValueMap;
	}
	
	
	
	
//	public void savePersistentProperty(){
//		IResource res = null;
//		if (cu != null)
//			res = cu.getResource();
//		if (xmlFile!=null)
//			res = xmlFile;
//		try{
//			res.setPersistentProperty(moduleNameQN, getResModuleName());
//			res.setPersistentProperty(fileNameQN, getResFileName());
//		}
//		catch (CoreException e){
//			e.printStackTrace();
//		}
//	}
//
}
