package nc.uap.lfw.chart.actions;

import nc.uap.lfw.chart.core.ChartEditor;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_chart;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWWebComponentTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewChartAction  extends Action {

	private class AddChartCommand extends Command{
		public AddChartCommand(){
			super(M_chart.NewChartAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	public NewChartAction() {
		super(WEBPersConstants.NEW_CHART, PaletteImage.getCreateGridImgDescriptor());
	}

	
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_CHART,M_chart.NewChartAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
			String dirName = input.getValue();
			if(dirName != null && dirName.trim().length()>0){
				dirName =dirName.trim();
				try {
					LFWWebComponentTreeItem chart = (LFWWebComponentTreeItem)view.addChartTreeNode(dirName);
					//´ò¿ªds±à¼­Æ÷
					view.openChartEditor(chart);
				} catch (Exception e) {
					String title =WEBPersConstants.NEW_CHART;
					String message = e.getMessage();
					MessageDialog.openError(shell, title, message);
				}
			}
			else {
				MessageDialog.openError(shell, M_chart.NewChartAction_3, M_chart.NewChartAction_4);
				return;
			}
			AddChartCommand cmd = new AddChartCommand();
			if(ChartEditor.getActiveEditor() != null)
				ChartEditor.getActiveEditor().executComand(cmd);
		}
		else return;
		
	}

}
