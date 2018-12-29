package nc.uap.lfw.perspective.action;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;
import nc.uap.lfw.perspective.webcomponent.LFWDSTreeItem;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建DS处理类
 * @author zhangxya
 *
 */
public class NewDsAction extends Action {
	
	public static String DEFAULTLISTENER = "defaultDsListener"; //$NON-NLS-1$
	public static String DEFAULTSERVER = "nc.uap.lfw.core.event.deft.DefaultDatasetServerListener"; //$NON-NLS-1$
	
	private class AddDSCommand extends Command{
		public AddDSCommand(){
			super(M_perspective.NewDsAction_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}

	public NewDsAction() {
		super(WEBPersConstants.NEW_DATASET, PaletteImage.getCreateDsImgDescriptor());
	}

	
	
	@SuppressWarnings("static-access")
	public void run() {
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view == null)
			return;
		Shell shell = new Shell(Display.getCurrent());
		InputDialog input = new InputDialog(shell, WEBPersConstants.NEW_DATASET,M_perspective.NewDsAction_1,"", null); //$NON-NLS-2$
		if(input.open() == InputDialog.OK){
				String dirName = input.getValue();
				if(dirName != null && dirName.trim().length()>0){
					dirName =dirName.trim();
					try {
						LFWDSTreeItem ds = (LFWDSTreeItem)view.addDSTreeNode(dirName, null);
						//添加默认的DsListener
//						DatasetListener dsListener = new DatasetListener();
//						EventHandlerConf eventHandler = dsListener.getOnDataLoadEvent();
//						DatasetRule dsRule = new DatasetRule();
//						dsRule.setId(dirName);
//						dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
//						WidgetRule widgetRule = new WidgetRule();
//						widgetRule.setId(LFWPersTool.getCurrentWidget().getId());
//						widgetRule.addDsRule(dsRule);
//						eventHandler.getSubmitRule().addWidgetRule(widgetRule);
//						eventHandler.setOnserver(true);
//						dsListener.addEventHandler(eventHandler);
//						dsListener.setId(DEFAULTLISTENER);
//						dsListener.setServerClazz(DEFAULTSERVER);
//						ds.getDs().addListener(dsListener);
						//打开ds编辑器
						view.openDsEdit(ds);
					} catch (Exception e) {
						MainPlugin.getDefault().logError(e.getMessage(), e);
						String title =WEBPersConstants.NEW_DATASET;
						String message = e.getMessage();
						MessageDialog.openError(shell, title, message);
					}
				}
				else {
					MessageDialog.openError(shell, M_perspective.NewDsAction_2, M_perspective.NewDsAction_3);
					return;
				}
			AddDSCommand cmd = new AddDSCommand();
			if(DataSetEditor.getActiveEditor() != null)
				DataSetEditor.getActiveEditor().executComand(cmd);
			}
			
		else return;
		}
	}

