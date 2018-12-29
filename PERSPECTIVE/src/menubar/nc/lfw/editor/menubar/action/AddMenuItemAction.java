package nc.lfw.editor.menubar.action;

import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.dialog.NewMenuItemDialog;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 增加菜单项操作
 * 
 * @author guoweic
 *
 */
public class AddMenuItemAction extends Action {
	
	private LfwElementObjWithGraph menuObj;
	
	public AddMenuItemAction(LfwElementObjWithGraph menuObj) {
		super(M_menubar.AddMenuItemAction_0);
		this.menuObj = menuObj;
	}

	
	public void run() {
		Shell shell = new Shell(Display.getCurrent());
		
		NewMenuItemDialog dialog = new NewMenuItemDialog(shell, M_menubar.AddMenuItemAction_1);
		if (menuObj instanceof MenuElementObj)
			dialog.setParentMenuItem(((MenuElementObj)menuObj).getMenuItem());
		dialog.setMenuObj(menuObj);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			List<MenuItemObj> existItems = null;
			
			if(menuObj instanceof MenubarElementObj)
				existItems = ((MenubarElementObj)menuObj).getChildrenList();
			if(menuObj instanceof ContextMenuElementObj)
				existItems = ((ContextMenuElementObj)menuObj).getChildrenList();
			if(menuObj instanceof MenuElementObj)
				existItems = ((MenuElementObj)menuObj).getChildrenList();
			if(existItems!=null&&existItems.size()>0){
				for(MenuItemObj item:existItems){
					if(item.getMenuItem().getId().equals(dialog.getItemId())){
						MessageDialog.openInformation(null, M_menubar.AddMenuItemAction_2, M_menubar.AddMenuItemAction_3);
						return;
					}
				}
			}
			MenuItem newMenuItem = new MenuItem();
			newMenuItem.setId(dialog.getItemId());
			newMenuItem.setText(dialog.getText());
			newMenuItem.setI18nName(dialog.getItemId());
			newMenuItem.setLangDir("lfwbuttons"); //$NON-NLS-1$
			if(menuObj instanceof ContextMenuElementObj){
				((ContextMenuElementObj)menuObj).getFigure().addItem(newMenuItem);
				ContextMenuEditor.getActiveMenubarEditor().setDirtyTrue();
			}
			else{
				// 增加子项
				if (menuObj instanceof MenubarElementObj)
					((MenubarElementObj)menuObj).getFigure().addItem(newMenuItem);
				else if (menuObj instanceof MenuElementObj)
					((MenuElementObj)menuObj).getFigure().addItem(newMenuItem);
			}
			if(ContextMenuEditor.getActiveMenubarEditor() != null)
				ContextMenuEditor.getActiveMenubarEditor().setDirtyTrue();
			else if(MenubarEditor.getActiveMenubarEditor() != null)
				MenubarEditor.getActiveMenubarEditor().setDirtyTrue();
		}
	}
}
