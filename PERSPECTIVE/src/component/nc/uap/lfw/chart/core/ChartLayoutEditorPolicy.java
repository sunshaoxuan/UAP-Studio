package nc.uap.lfw.chart.core;

import nc.uap.lfw.chart.actions.ChartConfigCreateCommand;
import nc.uap.lfw.chart.actions.ChartModelCreateCommand;
import nc.uap.lfw.chart.model.Bar2DChartModelEleObj;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class ChartLayoutEditorPolicy extends XYLayoutEditPolicy {

	
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart editPart, Object constraint) {
		return super.createChangeConstraintCommand(request, editPart,
				constraint);
	}

	
	protected Command createChangeConstraintCommand(EditPart arg0, Object arg1) {
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) {
		Object obj = request.getNewObject();
		if(BaseChartModelEleObj.class.isInstance(obj)){//ÐÂ½¨dataset
			return new ChartModelCreateCommand((BaseChartModelEleObj)obj, (ChartGraph)getHost().getModel(), 
					(Rectangle)getConstraintFor(request));
		}
		if(ChartConfigEleObj.class.isInstance(obj)){
			return new ChartConfigCreateCommand((ChartConfigEleObj)obj, (ChartGraph)getHost().getModel(), 
					(Rectangle)getConstraintFor(request));
		}
		return null;
	}

}
