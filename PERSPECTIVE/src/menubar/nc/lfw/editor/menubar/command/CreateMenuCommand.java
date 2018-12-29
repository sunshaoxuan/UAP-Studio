package nc.lfw.editor.menubar.command;

import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.dialog.CreateMenuElementDialog;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

public class CreateMenuCommand extends Command {
	private MenuElementObj menuObj;
	private boolean canUndo = true;
	private MenubarGraph graph;
	private Rectangle rect;
	private int level;

	public CreateMenuCommand(MenuElementObj menuObj, MenubarGraph graph, Rectangle rect) {
		super();
		this.menuObj = menuObj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new menu"); //$NON-NLS-1$
	}
	
	public boolean canExecute() {
		return menuObj != null && graph != null && rect != null;
	}
	
	public void execute() {
		Shell shell = new Shell();
		CreateMenuElementDialog dialog = new CreateMenuElementDialog(shell,M_menubar.CreateMenuCommand_0);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			menuObj.setId(dialog.getSubMenuId());
			menuObj.setLevel(dialog.getLevel());
			level = dialog.getLevel();
		}else{
			return;
		}
		menuObj.setGraph(graph);
		
//		MenuItem item = new MenuItem();
//		item.setId(menuObj.getId());
//		menuObj.setMenuItem(item);
		
		// 增加临时子菜单
		MenubarEditor.getActiveMenubarEditor().addTempChildMenuElement(menuObj);
		
		// 显示子菜单
		MenubarEditor.getActiveMenubarEditor().showChildMenuItem(menuObj, level);
		
		redo();
	}
	
	
	public void redo() {
		graph.addMenu(menuObj);
	}

	
	public void undo() {
		graph.removeMenu(menuObj);
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	
	public boolean canUndo() {
		return isCanUndo();
	}
}
