package nc.uap.lfw.application;

import nc.lfw.design.view.LFWWfmConnector;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_application;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import com.sun.org.apache.regexp.internal.recompile;

public class AppMenuCateSelPage extends WizardPage{
	
	private List menuList;
	
	private Button printBtn;
	
	private Button queryBtn;
	
	private CpMenuCategoryVO[] menuCates;

	protected AppMenuCateSelPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_EXCEL_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_EXCEL_MENU_DESC)); 
	}

	@Override
	public void createControl(Composite parent) {
		
		

		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setSize(400, 200);
		group.setLayout(new GridLayout(1, false));
		group.setText("可选菜单分类"); //$NON-NLS-1$	
		menuCates = LFWWfmConnector.getMenuCategory();
		String[] menuTitles = new String[menuCates.length];
		for(int i =0;i<menuCates.length;i++){
			CpMenuCategoryVO menuCate = menuCates[i];
			menuTitles[i] = menuCate.getTitle();
		}
		menuList = new List(group,SWT.SINGLE);
		menuList.setLayoutData(new GridData(GridData.FILL_BOTH));
		menuList.setItems(menuTitles);
		queryBtn = new Button(composite,SWT.CHECK);
		queryBtn.setText(M_application.AppMenuCateSelPage_0);
		printBtn = new Button(composite,SWT.CHECK);
		printBtn.setText(M_application.AppMenuCateSelPage_1);
		
		setControl(composite);
		
	}
	
	public String getSelectedMenuCateId(){
		int index = menuList.getSelectionIndex();
		if(index==-1) return null;
		return menuCates[index].getId();
	}
	public Boolean[] getTemplateSel(){
		Boolean[] tempSel = new Boolean[2];
		tempSel[0] = queryBtn.getSelection();
		tempSel[1] = printBtn.getSelection();
		return tempSel;
	}
	
}
