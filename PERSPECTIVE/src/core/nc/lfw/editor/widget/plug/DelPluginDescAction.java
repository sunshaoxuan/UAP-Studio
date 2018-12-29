package nc.lfw.editor.widget.plug;

import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * É¾³ýPluginDesc
 * @author dingrf
 *
 */
public class DelPluginDescAction extends Action {
	private class DelPluginCommand extends Command{
		
		private PluginDescElementObj pluginObj;
		public DelPluginCommand(PluginDescElementObj obj) {
			super(M_parts.DelPluginDescAction_0);
			this.pluginObj = obj;
//			this.label = label;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			WidgetElementObj weObj = pluginObj.getWidgetObj();
			if(weObj!=null){
				IPluginDesc plugin = pluginObj.getPlugin();
				weObj.getWidget().removePluginDesc(plugin.getId());
				pluginObj.getConn().disConnect();
				weObj.removePluginCell(pluginObj);
			}
			WindowObj winObj = pluginObj.getWindowObj();
			if(winObj != null){
				IPluginDesc plugin = pluginObj.getPlugin();
				winObj.getWindow().removePluginDesc(plugin.getId());
				pluginObj.getConn().disConnect();
				winObj.removePluginCell(pluginObj);
			}
//			WidgetEditor.getActiveWidgetEditor().getGraph().removePluginCell(pluginObj);
			
//			ExPoint point = label.getExPoint();
//			pluginObj.getExPoint().remove(point);
//			pluginObj.setCurrentExPoint(null);
//			pluginObj.setExtension(null);
//			pluginObj.getFingure().getContentFigure().remove(label);
//			pluginObj.getFingure().setCurrentLabel(null);
//			pluginObj.getFingure().reloadPropertySheet(pluginObj);
//			pluginObj.getFingure().reloadExtensionSheet(pluginObj);
//			pluginObj.getFingure().resizeHeight();
		}

		
		public void undo() {
//			pluginObj.getExPoint().add(label.getExPoint());
//			pluginObj.getFingure().addToContent(label);
//			pluginObj.getFingure().addExPointLabelListener(label);
//			pluginObj.getFingure().selectedLabel(label);
		}
		
	}	
	
	
	private PluginDescElementObj pluginObj;
	
	private String controllerClazz = null;
	
	private int actionType = 2;
	
	public DelPluginDescAction(PluginDescElementObj pluginObj) {
		setText(M_parts.DelPluginDescAction_0);
		setToolTipText(M_parts.DelPluginDescAction_0);
		this.pluginObj = pluginObj;
	}
	
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_parts.DelPluginDescAction_1, M_parts.DelPluginDescAction_2))
			deleteItem();
	}
	
	private void deleteItem() {
		String id = pluginObj.getPlugin().getId();
		WidgetElementObj widgetObj =  pluginObj.getWidgetObj();
		DelPluginCommand cmd = new DelPluginCommand(pluginObj);
		if(WidgetEditor.getActiveEditor() != null){
			WidgetEditor.getActiveEditor().setDirtyTrue();
			WidgetEditor.getActiveEditor().executComand(cmd);
		}
	}

}
