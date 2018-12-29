package nc.uap.lfw.chart.actions;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.uap.lfw.chart.core.ChartCompEleObj;
import nc.uap.lfw.chart.core.ChartGraph;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.lang.M_chart;

import org.eclipse.gef.commands.Command;

public class ChartModelDelCommand extends Command{
	private LFWBasicElementObj lfwobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	public ChartModelDelCommand(LFWBasicElementObj lfwobj,LfwBaseGraph graph) {
		super();
		this.lfwobj = lfwobj;
		this.graph = graph;
		setLabel(M_chart.ChartModelDelCommand_0);
	}

	
	public boolean canExecute() {
		return super.canExecute();
	}

	
	public void execute() {
		redo();
	}

	
	public void redo() {
		 if(graph instanceof ChartGraph && lfwobj instanceof BaseChartModelEleObj){
			ChartGraph chartgraph = (ChartGraph)graph;
			BaseChartModelEleObj chartModel = (BaseChartModelEleObj)lfwobj;
			chartgraph.removeCell(chartModel);
			ChartCompEleObj chartobj = (ChartCompEleObj)chartgraph.getCells().get(0);
			ChartComp chartcomp = chartobj.getChartComp();
			chartcomp.setChartModel(null);
		}
	}
	

	public void undo() {
		
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	
	public boolean canUndo() {
		return isCanUndo();
	}

}
