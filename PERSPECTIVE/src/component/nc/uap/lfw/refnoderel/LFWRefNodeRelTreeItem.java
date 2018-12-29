package nc.uap.lfw.refnoderel;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWRefNodeRelTreeItem extends LFWBasicTreeItem implements ILFWTreeNode {
	
	private String relationId;
	
	public LFWRefNodeRelTreeItem(TreeItem parentItem, RefNodeRelation refnodeRel, String name) {
		super(parentItem, SWT.NONE);
		setData(refnodeRel);
		String id = refnodeRel.getId();
		setRelationId(id);
		if(id.indexOf(".") != -1)
			id = id.substring(id.lastIndexOf(".") + 1);
		setText(name + id);
		setImage(getFileImage());
	}

	protected void checkSubclass () {
	}
	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "refnode.gif");
		return ImageProvider.refnode;
	}
	
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

		
	public File getFile() {
		return (File) getData();
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		dispose();
	}
	
	public void deleteNode(LFWRefNodeRelTreeItem refnode) {
		LfwView lfwwidget = LFWPersTool.getLfwWidget();
		if(lfwwidget != null){
			RefNodeRelation  web = (RefNodeRelation)refnode.getData();
			RefNodeRelation refnoderef = lfwwidget.getViewModels().getRefNodeRelations().getRefNodeRelation(web.getId());
			if(refnoderef == null)
				return;
			if(refnoderef != null){
				lfwwidget.getViewModels().getRefNodeRelations().removeRefNodeRelation(web.getId());
			}
			LFWPersTool.saveWidget(lfwwidget);
		}
		dispose();
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
		DelRefNodeRelAction delRefNodeRelAction = new DelRefNodeRelAction();
		manager.add(delRefNodeRelAction);
	} 
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);		
		view.openRefNodeRelEditor(this);
	}
}

