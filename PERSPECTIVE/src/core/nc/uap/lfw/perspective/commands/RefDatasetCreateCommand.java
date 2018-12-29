package nc.uap.lfw.perspective.commands;

import java.util.List;

import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.datasets.core.DatasetsGraph;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.data.Dataset;
//import nc.uap.lfw.excel.ExcelGraph;
import nc.uap.lfw.form.FormGraph;
import nc.uap.lfw.grid.GridGraph;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.listview.ListViewGraph;
import nc.uap.lfw.pagination.PaginationGraph;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDSFromWidget;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * 引用Ds的创建
 * @author zhangxya
 *
 */
public class RefDatasetCreateCommand extends Command{
	
	private LFWBasicElementObj refdsobj = null;
	private boolean canUndo = true;
	private LfwBaseGraph graph = null;
	private Rectangle rect = null;

	public RefDatasetCreateCommand(LFWBasicElementObj refdsobj,LfwBaseGraph graph, Rectangle rect) {
		super();
		this.refdsobj = refdsobj;
		this.graph = graph;
		this.rect = rect;
		setLabel("create new refds"); //$NON-NLS-1$
	}

	
	public boolean canExecute() {
		return refdsobj != null && graph != null && rect != null;
	}

	public void execute() {
		if(graph instanceof DatasetGraph && refdsobj instanceof RefDatasetElementObj){
			DatasetGraph graphnew = (DatasetGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0, true);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			List<RefDatasetElementObj> refdslist = graphnew.getRefds();
			RefDatasetElementObj lastrefds = null;
			for (int i = 0; i < refdslist.size(); i++) {
				if(refdslist.get(i).getParent() == null && refdslist.get(i).getChildren().size() == 0){
					lastrefds = refdslist.get(i);
				}
			}
			DatasetElementObj ds = (DatasetElementObj) graph.getCells().get(0);
			int x = 0;
			int y = 0;
			
			refdsobjnew.setSize(new Dimension(120,120));
			if(refdslist.size() == 0){
				x = 400;
				y = 0;
			}else{
				if(lastrefds == null){
				List<RefDatasetElementObj> refchild = ds.getCells();
				if(refchild.size() > 0)
					lastrefds = (RefDatasetElementObj) refchild.get(refchild.size() - 1);
				}
				x = lastrefds.getLocation().x;
				y = lastrefds.getLocation().y + lastrefds.getSize().height + WEBPersConstants.BETWEEN;
			}
			refdsobjnew.setLocation(new Point(x, y));
			redo();
		}else if(graph instanceof GridGraph && refdsobj instanceof RefDatasetElementObj){
			GridGraph gridgraph = (GridGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			List<LfwElementObjWithGraph> cells = gridgraph.getCellsExceptListener();
			if(cells.size() >= 2){
				MessageDialog.openConfirm(null, M_perspective.RefDatasetCreateCommand_1, M_perspective.RefDatasetCreateCommand_2);
				return;
			}
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0,  false);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350,100));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}else if(graph instanceof ListViewGraph && refdsobj instanceof RefDatasetElementObj){
			ListViewGraph lvgraph = (ListViewGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			List<LfwElementObjWithGraph> cells = lvgraph.getCellsExceptListener();
			if(cells.size() >= 2){
				MessageDialog.openConfirm(null, M_perspective.RefDatasetCreateCommand_1, M_perspective.RefDatasetCreateCommand_3);
				return;
			}
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0,  false);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350,100));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}
		else if(graph instanceof PaginationGraph && refdsobj instanceof RefDatasetElementObj){
			PaginationGraph pagegraph = (PaginationGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			List<LfwElementObjWithGraph> cells = pagegraph.getCellsExceptListener();
			if(cells.size() >= 2){
				MessageDialog.openConfirm(null, M_perspective.RefDatasetCreateCommand_1, M_perspective.RefDatasetCreateCommand_4);
				return;
			}
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0,  false);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350,100));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}else if(graph instanceof FormGraph && refdsobj instanceof RefDatasetElementObj){
			FormGraph formgrph = (FormGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			List<LfwElementObjWithGraph> cells = formgrph.getCellsExceptListener();
			if(cells.size() >= 2){
				MessageDialog.openConfirm(null, M_perspective.RefDatasetCreateCommand_1, M_perspective.RefDatasetCreateCommand_5);
				return;
			}
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0, false);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			refdsobjnew.setLocation(new Point(350,100));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}
		else if (graph instanceof TreeGraph && refdsobj instanceof TreeLevelElementObj){
			TreeGraph treegraph = (TreeGraph)graph;
			TreeLevelElementObj refdsobjnew = (TreeLevelElementObj)refdsobj;
			//从本widget查找ds
			Shell shell = new Shell();
			RefDSFromWidget dialog = new RefDSFromWidget(shell, M_perspective.RefDatasetCreateCommand_0, false);
			int result = dialog.open();
			if (result == IDialogConstants.OK_ID) {
				Dataset ds = dialog.getSelectedDataset();
				if (ds != null) {
					refdsobjnew.setDs(ds);
					refdsobjnew.setId(ds.getId());
				}
			}
			else if(result == IDialogConstants.CANCEL_ID){
				return;
			}
			int size = treegraph.getCellsExceptListener().size();
			int x,y;
			if(size == 1){
				x = 300;
				y = 100;
			}else{
				RefDatasetElementObj lastrefds = (RefDatasetElementObj)treegraph.getCellsExceptListener().get(size - 1);
				x = lastrefds.getLocation().x + lastrefds.getSize().width + WEBPersConstants.BETWEEN;
				y = lastrefds.getLocation().y;
			}
			refdsobjnew.setLocation(new Point(x, y));
			refdsobjnew.setSize(new Dimension(120, 120));
			redo();
		}
//		else if(graph instanceof ExcelGraph && refdsobj instanceof RefDatasetElementObj){
//			ExcelGraph excelgraph = (ExcelGraph)graph;
//			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
//			List<LfwElementObjWithGraph> cells = excelgraph.getCellsExceptListener();
//			if(cells.size() >= 2){
//				MessageDialog.openConfirm(null, "提示", "已经存在一个引用Dataset，一个Excel只能和一个Dataset绑定!");
//				return;
//			}
//			//从本widget查找ds
//			Shell shell = new Shell();
//			RefDSFromWidget dialog = new RefDSFromWidget(shell, "引用数据集",  false);
//			int result = dialog.open();
//			if (result == IDialogConstants.OK_ID) {
//				Dataset ds = dialog.getSelectedDataset();
//				if (ds != null) {
//					refdsobjnew.setDs(ds);
//					refdsobjnew.setId(ds.getId());
//				}
//			}
//			else if(result == IDialogConstants.CANCEL_ID){
//				return;
//			}
//			refdsobjnew.setLocation(new Point(350,100));
//			refdsobjnew.setSize(new Dimension(120, 120));
//			redo();
//		}
	}

	
	public void redo() {
		if(graph instanceof DatasetGraph && refdsobj instanceof RefDatasetElementObj){
			DatasetGraph graphnew = (DatasetGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addRefDs(refdsobjnew);
		}else if(graph instanceof GridGraph && refdsobj instanceof RefDatasetElementObj){
			GridGraph graphnew = (GridGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}else if(graph instanceof ListViewGraph && refdsobj instanceof RefDatasetElementObj){
			ListViewGraph graphnew = (ListViewGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}else if(graph instanceof PaginationGraph && refdsobj instanceof RefDatasetElementObj){
			PaginationGraph graphnew = (PaginationGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}else if(graph instanceof FormGraph && refdsobj instanceof RefDatasetElementObj){
			FormGraph graphnew = (FormGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}
		else if(graph instanceof TreeGraph && refdsobj instanceof RefDatasetElementObj){
			TreeGraph graphnew = (TreeGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.addCell(refdsobjnew);
		}
//		else if(graph instanceof ExcelGraph && refdsobj instanceof RefDatasetElementObj){
//			ExcelGraph excelnew = (ExcelGraph)graph;
//			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
//			excelnew.addCell(refdsobjnew);
//		}
	}

	
	public void undo() {
		if(graph instanceof DatasetGraph && refdsobj instanceof RefDatasetElementObj){
			DatasetGraph graphnew = (DatasetGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.removeRefDs(refdsobjnew);
		}else if(graph instanceof GridGraph && refdsobj instanceof RefDatasetElementObj){
			DatasetsGraph graphnew = (DatasetsGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.removeCell(refdsobjnew);
		}else if(graph instanceof ListViewGraph && refdsobj instanceof RefDatasetElementObj){
			DatasetsGraph graphnew = (DatasetsGraph)graph;
			RefDatasetElementObj refdsobjnew = (RefDatasetElementObj)refdsobj;
			graphnew.removeCell(refdsobjnew);
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
