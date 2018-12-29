/**
 * 
 */
package nc.uap.lfw.window.view;

import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.widget.WidgetFromPoolDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 * 从PublicView引用View行为类
 * @author chouhl
 *
 */
public class ViewNodeFromPublicViewAction extends NodeAction {

	public ViewNodeFromPublicViewAction() {
		super(WEBPersConstants.NEW_VIEW_FROM_PUBLIC_VIEW, PaletteImage.getCreateDsImgDescriptor());
	}
	
	public void run() {
		createViewNodeFromPublicView();
	}
	
	private void createViewNodeFromPublicView(){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null){
			return;
		}
		WidgetFromPoolDialog dialog = new WidgetFromPoolDialog(null, WEBPersConstants.NEW_VIEW_FROM_PUBLIC_VIEW);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID){
			LfwView refWidget = dialog.getSelectedWidget();
			if (refWidget != null ) {
				createView(refWidget);
			}
		}
	}
	
	private void createView(LfwView refWidget){
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		LfwWindow window = LFWAMCPersTool.getCurrentPageMeta();
		if(view == null || window == null){
			return;
		}
		try {
//			String refId = refWidget.getRefId();
			String refId = refWidget.getId();
			LfwView widget = null;
			widget = new LfwView();
			widget.setId(refId);
			String realRefid = "";
			if(refWidget.getComponentId()==null||"".equals(refWidget.getComponentId())||refWidget.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
				realRefid = refId;
			else realRefid = refWidget.getComponentId()+"."+refId;
			widget.setRefId("../" + realRefid);
			widget.setControllerClazz("");
			widget.setSourcePackage("");
			if(window.getView(widget.getId()) != null){
				MessageDialog.openInformation(null, WEBPersConstants.NEW_VIEW, "ID为" + widget.getId() + "的View节点已存在!");
				return;
			}
			
			ViewConfig wconf = new ViewConfig();
			wconf.setId(widget.getId());
			wconf.setRefId("../" + realRefid);
			window.addViewConfig(wconf);
			//保存该view到pagemeta.pm文件
			LFWSaveElementTool.savePagemeta(window);
			// 添加节点
//			RefreshNodeAction.refreshNode(view, LFWAMCPersTool.getTree());
			view.addViewTreeNode(widget); 
			// 刷新打开的Window编辑器页面
			PagemetaEditor.refreshPagemetaEditor();
		} catch (Exception e) {
			MainPlugin.getDefault().logError("创建View节点失败:" + e.getMessage(), e);
			MessageDialog.openError(null, WEBPersConstants.NEW_VIEW, "创建View节点失败:" + e.getMessage());
		}
	}
	
}
