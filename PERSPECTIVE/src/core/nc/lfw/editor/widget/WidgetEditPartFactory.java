package nc.lfw.editor.widget;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.pagemeta.PagemetaGraphPart;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.uap.lfw.editor.window.WindowConfigObj;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.parts.LFWConnectinPart;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * @author guoweic
 *
 */
public class WidgetEditPartFactory implements EditPartFactory {

	private WidgetEditor editor = null;

	public WidgetEditPartFactory(WidgetEditor editor) {
		super();
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
//		if (model instanceof WidgetElementObj || model instanceof ListenerElementObj) {
		if (model instanceof WidgetElementObj){
			editPart = new WidgetElementPart(editor);
		} else if (model instanceof WidgetGraph) {
			editPart = new WidgetGraphPart(editor);
		} else if (model instanceof PlugoutDescElementObj) {
			editPart = new PlugoutDescElementPart();
		} else if (model instanceof PluginDescElementObj) {
			editPart = new PluginDescElementPart();
		} else if (model instanceof ChildConnection|| model instanceof Connection) {
			editPart = new LFWConnectinPart();
		} else if (model instanceof WindowObj){
			editPart = new WidgetElementPart(editor);
		}else if (model instanceof WindowConfigObj){
			editPart = new WidgetElementPart(editor);
		}else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
