package nc.uap.lfw.editor.extNode;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.multilang.ExternalizeMLRWizard;
import nc.uap.lfw.multilang.MLRRefactoring;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.tool.ProjConstants;

public class JavaLangAction extends NodeAction{

	public JavaLangAction(){
		super(WEBPersConstants.LANG_CREATE);
	}
	public void run() {
		openLangTool();
	}
	public void openLangTool(){
		LFWDirtoryTreeItem treeItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
		String path = treeItem.getFile().getPath();
		String bcpPath = LFWPersTool.getBcpPath(treeItem);
		String bcp = null;
		String resourceName = null;
		IProject project = LFWPersTool.getCurrentProject();
		if(bcpPath!=null){
			bcp = bcpPath.substring(bcpPath.lastIndexOf("\\"));
			resourceName = LfwCommonTool.getProjectModuleName(project)+"_"+bcp.substring(1);
		}
		else{
			bcp = "\\src";
			resourceName = LfwCommonTool.getProjectModuleName(project);
		}
		
		IFolder folder = project.getFolder(path.substring(path.lastIndexOf(bcp+"\\")));
		if (folder != null) {
			try {
				MLRRefactoring refactoring = MLRRefactoring.create(project);
				refactoring.setResFileName(resourceName);
				refactoring.setFolder(folder);
				if (refactoring != null) {
					//打开XML多语资源向导
					RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(
							new ExternalizeMLRWizard(refactoring));
					Shell shell = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
					op.run(shell,ProjConstants.MORE_LANGUAGE_RESOURCES);
				}
			} catch (Exception e) {
				MainPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}
}
