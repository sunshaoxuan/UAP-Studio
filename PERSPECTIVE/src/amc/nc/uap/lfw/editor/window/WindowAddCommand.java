/**
 * 
 */
package nc.uap.lfw.editor.window;

import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetGraph;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.application.ApplicationGraph;
import nc.uap.lfw.lang.M_editor;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 * Application业务组件工具箱——选择Window命令处理类
 * @author chouhl
 *
 */
public class WindowAddCommand extends Command {

	private WindowConfigObj obj;

	private LfwBaseGraph graph;
	
	private Rectangle rect;

	public WindowAddCommand(WindowConfigObj obj, LfwBaseGraph graph, Rectangle rect) {
		super();
		this.obj = obj;
		this.graph = graph;
		this.rect = rect;
		setLabel("add exist Window"); //$NON-NLS-1$
	}

	public boolean canExecute() {
		return obj != null && graph != null && rect != null;
	}

	public void execute() {
		WindowConfigObj windowObj = (WindowConfigObj) obj;
		WindowListDialog dialog = new WindowListDialog(M_editor.WindowAddCommand_0, graph.getProject(), graph.getCurrentTreeItem());
		int result = dialog.open();
		if(result == IDialogConstants.OK_ID){
			WindowConfig window = new WindowConfig();
			window.setId(dialog.getWindowId());
			window.setCaption(dialog.getWindowName());
			windowObj.setWindowConfig(window);
		}else{
			return;
		}
		boolean isNotExist = true;
		if(graph instanceof ApplicationGraph){
			List<WindowConfig> windowList = ((ApplicationGraph)graph).getApplication().getWindowList();
			if(windowList != null && windowList.size() > 0){
				for(WindowConfig win : windowList){
					if(obj.getWindowConfig().getId().equals(win.getId())){
						isNotExist = false;
						break;
					}
				}
			}
			if(isNotExist){
				ApplicationEditor editor = ApplicationEditor.getActiveEditor();
				editor.repaintWindowObj(obj);
				redo();
				editor.setDirtyTrue();
			}else{
				MessageDialog.openInformation(null, WEBPersConstants.ADD_WINDOW, M_editor.WindowAddCommand_1+obj.getWindowConfig().getId()+M_editor.WindowAddCommand_2);
			}
		}
//		else if(graph instanceof PagemetaGraph){
//			List<PageMeta> windowList = ((PagemetaGraph)graph).getPagemeta().getInlineWindowList();
//			if(windowList != null && windowList.size() > 0){
//				for(PageMeta win : windowList){
//					if(obj.getWindow().getId().equals(win.getId())){
//						isNotExist = false;
//						break;
//					}
//				}
//			}
//			if(isNotExist){
//				PagemetaEditor editor = PagemetaEditor.getActivePagemetaEditor();
//				redo();
//				editor.repaintGraph();				
//				editor.setDirtyTrue();
//			}else{
//				MessageDialog.openInformation(null, WEBPersConstants.ADD_WINDOW, "已存在ID为："+obj.getWindow().getId()+"的Window");
//			}
//		}
		else if(graph instanceof WidgetGraph){
			WindowConfig[] windowList = ((WidgetGraph)graph).getWidget().getInlineWindows();
			if(windowList.length > 0){
				for(WindowConfig win : windowList){
					if(obj.getWindowConfig().getId().equals(win.getId())){
						isNotExist = false;
						break;
					}
				}
			}
			if(isNotExist){
				WidgetEditor editor = WidgetEditor.getActiveWidgetEditor();
				redo();
				editor.repaintGraph();				
				editor.setDirtyTrue();
			}else{
				MessageDialog.openInformation(null, WEBPersConstants.ADD_WINDOW, M_editor.WindowAddCommand_1+obj.getWindowConfig().getId()+M_editor.WindowAddCommand_2);
			}
		}
	}

	public void redo() {
		if(graph instanceof ApplicationGraph){
			((ApplicationGraph)graph).addWindowCell(obj);
			((ApplicationGraph)graph).getApplication().addWindow(obj.getWindowConfig());
		}
		else if(graph instanceof WidgetGraph){
			((WidgetGraph)graph).getWidget().addInlineWindow(obj.getWindowConfig());
		}
	}

}
