package nc.uap.lfw.chart.core;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_chart;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class ChartCompEleObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 101,201,116);
	public ChartCompEleObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_chart.ChartCompEleObjFigure_0);
		setBackgroundColor(bgColor);
		ChartCompEleObj chartobj = (ChartCompEleObj) ele;
		setTitleText(chartobj.getChartComp().getId(), chartobj.getChartComp().getId());
		markError(chartobj.validate());
		Point point = chartobj.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		setBounds(new Rectangle(x, y, 180, 180));
		
	}

	protected String getTypeText() {
		return M_chart.ChartCompEleObjFigure_1;
	}
	
}
