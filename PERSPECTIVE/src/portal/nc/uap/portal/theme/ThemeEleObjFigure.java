package nc.uap.portal.theme;

import nc.uap.portal.core.PortalBaseRectangleFigure;
import nc.uap.portal.core.PortalElementObjWithGraph;
import nc.uap.portal.lang.M_portal;
import nc.uap.portal.om.Theme;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * LookAndFeel Figure 
 * 
 * @author dingrf
 */
public class ThemeEleObjFigure extends PortalBaseRectangleFigure{

	/**Figure Color*/
	private static Color bgColor = new Color(null, 239, 255, 150);
	
	/**Default height*/
	private int height = 150;
	
	public ThemeEleObjFigure(PortalElementObjWithGraph ele){
		super(ele);
		setTypeLabText(M_portal.ThemeEleObjFigure_0);
		setBackgroundColor(bgColor);
		ThemeElementObj theme = (ThemeElementObj) ele;
		theme.setFingure(this);
		Point point = theme.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		setBounds(new Rectangle(x, y, 150, 150));
		LoadThemeItem(theme);
	}
	
	protected String getTypeText() {
		return M_portal.ThemeEleObjFigure_0;
	}
	
	/**
	 * add MouseListener to ThemeLabel
	 * 
	 * @param label ThemeLabel
	 */
	public void addThemeLabelListener(ThemeLabel label) {
		label.addMouseListener(new MouseListener() {
			public void mouseDoubleClicked(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				ThemeLabel currentLabel = (ThemeLabel) e.getSource();
				selectedLabel(currentLabel);
			}

		});
	}

	/**
	 * Select Theme Label 
	 * 
	 * @param label ThemeLabel
	 */
	public void selectedLabel(ThemeLabel label) {
		unSelectAllLabels();
		selectLabel(label);

		Theme theme = (Theme) label.getTheme();
		((ThemeElementObj) getElementObj()).setCurrentTheme(theme);

		reloadPropertySheet((ThemeElementObj) getElementObj());
	}

	/**
	 * Load ThemeTtem
	 * 
	 * @param obj  ThemeElementObj
	 */
	private void LoadThemeItem(ThemeElementObj obj) {
		for(Theme theme : obj.getThemes()){
			ThemeLabel label = new ThemeLabel(theme.getTitle(),theme);
			addToContent(label);
			this.addThemeLabelListener(label);
		}
		resizeHeight();
	}

	/**
	 * resize figure height
	 */
	public void resizeHeight() {
		int ItemHeight = this.getContentFigure().getChildren().size()*this.LINE_HEIGHT;
		this.height= ItemHeight;
		if(height<150){
			height=150;
		}
		this.setSize(150,this.height);
	}
	
}
	
