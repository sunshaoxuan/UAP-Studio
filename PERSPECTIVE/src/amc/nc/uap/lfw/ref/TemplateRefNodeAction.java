package nc.uap.lfw.ref;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardDialog;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.template.NewTempleteWindowWizard;

public class TemplateRefNodeAction extends NodeAction{ 
	
	LFWDirtoryTreeItem parentItem = null;
	public TemplateRefNodeAction(LFWDirtoryTreeItem treeItem){
		super(M_editor.TemplateRefNodeAction_0, M_editor.TemplateRefNodeAction_0);
		this.parentItem = treeItem;
	}
	public void run() {
		//打开模板创建向导
//		WizardDialog tempDialog = new WizardDialog(null, new NewTempleteWindowWizard());
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		WizardDialog tempDialog = new WizardDialog(null, new NewRefWizard(parentItem));
		tempDialog.open();
		
	}

}
