package nc.uap.lfw.perspective.action;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * É¾³ýgridÃüÁî
 * @author zhangxya
 *
 */
public class DeleteGridAction  extends Action {
	public DeleteGridAction() {
		super("É¾³ý", PaletteImage.getDeleteImgDescriptor());
		setText(WEBPersConstants.DEL_GRID);
		setToolTipText(WEBPersConstants.DEL_GRID);
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title =WEBPersConstants.DEL_GRID;
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
