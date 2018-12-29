package nc.uap.lfw.application;

import java.util.List;


import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.editor.common.tools.LFWTool;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class TempConfigPage extends WizardPage{

	private StringDialogField appIdField; 
	private StringDialogField appNameField; 
	private ComboDialogField appLocationField; 
	private Combo sourceFolderCombo;
	
	protected TempConfigPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC)); 
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(3, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		appIdField = new StringDialogField();
		appIdField.setLabelText("Application Id:");
		appIdField.doFillIntoGrid(composite, 3);
		appNameField = new StringDialogField();
		appNameField.setLabelText("Application 名称:");
		appNameField.doFillIntoGrid(composite, 3);
		appLocationField = new ComboDialogField(0);
		appLocationField.setLabelText("Application源文件夹");
		
		List<String> sourceFolderList = LFWTool.getAllRootPackage();
		appLocationField.setItems(sourceFolderList.toArray(new String[0])); 
		appLocationField.doFillIntoGrid(composite, 3);
		appLocationField.selectItem("src/public/");
		if(!"src/public/".equals(appLocationField.getText())) appLocationField.selectItem(0);
//		for (int i = 0 ; i < sourceFolderList.size(); i++ ) {
//			String sourceFolder = sourceFolderList.get(i);
//			appLocationField.
//			if(sourceFolder.equals("src/public/")){
//				sourceFolderCombo.select(i);
//				selected = true;
//			}
//		}
//		sourceFolderCombo = new Combo(composite, SWT.READ_ONLY);
//
//		boolean selected  = false;
//		List<String> sourceFolderList = LFWTool.getAllRootPackage();
//		for (int i = 0 ; i < sourceFolderList.size(); i++ ) {
//			String sourceFolder = sourceFolderList.get(i);
//			sourceFolderCombo.add(sourceFolder);
//			sourceFolderCombo.setData(sourceFolder, sourceFolder);
//			if(sourceFolder.equals("src/public/")){
//				sourceFolderCombo.select(i);
//				selected = true;
//			}
//		}
//		if(sourceFolderCombo.getItemCount() > 0 && !selected){
//			sourceFolderCombo.select(0);
//		}
//		appLocationField.
//		appLocationField.setLabelText("Application源文件夹");		
		setControl(composite);
		
	}
	
	public String getAppId(){
		return appIdField.getText();
	}
	
	public String getAppCaption(){
		return appNameField.getText();
	}
	
	public String getLocation(){
		return appLocationField.getText();
	}
	

}
