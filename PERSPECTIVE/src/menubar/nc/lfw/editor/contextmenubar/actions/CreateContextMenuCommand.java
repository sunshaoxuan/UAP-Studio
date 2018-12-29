package nc.lfw.editor.contextmenubar.actions;

import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.contextmenubar.ContextMenuGrahp;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.uap.lfw.core.comp.MenuItem;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

public class CreateContextMenuCommand extends Command {
	private MenuElementObj menuObj;
	private boolean canUndo = true;
	private ContextMenuGrahp graph;
	private Rectangle rect;
	private int level;

	public CreateContextMenuCommand(MenuElementObj menuObj, ContextMenuGrahp graph, Rectangle rect) {
		super();
		this.menuObj = menuObj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new menu");
	}
	
	public boolean canExecute() {
		return menuObj != null && graph != null && rect != null;
	}
	
	public void execute() {
		Shell shell = new Shell();
		CreateContextMenuDialog dialog = new CreateContextMenuDialog(shell);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID) {
			menuObj.setId(dialog.getSubMenuId());
			menuObj.setLevel(dialog.getLevel());
			level = dialog.getLevel();
		}else{
			return;
		}
		menuObj.setGraph(graph);
		
		MenuItem item = new MenuItem();
		item.setId(menuObj.getId());
		menuObj.setMenuItem(item);
		
		// 增加临时子菜单
		ContextMenuEditor.getActiveMenubarEditor().addTempChildMenuElement(menuObj);
		
		// 显示子菜单
		ContextMenuEditor.getActiveMenubarEditor().showChildMenuItem(menuObj, level);
		
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
