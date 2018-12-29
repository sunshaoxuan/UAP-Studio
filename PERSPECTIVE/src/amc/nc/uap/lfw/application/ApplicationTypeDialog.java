package nc.uap.lfw.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import nc.lfw.editor.common.DialogWithTitle;

/**
 * ѡ��application������ʽ
 * @author qinjianc
 *
 */
public class ApplicationTypeDialog extends DialogWithTitle {
	
	private Button normalApp;
	
	/**
	 * �Ƿ���ͨ����
	 */
	private boolean isNormalApp = true;
	
	public ApplicationTypeDialog(String title) {
		super(null, title);
	}

	public ApplicationTypeDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		//��ʽ����
		new Label(container, SWT.NONE).setText("Application���ͣ�");
		Composite radioContainer = new Composite(container, SWT.NONE);
		radioContainer.setLayout(new GridLayout(2, false));
		radioContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		normalApp = new Button(radioContainer, SWT.RADIO);
		normalApp.setSelection(true);
		normalApp.setText("��ͨApplication����");
		Button tempApp = new Button(radioContainer, SWT.RADIO);
		tempApp.setSelection(false);
		tempApp.setText("ģ��Application����");
		return container;
	}
	protected void okPressed() {
		//��ʽ����
		setAppType(normalApp.getSelection());
		super.okPressed();
	}
	public boolean isNormalApp() {
		return isNormalApp;
	}

	public void setAppType(boolean isFlowlayout) {
		this.isNormalApp = isFlowlayout;
	}

}
