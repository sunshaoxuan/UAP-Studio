package nc.lfw.editor.widget.plug;


import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_pagemeta;

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
 * 新建plugindesc对话框
 * 
 * @author dingrf
 */
public class PluginDescDialog extends DialogWithTitle {

	/**ID文本框*/
	private Text idText;
	/**方法名文本框*/
	private Text methodText;

	/**ID*/
	private String id;
	
	/**方法名*/
	private String method;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Text getIdText() {
		return idText;
	}

	public void setIdText(Text idText) {
		this.idText = idText;
	}
	

	public Text getMethodText() {
		return methodText;
	}

	public void setMethodText(Text methodText) {
		this.methodText = methodText;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public PluginDescDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	protected void okPressed() {
		if ("".equals(idText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_pagemeta.PluginDescDialog_0, M_pagemeta.PluginDescDialog_1);
			idText.setFocus();
			return;
		} 
		if ("".equals(methodText.getText())) { //$NON-NLS-1$
			MessageDialog.openInformation(this.getShell(), M_pagemeta.PluginDescDialog_0, M_pagemeta.PluginDescDialog_2);
			methodText.setFocus();
			return;
		} 
		id = idText.getText();
		method = methodText.getText();
		super.okPressed();
	}


	protected Point getInitialSize() {
		return new Point(250, 200);
	}

	@Override
	protected boolean isResizable(){
		return true;
	}
	
	protected Control createDialogArea(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		new Label(container, SWT.NONE).setText("id:"); //$NON-NLS-1$
		idText = new Text(container, SWT.BORDER);
		idText.setLayoutData(createGridData(150, 1));
		
		new Label(container, SWT.NONE).setText(M_pagemeta.PluginDescDialog_3);
		methodText = new Text(container, SWT.BORDER);
		methodText.setLayoutData(createGridData(150, 1));

		return container;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}
}

