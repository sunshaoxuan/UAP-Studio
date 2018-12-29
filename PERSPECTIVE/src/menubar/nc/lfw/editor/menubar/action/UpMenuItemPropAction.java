package nc.lfw.editor.menubar.action;

import java.util.ArrayList;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.contextmenubar.ContextMenuEditor;
import nc.lfw.editor.menubar.MenubarEditor;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.lfw.editor.menubar.page.MenuItemPropertiesView;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.lang.M_menubar;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Field œ¬“∆√¸¡Ó
 * 
 * @author zhangxya
 * 
 */
public class UpMenuItemPropAction extends Action {
	private class AttrMoveupCommand extends Command {
		private ArrayList<MenuItem> arraylist = null;
		private int index = -1;

		public AttrMoveupCommand(ArrayList<MenuItem> arraylist, int index) {
			super(M_menubar.UpMenuItemPropAction_0);
			this.arraylist = arraylist;
			this.index = index;
		}

		
		public void execute() {
			redo();
		}

		
		public void redo() {
			if (index > 0) {
				TableViewer tableView = view.getTv();
				MenuItem item = arraylist.get(index);
				arraylist.remove(item);
				arraylist.add(index - 1, item);
				tableView.refresh(arraylist);
			}
		}

		
		public void undo() {
			if (index < arraylist.size() - 1) {
				TableViewer tableView = view.getTv();
				MenuItem item = arraylist.remove(index - 1);
				arraylist.add(index, item);
				tableView.refresh(arraylist);
			}
		}

	}

	private MenuItemPropertiesView view = null;

	public UpMenuItemPropAction(MenuItemPropertiesView view) {
		super(M_menubar.UpMenuItemPropAction_1);
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	
	public void run() {
		TableViewer tableView = view.getTv();
		tableView.cancelEditing();
		StructuredSelection sel = (StructuredSelection) tableView
				.getSelection();
		Object o = sel.getFirstElement();
		if (o instanceof MenuItem) {
			ArrayList<MenuItem> al = (ArrayList<MenuItem>) (ArrayList) tableView
					.getInput();
			int index = al.indexOf(o);
			if (index > 0) {
				AttrMoveupCommand cmd = new AttrMoveupCommand(al, index);
				LFWBaseEditor editor = MenubarEditor.getActiveMenubarEditor();
				if(editor == null){
					editor = ContextMenuEditor.getActiveMenubarEditor();
				}
				if (editor != null) {
					editor.executComand(cmd);
					editor.setDirtyTrue();
					// À¢–¬ÕºœÒœ‘ æ
					StructuredSelection ss = (StructuredSelection) editor.getCurrentSelection();
					Object currentSel = ss.getFirstElement();
					if (currentSel instanceof MenuElementPart) {
						Object model = ((MenuElementPart)currentSel).getModel();
						if (model instanceof MenubarElementObj) {
							((MenubarElementObj) model).getFigure().refreshItems();
						} else if (model instanceof MenuElementObj) {
							((MenuElementObj) model).getFigure().refreshItems();
						}
					}
				}
			}
		}

	}
}
