package nc.lfw.editor.pagemeta;


import nc.lfw.editor.common.Connection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author guoweic
 *
 */
public class WidgetRelationDeletePolicy extends ConnectionEditPolicy {

	
	protected Command getDeleteCommand(GroupRequest request) {
		Connection conn = (Connection)((EditPart)request.getEditParts().get(0)).getModel();
		return new WidgetRelationDeleteCommand(conn);
	}

}
