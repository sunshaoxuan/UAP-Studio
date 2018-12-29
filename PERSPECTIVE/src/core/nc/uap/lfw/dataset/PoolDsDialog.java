package nc.uap.lfw.dataset;

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

public class PoolDsDialog extends DialogWithTitle {

	private Text dsNameText;
	private String dsId;
	private String defaultDsId;
	public String getDsId() {
		return dsId;
	}


	public void setDsId(String dsId) {
		this.dsId = dsId;
	}


	public PoolDsDialog(Shell parentShell, String title, String defaultDsId) {
		super(parentShell, title);
		this.defaultDsId = defaultDsId;
	}
	
	protected Point getInitialSize() {
		return new Point(400, 150); 
	}
	
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label pooldsIdLabel = new Label(container, SWT.NONE);
		pooldsIdLabel.setText("公共数据集ID:");
		dsNameText = new Text(container,SWT.NONE);
		dsNameText.setLayoutData(new GridData(225,20));
		dsNameText.setText(defaultDsId);
		return container;
	}
	
	protected void okPressed() {
		dsId = dsNameText.getText();
		if(dsId == null || dsId.equals("")){
			MessageDialog.openError(null, "错误提示", "公共数据集ID不能为空!");
			return;
		}
		super.okPressed();
	}
}
