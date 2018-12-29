package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWRefInfoTreeItem;

public class CreateRefInfoAction extends NodeAction{
	
	LFWDirtoryTreeItem parentItem = null;
	public CreateRefInfoAction(LFWDirtoryTreeItem treeItem){
		super(M_editor.CreateRefInfoAction_0);
		this.parentItem = treeItem;
	}
	public void run(){
		createRefInfoNode();
	}
	public void createRefInfoNode(){
		RefInfoDialog dialog = new RefInfoDialog(M_editor.CreateRefInfoAction_0);
		if(dialog.open() ==IDialogConstants.OK_ID){
			try{
				String module = LfwCommonTool.getProjectModuleName(LFWPersTool.getCurrentProject());
				String bcp = parentItem.getBcp().getName();
				LfwRefInfoVO refInfo = new LfwRefInfoVO();
				String code = dialog.getRefCode();
				String name = dialog.getRefName();
				int type = dialog.getRefType();
				String refclass = dialog.getRefClass();
				refInfo.setCode(code);
				refInfo.setName(name);
				refInfo.setResid(code);
				refInfo.setResidPath("ref"); //$NON-NLS-1$
				refInfo.setRefType(type);
				refInfo.setRefclass(refclass);
				refInfo.setModuleName(module);
				refInfo.setReserv2(module);
				refInfo.setReserv3(bcp);
//				refInfo.setMetadataTypeName(metadataTypeName)
				IProject project =LFWPersTool.getCurrentProject();
				String path = LFWAMCPersTool.getBCPProjectPath()+"/src/public/"; //$NON-NLS-1$
				File fileFolder = new File(path);
				String classPath = path+"/"+refclass.replace(".", "/")+".java"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				File classFile = new File(classPath);
				if(classFile.exists()){
					MessageDialog.openError(null, M_editor.CreateRefInfoAction_1, M_editor.CreateRefInfoAction_2);
					return;
				}
				else{
					String folderPath = classPath.substring(0,classPath.lastIndexOf("/")); //$NON-NLS-1$
					File folder = new File(folderPath);
					if(!folder.exists()){
						folder.mkdirs();
					}					
					classFile.createNewFile();
					InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/refinfo/RefModel.java.template"); //$NON-NLS-1$
					String content = FileUtilities.fetchFileContent(ins, "GBK").trim(); //$NON-NLS-1$
					ins.close();

					if(type==0){
						String fullName = "nc.ui.bd.ref.AbstractRefTreeModel"; //$NON-NLS-1$
						String superClassName = "AbstractRefTreeModel"; //$NON-NLS-1$
						String className = refclass.substring(refclass.lastIndexOf(".")+1); //$NON-NLS-1$
						String packagePath = refclass.substring(0,refclass.lastIndexOf(".")); //$NON-NLS-1$
						content = replaceTemp(content,"package",packagePath); //$NON-NLS-1$
						content = replaceTemp(content,"classFullName",fullName); //$NON-NLS-1$
						content = replaceTemp(content,"className",className); //$NON-NLS-1$
						content = replaceTemp(content,"superClassName",superClassName); //$NON-NLS-1$
					}else if(type==1){
						String fullName = "nc.ui.bd.ref.AbstractRefModel"; //$NON-NLS-1$
						String superClassName = "AbstractRefModel"; //$NON-NLS-1$
						String className = refclass.substring(refclass.lastIndexOf(".")+1); //$NON-NLS-1$
						String packagePath = refclass.substring(0,refclass.lastIndexOf(".")); //$NON-NLS-1$
						content = replaceTemp(content,"package",packagePath); //$NON-NLS-1$
						content = replaceTemp(content,"classFullName",fullName); //$NON-NLS-1$
						content = replaceTemp(content,"className",className); //$NON-NLS-1$
						content = replaceTemp(content,"superClassName",superClassName); //$NON-NLS-1$
					}else if(type==2){
						String fullName = "nc.ui.bd.ref.AbstractRefGridTreeModel"; //$NON-NLS-1$
						String superClassName = "AbstractRefGridTreeModel"; //$NON-NLS-1$
						String className = refclass.substring(refclass.lastIndexOf(".")+1); //$NON-NLS-1$
						String packagePath = refclass.substring(0,refclass.lastIndexOf(".")); //$NON-NLS-1$
						content = replaceTemp(content,"package",packagePath); //$NON-NLS-1$
						content = replaceTemp(content,"classFullName",fullName); //$NON-NLS-1$
						content = replaceTemp(content,"className",className); //$NON-NLS-1$
						content = replaceTemp(content,"superClassName",superClassName); //$NON-NLS-1$
					}
					FileUtilities.saveFile(classFile, content, "GBK"); //$NON-NLS-1$
				}
				String pk = LFWWfmConnector.addRefInfo(refInfo);
				refInfo.setPk_refinfo(pk);
				if(pk!=null){
					project.refreshLocal(2, null);
					LFWRefInfoTreeItem refNode = new LFWRefInfoTreeItem(parentItem, project, fileFolder, refInfo, name);
					parentItem.expandTree();
				}
			}
			catch(Exception e){
				MainPlugin.getDefault().logError(e.getMessage());
			}
		}
	}
	/*
	 * 替换模板文件中的标签
	 */
	private String replaceTemp(String content, String markersign,
			String replacecontent) {
		markersign = "${" + markersign + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		String target = content;
		target = target.replace(markersign, replacecontent);
		return target.trim();
	}

	private  CompilationUnit getASTUnit(String content){
		CompilationUnit unit = null;
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(content.toCharArray());
			parser.setResolveBindings(true);
			unit = (CompilationUnit)parser.createAST(null);
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage());
		}
		return unit;
		
	}

}
