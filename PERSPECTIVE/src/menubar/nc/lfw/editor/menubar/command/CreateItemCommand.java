package nc.lfw.editor.menubar.command;

import java.util.Iterator;

import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenuItemObj;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

/**
 * 该类已不用
 * @author guoweic
 *
 */
public class CreateItemCommand extends Command {
	private MenuItemObj menuObj;
	private boolean canUndo = true;
	private MenubarGraph graph;
	private Rectangle rect;

	public CreateItemCommand(MenuItemObj menuObj, MenubarGraph graph, Rectangle rect) {
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
		int heightSpace = 20;
		Iterator<MenuElementObj> it = graph.getChildrenList().iterator();
		int top = heightSpace;
		while(it.hasNext()){
			MenuElementObj ele = it.next();
			top += ele.getElementHeight();
			
			top += heightSpace;
		}
		Point point = new Point(400, top);
		point.x = 280;
		
		menuObj.setSize(new Dimension(100, 120));
		menuObj.setLocation(point);
		menuObj.setGraph(graph);
		redo();
	}
	
	
	public void redo() {
		//graph.addMenu(menuObj);
	}

	
	public void undo() {
		//graph.removeMenu(menuObj);
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
