package nc.uap.lfw.perspective.commands;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.TreeLevel;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.FieldRelations;
//import nc.uap.lfw.excel.ExcelElementObj;
//import nc.uap.lfw.excel.ExcelGraph;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.listview.ListViewGraph;
import nc.uap.lfw.pagination.PaginationElementObj;
import nc.uap.lfw.pagination.PaginationGraph;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 参照DS 删除命令
 * @author zhangxya
 *
 */
public class RefDatasetDeleteCommand extends Command{
	private LFWBasicElementObj lfwobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	private List<Connection> sourceConnections = null;
	private List<Connection> targetConnections = null;
	public RefDatasetDeleteCommand(LFWBasicElementObj lfwobj,LfwBaseGraph graph) {
		super();
		this.lfwobj = lfwobj;
		this.graph = graph;
		setLabel("delete refds"); //$NON-NLS-1$
	}

	
	public boolean canExecute() {
		return super.canExecute();
	}

	
	public void execute() {
		sourceConnections = lfwobj.getSourceConnections();
		targetConnections = lfwobj.getTargetConnections();
		redo();
	}

	
	public void redo() {
		if(graph instanceof DatasetGraph && lfwobj instanceof RefDatasetElementObj){
			DatasetGraph dsgraph = (DatasetGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			RefDatasetElementObj refparent = refdsobj.getParent();
			if(refdsobj.getChildren()!=null&&refdsobj.getChildren().size()>0){
				MessageDialog.openInformation(null, M_perspective.RefDatasetDeleteCommand_0, M_perspective.RefDatasetDeleteCommand_1);
				return;
			}
			if(refparent != null){
				if(dsgraph.removeRefDs(refdsobj)){
				deleteFieldRelation(refparent, refdsobj);
				refparent.removeChild(refdsobj);
				}
			}
			else {
				DatasetElementObj dsobj = refdsobj.getDsobj();
				if(dsobj != null){
					if(dsobj.getDs().getFrom() != null){
						MessageDialog.openInformation(null, M_perspective.RefDatasetDeleteCommand_0, M_perspective.RefDatasetDeleteCommand_2);
						return;
					}
					
					if(dsgraph.removeRefDs(refdsobj)){
					dsobj.removeCell(refdsobj);
					deleteFieldRelation(dsobj, refdsobj);
					}
				}
			}
		}else if(graph instanceof GridGraph && lfwobj instanceof RefDatasetElementObj){
			GridGraph gridgraph = (GridGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			gridgraph.removeCell(refdsobj);
			GridElementObj gridobj = (GridElementObj)gridgraph.getCells().get(0);
			GridComp gridcomp = gridobj.getGridComp();
			gridcomp.setColumnList(new ArrayList<IGridColumn>());
			gridcomp.setDataset(null);
//		}else if(graph instanceof ExcelGraph && lfwobj instanceof RefDatasetElementObj){
//			ExcelGraph excelgraph = (ExcelGraph)graph;
//			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
//			excelgraph.removeCell(refdsobj);
//			ExcelElementObj excelobj = (ExcelElementObj)excelgraph.getCells().get(0);
//			ExcelComp excelcomp = excelobj.getExcelComp();
//			excelcomp.setColumnList(new ArrayList<IExcelColumn>());
//			excelcomp.setDataset(null);
		}else if(graph instanceof ListViewGraph && lfwobj instanceof RefDatasetElementObj){
			ListViewGraph lvgraph = (ListViewGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			lvgraph.removeCell(refdsobj);
			ListViewElementObj lvobj = (ListViewElementObj)lvgraph.getCells().get(0);
			ListViewComp lvcomp = lvobj.getlistviewComp();
			lvcomp.setDataset(null);
		}else if(graph instanceof PaginationGraph && lfwobj instanceof RefDatasetElementObj){
			PaginationGraph pagegraph = (PaginationGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			pagegraph.removeCell(refdsobj);
			PaginationElementObj lvobj = (PaginationElementObj)pagegraph.getCells().get(0);
			PaginationComp pagecomp = lvobj.getPaginationComp();
			pagecomp.setDataset(null);
		}else if(graph instanceof FormGraph && lfwobj instanceof RefDatasetElementObj){
			FormGraph formgraph = (FormGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			formgraph.removeCell(refdsobj);
			FormElementObj formobj = (FormElementObj)formgraph.getCells().get(0);
			FormComp formcomp = formobj.getFormComp();
			formcomp.setElementList(new ArrayList<FormElement>());
			formcomp.setDataset(null);
		}
		else if(graph instanceof TreeGraph && lfwobj instanceof TreeLevelElementObj){
			TreeGraph treeGraph = (TreeGraph)graph;
			TreeLevelElementObj treelevelobj = (TreeLevelElementObj)lfwobj;
			treeGraph.removeCell(treelevelobj);
			LFWBasicElementObj parent = treelevelobj.getParentTreeLevel();
			while(parent != null && !(parent instanceof TreeElementObj)){
				TreeLevelElementObj treelevelparent = (TreeLevelElementObj)parent;
				parent = treelevelparent.getParentTreeLevel();
			}
			TreeElementObj treeEle = (TreeElementObj)parent;
			TreeViewComp treeViewcomp = treeEle.getTreeComp();
			TreeLevel topLevel = treeViewcomp.getTopLevel();
			if(topLevel.getId().equals(treelevelobj.getId()))
				treeViewcomp.setTopLevel(null);
			else{
				TreeLevel childLevel = topLevel.getChildTreeLevel();
				TreeLevel parentLevel = topLevel;
				while(!childLevel.getId().equals(treelevelobj.getId())){
					childLevel = parentLevel.getChildTreeLevel();
					parentLevel = childLevel;
				}
				parentLevel.setChildTreeLevel(null);
				treeViewcomp.setTopLevel(topLevel);
			}
			
		}
		removeConnections(sourceConnections);
		removeConnections(targetConnections);
	}
	
	
	private void deleteFieldRelation(LFWBasicElementObj source, LFWBasicElementObj target){
		//删除dsfieldRelation
		if(source instanceof DatasetElementObj){
			DatasetElementObj sourcenew = (DatasetElementObj)source;
			RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
			FieldRelations fieldsr = sourcenew.getDs().getFieldRelations();
			if(fieldsr != null){
				FieldRelation[] frs = fieldsr.getFieldRelations();
				for (int i = 0; i < frs.length; i++) {
					FieldRelation fr = (FieldRelation) frs[i];
					if(fr.getRefDataset().equals(targetnew.getDs().getId())){
						fieldsr.removeFieldRelation(fr.getId());
						sourcenew.deleteFieldRelation(fr);
						break;
					}
				}
				sourcenew.getDs().setFieldRelations(fieldsr);
			}
		}
		//如果为refds，删除refds 之间的子fieldrelation
		else if(source instanceof RefDatasetElementObj){
			RefDatasetElementObj sourcenew = (RefDatasetElementObj)source;
			RefDatasetElementObj targetnew = (RefDatasetElementObj)target;

			//RefDatasetElementObj refparent = targetnew.getParent();
			if(sourcenew != null){
				RefDatasetElementObj refpp = sourcenew.getParent();
				while(refpp != null){
					sourcenew = refpp;
					refpp = refpp.getParent();
				}
			}
			
			DatasetElementObj parentds = sourcenew.getDsobj();
			FieldRelations frs = parentds.getDs().getFieldRelations();
			//FieldRelation finalRelation = null;
			if(frs != null){
				FieldRelation[] frss = frs.getFieldRelations();
				for (int i = 0; i < frss.length; i++) {
					FieldRelation fr = frss[i];
					if(fr.getRefDatasetid().equals(targetnew.getDs().getId())){
						frs.removeFieldRelation(fr.getId());
					}
//					dealChildRelation(fr, targetnew);
				}
			}
			parentds.getDs().setFieldRelations(frs);
		}
		
	}
	
	private void dealChildRelation(FieldRelation fr,RefDatasetElementObj target){
		List<FieldRelation> childrelaiton = fr.getChildRelationList();
		if(childrelaiton != null){
			for (int i = 0; i < childrelaiton.size(); i++) {
				if(childrelaiton.get(i).getRefDataset().equals(target.getDs().getId())){
					fr.getChildRelationList().remove(childrelaiton.get(i));
					return;
				}
				else
					dealChildRelation(childrelaiton.get(i), target);
			}
		}
	}
	
	private void removeConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.disConnect();
		}
	}
	
	private void addConnections(List<Connection> conn){
		int count = conn.size();
		for (int i = 0; i < count; i++) {
			Connection relation = conn.get(i);
			relation.connect();
		}
	}

	
	public void undo() {
		if(graph instanceof DatasetGraph && lfwobj instanceof RefDatasetElementObj){
			DatasetGraph dsgraph = (DatasetGraph)graph;
			RefDatasetElementObj refdsobj = (RefDatasetElementObj)lfwobj;
			if(dsgraph.addRefDs(refdsobj)){
				addConnections(sourceConnections);
				addConnections(targetConnections);
			}
		}
	}

	public boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(boolean canUndo) {
		this.canUndo = canUndo;
	}

	
	public boolean canUndo() {
		return isCanUndo();
	}

}
