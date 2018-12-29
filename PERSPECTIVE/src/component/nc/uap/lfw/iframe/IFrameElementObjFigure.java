package nc.uap.lfw.iframe;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * iframeͼ
 * @author zhangxya
 *
 */
public class IFrameElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 102, 104, 148);
	public IFrameElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText("<<IFrame>>");
		setBackgroundColor(bgColor);
		IFrameElementObj iframeobj = (IFrameElementObj) ele;
		setTitleText(iframeobj.getIframecomp().getId(),iframeobj.getIframecomp().getId());
		markError(iframeobj.validate());
		Point point = iframeobj.getLocation();
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
		return "<<IFrame>>";
	}
}
	