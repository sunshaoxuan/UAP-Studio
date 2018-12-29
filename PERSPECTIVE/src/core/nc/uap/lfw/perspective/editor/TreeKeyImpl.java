package nc.uap.lfw.perspective.editor;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeKeyImpl {
	private TreeViewer tv = null;

	private Tree tree = null;

	private int colNum = -1;

	private TreeItem treeItem = null;
	private Action addAction = null;
	public TreeKeyImpl(TreeViewer tv, Action addAction) {
		super();
		this.tv = tv;
		tree = tv.getTree();
		this.addAction = addAction;
		init();
	}

	private void init() {
		tree.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				handleMouseDown(e);
			}
		});
		tree.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				boolean next = true;
				if(e.stateMask == SWT.SHIFT)
					next = false;
				if (e.keyCode == 13) {
					if (colNum != -1) {
						if (next)
							colNum++;
						else
							colNum--;
						if (next && colNum == tree.getColumnCount()) {
							colNum = 0;
							TreeItem nextTreeItem = findNextItem(treeItem, next);
							if (nextTreeItem == null && next && addAction != null) {
								addAction.run();
								nextTreeItem = findNextItem(treeItem, next);
							}
							treeItem = nextTreeItem;
						}
						if(!next && colNum == -1){
							colNum = tree.getColumnCount() -1;
							TreeItem nextTreeItem = findNextItem(treeItem, next);
							if (nextTreeItem == null && next && addAction != null) {
								addAction.run();
								nextTreeItem = findNextItem(treeItem, next);
							}
							if(nextTreeItem != null)
								treeItem = nextTreeItem;
							else{
								colNum = 0;
								return;
							}
						}

						if (treeItem == null){
							return;
						}

						tv.editElement(getPath(treeItem), colNum);
						tree.showColumn(tree.getColumn(colNum));

					}
				}
			}
		});
	}

	private TreeItem findNextItem(TreeItem item, boolean next) {
		TreeItem ti = null;
		for (int i = 0; i < tree.getItemCount(); i++) {
			ti = findNextItem(tree.getItem(i), item, next);
			if (ti != null) {
				break;
			}
		}
		return ti;

	}

	private TreeItem findNextItem(TreeItem parent, TreeItem item, boolean next) {
		for (int i = 0; i < parent.getItemCount(); i++) {
			TreeItem ti = parent.getItem(i);
			if (ti.equals(item) ) {
				if(next && i + 1 < parent.getItemCount())
					return parent.getItem(i + 1);
				if(!next && i -1 > -1){
					return parent.getItem(i -1);
				}
		
			} else {
				TreeItem retr = findNextItem(ti, item, next);
				if (retr != null)
					return retr;
			}

		}
		return null;
	}

	private TreePath getPath(TreeItem item) {
		ArrayList<Object> al = new ArrayList<Object>();
		TreeItem ti = item;
		while (ti != null) {
			al.add(0, ti.getData());
			ti = ti.getParentItem();
		}
		return new TreePath(al.toArray(new Object[0]));
	}

	protected void handleMouseDown(MouseEvent e) {
		if (e.button != 1) {
			return;
		}

		Item[] items = tree.getSelection();
		if (items.length != 1) {
			treeItem = null;
			return;
		}
		treeItem = (TreeItem) items[0];

		int columnToEdit;
		int columns = tree.getColumnCount();// getColumnCount();
		if (columns == 0) {
			columnToEdit = 0;
		} else {
			columnToEdit = -1;
			for (int i = 0; i < columns; i++) {
				Rectangle bounds = treeItem.getBounds(i);// getBounds(treeItem,
															// i);
				if (bounds.contains(e.x, e.y)) {
					columnToEdit = i;
					break;
				}
			}
			if (columnToEdit == -1) {
				return;
			}
		}
		colNum = columnToEdit;
	}

}
