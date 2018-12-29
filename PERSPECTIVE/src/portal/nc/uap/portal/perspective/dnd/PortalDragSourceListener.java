package nc.uap.portal.perspective.dnd;

import nc.uap.portal.portlets.PortletTreeItem;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 树拖拽事件
 * 
 * @author dingrf
 *
 */

public class PortalDragSourceListener implements DragSourceListener {

	/**当前树*/
	private Tree tree = null;
	
	/**拖拽源树节点*/
	private TreeItem[] dragSourceItem = null;
	
	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public TreeItem[] getDragSourceItem() {
		return dragSourceItem;
	}

	public void setDragSourceItem(TreeItem[] dragSourceItem) {
		this.dragSourceItem = dragSourceItem;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE)
			dragSourceItem[0].dispose();
		dragSourceItem[0] = null;
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		event.data = dragSourceItem[0].getText();
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		TreeItem[] selection = tree.getSelection();
		if (selection.length > 0 && selection[0] instanceof PortletTreeItem) {
			event.doit = true;
			dragSourceItem[0] = selection[0];
		} else {
			event.doit = false;
		}
	}

}
