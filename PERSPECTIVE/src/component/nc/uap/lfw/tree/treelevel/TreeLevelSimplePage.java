package nc.uap.lfw.tree.treelevel;

import java.util.ArrayList;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.core.comp.SimpleTreeLevel;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

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
 * 简单树页面
 * @author zhangxya
 *
 */
public class TreeLevelSimplePage extends WizardPage{

	protected TreeLevelSimplePage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}
	
	public IWizardPage getNextPage() {
		return null;
	}

	private TreeLevelElementObj treelevel = null;
	public TreeLevelElementObj getTreelevel() {
		return treelevel;
	}

	public void setTreelevel(TreeLevelElementObj treelevel) {
		this.treelevel = treelevel;
	}

	private LFWBasicElementObj treeobj;
	public LFWBasicElementObj getTreeobj() {
		return treeobj;
	}

	public void setTreeobj(LFWBasicElementObj treeobj) {
		this.treeobj = treeobj;
	}

	public TreeLevelSimplePage(String pageName, LFWBasicElementObj treeobj, TreeLevelElementObj treelevel, String flag){
		super(pageName);
		setTitle(M_tree.TreeLevelSimplePage_0);
		this.treeobj = treeobj;
		this.treelevel = treelevel;
		this.flag =flag;
		
	}
	
	protected IDialogSettings getDialogSettings() {
		// TODO Auto-generated method stub
		return super.getDialogSettings();
	}

	
	private Combo treeType = null;
	private String flag;
	
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

	public Combo getMasterKeyField() {
		return masterKeyField;
	}

	public void setMasterKeyField(Combo masterKeyField) {
		this.masterKeyField = masterKeyField;
	}

	public List getLabelFields() {
		return labelFields;
	}

	public void setLabelFields(List labelFields) {
		this.labelFields = labelFields;
	}

	public Text getLabelDelims() {
		return labelDelims;
	}

	public void setLabelDelims(Text labelDelims) {
		this.labelDelims = labelDelims;
	}

	public Text getId() {
		return id;
	}

	public void setId(Text id) {
		this.id = id;
	}

	public Text getCodeField() {
		return codeField;
	}

	public void setCodeField(Text codeField) {
		this.codeField = codeField;
	}

	public Text getCodeRule() {
		return codeRule;
	}

	public void setCodeRule(Text codeRule) {
		this.codeRule = codeRule;
	}

	private Text dataset = null;
	private Combo recursiveKeyField = null;
	private Combo recursivePKeyField = null;
	private Combo masterKeyField = null;
	private List labelFields = null;
	private List rightlabelFields = null;
	public List getRightlabelFields() {
		return rightlabelFields;
	}

	public void setRightlabelFields(List rightlabelFields) {
		this.rightlabelFields = rightlabelFields;
	}

	private Text labelDelims = null;
	private Text id = null;
	private Text codeField = null;
	private Text codeRule = null;
	private Text detailKeyParameter = null;
	

	public Text getDetailKeyParameter() {
		return detailKeyParameter;
	}

	public void setDetailKeyParameter(Text detailKeyParameter) {
		this.detailKeyParameter = detailKeyParameter;
	}

	public void createControl(Composite parent) {
		setTitle(M_tree.TreeLevelSimplePage_1); 
		setMessage(M_tree.TreeLevelSimplePage_1);
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_BOTH));
		grouId.setLayout(new GridLayout(2,false));
	
		
		Label labelId = new Label(grouId, SWT.NONE);
		labelId.setText(M_tree.TreeLevelSimplePage_3);
		id = new Text(grouId, SWT.BORDER);
		id.setLayoutData(new GridData(140,15));
		id.setText(M_tree.TreeLevelSimplePage_4);
		
		Label DatasetLabel = new Label(grouId, SWT.NONE);
		DatasetLabel.setText(M_tree.TreeLevelSimplePage_5);
		
		dataset = new Text(grouId, SWT.BORDER);
		dataset.setLayoutData(new GridData(140,15));
		dataset.setText(treelevel.getDs().getId());
		dataset.setEditable(false);
		
		Field[] fields = treelevel.getDs().getFieldSet().getFields();
		java.util.List <String> list = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++) {
			String fieldId = fields[i].getId();
			if(fieldId != null)
				list.add(fieldId);
		}
		Label masterKeyFieldLabel = new Label(grouId, SWT.NONE);
		masterKeyFieldLabel.setText(M_tree.TreeLevelSimplePage_6);
		masterKeyField = new Combo(grouId, SWT.READ_ONLY);
		masterKeyField.setLayoutData(new GridData(120,15));
		masterKeyField.setItems((String[])list.toArray(new String[list.size()]));
		masterKeyField.select(0);
		
		Label detailKeyParameterLabel = new Label(grouId, SWT.NONE);
		detailKeyParameterLabel.setText(M_tree.TreeLevelSimplePage_7);
		detailKeyParameter = new Text(grouId, SWT.BORDER);
		detailKeyParameter.setLayoutData(new GridData(140,15));
		String primarykey = ""; //$NON-NLS-1$
		for (int i = 0; i < fields.length; i++) {
			Field fi = fields[i];
			if(fi.isPrimaryKey()){
				primarykey = fi.getId();
				break;
			}
		}
		detailKeyParameter.setText(primarykey);
		if(flag != null && flag.equals("Y")) //$NON-NLS-1$
			detailKeyParameter.setEditable(false);
		//detailKeyParameter.setItems((String[])list.toArray(new String[list.size()]));
		
		Label labelFieldsLabel = new Label(grouId, SWT.NONE);
		labelFieldsLabel.setText(M_tree.TreeLevelSimplePage_10);
		
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
		//rightlabelFields.setItems(new String[0]);
		//rightlabelFields.setItems((String[])list.toArray(new String[list.size()]));
		//labelFields.select(0);
		
		Label labelDelimsLabel = new Label(grouId, SWT.NONE);
		labelDelimsLabel.setText(M_tree.TreeLevelSimplePage_11);
		labelDelims = new Text(grouId, SWT.BORDER);
		labelDelims.setLayoutData(new GridData(140,15));
		//处理默认数据
		dealDefaultData();
		setControl(container);
	}
	
	private void dealDefaultData(){
		if(treeobj instanceof TreeElementObj){
			TreeElementObj treeelement = (TreeElementObj)treeobj;
			TreeLevel toplevel = treeelement.getTreeComp().getTopLevel();
			if(toplevel != null && toplevel instanceof SimpleTreeLevel){
				SimpleTreeLevel sim = (SimpleTreeLevel)toplevel;
				id.setText(sim.getId());
				dataset.setText(sim.getDataset());
				masterKeyField.setText(sim.getMasterKeyField());
				String field = sim.getLabelFields();
				String[] fields = field.split(","); //$NON-NLS-1$
				rightlabelFields.setItems(fields);
				labelFields.setSelection(fields);
				String lims = sim.getLabelDelims();
				if(lims == null)
					lims = ""; //$NON-NLS-1$
				labelDelims.setText(lims);
			}
		}else if(treeobj instanceof TreeLevelElementObj){
			TreeLevelElementObj treelevelobj = (TreeLevelElementObj)treeobj;
			LFWBasicElementObj parent = treelevelobj.getParentTreeLevel();
			while(parent != null && parent instanceof TreeLevelElementObj){
				TreeLevelElementObj parenttreelvel = (TreeLevelElementObj)parent;
				treelevelobj = parenttreelvel;
				parent = parenttreelvel.getParentTreeLevel();
			}
			TreeElementObj treeElement = (TreeElementObj)parent;
			TreeLevel topLevel = treeElement.getTreeComp().getTopLevel();
			TreeLevel childLevel = topLevel.getChildTreeLevel();
			while(childLevel != null){
				if(childLevel.getDataset().equals(treelevel.getDs().getId())){
					break;
				}
				else{
					childLevel = childLevel.getChildTreeLevel();
				}
			}
			if(childLevel != null && childLevel instanceof SimpleTreeLevel){
				SimpleTreeLevel sim = (SimpleTreeLevel)childLevel;
				id.setText(sim.getId());
				dataset.setText(sim.getDataset());
				masterKeyField.setText(sim.getMasterKeyField());
				detailKeyParameter.setText(sim.getDetailKeyParameter());
				String field = sim.getLabelFields();
				String[] fields = field.split(","); //$NON-NLS-1$
				rightlabelFields.setItems(fields);
				labelFields.setSelection(fields);
				String lims = sim.getLabelDelims();
				if(lims == null)
					lims = ""; //$NON-NLS-1$
				labelDelims.setText(lims);
			}
		}
	
	}

}
