/**
 * 
 */
package nc.uap.lfw.grid.gridlevel;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.lang.M_grid;

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
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelFirstPage extends WizardPage {

	public IWizardPage getNextPage() {
		boolean recurseTreeLevel = getTreelevelTypeRe().getSelection();
		GridLevelWizard wizard = (GridLevelWizard)getWizard();
		if(recurseTreeLevel){
			return wizard.getTreeLevelRecusivePage();
		}
		return super.getNextPage();
	}

	private Label treeTypeLabel;
	private Button treelevelTypeRe;
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
	
	private LFWBasicElementObj treeobj;
	
	public LFWBasicElementObj getTreeobj() {
		return treeobj;
	}

	public void setTreeobj(LFWBasicElementObj treeobj) {
		this.treeobj = treeobj;
	}
	
	private GridLevelElementObj gridlevel;
	protected GridLevelFirstPage(String pageName, LFWBasicElementObj treeobj,GridLevelElementObj gridlevel) {
		super(pageName);
		this.treeobj = treeobj;
		this.gridlevel = gridlevel;
	}

	public void createControl(Composite parent) {
		setTitle(M_grid.GridLevelFirstPage_0); 
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1,false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(1,false));
		treeTypeLabel = new Label(grouId, SWT.NONE);
		treeTypeLabel.setText(M_grid.GridLevelFirstPage_1);

		treelevelTypeRe = new Button(grouId, SWT.RADIO);
		treelevelTypeRe.setText(M_grid.GridLevelFirstPage_2);	

		dealDefaultData();
		setControl(container);
	}
	
	private void dealDefaultData(){
		if(treeobj instanceof GridElementObj){
			GridElementObj treeelement = (GridElementObj)treeobj;
			GridTreeLevel toplevel = treeelement.getGridComp().getTopLevel();
			if(toplevel != null){
				treelevelTypeRe.setSelection(true);
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
				if(childLevel.getDataset().equals(gridlevel.getDs().getId())){
					break;
				}
				else{
					childLevel = childLevel.getChildTreeLevel();
				}
			}
			if(childLevel != null){
				treelevelTypeRe.setSelection(true);
			}
		}
	}

}
