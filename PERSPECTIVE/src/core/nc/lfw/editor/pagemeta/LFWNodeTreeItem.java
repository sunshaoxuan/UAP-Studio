package nc.lfw.editor.pagemeta;

import java.io.File;

import nc.uap.lfw.perspective.project.ILFWTreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

public class LFWNodeTreeItem extends TreeItem implements ILFWTreeNode {

	public LFWNodeTreeItem(TreeItem parentItem) {
		super(parentItem, SWT.NONE);
	}

//	public LFWNodeTreeItem(TreeItem parentItem, File file, String text) {
//		super(parentItem, SWT.NONE);
//	}

	public void deleteNode() {
		
	}

	public File getFile() {
		return null;
	}

	public String getIPathStr() {
		String parentIPath = "";
		TreeItem parent = getParentItem();
		if (parent instanceof ILFWTreeNode) {
			parentIPath = ((ILFWTreeNode) parent).getIPathStr();
		}
		return parentIPath + "/" + getFile().getName();
	}
}