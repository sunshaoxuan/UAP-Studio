package nc.uap.lfw.chart.model;

import nc.lfw.editor.common.LfwElementObjWithGraph;

public class Bar2DChartModelFigure extends BaseChartModelFigure{

	public Bar2DChartModelFigure(LfwElementObjWithGraph ele) {
		super(ele);
		// TODO Auto-generated constructor stub
	}

	protected String getTypeText() {
		return "<<2DChartModel>>";
	}
	
	protected void setTypeLabText(){
		setTypeLabText("<<2DChartModel>>");
	}
}
