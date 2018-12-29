package nc.uap.lfw.perspective.policy;

import nc.lfw.editor.common.Connection;
import nc.uap.lfw.perspective.commands.RelationdeleteCommand;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 *各种组件的删除操作
 * @author zhangxya
 *
 */
public class LFWElementReEditPolicy  extends ConnectionEditPolicy {

	
	protected Command getDeleteCommand(GroupRequest request) {
		Connection conn = (Connection)((EditPart)request.getEditParts().get(0)).getModel();
		return new RelationdeleteCommand(conn);
	}

}