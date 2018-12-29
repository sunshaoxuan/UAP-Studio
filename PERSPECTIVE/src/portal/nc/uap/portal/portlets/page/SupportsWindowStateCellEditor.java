package nc.uap.portal.portlets.page;

import nc.uap.portal.container.om.Supports;
import nc.uap.portal.portlets.dialog.SupportsWindowStateDialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * SupportsWindowStateCellEditor
 * 
 * @author dingrf
 * 
 */
public class SupportsWindowStateCellEditor extends DialogCellEditor {

	private TreeViewer tv;
	

	public SupportsWindowStateCellEditor(Composite parent,TreeViewer tv) {
		this(parent, SWT.NONE);
		this.tv = tv;
	}

	public SupportsWindowStateCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	protected Object openDialogBox(Control cellEditorWindow) {
		SupportsWindowStateDialog dialog = new SupportsWindowStateDialog(cellEditorWindow.getShell());
		IStructuredSelection selection = (IStructuredSelection) tv.getSelection();
		Supports supports = (Supports)selection.getFirstElement();
		String values ="";
		for(int i=0;i<supports.getWindowStates().size();i++){
			if(i != supports.getWindowStates().size() -1 )
				values += supports.getWindowStates().get(i) + ",";
			else 
				values += supports.getWindowStates().get(i);
		}
		dialog.setStates(values);
		if (dialog.open() == Dialog.OK) {
			return dialog.getResult();
		}
		return null;
	}

}
