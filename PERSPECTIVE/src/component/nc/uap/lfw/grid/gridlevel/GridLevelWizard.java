/**
 * 
 */
package nc.uap.lfw.grid.gridlevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.GridTreeLevel;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.core.GridEditor;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.lang.M_grid;
import nc.uap.lfw.perspective.project.LFWExplorerTreeView;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

/**
 * @author chouhl
 * 2011-12-15
 */
public class GridLevelWizard extends Wizard {

	private GridLevelRecusivePage treeLevelRecusivePage;
	public static final String TREELEVEL_RECUSIVE = M_grid.GridLevelWizard_0;
	private String id = null;
	private String dataset = null;
	private String[] labelFields =null;
	private String labelField = ""; //$NON-NLS-1$
	private String recursiveKeyField = null;;
	private String recursivePKeyField = null;
	private GridElementObj treeobjnew  = null;
	private List<String> levelIds = new ArrayList<String>();
	
	private class ModifyTreeLevelCommand extends Command{
		public ModifyTreeLevelCommand(){
			super(M_grid.GridLevelWizard_2);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}
	private GridLevelElementObj refds;
	private LFWBasicElementObj treeobj;
	private String flag ;
	
	public LFWBasicElementObj getTreeobj() {
		return treeobj;
	}
	public void setTreeobj(LFWBasicElementObj treeobj) {
		this.treeobj = treeobj;
	}
	public GridLevelWizard(LFWBasicElementObj treeobj, GridLevelElementObj refds, String flag){
		super();
		setWindowTitle(M_grid.GridLevelWizard_3);
		this.refds = refds;
		this.treeobj = treeobj;
		this.flag = flag;
	}
	
	private GridLevelFirstPage treeLevelFirstPage;
	
	public GridLevelFirstPage getTreeLevelFirstPage() {
		return treeLevelFirstPage;
	}
	public void setTreeLevelFirstPage(GridLevelFirstPage treeLevelFirstPage) {
		this.treeLevelFirstPage = treeLevelFirstPage;
	}
	public GridLevelRecusivePage getTreeLevelRecusivePage() {
		return treeLevelRecusivePage;
	}
	public void setTreeLevelRecusivePage(GridLevelRecusivePage treeLevelRecusivePage) {
		this.treeLevelRecusivePage = treeLevelRecusivePage;
	}

	public void addPages() {
		treeLevelFirstPage = new GridLevelFirstPage(M_grid.GridLevelWizard_4,treeobj,refds);
		treeLevelRecusivePage = new GridLevelRecusivePage(M_grid.GridLevelWizard_5, treeobj, refds, flag);
		addPage(treeLevelFirstPage);
		addPage(treeLevelRecusivePage);
	}
	
	public boolean canFinish() {
		if(this.getContainer().getCurrentPage() == treeLevelRecusivePage)
			return true;
		else 
			return false;
	}

	public boolean performFinish() {
		boolean type2 = treeLevelFirstPage.getTreelevelTypeRe().getSelection();
		LFWExplorerTreeView view = LFWExplorerTreeView.getLFWExploerTreeView(null);
		if(view != null){
			if(treeobj instanceof GridElementObj)
				 treeobjnew = (GridElementObj)treeobj;
			else if(treeobj instanceof GridLevelElementObj){
				GridLevelElementObj treelevelEle = (GridLevelElementObj)treeobj;
				LFWBasicElementObj parent = treelevelEle.getParentTreeLevel();
				while(parent != null && !(parent instanceof GridElementObj)){
					 GridLevelElementObj parentnew = (GridLevelElementObj)parent;
					 parent = parentnew.getParentTreeLevel();
				}
				treeobjnew = (GridElementObj)parent;
			}
				
			GridComp treeViewcomp = treeobjnew.getGridComp();
			GridTreeLevel topLevel = treeViewcomp.getTopLevel();
			if(type2){
				id = treeLevelRecusivePage.getId().getText();
				treeobjnew.setId(id);
				dataset = treeLevelRecusivePage.getDataset().getText();
				labelFields = treeLevelRecusivePage.getRightlabelFields().getItems();
				labelField = ""; //$NON-NLS-1$
				for (int i = 0; i < labelFields.length; i++) {
					if(i != labelFields.length - 1)
						labelField += labelFields[i] + ","; //$NON-NLS-1$
					else
						labelField += labelFields[i];
				}
				recursiveKeyField = treeLevelRecusivePage.getRecursiveKeyField().getText();;
				recursivePKeyField = treeLevelRecusivePage.getRecursivePKeyField().getText();
				if(topLevel == null){
					GridTreeLevel topLevelre = new GridTreeLevel();
					topLevelre.setId(id);
					topLevelre.setDataset(dataset);
					topLevelre.setRecursiveKeyField(recursiveKeyField);
					topLevelre.setRecursivePKeyField(recursivePKeyField);
					topLevelre.setLabelFields(labelField);
					treeViewcomp.setTopLevel(topLevelre);
				}
				else{
					//是否正在修改此TopLevel
					if(topLevel.getDataset().equals(refds.getDs().getId())){
						GridTreeLevel childLev = topLevel.getChildTreeLevel();
						treeViewcomp.setTopLevel(null);
						GridTreeLevel topLevelre = new GridTreeLevel();
						topLevelre.setChildTreeLevel(childLev);
						topLevelre.setId(id);
						topLevelre.setDataset(dataset);
						topLevelre.setRecursiveKeyField(recursiveKeyField);
						topLevelre.setRecursivePKeyField(recursivePKeyField);
						topLevelre.setLabelFields(labelField);
						treeViewcomp.setTopLevel(topLevelre);
					}else{
						if(!topLevel.getDataset().equals(refds.getId()))
							levelIds.add(topLevel.getId());
						GridTreeLevel childLevel = topLevel.getChildTreeLevel();
						GridTreeLevel originalLevel = null;
						while(childLevel != null){
							if(childLevel.getDataset().equals(refds.getDs().getId())){
								GridTreeLevel childchiLevel = childLevel.getChildTreeLevel();
								if(originalLevel != null){
									originalLevel.setChildTreeLevel(null);
								}else{
									topLevel.setChildTreeLevel(null);
								}
								GridTreeLevel chidlevelre = new GridTreeLevel();
								chidlevelre.setId(id);
								chidlevelre.setDataset(dataset);
								chidlevelre.setLabelFields(labelField);
								chidlevelre.setRecursiveKeyField(recursiveKeyField);
								chidlevelre.setRecursivePKeyField(recursivePKeyField);
								chidlevelre.setChildTreeLevel(childchiLevel);
								if(originalLevel != null){
									originalLevel.setChildTreeLevel(chidlevelre);
								}else{
									topLevel.setChildTreeLevel(chidlevelre);
								}
								break;
							}
							else{
								//如果不是当前打开的TreeLevel,则将dsid保存进去
								if(!childLevel.getDataset().equals(refds.getId()))
									levelIds.add(childLevel.getId());
								originalLevel = childLevel;
								childLevel = childLevel.getChildTreeLevel();
							}
						}
						
						if(childLevel == null){
							if(levelIds.contains(id)){
								MessageDialog.openError(null, M_grid.GridLevelWizard_8, M_grid.GridLevelWizard_9);
								return false;
							}
							GridTreeLevel chidlevelre = new GridTreeLevel();
							chidlevelre.setId(id);
							chidlevelre.setDataset(dataset);
							chidlevelre.setLabelFields(labelField);
							chidlevelre.setRecursiveKeyField(recursiveKeyField);
							chidlevelre.setRecursivePKeyField(recursivePKeyField);
							if(originalLevel != null){
								originalLevel.setChildTreeLevel(chidlevelre);
							}else{
								topLevel.setChildTreeLevel(chidlevelre);
							}
						}
						treeViewcomp.setTopLevel(topLevel);
						
						refds.setGridTreelevel(childLevel);
					}
				}
			}
			LfwView lfwwidget = LFWPersTool.getLfwWidget();
			Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
			map.put(treeViewcomp.getId(), treeViewcomp);
			LFWPersTool.setLfwWidget(lfwwidget);
			ModifyTreeLevelCommand cmd = new ModifyTreeLevelCommand();
			if(GridEditor.getActiveEditor() != null)
				GridEditor.getActiveEditor().executComand(cmd);
			return true;
		}
		return false;
	}

}
