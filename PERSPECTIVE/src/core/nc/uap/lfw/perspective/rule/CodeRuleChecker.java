package nc.uap.lfw.perspective.rule;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.lang.M_perspective;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class CodeRuleChecker {
	public static final String PACK_PREFIX = "pack.prefix"; //$NON-NLS-1$

	public static boolean checkForProject(IProject proj){
		String prefix = LfwCommonTool.getModuleProperty(proj, PACK_PREFIX);
		if(prefix == null || prefix.equals("")){ //$NON-NLS-1$
			prefix = showCodePackDialog(proj);
			if(prefix == null || prefix.equals("")){ //$NON-NLS-1$
				MessageDialog.openError(null, "error", M_perspective.CodeRuleChecker_0); //$NON-NLS-1$
				return false;
			}
		}
		return true;
	}

	private static String showCodePackDialog(IProject proj) {
		CodePackPrefixDialog checker = new CodePackPrefixDialog(Display.getCurrent().getActiveShell(), M_perspective.CodeRuleChecker_1);
		if(checker.open() == Dialog.OK){
			String prefix = checker.getCodePrefix();			
			LfwCommonTool.updateModuleProperty(proj, PACK_PREFIX, prefix);
			return prefix;
		}
		return null;
	}
}
