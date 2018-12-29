package nc.uap.lfw.perspective.listener;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.LfwParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * DatasetChangeEvent的参数选择对话框
 * 
 * @author guoweic
 *
 */
public class DataChangeParamDialog extends Dialog {
	
	private DataChangeParamPanel mainContainer;
	
	private Dataset dataset = null;
	
	private LfwParameter extendParameter = null;

	protected DataChangeParamDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Point getInitialSize() {
		return new Point(450, 500);
	}
	
	
	public Control createDialogArea(Composite parent) {

		mainContainer = new DataChangeParamPanel(parent, SWT.NONE, dataset, extendParameter);
		
		return mainContainer;
	}
	
	protected void okPressed() {
		super.okPressed();
	}

	public DataChangeParamPanel getMainContainer() {
		return mainContainer;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public void setExtendParameter(LfwParameter extendParameter) {
		this.extendParameter = extendParameter;
	}

}
