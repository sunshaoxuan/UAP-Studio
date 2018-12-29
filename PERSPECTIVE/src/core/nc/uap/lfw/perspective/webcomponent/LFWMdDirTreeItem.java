package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.design.itf.BusinessComponent;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.md.component.DeleteAction;
import nc.uap.lfw.md.component.ImportMdFile;
import nc.uap.lfw.md.component.NewBpfFileAction;
import nc.uap.lfw.md.component.NewMdDirAction;
import nc.uap.lfw.md.component.NewMDPFileAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;

public class LFWMdDirTreeItem extends LFWDirtoryTreeItem {

	/**
	 * 文件夹类型
	 */
	private String type = "";

	public Object object;

	public BusinessComponent bcp;
	
	TreeItem parentItem;

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public LFWMdDirTreeItem(TreeItem parentItem, Object object, String text) {
		super(parentItem, object, text);
		this.parentItem = parentItem;
	}

	public LFWMdDirTreeItem(TreeItem parentItem, File file) {
		this(parentItem, file, file.getName());
	}


	protected Image getDirImage() {
//		imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH,
//				"metadata.png");
		return ImageProvider.metadata;
	}

	public File getFile() {
		return (File) getData();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
//		Image img = getImage();
//		if(img != null)
//			img.dispose();
		setImage(getDirImage());
	}

	public void addMenuListener(IMenuManager manager) {
		NewMdDirAction newdirAction = new NewMdDirAction();
		manager.add(newdirAction);
		NewMDPFileAction newmdpFileAction = new NewMDPFileAction();
		manager.add(newmdpFileAction);
		NewBpfFileAction newbpfFileAction = new NewBpfFileAction();
		manager.add(newbpfFileAction);
		ImportMdFile importAction = new ImportMdFile();
		manager.add(importAction);
		if(!(parentItem instanceof LFWBusinessCompnentTreeItem) && !(parentItem instanceof LFWProjectTreeItem)){
		DeleteAction deleteAction = new DeleteAction();
		manager.add(deleteAction);
		}
	}

	public void deleteNode() {
		File folder = getFile();
		if(folder!=null){
			FileUtilities.deleteFile(folder);
		}		
		LFWAMCPersTool.refreshCurrentPorject();
		dispose();
	}

}
