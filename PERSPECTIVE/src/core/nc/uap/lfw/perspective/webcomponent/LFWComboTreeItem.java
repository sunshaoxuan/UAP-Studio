package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.combodata.commands.DeleteComboAction;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.action.LFWCopyAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * œ¬¿≠øÚtreeItem
 * @author zhangxya
 *
 */
public class LFWComboTreeItem extends LFWBasicTreeItem implements ILFWTreeNode {
	
	public LFWComboTreeItem(TreeItem parentItem, ComboData combodata, String name) {
		super(parentItem, SWT.NONE);
		setData(combodata);
		if(combodata != null){
			if(combodata.getCaption() != null && !"".equals(combodata.getCaption())){
				setText(name + combodata.getId() + "[" + combodata.getCaption() + "]");
				}
			else{
				setText(name + combodata.getId());
			}
		}
		else
			setText(name);
		setImage(getFileImage());
	}
	
	protected void checkSubclass () {
	}
	
	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "combodata.gif");
		return ImageProvider.combodata;
	}

		
	public File getFile() {
		return (File) getData();
	}

	public void deleteNode(LFWComboTreeItem combTreeItem) {
		LfwView lfwwidget = LFWPersTool.getLfwWidget();
		if(lfwwidget != null){
			ComboData combo = (ComboData)combTreeItem.getData();
			ComboData comboData = lfwwidget.getViewModels().getComboData(combo.getId());
			if(comboData == null)
				return;
			if(comboData != null)
				lfwwidget.getViewModels().removeComboData(comboData.getId());
			LFWPersTool.saveWidget(lfwwidget);
		}
		dispose();

	}
	
	
	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
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
		//øΩ±¥ds√¸¡Ó
		LFWCopyAction coypAction = new LFWCopyAction(WEBPersConstants.COMBODATA);
		manager.add(coypAction);
		ComboData combo = (ComboData)this.getData();
		if(combo.getFrom() != null)
			return;
		DeleteComboAction deleteComboAction = new DeleteComboAction();
		manager.add(deleteComboAction);
	} 
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		view.openComboEditor(this);
	} 
	
}
