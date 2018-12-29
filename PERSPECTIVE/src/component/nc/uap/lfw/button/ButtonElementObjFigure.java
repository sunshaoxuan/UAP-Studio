package nc.uap.lfw.button;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.lang.M_button;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * button°´Å¥Í¼ÐÎ
 * @author zhangxya
 *
 */
public class ButtonElementObjFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 002,174,141);
	public ButtonElementObjFigure(LfwElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_button.ButtonElementObjFigure_0);
		setBackgroundColor(bgColor);
		ButtonElementObj buttonobj = (ButtonElementObj) ele;
		setTitleText(buttonobj.getButtonComp().getId(), buttonobj.getButtonComp().getId());
		markError(buttonobj.validate());
		Point point = buttonobj.getLocation();
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
		return M_button.ButtonElementObjFigure_1;
	}
}
	
