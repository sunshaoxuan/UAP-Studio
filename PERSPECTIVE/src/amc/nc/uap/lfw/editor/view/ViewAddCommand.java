/**
 * 
 */
package nc.uap.lfw.editor.view;

import java.io.File;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.PagemetaGraph;
import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.common.tools.LFWSaveElementTool;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.window.view.CreateNormalViewDialog;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author chouhl
 *
 */
public class ViewAddCommand extends Command {

	private WidgetElementObj wdObj;
	private boolean canUndo = true;
	private PagemetaGraph graph;

	private Rectangle rect;

	public ViewAddCommand(WidgetElementObj wdObj, PagemetaGraph graph, Rectangle rect) {
		super();
		this.wdObj = wdObj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new view");
	}

	public boolean canExecute() {
		return wdObj != null && graph != null && rect != null;
	}
	
	public void execute() {
		CreateNormalViewDialog dialog = new CreateNormalViewDialog(WEBPersConstants.NEW_VIEW);
		if(dialog.open() == IDialogConstants.OK_ID){
			LfwView widget = new LfwView();
			widget.setId(dialog.getId().trim());
			widget.setRefId(dialog.getId().trim());
			widget.setCaption(dialog.getName().trim());
			widget.setControllerClazz(dialog.getControllerClazz().trim());
			widget.setSourcePackage(dialog.getSourcePackage());
			wdObj.setWidget(widget);
		}else{
			return;
		}
		int realNum = graph.elementsCount;
		int num = realNum - 2;
		wdObj.setSize(new Dimension(100,100));
		int pointY = num / 2 * 200 - 150;
		Point point = new Point(400, pointY);
		if (num % 2 == 0){
			point.x = 100;
		}
		wdObj.setLocation(point);
		wdObj.setGraph(graph);
		redo();
		// 获取PagemetaEditor
		PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
		// 重新设置页面状态图标显示位置
//		editor.adjustPageStateCell(graph);
		// 重新设置JsListener图标显示位置
//		editor.repaintListenerPositon();
		// 生成并保存widget.wd文件
		editor.addTempWidget(wdObj.getWidget());
		// 保存该Widget到pagemeta.pm文件中
		LfwWindow pagemeta = graph.getPagemeta();
		LfwView widgetConf = new LfwView();
		widgetConf.setId(wdObj.getWidget().getId());
		widgetConf.setRefId(wdObj.getWidget().getRefId());
		widgetConf.setCaption(wdObj.getWidget().getCaption());
		widgetConf.setSourcePackage(wdObj.getWidget().getSourcePackage());
		widgetConf.setControllerClazz(wdObj.getWidget().getControllerClazz());
		if(pagemeta.getWidget(widgetConf.getId()) != null){
			MessageDialog.openInformation(null, WEBPersConstants.NEW_VIEW, "ID为" + widgetConf.getId() + "的View节点已存在!");
			return;
		}
		
		ViewConfig wconf = new ViewConfig();
		wconf.setId(widgetConf.getId());
		wconf.setRefId(wdObj.getWidget().getRefId());
		pagemeta.addViewConfig(wconf);
		pagemeta.addWidget(wdObj.getWidget());
		
		UIMeta meta = new UIMeta();
		String folderPath = LFWPersTool.getCurrentFolderPath();
		String filePath = folderPath + File.separator + widgetConf.getId();
		String fp = filePath.replaceAll("\\\\", "/");
		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
		meta.setAttribute(UIMeta.ID, id);
		meta.setFlowmode(dialog.isFlowlayout());
		
		//保存view节点到文件
		LFWSaveElementTool.createView(widgetConf, meta);
		//保存该view到pagemeta.pm文件
		LFWSaveElementTool.savePagemeta(pagemeta);
		// 刷新节点
		RefreshNodeAction.refreshNode(LFWExplorerTreeView.getLFWExploerTreeView(null), LFWAMCPersTool.getTree());
		// 刷新打开的Window编辑器页面
		PagemetaEditor.refreshPagemetaEditor();
		
//		editor.save();
//		// 刷新树中该节点
//		editor.refreshTreeNode();
	}

	public void redo() {
		graph.addWidgetCell(wdObj);
	}

	public void undo() {
		graph.removeWidgetCell(wdObj);
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	public boolean canUndo() {
		return isCanUndo();
	}
	
}
