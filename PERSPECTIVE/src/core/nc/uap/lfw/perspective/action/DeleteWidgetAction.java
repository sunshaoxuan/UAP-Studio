package nc.uap.lfw.perspective.action;

import java.io.File;
import java.util.Map;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.LFWPageMetaTreeItem;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.PagemetaGraph;
import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.lfw.editor.pagemeta.RelationEditor;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.listener.FileUtil;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * ɾ��Widget����
 * @author guoweic
 *
 */
public class DeleteWidgetAction extends NodeAction {
	
	private LfwView selectedWidget = null;
	
	private LfwView widget = null;
	
	private boolean inPagemetaEditor = false;
	
	private LfwWindow parentWindow;
	
	public DeleteWidgetAction() {
		setText(WEBPersConstants.DEL_VIEW);
		setToolTipText(WEBPersConstants.DEL_VIEW);
	}

	
	public void run() {
		String msg = WEBPersConstants.VIEW_SUB;
		if (MessageDialog.openConfirm(null, M_perspective.DeleteWidgetAction_0, M_perspective.DeleteWidgetAction_1 + msg + M_perspective.DeleteWidgetAction_2)) {
			widget = LFWPersTool.getCurrentWidget();
			if (null == widget) {
				widget = selectedWidget;
			}
			delete();
		}
	}
	
	private void delete() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		Tree tree = LFWPersTool.getTree();
		LfwWindow pm = null;
		if (inPagemetaEditor)
			pm = ((PagemetaGraph)PagemetaEditor.getActiveEditor().getGraph()).getPagemeta();
		else
			pm = LFWPersTool.getCurrentPageMeta();
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());

		try {
			
			view.closeOpenedEidtor(LFWPersTool.getCurrentTreeItem());
			// ��ȡID
			String widgetId = widget.getId();
			
			// ɾ��widget.wd�ļ������ļ�������������
			deleteWidget();
			
			//ɾ��uimeta�е�widget���
//			String winId = null;
//			if(pm.getComponentId()==null||pm.getComponentId().equals(LfwUIComponent.ANNOYUICOMPONENT))
//				winId = pm.getId();
//			else
//				winId = pm.getComponentId()+"."+pm.getId();
//			UIMeta uimeta = LFWAMCConnector.getUImetaById(winId);
//			UIView uiview = UIElementFinder.findUIWidget(uimeta, widgetId);
//			uimeta.removeElement(uiview);
//			LFWAMCConnector.createUIMeta(LFWPersTool.getCurrentFolderPath(), "uimeta.um", uimeta);
			// ɾ��Pagemeta�еĸ�Widget
//			pm.getWidgetMap().remove(widgetId);
			pm.removeView(widgetId);
			ViewConfig[] configs = pm.getViewConfigs();
			
//			PagemetaEditor editor = null;
			
			for (ViewConfig widgetConf : configs) {
				if (widgetId.equals(widgetConf.getId())) {
					pm.removeViewConfig(widgetConf);
					break;
				}
			}
			// ɾ��Pagemeta�е����Connector
			Map<String, Connector> connectorMap = pm.getConnectorMap();

			String[] ids = connectorMap.keySet().toArray(new String[0]);
			for (int i = 0, n = ids.length; i < n; i++) {
				String id = ids[i];
				Connector connector = connectorMap.get(id);
				if (widgetId.equals(connector.getSource()) || widgetId.equals(connector.getTarget())) {
					connectorMap.remove(id);
				}
			}
			
			// ���浽pagemeta.pm�ļ���
			savePagemeta(pm);
			
			if (inPagemetaEditor) {
				if(PagemetaEditor.getActivePagemetaEditor()!=null)
					PagemetaEditor.getActivePagemetaEditor().repaintGraph();
				else
					RelationEditor.getActiveRelationEditor().repaintGraph();
				PagemetaEditor.getActiveEditor().setDirtyTrue();
			} else {
				// ˢ�´򿪵�Pagemeta�༭��ҳ��
				PagemetaEditor.refreshPagemetaEditor();
			}
			
			if(LFWPersTool.getCurrentTreeItem() instanceof LFWPageMetaTreeItem){
				((LFWPageMetaTreeItem)LFWPersTool.getCurrentTreeItem()).setPm(pm);
			}
			// ˢ�½ڵ�
			RefreshNodeAction.refreshNode(view, tree);
			
		} catch (Exception e) {
			String title = WEBPersConstants.DEL_VIEW;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}

	}

	/**
	 * ����Pagemeta���ļ���
	 * @param widget
	 */
	private void savePagemeta(LfwWindow pagemeta) {
		LFWDirtoryTreeItem dirItem = LFWPersTool.getDirectoryTreeItem(LFWPersTool.getCurrentPageMetaTreeItem());
		String folderPath = ""; //$NON-NLS-1$
		if(dirItem != null) {
			File file = dirItem.getFile();
			folderPath = file.getPath();
		}
		
		String projectPath = LFWPersTool.getProjectPath();
		// ����Widget��pagemeta.pm��
		LFWConnector.savePagemetaToXml(folderPath, "pagemeta.pm", projectPath, pagemeta); //$NON-NLS-1$
	}
	
	/**
	 * ɾ��widget.wd�ļ������ļ�������������
	 */
	private void deleteWidget() {
		String folderPath = LFWPersTool.getCurrentFolderPath();
		if (null == LFWPersTool.getCurrentWidget())
			folderPath += "\\" + widget.getId(); //$NON-NLS-1$
		FileUtil.deleteFile(folderPath);
	}

	public void setSelectedWidget(LfwView selectedWidget) {
		this.selectedWidget = selectedWidget;
	}

	public void setInPagemetaEditor(boolean inPagemetaEditor) {
		this.inPagemetaEditor = inPagemetaEditor;
	}
	

}
