package nc.uap.portal.portlets.page;

import nc.uap.portal.container.om.Supports;
import nc.uap.portal.portlets.dialog.SupportsPortletModeDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author dingrf
 * 
 */
public class SupportsPortletModeCellEditor extends DialogCellEditor {

	private TreeViewer tv;
	

	public SupportsPortletModeCellEditor(Composite parent,TreeViewer tv) {
		this(parent, SWT.NONE);
		this.tv = tv;
	}

	public SupportsPortletModeCellEditor(Composite parent, int style) {
		super(parent, style);
		//doSetValue("");
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		SupportsPortletModeDialog dialog = new SupportsPortletModeDialog(cellEditorWindow.getShell());
		IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
		Supports supports = (Supports)selection.getFirstElement();
		String values ="";
		for(int i=0;i<supports.getPortletModes().size();i++){
			if(i != supports.getPortletModes().size() -1 )
				values += supports.getPortletModes().get(i) + ",";
			else 
				values += supports.getPortletModes().get(i);
		}
		dialog.setModels(values);
		if (dialog.open() == Dialog.OK) {
			return dialog.getResult();
		}
		return null;
	}

}
