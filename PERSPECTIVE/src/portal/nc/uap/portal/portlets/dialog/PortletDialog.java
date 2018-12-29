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
 * 新建portlet对话框
 * 
 * @author dingrf
 */
public class PortletDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;

	/**名称文本框*/
	private Text nameText;
	
	/**ID*/
	private String id;

	/**名称*/
	private String name;
	
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

	public PortletDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.PortletDialog_0, M_portal.PortletDialog_1);
			idText.setFocus();
			return;
		} else if ("".equals(nameText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_portal.PortletDialog_0, M_portal.PortletDialog_2);
			nameText.setFocus();
			return;
		} 
		id = idText.getText();
		name = nameText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(250, 150);
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("id:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(150, 1));

		new Label(container, SWT.NONE).setText("name:"); //$NON-NLS-1$
		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(createGridData(150, 1));
		
		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

