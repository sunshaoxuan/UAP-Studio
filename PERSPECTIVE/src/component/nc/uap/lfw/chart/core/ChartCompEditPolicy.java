package nc.uap.lfw.chart.core;

import nc.uap.lfw.chart.actions.ChartConfigDelCommand;
import nc.uap.lfw.chart.actions.ChartModelDelCommand;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class ChartCompEditPolicy  extends ComponentEditPolicy {

	
	protected Command createDeleteCommand(GroupRequest request) {
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if ((parent instanceof ChartGraph && child instanceof BaseChartModelEleObj)) {
			return new ChartModelDelCommand((BaseChartModelEleObj) child,(ChartGraph) parent);
		}
		else if ((parent instanceof ChartGraph && child instanceof ChartConfigEleObj)) {
			return new ChartConfigDelCommand((ChartConfigEleObj) child,(ChartGraph) parent);
		}
		else 
			return super.createDeleteCommand(request);
	}

}
