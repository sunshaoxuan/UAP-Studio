package nc.uap.portal.webconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.portal.core.PortalTools;
import nc.uap.portal.freemarker.Editor;
import nc.uap.portal.perspective.PortalPlugin;

import nc.lfw.editor.common.tools.LFWPersTool;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * Web配置
 * 
 * @author dingrf
 *
 */
public class WebConfigEditor extends MultiPageEditorPart {

	private IEditorSite site;
	
	private IEditorInput input;
	
	protected void setInput(IEditorInput input) {
		super.setInput(input);
	}
	
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
		
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		IFile file = ((Editor)getEditor(0)).getFile();
		
		File fromFile = new File(file.getLocation().toString());
		BufferedReader reader = null;
		try {
			IProject iPortalProj = PortalTools.getPortalProject();
			String toFilePath = iPortalProj.getLocation().toString() + "/web/WEB-INF/web.xml";
			File toFile = new File(toFilePath);
//			IFile  toiFile =  iPortalProj.getFile("/web/WEB-INF/web.xml");
//			String toFileText = FileTools.getFileText(toiFile);
			
			
			
//	 		BufferedReader reader = null;
	 		StringBuffer sb = new StringBuffer();
	 		String tempString = null;
	 		
	 		/* 找到part部分，进行删除 */
	 		Boolean needDelete = false;
	 		
	 		/* 已找到part部分*/
	 		Boolean findPart = false;
	 		
	 		String paramName = "";
 			reader = new BufferedReader(new FileReader(toFile));
 			int line = 1;
 			while ((tempString = reader.readLine()) != null){
 				
 				/*处理 <param-name>modules</param-name>*/
 				if (tempString.indexOf("<param-name>modules</param-name>") != -1){
 					paramName = "modules";
 				}
 				if (paramName.equals("modules")  && tempString.indexOf("<param-value>") != -1){
 					String modules = tempString.replace("<param-value>", "").replace("</param-value>", "");
 					if ((modules.trim()+",").indexOf(projectModuleName+",") == -1){
 						modules =  modules + ","+projectModuleName;
 						sb.append("<param-value>"+ modules.trim()+ "</param-value>\n");
 	 					line++;
 	 					continue;
 					}
 				}
 				if (tempString.indexOf("</context-param>")!=-1){
 					paramName = "";
 				}
 				
 				/*处理 part*/
 				if (tempString.indexOf("<!--partbegin:"+ projectModuleName +"-->")!=-1){
 					needDelete = true;
 					findPart = true;
 					addPartContent(sb,fromFile);
 				}
 				else if (tempString.indexOf("<!--partend:"+ projectModuleName +"-->")!=-1){
 					needDelete = false;
 					line++;
 					continue;
 				}
 				/* 到结尾时，有找到partbegin但没有找到partend*/
 				if (needDelete && tempString.indexOf("</web-app>")!=-1){
 					throw new RuntimeException("解释web.xml文件错误!");
 				}
 				
 				/*到结尾 没有找到part,新增part*/
 				if (!findPart &&  tempString.indexOf("</web-app>")!=-1){
 					addPartContent(sb,fromFile);
 				}
 				
 				if(!needDelete){
 					sb.append(tempString + "\n");
 				}
 				line++;
 			}
 			
 			FileUtilities.saveFile(toFile, sb.toString());
 			
 			/* 改写 modules context-param */
// 			Document doc = XMLUtil.getDocument(sb.toString());
// 			NodeList paramList = doc.getElementsByTagName("context-param");
// 			for(int i = 0 ; i< paramList.getLength() - 1; i++){
// 				Node node = paramList.item(i);
// 				Node paramName = XMLUtil.getChildNodeOf(node, "param-name");
// 				if (paramName != null && XMLUtil.getValueOf(paramName).equals("modules")){
// 					Node paramValue = XMLUtil.getChildNodeOf(node, "param-value");
// 					String modules = XMLUtil.getValueOf(paramValue);
// 					if ((modules+",").indexOf(projectModuleName) == -1){
// 						modules =  modules + ","+projectModuleName;
// 						paramValue.setNodeValue(modules);
// 					}
// 				}
// 			}
// 			
// 			Writer wr = new StringWriter();
// 			XMLUtil.printDOMTree(wr, doc, 0, "UTF-8");
// 			FileTools.saveToFile(toFile, wr.toString());
 		}
 		catch(Exception e){
			PortalPlugin.getDefault().logError(e);
			throw new RuntimeException("合并配置文件失败!");
 		}finally{
 			try {
				reader.close();
			} catch (IOException e) {
			}
 		}
	}
	

	private void addPartContent(StringBuffer webBuffer, File fromFile) {
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		webBuffer.append("<!--partbegin:"+ projectModuleName +"-->\n");

 		String tempString = null;
 		Boolean needCopy = false;
 		BufferedReader reader = null;
 		try{
 			reader = new BufferedReader(new FileReader(fromFile));
 			int line = 1;
 			while ((tempString = reader.readLine()) != null){
 				if (tempString.indexOf("<listener>")!=-1 || tempString.indexOf("<filter>")!=-1 ||
 						tempString.indexOf("<filter-mapping>")!=-1 || tempString.indexOf("<servlet>")!=-1 ||
 						tempString.indexOf("<servlet-mapping>")!=-1 ){
 					needCopy = true;
 				}
 				else if (tempString.indexOf("</listener>")!=-1 || tempString.indexOf("</filter>")!=-1 ||
 						tempString.indexOf("</filter-mapping>")!=-1 || tempString.indexOf("</servlet>")!=-1 ||
 						tempString.indexOf("</servlet-mapping>")!=-1 ){
 					webBuffer.append(tempString + "\n");
 					needCopy = false;
 					line++;
 					continue;
 				}
 				if(needCopy){
 					webBuffer.append(tempString + "\n");
 				}
 				line++;
 			}
 			webBuffer.append("<!--partend:"+ projectModuleName +"-->\n");
 		}
 		catch(Exception e){
 			PortalPlugin.getDefault().logError(e.getMessage(), e);
 			throw new RuntimeException("合并配置文件失败!");
 		}finally{
 			try {
				reader.close();
			} catch (IOException e) {
			}
 		}
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.site = site;
		this.input = input;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public IEditorInput getEditorInput() {
		return input;
	}

	public IEditorSite getEditorSite() {
		return site;
	}

	public IWorkbenchPartSite getSite() {
		return site;
	}

	@Override
	protected void createPages() {
		IFile file = ((FileEditorInput)input).getFile();
		createPage(file);

	}
	
	private void createPage(IFile file) {
		try {
			int index = addPage(new Editor(), new FileEditorInput(file));
			setPageText(index, file.getName()); 
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
				"Error creating nested text editor",null,e.getStatus()); //$NON-NLS-1$
		}
	}

}
