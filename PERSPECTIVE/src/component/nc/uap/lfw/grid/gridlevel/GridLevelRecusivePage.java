/**
 * 
 */
package nc.uap.lfw.grid.gridlevel;

import java.util.ArrayList;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.lang.M_grid;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelRecusivePage extends WizardPage {

	public Combo getTreeType() {
		return treeType;
	}
	public void setTreeType(Combo treeType) {
		this.treeType = treeType;
	}
	public Text getDataset() {
		return dataset;
	}
	public void setDataset(Text dataset) {
		this.dataset = dataset;
	}
	public Combo getRecursiveKeyField() {
		return recursiveKeyField;
	}
	public void setRecursiveKeyField(Combo recursiveKeyField) {
		this.recursiveKeyField = recursiveKeyField;
	}
	public Combo getRecursivePKeyField() {
		return recursivePKeyField;
	}
	public void setRecursivePKeyField(Combo recursivePKeyField) {
		this.recursivePKeyField = recursivePKeyField;
	}
	public List getLabelFields() {
		return labelFields;
	}
	public void setLabelFields(List labelFields) {
		this.labelFields = labelFields;
	}
	public Text getId() {
		return id;
	}
	public void setId(Text id) {
		this.id = id;
	}
	
	protected GridLevelRecusivePage(String pageName) {
		super(pageName);
	}

	private GridLevelElementObj treelevel = null;
	
	public GridLevelElementObj getTreelevel() {
		return treelevel;
	}
	public void setTreelevel(GridLevelElementObj treelevel) {
		this.treelevel = treelevel;
	}
	private LFWBasicElementObj treeobj = null;
	public GridLevelRecusivePage(String pageName, LFWBasicElementObj treeobj,  GridLevelElementObj treelevel, String flag){
		super(pageName);
		this.treelevel = treelevel;
		this.treeobj = treeobj;
	}
	
	protected IDialogSettings getDialogSettings() {
		return super.getDialogSettings();
	}

	private Combo treeType = null;
	private Text dataset = null;
	private Combo recursiveKeyField = null;
	private Combo recursivePKeyField = null;
	private List labelFields = null;
	private List rightlabelFields = null;
	public List getRightlabelFields() {
		return rightlabelFields;
	}
	public void setRightlabelFields(List rightlabelFields) {
		this.rightlabelFields = rightlabelFields;
	}

	private Text id = null;
	
	public IWizardPage getNextPage() {
		return null;
	}
	
	public void createControl(Composite parent) {
		setTitle(M_grid.GridLevelRecusivePage_0); 
		setMessage(M_grid.GridLevelRecusivePage_1);
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(2,false));
		Label labelId = new Label(grouId, SWT.NONE);
		labelId.setText(M_grid.GridLevelRecusivePage_2);
		id = new Text(grouId, SWT.BORDER);
		id.setLayoutData(new GridData(140,15));
		id.setText(M_grid.GridLevelRecusivePage_3);
		
		Label DatasetLabel = new Label(grouId, SWT.NONE);
		DatasetLabel.setText(M_grid.GridLevelRecusivePage_4);
		
		
		dataset = new Text(grouId, SWT.BORDER);
		dataset.setLayoutData(new GridData(140,15));
		dataset.setText(treelevel.getDs().getId());
		dataset.setEditable(false);
		
		
		Label recursiveKeyFieldLabel = new Label(grouId, SWT.NONE);
		recursiveKeyFieldLabel.setText(M_grid.GridLevelRecusivePage_5);
		recursiveKeyField = new Combo(grouId, SWT.READ_ONLY);
		recursiveKeyField.setLayoutData(new GridData(120,15));
		Field[] fields = treelevel.getDs().getFieldSet().getFields();
		java.util.List<String> list = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			String fieldId = fields[i].getId();
			if(fieldId != null)
				list.add(fieldId);
		}
		
		recursiveKeyField.setItems((String[])list.toArray(new String[list.size()]));
		recursiveKeyField.select(0);
		
		Label recursivePKeyFieldLabel = new Label(grouId, SWT.NONE);
		recursivePKeyFieldLabel.setText(M_grid.GridLevelRecusivePage_6);
		recursivePKeyField = new Combo(grouId, SWT.READ_ONLY);
		recursivePKeyField.setLayoutData(new GridData(120,15));
		recursivePKeyField.setItems((String[])list.toArray(new String[list.size()]));
		recursivePKeyField.select(0);
		
		Label labelFieldsLabel = new Label(grouId, SWT.NONE);
		labelFieldsLabel.setText(M_grid.GridLevelRecusivePage_7);
		
		Group labelFieldsGroup = new Group(grouId, SWT.NONE);
		labelFieldsGroup.setLayoutData(new GridData(450,150));
		labelFieldsGroup.setLayout(new GridLayout(3,false));
		labelFields = new List(labelFieldsGroup, SWT.BORDER|SWT.MULTI| SWT.V_SCROLL);
		labelFields.setLayoutData(new GridData(120,125));
		labelFields.setItems((String[])list.toArray(new String[list.size()]));
		
		Group buttongroup = new Group(labelFieldsGroup,SWT.NONE|SWT.VERTICAL);
		buttongroup.setLayoutData(new GridData(40,50));
		buttongroup.setLayout(new GridLayout(1,false));
		//向右的箭头
		Button rightbutton = new Button(buttongroup, SWT.RIGHT | SWT.ARROW | SWT.FILL);
		//向左的箭头
		Button leftbutton = new Button(buttongroup, SWT.LEFT | SWT.ARROW);
		
		rightlabelFields = new List(labelFieldsGroup, SWT.BORDER|SWT.MULTI| SWT.V_SCROLL);
		rightlabelFields.setLayoutData(new GridData(120,125));
		rightbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				String[] leftlables = labelFields.getSelection();
				for (int i = 0; i < leftlables.length; i++) {
					rightlabelFields.add(leftlables[i]);
					labelFields.remove(leftlables[i]);
				}
				
			}
		});
		
		leftbutton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				String[] rightlabels = rightlabelFields.getSelection();
				for (int i = 0; i < rightlabels.length; i++) {
					labelFields.add(rightlabels[i]);
					rightlabelFields.remove(rightlabels[i]);
				}
			}
		});
		//处理默认数据
		dealDefaultData();
		setControl(container);
	}
	
	private void dealDefaultData(){
		if(treeobj instanceof GridElementObj){
			GridElementObj treeelement = (GridElementObj)treeobj;
			GridTreeLevel toplevel = treeelement.getGridComp().getTopLevel();
			if(toplevel != null){
				id.setText(toplevel.getId());
				dataset.setText(toplevel.getDataset());
				recursiveKeyField.setText(toplevel.getRecursiveKeyField());
				recursivePKeyField.setText(toplevel.getRecursivePKeyField());
				String field = toplevel.getLabelFields();
				String[] fields = field.split(","); //$NON-NLS-1$
				rightlabelFields.setItems(fields);
				labelFields.setSelection(fields);
			}
		}else if(treeobj instanceof GridLevelElementObj){
			GridLevelElementObj treelevelobj = (GridLevelElementObj)treeobj;
			LFWBasicElementObj parent = treelevelobj.getParentTreeLevel();
			while(parent != null && parent instanceof GridLevelElementObj){
				GridLevelElementObj parenttreelvel = (GridLevelElementObj)parent;
				treelevelobj = parenttreelvel;
				parent = parenttreelvel.getParentTreeLevel();
			}
			GridElementObj treeElement = (GridElementObj)parent;
			GridTreeLevel topLevel = treeElement.getGridComp().getTopLevel();
			GridTreeLevel childLevel = topLevel.getChildTreeLevel();
			while(childLevel != null){
				if(childLevel.getDataset().equals(treelevel.getDs().getId())){
					break;
				}else{
					childLevel = childLevel.getChildTreeLevel();
				}
			}
			if(childLevel != null){
				id.setText(childLevel.getId());
				dataset.setText(childLevel.getDataset());
				recursiveKeyField.setText(childLevel.getRecursiveKeyField());
				recursivePKeyField.setText(childLevel.getRecursivePKeyField());
				String field = childLevel.getLabelFields();
				String[] fields = field.split(","); //$NON-NLS-1$
				rightlabelFields.setItems(fields);
				labelFields.setSelection(fields);
			}
		}
	}

}
