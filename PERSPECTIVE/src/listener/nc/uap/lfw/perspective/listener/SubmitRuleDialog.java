package nc.uap.lfw.perspective.listener;

import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.page.LfwView;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 提交规则对话框
 * @author guoweic
 *
 */
public class SubmitRuleDialog extends Dialog {
	
	private SubmitRulePanel mainContainer;
	
//	private LfwWindow pagemeta = null;
	
	private LfwView widget = null;
	
	private EventSubmitRule submitRule = null;
	
	private boolean isParentSubmitRule = false;

	public SubmitRuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected Point getInitialSize() {
		return new Point(400, 500);
	}
	
	
	public Control createDialogArea(Composite parent) {

//		mainContainer = new SubmitRulePanel(parent, SWT.NONE, pagemeta, submitRule, isParentSubmitRule);
		mainContainer = new SubmitRulePanel(parent, SWT.NONE, widget, submitRule, isParentSubmitRule);
		
		return mainContainer;
	}
	
	protected void okPressed() {
		super.okPressed();
	}

	public SubmitRulePanel getMainContainer() {
		return mainContainer;
	}

//	public void setPagemeta(LfwWindow pagemeta) {
//		this.pagemeta = pagemeta;
//	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}

	public void setParentSubmitRule(boolean isParentSubmitRule) {
		this.isParentSubmitRule = isParentSubmitRule;
	}
	

}
