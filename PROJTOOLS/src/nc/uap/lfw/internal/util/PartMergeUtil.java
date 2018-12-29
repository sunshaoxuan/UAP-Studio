package nc.uap.lfw.internal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.common.XmlUtility;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.lang.M_internal;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * part合并到web.xml
 * 
 * @author dingrf
 *
 */

public class PartMergeUtil {

	private static String NC_HOME = ProjCoreUtility.getNcHomeFolderPath().toString();

	private static String HOTWEBS = NC_HOME + "/hotwebs"; //$NON-NLS-1$
	
	/* web.xml 中元素类型  */
	private static String LISTENER = "listener"; //$NON-NLS-1$
	
	private static String FILTER = "filter"; //$NON-NLS-1$
	
	private static String FILTER_MAPPING = "filter-mapping"; //$NON-NLS-1$
	
	private static String SERVLET = "servlet"; //$NON-NLS-1$
	
	private static String SERVLET_MAPPING = "servlet-mapping"; //$NON-NLS-1$
	
	/**
	 * 把part文件内容合并到web.xml
	 * 
	 * @param proj	当前part项目
	 * @param partFile	*.part文件
	 */
	public static void mergeWebXml(IProject proj,File partFile){
		String webName = getMainProjNameByPart(partFile.getName());
		if (webName == null) return;
//		String webName = partFile.getName().replace(".part", "");
		String webPath = HOTWEBS + "/" + webName + "/WEB-INF"; //$NON-NLS-1$ //$NON-NLS-2$
		File webFile = new File(webPath + "/web.xml"); //$NON-NLS-1$
		/* 如果不存在web.xml 不处理合并 */
		if (!webFile.exists()){
			return;
		}
//		String moduleName = LfwCommonTool.getModuleProperty(proj,"module.name");
		String moduleName = getModuleName(proj ,partFile);
		if (moduleName == null){
			WEBProjPlugin.getDefault().logInfo(M_internal.PartMergeUtil_0 + partFile.getPath() + M_internal.PartMergeUtil_1);
			return;
		}
		
		/* web.xml Buffer*/
 		StringBuffer sb = new StringBuffer();
 		String tempString = null;
 		
 		/* 找到part部分，进行删除 */
 		Boolean needDelete = false;
 		
 		/* 是否找到part部分*/
// 		Boolean findPart = false;
 		
 		Boolean havInsertListener = false;
 		
 		Boolean havInsertFilter = false;
 		
 		Boolean havInsertFilterMapping = false;
 		
 		Boolean havInsertServlet = false;
 		
 		Boolean havInsertServletMapping = false;
 		
 		String paramName = ""; //$NON-NLS-1$

 		BufferedReader reader = null;
		try {
			Document doc = null;
			try{
				doc = XmlUtility.getDocument(partFile);
			}catch(Exception e){
//	 			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
	 			WEBProjPlugin.getDefault().logInfo(M_internal.PartMergeUtil_2);
			}	
 			reader = new BufferedReader(new FileReader(webFile));
			while ((tempString = reader.readLine()) != null){
				if (tempString.indexOf("<param-name>modules</param-name>") != -1){ //$NON-NLS-1$
					paramName = "modules"; //$NON-NLS-1$
				}
				if (paramName.equals("modules")  && tempString.indexOf("<param-value>") != -1){ //$NON-NLS-1$ //$NON-NLS-2$
					String modules = tempString.replace("<param-value>", "").replace("</param-value>", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					if (("," + modules.trim()+",").indexOf("," + moduleName + ",") == -1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						modules =  modules + ","+moduleName; //$NON-NLS-1$
						sb.append("<param-value>"+ modules.trim()+ "</param-value>\n"); //$NON-NLS-1$ //$NON-NLS-2$
	 					continue;
					}
				}
				if (tempString.indexOf("</context-param>")!=-1){ //$NON-NLS-1$
					paramName = ""; //$NON-NLS-1$
				}
				/*删除原有part*/
				if (tempString.indexOf("<!--" + LISTENER + "_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = true;
				}
				else if (tempString.indexOf("<!--" + LISTENER + "_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = false;
					continue;
				}
				else if (tempString.indexOf("<!--" + FILTER + "_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = true;
				}
				else if (tempString.indexOf("<!--" + FILTER + "_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = false;
					continue;
				}
				else if (tempString.indexOf("<!--" + FILTER_MAPPING + "_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = true;
				}
				else if (tempString.indexOf("<!--" + FILTER_MAPPING + "_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = false;
					continue;
				}
				else if (tempString.indexOf("<!--" + SERVLET + "_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = true;
				}
				else if (tempString.indexOf("<!--" + SERVLET + "_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = false;
					continue;
				}
				else if (tempString.indexOf("<!--" + SERVLET_MAPPING + "_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = true;
				}
				else if (tempString.indexOf("<!--" + SERVLET_MAPPING + "_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					needDelete = false;
					continue;
				}
				
				/* 增加新的part信息 */
				if (tempString.indexOf("<filter>")!=-1 || tempString.indexOf("<!--" + FILTER + "_begin:")!=-1 ){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (!havInsertListener){
						addPartContent(sb,doc,moduleName,LISTENER);
						havInsertListener=true;
					}
				}
				else if (tempString.indexOf("<filter-mapping>")!=-1 || tempString.indexOf("<!--" + FILTER_MAPPING + "_begin:")!=-1 ){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (!havInsertListener){
						addPartContent(sb,doc,moduleName,LISTENER);
						havInsertListener=true;
					}
					if (!havInsertFilter){
						addPartContent(sb,doc,moduleName,FILTER);
						havInsertFilter=true;
					}
				}
				else if (tempString.indexOf("<servlet>")!=-1 || tempString.indexOf("<!--" + SERVLET + "_begin:")!=-1 ){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (!havInsertListener){
						addPartContent(sb,doc,moduleName,LISTENER);
						havInsertListener=true;
					}
					if (!havInsertFilter){
						addPartContent(sb,doc,moduleName,FILTER);
						havInsertFilter=true;
					}
					if (!havInsertFilterMapping){
						addPartContent(sb,doc,moduleName,FILTER_MAPPING);
						havInsertFilterMapping=true;
					}
				}
				else if (tempString.indexOf("<servlet-mapping>")!=-1 || tempString.indexOf("<!--" + SERVLET_MAPPING + "_begin:")!=-1 ){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if (!havInsertListener){
						addPartContent(sb,doc,moduleName,LISTENER);
						havInsertListener=true;
					}
					if (!havInsertFilter){
						addPartContent(sb,doc,moduleName,FILTER);
						havInsertFilter=true;
					}
					if (!havInsertFilterMapping){
						addPartContent(sb,doc,moduleName,FILTER_MAPPING);
						havInsertFilterMapping=true;
					}
					if (!havInsertServlet){
						addPartContent(sb,doc,moduleName,SERVLET);
						havInsertServlet=true;
					}
				}
				else if (tempString.indexOf("<session-config>")!=-1 || tempString.indexOf("<jsp-config>")!=-1  //$NON-NLS-1$ //$NON-NLS-2$
						|| tempString.indexOf("<welcome-file-list>")!=-1 || tempString.indexOf("</web-app>")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$
					if (!havInsertListener){
						addPartContent(sb,doc,moduleName,LISTENER);
						havInsertListener=true;
					}
					if (!havInsertFilter){
						addPartContent(sb,doc,moduleName,FILTER);
						havInsertFilter=true;
					}
					if (!havInsertFilterMapping){
						addPartContent(sb,doc,moduleName,FILTER_MAPPING);
						havInsertFilterMapping=true;
					}
					if (!havInsertServlet){
						addPartContent(sb,doc,moduleName,SERVLET);
						havInsertServlet=true;
					}
					if (!havInsertServletMapping){
						addPartContent(sb,doc,moduleName,SERVLET_MAPPING);
						havInsertServletMapping=true;
					}
				}
				
				if(!needDelete){
					sb.append(tempString + "\n"); //$NON-NLS-1$
				}
			}
			FileUtilities.saveFile(webPath + "/web.xml", sb.toString().getBytes()); //$NON-NLS-1$
 		}
 		catch(Exception e){
 			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
 		}finally{
 			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(), e);
			}
 		}
	}
	
	/**
	 * 根据part名称取所属主项目名称
	 * 
	 * @param partName   oainf.portal.part  ==> portal
	 * @return
	 */
	public static String getMainProjNameByPart(String partName){
		if (partName.endsWith(".part")){ //$NON-NLS-1$
			String fileName = partName.substring(0,partName.length() - 5);
			int pos = fileName.lastIndexOf("."); //$NON-NLS-1$
			if (pos == -1){
				WEBProjPlugin.getDefault().logError(M_internal.PartMergeUtil_3 + partName + M_internal.PartMergeUtil_4);
				return null;
			}
			else{
				fileName =  fileName.substring(pos + 1, fileName.length());
				return fileName;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据*.part文件路径，得到modlueName
	 * 
	 */
	private static String getModuleName(IProject proj, File partFile) {
		String[] bcpNames = LfwCommonTool.getBCPNames(proj);
		if (bcpNames == null || bcpNames.length == 0)
			return LfwCommonTool.getModuleProperty(proj,"module.name"); //$NON-NLS-1$
		else {
			String partPath = partFile.getPath();
			for (int i = 0; i < bcpNames.length; i++) {
				if (partPath.indexOf("/" + bcpNames[i] + "/web/WEB-INF/") > -1   //$NON-NLS-1$ //$NON-NLS-2$
					|| partPath.indexOf("\\" + bcpNames[i] + "\\web\\WEB-INF\\") > -1 ){ //$NON-NLS-1$ //$NON-NLS-2$
					return 	bcpNames[i];
				}
			}
		}
		return null;
	}

//	private static String getModuleNameByPartFilePath(String partFilePath) {
//		//  /NC_CS_OAINF/informationpublish/web/WEB-INF/portal.part
//		
//	}

	/**
	 * 合并主项目的所有从属项目
	 * 
	 * @param mainProj 主项目
	 */
	public static void mergeParts(IProject mainProj){
		//主项目路径
		String ctx = LfwCommonTool.getLfwProjectCtx(mainProj);
		/*得到所有lfw项目*/
		IProject[] projs = LfwCommonTool.getOpenedLfwProjects();
		for (int i = 0; i < projs.length; i++) {
			IProject proj = projs[i];
			String projPath = proj.getLocation().toString();
			String[] prePaths = null;
			if(LfwCommonTool.isBCPProject(proj)) {
				prePaths = LfwCommonTool.getBCPNames(proj);
			}
			else
				prePaths = new String[]{""}; //$NON-NLS-1$
			for (int j = 0; j < prePaths.length; j++) {
				String path = prePaths[j];
				if(!path.equals("")) //$NON-NLS-1$
					path = "/" + path; //$NON-NLS-1$
				File partFile = new File(projPath + path + "/web/WEB-INF/" + ctx + ".part"); //$NON-NLS-1$ //$NON-NLS-2$
				if(partFile.exists()){
					mergeWebXml(proj,partFile);
				}
			}
		}
	}
	
	/**
	 * 删除*.part时，在web.xml中删除相关内容
	 * 
	 * @param proj
	 * @param webName
	 */
	public static void deletePart(IProject proj,String webName){
		String webPath = HOTWEBS + "/" + webName + "/WEB-INF"; //$NON-NLS-1$ //$NON-NLS-2$
		File webFile = new File(webPath + "/web.xml"); //$NON-NLS-1$
		/* 如果不存在web.xml 不处理 */
		if (!webFile.exists()){
			return;
		}
		String moduleName = LfwCommonTool.getModuleProperty(proj,"module.name"); //$NON-NLS-1$
		/* web.xml Buffer*/
 		StringBuffer sb = new StringBuffer();
 		String tempString = null;
 		/* 找到part部分，进行删除 */
 		Boolean needDelete = false;
 		String paramName = ""; //$NON-NLS-1$
 		BufferedReader reader = null;
		try {
 			reader = new BufferedReader(new FileReader(webFile));
			while ((tempString = reader.readLine()) != null){
				/*处理 <param-name>modules</param-name>*/
				if (tempString.indexOf("<param-name>modules</param-name>") != -1){ //$NON-NLS-1$
					paramName = "modules"; //$NON-NLS-1$
				}
				else if (paramName.equals("modules")  && tempString.indexOf("<param-value>") != -1){ //$NON-NLS-1$ //$NON-NLS-2$
					String modules = tempString.replace("<param-value>", "").replace("</param-value>", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					String newModules = ""; //$NON-NLS-1$
					if (modules.indexOf(moduleName+",")!= -1){ //$NON-NLS-1$
						newModules = modules.replace(moduleName+",", ""); //$NON-NLS-1$ //$NON-NLS-2$
					}
					else if (modules.indexOf(moduleName)!= -1){
						newModules = modules.replace(moduleName, ""); //$NON-NLS-1$
					}
					sb.append("<param-value>" + newModules + "</param-value>\n"); //$NON-NLS-1$ //$NON-NLS-2$
					continue;
				}
				else if (tempString.indexOf("</context-param>")!=-1){ //$NON-NLS-1$
					paramName = ""; //$NON-NLS-1$
				}
				
				/*删除part*/
				if (tempString.indexOf("_begin:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$
					needDelete = true;
				}
				else if (tempString.indexOf("_end:"+ moduleName +"-->")!=-1){ //$NON-NLS-1$ //$NON-NLS-2$
					needDelete = false;
					continue;
				}
				if(!needDelete){
					sb.append(tempString + "\n"); //$NON-NLS-1$
				}
			}
			FileUtilities.saveFile(webPath + "/web.xml", sb.toString().getBytes()); //$NON-NLS-1$
 		}
 		catch(Exception e){
 			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
 		}finally{
 			try {
 				if (reader != null)
 					reader.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
 		}
				
	}	

	/**
	 * 往web.xml中增加内容
	 * 
	 * @param webBuffer
	 * @param doc
	 * @param moduleName
	 * @param type
	 */
	private static void addPartContent(StringBuffer webBuffer, Document doc, String moduleName, String type) {
		if (doc == null) return;
		NodeList nodeList = doc.getElementsByTagName(type);
		if (nodeList.getLength()<1)
			return;
		webBuffer.append("<!--"+ type +"_begin:"+ moduleName +"-->\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		for (int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
 			Writer wr = new StringWriter();
 			XmlUtility.printDOMTree(wr, node, 0, "UTF-8"); //$NON-NLS-1$
			webBuffer.append(wr.toString() + "\n"); //$NON-NLS-1$
		}	
		webBuffer.append("<!--"+ type +"_end:"+ moduleName +"-->\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
}
