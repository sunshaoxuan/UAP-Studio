package nc.lfw.editor.common.extend;

import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.lang.M_lfw_core;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * É¾³ýÀ©Õ¹ÊôÐÔ²Ù×÷
 * @author guoweic
 *
 */
public class DelExtendAttributeAction extends Action {

	private ExtendAttributesView view = null;

	public DelExtendAttributeAction(ExtendAttributesView view) {
		setText(M_lfw_core.DelExtendAttributeAction_0);
		this.view = view;
	}

	
	public void run() {
		if (MessageDialog.openConfirm(null, M_lfw_core.DelExtendAttributeAction_1, M_lfw_core.DelExtendAttributeAction_2))
			delAttribute();
	}
	
	private void delAttribute() {
		IStructuredSelection selection = (IStructuredSelection) view.getTv().getSelection();
		ExtAttribute attr = (ExtAttribute) selection.getFirstElement();
		view.deleteExtendAttribute(attr);
	}

}
