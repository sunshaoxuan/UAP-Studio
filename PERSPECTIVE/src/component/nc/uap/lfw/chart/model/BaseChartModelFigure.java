package nc.uap.lfw.chart.model;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class BaseChartModelFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 101,201,116);
	public BaseChartModelFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText();
		setBackgroundColor(bgColor);
		BaseChartModelEleObj chartobj = (BaseChartModelEleObj) ele;
		setTitleText(chartobj.getId(), chartobj.getId());
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
		setBounds(new Rectangle(x, y, 120, 120));
		
	}

	
	protected String getTypeText() {
		return "<<ChartModel>>";
	}
	
	protected void setTypeLabText(){
		setTypeLabText("<<ChartModel>>");
	}
	
	
}
