package nc.lfw.editor.pagemeta.plug;

import java.util.List;

import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.PluginProxy;
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

public class NewPluginProxyAction extends Action{
		
	private class NewPluginCommand extends Command{
		private WindowObj windowObj;
		
		private String id;

		public NewPluginCommand(WindowObj obj, String id) {
			super(WEBPersConstants.NEW_PLUGINDESC);
			this.windowObj = obj;
			this.id = id;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			PluginDescElementObj pluginObj = new PluginDescElementObj();
			pluginObj.setPlugin(new PluginProxy());
			pluginObj.getPlugin().setId(this.id);
//			pluginObj.getPlugin().setSubmitRule(new EventSubmitRule());
			if(windowObj!=null){
//				List<IPluginDesc> pluginDescs = windowObj.getWindow().getPluginDescList();
//				if (pluginDescs == null){
//					pluginDescs = new ArrayList<IPluginDesc>(1);
//					windowObj.getWindow().setPluginDescs(pluginDescs);
//				}
				windowObj.getWindow().addPluginDesc(pluginObj.getPlugin());
				Point point = new Point();
				int count =getCount(windowObj.getPluginCells());
				point.x = windowObj.getLocation().x -100 - 50;
				point.y = windowObj.getLocation().y + count * 40 + 40;
				pluginObj.setLocation(point);
				ChildConnection conn = new ChildConnection(windowObj,pluginObj);
				conn.connect();
				pluginObj.setConn(conn);
				pluginObj.setSize(new Dimension(100, 30));
				pluginObj.setWindowObj(windowObj);
				windowObj.addPluginCell(pluginObj);
			}

		}

		public void undo() {
		}
		
	}	
	
	private int getCount(List<PluginDescElementObj> ins){
		int count = 0;
		if(ins.size()==0){
			return 0;
		}
		for(PluginDescElementObj pluginDesc:ins){
			if(pluginDesc.getPlugin() instanceof PluginProxy){
				count++;
			}
		}
		return count;
	}
	
	private WindowObj windowObj;
	
	public NewPluginProxyAction(WindowObj obj){
		super(WEBPersConstants.NEW_PLUGINPROXYDESC);
		this.windowObj = obj;
	}
	
	public void run(){
		Shell shell = new Shell(Display.getCurrent());
		PluginProxyDialog dialog = new PluginProxyDialog(shell, WEBPersConstants.NEW_PLUGINPROXYDESC);
		if(dialog.open() == IDialogConstants.OK_ID){
			String id = dialog.getId();
			for (PluginDescElementObj obj : windowObj.getPluginCells()){
				if (obj.getPlugin().getId().equals(id)){
					MessageDialog.openError(shell, M_pagemeta.NewPluginProxyAction_0, M_pagemeta.NewPluginProxyAction_1+ id +M_pagemeta.NewPluginProxyAction_2);
					return;
				}
			}
			NewPluginCommand cmd = new NewPluginCommand(windowObj, id);
			if(RelationEditor.getActiveEditor() != null)
				RelationEditor.getActiveEditor().executComand(cmd);
			
			RelationEditor relationEditor = RelationEditor.getActiveRelationEditor();
			relationEditor.setDirtyTrue();
		}
	}

}
