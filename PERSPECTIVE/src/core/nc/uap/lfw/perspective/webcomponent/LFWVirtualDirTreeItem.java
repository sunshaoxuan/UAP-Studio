package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.uap.lfw.core.WEBPersPlugin;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ÐéÄâÄ¿Â¼TreeItem
 * @author zhangxya
 *
 */
public class LFWVirtualDirTreeItem extends LFWDirtoryTreeItem{
//	private ImageDescriptor imageDescriptor = null;
	
	public LFWVirtualDirTreeItem(TreeItem parentItem, File file) {
		super(parentItem, file);
	}
	
	protected Image getDirImage() {
//		imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "virtualdir.gif");
		return ImageProvider.virtualdir;
	}
	
	public void addMenuListener(IMenuManager manager){
		addAMCMenuListener(manager);
	} 

	public void mouseDoubleClick(){
	} 
	
}
