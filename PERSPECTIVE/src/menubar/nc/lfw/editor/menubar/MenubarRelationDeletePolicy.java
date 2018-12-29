package nc.lfw.editor.menubar;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.menubar.command.MenubarRelationDeleteCommand;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author guoweic
 *
 */
public class MenubarRelationDeletePolicy extends ConnectionEditPolicy {

	
	protected Command getDeleteCommand(GroupRequest request) {
		Connection conn = (Connection)((EditPart)request.getEditParts().get(0)).getModel();
		return new MenubarRelationDeleteCommand(conn);
	}

}
