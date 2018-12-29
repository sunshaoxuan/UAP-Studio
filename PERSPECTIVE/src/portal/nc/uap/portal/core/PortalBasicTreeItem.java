package nc.uap.portal.core;

import java.io.File;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.swt.widgets.TreeItem;

/**
 * 树节点基类
 * 
 * @author dingrf
 *
 */
public class PortalBasicTreeItem extends TreeItem implements IPortalTreeNode{

//	/**双击树节点打开editor时的EditorInput*/
	private PortalBaseEditorInput editorInput;

	/**
	 * 构造函数
	 * 
	 * @param parentItem	父节点类型为TreeItem
	 * @param style			样式
	 */
	public PortalBasicTreeItem(TreeItem parentItem, int style) {
		super(parentItem, style);
	}

	/**
	 * 构造函数
	 * 
	 * @param parentItem	父节点类型为TreeItem
	 * @param style			样式
	 * @param index			在父节点中的位置索引
	 */
	public PortalBasicTreeItem(TreeItem parentItem, int style, int index) {
		super(parentItem, style, index);
	}

	/**
	 * 构造函数
	 * 
	 * @param parentItem	父节点类型为Tree
	 * @param style			样式
	 */
	public PortalBasicTreeItem(Tree parent, int style) {
		super(parent, style);
	}

	public PortalBaseEditorInput getEditorInput() {
		return editorInput;
	}

	public void setEditorInput(PortalBaseEditorInput editorInput) {
		this.editorInput = editorInput;
	}

	/**
	 * 删除自己
	 */
	public void deleteNode() {
	}

	/**
	 * 得到树节点对应文件
	 * 
	 * @return 
	 */
	public File getFile() {
		return null;
	}

	/**
	 * 得到树节点对应文件的项目中路径
	 * 
	 * @return
	 */
	public String getIPathStr() {
		return null;
	}

	@Override
	protected void checkSubclass() {
	}

	/**
	 * 增加右键菜单
	 * 
	 * @param manager	右键菜单管理器
	 */
	public void addMenuListener(IMenuManager manager){
	}
	
	/**
	 * 双击鼠标事件
	 * 
	 */
	public void mouseDoubleClick(){
	} 
	
}
