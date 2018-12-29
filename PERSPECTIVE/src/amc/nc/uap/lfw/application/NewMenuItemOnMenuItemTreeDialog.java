/**
 * 
 */
package nc.uap.lfw.application;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Control;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.lfw.lang.M_application;

/**
 * @TODO 在菜单树上新建菜单,并可选择父菜单
 * @author guomq1
 * @data 2012-8-23
 */
public class NewMenuItemOnMenuItemTreeDialog extends DialogWithTitle {
	public StringDialogField menuItemIdField; 
	public StringDialogField menuItemNameField; 
	public StringDialogField parentmenuitemField; 
	private String menuCategory; 
	private String menuCateName; 
	private CpMenuItemVO menuItemVo; 
	private CpMenuItemVO parentMenuItemVo; 

	
	private MenuItemShowDidalog newparentmenu = null;


	public NewMenuItemOnMenuItemTreeDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}	
	public NewMenuItemOnMenuItemTreeDialog(Shell shell, String title,
			String menuCategory, String menuCateName,CpMenuItemVO parentItemVo) {
		super(shell, title);
		this.menuCategory = menuCategory;
		this.menuCateName = menuCateName;
		setParentMenuItemVo(parentItemVo);
		
	}
	public NewMenuItemOnMenuItemTreeDialog(Shell shell, String title,
			String menuCategory, String menuCateName, CpMenuItemVO treeItemVo, CpMenuItemVO parentItemVo) {
		super(shell, title);
		this.menuCategory = menuCategory;
		this.menuCateName = menuCateName;
		this.menuItemVo= treeItemVo;
		setParentMenuItemVo(parentItemVo);
	}

	protected Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(350,150));
		
		parentmenuitemField = new StringDialogField();
		parentmenuitemField.setLabelText(M_application.NewMenuItemOnMenuItemTreeDialog_0);
		parentmenuitemField.doFillIntoGrid(composite, 4);
		if(parentMenuItemVo == null){
			parentmenuitemField.setText(menuCateName);
		}else parentmenuitemField.setText(parentMenuItemVo.getName());
		
//		final Button newparentmenubt = new Button(composite,SWT.BUTTON1);
////		newparentmenubt.setLayoutData(new GridData(150, 20));
//		newparentmenubt.setText(M_application.NewMenuItemOnMenuItemTreeDialog_1);
//		newparentmenubt.setSize(100, 20);
		
		menuItemIdField = new StringDialogField();
		menuItemIdField.setLabelText(M_application.NewMenuItemOnMenuItemTreeDialog_2);
		menuItemIdField.doFillIntoGrid(composite, 4);
		
		menuItemNameField = new StringDialogField();
		menuItemNameField.setLabelText(M_application.NewMenuItemOnMenuItemTreeDialog_3);
		menuItemNameField.doFillIntoGrid(composite,4);

		
		parentmenuitemField.setEnabled(false);
//		if(menuItemVo != null && parentMenuItemVo != null){
		if(menuItemVo != null){
			menuItemIdField.setText(menuItemVo.getCode());
			menuItemNameField.setText(menuItemVo.getName());
		}
		

		

//		newparentmenubt.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event){
//				newparentmenubt.setEnabled(false);
//				if(menuCategory != null && menuCateName != null){
//				newparentmenu = new MenuItemShowDidalog(getShell(), M_application.NewMenuItemOnMenuItemTreeDialog_4,menuCategory,menuCateName);
//				}
//				if(newparentmenu.open() == IDialogConstants.OK_ID){
//
//					parentmenuitemField.setText(newparentmenu.getMenuItemName());
//					setParentMenuItemVo(newparentmenu.getMenuItem());
//				}else{
//					newparentmenubt.setEnabled(true);
//					}
//				newparentmenubt.setEnabled(true);
//				}
//
//
//			});
		return composite;
		}
	protected void okPressed() {
		if(parentmenuitemField.getText()==null||"".equals(parentmenuitemField.getText())){ //$NON-NLS-1$
			if(MessageDialog.openConfirm(getShell(), M_application.NewMenuItemOnMenuItemTreeDialog_5, M_application.NewMenuItemOnMenuItemTreeDialog_6))return;	
		}
		if(menuItemIdField.getText()==null||"".equals(menuItemIdField.getText())){ //$NON-NLS-1$
			if(MessageDialog.openConfirm(getShell(), M_application.NewMenuItemOnMenuItemTreeDialog_7, M_application.NewMenuItemOnMenuItemTreeDialog_8))return;	
		}
		if(menuItemNameField.getText()==null||"".equals(menuItemNameField.getText())){ //$NON-NLS-1$
			if(MessageDialog.openConfirm(getShell(), M_application.NewMenuItemOnMenuItemTreeDialog_9, M_application.NewMenuItemOnMenuItemTreeDialog_10))return;	
		}
		if(menuItemVo == null){
			CpMenuItemVO parentmi = LFWWfmConnector.getMenuItemById(menuItemIdField.getText());
			if(parentmi != null){
					if(MessageDialog.openConfirm(getShell(), M_application.NewMenuItemOnMenuItemTreeDialog_11, M_application.NewMenuItemOnMenuItemTreeDialog_12)){
						menuItemIdField.setText(""); //$NON-NLS-1$
						menuItemNameField.setText(""); //$NON-NLS-1$
						return;
					}else return;
					
				}
		}
		super.okPressed();
	}
	
	public void setParentMenuItemVo(CpMenuItemVO menuItem) {
		this.parentMenuItemVo = menuItem;
		
	}
	public CpMenuItemVO getParentMenuItemVo() {
		return this.parentMenuItemVo;
		
	}
}