package nc.uap.lfw.perspective.action;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * 代码生成设置
 * @author zhangxya
 *
 */

public class GeneratorCodeDialog  extends DialogWithTitle {

	private static final long serialVersionUID = 1L;
	private String rootPath;
	private String tableName;
	private Text tableNameText = null;
	private Combo rootPathField = null;
	private Button isSqlCode = null;
	private boolean isSql;
	
	public String getRootPath(){
		return rootPath;
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public boolean getIsSql(){
		return isSql;
	}
	
	public GeneratorCodeDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected void okPressed() {
		//tableName
		tableName = tableNameText.getText();
		if(tableName == null || "".equals(tableName)){
			MessageDialog.openError(null, "错误提示", "请设置表名!");
			return;
		}
		//包所在路径
		rootPath= rootPathField.getText();
		if(rootPath == null || "".equals(rootPath)){
			MessageDialog.openError(null, "错误提示", "请设置包所在路径!");
			return;
		}
		isSql = isSqlCode.isVisible();
		super.okPressed();
	}
	
		
	protected Point getInitialSize() {
		return new Point(300,200); 
	}

	protected Control createDialogArea(Composite parent) {
		//setTitle("生成代码对话框"); 
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(2,false));
		
		Label tableLabel = new Label(grouId, SWT.NONE);
		tableLabel.setText("表名:");
		tableNameText = new Text(grouId, SWT.NONE);
		tableNameText.setLayoutData(new GridData(140,15));
		
		
		Label rootPath = new Label(grouId, SWT.NONE);
		rootPath.setText("根路径:");
		rootPathField = new Combo(grouId, SWT.READ_ONLY);
		rootPathField.setLayoutData(new GridData(115,20));
		String[] rootPackages = LFWPersTool.getAllRootPackage();
		if(rootPackages !=  null)
			rootPathField.setItems(rootPackages);
		rootPathField.select(0);
		isSqlCode = new Button(grouId, SWT.CHECK);
		isSqlCode.setText("生成建库脚本");
		return container;
	}
}