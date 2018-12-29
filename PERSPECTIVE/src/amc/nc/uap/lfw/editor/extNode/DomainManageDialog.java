package nc.uap.lfw.editor.extNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;


import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.lang.M_editor;

public class DomainManageDialog extends DialogWithTitle{

	
	private ModuleListGroup moduleGroup;
	private SelModuleListGroup selGroup;
	private DependGroup dependGroup;
	private HashMap<String,ArrayList<String>> dependMapping = new HashMap();
	private ArrayList<String> selModuleList = new ArrayList();
	
	public DomainManageDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	protected Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(5, false));
		composite.setLayoutData(new GridData(600, 400));
		composite.setLocation(150, 150);
		moduleGroup = new ModuleListGroup(composite);
		selGroup = new SelModuleListGroup(composite);
		dependGroup = new DependGroup(composite);
		return composite;
	}
	public final class ModuleListGroup extends Observable implements IDialogFieldListener, Observer {

		private List moduleList;
		public ModuleListGroup(Composite composite){
			Group group = new Group(composite,SWT.NONE);
			group.setLayout(new GridLayout(1, false));			
			GridData layoutData = new GridData(150,380);
			group.setLayoutData(layoutData);
			group.setText(M_editor.DomainManageDialog_0);
			moduleList = new List(group,SWT.V_SCROLL);
			moduleList.setLayoutData(new GridData(GridData.FILL_BOTH));
			IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
			if(projects!=null){
				for(IProject module:projects){
					moduleList.add(module.getName());
				}				
			}
			Composite group2 = new Composite(composite, SWT.NONE);
			group2.setLayout(new GridLayout(1, false));
			GridData layoutData2 = new GridData(50,100);
			group2.setLayoutData(layoutData2);
			Button addBtn = new Button(group2, SWT.ARROW|SWT.RIGHT);
			addBtn.setLayoutData(new GridData(40,20));
			addBtn.addMouseListener(new MouseListener() {
				
				
				@Override
				public void mouseDown(MouseEvent e) {
					String[] selModule = moduleList.getSelection();
					if(selModule!=null){
						selGroup.addSelItem(selModule[0]);
					}
					
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}

				@Override
				public void mouseUp(MouseEvent e) {
					
				}

			});
			Button removeBtn = new Button(group2, SWT.ARROW|SWT.LEFT);
			removeBtn.setLayoutData(new GridData(40,20));
			removeBtn.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					int index = selGroup.getSelectIndex();
					String key = selGroup.getSelectItem();
					if(index>=0){
						selGroup.removeItem(index);
						dependMapping.remove(key);
						selGroup.moduleList.update();
					}
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
		
	}
	public final class SelModuleListGroup extends Observable implements IDialogFieldListener, Observer {
		private List moduleList;
		public SelModuleListGroup(Composite composite){
			Group group = new Group(composite,SWT.NONE);
			group.setLayout(new GridLayout(1, false));			
			GridData layoutData = new GridData(150,380);
			group.setLayoutData(layoutData);
			group.setText(M_editor.DomainManageDialog_1);
			moduleList = new List(group,SWT.V_SCROLL);
			if(selModuleList.size()>0){
				moduleList.setItems(selModuleList.toArray(new String[0]));
			}
			
			moduleList.setLayoutData(new GridData(GridData.FILL_BOTH));
			moduleList.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					for(Button btn:dependGroup.btnList){
						btn.setSelection(false);
					}
					if(dependMapping.get(moduleList.getSelection()[0])!=null){
						ArrayList<String> dependList = dependMapping.get(moduleList.getSelection()[0]);
						for(Button btn:dependGroup.btnList){
							if(dependList.contains(btn.getText())){
								btn.setSelection(true);
							}
						}
						dependGroup.dependList = dependList;
					}
					else{
						dependGroup.dependList = new ArrayList<String>();
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		public void addSelItem(String item){
			moduleList.add(item);
			moduleList.update();
		}
		public void removeItem(int index){
			moduleList.remove(index);
		}
		public String getSelectItem(){
			String[] selItem = moduleList.getSelection();
			if(selItem!=null){
				return selItem[0];
			}
			else return null;
		}
		
		public int getSelectIndex(){
//			String[] selItem = moduleList.getSelection();
			int index = moduleList.getSelectionIndex();
			if(index>=0){
				return index;
			}
			else return -1;
		}

		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public final class DependGroup extends Observable implements IDialogFieldListener, Observer {
		private ArrayList<Button> btnList = new ArrayList<Button>();
		private ArrayList<String> dependList = new ArrayList<String>();
		public DependGroup(Composite composite){
			Composite group2 = new Composite(composite, SWT.NONE);
			group2.setLayout(new GridLayout(1, false));
			GridData layoutData2 = new GridData(50,80);
			group2.setLayoutData(layoutData2);
			Button bindBtn = new Button(group2, SWT.PUSH);
			bindBtn.setLayoutData(new GridData(40,20));
			bindBtn.setText(M_editor.DomainManageDialog_2);
			bindBtn.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					String key = selGroup.getSelectItem();
					if(key!=null){
						dependMapping.put(key, dependList);
						dependList = new ArrayList<String>();
					}
					
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			Group group = new Group(composite,SWT.V_SCROLL);
			GridLayout layout = new GridLayout(1, false);
			group.setLayout(layout);			
			GridData layoutData = new GridData(150,380);
			group.setLayoutData(layoutData);
			group.setText(M_editor.DomainManageDialog_3);
			IProject[] projects = LFWPersTool.getOpenedBcpJavaProjects();
			if(projects!=null){
				for(IProject module:projects){
					Button btn = new Button(group, SWT.CHECK);
					btn.setText(module.getName());
					btn.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							String key = selGroup.getSelectItem();
							if(((Button)e.getSource()).getSelection()){
								dependList.add(((Button)e.getSource()).getText());
							}
							else{
								dependList.remove(((Button)e.getSource()).getText());
							}
							dependMapping.put(key, dependList);
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {

						}
					});
					btnList.add(btn);
				}
			}
			
		}
		

		public void addSelItem(){
			
		}
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
		
	}
		
	
		
		
	
	
	public HashMap<String, ArrayList<String>> getDependMapping() {
		return dependMapping;
	}
	public void setDependMapping(HashMap<String, ArrayList<String>> dependMapping) {
		this.dependMapping = dependMapping;
	}
	
	
	public ArrayList<String> getSelModuleList() {
		return selModuleList;
	}
	public void setSelModuleList(ArrayList<String> selModuleList) {
		this.selModuleList = selModuleList;
	}
	protected void okPressed() {
		selModuleList = new ArrayList();
		if(selGroup.moduleList.getItemCount()>0){
			for(String module:selGroup.moduleList.getItems()){
				selModuleList.add(module);
			}
			super.okPressed();
		}
		else{
			MessageDialog.openError(null, M_editor.DomainManageDialog_4, M_editor.DomainManageDialog_5);
		}
	}
	
	

}
