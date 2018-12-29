package nc.uap.lfw.perspective.webcomponent;

import java.io.File;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.common.action.LFWCopyAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.core.refnode.SelfDefRefNode;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.refnode.core.DeleteRefNodeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWRefNodeTreeItem extends LFWBasicTreeItem implements ILFWTreeNode {
	
	public LFWRefNodeTreeItem(TreeItem parentItem, IRefNode refnode, String name) {
		super(parentItem, SWT.NONE);
		setData(refnode);
		String id = refnode.getId();
		if(id.indexOf(".") != -1)
			id = id.substring(id.lastIndexOf(".") + 1);
		String caption = null;
		if(refnode instanceof BaseRefNode){
			caption = ((BaseRefNode)refnode).getText();
		}
		if(caption != null && !"".equals(caption))
			setText(name + id + "[" + caption + "]");
		else
			setText(name + id);
		setImage(getFileImage());
	}

	protected void checkSubclass () {
	}
	private Image getFileImage() {
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "refnode.gif");
		return ImageProvider.refnode;
	}

		
	public File getFile() {
		return (File) getData();
	}

	public void deleteNode() {
		FileUtilities.deleteFile(getFile());
		dispose();
	}
	
	public void deleteNode(LFWRefNodeTreeItem refnode) {
		LfwView lfwwidget = LFWPersTool.getLfwWidget();
		if(lfwwidget != null){
			IRefNode web = (IRefNode)refnode.getData();
			IRefNode refnodedata = lfwwidget.getViewModels().getRefNode(web.getId());
			if(refnodedata == null)
				return;
			if(refnodedata != null)
				lfwwidget.getViewModels().removeRefNode(web.getId());
			LFWPersTool.saveWidget(lfwwidget);
		}
//		else{
//			String projectPath = LFWPersTool.getProjectPath();
//			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
//			String directory = treeItem.getText();
//			IRefNode refNode = (IRefNode)refnode.getData();
//			TreeItem parent = (TreeItem)treeItem.getParentItem();
//			while (parent != null && parent instanceof LFWDirtoryTreeItem) {
//				if (parent.getText().equals("公共参照"))
//					break;
//				directory = ((LFWDirtoryTreeItem)parent).getFile().getName() + "/" + directory;
//				//treeItem = parent;
//				parent = (TreeItem)parent.getParentItem();
//			}
//			String filePath = projectPath + "/web/pagemeta/public/refnodes";
//			if(directory.lastIndexOf("/") != -1){
//				String folder = directory.substring(0, directory.lastIndexOf("/"));
//				filePath = projectPath + "/web/pagemeta/public/refnodes/" + folder;
//			}
//			String fileName = treeItem.getText().substring("[参照]".length()) + ".xml";
//			File file = new File(filePath + "/" + fileName);
//			FileUtilities.deleteFile(file);
//			LFWPersTool.getCurrentProject().getLocation();
//			//String rootPath = LFWPersTool.getProjectModuleName(project);
//			String rootPath = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
//			LFWConnector.removeRefNodeFromPool("/" + rootPath, refNode);
//		}
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
		//拷贝refnode命令
		LFWCopyAction coypAction = new LFWCopyAction(WEBPersConstants.REFNODE);
		manager.add(coypAction);
		
		DeleteRefNodeAction deleterefnodeAction = null;
		Object data = getData();
		if(data instanceof SelfDefRefNode){
			deleterefnodeAction = new DeleteRefNodeAction();
		}else if(data instanceof NCRefNode){
			if(((NCRefNode)data).getFrom() != null){
				return;
			}else{
				deleterefnodeAction = new DeleteRefNodeAction();
			}
		}else if(data instanceof RefNode){
			if(((RefNode)data).getFrom() != null){
				return;
			}else{
				deleterefnodeAction = new DeleteRefNodeAction();
			}
		}else if(data instanceof IRefNode){
			deleterefnodeAction = new DeleteRefNodeAction();
		}
		if(deleterefnodeAction != null){
			manager.add(deleterefnodeAction);
		}
	}
	
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);		
		TreeItem parentDs = getParentItem();
		if(parentDs instanceof LFWSeparateTreeItem)
			view.openRefNodeEditor(this);
		else 
			view.openPoolRefNodeEdit(this);
	}

}

