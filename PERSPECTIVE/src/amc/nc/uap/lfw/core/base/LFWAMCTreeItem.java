/**
 * 
 */
package nc.uap.lfw.core.base;

import java.io.File;

import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * AMC TreeItem³éÏó»ùÀà
 * @author chouhl
 *
 */
public class LFWAMCTreeItem extends LFWDirtoryTreeItem{

	public LFWAMCTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, object, text);
	}
	
	public LFWAMCTreeItem(TreeItem parentItem, File file) {
		super(parentItem, file);
	}

	public LFWAMCTreeItem(TreeItem parentItem, File file, String text) {
		super(parentItem, file, text);
	}
	
	protected Image getDirImage() {
//		String imageName = "page.gif";
		Image currentImage = ImageProvider.page;
		if(ILFWTreeNode.APPLICATION_FOLDER.equals(getType())){
			currentImage = ImageProvider.pages;
		}else if(ILFWTreeNode.MODEL_FOLDER.equals(getType())){
			currentImage = ImageProvider.pages;
		}else if(ILFWTreeNode.WINDOW_FOLDER.equals(getType())){
			currentImage = ImageProvider.pages;
		}else if(ILFWTreeNode.PUBLIC_VIEW_FOLDER.equals(getType())){
			currentImage = ImageProvider.pages;
		}else if(ILFWTreeNode.VIEW.equals(getType())){
			currentImage = ImageProvider.widget;
		}
		return currentImage;
	}
	
	public void mouseDoubleClick(){
	}
}
