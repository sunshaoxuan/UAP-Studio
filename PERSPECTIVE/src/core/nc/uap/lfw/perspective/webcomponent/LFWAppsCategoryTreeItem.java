package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.uap.lfw.core.WEBPersPlugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWAppsCategoryTreeItem extends LFWDirtoryTreeItem{

	String pk = null;
	public LFWAppsCategoryTreeItem(TreeItem parentItem, File file,
			String text , String pk) {
		super(parentItem, file, text);
		this.pk = pk;
	}
	protected Image getDirImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "groups.gif");
		return ImageProvider.groups;
	}

}
