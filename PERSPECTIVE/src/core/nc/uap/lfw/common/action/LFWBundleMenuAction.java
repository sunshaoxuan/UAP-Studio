package nc.uap.lfw.common.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.menubar.dialog.MenuBundleDialog;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.grid.GridMenuDialog;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.webcomponent.LFWMenubarCompTreeItem;

public class LFWBundleMenuAction extends Action{

	public LFWBundleMenuAction(String title) {
		super("绑定表格",PaletteImage.getCreateDsImgDescriptor());
	}
	public void run() {
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		MenubarComp menubarComp = ((MenubarComp)treeItem.getData());
		Shell shell = new Shell(Display.getCurrent());
		MenuBundleDialog input = new MenuBundleDialog(null,M_grid.NewGridMenuAction_1);
		if(input.open() == InputDialog.OK){
			String bundleGrid = input.getGridId();
			LfwView widget = LFWPersTool.getCurrentWidget();
			if(bundleGrid!=null){
				WebComponent comp=widget.getViewComponents().getComponent(bundleGrid);
				if(comp instanceof GridComp){
					((GridComp) comp).setMenuBar(menubarComp);
				}
			}
			widget.getViewMenus().removeMenuBar(menubarComp.getId());
			((LFWMenubarCompTreeItem)treeItem).getEditorInput();
			LFWPersTool.saveWidget(widget);
			MessageDialog.openInformation(null, "成功", "菜单已成功绑定表格,请刷新左侧树使变更生效");
		}

	}
}
