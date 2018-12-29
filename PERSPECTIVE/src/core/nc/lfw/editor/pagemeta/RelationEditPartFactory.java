package nc.lfw.editor.pagemeta;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementPart;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementPart;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.parts.LFWConnectinPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class RelationEditPartFactory implements EditPartFactory{

	private RelationEditor editor = null;
	
	
	public RelationEditor getEditor() {
		return editor;
	}


	public void setEditor(RelationEditor editor) {
		this.editor = editor;
	}
	
	public RelationEditPartFactory(RelationEditor editor) {
		super();
		this.editor = editor;
	}


	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
//		if (model instanceof WidgetElementObj || model instanceof ListenerElementObj) {
		if (model instanceof WidgetElementObj){
			editPart = new RelationElementPart();
		} 
		else if (model instanceof WindowObj){
			editPart = new RelationElementPart();
		}
		else if (model instanceof PagemetaGraph) {
			editPart = new RelationGraphPart(editor);
		}
		else if (model instanceof PlugoutDescElementObj) {
			editPart = new PlugoutDescElementPart();
		} 
		else if (model instanceof PluginDescElementObj) {
			editPart = new PluginDescElementPart();
		}
		else if (model instanceof ChildConnection || model instanceof Connection) {
			editPart = new LFWConnectinPart();
		}
		else
			throw new RuntimeException("illegal param");
		editPart.setModel(model);
		return editPart;
	}

}
