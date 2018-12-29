package nc.uap.lfw.perspective.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.wizards.NewBcpModuleWizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;

public class NewBcpModuleAction extends Action{
	public NewBcpModuleAction(){
		super(M_perspective.NewBcpModuleAction_0);
	}
	public void run(){
		IProject project = LFWPersTool.getCurrentProject();		
		WizardDialog newBcpWizard = new WizardDialog(null, new NewBcpModuleWizard(project));
		if(newBcpWizard.open()==IDialogConstants.OK_ID){
			LFWExplorerTreeView.getLFWExploerTreeView(null).refreshTree();			
		}
	}
}
