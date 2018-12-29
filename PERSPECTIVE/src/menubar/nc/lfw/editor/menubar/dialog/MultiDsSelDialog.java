package nc.lfw.editor.menubar.dialog;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;

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
 * 多选DS选择对话框
 * @author guoweic
 *
 */
public class MultiDsSelDialog extends DialogWithTitle {

	private List<String> dsIds = null;
	
	private List<String> selectedDsIds = new ArrayList<String>();
	
	public MultiDsSelDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Point getInitialSize() {
		return new Point(350, 250); 
	}
	
	
	public Control createDialogArea(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group group = new Group(container, SWT.NONE);
		group.setText("选择数据集");
		
		group.setLayout(new GridLayout(3, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalIndent = 5;
		group.setLayoutData(gridData);
		
		if (null != dsIds) {
			for (String id : dsIds) {
				Button check = new Button(group, SWT.CHECK);
				check.setText(id);
				check.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						Button check = (Button) e.getSource();
						String id = check.getText();
						if (check.getSelection()) {
							selectedDsIds.add(id);
						} else if (selectedDsIds.contains(id)) {
							selectedDsIds.remove(id);
						}
					}
				});
			}
		}
		
		return container;
	}

	public void setDsIds(List<String> dsIds) {
		this.dsIds = dsIds;
	}

	public List<String> getSelectedDsIds() {
		return selectedDsIds;
	}

}
