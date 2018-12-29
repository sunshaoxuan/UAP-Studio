package nc.lfw.editor.common;

import nc.lfw.editor.menubar.dialog.OperateStatusSelDialog;

import org.eclipse.swt.widgets.Control;

/**
 * dataset可用状态的选择
 * @author zhangxya
 *
 */
public class OperateStatusPropertyDescriptor extends AbstractDialogPropertyDescriptor {

	/**
	 * @param id
	 * @param displayName
	 */
	public OperateStatusPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	protected Object openDialogBox(Object obj, Control cellEditorWindow) {
		OperateStatusSelDialog dialog = new OperateStatusSelDialog(null, "数据集可选状态");
		if(dialog.open() == OperateStatusSelDialog.OK)
			return dialog.getResult();
		return null;	
	}
	
	
}
