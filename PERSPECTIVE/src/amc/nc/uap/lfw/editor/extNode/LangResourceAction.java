package nc.uap.lfw.editor.extNode;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.swt.widgets.Shell;


import nc.lfw.editor.common.tools.LFWPersTool;
//import nc.uap.lfw.actions.ExternalizeMLRActionDelegate;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.tool.ProjConstants;
import nc.uap.lfw.wizard.ExternalizeMLRWizard;
import nc.uap.lfw.wizard.MLRRefactoring;

public class LangResourceAction extends NodeAction {

	public LangResourceAction() {
		super(WEBPersConstants.LANG_CREATE);
	}

	public void run() {
		openLangTool();
	}
	public void openLangTool(){
		LFWDirtoryTreeItem treeItem = (LFWDirtoryTreeItem)LFWPersTool.getCurrentTreeItem();
		String path = treeItem.getFile().getPath();
//		ICompilationUnit unit = getCompilationUnit(this.selection);
		String bcpPath = LFWPersTool.getBcpPath(treeItem);
		String bcp = null;
		if(bcpPath!=null){
			bcp = bcpPath.substring(bcpPath.lastIndexOf("\\"));
		}
		else bcp = "\\web";
		IProject project = LFWPersTool.getCurrentProject();
//		IPath ipath = project.getf
		IFolder folder = project.getFolder(path.substring(path.lastIndexOf(bcp+"\\")));
//		project.getf
		if (folder != null) {
			try {
				MLRRefactoring refactoring = MLRRefactoring.create(project);
				refactoring.setFolder(folder);
				if (refactoring != null) {
					//打开XML多语资源向导
					RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(
							new ExternalizeMLRWizard(refactoring));
					Shell shell = WEBPersPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
					op.run(shell,ProjConstants.MORE_LANGUAGE_RESOURCES);
				}
			} catch (Exception e) {
				MessageDialog.openError(null, "err",
						(new StringBuilder()).append(e.getClass())
								.append(":").append(e.getMessage())
								.toString());
			}
		}
	}
}
