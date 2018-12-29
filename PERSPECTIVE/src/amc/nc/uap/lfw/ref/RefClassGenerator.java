package nc.uap.lfw.ref;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefFolderTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefInfoTreeItem;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.ref.model.RefSQLInfo;
import nc.uap.lfw.ref.model.RefTypeEnum;

public class RefClassGenerator {
	private LfwRefInfoVO refInfo = null;
	private RefSQLInfo sqlInfo = null;
	private Map<String, Object> context;
	private LFWDirtoryTreeItem parentItem;
	private IProject project;
	private File classFolder;
	private File fileFolder;
	
	public RefClassGenerator(Map<String, Object> context,LFWDirtoryTreeItem parentItem){
		this.context = context; 
		this.parentItem = parentItem;
	}
	
	public boolean generate(){
		if(context!=null&&context.containsKey(IConst.REF_INFO)){
			refInfo = (LfwRefInfoVO) context.get(IConst.REF_INFO);
		}
		if(context!=null&&context.containsKey(IConst.REF_SQLINFO)){
			sqlInfo = (RefSQLInfo) context.get(IConst.REF_SQLINFO);
		}
		
		if(genClassFile( IConst.MODEL_CLASSTYPE)
				&&genClassFile(IConst.CONTROL_CLASSTYPE)){
			String pk = LFWWfmConnector.addRefInfo(refInfo);
			refInfo.setPk_refinfo(pk);
			if(pk!=null){
				try {
					project.refreshLocal(2, null);
				} catch (CoreException e) {
					MainPlugin.getDefault().logError(e.getMessage(),e);
				}
				LFWRefFolderTreeItem refFolder = new LFWRefFolderTreeItem(parentItem, classFolder, refInfo, refInfo.getName());
				LFWRefInfoTreeItem refModelNode = new LFWRefInfoTreeItem(refFolder, IConst.MODEL_CLASSTYPE, project, fileFolder, refInfo, 
						refInfo.getRefclass().substring(refInfo.getRefclass().lastIndexOf(".")+1)+".java");
				LFWRefInfoTreeItem refControlNode = new LFWRefInfoTreeItem(refFolder, IConst.CONTROL_CLASSTYPE, project, fileFolder, refInfo, 
						refInfo.getRefControlClass().substring(refInfo.getRefControlClass().lastIndexOf(".")+1)+".java");
				parentItem.expandTree();
			}
			return true;
		}else{
			return false;
		}
	}
	
	private boolean genClassFile(String classtype){
		int type = refInfo.getRefType();
		String refclass = refInfo.getRefclass();
		String name = refInfo.getName();
		String refcontroller = refInfo.getRefControlClass();
		String module = LfwCommonTool.getProjectModuleName(LFWPersTool.getCurrentProject());
		MainPlugin.getDefault().logError(module);
		String bcp = parentItem.getBcp().getName();
		refInfo.setModuleName(module);
		refInfo.setReserv2(module);
		refInfo.setReserv3(bcp);

		project = LFWPersTool.getCurrentProject();
		String path = LFWAMCPersTool.getBCPProjectPath()+"/src/public/";
		classFolder = new File(path+IConst.PACKAGE.replace(".", "/")+"/"+name);
		fileFolder = new File(path);
		String classPath = null;
		String templateFix = null;
		String templateFileName = null;
		
		if(type==0){
			templateFix = RefTypeEnum.Tree.name();
		}else if(type==1){
			templateFix = RefTypeEnum.Grid.name();
		}else if(type==2){
			templateFix = RefTypeEnum.TreeGrid.name();
		}
		String className = null;
		String packagePath = null;
		if(classtype.equals(IConst.CONTROL_CLASSTYPE)){
			classPath = path+"/"+refcontroller.replace(".", "/")+".java";
			templateFileName = "templates/refinfo/RefController"+templateFix+".java.template";
			className = refcontroller.substring(refcontroller.lastIndexOf(".")+1);
			packagePath = refcontroller.substring(0,refcontroller.lastIndexOf("."));
		}else{
			classPath = path+"/"+refclass.replace(".", "/")+".java";
			templateFileName = "templates/refinfo/RefModel"+templateFix+".java.template";
			className = refclass.substring(refclass.lastIndexOf(".")+1);
			packagePath = refclass.substring(0,refclass.lastIndexOf("."));
		}
		
		File classFile = new File(classPath);
		if(classFile.exists()){
			MessageDialog.openError(null, "提示", "已有同名参照类存在，请重新输入参照类");
			return false;
		}else{
			String folderPath = classPath.substring(0,classPath.lastIndexOf("/"));
			File folder = new File(folderPath);
			if(!folder.exists()){
				folder.mkdirs();
			}		
			InputStream ins = null;
			try {
				classFile.createNewFile();
			
				ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(templateFileName);
				String content = FileUtilities.fetchFileContent(ins, "GBK").trim();
//				ins.close();
			
				content = replaceTemp(content,"package",packagePath);
				content = replaceTemp(content,"className",className);
				
				if(classtype.equals(IConst.CONTROL_CLASSTYPE)){
					if(type==0){
						content = replaceTemp(content,"tableName",sqlInfo.getTreeTableName());
						content = replaceTemp(content,"realTableName",sqlInfo.getTreeRealTableName());
					}else if(type==1){
						content = replaceTemp(content,"tableName",sqlInfo.getGridTableName());
						content = replaceTemp(content,"realTableName",sqlInfo.getGridRealTableName());
						content = replaceTemp(content,"orderByPart",sqlInfo.getOrderbyPart());
					}else if(type==2){
						content = replaceTemp(content,"tableName_tree",sqlInfo.getTreeTableName());
						content = replaceTemp(content,"realTableName_tree",sqlInfo.getTreeRealTableName());
						content = replaceTemp(content,"tableName_grid",sqlInfo.getGridTableName());
						content = replaceTemp(content,"realTableName_grid",sqlInfo.getGridRealTableName());
						content = replaceTemp(content,"orderByPart_grid",sqlInfo.getOrderbyPart());
					}
				}else{
					String controllerName = refcontroller.substring(refcontroller.lastIndexOf(".")+1);
					content = replaceTemp(content,"controllerFullName",refcontroller);
					content = replaceTemp(content,"controllerName",controllerName);
					
					if(type==0){
						content = replaceTemp(content,"pkField",sqlInfo.getClassPkField());
						content = replaceTemp(content,"codeField",sqlInfo.getClassCodeField());
						content = replaceTemp(content,"nameField",sqlInfo.getClassNameField());
						content = replaceTemp(content,"fatherField",sqlInfo.getFatherField());
						content = replaceTemp(content,"childField",sqlInfo.getChildField());
						content = replaceTemp(content,"fieldCodes",getStringArray(sqlInfo.getClassCodesField()));
						content = replaceTemp(content,"multiLanFieldCodes",getStringArray(sqlInfo.getClassMultiLanField()));
					}else if(type==1){
						content = replaceTemp(content,"pkField",sqlInfo.getPkField());
						content = replaceTemp(content,"codeField",sqlInfo.getCodeField());
						content = replaceTemp(content,"nameField",sqlInfo.getNameField());
						content = replaceTemp(content,"fieldNames",getStringArray(sqlInfo.getNamesField()));
						content = replaceTemp(content,"fieldCodes",getStringArray(sqlInfo.getCodesField()));
						content = replaceTemp(content,"hiddenFieldCodes",getStringArray(sqlInfo.getHiddenNamesField()));
						content = replaceTemp(content,"multiLanFieldCodes",getStringArray(sqlInfo.getMultiLanField()));
					}else if(type==2){
						content = replaceTemp(content,"pkField",sqlInfo.getPkField());
						content = replaceTemp(content,"codeField",sqlInfo.getCodeField());
						content = replaceTemp(content,"nameField",sqlInfo.getNameField());
						content = replaceTemp(content,"fieldNames",getStringArray(sqlInfo.getNamesField()));
						content = replaceTemp(content,"fieldCodes",getStringArray(sqlInfo.getCodesField()));
						content = replaceTemp(content,"hiddenFieldCodes",getStringArray(sqlInfo.getHiddenNamesField()));
						
						content = replaceTemp(content,"classPkField",sqlInfo.getClassPkField());
						content = replaceTemp(content,"docJoinField",sqlInfo.getDocjoinField());
						content = replaceTemp(content,"classCodeField",sqlInfo.getClassCodeField());
						content = replaceTemp(content,"classNameField",sqlInfo.getClassNameField());
						content = replaceTemp(content,"fatherField",sqlInfo.getFatherField());
						content = replaceTemp(content,"childField",sqlInfo.getChildField());
						content = replaceTemp(content,"classFieldCodes",getStringArray(sqlInfo.getClassCodesField()));
						content = replaceTemp(content,"multiLanFieldCodes",getStringArray(sqlInfo.getClassMultiLanField()));
					}
					
				}
				
				FileUtilities.saveFile(classFile, content, "GBK");
				project.refreshLocal(IProject.DEPTH_INFINITE, null);
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
				return false;
			}
			finally{
				try {				
					if(ins!=null)
						ins.close();
					}
				catch (IOException e) {
						MainPlugin.getDefault().logError(e.getMessage(),e);
					}
			}
		}
		return true;
		
	}
	
	/*
	 * 替换模板文件中的标签
	 */
	private static String replaceTemp(String content, String markersign,
			String replacecontent) {
		markersign = "${" + markersign + "}";
		String target = content;
		target = target.replace(markersign, replacecontent);
		return target.trim();
	}
	
	private static String getStringArray(String str){
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"");
		buffer.append(str.replaceAll(",", "\",\""));
		buffer.append("\"");
		return buffer.toString();
	}
}
