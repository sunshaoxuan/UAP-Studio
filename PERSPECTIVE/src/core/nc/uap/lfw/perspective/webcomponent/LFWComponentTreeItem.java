package nc.uap.lfw.perspective.webcomponent;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.uap.lfw.component.EditComponentAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.CreateVirtualFolderAction;
import nc.uap.lfw.core.base.DeleteVirtualFolderAction;
import nc.uap.lfw.core.base.RefreshAMCNodeGroupAction;
import nc.uap.lfw.core.dev.LfwComponent;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.editor.extNode.LangResourceAction;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.publicview.PublicViewNodeAction;
import nc.uap.lfw.window.WindowNodeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class LFWComponentTreeItem extends LFWDirtoryTreeItem{

	private LfwUIComponent component = null;
	
	private String type = null;
	
	
	public LfwUIComponent getComponent() {
		return component;
	}
	public void setComponent(LfwUIComponent component) {
		this.component = component;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LFWComponentTreeItem(TreeItem parentItem, Object object, LfwUIComponent component,String text) {
		super(parentItem, object, text);
		this.component = component;
	}
	protected Image getDirImage() {
		return ImageProvider.skin;
	}
	public void addMenuListener(IMenuManager manager) {
		LFWDirtoryTreeItem item = (LFWDirtoryTreeItem) LFWPersTool.getTopList(LFWPersTool.getCurrentTreeItem());
		if (!item.getType().equals(LFWDirtoryTreeItem.MLR_FOLDER)) {
			EditComponentAction action = new EditComponentAction();
			manager.add(action);
			if(type.equals(ILFWTreeNode.WINDOW)){
				// 新建
				WindowNodeAction windowNodeAction = new WindowNodeAction();
				// 新建虚拟目录
				CreateVirtualFolderAction createVirFolderAction = new CreateVirtualFolderAction(
						type);
				// 删除虚拟目录
				DeleteVirtualFolderAction deleteVirFolderAction = new DeleteVirtualFolderAction();
				// 刷新
				RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
						WEBPersConstants.AMC_WINDOW_PATH, ILFWTreeNode.WINDOW,
						WEBPersConstants.AMC_WINDOW_FILENAME,getBcp());
				// 右键菜单功能
				manager.add(windowNodeAction);
	//			manager.add(createVirFolderAction);
				manager.add(refreshAMCNodeGroupAction);
			}
			else if(type.equals(ILFWTreeNode.PUBLIC_VIEW)){
				// 新建
				PublicViewNodeAction publicViewAction = new PublicViewNodeAction();
				// RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new
				// RefreshAMCNodeGroupAction(WEBProjConstants.AMC_PUBLIC_VIEW_PATH,
				// ILFWTreeNode.PUBLIC_VIEW,
				// WEBProjConstants.AMC_PUBLIC_VIEW_FILENAME);
				RefreshAMCNodeGroupAction refreshAMCNodeGroupAction = new RefreshAMCNodeGroupAction(
						WEBPersConstants.AMC_PUBVIEW_PATH, ILFWTreeNode.PUBLIC_VIEW,
						WEBPersConstants.AMC_WINDOW_FILENAME,getBcp());
				// 右键菜单功能
				manager.add(publicViewAction);
				manager.add(refreshAMCNodeGroupAction);
			}
		}
		
	}
//	public void mouseDoubleClick() {
//		if(this.getItemCount()==0){
//			File windowFile = new File(this.getFile(), WEBPersConstants.AMC_WINDOW_PATH);
//			LFWDirtoryTreeItem windowNode = new LFWDirtoryTreeItem(this,windowFile, WEBPersConstants.WINDOW);
//			windowNode.setType(LFWDirtoryTreeItem.WINDOW_FOLDER);
//			windowNode.setBcp(bcp);
//			Map<String, LfwWindow> windowMap = component.getWindowMap();
//			if(windowMap!=null){
//				Iterator<String> iter = windowMap.keySet().iterator();
//				while(iter.hasNext()){
//					String windowId = iter.next();
//					LfwWindow win = windowMap.get(windowId);
//					String path = win.getFoldPath();
//					File file = new File(path);
//					LFWPageMetaTreeItem pmItem = new LFWPageMetaTreeItem(windowNode, file, win.getCaption());
//					pmItem.setPm(win);
//					LFWDirtoryTreeItem direc = (LFWDirtoryTreeItem) pmItem;
//					direc.setId(win.getId());
//					direc.setItemName(win.getCaption());
//					direc.setType(ILFWTreeNode.WINDOW);
//				}
//			}
//		}
//	}
	
}
