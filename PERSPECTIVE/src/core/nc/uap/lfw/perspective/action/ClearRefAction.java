package nc.uap.lfw.perspective.action;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefDataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.dataset.ClearRefDialog;
import nc.uap.lfw.palette.PaletteImage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;


public class ClearRefAction extends Action{
	
	public ClearRefAction() {
		super("清除冗余引用",PaletteImage.getCreateDsImgDescriptor());
	}
	public void run(){
		LfwView widget = LFWPersTool.getCurrentWidget();
		Dataset[] datasets = widget.getViewModels().getDatasets();
		List<String> refDatasetList = new ArrayList<String>();
		List<MdDataset> mdDatasetList = new ArrayList<MdDataset>();
		for(Dataset dataset:datasets){
			if(dataset instanceof MdDataset){
				mdDatasetList.add((MdDataset)dataset);
			}
			else{
				refDatasetList.add(dataset.getId());
			}
		}
		for(MdDataset md:mdDatasetList){
			FieldRelation[] frs = md.getFieldRelations().getFieldRelations();
			for(FieldRelation fr:frs){
				if(refDatasetList.contains(fr.getRefDataset())){
					refDatasetList.remove(fr.getRefDataset());
				}
			}
		}
		
		IRefNode[] refnodes = widget.getViewModels().getRefNodes();
		ComboData[] combos = widget.getViewModels().getComboDatas();
		List<String> refNodeList = new ArrayList<String>();
		List<String> comboList = new ArrayList<String>();
		for(IRefNode ref:refnodes){
			refNodeList.add(ref.getId());
		}
		for(ComboData combo:combos){
			comboList.add(combo.getId());
		}
		FormComp[] forms = widget.getViewComponents().getComponentByType(FormComp.class);
		for(FormComp form:forms){
			List<FormElement> elements = form.getElementList();
			if(elements == null) continue;
			for(FormElement element:elements){
				if(element.getRefNode()!=null&&refNodeList.contains(element.getRefNode()))
					refNodeList.remove(element.getRefNode());
				if(element.getRefComboData()!=null&&comboList.contains(element.getRefComboData()))
					comboList.remove(element.getRefComboData());
			}
		}
		GridComp[] grids = widget.getViewComponents().getComponentByType(GridComp.class);
		for(GridComp grid:grids){
			List<IGridColumn> columns = grid.getColumnList();
			if(columns == null) continue;
			for(IGridColumn icolumn:columns){
				if(icolumn instanceof GridColumn){					
					GridColumn column = (GridColumn)icolumn;
					if(column.getRefNode()!=null&&refNodeList.contains(column.getRefNode()))
						refNodeList.remove(column.getRefNode());
					if(column.getRefComboData()!=null&&comboList.contains(column.getRefComboData()))
						comboList.remove(column.getRefComboData());
				}
			}
		}
		ClearRefDialog dialog = new ClearRefDialog(null, "清除冗余引用");
		dialog.setRefDatasetList(refDatasetList);
		dialog.setRefNodeList(refNodeList);
		dialog.setComboList(comboList);
		dialog.setWidget(widget);
		if(dialog.open() == IDialogConstants.OK_ID){
			LFWPersTool.saveWidget(widget);
		}
//		widget.getViewComponents().getComponentByType(c)
	}

}
