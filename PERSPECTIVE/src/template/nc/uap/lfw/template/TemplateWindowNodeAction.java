/**
 * 
 */
package nc.uap.lfw.template;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.application.LFWApplicationTreeItem;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;

/**
 * @author guomq1
 * 2012-7-30
 */



public class TemplateWindowNodeAction extends NodeAction{ 
	
	public TemplateWindowNodeAction(){
		super(WEBPersConstants.TEMP_APPLICATION, WEBPersConstants.TEMP_APPLICATION);
	}
	public void run() {
		IProject proj = LFWPersTool.getCurrentProject();
		boolean prefixCheck = CodeRuleChecker.checkForProject(proj);
		if(!prefixCheck)
			return;
		//打开模板创建向导
		LFWApplicationTreeItem appItem = (LFWApplicationTreeItem)LFWPersTool.getCurrentTreeItem();
		if(appItem.getApplication().getWindowList().size()>0){
			MessageDialog.openError(null, M_template.TemplateWindowNodeAction_0, M_template.TemplateWindowNodeAction_1);
			return;
		}
		WizardDialog tempDialog = new TemplateWizardDialog(null, new NewTempleteWindowWizard());
		tempDialog.open();
		
	}

}
