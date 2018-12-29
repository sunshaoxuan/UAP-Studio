package nc.uap.lfw.refnode;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AreaDialog extends Dialog {
	
	private Text text = null;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
	
	private String originalValue;

	public AreaDialog(Shell parentShell, String originalValue) {
		super(parentShell);
		this.originalValue =  originalValue;
	}

	private String result;
	public String getResult(){
		return result;
	}
	
	protected void okPressed() {
		result = text.getText();
		super.okPressed();
	}
	
	protected Point getInitialSize() {
		return new Point(350,200); 
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		text = new Text(container, SWT.BORDER| SWT.WRAP);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		if(originalValue != null)
			text.setText(originalValue);
		return container;
	}
}
	

