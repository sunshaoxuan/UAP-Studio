package nc.uap.portal.category.dialog;


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
 * 新建portlet分类对话框
 * 
 * @author dingrf
 */
public class CategoryDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**Text文本框*/
	private Text textText;
	
	/**多语文本框*/
	private Text i18nNameText;

	/**分类ID*/
	private String id;

	/**分类名称*/
	private String text;
	
	/**多语*/
	private String i18nName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public Text getIdText() {
		return idText;
	}

	public Text getTextText() {
		return textText;
	}

	public Text getI18nNameText() {
		return i18nNameText;
	}

	public CategoryDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	/**
	 * 确定按钮
	 */
	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.CategoryDialog_0, M_portal.CategoryDialog_1);
			idText.setFocus();
			return;
		} else if ("".equals(textText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.CategoryDialog_0, M_portal.CategoryDialog_2);
			textText.setFocus();
			return;
		} 
		id = idText.getText();
		text = textText.getText();
		i18nName =i18nNameText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(250, 180);
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("ID:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("text:"); //$NON-NLS-1$
		textText = new Text(container, SWT.BORDER);
		textText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("i18nName:"); //$NON-NLS-1$
		i18nNameText = new Text(container, SWT.BORDER);
		i18nNameText.setLayoutData(createGridData(150, 1));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

