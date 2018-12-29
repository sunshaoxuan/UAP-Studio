package nc.lfw.editor.widget.plug;


import java.util.List;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.palette.ChildConnection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 新建plugout
 * @author dingrf
 *
 */
public class NewPlugoutDescAction extends Action {
	
	private class NewPlugoutCommand extends Command{
		private WidgetElementObj widgetObj;
		
		private WindowObj windowObj;
		
		private String id;
		
		public NewPlugoutCommand(WidgetElementObj obj, String id) {
			super(WEBPersConstants.NEW_PLUGOUTDESC);
			this.widgetObj = obj;
			this.id = id;
		}
		public NewPlugoutCommand(WindowObj obj, String id) {
			super(WEBPersConstants.NEW_PLUGOUTDESC);
			this.windowObj = obj;
			this.id = id;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			PlugoutDescElementObj plugoutObj = new PlugoutDescElementObj();
			plugoutObj.setPlugout(new PlugoutDesc());
			plugoutObj.getPlugout().setId(this.id);
			if(widgetObj!=null){
//				List<PlugoutDesc> plugoutDescs = widgetObj.getWidget().getPlugoutDescList();
//				plugoutDescs.add((PlugoutDesc) plugoutObj.getPlugout());
				widgetObj.getWidget().addPlugoutDescs((PlugoutDesc) plugoutObj.getPlugout());
				Point point = new Point();
				int count =getCount(widgetObj.getPlugoutCells())-2;
				point.x = widgetObj.getLocation().x + widgetObj.getSize().width + 50;
				point.y = widgetObj.getLocation().y + count * 40;
				ChildConnection conn = new ChildConnection(widgetObj,plugoutObj);
				conn.connect();
				plugoutObj.setConn(conn);
				plugoutObj.setLocation(point);
				plugoutObj.setSize(new Dimension(100, 30));
				plugoutObj.setWidgetObj(widgetObj);
				widgetObj.addPlugoutCell(plugoutObj);
				//Graph中加入plugout
	//			((WidgetGraph)widgetObj.getGraph()).addPlugoutCell(plugoutObj);
				WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
				wdEditor.setDirtyTrue();
			}
			else if(windowObj!=null){
//				List<IPlugoutDesc> plugoutDescs = windowObj.getWindow().getPlugoutDescList();
//			
//				plugoutDescs.add(plugoutObj.getPlugout());
				
				windowObj.getWindow().addPlugoutDesc(plugoutObj.getPlugout());
				Point point = new Point();
				int count =getCount(windowObj.getPlugoutCells());
				point.x = windowObj.getLocation().x + windowObj.getSize().width + 50;
				point.y = windowObj.getLocation().y - count * 40;
				ChildConnection conn = new ChildConnection(windowObj,plugoutObj);
				conn.connect();
				plugoutObj.setConn(conn);
				plugoutObj.setLocation(point);
				plugoutObj.setSize(new Dimension(100, 30));
				plugoutObj.setWindowObj(windowObj);
				windowObj.addPlugoutCell(plugoutObj);
			}
//			widgetObj.addPlugoutCell(plugoutObj);
		}

		public void undo() {
		}
		
	}	
	
	private int getCount(List<PlugoutDescElementObj> outs){
		int count = 0;
		if(outs.size()==0){
			return 0;
		}
		for(PlugoutDescElementObj plugoutDesc:outs){
			if(plugoutDesc.getPlugout() instanceof PlugoutDesc){
				count++;
			}
		}
		return count;
	}
	
	private WidgetElementObj widgetObj;
	
	private WindowObj windowObj;

	public NewPlugoutDescAction(WidgetElementObj obj) {
		super(WEBPersConstants.NEW_PLUGOUTDESC);
		this.widgetObj = obj;
	}
	public NewPlugoutDescAction(WindowObj obj) {
		super(WEBPersConstants.NEW_PLUGOUTDESC);
		this.windowObj = obj;
	}

	public void run() {
		Shell shell = new Shell(Display.getCurrent());
		
		PlugoutDescDialog plugoutDescDialog = new PlugoutDescDialog(new Shell(), WEBPersConstants.NEW_PLUGOUTDESC);
		if(plugoutDescDialog.open() == InputDialog.OK){
			String id = plugoutDescDialog.getId(); 
			
			if(widgetObj!=null){
			/*判断ID重复*/
				for (PlugoutDescElementObj obj : widgetObj.getPlugoutCells()){
					if (obj.getPlugout().getId().equals(id)){
						MessageDialog.openError(shell, M_pagemeta.NewPlugoutDescAction_0, M_pagemeta.NewPlugoutDescAction_1+ id +M_pagemeta.NewPlugoutDescAction_2);
						return;
					}
				}
				
				NewPlugoutCommand cmd = new NewPlugoutCommand(widgetObj, id);
				if(WidgetEditor.getActiveEditor() != null)
					WidgetEditor.getActiveEditor().executComand(cmd);
			}
			else if(windowObj!=null){
				for (PlugoutDescElementObj obj : windowObj.getPlugoutCells()){
					if (obj.getPlugout().getId().equals(id)){
						MessageDialog.openError(shell, M_pagemeta.NewPlugoutDescAction_0, M_pagemeta.NewPlugoutDescAction_1+ id +M_pagemeta.NewPlugoutDescAction_2);
						return;
					}
				}
				
				NewPlugoutCommand cmd = new NewPlugoutCommand(windowObj, id);
				if(RelationEditor.getActiveEditor() != null)
					RelationEditor.getActiveEditor().executComand(cmd);
				RelationEditor relationEditor = RelationEditor.getActiveRelationEditor();
				relationEditor.setDirtyTrue();
			}
			
		}
		else return;
	}
}
