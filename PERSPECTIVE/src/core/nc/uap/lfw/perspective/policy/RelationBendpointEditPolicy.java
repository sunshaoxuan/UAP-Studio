package nc.uap.lfw.perspective.policy;

import nc.lfw.editor.common.Connection;
import nc.uap.lfw.perspective.commands.CreateBendPointCommand;
import nc.uap.lfw.perspective.commands.DeleteBendPointCommand;
import nc.uap.lfw.perspective.commands.MoveBendPointCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

public class RelationBendpointEditPolicy extends BendpointEditPolicy {

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendPointCommand cmd = new CreateBendPointCommand();
		cmd.setIndex(request.getIndex());
		cmd.setPoint(request.getLocation());
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		DeleteBendPointCommand cmd = new DeleteBendPointCommand();
		cmd.setIndex(request.getIndex());
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendPointCommand cmd = new MoveBendPointCommand();
		cmd.setIndex(request.getIndex());
		cmd.setPoint(request.getLocation());
		cmd.setConnection((Connection)getHost().getModel());
		return cmd;
	
	}
	

}
