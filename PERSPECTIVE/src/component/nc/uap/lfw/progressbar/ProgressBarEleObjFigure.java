package nc.uap.lfw.progressbar;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_listview;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class ProgressBarEleObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 236,174,141);
	public ProgressBarEleObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_listview.ProgressBarEleObjFigure_0);
		setBackgroundColor(bgColor);
		ProgressBarEleObj progressbarObj = (ProgressBarEleObj) ele;
		setTitleText(progressbarObj.getProgressBar().getId(), progressbarObj.getProgressBar().getId());
		markError(progressbarObj.validate());
		Point point = progressbarObj.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		setBounds(new Rectangle(x, y, 150, 150));
		
	}

	
	protected String getTypeText() {
		return M_listview.ProgressBarEleObjFigure_0;
	}
}
	
