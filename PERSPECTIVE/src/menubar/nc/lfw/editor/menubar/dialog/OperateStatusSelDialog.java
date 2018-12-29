package nc.lfw.editor.menubar.dialog;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.menubar.BillOperateState;

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
 * 单据状态选择对话框
 * 
 * @author guoweic
 *
 */
public class OperateStatusSelDialog extends DialogWithTitle {
	
	// 传入的初始状态
	private String states = "";
	// 选中状态
	private List<String> selectedStates = new ArrayList<String>();
	// 所有状态类型
	private List<BillOperateState> allStates = null;
	
	public OperateStatusSelDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Point getInitialSize() {
		return new Point(350, 250); 
	}
	
	public String getResult(){
		return result;
	}
	private String result = "";
	
	protected void okPressed() {
		List<String> selects = getSelectedStates();
		for (int i = 0; i < selects.size(); i++) {
			if(i != selects.size() -1 )
				result += selects.get(i) + ",";
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
		group.setText("选择单据状态");
		
		group.setLayout(new GridLayout(3, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalIndent = 5;
		group.setLayoutData(gridData);
		
		getAllStates();
		
		if (allStates.size() > 0) {
			for (BillOperateState state : allStates) {
				Button check = new Button(group, SWT.CHECK);
				check.setLayoutData(createGridData(100, 1));
				check.setText(state.getText());
				check.setData(state.getText(), state.getValue());
				if (checkStatesContained(state.getValue())) {
					check.setSelection(true);
					selectedStates.add(state.getValue());
				}
				check.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Button check = (Button) e.getSource();
						String value = (String) check.getData(check.getText());
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
	 * 校验是否包含了该状态
	 * @return
	 */
	private boolean checkStatesContained(String stateValue) {
		String[] stateArray = states.split(",");
		for (String value : stateArray) {
			if (stateValue.equals(value.trim()))
				return true;
		}
		return false;
	}
	
	/**
	 * 获取所有状态类型
	 * @return
	 */
	private List<BillOperateState> getAllStates() {
		if (allStates == null) {
			allStates = new ArrayList<BillOperateState>();
			allStates.add(new BillOperateState("初始", BillOperateState.OP_INIT));
			allStates.add(new BillOperateState("单行选中", BillOperateState.OP_SINGLESEL));
			allStates.add(new BillOperateState("多行选中", BillOperateState.OP_MULTISEL));
			allStates.add(new BillOperateState("新建", BillOperateState.OP_ADD));
			allStates.add(new BillOperateState("编辑", BillOperateState.OP_EDIT));
			//TODO 不全
			
			allStates.add(new BillOperateState("所有状态", BillOperateState.OP_ALL));
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
