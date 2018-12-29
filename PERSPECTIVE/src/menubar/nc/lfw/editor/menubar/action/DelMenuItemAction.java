package nc.lfw.editor.menubar.action;

import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.menubar.MenuItemLabel;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.draw2d.Label;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 删除MenuItem命令
 *
 */
public class DelMenuItemAction extends Action {
	
	private LfwElementObjWithGraph elementObj;
	private Label label;
	private String itemId;
	
	public DelMenuItemAction(Label label, LfwElementObjWithGraph elementObj, String itemId) {
		setText(M_menubar.DelMenuItemAction_0 + itemId);
		setToolTipText(M_menubar.DelMenuItemAction_0);
		this.elementObj = elementObj;
		this.label = label;
		this.itemId = itemId;
	}
	
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_menubar.DelMenuItemAction_1, M_menubar.DelMenuItemAction_2 + itemId + M_menubar.DelMenuItemAction_3))
			deleteItem();
	}
	
	private void deleteItem() {
		MenubarEditor editor = MenubarEditor.getActiveMenubarEditor();
		if (label instanceof MenuItemLabel) {
			LfwView widget = LFWPersTool.getCurrentWidget();
			if (elementObj instanceof MenubarElementObj) {
				((MenubarElementObj) elementObj).getFigure().deleteItem((MenuItemLabel)label);
				//新加于2013.2.1
				
				List<MenuItemObj> existItems = ((MenubarElementObj) elementObj).getChildrenList();
				if(existItems!=null&&existItems.size()>0){
					for(MenuItemObj item:existItems){
						if(item.getMenuItem().getId().equals(itemId)){
							((MenubarElementObj) elementObj).removeChild(item);
							break;
						}
					}
				}
//				MenubarComp menubarComp = ((MenubarElementObj) elementObj).getMenubar();
//				widget.getViewMenus().removeMenuBar(menubarComp.getId());
//				widget.getViewMenus().addMenuBar(menubarComp);
				
			} else if (elementObj instanceof MenuElementObj) {
				((MenuElementObj) elementObj).getFigure().deleteItem((MenuItemLabel)label);
				List<MenuItemObj> existItems = ((MenuElementObj) elementObj).getChildrenList();
				if(existItems!=null&&existItems.size()>0){
					for(MenuItemObj item:existItems){
						if(item.getMenuItem().getId().equals(itemId)){
							((MenuElementObj) elementObj).removeChild(item);
							break;
						}
					}
				}
			}
			
			
			// 设置当前选中项为空
			StructuredSelection ss = (StructuredSelection) editor.getCurrentSelection();
			Object currentSel = ss.getFirstElement();
			if (currentSel instanceof MenuElementPart) {
				Object model = ((MenuElementPart)currentSel).getModel();
				if (model instanceof MenubarElementObj) {
					((MenubarElementObj) model).setCurrentItem(null);
					((MenubarElementObj) model).getFigure().setCurrentLabel(null);
				} else if (model instanceof MenuElementObj) {
					((MenuElementObj) model).setCurrentItem(null);
					((MenuElementObj) model).getFigure().setCurrentLabel(null);
				}
			}
			
			// 保存pagemeta
//			editor.savePagemeta(pagemeta);
			editor.setDirtyTrue();
		}
	}
}
