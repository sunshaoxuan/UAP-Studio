package nc.uap.lfw.chart.actions;

import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.chart.core.ChartCompEleObj;
import nc.uap.lfw.chart.core.ChartConfigEleObj;
import nc.uap.lfw.chart.core.ChartGraph;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.ChartConfig;
import nc.uap.lfw.lang.M_chart;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ChartConfigCreateCommand extends Command{
	
	private ChartConfigEleObj chartconfigobj = null;
	private ChartGraph graph = null;
	private Rectangle rect = null;
	private boolean canUndo = true;
	
	public ChartConfigCreateCommand(ChartConfigEleObj chartconfigobj, ChartGraph graph, Rectangle rect){
		super();
		this.chartconfigobj = chartconfigobj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create a new chartconfig");
	}
	public boolean canExecute() {
		return chartconfigobj != null && graph != null && rect != null;
	}
	public void execute() {
		List<LfwElementObjWithGraph> cells = graph.getCells();
		if(cells.size() >= 3 ||(cells.size()>0 && (cells.get(0) instanceof ChartConfigEleObj))){
			MessageDialog.openConfirm(null, M_chart.ChartModelCreateCommand_1, M_chart.ChartModelCreateCommand_2);
			return;
		}
		ChartConfig config = new ChartConfig();
		config.setCaption("ÐÂ½¨Í¼±í");
		config.setId("new Chart");
		chartconfigobj.setChartconfig(config);
		ChartCompEleObj chartobj = (ChartCompEleObj) this.graph.getCells().get(0);
		ChartComp chartComp = chartobj.getChartComp();
		chartComp.setConfig(config);
		chartconfigobj.setLocation(new Point(350, 250));
		redo();
		
	}
	public void redo() {
		graph.addCell(chartconfigobj);
	}

	
	public void undo() {
		graph.removeCell(chartconfigobj);
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
