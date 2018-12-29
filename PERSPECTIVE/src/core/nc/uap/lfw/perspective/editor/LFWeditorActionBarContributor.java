package nc.uap.lfw.perspective.editor;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;


/**
 * Ö§³Ö¼üÅÌÉ¾³ý²Ù×÷
 * @author zhangxya
 *
 */
public class LFWeditorActionBarContributor extends ActionBarContributor {
	public LFWeditorActionBarContributor() {
	}
	
	protected void buildActions() {
//		addRetargetAction(new UndoRetargetAction());
//		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
//		addRetargetAction(new ZoomInRetargetAction());
//		addRetargetAction(new ZoomOutRetargetAction());
	}
	
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
//		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
//		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
//		toolBarManager.add(new Separator());
//		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
//		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
//		toolBarManager.add(new ZoomComboContributionItem(getPage()) );
		
	}
	protected void declareGlobalActionKeys() {
	}


}
