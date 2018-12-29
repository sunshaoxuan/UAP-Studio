package nc.uap.lfw.perspective.policy;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.perspective.commands.LFWConnectionCommand;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * Ds Relation policy
 * @author zhangxya
 *
 */
public class LFWDSFieldRelationEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		LFWConnectionCommand comd =(LFWConnectionCommand) request.getStartCommand();
		if(getHost().getModel() instanceof DatasetElementObj){
			DatasetElementObj targetds = (DatasetElementObj)getHost().getModel();
			comd.setTarget(targetds);
		}
		else if(getHost().getModel() instanceof RefDatasetElementObj){
			RefDatasetElementObj target = (RefDatasetElementObj)getHost().getModel();
			comd.setTarget(target);
		}else if(getHost().getModel() instanceof GridElementObj){
			GridElementObj target = (GridElementObj)getHost().getModel();
			comd.setTarget(target);
		}else if(getHost().getModel() instanceof TreeElementObj){
			TreeElementObj target = (TreeElementObj)getHost().getModel();
			comd.setTarget(target);
		}else if(getHost().getModel() instanceof TreeLevelElementObj){
			TreeLevelElementObj target = (TreeLevelElementObj)getHost().getModel();
			comd.setTarget(target);
		}else if(getHost().getModel() instanceof GridLevelElementObj){
			GridLevelElementObj target = (GridLevelElementObj)getHost().getModel();
			comd.setTarget(target);
		}
		else if(getHost().getModel() instanceof RefNodeElementObj){
			RefNodeElementObj target = (RefNodeElementObj) getHost().getModel();
			comd.setTarget(target);
		}
		
		
		return comd;
	}

	
	@SuppressWarnings("unchecked")
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if(getHost().getModel() instanceof LFWBasicElementObj){
			LFWBasicElementObj sour = (LFWBasicElementObj)getHost().getModel();
			Class conCls =(Class) request.getNewObject();
			Command command = new LFWConnectionCommand(sour,conCls);
			request.setStartCommand(command);
			return command;
		}
		return null;
	}


	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}

