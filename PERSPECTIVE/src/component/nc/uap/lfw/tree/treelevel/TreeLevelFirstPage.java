package nc.uap.lfw.tree.treelevel;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.core.comp.CodeTreeLevel;
import nc.uap.lfw.core.comp.RecursiveTreeLevel;
import nc.uap.lfw.core.comp.SimpleTreeLevel;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.lang.M_tree;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;


/**
 * treelevel 第一个页面
 * @author zhangxya
 *
 */

public class TreeLevelFirstPage extends WizardPage{

	
	public IWizardPage getNextPage() {
		boolean simpleTreeLevel = getTreelevelTypeSi().getSelection();
		boolean recurseTreeLevel = getTreelevelTypeRe().getSelection();
		boolean codeTreeLevel = getTreelevelTypeCode().getSelection();
		TreeLevelWizard wizard = (TreeLevelWizard)getWizard();
		if(simpleTreeLevel){
			return wizard.getTreeLevelSimplePage();
		}else if(recurseTreeLevel){
			return wizard.getTreeLevelRecusivePage();
		}
		else if(codeTreeLevel)
			return wizard.getTreeLevelCodeTreePage();
		return super.getNextPage();
	}


	private Label treeTypeLabel;
	private Button treelevelTypeRe;
	private Button treelevelTypeSi;
	private Button treelevelTypeCode;
	public Label getTreeTypeLabel() {
		return treeTypeLabel;
	}

	public void setTreeTypeLabel(Label treeTypeLabel) {
		this.treeTypeLabel = treeTypeLabel;
	}

	public Button getTreelevelTypeRe() {
		return treelevelTypeRe;
	}

	public void setTreelevelTypeRe(Button treelevelTypeRe) {
		this.treelevelTypeRe = treelevelTypeRe;
	}

	public Button getTreelevelTypeSi() {
		return treelevelTypeSi;
	}

	public void setTreelevelTypeSi(Button treelevelTypeSi) {
		this.treelevelTypeSi = treelevelTypeSi;
	}

	public Button getTreelevelTypeCode() {
		return treelevelTypeCode;
	}

	public void setTreelevelTypeCode(Button treelevelTypeCode) {
		this.treelevelTypeCode = treelevelTypeCode;
	}

	
	private LFWBasicElementObj treeobj;
	
	public LFWBasicElementObj getTreeobj() {
		return treeobj;
	}

	public void setTreeobj(LFWBasicElementObj treeobj) {
		this.treeobj = treeobj;
	}

	private TreeLevelElementObj treelevel;
	protected TreeLevelFirstPage(String pageName, LFWBasicElementObj treeobj,TreeLevelElementObj treelvel) {
		super(pageName);
		this.treeobj = treeobj;
		this.treelevel = treelvel;
	}

	public void createControl(Composite parent) {
		setTitle(M_tree.TreeLevelFirstPage_0); 
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(1,false));
		treeTypeLabel = new Label(grouId, SWT.NONE);
		treeTypeLabel.setText(M_tree.TreeLevelFirstPage_1);
		treelevelTypeSi = new Button(grouId, SWT.RADIO);
		treelevelTypeSi.setText(M_tree.TreeLevelFirstPage_2);
		treelevelTypeSi.setSelection(true);
		treelevelTypeRe = new Button(grouId, SWT.RADIO);
		treelevelTypeRe.setText(M_tree.TreeLevelFirstPage_3);	
		treelevelTypeCode = new Button(grouId, SWT.RADIO);
		treelevelTypeCode.setText(M_tree.TreeLevelFirstPage_4);
		dealDefaultData();
		setControl(container);
	}
	
	private void dealDefaultData(){
		if(treeobj instanceof TreeElementObj){
			TreeElementObj treeelement = (TreeElementObj)treeobj;
			TreeLevel toplevel = treeelement.getTreeComp().getTopLevel();
			if(toplevel != null){
				if(toplevel instanceof SimpleTreeLevel){
					treelevelTypeSi.setSelection(true);
				}
				else if(toplevel instanceof CodeTreeLevel){
					treelevelTypeSi.setSelection(false);
					treelevelTypeCode.setSelection(true);
				}
				else if(toplevel instanceof RecursiveTreeLevel){
					treelevelTypeSi.setSelection(false);
					treelevelTypeRe.setSelection(true);
				}
			}
		}
		else if(treeobj instanceof TreeLevelElementObj){
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
				treelevelTypeSi.setSelection(true);
			}
			if(childLevel != null && childLevel instanceof CodeTreeLevel){
				treelevelTypeSi.setSelection(false);
				treelevelTypeCode.setSelection(true);
			}
			if(childLevel != null && childLevel instanceof RecursiveTreeLevel){
				treelevelTypeSi.setSelection(false);
				treelevelTypeRe.setSelection(true);
			}
		}
	}

}
