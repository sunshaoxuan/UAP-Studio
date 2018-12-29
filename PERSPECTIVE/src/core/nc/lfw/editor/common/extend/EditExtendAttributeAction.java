package nc.lfw.editor.common.extend;

import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.lang.M_lfw_component;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

/**
 * ±à¼­À©Õ¹ÊôÐÔ²Ù×÷
 * 
 * @author guoweic
 *
 */
public class EditExtendAttributeAction extends Action {
	
	private ExtendAttributesView view = null;
	
	public EditExtendAttributeAction(ExtendAttributesView view) {
		super(M_lfw_component.EditExtendAttributeAction_0);
		this.view = view;
	}
	
	public void run() {
		createEvents();
	}
	
	public void createEvents() {
		IStructuredSelection selection = (IStructuredSelection) view.getTv().getSelection();
		ExtAttribute attribute = (ExtAttribute) selection.getFirstElement();
		Shell shell = new Shell();
		ExtendAttributeDialog dialog = new ExtendAttributeDialog(shell);
		
		dialog.initDialogArea(attribute.getKey(), attribute.getValue() == null ? "" : attribute.getValue().toString(), attribute.getDesc(), view.getAttributeList()); //$NON-NLS-1$
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			attribute.setKey(dialog.getKey());
			attribute.setValue(dialog.getValue());
			attribute.setDesc(dialog.getDesc());
			view.saveExtendAttribute(attribute);
		}
	}

}
