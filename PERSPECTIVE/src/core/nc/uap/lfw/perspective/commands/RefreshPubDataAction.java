package nc.uap.lfw.perspective.commands;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 更新公共池中的数据
 * @author zhangxya
 *
 */
public class RefreshPubDataAction extends Action {

	public RefreshPubDataAction() {
		super("刷新", PaletteImage.getRefreshImgDescriptor());
		setText(WEBPersConstants.REFRESH);
		setToolTipText(WEBPersConstants.REFRESH);
	}

	
	public void run() {
		refreshNode();
	}
	
	private void refreshNode() {
		Tree tree = LFWPersTool.getTree();
		TreeItem[] tis = tree.getSelection();
		if (tis == null || tis.length == 0)
			return;
		TreeItem ti = tis[0];
		ti.removeAll();
		//TODO 
		LFWExplorerTreeView.getLFWExploerTreeView(null).refreshPubTreeItem(tis);
	}


	
	/**
	 * 刷新节点（供外部调用）
	 * @param explorerTreeView
	 * @param treeView
	 */
	public static void refreshNode(LFWExplorerTreeView explorerTreeView, Tree tree) {
		TreeItem[] tis = tree.getSelection();
		if (tis == null || tis.length == 0)
			return;
		TreeItem ti = tis[0];
		ti.removeAll();
		explorerTreeView.refreshPubTreeItem(tis);
	}

}
