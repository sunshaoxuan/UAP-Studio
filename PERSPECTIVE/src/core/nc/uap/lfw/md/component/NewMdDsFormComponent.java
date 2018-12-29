package nc.uap.lfw.md.component;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.project.LFWPerspectiveNameConst;
import nc.uap.lfw.perspective.webcomponent.LFWWidgetTreeItem;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;


/**
 * 从元数据的组件引入所有的ds
 * @author zhangxya
 *
 */
public class NewMdDsFormComponent extends Action{
	
	public NewMdDsFormComponent() {
		super(WEBPersConstants.NEW_DS_FORM_COMPONENT);
	}

	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell();
		MDComponentDialog componetDialog = new MDComponentDialog(shell, "引用" + LFWPerspectiveNameConst.MD_COMPONENT);
		int result = componetDialog.open();
		if (result == IDialogConstants.OK_ID){
			LfwView widget = componetDialog.getWidget();
			LFWPersTool.saveWidget(widget);
			TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
			if(treeItem instanceof LFWWidgetTreeItem){
				LFWWidgetTreeItem widgetTreeItem = (LFWWidgetTreeItem) treeItem;
				widgetTreeItem.setWidget(widget);
				view.dealCurrentWidgetTreeItem(widgetTreeItem);
			}
		}
		else 
			return;

	}
}

