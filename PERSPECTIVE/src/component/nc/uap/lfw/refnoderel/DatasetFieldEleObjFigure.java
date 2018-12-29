package nc.uap.lfw.refnoderel;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_refnoderel;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class DatasetFieldEleObjFigure  extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 101, 150, 114);
	public DatasetFieldEleObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_refnoderel.DatasetFieldEleObjFigure_0);
		setBackgroundColor(bgColor);
		DatasetFieldElementObj dsFieldEle = (DatasetFieldElementObj) ele;
		setTitleText(dsFieldEle.getId(), dsFieldEle.getId());
		
	//	markError(refnodeobj.validate());
		Point point = dsFieldEle.getLocation();
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
		return M_refnoderel.DatasetFieldEleObjFigure_1;
	}
	
}