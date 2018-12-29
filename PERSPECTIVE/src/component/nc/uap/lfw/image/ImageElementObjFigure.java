package nc.uap.lfw.image;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_iframe;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * 图形控件的图形
 * @author zhangxya
 *
 */
public class ImageElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 207, 132, 86);
	public ImageElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_iframe.ImageElementObjFigure_0);
		setBackgroundColor(bgColor);
		ImageElementObj imageobj = (ImageElementObj) ele;
		setTitleText(imageobj.getImageComp().getId(), imageobj.getImageComp().getId());
		markError(imageobj.validate());
		Point point = imageobj.getLocation();
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
		return M_iframe.ImageElementObjFigure_0;
	}
}
	
