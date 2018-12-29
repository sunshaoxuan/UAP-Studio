package nc.uap.portal.skin.dialog;

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
 *  样式对话框
 * 
 * @author dingrf
 */
public class SkinDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**名称文本框*/
	private Text nameText;
	
	/**i18nname文本框*/
	private Text i18nnameText;

	/**langdir文本框*/
	private Text langdirText;
	
	/**icon文本框*/
	private Text iconText;

	/**ID*/
	private String id;

	/**名称*/
	private String name;
	
	/**i18nname*/
	private String i18nname;

	/**langdir*/
	private String langdir;
	
	/**icon*/
	private String icon;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getI18nname() {
		return i18nname;
	}

	public void setI18nname(String i18nname) {
		this.i18nname = i18nname;
	}

	public String getLangdir() {
		return langdir;
	}

	public void setLangdir(String langdir) {
		this.langdir = langdir;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Text getIdText() {
		return idText;
	}

	public Text getNameText() {
		return nameText;
	}

	public Text getI18nnameText() {
		return i18nnameText;
	}

	public void setI18nnameText(Text i18nnameText) {
		this.i18nnameText = i18nnameText;
	}

	public Text getLangdirText() {
		return langdirText;
	}

	public void setLangdirText(Text langdirText) {
		this.langdirText = langdirText;
	}

	public Text getIconText() {
		return iconText;
	}

	public void setIconText(Text iconText) {
		this.iconText = iconText;
	}

	public SkinDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.SkinDialog_0, M_portal.SkinDialog_1);
			idText.setFocus();
			return;
		} else if ("".equals(nameText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.SkinDialog_0, M_portal.SkinDialog_2);
			nameText.setFocus();
			return;
		} 
		id = idText.getText();
		name = nameText.getText();
		i18nname = i18nnameText.getText();
		langdir = langdirText.getText();
		icon = iconText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(350, 250);
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("id:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(250, 1));

		new Label(container, SWT.NONE).setText("name:"); //$NON-NLS-1$
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(createGridData(250, 1));
		
		new Label(container, SWT.NONE).setText("i18nname:"); //$NON-NLS-1$
		i18nnameText = new Text(container, SWT.BORDER);
		i18nnameText.setLayoutData(createGridData(250, 1));

		new Label(container, SWT.NONE).setText("langdir:"); //$NON-NLS-1$
		langdirText = new Text(container, SWT.BORDER);
		langdirText.setLayoutData(createGridData(250, 1));
		
		new Label(container, SWT.NONE).setText("icon:"); //$NON-NLS-1$
		iconText = new Text(container, SWT.BORDER);
		iconText.setLayoutData(createGridData(250, 1));

		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

