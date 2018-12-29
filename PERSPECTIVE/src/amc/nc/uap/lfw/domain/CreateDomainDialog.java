package nc.uap.lfw.domain;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_window;


public class CreateDomainDialog extends DialogWithTitle{
	
	private Text code = null;
	private Text id = null;
	private Text name = null;
	
	private String codeText;
	private String idText;
	private String nameText;

	public CreateDomainDialog(String title) {
		super(null, title);
	}
	protected Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(500, 200));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label codeLabel = new Label(composite, SWT.NONE);
		codeLabel.setText(M_window.CreateDomainDialog_0);
		code = new Text(composite, SWT.NONE);
		code.setLayoutData(new GridData(220,20));
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(M_window.CreateDomainDialog_1);
		name = new Text(composite, SWT.NONE);
		name.setLayoutData(new GridData(220,20));
		Label idLabel = new Label(composite, SWT.NONE);
		idLabel.setText(M_window.CreateDomainDialog_2);
		id = new Text(composite, SWT.NONE);		
		id.setLayoutData(new GridData(220,20));
		
		return composite;	
	}
	
	public String getIdText() {
		return idText;
	}
	public void setIdText(String idText) {
		this.idText = idText;
	}
	public String getNameText() {
		return nameText;
	}
	public void setNameText(String nameText) {
		this.nameText = nameText;
	}
	
	public String getCodeText() {
		return codeText;
	}
	public void setCodeText(String codeText) {
		this.codeText = codeText;
	}
	protected void okPressed() {
		if(code.getText()!=""&&name.getText()!=""&&id.getText()!=""){ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setCodeText(code.getText());
			setNameText(name.getText());
			setIdText(id.getText());
			super.okPressed();
		}
		else
			MessageDialog.openError(getShell(), M_window.CreateDomainDialog_3, M_window.CreateDomainDialog_4);
	}

}
