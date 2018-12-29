package nc.lfw.editor.widget.plug;

import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * É¾³ýPlugoutDesc
 * @author dingrf
 *
 */
public class DelPlugoutDescAction extends Action {
	private class DelPlugoutCommand extends Command{
		
		private PlugoutDescElementObj plugoutObj;
		public DelPlugoutCommand(PlugoutDescElementObj obj) {
			super(M_parts.DelPlugoutDescAction_0);
			this.plugoutObj = obj;
//			this.label = label;
		}

		public void execute() {
			redo();
		}

		
		public void redo() {
			WidgetElementObj weObj = plugoutObj.getWidgetObj();
			if(weObj != null){
				IPlugoutDesc plugout = plugoutObj.getPlugout();
				weObj.getWidget().removePlugoutDesc(plugout.getId());
				plugoutObj.getConn().disConnect();
				weObj.removePlugoutCell(plugoutObj);
			}
			WindowObj winObj = plugoutObj.getWindowObj();
			if(winObj != null){
				IPlugoutDesc plugin = plugoutObj.getPlugout();
				winObj.getWindow().removePlugoutDesc(plugin.getId());
				plugoutObj.getConn().disConnect();
				winObj.removePlugoutCell(plugoutObj);
			}
//			WidgetEditor.getActiveWidgetEditor().getGraph().removePlugoutCell(plugoutObj);
			
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
	
	
	private PlugoutDescElementObj plugoutObj;
	
	public DelPlugoutDescAction(PlugoutDescElementObj plugoutObj) {
		setText(M_parts.DelPlugoutDescAction_0);
		setToolTipText(M_parts.DelPlugoutDescAction_0);
		this.plugoutObj = plugoutObj;
	}
	
	
	public void run() {
		if (MessageDialog.openConfirm(null, M_parts.DelPlugoutDescAction_1, M_parts.DelPlugoutDescAction_2))
			deleteItem();
	}
	
	private void deleteItem() {
		DelPlugoutCommand cmd = new DelPlugoutCommand(plugoutObj);
		if(WidgetEditor.getActiveEditor() != null)
			WidgetEditor.getActiveEditor().setDirtyTrue();
			WidgetEditor.getActiveEditor().executComand(cmd);
	}

}
