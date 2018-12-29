package nc.uap.lfw.perspective.action;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýÊ÷½ÚµãÃüÁî
 * @author zhangxya
 *
 */
public class DeleteTreeAction  extends Action {
	public DeleteTreeAction() {
		super(M_perspective.DeleteTreeAction_0, PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_TREE);
		setToolTipText(WEBPersConstants.DEL_TREE);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_TREE;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
