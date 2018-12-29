package nc.uap.lfw.perspective.action;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.dataset.ChooseFrDialog;
import nc.uap.lfw.dataset.MDDatsetDialog;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.palette.PaletteImage;
import nc.uap.lfw.parts.LFWElementPart;
import nc.uap.lfw.perspective.editor.DataSetEditor;
import nc.uap.lfw.perspective.model.DatasetElementFigure;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetGraph;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.perspective.views.CellPropertiesView;
import nc.uap.lfw.perspective.views.LFWViewPage;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.WorkbenchPage;


/**
 * 从元数据中引入ds
 * 
 * @author zhangxya
 *
 */
public class NewDsFormMDAction extends Action{
	
	private DatasetElementObj dsobj = null;
	private LfwView widget = null;
	
	private class AddDSCommand extends Command{
		public AddDSCommand(Dataset ds){
			super(M_perspective.NewDsFormMDAction_0);
		}
		
		public void execute() {
			redo();
		}

		public void redo() {
		}
		
		public void undo() {
		}
	}
	
	public NewDsFormMDAction(DatasetElementObj dsobj) {
		super(M_perspective.NewDsFormMDAction_1,PaletteImage.getCreateDsImgDescriptor());
		this.dsobj = dsobj;
	}

	/**
	 * 清空原来的信息
	 */
	private void clearOriginalData(){
		Dataset oringinalDs = dsobj.getDs();
		//清空原来的refnode
		if(widget == null)
			widget = LFWPersTool.getCurrentWidget();
		//清空原来的FieldRelation的图标
		if(widget != null){
			FieldRelation[] frs = oringinalDs.getFieldRelations().getFieldRelations();
			if(frs != null && frs.length > 0){
				for (int i = 0; i < frs.length; i++) {
					FieldRelation fr = frs[i];
					dsobj.deleteFieldRelation(fr);
				}
				List<RefDatasetElementObj> refdss = dsobj.getCells();
					if(refdss != null && refdss.size() > 0){
					int size = refdss.size();
					DatasetGraph dsgraph = (DatasetGraph) dsobj.getGraph();
					for (int i = 0; i < size; i++) {
						RefDatasetElementObj refds = refdss.get(i);
						if(dsgraph.removeRefDs(refds)){
							//dsobj.removeCell(refdss.get(i));
						}
					}
				}
				List<Connection> conList = dsobj.getGraph().getConnections();
				if(conList != null && conList.size() > 0){
					for (int i = 0; i < conList.size(); i++) {
						Connection con = conList.get(i);
						con.disConnect();
					}
				}
			}
			IRefNode[] orginalRefNodes = widget.getViewModels().getRefNodes();
			if(orginalRefNodes != null && orginalRefNodes.length > 0){
				for (int i = 0; i < orginalRefNodes.length; i++) {
					IRefNode refnode = orginalRefNodes[i];
					if(refnode.getId().startsWith("refNode_" + oringinalDs.getId())) //$NON-NLS-1$
						widget.getViewModels().removeRefNode(refnode.getId());
				}
			}
			//情况原来的combo
			ComboData[] comboDatas = widget.getViewModels().getComboDatas();
			if(comboDatas != null && comboDatas.length > 0){
				for (int i = 0; i < comboDatas.length; i++) {
					ComboData com = comboDatas[i];
					if(com.getId().startsWith("comboComp_" + oringinalDs.getId())) //$NON-NLS-1$
						widget.getViewModels().removeComboData(com.getId());
				}
			}
		}
	}
	
	public void run() {
		Shell shell = new Shell();
		MDDatsetDialog dialog = new MDDatsetDialog(shell, M_perspective.NewDsFormMDAction_2);
		int result = dialog.open();
		if (result == IDialogConstants.OK_ID){
			//将原来ds中的所有fieldRelation及关联refds删除
			clearOriginalData();
			String originaldsId = dsobj.getDs().getId();
			List<EventConf> eventList = dsobj.getDs().getEventConfList();
			Dataset refds = dialog.getDataset();
			MdDataset mdds  = (MdDataset)refds;
			mdds.setId(originaldsId);
			mdds.setEventConfList(eventList);
//			dsobj.setDs(mdds);
			Field[] pubfields = (Field[])mdds.getFieldSet().getFields();
			if(mdds.getId()==null)
				mdds.setId(mdds.getCaption());
			dsobj.setDs(mdds);
			LFWViewPage lfwviewpage = LFWPersTool.getLFWViewPage();
			TreeViewer tv = ((CellPropertiesView) lfwviewpage.getCellPropertiesView()).getTv();
			if(pubfields == null){
				MessageDialog.openError(shell, M_perspective.NewDsFormMDAction_3, M_perspective.NewDsFormMDAction_4);
				return;
			}
			tv.refresh();
			tv.expandAll();
			boolean flag = MessageDialog.openConfirm(null, M_perspective.NewDsFormMDAction_5, M_perspective.NewDsFormMDAction_6);
			if(flag){
				dsobj.setExtendFlag(true);
				//得到所有参照ds
				putAllRefDs(mdds);
				//得到所有的refNode
				putAllRefNodeDs(mdds);
				//得到所有的combodata
				putAllComboData(mdds);
			}
			else{
				ChooseFrDialog chooseFr = new ChooseFrDialog(shell, "请选择需要引入的fieldRelation");
				chooseFr.setDs(mdds);
				if(chooseFr.open()==IDialogConstants.OK_ID){
//					mdds.getfi
					List<RefMdDataset> refdsAllList = LFWConnector.getNCRefMdDataset(mdds);
					List<RefMdDataset> refdsList = new ArrayList<RefMdDataset>();
					for(RefMdDataset ref:refdsAllList){
						for(FieldRelation fr:mdds.getFieldRelations().getFieldRelations()){
							if(fr.getRefDatasetid().equals(ref.getId()))
								refdsList.add(ref);
						}
					}
					widget = LFWPersTool.getCurrentWidget();
					if(widget != null){
						ViewModels viewModels = widget.getViewModels();
						if(refdsList != null && refdsList.size() > 0){
							for (int i = 0; i < refdsList.size(); i++) {
								RefMdDataset refmdDs = (RefMdDataset) refdsList.get(i);
								RefMdDataset refmdDsold = (RefMdDataset) viewModels.getDataset(refmdDs.getId());
								if(refmdDsold != null)
									viewModels.removeDataset(refmdDs.getId());
//								if(mdds.getFieldRelations().getf)
								viewModels.addDataset(refmdDs);
							}
							
							int  parenty = 0;
							//加入所有的refds
							for (int i = 0; i < refdsList.size(); i++) {
								RefDatasetElementObj refdsobj = new RefDatasetElementObj();
								Dataset ref = (Dataset)refdsList.get(i);
								refdsobj.setDs(ref);
								int pointy = 0;
								refdsobj.setSize(new Dimension(100,100));
								if(i == 0)
									pointy = 0;
								else 
									pointy = parenty + 100 + WEBPersConstants.BETWEEN;
								parenty = pointy;
								Point point = new Point(400, pointy);
								refdsobj.setLocation(point);
								if(dsobj.getGraph() instanceof DatasetGraph){
									DatasetGraph dsgraph = (DatasetGraph) dsobj.getGraph();
									dsgraph.addRefDs(refdsobj);
									dsobj.setGraph(dsgraph);
								}
								dsobj.addRefDataset(refdsobj);
								Connection con = new Connection(dsobj, refdsobj);
								con.connect();
								dsobj.getGraph().addConns(con);
								
							}
						}
						
						//处理所有的FieldRelation
						FieldRelation[] frs = mdds.getFieldRelations().getFieldRelations();
						if(frs != null && frs.length > 0){
							List<RefDatasetElementObj> refdss = dsobj.getCells();
							int size = refdss.size();
							List<RefDatasetElementObj> copyrefds = new ArrayList<RefDatasetElementObj>();
							for (int i = 0; i < refdss.size(); i++) {
								copyrefds.add(refdss.get(i));
							}
							if(frs != null){
								for (int j = 0; j < frs.length; j++) {
									FieldRelation frr = frs[j];
									dsobj.addFieldRelation(frr);
									size = refdss.size();
									for (int i = 0; i < size; i++) {
										RefDatasetElementObj refdsobj = (RefDatasetElementObj) refdss.get(i);
										if(frr.getRefDataset().equals(refdsobj.getDs().getId())){
											refdsobj.addFieldRelation(frr);
//											refdss.remove(refds);
											break;
										}
									}
								}
							}
						}
						//得到ds编辑器
						DataSetEditor dsEditor = DataSetEditor.getActiveEditor();
						//重新调整Listener位置
//						if(dsEditor != null)
//							dsEditor.repaintListenerPositon();
					}
					putAllRefNodeDs(mdds);
					putAllComboData(mdds);
				}
			}
			
			AddDSCommand cmd = new AddDSCommand(refds);
			if(DataSetEditor.getActiveEditor() != null){
				DataSetEditor editor = DataSetEditor.getActiveEditor();				
				editor.executComand(cmd);
				StructuredSelection selection = (StructuredSelection)editor.getCurrentSelection();
				DatasetElementFigure figure = (DatasetElementFigure)((LFWElementPart)selection.getFirstElement()).getFigure();
//				figure.getTitleFigure().removeAll();
//				figure.setTypeLab("数据集[" + dsobj.getDs().getCaption() + "]");
//				figure.setTitleLab(dsobj.getDs().getId(), dsobj.getDs().getId());
				IWorkbenchPage page = MainPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
				editor.getViewPage().selectionChanged(page.getActivePart(), editor.getCurrentSelection());
				figure.setTypeLab("数据集[" + dsobj.getDs().getCaption() + "]");
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void putAllRefNodeDs(MdDataset mdds){
		List<RefNode> refnodeList = LFWConnector.getAllNCRefNode(mdds);
		ViewModels viewModels = widget.getViewModels();
		for (int i = 0; i < refnodeList.size(); i++) {
			RefNode refNode = (RefNode) refnodeList.get(i);
			IRefNode refNodeold = viewModels.getRefNode(refNode.getId());
			if(refNodeold != null){
				viewModels.removeRefNode(refNodeold.getId());
			}
			viewModels.addRefNode(refNode);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void putAllComboData(MdDataset mdds){
		List<ComboData> combodataList = LFWConnector.getAllNcComboData(mdds);
		ViewModels viewModels = widget.getViewModels();
		for (int i = 0; i < combodataList.size(); i++) {
			ComboData combo = (ComboData) combodataList.get(i);
			ComboData comboold = viewModels.getComboData(combo.getId());
			if(comboold != null){
				viewModels.removeComboData(combo.getId());
			}
			viewModels.addComboData(combo);
		}
	}
	
	
	//得到所有的refds
	@SuppressWarnings("unchecked")
	private void putAllRefDs(MdDataset mdds){
		//获取所有的Fieldrelation
		List<FieldRelation> fieldRelations = LFWConnector.getNCFieldRelations(mdds);
		for (int i = 0; i < fieldRelations.size(); i++) {
			FieldRelation fr = (FieldRelation) fieldRelations.get(i);
			mdds.getFieldRelations().addFieldRelation(fr);
		}
		List refdsList = LFWConnector.getNCRefMdDataset(mdds);
		widget = LFWPersTool.getCurrentWidget();
		if(widget != null){
			ViewModels viewModels = widget.getViewModels();
			if(refdsList != null && refdsList.size() > 0){
				for (int i = 0; i < refdsList.size(); i++) {
					RefMdDataset refmdDs = (RefMdDataset) refdsList.get(i);
					RefMdDataset refmdDsold = (RefMdDataset) viewModels.getDataset(refmdDs.getId());
					if(refmdDsold != null)
						viewModels.removeDataset(refmdDs.getId());
					viewModels.addDataset(refmdDs);
				}
				
				int  parenty = 0;
				//加入所有的refds
				for (int i = 0; i < refdsList.size(); i++) {
					RefDatasetElementObj refdsobj = new RefDatasetElementObj();
					Dataset ref = (Dataset)refdsList.get(i);
					refdsobj.setDs(ref);
					int pointy = 0;
					refdsobj.setSize(new Dimension(100,100));
					if(i == 0)
						pointy = 0;
					else 
						pointy = parenty + 100 + WEBPersConstants.BETWEEN;
					parenty = pointy;
					Point point = new Point(400, pointy);
					refdsobj.setLocation(point);
					if(dsobj.getGraph() instanceof DatasetGraph){
						DatasetGraph dsgraph = (DatasetGraph) dsobj.getGraph();
						dsgraph.addRefDs(refdsobj);
						dsobj.setGraph(dsgraph);
					}
					dsobj.addRefDataset(refdsobj);
					Connection con = new Connection(dsobj, refdsobj);
					con.connect();
					dsobj.getGraph().addConns(con);
					
				}
			}
			
			//处理所有的FieldRelation
			FieldRelation[] frs = mdds.getFieldRelations().getFieldRelations();
			if(frs != null && frs.length > 0){
				List<RefDatasetElementObj> refdss = dsobj.getCells();
				int size = refdss.size();
				List<RefDatasetElementObj> copyrefds = new ArrayList<RefDatasetElementObj>();
				for (int i = 0; i < refdss.size(); i++) {
					copyrefds.add(refdss.get(i));
				}
				if(frs != null){
					for (int j = 0; j < frs.length; j++) {
						FieldRelation frr = frs[j];
						dsobj.addFieldRelation(frr);
						size = refdss.size();
						for (int i = 0; i < size; i++) {
							RefDatasetElementObj refds = (RefDatasetElementObj) refdss.get(i);
							if(frr.getRefDataset().equals(refds.getDs().getId())){
								refds.addFieldRelation(frr);
//								refdss.remove(refds);
								break;
							}
						}
					}
				}
			}
			//得到ds编辑器
			DataSetEditor dsEditor = DataSetEditor.getActiveEditor();
			//重新调整Listener位置
//			if(dsEditor != null)
//				dsEditor.repaintListenerPositon();
		}
	}
	
	//循环解析FieldRelation
		private void dealFieldRelation(RefDatasetElementObj refdsobj, FieldRelation fieldrelation){
			List<FieldRelation>  childrelationlist = fieldrelation.getChildRelationList();
			if(childrelationlist != null){
				for (int j = 0; j < childrelationlist.size(); j++) {
					Point refpoint = refdsobj.getLocation();
					Dimension refsize = refdsobj.getSize();
					FieldRelation childrefrelation = childrelationlist.get(j);
					String childrefid = childrefrelation.getRefDatasetid();
					Dataset childrefds = widget.getViewModels().getDataset(childrefid);
					RefDatasetElementObj childrefobj = new RefDatasetElementObj();
					childrefobj.setDs(childrefds);
					refdsobj.addChild(childrefobj);
					childrefobj.setSize(new Dimension(100,100));
					childrefobj.setLocation(new Point((refpoint.x + refsize.width + 100),(refpoint.y + 200*j)));
					DatasetGraph dsgraph = (DatasetGraph) dsobj.getGraph();
					dsgraph.addRefDs(childrefobj);
					ChildConnection childcon = new ChildConnection(refdsobj, childrefobj);
					childcon.connect();
					List<FieldRelation> chdchachildfrs = childrefrelation.getChildRelationList();
					if(chdchachildfrs != null && chdchachildfrs.size() > 0)
						dealFieldRelation(refdsobj, childrefrelation);
				}
			}
		}
}
