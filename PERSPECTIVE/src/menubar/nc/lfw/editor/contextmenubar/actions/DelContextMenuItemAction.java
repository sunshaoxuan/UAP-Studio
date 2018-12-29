package nc.lfw.editor.contextmenubar.actions;

import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.menubar.MenuItemLabel;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.draw2d.Label;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 删除context的menuItem
 * @author zhangxya
 *
 */
public class DelContextMenuItemAction extends Action {
	
	private LfwElementObjWithGraph elementObj;
	private Label label;
	private String itemId;
	
	public DelContextMenuItemAction(Label label, LfwElementObjWithGraph elementObj, String itemId) {
		setText(M_menubar.DelContextMenuItemAction_0 + itemId);
		setToolTipText(M_menubar.DelContextMenuItemAction_1);
		this.elementObj = elementObj;
		this.label = label;
		this.itemId = itemId;
	}
	
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_menubar.DelContextMenuItemAction_2, M_menubar.DelContextMenuItemAction_3 + itemId + M_menubar.DelContextMenuItemAction_4))
			deleteItem();
	}
	
	private void deleteItem() {
		ContextMenuEditor editor = ContextMenuEditor.getActiveMenubarEditor();
		if (label instanceof MenuItemLabel) {
			
			if (elementObj instanceof ContextMenuElementObj) {
				((ContextMenuElementObj) elementObj).getFigure().deleteItem((MenuItemLabel)label);
				List<MenuItemObj> existItems = ((ContextMenuElementObj) elementObj).getChildrenList();
				if(existItems!=null&&existItems.size()>0){
					for(MenuItemObj item:existItems){
						if(item.getMenuItem().getId().equals(itemId)){
							((ContextMenuElementObj) elementObj).removeChild(item);
							break;
						}
					}
				}
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
