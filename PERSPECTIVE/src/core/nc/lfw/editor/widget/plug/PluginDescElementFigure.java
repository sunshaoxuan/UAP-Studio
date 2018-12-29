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
public class PluginDescElementFigure extends LFWBaseRectangleFigure {

	// 默认大小
	private Dimension dimen;
	// 总高度
	private int height = 0;

	private static Color bgColor = new Color(null, 239, 255, 150);
	
	public PluginDescElementFigure(PluginDescElementObj pluginObj) {
		super(pluginObj);
		setTypeLabText("<<plugin>>");
		
		setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		
		pluginObj.setFigure(this);
		
		setTitleText(pluginObj.getPlugin().getId(), pluginObj.getPlugin().getId());

		// 设置大小和位置
		Point point = pluginObj.getLocation();
		dimen = pluginObj.getSize();
//		this.height += 1 * LINE_HEIGHT;
		this.height = 40;
		setBounds(new Rectangle(point.x, point.y, dimen.width, dimen.height < this.height ? this.height : dimen.height));
		
	}
	
	@Override
	protected String getTypeText() {
		return "<<plugin>>";
	}

	
}
