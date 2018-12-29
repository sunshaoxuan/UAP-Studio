package nc.uap.lfw.progressbar.commands;

import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DelProjessBarAction extends Action {
	public DelProjessBarAction() {
		super("删除", PaletteImage.getDeleteImgDescriptor());
		setText("删除进度条");
		setToolTipText("删除进度条");
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		try {
			if(view != null)
				view.deleteSelectedWebComponentNode();
		} catch (Exception e) {
			Shell shell = new Shell(Display.getCurrent());
			String title ="删除进度条";
			String message = e.getMessage();
			MessageDialog.openError(shell, title, message);
		}
	}
	

}
