package nc.uap.portal.page.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.portal.core.PortalConnector;

public class CreatePageDialog extends DialogWithTitle{

	private Combo combo;
	
	private int selectIndex;
	
	public CreatePageDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}
	public Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		combo = new Combo(composite, SWT.READ_ONLY);
		CpMenuCategoryVO[] menuCates = LFWWfmConnector.getMenuCategory();
		int index = 0;
		for(CpMenuCategoryVO menu:menuCates){
			if(menu.getTitle()==null)
				continue;
			combo.add(menu.getTitle(), index);
			index++;
		}
		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectIndex = combo.getSelectionIndex();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectIndex = combo.getSelectionIndex();
			}
		});
		
		return composite;
		
	}
	public int getSelection(){		
		return selectIndex;
	}
	

}
