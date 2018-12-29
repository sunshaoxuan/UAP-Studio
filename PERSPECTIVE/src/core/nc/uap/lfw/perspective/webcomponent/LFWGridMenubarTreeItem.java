package nc.uap.lfw.perspective.webcomponent;

import nc.lfw.editor.menubar.action.DelMenubarAction;
import nc.uap.lfw.common.action.LFWCopyAction;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.TreeItem;

public class LFWGridMenubarTreeItem extends LFWWebComponentTreeItem{

	private boolean fromWidget;
	
	private String gridId;
	
	public boolean isFromWidget() {
		return fromWidget;
	}

	public void setFromWidget(boolean fromWidget) {
		this.fromWidget = fromWidget;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public LFWGridMenubarTreeItem(TreeItem parentItem, MenubarComp menubarItem) {
		super(parentItem, "[Menubar]", menubarItem);
		setData(menubarItem);
		setText(menubarItem.getId());
		setImage(super.getImage());
	}
	
	public LFWGridMenubarTreeItem(TreeItem parentItem, String type, MenubarComp menubarItem) {
		super(parentItem, "[Menubar]", menubarItem);
		setData(menubarItem);
		setText(type + menubarItem.getId());
		setImage(super.getImage());
	}
	
	public void addMenuListener(IMenuManager manager){
		DelMenubarAction delMenubarAction = new DelMenubarAction();
		manager.add(delMenubarAction);
		
//		LFWCopyAction copyAction = new LFWCopyAction(WEBPersConstants.MENUBAR_CN);
//		manager.add(copyAction);
	} 

	public void mouseDoubleClick(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
//		LFWMenubarCompTreeItem menubar = (LFWMenubarCompTreeItem) ti;
		if(getParentItem() instanceof LFWSeparateTreeItem && (getParentItem().getText().equals(WEBPersConstants.MENUBAR_CN) || getParentItem().getText().equals(WEBPersConstants.COMPONENTS_EN)))
			setFromWidget(true);
		else 
			setFromWidget(false);
		view.openGridMenubarEditor(this);
} 
}
