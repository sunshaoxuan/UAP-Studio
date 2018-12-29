package nc.lfw.editor.common;

import nc.uap.lfw.refnode.AreaDialog;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * 弹出input对话框的PropertyDescriptor
 * @author zhangxya
 *
 */
public class TextOperatePropertyDescriptor extends PropertyDescriptor {

	private String originalValue;
	/**
	 * @param id
	 * @param displayName
	 */
	public TextOperatePropertyDescriptor(Object id, String displayName, String originalValue) {
		super(id, displayName);
		this.originalValue = originalValue;
	}
	
	protected Object openDialogBox(Object obj, Control cellEditorWindow) {
		AreaDialog dialog = new AreaDialog(new Shell(), originalValue);
		if(dialog.open() == AreaDialog.OK)
			return dialog.getResult();
		return null;	
	}
	
	
}