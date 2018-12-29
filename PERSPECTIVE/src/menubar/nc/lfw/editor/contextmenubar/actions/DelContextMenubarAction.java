package nc.lfw.editor.contextmenubar.actions;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

public class DelContextMenubarAction  extends Action {
	public DelContextMenubarAction() {
		super(M_menubar.DelContextMenubarAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_CONTEXT_MENU);
		setToolTipText(WEBPersConstants.DEL_CONTEXT_MENU);
	}
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_menubar.DelContextMenubarAction_1, M_menubar.DelContextMenubarAction_2 + WEBPersConstants.CONTEXT_MENUBAR + M_menubar.DelContextMenubarAction_3)) {
			delete();
		}
	}
	
	private void delete() {
		try {
			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
			ContextMenuComp menubarComp = ((ContextMenuComp)treeItem.getData());
			LfwView widget = LFWPersTool.getCurrentWidget();
			if(widget.getViewMenus().getContextMenu(menubarComp.getId()) != null){
				widget.getViewMenus().removeContextMenu(menubarComp.getId());
				LFWPersTool.saveWidget(widget);
			}
			treeItem.dispose();
			
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_CONTEXT_MENU;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	
}
