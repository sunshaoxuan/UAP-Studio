package nc.uap.portal.theme.dialog;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.portal.lang.M_portal;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 新建主题对话框
 * 
 * @author dingrf
 */
public class ThemeDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**名称文本框*/
	private Text titleText;
	
	/**多语文本框*/
	private Text i18nNameText;
	
	/**主题ID文本框*/
	private Text themeIdText;

	/**主题ID*/
	private String id;

	/**主题名称*/
	private String title;
	
	/**主题多语id*/
	private String i18nName;
	
	/**主题ID*/
	private String themeId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		this.i18nName = name;
	}

	public String getThemeId() {
		return themeId;
	}
	
	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}
	
	public Text getIdText() {
		return idText;
	}

	public Text getTitleText() {
		return titleText;
	}

	public Text getI18nNameText() {
		return i18nNameText;
	}

	public Text getThemeIdText() {
		return themeIdText;
	}

	public ThemeDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.ThemeDialog_0, M_portal.ThemeDialog_1);
			idText.setFocus();
			return;
		} else if ("".equals(titleText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.ThemeDialog_0, M_portal.ThemeDialog_2);
			titleText.setFocus();
			return;
		} else if ("".equals(themeIdText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.ThemeDialog_0, M_portal.ThemeDialog_3);
			themeIdText.setFocus();
			return;
		}
		id = idText.getText();
		title = titleText.getText();
		i18nName =i18nNameText.getText();
		themeId =themeIdText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(250, 210);
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("ID:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("text:"); //$NON-NLS-1$
		titleText = new Text(container, SWT.BORDER);
		titleText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("i18nName:"); //$NON-NLS-1$
		i18nNameText = new Text(container, SWT.BORDER);
		i18nNameText.setLayoutData(createGridData(150, 1));
		
		new Label(container, SWT.NONE).setText("lfw-theme-id:"); //$NON-NLS-1$
		themeIdText = new Text(container, SWT.BORDER);
		themeIdText.setLayoutData(createGridData(150, 1));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

