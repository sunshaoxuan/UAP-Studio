/**
 * 
 */
package nc.uap.lfw.publicview;

import java.io.File;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.LFWAMCTreeItem;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.perspective.project.ILFWTreeNode;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
//import nc.uap.lfw.widget.DelPoolWidgetAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * PublicView TreeItem类
 * @author chouhl
 *
 */
public class LFWPublicViewTreeItem extends LFWAMCTreeItem {
	
	/**
	 * View元素
	 */
	private LfwView widget = null;
	
	public LFWPublicViewTreeItem(TreeItem parentItem, File file, LfwView widget, String text) {
		super(parentItem, file, text);
		this.widget = widget;
	}
	
	public LFWPublicViewTreeItem(TreeItem parentItem, File file, LfwView widget) {
		super(parentItem, file, null);
		this.widget = widget;
	}
	
	public LFWPublicViewTreeItem(TreeItem parentItem, File file, String text){
		super(parentItem, file, text);
	}
	
	protected void checkSubclass() {}

	public File getFile() {
		return (File)getData();
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
		return parentIPath + "/" + getFile().getName();
	}
	
	public void addMenuListener(IMenuManager manager){
		final LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		// UI设计
		final MenuManager uiMenuManager = new MenuManager(WEBPersConstants.UI_DESIGN);
		uiMenuManager.setRemoveAllWhenShown(true);
//		uiMenuManager.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				DeleteUIGuildAction deleteGuild = new DeleteUIGuildAction();
//				deleteGuild.setSite(view.getViewSite());
//				UIGuideAction uiGuild = new UIGuideAction();
//				uiGuild.setSite(view.getViewSite());
//				ViewUIGuideAction viewUIGuide = new ViewUIGuideAction();
//				manager.add(viewUIGuide);
////				manager.add(uiGuild);
//				manager.add(deleteGuild);
//			}
//		});
		
//		EditWidgetAction editWidgetAction = new EditWidgetAction();
//		RenameWidgetAction renameWidgetAction = new RenameWidgetAction();
//		editWidgetAction.setTreeItem(this);
		// 删除
//		DelPoolWidgetAction delWidgetAction = new DelPoolWidgetAction();
		// 右键菜单功能
		manager.add(uiMenuManager);
//		manager.add(editWidgetAction);
//		manager.add(renameWidgetAction);
//		manager.add(delWidgetAction);
	} 
	
	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view.hasOpened(this)){
			return;
		}
		if(LFWDirtoryTreeItem.POOLWIDGETFOLDER.equals(getType())){
			view.refreshPubWidgetTreeItem(this);
		}else{
			view.refreshWidgetTreeItem(this);
		}
	}
	
	public LfwView getWidget() {
		return widget;
	}
	public void setWidget(LfwView widget) {
		this.widget = widget;
	}
	
}
