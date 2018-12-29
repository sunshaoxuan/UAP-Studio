package nc.uap.lfw.listview;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_listview;

public class ListViewElementFigure extends LFWBaseRectangleFigure{

	private static Color bgColor = new Color(null, 57, 242, 235);
	public ListViewElementFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_listview.ListViewElementFigure_0);
		setBackgroundColor(bgColor);
		ListViewElementObj lvobj = (ListViewElementObj) ele;
		setTitleText(lvobj.getlistviewComp().getId(), lvobj.getlistviewComp().getId());
		markError(lvobj.validate());
		Point point = lvobj.getLocation();
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
		return M_listview.ListViewElementFigure_0;
	}

}
