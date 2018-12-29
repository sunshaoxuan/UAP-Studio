package nc.uap.lfw.perspective.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.rule.CodePackPrefixDialog;
import nc.uap.lfw.perspective.rule.CodeRuleChecker;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;

public class CodePackPrefixAction extends Action{
	public CodePackPrefixAction(){
		super(M_perspective.CodePackPrefixAction_0);
	}
	public void run(){
		CodePackPrefixDialog dialog = new CodePackPrefixDialog(Display.getCurrent().getActiveShell(), M_perspective.CodePackPrefixAction_1);
		if(dialog.open() == IDialogConstants.OK_ID){
			String codePrefix = dialog.getCodePrefix();
			IProject proj = LFWPersTool.getCurrentProject();
			LfwCommonTool.updateModuleProperty(proj, CodeRuleChecker.PACK_PREFIX, codePrefix);
		}
	}
}
