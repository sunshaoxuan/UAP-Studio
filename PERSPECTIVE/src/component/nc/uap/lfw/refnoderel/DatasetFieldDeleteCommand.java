package nc.uap.lfw.refnoderel;

import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.lang.M_refnoderel;
import nc.uap.lfw.refnode.RefNodeElementObj;

import org.eclipse.gef.commands.Command;

public class DatasetFieldDeleteCommand extends Command{
	private LFWBasicElementObj lfwobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	private List<Connection> sourceConnections = null;
	private List<Connection> targetConnections = null;
	public DatasetFieldDeleteCommand(LFWBasicElementObj lfwobj,LfwBaseGraph graph) {
		super();
		this.lfwobj = lfwobj;
		this.graph = graph;
		setLabel(M_refnoderel.DatasetFieldDeleteCommand_0);
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
		 if(graph instanceof RefnoderelGraph && lfwobj instanceof DatasetFieldElementObj){
			 RefnoderelGraph refRelgraph = (RefnoderelGraph)graph;
			 RefNodeElementObj refnodeobj = refRelgraph.getDetailRefNode();
			 DatasetFieldElementObj dsfieldObj = (DatasetFieldElementObj)lfwobj;
			 refRelgraph.removeMainRefNode(dsfieldObj);
			 String refnodeRelId = refnodeobj.getRefRelationId();
			 LfwView widget = LFWPersTool.getCurrentWidget();
			 RefNodeRelation refnodeRelation = widget.getViewModels().getRefNodeRelations().getRefNodeRelation(refnodeRelId);
			 if(refnodeRelation != null){
				 String fieldId = dsfieldObj.getField().getId();
				 String dsId = dsfieldObj.getDsId();
				 refnodeRelation.removeMasterFieldInfo(dsId, fieldId);
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
		if(graph instanceof RefnoderelGraph && lfwobj instanceof RefNodeElementObj){
			RefnoderelGraph refgraph = (RefnoderelGraph)graph;
			RefNodeElementObj refdsobj = (RefNodeElementObj)lfwobj;
			if(refgraph.addCell(refdsobj)){
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
