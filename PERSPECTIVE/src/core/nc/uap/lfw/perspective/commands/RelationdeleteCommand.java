package nc.uap.lfw.perspective.commands;
import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.datasets.core.DsRelationConnection;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.RefNodeRelation;
//import nc.uap.lfw.excel.DatasetToExcelConnection;
//import nc.uap.lfw.excel.ExcelElementObj;
import nc.uap.lfw.form.DatasetToFormConnection;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.listview.DatasetToListViewConnection;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.pagination.DatasetToPaginationConnection;
import nc.uap.lfw.pagination.PaginationElementObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.refnoderel.RefNodeRelConnection;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 *各种关系的删除操作
 * @author zhangxya
 *
 */
public class RelationdeleteCommand extends Command {
	private Connection conn = null;
	public RelationdeleteCommand(Connection relation){
		super();
		this.conn = relation;
		setLabel("delete relation"); //$NON-NLS-1$
	}
	
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}
	
	public void execute() {
		redo();
	}
	
	public void redo() {
		if((conn.getSource() instanceof DatasetElementObj && conn.getTarget() instanceof RefDatasetElementObj)|| conn instanceof ChildConnection){
			MessageDialog.openInformation(null, M_perspective.RelationdeleteCommand_0, M_perspective.RelationdeleteCommand_1);
			return;
		}
		conn.disConnect();
		dealData();
	}
	
	public void undo() {
		conn.connect();
	}

	private void dealChildRelation(FieldRelation fr,RefDatasetElementObj target){
		List<FieldRelation> childrelaiton = fr.getChildRelationList();
		for (int i = 0; i < childrelaiton.size(); i++) {
			if(childrelaiton.get(i).getRefDataset().equals(target.getDs().getId())){
				fr.getChildRelationList().remove(childrelaiton.get(i));
				return;
			}
			else
				dealChildRelation(childrelaiton.get(i), target);
		}
	}
	
	private void dealData(){
		if(conn instanceof RefNodeRelConnection){
			LfwView widget = LFWPersTool.getCurrentWidget();
			DatasetFieldElementObj source = (DatasetFieldElementObj) conn.getSource();
			RefNodeElementObj target = (RefNodeElementObj) conn.getTarget();
			String refRelId = target.getRefRelationId();
			RefNodeRelation refNodeRelation = widget.getViewModels().getRefNodeRelations().getRefNodeRelation(refRelId);
			if(refNodeRelation != null){
				refNodeRelation.removeMasterFieldInfo(source.getDsId(), source.getField().getId());
			}
		}
		if(conn instanceof TreeLevelChildConnection){
			if(conn.getSource() instanceof TreeLevelElementObj){
				TreeLevelElementObj source = (TreeLevelElementObj)conn.getSource();
				TreeLevelElementObj treelevelobj = (TreeLevelElementObj)conn.getTarget();
				//treeGraph.removeCell(treelevelobj);
				LFWBasicElementObj parent = source.getParentTreeLevel();
				while(parent != null && !(parent instanceof TreeElementObj)){
					TreeLevelElementObj treelevelparent = (TreeLevelElementObj)parent;
					parent = treelevelparent.getParentTreeLevel();
				}
				TreeElementObj treeEle = (TreeElementObj)parent;
				TreeViewComp treeViewcomp = treeEle.getTreeComp();
				TreeLevel topLevel = treeViewcomp.getTopLevel();
	//			if(topLevel.getId().equals(treelevelobj.getId()))
	//				treeViewcomp.setTopLevel(null);
	//			else{
					TreeLevel childLevel = topLevel.getChildTreeLevel();
					TreeLevel parentLevel = topLevel;
					while(!childLevel.getId().equals(treelevelobj.getId())){
						childLevel = parentLevel.getChildTreeLevel();
						parentLevel = childLevel;
					}
					parentLevel.setChildTreeLevel(null);
				//}
				treeViewcomp.setTopLevel(topLevel);
			}
		}else if(conn instanceof DatasetToFormConnection){
			FormElementObj source = (FormElementObj)conn.getSource();
			//RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
			FormComp formcomp = source.getFormComp();
			formcomp.setElementList(new ArrayList<FormElement>());
			formcomp.setDataset(null);
	
		}else if(conn instanceof DatasetToGridConnection){
			GridElementObj source = (GridElementObj)conn.getSource();
			//RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
			GridComp gridcomp = source.getGridComp();
			gridcomp.setColumnList(new ArrayList<IGridColumn>());
			gridcomp.setDataset(null);
		}		
		else if(conn instanceof DatasetToListViewConnection){
			ListViewElementObj source = (ListViewElementObj)conn.getSource();
			//RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
			ListViewComp lvcomp = source.getlistviewComp();
			lvcomp.setDataset(null);
		}
		else if(conn instanceof DatasetToPaginationConnection){
			PaginationElementObj source = (PaginationElementObj)conn.getSource();
			//RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
			PaginationComp pagecomp = source.getPaginationComp();
			pagecomp.setDataset(null);
		}
//		else if(conn instanceof DatasetToExcelConnection){
//			ExcelElementObj source = (ExcelElementObj)conn.getSource();
//			//RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
//			ExcelComp excelcomp = source.getExcelComp();
//			excelcomp.setColumnList(new ArrayList<IExcelColumn>());
//			excelcomp.setDataset(null);
//		}
		else if (conn instanceof TreeTopLevelConnection){
			if(conn.getSource() instanceof TreeElementObj){
				TreeElementObj source = (TreeElementObj)conn.getSource();
				//TreeLevelElementObj target = (TreeLevelElementObj)conn.getTarget();
				TreeViewComp treeViewcomp = source.getTreeComp();
				//TreeLevel topLevel = treeViewcomp.getTopLevel();
				treeViewcomp.setTopLevel(null);
			}else if(conn.getSource() instanceof GridElementObj){
				GridElementObj source = (GridElementObj)conn.getSource();
				GridComp comp = source.getGridComp();
				comp.setTopLevel(null);
			}
		}
		//refds间的连接
		else if(conn instanceof ChildConnection){
//			return;
			//RefDatasetElementObj source = (RefDatasetElementObj)conn.getSource();
			RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();

			RefDatasetElementObj refparent = target.getParent();
			if(refparent != null){
				RefDatasetElementObj refpp = refparent.getParent();
				while(refpp != null){
					refparent = refpp;
					refpp = refpp.getParent();
				}
			}
			
			DatasetElementObj parentds = refparent.getDsobj();
			FieldRelations frs = parentds.getDs().getFieldRelations();
			//FieldRelation finalRelation = null;
			if(frs != null){
				FieldRelation[] frss = frs.getFieldRelations();
				for (int i = 0; i < frss.length; i++) {
					FieldRelation fr = frss[i];
					dealChildRelation(fr, target);
				}
			}
			parentds.getDs().setFieldRelations(frs);
		}
		//ds间的连接
		else if(conn instanceof DsRelationConnection){
			DatasetElementObj source = (DatasetElementObj)conn.getSource();
			DatasetElementObj target = (DatasetElementObj)conn.getTarget();
			LfwView lfwwidget = LFWPersTool.getLfwWidget();
			if(lfwwidget != null){
				DatasetRelations dsrelations = lfwwidget.getViewModels().getDsrelations();
				if(dsrelations != null){
					DatasetRelation oldDsRelation = dsrelations.getDsRelation(source.getDs().getId(), target.getDs().getId());
					if(oldDsRelation != null){
						dsrelations.removeDsRelation(oldDsRelation);
						lfwwidget.getViewModels().setDsrelations(dsrelations);
					}
				}
				LFWPersTool.setLfwWidget(lfwwidget);
			}
			
		}
		//ds/refds之间的连接
		else if(conn.getSource() instanceof DatasetElementObj){
			DatasetElementObj source = (DatasetElementObj)conn.getSource();
			if(source.getDs().getFrom() != null){
				undo();
				MessageDialog.openInformation(null, M_perspective.RelationdeleteCommand_0, M_perspective.RelationdeleteCommand_2);
				return;
			}
			RefDatasetElementObj target = (RefDatasetElementObj)conn.getTarget();
			DatasetGraph dsgraph = (DatasetGraph) source.getGraph();
			dsgraph.removeRefDs(target);
			FieldRelations fieldsr = source.getDs().getFieldRelations();
			if(fieldsr != null){
				FieldRelation[] frs = fieldsr.getFieldRelations();
				for (int i = 0; i < frs.length; i++) {
					FieldRelation fr = (FieldRelation) frs[i];
					if(fr.getRefDataset().equals(target.getDs().getId())){
						fieldsr.removeFieldRelation(fr.getId());
						source.deleteFieldRelation(fr);
						//target.deleteFieldRelation(fr);
						break;
					}
				}
				source.getDs().setFieldRelations(fieldsr);
			}
			
		}
	}
}
