package nc.lfw.editor.menubar.command;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.menubar.MenubarConnector;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 删除Menubar连接关系
 * 
 * @author guoweic
 * 
 */
public class MenubarRelationDeleteCommand extends Command {
	private Connection conn = null;

	public MenubarRelationDeleteCommand(Connection relation) {
		super();
		this.conn = relation;
		setLabel("delete relation"); //$NON-NLS-1$
	}

	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}

	public void execute() {
		redo();
	}

	public void redo() {
		if (MessageDialog.openConfirm(null, M_menubar.MenubarRelationDeleteCommand_0, M_menubar.MenubarRelationDeleteCommand_1))
			delData();
	}

	public void undo() {
		conn.connect();
	}

	private void delData() {
		if (conn instanceof MenubarConnector) {
			MenuElementObj target = (MenuElementObj) ((MenubarConnector) conn).getTarget();
			if (((MenubarConnector) conn).getSource() instanceof MenubarElementObj) {
//				MenubarElementObj source = (MenubarElementObj) ((MenubarConnector) conn).getSource();
//				MenubarComp mc = source.getMenubar();
				MenuItem targetItem = target.getMenuItem();
//				List<MenuItem> menuList = new ArrayList<MenuItem>();
//				menuList = mc.getMenuList();
//				MenuItem mi = new MenuItem();
//				for (int i = 0; i < menuList.size(); i++) {
//					mi = menuList.get(i);
//					if(mi.getChildList()==null||mi.getChildList().size()==0)
//						continue;
////					if (mi.getChildList().get(0).getId().equals(
////							targetItem.getId())) {
////						break;
////					}
//					if(mi.getId().equals(targetItem.getId()))
//						break;
//				}
				targetItem.removeAllChildren();
				target.setMenuItem(null);
				MenubarEditor editor = MenubarEditor.getActiveMenubarEditor();
				if (editor != null) {
					editor.setDirtyTrue();
					conn.disConnect();
					editor.removeConnector(targetItem.getId());
				}
			} else if(((MenubarConnector) conn).getSource() instanceof ContextMenuElementObj){
//				ContextMenuElementObj source = (ContextMenuElementObj) ((MenubarConnector) conn).getSource();
//				ContextMenuComp mc = source.getMenubar();
				MenuItem targetItem = target.getMenuItem();
//				List<MenuItem> menuList = new ArrayList<MenuItem>();
//				menuList = mc.getItemList();
//				MenuItem mi = new MenuItem();
//				for (int i = 0; i < menuList.size(); i++) {
//					mi = menuList.get(i);
//					if (mi.getChildList().get(0).getId().equals(
//							targetItem.getId())) {
//						break;
//					}
//				}
				targetItem.removeAllChildren();
				target.setMenuItem(null);
				ContextMenuEditor ceditor = ContextMenuEditor.getActiveMenubarEditor();
				if (ceditor != null) {
					ceditor.setDirtyTrue();
					conn.disConnect();
					ceditor.removeConnector(targetItem.getId());
				}
			} else if(((MenubarConnector) conn).getSource() instanceof MenuElementObj){
				MenuItem targetItem = target.getMenuItem();
				targetItem.removeAllChildren();
				target.setMenuItem(null);
				MenubarEditor editor = MenubarEditor.getActiveMenubarEditor();
				if (editor != null) {
					editor.setDirtyTrue();
					conn.disConnect();
					editor.removeConnector(targetItem.getId());
				}
			}

			// List<MenuItemObj> miObj = source.getChildrenList();
			// for(int i=0;i<miObj.size();i++){
			// if(miObj.get(i).getMenuItem().getId().equals(mi.getId())){
			// source.removeChild(miObj.get(i));
			// break;
			// }
			// }

			// source.removeChild(obj)
			// source.removeChild(target.getMenuItem().get);
			// 获取源菜单项

			//
			// // 新建MenuItem为目标菜单的MenuItem
			// MenuItem newItem = new MenuItem();
			// newItem.setId(sourceItem.getId() + "temp");
			// for (MenuItem item : sourceItem.getChildList()) {
			// newItem.addMenuItem(item);
			// }
			// target.setMenuItem(newItem);
			//
			// // 删除源菜单项的所有子项
			// sourceItem.removeAllChildren();
			//
			// target.getFigure().refresh();

		}
	}

}
