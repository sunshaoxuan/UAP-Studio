package nc.uap.portal.portlets.dialog;


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
 * 新建iframeportlet对话框
 * 
 * @author dingrf
 */
public class IframePortletDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**名称文本框*/
	private Text nameText;
	
	/**URL文本框*/
	private Text urlText;
	
	/**ID*/
	private String id;

	/**名称*/
	private String name;
	
	/**URL*/
	private String url;
	
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Text getIdText() {
		return idText;
	}

	public Text getNameText() {
		return nameText;
	}

	public void setNameText(Text nameText) {
		this.nameText = nameText;
	}

	public void setIdText(Text idText) {
		this.idText = idText;
	}

	public Text getUrlText() {
		return urlText;
	}

	public void setUrlText(Text urlText) {
		this.urlText = urlText;
	}

	public IframePortletDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.IframePortletDialog_0, M_portal.IframePortletDialog_1);
			idText.setFocus();
			return;
		} else if ("".equals(nameText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.IframePortletDialog_0, M_portal.IframePortletDialog_2);
			nameText.setFocus();
			return;
		} 
		id = idText.getText();
		name = nameText.getText();
		url = urlText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(350, 180);
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
		
		new Label(container, SWT.NONE).setText("URL:"); //$NON-NLS-1$
		urlText = new Text(container, SWT.BORDER);
		urlText.setLayoutData(createGridData(250, 1));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

