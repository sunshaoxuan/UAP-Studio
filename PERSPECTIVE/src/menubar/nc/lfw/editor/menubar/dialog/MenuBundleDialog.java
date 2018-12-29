package nc.lfw.editor.menubar.dialog;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MenuBundleDialog extends DialogWithTitle{

private Text idText;
	
	private Combo gridCombo;
	
	private String gridId;

	public MenuBundleDialog(Shell parentShell, String title) {
		super(null, title);
		// TODO 自动生成的构造函数存根
	}
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label gridLabel = new Label(container, SWT.NONE);
		gridLabel.setText("GRID ID:");
		gridCombo= new Combo(container, SWT.PUSH);
		gridCombo.setLayoutData(new GridData(150, 1));
		gridCombo.removeAll();
		LfwView view = LFWPersTool.getCurrentWidget();
		
		WebComponent[] grids = view.getViewComponents().getComponentByType(GridComp.class);
		if(grids!=null&&grids.length>0){
			List<String> gridList = new ArrayList<String>();
			for(WebComponent grid:grids){
				gridList.add(grid.getId());
			}
			gridCombo.setItems(gridList.toArray(new String[0]));
		}
		
		return container;
		
		
	}
	
	
	public String getGridId() {
		return gridId;
	}
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	protected void okPressed() {
		if(gridCombo.getText()!=""){
			setGridId(gridCombo.getText());
		}
		super.okPressed();
	}
}
