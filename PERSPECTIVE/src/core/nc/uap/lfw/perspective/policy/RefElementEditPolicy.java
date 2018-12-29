package nc.uap.lfw.perspective.policy;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.contextmenubar.actions.ContextMenuDeleteCommand;
//import nc.uap.lfw.excel.ExcelGraph;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.listview.ListViewGraph;
import nc.uap.lfw.pagination.PaginationGraph;
import nc.uap.lfw.perspective.commands.RefDatasetDeleteCommand;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.DatasetFieldDeleteCommand;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.refnoderel.RefNodeDeleteCommand;
import nc.uap.lfw.refnoderel.RefnoderelGraph;
import nc.uap.lfw.toolbar.ToolBarGraph;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * ref ds ±‡º≠√¸¡Ó
 * @author zhangxya
 *
 */
public class RefElementEditPolicy  extends ComponentEditPolicy {

	
	protected Command createDeleteCommand(GroupRequest request) {
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if ((parent instanceof DatasetGraph && child instanceof RefDatasetElementObj)) {
			return new RefDatasetDeleteCommand((RefDatasetElementObj) child,(DatasetGraph) parent);
		}else if((parent instanceof GridGraph && child instanceof RefDatasetElementObj)){
			return new RefDatasetDeleteCommand((RefDatasetElementObj) child,(GridGraph) parent);
//		}else if((parent instanceof ExcelGraph && child instanceof RefDatasetElementObj)){
//			return new RefDatasetDeleteCommand((RefDatasetElementObj) child,(ExcelGraph) parent);
		}else if((parent instanceof ListViewGraph && child instanceof RefDatasetElementObj)){
			return new RefDatasetDeleteCommand((RefDatasetElementObj) child,(ListViewGraph) parent);
		}
		else if((parent instanceof PaginationGraph && child instanceof RefDatasetElementObj)){
			return new RefDatasetDeleteCommand((RefDatasetElementObj) child,(PaginationGraph) parent);
		}
		else if (parent instanceof TreeGraph && child instanceof TreeLevelElementObj){
			return new RefDatasetDeleteCommand((TreeLevelElementObj)child, (TreeGraph)parent);
		}else if(parent instanceof FormGraph && child instanceof RefDatasetElementObj)
			return new RefDatasetDeleteCommand((RefDatasetElementObj)child, (FormGraph)parent);
		else if((parent instanceof GridGraph || parent instanceof TreeGraph ||  parent instanceof FormGraph || parent instanceof ToolBarGraph) && child instanceof ContextMenuElementObj){
			return new ContextMenuDeleteCommand((ContextMenuElementObj)child, (LfwBaseGraph)parent);
		}
		else if(parent instanceof RefnoderelGraph && child instanceof RefNodeElementObj){
			return new RefNodeDeleteCommand((RefNodeElementObj)child, (RefnoderelGraph)parent);
		}
		else if(parent instanceof RefnoderelGraph && child instanceof DatasetFieldElementObj){
			return new DatasetFieldDeleteCommand((DatasetFieldElementObj)child, (RefnoderelGraph)parent);
		}
		return super.createDeleteCommand(request);
	}

}
