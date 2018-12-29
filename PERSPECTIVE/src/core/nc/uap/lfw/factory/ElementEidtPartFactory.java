package nc.uap.lfw.factory;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseEditor;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwCanvasGraphPart;
import nc.lfw.editor.contextmenubar.ContextMenuGrahp;
import nc.lfw.editor.contextmenubar.ContextMenuGraphPart;
import nc.lfw.editor.datasets.core.DatasetsGraph;
import nc.lfw.editor.datasets.core.DatasetsGraphPart;
import nc.lfw.editor.datasets.core.DsRelationConnection;
import nc.lfw.editor.menubar.MenubarGraph;
import nc.lfw.editor.menubar.ele.MenuElementObj;
import nc.lfw.editor.menubar.ele.MenubarElementObj;
import nc.lfw.editor.menubar.graph.MenuElementPart;
import nc.lfw.editor.menubar.graph.MenubarGraphPart;
//import nc.uap.lfw.excel.ExcelElementObj;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.pagination.PaginationElementObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.parts.DatasetGraphPart;
import nc.uap.lfw.parts.LFWConnectinPart;
import nc.uap.lfw.parts.LFWElementPart;
//import nc.uap.lfw.perspective.listener.ListenerElementObj;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.RefNodeRelGraphPart;
import nc.uap.lfw.refnoderel.RefnoderelGraph;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * ELement ±à¼­²¿·ÖFactory
 * @author zhangxya
 *
 */
public class ElementEidtPartFactory  implements EditPartFactory {
	private LFWBaseEditor editor = null;
	public ElementEidtPartFactory(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
//		if((model instanceof DatasetElementObj) || model instanceof RefDatasetElementObj || model instanceof ListenerElementObj
		if((model instanceof DatasetElementObj) || model instanceof RefDatasetElementObj 
				|| model instanceof TreeElementObj || model instanceof RefNodeElementObj || model instanceof GridElementObj ||  model instanceof ListViewElementObj ||model instanceof PaginationElementObj || model instanceof TreeLevelElementObj){
			editPart= new LFWElementPart(editor);
		}
		else if(model instanceof DatasetGraph){
			editPart = new DatasetGraphPart(editor);
		}else if(model instanceof DatasetsGraph){
			editPart = new DatasetsGraphPart(editor);
		}
		else if(model instanceof ContextMenuGrahp){
			editPart = new ContextMenuGraphPart(editor);
		}
		else if(model instanceof MenubarGraph){
			editPart = new MenubarGraphPart(editor);
		}
		else if(model instanceof RefnoderelGraph){
			editPart = new RefNodeRelGraphPart(editor);
		}
		else if(model instanceof MenubarElementObj){
			editPart = new MenuElementPart(editor);
		}
		else if(model instanceof MenuElementObj)
			editPart = new MenuElementPart(editor);
		else if(model instanceof LfwBaseGraph)
			editPart = new LfwCanvasGraphPart(editor);
		else if(model instanceof Connection || model instanceof ChildConnection || model instanceof DsRelationConnection || model instanceof TreeLevelChildConnection){
			 editPart = new LFWConnectinPart();
		}
		else 
			editPart= new LFWElementPart(editor);
		editPart.setModel(model);
		return editPart;
	}
}