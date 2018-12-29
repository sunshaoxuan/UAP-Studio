package nc.uap.lfw.perspective.policy;

import nc.uap.lfw.parts.LFWElementPart;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 基本组件的Edit Policy
 * @author zhangxya
 *
 */
public class LFWComponentEditPolicy extends  XYLayoutEditPolicy {

	
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart editPart, Object constraint) {
		if(editPart instanceof LFWElementPart && constraint instanceof Rectangle){
			//return new CellConstraintUpdateCommand((Cell)editPart.getModel(),(Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, editPart, constraint);
	}

	
	protected Command createChangeConstraintCommand(EditPart arg0, Object arg1) {
		return null;
	}

	
	protected Command getCreateCommand(CreateRequest request) {
//		Object obj = request.getNewObject();
//		if(RefDatasetElementObj.class.isInstance(obj)){
//			return new RefDatasetCreateCommand((RefDatasetElementObj)obj, (DatasetElementObj)getHost().getModel(),(Rectangle)getConstraintFor(request));
//			
//		}
		return null;
	}

}