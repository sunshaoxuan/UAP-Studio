package nc.uap.lfw.perspective.webcomponent;

import java.io.File; 

import nc.lfw.editor.common.LfwBaseEditorInput;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.editor.common.tools.LFWTool;
import nc.uap.lfw.perspective.project.ILFWTreeNode;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LFWBasicTreeItem extends TreeItem implements ILFWTreeNode{
	
//	private String lfwVersion = "OLD_VERSION";
	
	private String id = null;
	
	private String itemName = null;

	public LFWBasicTreeItem(TreeItem parentItem, int style) {
		super(parentItem, style);
		// TODO Auto-generated constructor stub
	}
	
	public LFWBasicTreeItem(TreeItem parentItem, int style, int index) {
		super(parentItem, style, index);
		// TODO Auto-generated constructor stub
	}

	public LFWBasicTreeItem(Tree parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	private LfwBaseEditorInput editorInput;

	public LfwBaseEditorInput getEditorInput() {
		return editorInput;
	}

	public void setEditorInput(LfwBaseEditorInput editorInput) {
		this.editorInput = editorInput;
	}

	public void deleteNode() {
	}

	public File getFile() {
		return null;
	}

	public String getIPathStr() {
		return null;
	}
	
	/**
	 * 增加右键菜单
	 * 
	 * @param manager
	 */
	public void addMenuListener(IMenuManager manager){
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
	}

//	public String getLfwVersion() {
//		TreeItem parent = getParentItem();
//		while(parent instanceof LFWBasicTreeItem && !parent.getClass().getName().equals(LFWBasicTreeItem.class.getName())){
//			if(parent.getText().equals(WEBProjConstants.APPLICATION) || parent.getText().equals(WEBProjConstants.MODEL) || parent.getText().equals(WEBProjConstants.WINDOW) || parent.getText().equals(WEBProjConstants.PUBLIC_VIEW)){
//				lfwVersion = LFWTool.NEW_VERSION;
//				break;
//			}
//			parent = parent.getParentItem();
//		}
//		return lfwVersion;
//	}
//
//	public void setLfwVersion(String lfwVersion) {
//		this.lfwVersion = lfwVersion;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}
