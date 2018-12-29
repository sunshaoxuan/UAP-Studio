package nc.uap.lfw.button;

import nc.lfw.editor.common.AbstractDialogPropertyDescriptor;
import nc.uap.lfw.lang.M_button;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class HotKeySetPropertyDescriptor extends AbstractDialogPropertyDescriptor{

	public HotKeySetPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		
	}

	@Override
	protected Object openDialogBox(Object value, Control cellEditorWindow) {
		HotKeySetDialog dialog = new HotKeySetDialog(null, M_button.HotKeySetPropertyDescriptor_0);
		if(dialog.open() == HotKeySetDialog.OK)
			return dialog.getResult();
		return null;	
	}

}
