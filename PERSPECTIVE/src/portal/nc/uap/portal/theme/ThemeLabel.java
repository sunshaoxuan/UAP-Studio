package nc.uap.portal.theme;

import nc.uap.portal.om.Theme;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

/**
 * Theme label
 * 
 * @author dingrf
 */
public class ThemeLabel extends Label{

	/**Theme*/
	private Theme theme;

	public ThemeLabel(String text, Theme theme) {
		super(text);
		this.theme = theme;
		setLabelAlignment(PositionConstants.LEFT);
		markError();
	}

	private void markError() {

	}

	public Theme getTheme() {
		return theme;
	}
}
