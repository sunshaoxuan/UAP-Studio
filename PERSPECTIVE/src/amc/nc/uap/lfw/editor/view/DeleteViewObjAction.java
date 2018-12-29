/**
 * 
 */
package nc.uap.lfw.editor.view;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.pagemeta.PagemetaEditor;
import nc.lfw.editor.pagemeta.PagemetaElementPart;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.parts.LFWConnectinPart;
import nc.uap.lfw.perspective.action.DeleteWidgetAction;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.actions.ActionFactory;

/**
 * @author chouhl
 *
 */
public class DeleteViewObjAction extends NodeAction {
	
	public DeleteViewObjAction(){
		super(WEBPersConstants.DEL_VIEW, PaletteImage.getDeleteImgDescriptor());
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
		PagemetaEditor winEditor = PagemetaEditor.getActivePagemetaEditor();
		if(winEditor != null){
			if (winEditor.getCurrentSelection() != null) {
				StructuredSelection ss = (StructuredSelection)winEditor.getCurrentSelection();
				Object sel = ss.getFirstElement();
				if (sel instanceof PagemetaElementPart) {
					PagemetaElementPart pmPart = (PagemetaElementPart)sel;
					Object model = pmPart.getModel();
					if (model instanceof WidgetElementObj) {
						WidgetElementObj viewObj = (WidgetElementObj) model;
						LfwView delWidget = viewObj.getWidget();
						DeleteWidgetAction delWidgetAction = new DeleteWidgetAction();
						delWidgetAction.setSelectedWidget(delWidget);
						delWidgetAction.setInPagemetaEditor(true);
						delWidgetAction.run();
					}
				}
				else if (sel instanceof LFWConnectinPart){
					LFWConnectinPart connPart = (LFWConnectinPart)sel;
					Connection conn = (Connection)connPart.getModel();
					conn.disConnect();
					LfwWindow pagemeta = winEditor.getGraph().getPagemeta();
					pagemeta.getConnectorMap().remove(conn.getId());
					winEditor.setDirtyTrue();
				}
			}
		}
	}
	
}
