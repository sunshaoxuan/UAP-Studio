/**
 * 
 */
package nc.uap.lfw.window;

import nc.lfw.editor.pagemeta.RefreshNodeAction;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.palette.PaletteImage;

/**
 * 
 * ˢ��Window�ڵ���
 * @author chouhl
 *
 */
public class RefreshWindowNodeAction extends RefreshNodeAction {
	
	public RefreshWindowNodeAction(){
		super();
		setText(M_window.RefreshWindowNodeAction_0);
		setImageDescriptor(PaletteImage.getViewDescriptor());
	}

}
