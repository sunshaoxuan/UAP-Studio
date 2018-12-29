package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.editor.extNode.DeleteRefFolderAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWRefFolderTreeItem extends LFWDirtoryTreeItem{
	LfwRefInfoVO refInfo = null;
	public LFWRefFolderTreeItem(TreeItem parentItem,File folder, LfwRefInfoVO refInfo, String text) {
		super(parentItem, folder, text);
		setImage(getRefImage());
		setRefInfo(refInfo);
	}
	private Image getRefImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "file.png");
		return ImageProvider.component;
	}
	
	
	public void addMenuListener(IMenuManager manager) {
		DeleteRefFolderAction deleteAction = new DeleteRefFolderAction(this);
		manager.add(deleteAction);
	}
	public LfwRefInfoVO getRefInfo() {
		return refInfo;
	}
	public void setRefInfo(LfwRefInfoVO refInfo) {
		this.refInfo = refInfo;
	}
	
}
