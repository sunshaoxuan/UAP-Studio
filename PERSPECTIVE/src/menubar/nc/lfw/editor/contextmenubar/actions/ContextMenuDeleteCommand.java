package nc.lfw.editor.contextmenubar.actions;

import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.ToolBarComp;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.toolbar.ToolBarElementObj;
import nc.uap.lfw.toolbar.ToolBarGraph;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.gef.commands.Command;

/**
 * ÓÒ¼ü²Ëµ¥É¾³ýÃüÁî
 * @author zhangxya
 *
 */
public class ContextMenuDeleteCommand extends Command{
	private LFWBasicElementObj lfwobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	private List<Connection> sourceConnections = null;
	private List<Connection> targetConnections = null;
	public ContextMenuDeleteCommand(LFWBasicElementObj lfwobj,LfwBaseGraph graph) {
		super();
		this.lfwobj = lfwobj;
		this.graph = graph;
		setLabel("delete refds");
	}

	
	public boolean canExecute() {
		return super.canExecute();
	}

	
	public void execute() {
		sourceConnections = lfwobj.getSourceConnections();
		targetConnections = lfwobj.getTargetConnections();
		redo();
	}

	
	public void redo() {
		 if(graph instanceof GridGraph && lfwobj instanceof ContextMenuElementObj){
			GridGraph gridgraph = (GridGraph)graph;
			ContextMenuElementObj refdsobj = (ContextMenuElementObj)lfwobj;
			gridgraph.removeCell(refdsobj);
			GridElementObj gridobj = (GridElementObj)gridgraph.getCells().get(0);
			GridComp gridcomp = gridobj.getGridComp();
			gridcomp.setContextMenu(null);
		}else if(graph instanceof FormGraph && lfwobj instanceof ContextMenuElementObj){
			FormGraph formgraph = (FormGraph)graph;
			ContextMenuElementObj refdsobj = (ContextMenuElementObj)lfwobj;
			formgraph.removeCell(refdsobj);
			FormElementObj formobj = (FormElementObj)formgraph.getCells().get(0);
			FormComp formcomp = formobj.getFormComp();
			formcomp.setContextMenu(null);
		}
		else if(graph instanceof ToolBarGraph && lfwobj instanceof ContextMenuElementObj){
			ToolBarGraph toolbarGraph = (ToolBarGraph)graph;
			ContextMenuElementObj refdsobj = (ContextMenuElementObj)lfwobj;
			toolbarGraph.removeCell(refdsobj);
			ToolBarElementObj toolbarCompEle = (ToolBarElementObj)toolbarGraph.getCells().get(0);
			ToolBarComp toolbarComp = toolbarCompEle.getToolbarComp();
			toolbarComp.setContextMenu(null);
		}
		else if(graph instanceof TreeGraph && lfwobj instanceof ContextMenuElementObj){
			TreeGraph treeGraph = (TreeGraph)graph;
			ContextMenuElementObj refdsobj = (ContextMenuElementObj)lfwobj;
			String contextMenuId = refdsobj.getMenubar().getId();
			treeGraph.removeCell(refdsobj);
			TreeElementObj treeEle = (TreeElementObj)treeGraph.getCells().get(0);
			TreeLevel topLevel = treeEle.getTreeComp().getTopLevel();
			if(topLevel!=null){
				if(topLevel.getContextMenu() != null && topLevel.getContextMenu().equals(contextMenuId)){
					topLevel.setContextMenu(null);
				}
				else{
					TreeLevel childLevel = topLevel.getChildTreeLevel();
					while(childLevel != null){
						if(childLevel.getContextMenu() != null && childLevel.getContextMenu().equals(contextMenuId)){
							childLevel.setContextMenu(null);
							break;
						}
						else
							childLevel = childLevel.getChildTreeLevel();
					}
				}
			}
		}
		else if(graph instanceof TreeGraph && lfwobj instanceof TreeLevelElementObj){
			TreeGraph treeGraph = (TreeGraph)graph;
			TreeLevelElementObj treelevelobj = (TreeLevelElementObj)lfwobj;
			treeGraph.removeCell(treelevelobj);
			LFWBasicElementObj parent = treelevelobj.getParentTreeLevel();
			while(parent != null && !(parent instanceof TreeElementObj)){
				TreeLevelElementObj treelevelparent = (TreeLevelElementObj)parent;
				parent = treelevelparent.getParentTreeLevel();
			}
			TreeElementObj treeEle = (TreeElementObj)parent;
			TreeViewComp treeViewcomp = treeEle.getTreeComp();
			TreeLevel topLevel = treeViewcomp.getTopLevel();
			if(topLevel.getId().equals(treelevelobj.getId()))
				treeViewcomp.setTopLevel(null);
			else{
				TreeLevel childLevel = topLevel.getChildTreeLevel();
				TreeLevel parentLevel = topLevel;
				while(!childLevel.getId().equals(treelevelobj.getId())){
					childLevel = parentLevel.getChildTreeLevel();
					parentLevel = childLevel;
				}
				parentLevel.setChildTreeLevel(null);
				treeViewcomp.setTopLevel(topLevel);
			}
			
		}
		removeConnections(sourceConnections);
		removeConnections(targetConnections);
	}
	
	private void removeConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.disConnect();
		}
	}
	
	private void addConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.connect();
		}
	}

	
	public void undo() {
		if(graph instanceof GridGraph && lfwobj instanceof ContextMenuElementObj){
			GridGraph gridgraph = (GridGraph)graph;
			ContextMenuElementObj refdsobj = (ContextMenuElementObj)lfwobj;
			if(gridgraph.addCell(refdsobj)){
				addConnections(sourceConnections);
				addConnections(targetConnections);
			}
		}
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	
	public boolean canUndo() {
		return isCanUndo();
	}

}
