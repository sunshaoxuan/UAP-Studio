package nc.uap.lfw.perspective.model;

import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.figure.ui.IDirectEditable;
import nc.uap.lfw.figure.ui.NullBorder;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * 向ds或refds图形中加入Field字段
 * @author zhangxya
 *
 */
public class DsFieldLabel extends Label implements IDirectEditable{
//	private String msg = null;
	private String prop = null;
	public DsFieldLabel(String prop, String isMatch) {
		super(prop);
		this.prop = prop;
		setLabelAlignment(PositionConstants.LEFT);
		Image currentImage = null;
		currentImage = null;
//		ImageDescriptor imageDescriptor = WEBProjPlugin.loadImage(WEBProjPlugin.ICONS_PATH, "about.png");
//		imageDescriptor.createImage()
		if(isMatch == null || isMatch.equals("N")){
			currentImage = ImageProvider.file;
			setLabelAlignment(PositionConstants.LEFT);
		}
		if(isMatch!=null){
			if(isMatch.equals("key")){
				currentImage = ImageProvider.key;
			}
			if(isMatch.equals("field")){
				currentImage = ImageProvider.property;
			}
			setLabelAlignment(PositionConstants.LEFT);
		} 
		setIcon(currentImage);
		setBorder(new NullBorder());
		markError();
	}
	public void updateFigure(String prop){
		this.prop = prop;
		setText(prop);
		markError();
	}
	public String getProp(){
		return prop;
	}
	private void markError(){

	}
//	public String getErrStr(){
//		return msg;
//	}
	public Object getEditableObj() {
		// TODO Auto-generated method stub
		return getProp();
	}
}
