package nc.uap.portal.freemarker.outline;

import nc.uap.portal.freemarker.ImageManager;
import nc.uap.portal.freemarker.model.Item;
import nc.uap.portal.freemarker.preferences.IPreferenceConstants;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class OutlineLabelProvider
	extends LabelProvider
	implements IPreferenceConstants {

	public OutlineLabelProvider() {
		super();
	}

	public Image getImage(Object anElement) {
		if (null == anElement)
			return null;
		if (anElement instanceof Item) {
			return ImageManager.getImage(((Item) anElement).getTreeImage());
		}
		else {
			return null;
		}
	}

	public String getText(Object anElement) {
		if (anElement instanceof Item)
			return ((Item) anElement).getTreeDisplay();
		else
			return null;
	}

	public void dispose() {
	}	
}
