package nc.lfw.editor.menubar.action;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.webcomponent.LFWGridMenubarTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @author guoweic
 *
 */
public class DelMenubarAction extends Action {
	public DelMenubarAction() {
		super(M_menubar.DelMenubarAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_MENUBAR);
		setToolTipText(WEBPersConstants.DEL_MENUBAR);
	}
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_menubar.DelMenubarAction_1, M_menubar.DelMenubarAction_2 + WEBPersConstants.MENUBAR_CN + M_menubar.DelMenubarAction_3)) {
			delete();
		}
	}
	
	private void delete() {
		try {
//			LfwWindow pm = LFWPersTool.getCurrentPageMeta();
			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
			MenubarComp menubarComp = ((MenubarComp)treeItem.getData());
//			if(pm.getgetViewMenus().getMenuBar(menubarComp.getId()) != null){
////				pm.getViewMenus().removeMenuBar(menubarComp.getId());
//				LFWPersTool.savePagemeta(pm);
//			}
//			else {
			LfwView widget = LFWPersTool.getCurrentWidget();
			if(treeItem instanceof LFWGridMenubarTreeItem){
				LFWGridMenubarTreeItem gridMenuItem = (LFWGridMenubarTreeItem)treeItem;
				String gridId = gridMenuItem.getGridId();
				GridComp grid = (GridComp)widget.getViewComponents().getComponent(gridId);
				grid.setMenuBar(null);
				LFWPersTool.saveWidget(widget);
			}
			else{	
				
				if(widget.getViewMenus().getMenuBar(menubarComp.getId()) != null){
					widget.getViewMenus().removeMenuBar(menubarComp.getId());
					LFWPersTool.saveWidget(widget);
				}
			}
				
//			}
			treeItem.dispose();
			
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_MENUBAR;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	
}
