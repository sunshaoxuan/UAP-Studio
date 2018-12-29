/**
 * 手动发布应用
 */
package nc.uap.lfw.application;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;

/**
 * 手动发布应用
 * @author guomq1
 * 2012-8-14
 */
public class ManualAppNodeAction extends NodeAction {
	public ManualAppNodeAction(){
		super(WEBPersConstants.MANUAL_APPLICATION, WEBPersConstants.MANUAL_APPLICATION);
	}
	public void run() {
		Shell shell = new Shell(Display.getCurrent());
		Shell shell1 = new Shell(Display.getCurrent(), GridData.FILL_BOTH);
		shell1.setSize(800, 600);
		NewAppNodeManualDialog manualDialog = new NewAppNodeManualDialog(shell1,WEBPersConstants.MANUAL_APPLICATION);
		manualDialog.open();
//			manualDialog.open();
	}
}
