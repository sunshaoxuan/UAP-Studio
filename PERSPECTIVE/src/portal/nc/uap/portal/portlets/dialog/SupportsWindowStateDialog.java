package nc.uap.portal.portlets.dialog;

import nc.uap.portal.lang.M_portal;

import org.eclipse.jface.dialogs.Dialog;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.WindowState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 * window状态选择对话框
 * 
 * @author dingrf
 *
 */
public class SupportsWindowStateDialog extends Dialog {
	
	/**传入的初始状态*/
	private String states = ""; //$NON-NLS-1$
	
	/**选中状态*/
	private List<String> selectedStates = new ArrayList<String>();
	
	/**所有状态类型*/
	private List<WindowState> allStates = null;
	
	/**返回状态*/
	private String result = ""; //$NON-NLS-1$

	public SupportsWindowStateDialog(Shell parentShell) {
		super(parentShell);
	}
	
//	protected Point getInitialSize() {
//		return new Point(250, 160); 
//	}
	
	public String getResult(){
		return result;
	}
	
	protected void okPressed() {
		List<String> selects = getSelectedStates();
		for (int i = 0; i < selects.size(); i++) {
			if(i != selects.size() -1 )
				result += selects.get(i) + ","; //$NON-NLS-1$
			else 
				result += selects.get(i);
		}
		super.okPressed();
	}
	
	
	public Control createDialogArea(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group group = new Group(container, SWT.NONE);
		group.setText(M_portal.SupportsWindowStateDialog_0);
		
		group.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalIndent = 5;
		group.setLayoutData(gridData);
		getAllStates();
		if (allStates.size() > 0) {
			for (WindowState state : allStates) {
				Button check = new Button(group, SWT.CHECK);
				check.setLayoutData(createGridData(100, 1));
				check.setText(state.toString());
				//check.setData(mode.toString(), mode);
				if (checkStatesContained(state.toString())) {
					check.setSelection(true);
					selectedStates.add(state.toString());
				}
				check.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Button check = (Button) e.getSource();
						String value = check.getText();
						if (check.getSelection()) {
							selectedStates.add(value);
						} else if (selectedStates.contains(value)) {
							selectedStates.remove(value);
						}
					}
				});
			}
		}
		return container;
	}
	
	/**
	 * 校验是否包含了该模式
	 * @return
	 */
	private boolean checkStatesContained(String StateValue) {
		String[] stateArray = states.split(","); //$NON-NLS-1$
		for (String value : stateArray) {
			if (StateValue.equalsIgnoreCase(value.trim()))
				return true;
		}
		return false;
	}
	
	/**
	 * 获取所有状态类型
	 * @return
	 */
	private List<WindowState> getAllStates() {
		if (allStates == null) {
			allStates = new ArrayList<WindowState>();
			allStates.add(new WindowState("maximized")); //$NON-NLS-1$
			allStates.add(new WindowState("minimized")); //$NON-NLS-1$
			allStates.add(new WindowState("normal")); //$NON-NLS-1$
		}
		return allStates;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	public List<String> getSelectedStates() {
		return selectedStates;
	}

	public void setStates(String states) {
		this.states = states;
	}
}
