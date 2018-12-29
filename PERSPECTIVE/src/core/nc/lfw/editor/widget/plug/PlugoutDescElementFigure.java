package nc.lfw.editor.widget.plug;

import nc.lfw.editor.common.LFWBaseRectangleFigure;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * PlugoutDesc图像
 * @author dingrf
 *
 */
public class PlugoutDescElementFigure extends LFWBaseRectangleFigure {

	// 默认大小
	private Dimension dimen;
	// 总高度
	private int height = 0;

	private static Color bgColor = new Color(null, 239, 255, 150);
	
	public PlugoutDescElementFigure(PlugoutDescElementObj plugoutObj) {
		super(plugoutObj);
		setTypeLabText("<<plugout>>");
		
		setBackgroundColor(new Color(null, 239, 255, 180));
		
		plugoutObj.setFigure(this);
		
		setTitleText(plugoutObj.getPlugout().getId(), plugoutObj.getPlugout().getId());

		// 设置大小和位置
		Point point = plugoutObj.getLocation();
		dimen = plugoutObj.getSize();
//		this.height += 1 * LINE_HEIGHT;
		this.height = 40;
		setBounds(new Rectangle(point.x, point.y, dimen.width, dimen.height < this.height ? this.height : dimen.height));
		
	}
	
	@Override
	protected String getTypeText() {
		return "<<plugout>>";
	}

	
}
