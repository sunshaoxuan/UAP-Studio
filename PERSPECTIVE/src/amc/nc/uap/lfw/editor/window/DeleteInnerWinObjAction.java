package nc.uap.lfw.editor.window;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;

import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementPart;
import nc.lfw.editor.widget.WidgetGraph;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.application.ApplicationPart;
import nc.uap.lfw.palette.PaletteImage;

public class DeleteInnerWinObjAction extends NodeAction{

	WindowConfigObj windowObj;
	public DeleteInnerWinObjAction(WindowConfigObj win){
		super(WEBPersConstants.DEL_WINDOW, PaletteImage.getDeleteImgDescriptor());
		windowObj = win;
	}
	@Override
	public void run() {
		deleteElementObj();
	}
	private void deleteElementObj(){
		WidgetEditor viewEditor = WidgetEditor.getActiveWidgetEditor();
		if(viewEditor!=null){
			WidgetGraph graph = viewEditor.getGraph();
			List<WindowConfigObj> objList = graph.getWindowCells();
			if(objList != null && objList.size() > 0){
				boolean isChanged = false;
				List<WindowConfig> windowList = new ArrayList<WindowConfig>();
				for(int i = objList.size() - 1; i >= 0; i--){
					if(objList.get(i).getWindowConfig().getId().equals(windowObj.getWindowConfig().getId())){
						graph.removeWindowCell(windowObj);
						isChanged = true;
						continue;
					}
					windowList.add(objList.get(i).getWindowConfig());
				}
				if(isChanged){
					LfwView view = viewEditor.getGraph().getWidget();
					view.setInlineWindowList(windowList);
				}
			}
			viewEditor.setDirtyTrue();			
		}
				
	}
	
}
