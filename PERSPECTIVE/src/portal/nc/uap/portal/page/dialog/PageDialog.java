package nc.uap.portal.page.dialog;


import nc.lfw.editor.common.DialogWithTitle;

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
 * 新建page对话框
 * 
 * @author dingrf
 */
public class PageDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**标题文本框*/
	private Text titleText;
	
	/**ID*/
	private String id;

	/**标题*/
	private String title;
	
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

	public Text getIdText() {
		return idText;
	}

	public Text getTitleText() {
		return titleText;
	}

	public void setTitleText(Text titleText) {
		this.titleText = titleText;
	}

	public void setIdText(Text idText) {
		this.idText = idText;
	}

	public PageDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	@Override
	protected void okPressed() {
		if ("".equals(idText.getText())) {
			MessageDialog.openInformation(this.getShell(), "提示", "请输入ID");
			idText.setFocus();
			return;
		} else if ("".equals(titleText.getText())) {
			MessageDialog.openInformation(this.getShell(), "提示", "请输入text");
			titleText.setFocus();
			return;
		} 
		id = idText.getText();
		title = titleText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(250, 160);
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("id:");
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("title:");
		titleText = new Text(container, SWT.BORDER);
		titleText.setLayoutData(createGridData(150, 1));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

