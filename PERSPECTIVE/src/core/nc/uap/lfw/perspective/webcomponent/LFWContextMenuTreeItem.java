package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.contextmenubar.actions.DelContextMenubarAction;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ÓÒ¼ü²Ëµ¥
 * @author zhangxya
 *
 */
public class LFWContextMenuTreeItem extends LFWBasicTreeItem implements ILFWTreeNode{

	public LFWContextMenuTreeItem(TreeItem parentItem, int style) {
		super(parentItem, style);
	}
	
	public LFWContextMenuTreeItem(TreeItem parentItem, String type, ContextMenuComp contextMenubar) {
		super(parentItem, SWT.NONE);
		setData(contextMenubar);
		setText(type + contextMenubar.getId());
		setImage(getFileImage());
	}

	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "menubar.gif");
		return ImageProvider.menubar;
	}

	protected void checkSubclass () {
	}
	
	public void deleteNode() {
		// TODO Auto-generated method stub
		
	}

	public File getFile() {
		return (File) getData();
	}

	public String getIPathStr() {
		String parentIPath = "";
		TreeItem parent = getParentItem();
		if(parent instanceof ILFWTreeNode){
			parentIPath = ((ILFWTreeNode)parent).getIPathStr();
		}
		return parentIPath+"/"+getFile().getName();
	}
	public void addMenuListener(IMenuManager manager){
		DelContextMenubarAction delContextMenuAction = new DelContextMenubarAction();
		manager.add(delContextMenuAction);
	} 
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);		
		view.openContextMenuEditor(this);
	} 

}
