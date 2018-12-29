package nc.lfw.editor.pagemeta.plug;

import java.util.List;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.palette.ChildConnection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class NewPlugoutProxyAction extends Action{
	
	private class NewPlugoutCommand extends Command{
		private WidgetElementObj widgetObj;
		
		private WindowObj windowObj;
		
		private String id;
		
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
			plugoutObj.setPlugout(new PlugoutProxy());
			plugoutObj.getPlugout().setId(this.id);
			if(windowObj!=null){
//				List<IPlugoutDesc> plugoutDescs = windowObj.getWindow().getPlugoutDescList();
//			
//				plugoutDescs.add(plugoutObj.getPlugout());
				
				windowObj.getWindow().addPlugoutDesc(plugoutObj.getPlugout());
				
				Point point = new Point();
				int count =getCount(windowObj.getPlugoutCells());
				point.x = windowObj.getLocation().x - windowObj.getSize().width - 50;
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
			if(plugoutDesc.getPlugout() instanceof PlugoutProxy){
				count++;
			}
		}
		return count;
	}
	
	private WindowObj windowObj;
	
	public NewPlugoutProxyAction(WindowObj obj){
		super(WEBPersConstants.NEW_PLUGOUTPROXYDESC);
		this.windowObj = obj;
	}
	
	public void run(){
		Shell shell = new Shell(Display.getCurrent());
		PlugoutProxyDialog dialog = new PlugoutProxyDialog(shell, WEBPersConstants.NEW_PLUGOUTPROXYDESC);
		if(dialog.open() == IDialogConstants.OK_ID){
			String id = dialog.getId();
			for (PlugoutDescElementObj obj : windowObj.getPlugoutCells()){
				if (obj.getPlugout().getId().equals(id)){
					MessageDialog.openError(shell, M_pagemeta.NewPlugoutProxyAction_0, M_pagemeta.NewPlugoutProxyAction_1+ id +M_pagemeta.NewPlugoutProxyAction_2);
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

}
