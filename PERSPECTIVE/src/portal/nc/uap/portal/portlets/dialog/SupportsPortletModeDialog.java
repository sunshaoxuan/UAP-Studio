package nc.uap.portal.portlets.dialog;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletMode;

import nc.uap.portal.lang.M_portal;

import org.eclipse.jface.dialogs.Dialog;
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
 * portlet模式选择对话框
 * 
 * @author dingrf
 *
 */
public class SupportsPortletModeDialog extends Dialog {
	
	/**传入的初始状态*/
	private String modes = ""; //$NON-NLS-1$
	
	/**选中状态*/
	private List<String> selectedModes = new ArrayList<String>();
	
	/**所有状态类型*/
	private List<PortletMode> allModes = null;
	
	/**返回状态*/
	private String result = ""; //$NON-NLS-1$

	public SupportsPortletModeDialog(Shell parentShell) {
		super(parentShell);
	}
	
//	protected Point getInitialSize() {
//		return new Point(250, 160); 
//	}
	
	public String getResult(){
		return result;
	}
	
	protected void okPressed() {
		List<String> selects = getSelectedModes();
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
		group.setText(M_portal.SupportsPortletModeDialog_0);
		
		group.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalIndent = 5;
		group.setLayoutData(gridData);
		getAllModes();
		if (allModes.size() > 0) {
			for (PortletMode mode : allModes) {
				Button check = new Button(group, SWT.CHECK);
				check.setLayoutData(createGridData(100, 1));
				check.setText(mode.toString());
				//check.setData(mode.toString(), mode);
				if (checkModesContained(mode.toString())) {
					check.setSelection(true);
					selectedModes.add(mode.toString());
				}
				check.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Button check = (Button) e.getSource();
						String value = check.getText();
						if (check.getSelection()) {
							selectedModes.add(value);
						} else if (selectedModes.contains(value)) {
							selectedModes.remove(value);
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
	private boolean checkModesContained(String modeValue) {
		String[] modeArray = modes.split(","); //$NON-NLS-1$
		for (String value : modeArray) {
			if (modeValue.equalsIgnoreCase(value.trim()))
				return true;
		}
		return false;
	}
	
	/**
	 * 获取所有状态类型
	 * @return
	 */
	private List<PortletMode> getAllModes() {
		if (allModes == null) {
			allModes = new ArrayList<PortletMode>();
			allModes.add(new PortletMode("edit")); //$NON-NLS-1$
			allModes.add(new PortletMode("help")); //$NON-NLS-1$
			allModes.add(new PortletMode("view")); //$NON-NLS-1$
		}
		return allModes;
	}
	
	private GridData createGridData(int width, int horizontalSpan) {
		GridData gridData = new GridData(width, 15);
		gridData.horizontalSpan = horizontalSpan;
		return gridData;
	}

	public List<String> getSelectedModes() {
		return selectedModes;
	}

	public void setModels(String modes) {
		this.modes = modes;
	}
}
