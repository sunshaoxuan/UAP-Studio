package nc.uap.lfw.chart.core;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;

public class ChartGraph  extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
	
	private BaseChartModelEleObj baseChartModelEle;
		
	public BaseChartModelEleObj getBaseChartModelEle() {
		return baseChartModelEle;
	}

	public void setBaseChartModelEle(BaseChartModelEleObj baseChartModelEle) {
		this.baseChartModelEle = baseChartModelEle;
	}

	public ChartGraph() {
		super();
	}
	
	public Object getPropertyValue(Object id) {
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
	}
	

}

