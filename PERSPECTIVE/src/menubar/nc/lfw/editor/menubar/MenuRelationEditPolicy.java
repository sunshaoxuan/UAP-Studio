package nc.lfw.editor.menubar;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.menubar.command.MenuRelationConnectionCommand;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class MenuRelationEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		MenuRelationConnectionCommand comd = (MenuRelationConnectionCommand) request.getStartCommand();

		LFWBasicElementObj target = (LFWBasicElementObj) getHost().getModel();
		comd.setTarget(target);
		return comd;
	}

	
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if (getHost().getModel() instanceof LFWBasicElementObj) {
			LFWBasicElementObj sour = (LFWBasicElementObj) getHost().getModel();
			Class conCls = (Class) request.getNewObject();
			Command command = new MenuRelationConnectionCommand(sour, conCls);
			request.setStartCommand(command);
			return command;
		}
		return null;
	}

	
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

}
