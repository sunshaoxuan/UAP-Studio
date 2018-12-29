package nc.uap.lfw.perspective.policy;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.grid.GridElementObj;
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
 * widget plug Relation policy
 * @author dingrf
 *
 */
public class LFWPlugRelationEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		LFWConnectionCommand comd =(LFWConnectionCommand) request.getStartCommand();
		if(getHost().getModel() instanceof PluginDescElementObj){
			PluginDescElementObj targetIn = (PluginDescElementObj)getHost().getModel();
			comd.setTarget(targetIn);
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

