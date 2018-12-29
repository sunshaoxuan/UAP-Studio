package nc.lfw.editor.menubar.action;

import nc.lfw.editor.menubar.dialog.EditMenuItemDialog;
import nc.lfw.editor.menubar.page.MenuItemPropertiesView;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

/**
 * 编辑菜单项操作
 * 
 * @author guoweic
 *
 */
public class EditMenuItemAction extends Action {
	
	private MenuItemPropertiesView view = null;
	
	public EditMenuItemAction(MenuItemPropertiesView view) {
		super(M_menubar.EditMenuItemAction_0);
		this.view = view;
	}
	
	public void run() {
		createEvents();
	}
	
	public void createEvents() {
		IStructuredSelection selection = (IStructuredSelection) view.getTv().getSelection();
		MenuItem item = (MenuItem) selection.getFirstElement();
		if(item == null)
			return;
		Shell shell = new Shell();
		EditMenuItemDialog dialog = new EditMenuItemDialog(shell, M_menubar.EditMenuItemAction_1);
		dialog.setItem(item);
		
		//如果是子菜单，可以修改分隔线属性
		if(view.isMenuElement()){
			dialog.setCanChangeSep(true);
		}
		
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			item.setId(dialog.getId());
			item.setText(dialog.getText());
//			item.setOperatorStatusArray(dialog.getOperatorStatusArray());
//			item.setBusinessStatusArray(dialog.getBusinessStatusArray());
//			item.setOperatorVisibleStatusArray(dialog.getOperatorVisibleStatusArray());
//			item.setBusinessVisibleStatusArray(dialog.getBusinessVisibleStatusArray());
			item.setI18nName(dialog.getI18nName());
			item.setImgIcon(dialog.getImgIcon());
			item.setImgIconOn(dialog.getImgIconOn());
			item.setImgIconDisable(dialog.getImgIconDisabled());
			item.setHotKey(dialog.getHotKey());
			item.setDisplayHotKey(dialog.getDisplayHotKey());
			item.setModifiers(dialog.getModifier());
			item.setSep(dialog.isSep());
			item.setStateManager(dialog.getStateManager());
			view.saveChildItem(item);
		}
	}

}
