/**
 * 
 */
package nc.uap.lfw.editor.window;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.editor.application.ApplicationGraph;
import nc.uap.lfw.editor.application.ApplicationPart;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author chouhl
 *
 */
public class DeleteWindowObjAction extends NodeAction {
	
	public DeleteWindowObjAction(){
		super(WEBPersConstants.DEL_WINDOW, PaletteImage.getDeleteImgDescriptor());
	}

	@Override
	public String getId() {
		return ActionFactory.DELETE.getId();
	}
	
	@Override
	public void run() {
		deleteElementObj();
	}
	
	private void deleteElementObj(){
		ApplicationEditor appEditor = ApplicationEditor.getActiveEditor();
		if(appEditor != null){
			ApplicationGraph graph = appEditor.getGraph();
			if (appEditor.getCurrentSelection() != null) {
				StructuredSelection ss = (StructuredSelection)appEditor.getCurrentSelection();
				Object sel = ss.getFirstElement();
				if (sel instanceof ApplicationPart) {
					ApplicationPart appPart = (ApplicationPart)sel;
					Object model = appPart.getModel();
					if (model instanceof WindowConfigObj) {
						WindowConfigObj winObj = (WindowConfigObj) model;
						List<WindowConfigObj> objList = appEditor.getGraph().getWindowCells();
						if(objList != null && objList.size() > 0){
							boolean isChanged = false;
							List<WindowConfig> windowList = new ArrayList<WindowConfig>();
							for(int i = objList.size() - 1; i >= 0; i--){
								if(objList.get(i).getWindowConfig().getId().equals(winObj.getWindowConfig().getId())){
									graph.removeWindowCell(winObj);
									isChanged = true;
									continue;
								}
								windowList.add(objList.get(i).getWindowConfig());
							}
							if(isChanged){
								Application application = appEditor.getGraph().getApplication();
								application.setWindowList(windowList);
							}
						}
						appEditor.setDirtyTrue();
					}
				}
			}
		}
	}
	
}
