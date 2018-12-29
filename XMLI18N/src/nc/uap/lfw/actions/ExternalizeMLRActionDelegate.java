package nc.uap.lfw.actions;

import nc.uap.lfw.lang.M_actions;
import nc.uap.lfw.tool.ProjConstants;
import nc.uap.lfw.wizard.ExternalizeMLRWizard;
import nc.uap.lfw.wizard.MLRRefactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.custom.BusyIndicator;

/**
 * XML多语资源外部化
 * 
 * @author dingrf
 * 
 */
public class ExternalizeMLRActionDelegate extends AbstractMLResRefactorActionDelegate {

	@Override
	public void run(IAction action) {
		final IProject project = getLfwProject(selection);
		final IFolder folder = getFolder(selection);
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
//				if (project != null) {
				if (folder != null) {
					try {
						MLRRefactoring refactoring = MLRRefactoring.create(project);
						refactoring.setFolder(folder);
						if (refactoring != null) {
							//打开XML多语资源向导
							RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(
									new ExternalizeMLRWizard(refactoring));
							op.run(getShell(),ProjConstants.MORE_LANGUAGE_RESOURCES);
						}
					} catch (Exception e) {
						MessageDialog.openError(getShell(), M_actions.ExternalizeMLRActionDelegate_0,
								(new StringBuilder()).append(e.getClass())
										.append(":").append(e.getMessage()) //$NON-NLS-1$
										.toString());
					}
				}
			}
		});
	}
}
