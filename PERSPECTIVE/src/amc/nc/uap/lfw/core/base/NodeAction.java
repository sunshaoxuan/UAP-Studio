/**
 * 
 */
package nc.uap.lfw.core.base;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 
 * 节点行为抽象基类
 * @author chouhl
 *
 */
public abstract class NodeAction extends Action {

	public NodeAction(){
		super();
	}
	
	public NodeAction(String text) {
		super(text);
		
	}
	
	public NodeAction(String text, String toolTipText) {
		super(text);
		setToolTipText(toolTipText);
	}
	
	public NodeAction(String text, ImageDescriptor image){
		super(text, image);
	}
	
	public NodeAction(String text, int style){
		super(text, style);
	}
	
	public void run(){
		super.run();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		ImageDescriptor image = null;
		if(getText() != null){
			if(getText().contains(WEBPersConstants.NEW) || getText().contains(WEBPersConstants.ADD)||getText().contains(WEBPersConstants.CREATE)){
				image = PaletteImage.getCreateTreeImgDescriptor();
			}else if(getText().contains(WEBPersConstants.REFRESH)||getText().contains(WEBPersConstants.REFRESH_DATASET)){
				image = PaletteImage.getRefreshImgDescriptor();
			}else if(getText().contains(WEBPersConstants.MODIFY) || getText().contains(WEBPersConstants.EDITOR)){
				image = PaletteImage.getEditorDescriptor();
			}else if(getText().contains(WEBPersConstants.DELETE)){
				image = PaletteImage.getDeleteImgDescriptor();
			}else if(getText().contains(WEBPersConstants.UI_GUIDE)||getText().contains(WEBPersConstants.MODEL_GUIDE)){
				image = PaletteImage.getPortletDescriptor();
			}else if(getText().contains(WEBPersConstants.REFERENCE)||getText().contains(WEBPersConstants.IMPORT)){
				image = PaletteImage.getRefnodeDescriptor();
			}else if(getText().contains(WEBPersConstants.PREVIEW_APPLICATION)){
				image = PaletteImage.getRefnodeDescriptor();			
			}else if(getText().contains(WEBPersConstants.REGISTRY)){
				image = PaletteImage.getCreateTreeImgDescriptor();
			}else if(getText().contains(WEBPersConstants.MANAGER)){
				image = PaletteImage.getCreateTreeImgDescriptor();
			}else if(getText().contains(WEBPersConstants.OPEN)){
				image = PaletteImage.getOpenDescriptor();
			}
		}
		return image;
	}
	
}
