package nc.lfw.editor.menubar;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.contextmenubar.ContextMenuGrahp;
import nc.lfw.editor.contextmenubar.ContextMenuGraphPart;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.lfw.editor.menubar.graph.MenubarConnectionPart;
import nc.lfw.editor.menubar.graph.MenubarGraphPart;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * @author guoweic
 *
 */
public class MenubarEditPartFactory implements EditPartFactory {
	private LFWBaseEditor editor = null;

	public MenubarEditPartFactory(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof ContextMenuGrahp){
			editPart = new ContextMenuGraphPart(editor);
		}
		else if (model instanceof MenubarGraph) {
			editPart = new MenubarGraphPart((MenubarEditor) editor);
		} else if (model instanceof MenubarElementObj) {
			editPart = new MenuElementPart(editor);
		} else if (model instanceof MenuElementObj) {
			editPart = new MenuElementPart(editor);
		} else if (model instanceof LfwBaseGraph) {
			editPart = new LfwCanvasGraphPart(editor);
		} else if (model instanceof Connection) {
			editPart = new MenubarConnectionPart();
//		} else if (model instanceof ListenerElementObj) {
//			editPart = new MenuElementPart(editor);
		} else if (model instanceof ContextMenuElementObj){
			editPart = new MenuElementPart(editor);
		} 
		editPart.setModel(model);
		return editPart;
	}
}