package nc.uap.lfw.perspective.commands;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.Connection;
import nc.lfw.editor.common.LFWBasicElementObj;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.editor.contextmenubar.ContextMenuElementObj;
import nc.lfw.editor.datasets.core.DsRelationConnection;
import nc.lfw.editor.pagemeta.PlugRelationDialog;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.RenderTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
//import nc.uap.lfw.core.comp.ExcelColumn;
//import nc.uap.lfw.core.comp.ExcelComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IExcelColumn;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.ListViewComp;
import nc.uap.lfw.core.comp.PaginationComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
//import nc.uap.lfw.excel.DatasetToExcelConnection;
//import nc.uap.lfw.excel.ExcelElementObj;
import nc.uap.lfw.form.DatasetToFormConnection;
import nc.uap.lfw.form.FormElementObj;
import nc.uap.lfw.grid.DatasetToGridConnection;
import nc.uap.lfw.grid.GridElementObj;
import nc.uap.lfw.grid.GridToContextMenuConnection;
import nc.uap.lfw.grid.core.GridLevelElementObj;
import nc.uap.lfw.grid.gridlevel.GridLevelWizard;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.listview.ListViewElementObj;
import nc.uap.lfw.listview.DatasetToListViewConnection;
import nc.uap.lfw.pagination.DatasetToPaginationConnection;
import nc.uap.lfw.pagination.PaginationElementObj;
import nc.uap.lfw.palette.ChildConnection;
import nc.uap.lfw.perspective.model.DatasetElementObj;
import nc.uap.lfw.perspective.model.DatasetRelationDialog;
import nc.uap.lfw.perspective.model.DsRelationSetDialog;
import nc.uap.lfw.perspective.model.RefDatasetElementObj;
import nc.uap.lfw.refnode.RefNodeElementObj;
import nc.uap.lfw.refnoderel.DatasetFieldElementObj;
import nc.uap.lfw.refnoderel.RefNodeRelConnection;
import nc.uap.lfw.refnoderel.RefNodeRelationDialog;
import nc.uap.lfw.tree.TreeElementObj;
import nc.uap.lfw.tree.TreeTopLevelConnection;
import nc.uap.lfw.tree.core.TreeLevelElementObj;
import nc.uap.lfw.tree.treelevel.TreeLevelChildConnection;
import nc.uap.lfw.tree.treelevel.TreeLevelWizard;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * 组件间关系的创建
 * @author zhangxya
 *
 */
public class LFWConnectionCommand extends Command{
   protected LFWBasicElementObj source;
    protected LFWBasicElementObj target;
    private Connection conn = null;
  	private Constructor constructor = null;
  	private Class conCls = null;
 
  	public LFWConnectionCommand(LFWBasicElementObj refdsobj,Class conCls){
    	super();
		this.source = refdsobj;
		this.conCls = conCls;
		try {
			constructor = conCls.getConstructor(new Class[]{LFWBasicElementObj.class, LFWBasicElementObj.class});
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
		setLabel("create new cell"); //$NON-NLS-1$
	}
    
    public boolean canExecute() {
		//枚举只能和注释建立连接
		if( (source instanceof RefDatasetElementObj && target instanceof RefDatasetElementObj) && ChildConnection.class.equals(conCls)){
			return true;
		}else	if(source instanceof DatasetElementObj && target instanceof RefDatasetElementObj && Connection.class.equals(conCls))
			return true;
		else if(source instanceof DatasetElementObj && target instanceof DatasetElementObj && DsRelationConnection.class.equals(conCls))
			return true;
		else if(source instanceof GridElementObj && target instanceof RefDatasetElementObj && DatasetToGridConnection.class.equals(conCls))
			return true;
		else if(source instanceof ListViewElementObj && target instanceof RefDatasetElementObj && DatasetToListViewConnection.class.equals(conCls))
			return true;
		else if(source instanceof PaginationElementObj && target instanceof RefDatasetElementObj && DatasetToPaginationConnection.class.equals(conCls))
			return true;
		else if(source instanceof TreeElementObj && target instanceof TreeLevelElementObj && TreeTopLevelConnection.class.equals(conCls))
			return true;
		else if(source instanceof TreeLevelElementObj && target instanceof TreeLevelElementObj && TreeLevelChildConnection.class.equals(conCls))
			return true;
		else if(source instanceof FormElementObj && target instanceof RefDatasetElementObj && DatasetToFormConnection.class.equals(conCls))
			return true;
		else if(source instanceof GridElementObj && target instanceof ContextMenuElementObj && GridToContextMenuConnection.class.equals(conCls))
			return true;
		else if(source instanceof DatasetFieldElementObj && target instanceof RefNodeElementObj && RefNodeRelConnection.class.equals(conCls))
			return true;
//		else if(source instanceof ExcelElementObj && target instanceof RefDatasetElementObj && DatasetToExcelConnection.class.equals(conCls))
//			return true;
		else if(source instanceof PlugoutDescElementObj && target instanceof PluginDescElementObj && Connection.class.equals(conCls))
			return true;
		else if(source instanceof GridElementObj && target instanceof GridLevelElementObj && TreeTopLevelConnection.class.equals(conCls))
			return true;
		else if(source instanceof GridLevelElementObj && target instanceof GridLevelElementObj && TreeLevelChildConnection.class.equals(conCls))
			return true;
		return false;
	}
    

    void setSource(LFWBasicElementObj source) {
        this.source = source;
    }

   

    public void setTarget(LFWBasicElementObj target) {
        this.target = target;
    }

    

    public String getLabel() {
        return "Create Connection"; //$NON-NLS-1$
    }

    
	public void redo() {
    	if(source instanceof RefDatasetElementObj && target instanceof RefDatasetElementObj && ChildConnection.class.equals(conCls)){
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		RefDatasetElementObj sourcenew = (RefDatasetElementObj)source;
    		for(FieldRelation relation:sourcenew.getDsobj().getDs().getFieldRelations().getFieldRelations()){
    			if(targetnew.getDs().getId().equals(relation.getRefDatasetid())){
    				MainPlugin.getDefault().logError(M_perspective.LFWConnectionCommand_0);
    				return;
    			}
    		}
    		repaint(sourcenew, targetnew);
       	}
    	else if(source instanceof DatasetElementObj && target instanceof RefDatasetElementObj){
     		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		DatasetElementObj sourcenew = (DatasetElementObj)source;
    		for(FieldRelation relation:sourcenew.getDs().getFieldRelations().getFieldRelations()){
    			if(targetnew.getDs().getId().equals(relation.getRefDatasetid())){
    				MainPlugin.getDefault().logError(M_perspective.LFWConnectionCommand_0);
    				return;
    			}
    		}
    		repaint(sourcenew, targetnew);
    		sourcenew.addRefDataset(targetnew);
    	}
    	else if(source instanceof DatasetElementObj && target instanceof DatasetElementObj){
    		if(source == target){
    			MessageDialog.openError(null, "错误", "不允许对自身建立关联");
    			return;
    		}
    		if(target.getSourceConnections()!=null&&target.getSourceConnections().size()>0){
    			for(Connection conn:target.getSourceConnections()){
    				if(conn.getTarget()==source){
    					MessageDialog.openError(null, "错误", "不允许相互关联");
    					return;
    				}
    			}
    		}
//    		DatasetElementObj sournew = (DatasetElementObj)source;
//    		DatasetElementObj targetnew = (DatasetElementObj)target;
    	}else if(source instanceof GridElementObj && target instanceof RefDatasetElementObj){
    		GridElementObj sourcenew = (GridElementObj)source;
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		sourcenew.setDs(targetnew.getDs());
    		
    	}else if(source instanceof ListViewElementObj && target instanceof RefDatasetElementObj){
    		ListViewElementObj sourcenew = (ListViewElementObj)source;
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		sourcenew.setDs(targetnew.getDs());
    	}else if(source instanceof PaginationElementObj && target instanceof RefDatasetElementObj){
    		PaginationElementObj sourcenew = (PaginationElementObj)source;
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		sourcenew.setDs(targetnew.getDs());
    	}else if(source instanceof FormElementObj && target instanceof RefDatasetElementObj){
    		FormElementObj sourcenew = (FormElementObj)source;
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		sourcenew.setDs(targetnew.getDs());
    	}
    	else if(source instanceof TreeElementObj && target instanceof TreeLevelElementObj){
    		TreeElementObj sourcenew = (TreeElementObj)source;
    		TreeLevelElementObj targetnew = (TreeLevelElementObj)target;
    		sourcenew.setDs(targetnew.getDs());
    	}else if(source instanceof TreeLevelElementObj && target instanceof TreeLevelElementObj){
//    		TreeLevelElementObj sourcenew = (TreeLevelElementObj)source;
//    		TreeLevelElementObj targetnew = (TreeLevelElementObj)target;
    	}
//    	else if(source instanceof ExcelElementObj && target instanceof RefDatasetElementObj){
//    		ExcelElementObj sourcenew = (ExcelElementObj)source;
//    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
//    		sourcenew.setDs(targetnew.getDs());
//    	}
    	else if(source instanceof PlugoutDescElementObj && target instanceof PluginDescElementObj){
    		PlugoutDescElementObj sourcenew = (PlugoutDescElementObj)source;
    		PluginDescElementObj targetnew = (PluginDescElementObj)target;
    	}
    	conn.connect();
    	createConnection(source, target);
    }
    
    private void createConnection(LFWBasicElementObj source, LFWBasicElementObj target){
  		if(source instanceof DatasetElementObj && target instanceof DatasetElementObj){
  			DatasetElementObj sourcenew = (DatasetElementObj)source;
  			DatasetElementObj targetnew = (DatasetElementObj)target;
  			Field[] leftfields = sourcenew.getDs().getFieldSet().getFields();
    		if(leftfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_2);
				conn.disConnect();
				return;
			}
    		Field[] rightfields = targetnew.getDs().getFieldSet().getFields();
    		if(rightfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_3);
				conn.disConnect();
				return;
			}
    		DatasetRelationDialog datasetReDialog = new DatasetRelationDialog(null, sourcenew, targetnew);
    		datasetReDialog.open();
    	}
    	else if(source instanceof DatasetElementObj && target instanceof RefDatasetElementObj){
    		DatasetElementObj sourcenew = (DatasetElementObj)source;
    		Field[] leftfields = sourcenew.getDs().getFieldSet().getFields();
    		if(leftfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_4);
				conn.disConnect();
				return;
			}
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		Field[] rightfields = targetnew.getDs().getFieldSet().getFields();
    		if(rightfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_5);
				conn.disConnect();
				return;
			}
    		for(FieldRelation relation:sourcenew.getDs().getFieldRelations().getFieldRelations()){
    			if(targetnew.getDs().getId().equals(relation.getRefDatasetid())){
    				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_0);
    				conn.disConnect();
    				return;
    			}
    		}
	    	DsRelationSetDialog dsRelation = new DsRelationSetDialog(null,M_perspective.LFWConnectionCommand_6,  source, target);
			dsRelation.open();
    	}else if(source instanceof RefDatasetElementObj && target instanceof RefDatasetElementObj){
    		RefDatasetElementObj sourcenew = (RefDatasetElementObj)source;
    		Field[] leftfields = sourcenew.getDs().getFieldSet().getFields();
    		if(leftfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_4);
				conn.disConnect();
				return;
			}
    		RefDatasetElementObj targetnew = (RefDatasetElementObj)target;
    		Field[] rightfields = targetnew.getDs().getFieldSet().getFields();
    		if(rightfields == null){
				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_5);
				conn.disConnect();
				return;
			}
    		for(FieldRelation relation:sourcenew.getDsobj().getDs().getFieldRelations().getFieldRelations()){
    			if(targetnew.getDs().getId().equals(relation.getRefDatasetid())){
    				MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1 , M_perspective.LFWConnectionCommand_0);
    				conn.disConnect();
    				return;
    			}
    		}
	    	DsRelationSetDialog dsRelation = new DsRelationSetDialog(null, M_perspective.LFWConnectionCommand_6,  source, target);
			dsRelation.open();
    	}
    	else if(source instanceof TreeElementObj && target instanceof TreeLevelElementObj){
    		TreeLevelElementObj targetnew = (TreeLevelElementObj)target;
    		TreeElementObj sourcenew = (TreeElementObj)source;
    		if(targetnew.getDs().getFieldSet().getFields() == null){
    			MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1, M_perspective.LFWConnectionCommand_7);
    			conn.disConnect();
    			return;
    		}
    		targetnew.setParentTreeLevel(sourcenew);
    		WizardDialog treeLevelDialog = new WizardDialog(null, new TreeLevelWizard(sourcenew,targetnew, "Y")); //$NON-NLS-1$
     		treeLevelDialog.open();
    	}else if(source instanceof GridElementObj && target instanceof GridLevelElementObj){
    		GridLevelElementObj targetnew = (GridLevelElementObj)target;
    		GridElementObj sourcenew = (GridElementObj)source;
    		if(targetnew.getDs().getFieldSet().getFields() == null){
    			MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1, M_perspective.LFWConnectionCommand_7);
    			conn.disConnect();
    			return;
    		}
    		targetnew.setParentTreeLevel(sourcenew);
    		WizardDialog treeLevelDialog = new WizardDialog(null, new GridLevelWizard(sourcenew,targetnew, "Y")); //$NON-NLS-1$
     		treeLevelDialog.open();
    	}
    	else if(source instanceof DatasetFieldElementObj && target instanceof RefNodeElementObj){
    		RefNodeElementObj targetnew = (RefNodeElementObj) target;
    		DatasetFieldElementObj sourcenew = (DatasetFieldElementObj) source;
    		RefNodeRelationDialog dialog = new RefNodeRelationDialog(null, M_perspective.LFWConnectionCommand_8, sourcenew, targetnew);
    		dialog.open();
    	}
    	else if(source instanceof TreeLevelElementObj && target instanceof TreeLevelElementObj){
    		TreeLevelElementObj targetnew = (TreeLevelElementObj)target;
    		TreeLevelElementObj sourcenew = (TreeLevelElementObj)source;
    		if(targetnew.getDs().getFieldSet().getFields() == null){
    			MessageDialog.openError(null, M_perspective.LFWConnectionCommand_1, M_perspective.LFWConnectionCommand_7);
    			conn.disConnect();
    			return;
    		}
    		targetnew.setParentTreeLevel(sourcenew);
    		WizardDialog treeLevelDialog = new WizardDialog(null, new TreeLevelWizard(sourcenew,targetnew, null));
     		treeLevelDialog.open();
    	}else if(source instanceof GridLevelElementObj && target instanceof GridLevelElementObj){
    		GridLevelElementObj targetnew = (GridLevelElementObj)target;
    		GridLevelElementObj sourcenew = (GridLevelElementObj)source;
    		if(targetnew.getDs().getFieldSet().getFields() == null){
    			MessageDialog.openError(null, "错误提示", M_perspective.LFWConnectionCommand_7); //$NON-NLS-1$
    			conn.disConnect();
    			return;
    		}
    		targetnew.setParentTreeLevel(sourcenew);
    		WizardDialog treeLevelDialog = new WizardDialog(null, new GridLevelWizard(sourcenew,targetnew, null));
     		treeLevelDialog.open();
    	}
  		//grid与ds建立连接后将ds中字段加入到grid中
    	else if(source instanceof GridElementObj && target instanceof RefDatasetElementObj){
    		GridElementObj gridobj = (GridElementObj)source;
    		GridComp gridcomp = gridobj.getGridComp();
    		List<String> notVisibleFields = new ArrayList<String>();
    		List<IGridColumn> griddatas = gridcomp.getColumnList();
    		if(griddatas != null)
    			griddatas.clear();
    		else 
    			griddatas = new ArrayList<IGridColumn>();
    		RefDatasetElementObj dsobj = (RefDatasetElementObj)target;
    		gridcomp.setDataset(dsobj.getDs().getId());
    		gridcomp.setCaption(dsobj.getDs().getCaption());
    		Field[] fields = dsobj.getDs().getFieldSet().getFields();
    		if(fields == null)
    			return;
    		List<String> fieldList = new ArrayList<String>();
    		for (int i = 0; i < fields.length; i++) {
				fieldList.add(fields[i].getId());
			}
    		LfwView lfwwidget = LFWPersTool.getLfwWidget();
    		for (int i = 0; i < fields.length; i++) {
				if(fieldList.contains(fields[i].getId() + "_mc")) //$NON-NLS-1$
					continue;	
				if(fields[i].getId().equals("dr") || fields[i].getId().equals("ts")) //$NON-NLS-1$ //$NON-NLS-2$
					continue;
				ExtAttribute hide = fields[i].getExtendAttribute(Field.HIDE);
				if(hide!=null){
					boolean isHidden = (Boolean)hide.getValue();
					if(isHidden)
						continue;
				}
				GridColumn column = new GridColumn();
				column.setId(fields[i].getId());
				column.setField(fields[i].getId());
				column.setText(fields[i].getText());
				column.setI18nName(fields[i].getI18nName());
				column.setLangDir(fields[i].getLangDir());
				//datatype
				column.setDataType(fields[i].getDataType());
				column.setPrecision(fields[i].getPrecision());
				//editorType,rendertype
				String editorType = null;
				String renderType = null;
				String sourceField = fields[i].getSourceField();
				notVisibleFields.add(sourceField);
				if(fields[i].isPrimaryKey())
					notVisibleFields.add(fields[i].getId());
				if(sourceField != null){
					String refNodeFlag = CompIdGenerator.generateRefCompId(dsobj.getDs().getId() , fields[i].getId());
					IRefNode refnode = lfwwidget.getViewModels().getRefNode(refNodeFlag);
					if(refnode != null){
						editorType = EditorTypeConst.REFERENCE;
						renderType = RenderTypeConst.getRenderTypeByString(fields[i].getDataType());
						column.setRefNode(refNodeFlag);
					}
				}
				if(editorType == null) {
					String comboDataFlag = CompIdGenerator.generateComboCompId(dsobj.getDs().getId(), fields[i].getId());
					//Map<String, ComboData> comboDataMap = lfwwidget.getViewModels().getComboDataMap();
					ComboData combo = lfwwidget.getViewModels().getComboData(comboDataFlag);
					if(combo != null){
						editorType = EditorTypeConst.COMBODATA;
						renderType = RenderTypeConst.ComboRender;
						column.setRefComboData(comboDataFlag);
					}
					else {
						editorType = EditorTypeConst.getEditorTypeByString(fields[i].getDataType());
						renderType = RenderTypeConst.getRenderTypeByString(fields[i].getDataType());
					}
				}
			
				if(editorType == null)
					editorType = EditorTypeConst.STRINGTEXT;
				column.setEditorType(editorType);
				column.setRenderType(renderType);
				
				// 根据数据类型设置textAlign
				String textAlign = ""; //$NON-NLS-1$
				if(fields[i].getDataType() != null){
					if(fields[i].getDataType().equals(StringDataTypeConst.bOOLEAN) || fields[i].getDataType().equals(StringDataTypeConst.BOOLEAN) || fields[i].getDataType().equals(StringDataTypeConst.UFBOOLEAN))
						textAlign = "center"; //$NON-NLS-1$
					else if(fields[i].getDataType() != null && fields[i].getDataType().equals(StringDataTypeConst.Decimal)|| fields[i].getDataType().equals(StringDataTypeConst.UFDOUBLE) || fields[i].getDataType().equals(StringDataTypeConst.DATE) || fields[i].getDataType().equals(StringDataTypeConst.INTEGER))
						textAlign = "right"; //$NON-NLS-1$
					else
						textAlign = "left"; //$NON-NLS-1$
    			}else
    				textAlign = "left"; //$NON-NLS-1$
				column.setTextAlign(textAlign);
				column.setNullAble(fields[i].isNullAble());
		
				griddatas.add(column);
			}
    		for (int j = 0; j < griddatas.size(); j++) {
    			GridColumn column = (GridColumn) griddatas.get(j);
    			if(notVisibleFields.contains(column.getId()))
    				column.setVisible(false);
			}
    		gridcomp.setColumnList(griddatas);
      		Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
    		map.put(gridcomp.getId(), gridcomp);
    		gridcomp.setColumnList(griddatas);
    		LFWPersTool.setLfwWidget(lfwwidget);
    	}
    	else if(source instanceof ListViewElementObj && target instanceof RefDatasetElementObj){
    		ListViewElementObj lvobj = (ListViewElementObj)source;
    		ListViewComp lvcomp = lvobj.getlistviewComp();
    		RefDatasetElementObj dsobj = (RefDatasetElementObj)target;
    		lvcomp.setDataset(dsobj.getDs().getId());
    		lvcomp.setCaption(dsobj.getDs().getCaption());
    		LfwView lfwwidget = LFWPersTool.getLfwWidget();
      		Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
    		map.put(lvcomp.getId(), lvcomp);
    		LFWPersTool.setLfwWidget(lfwwidget);
    	}
    	else if(source instanceof PaginationElementObj && target instanceof RefDatasetElementObj){
    		PaginationElementObj pageobj = (PaginationElementObj)source;
    		PaginationComp pagecomp = pageobj.getPaginationComp();
    		RefDatasetElementObj dsobj = (RefDatasetElementObj)target;
    		pagecomp.setDataset(dsobj.getDs().getId());
    		LfwView lfwwidget = LFWPersTool.getLfwWidget();
      		Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
    		map.put(pagecomp.getId(), pagecomp);
    		LFWPersTool.setLfwWidget(lfwwidget);
    	}
    	else if(source instanceof FormElementObj && target instanceof RefDatasetElementObj){
    		LfwView lfwwidget = LFWPersTool.getLfwWidget();
        	FormElementObj formobj = (FormElementObj)source;
    		FormComp formcomp = formobj.getFormComp();
    		List<String> notVisibleFields = new ArrayList<String>();
    		List<FormElement> formdatas = formcomp.getElementList();
    		if(formdatas != null)
    			formdatas.clear();
    		else 
    			formdatas = new ArrayList<FormElement>();
    	
    		RefDatasetElementObj dsobj = (RefDatasetElementObj)target;
    		formcomp.setDataset(dsobj.getDs().getId());
    		formcomp.setCaption(dsobj.getDs().getCaption());
    		Field[] fields = dsobj.getDs().getFieldSet().getFields();
    		if(fields == null)
    			return;
    		List<String> fieldList = new ArrayList<String>();
    		for (int i = 0; i < fields.length; i++) {   			
				fieldList.add(fields[i].getId());
			}
    		for (int i = 0; i < fields.length; i++) {
    			if(fields[i].getId().equals("dr") || fields[i].getId().equals("ts")) //$NON-NLS-1$ //$NON-NLS-2$
					continue;
    			ExtAttribute hide = fields[i].getExtendAttribute(Field.HIDE);
    			if(hide!=null){
    				boolean isHidden = (Boolean)hide.getValue();
    				if(isHidden)
    					continue;
    			}
    			FormElement element = new FormElement();
				element.setId(fields[i].getId());
				element.setField(fields[i].getId());
				element.setText(fields[i].getText());
				element.setI18nName(fields[i].getI18nName());
				element.setLangDir(fields[i].getLangDir());
				String editorType = null;
				String sourceField = fields[i].getSourceField();
				notVisibleFields.add(sourceField);
				if(fields[i].isPrimaryKey())
					notVisibleFields.add(fields[i].getId());
				if(sourceField != null){
					String refNodeFlag = CompIdGenerator.generateRefCompId(dsobj.getDs().getId(), fields[i].getId());
					IRefNode refnode = lfwwidget.getViewModels().getRefNode(refNodeFlag);
					if(refnode != null){
						editorType = EditorTypeConst.REFERENCE;
						element.setRefNode(refNodeFlag);
					}
				}
				if(editorType == null) {
					String comboDataFlag = CompIdGenerator.generateComboCompId(dsobj.getDs().getId(),fields[i].getId());
					ComboData combo = lfwwidget.getViewModels().getComboData(comboDataFlag);
					if(combo != null){
						editorType = EditorTypeConst.COMBODATA;
						element.setRefComboData(comboDataFlag);
					}
					else {
						editorType = EditorTypeConst.getEditorTypeByString(fields[i].getDataType());
					}
				}
				if(editorType == null)
					editorType = EditorTypeConst.STRINGTEXT;
				element.setEditorType(editorType);
				element.setNullAble(fields[i].isNullAble());
				element.setPrecision(fields[i].getPrecision());
				element.setEditable(true);
				element.setEnabled(true);
				formdatas.add(element);
			}
    		for (int i = 0; i < formdatas.size(); i++) {
				FormElement formEle = formdatas.get(i);
				if(notVisibleFields.contains(formEle.getId()))
					formEle.setVisible(false);
			}
    		formcomp.setElementList(formdatas);
    		Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
    		map.put(formcomp.getId(), formcomp);
    		formcomp.setElementList(formdatas);
    		LFWPersTool.setLfwWidget(lfwwidget);
//    	}else if(source instanceof ExcelElementObj && target instanceof RefDatasetElementObj){
//    		ExcelElementObj excelobj = (ExcelElementObj)source;
//    		ExcelComp excelcomp = excelobj.getExcelComp();
//    		List<String> notVisibleFields = new ArrayList<String>();
//    		List<IExcelColumn> exceldatas = excelcomp.getColumnList();
//    		if(exceldatas != null)
//    			exceldatas.clear();
//    		else 
//    			exceldatas = new ArrayList<IExcelColumn>();
//    		RefDatasetElementObj dsobj = (RefDatasetElementObj)target;
//    		excelcomp.setDataset(dsobj.getDs().getId());
//    		excelcomp.setCaption(dsobj.getDs().getCaption());
//    		Field[] fields = dsobj.getDs().getFieldSet().getFields();
//    		if(fields == null)
//    			return;
//    		List<String> fieldList = new ArrayList<String>();
//    		for (int i = 0; i < fields.length; i++) {
//				fieldList.add(fields[i].getId());
//			}
//    		LfwWidget lfwwidget = LFWPersTool.getLfwWidget();
//    		for (int i = 0; i < fields.length; i++) {
//				if(fieldList.contains(fields[i].getId() + "_mc"))
//					continue;	
//				if(fields[i].getId().equals("dr") || fields[i].getId().equals("ts"))
//					continue;
//				ExcelColumn column = new ExcelColumn();
//				column.setId(fields[i].getId());
//				column.setField(fields[i].getId());
//				column.setText(fields[i].getText());
//				column.setI18nName(fields[i].getI18nName());
//				//datatype
//				column.setDataType(fields[i].getDataType());
//				column.setPrecision(fields[i].getPrecision());
//				//editorType,rendertype
//				String editorType = null;
//				String renderType = null;
//				String sourceField = fields[i].getSourceField();
//				notVisibleFields.add(sourceField);
//				if(sourceField != null){
//					String refNodeFlag = CompIdGenerator.generateRefCompId(dsobj.getDs().getId() , fields[i].getId());
//					IRefNode refnode = lfwwidget.getViewModels().getRefNode(refNodeFlag);
//					if(refnode != null){
//						editorType = EditorTypeConst.REFERENCE;
//						renderType = RenderTypeConst.getRenderTypeByString(fields[i].getDataType());
//						column.setRefNode(refNodeFlag);
//					}
//				}
//				if(editorType == null) {
//					String comboDataFlag = CompIdGenerator.generateComboCompId(dsobj.getDs().getId(), fields[i].getId());
//					//Map<String, ComboData> comboDataMap = lfwwidget.getViewModels().getComboDataMap();
//					ComboData combo = lfwwidget.getViewModels().getComboData(comboDataFlag);
//					if(combo != null){
//						editorType = EditorTypeConst.COMBODATA;
//						renderType = RenderTypeConst.ComboRender;
//						column.setRefComboData(comboDataFlag);
//					}
//					else {
//						editorType = EditorTypeConst.getEditorTypeByString(fields[i].getDataType());
//						renderType = RenderTypeConst.getRenderTypeByString(fields[i].getDataType());
//					}
//				}
//			
//				if(editorType == null)
//					editorType = EditorTypeConst.STRINGTEXT;
//				column.setEditorType(editorType);
//				column.setRenderType(renderType);
//				
//				// 根据数据类型设置textAlign
//				String textAlign = "";
//				if(fields[i].getDataType() != null){
//					if(fields[i].getDataType().equals(StringDataTypeConst.bOOLEAN) || fields[i].getDataType().equals(StringDataTypeConst.BOOLEAN) || fields[i].getDataType().equals(StringDataTypeConst.UFBOOLEAN))
//						textAlign = "center";
//					else if(fields[i].getDataType() != null && fields[i].getDataType().equals(StringDataTypeConst.Decimal)|| fields[i].getDataType().equals(StringDataTypeConst.UFDOUBLE) || fields[i].getDataType().equals(StringDataTypeConst.DATE) || fields[i].getDataType().equals(StringDataTypeConst.INTEGER))
//						textAlign = "right";
//					else
//						textAlign = "left";
//    			}else
//    				textAlign = "left";
//				column.setTextAlign(textAlign);
//				column.setNullAble(fields[i].isNullAble());
//				exceldatas.add(column);
//			}
//    		for (int j = 0; j < exceldatas.size(); j++) {
//    			ExcelColumn column = (ExcelColumn) exceldatas.get(j);
//    			if(notVisibleFields.contains(column.getId()))
//    				column.setVisible(false);
//			}
//    		excelcomp.setColumnList(exceldatas);
//      		Map<String, WebComponent> map = lfwwidget.getViewComponents().getComponentsMap();
//    		map.put(excelcomp.getId(), excelcomp);
//    		excelcomp.setColumnList(exceldatas);
//    		LFWPersTool.setLfwWidget(lfwwidget);
    	}else if(source instanceof PlugoutDescElementObj && target instanceof PluginDescElementObj){
    		PluginDescElementObj targetnew = (PluginDescElementObj) target;
    		PlugoutDescElementObj sourcenew = (PlugoutDescElementObj) source;
//    		if(sourcenew.getPlugout().getDescItemList().size() == 0){
//    			MessageDialog.openError(null, "错误提示", "plugout中没有关联值，无法关联!");
//    			conn.disConnect();
//    			return;
//    		}
//    		if(targetnew.getPlugin().getDescItemList().size() == 0){
//    			MessageDialog.openError(null, "错误提示", "plugin中没有关联值，无法关联!");
//    			conn.disConnect();
//    			return;
//    		}
    		
    		PlugRelationDialog dialog = new PlugRelationDialog(null, M_perspective.LFWConnectionCommand_9, sourcenew, targetnew, null);
    		if(dialog.open() != InputDialog.OK){
    			conn.disConnect();
    		}
    	}
    }

	
    
    private void repaint(LFWBasicElementObj source, RefDatasetElementObj target){
    	//如果是建立ds和refdataset连接，只需要判断此refdataset的上级的子的位置
    	if(source instanceof DatasetElementObj){
    		DatasetElementObj dsobj = (DatasetElementObj)source;
    		target.setDsobj(dsobj);
    		refPaint(dsobj, target);
    	}else if (source instanceof RefDatasetElementObj){
    		//如果是refds和refds之间的连接，则根据上一个refds连接的位置判断当前位置，并且更新所有的子的位置
    		//首先确定本身refds的位置
     		RefDatasetElementObj refdsobj = (RefDatasetElementObj)source;
     		refdsobj.addChild(target);
     		target.setParent(refdsobj);
    		setselfLocation(refdsobj, target);
    	}
     }
    
    private void refPaint(DatasetElementObj dsobj, RefDatasetElementObj target){
    	List<RefDatasetElementObj> refdss = dsobj.getCells();
    	if(refdss.size() == 0){
			target.setLocation(new Point(400,0));
			return;
		}
		RefDatasetElementObj lastrefds = null;
		for (int i = 0; i < refdss.size(); i++) {
			if(refdss.get(i).equals(target)){
				if(i != 0)
					lastrefds =  (RefDatasetElementObj)refdss.get(i -1);
				break;
			}
		}
		if(lastrefds != null){
			List<RefDatasetElementObj> lastrefdschild = lastrefds.getChildren();
			if(lastrefdschild.size() == 0){
				target.setLocation(new Point(lastrefds.getLocation().x, lastrefds.getLocation().y + lastrefds.getSize().height + 50));
				return;
			}
		RefDatasetElementObj lastrefdslast = (RefDatasetElementObj) lastrefdschild.get(lastrefdschild.size() - 1);
		//Point lastdslocation = lastrefdslast.getLocation();
		int x = lastrefds.getLocation().x;
		int y = lastrefdslast.getLocation().y + lastrefdslast.getSize().height + 50;
		target.setLocation(new Point(x,y));
    }
    }
    
    
    
    private void setself(RefDatasetElementObj source, RefDatasetElementObj target){
    	List<RefDatasetElementObj> refdschildren = source.getChildren();
    	RefDatasetElementObj refdschild = null;
		if(refdschildren.size() -1  == 0){
			int x = source.getLocation().x + source.getSize().width +  WEBPersConstants.BETWEEN;
			int y = source.getLocation().y;
			target.setLocation(new Point(x, y));
			//return;
		}else{
			for (int i = 0; i < refdschildren.size(); i++) {
				if(refdschildren.get(i).equals(target)){
					if(i != 0){
						refdschild = (RefDatasetElementObj)refdschildren.get(i - 1);
						break;
					}
				}
			}
			if(refdschild != null){
				List<RefDatasetElementObj> refdschildchild = refdschild.getChildren();
				if(refdschildchild == null || refdschildchild.size() == 0){
					int x = refdschild.getLocation().x;
					int y = refdschild.getLocation().y + refdschild.getSize().height + WEBPersConstants.BETWEEN;
					target.setLocation(new Point(x, y));
					//return;
				}
			else{
				RefDatasetElementObj lastrefdslast = (RefDatasetElementObj) refdschildchild.get(refdschildchild.size() - 1);
				//Point lastdslocation = lastrefdslast.getLocation();
				int x = refdschild.getLocation().x;
				int y = lastrefdslast.getLocation().y + lastrefdslast.getSize().height + WEBPersConstants.BETWEEN;
				target.setLocation(new Point(x,y));
				}
			}
    }
		
    }
    
    
    //设置自己的位置
    private void setselfLocation(RefDatasetElementObj source, RefDatasetElementObj target){
    	List<RefDatasetElementObj> refdschildren = source.getChildren();
    	RefDatasetElementObj refdschild = null;
		if(refdschildren.size() -1  == 0){
			int x = source.getLocation().x + source.getSize().width +  WEBPersConstants.BETWEEN;
			int y = source.getLocation().y;
			target.setLocation(new Point(x, y));
			//return;
		}else{
			for (int i = 0; i < refdschildren.size(); i++) {
				if(refdschildren.get(i).equals(target)){
					if(i != 0){
						refdschild = (RefDatasetElementObj)refdschildren.get(i - 1);
						break;
					}
				}
			}
			if(refdschild != null){
				List<RefDatasetElementObj> refdschildchild = refdschild.getChildren();
				if(refdschildchild == null || refdschildchild.size() == 0){
					int x = refdschild.getLocation().x;
					int y = refdschild.getLocation().y + refdschild.getSize().height + WEBPersConstants.BETWEEN;
					target.setLocation(new Point(x, y));
					//return;
				}
			else{
				RefDatasetElementObj lastrefdslast = (RefDatasetElementObj) refdschildchild.get(refdschildchild.size() - 1);
				//Point lastdslocation = lastrefdslast.getLocation();
				int x = refdschild.getLocation().x;
				int y = lastrefdslast.getLocation().y + lastrefdslast.getSize().height + WEBPersConstants.BETWEEN;
				target.setLocation(new Point(x,y));
				}
			}
    }
		
		//重新更新所有parent的位置
		//todo(需要递归直到parent为空)
		RefDatasetElementObj sourceparent = source.getParent();
		//其父还为refdataset
		if(sourceparent != null){
			//while(sourceparent != null){
				List<RefDatasetElementObj> list = sourceparent.getChildren();
				int k = 1000;
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).equals(source))
						k = i;
					if(i > k)
						setself(sourceparent, list.get(i));
				}
				//重复循环parent
				
//				RefDatasetElementObj sourceparentp = sourceparent.getParent();
//				if(sourceparentp != null)
				refdsrepaint(sourceparent);
				//else{
				//	DatasetElementObj ds = sourceparent.getDsobj();
				//	if(ds != null)
				dsrepaint(sourceparent);
				//}
				
					
		//		if(sourceparent != null)
			//}
		}
		else {
			DatasetElementObj dsobj = source.getDsobj();
			if(dsobj != null){
				int k = 1000;
				List<RefDatasetElementObj> cells = dsobj.getCells();
				for (int i = 0; i < cells.size(); i++) {
					if(cells.get(i).equals(source))
						k = i;
					if(!cells.get(i).equals(source)){
						if(k < i){
							repaint(dsobj, cells.get(i));
							List<RefDatasetElementObj> childrenren = cells.get(i).getChildren();
							repaintChild(cells.get(i), childrenren);
						}
					}
				}
			}
		}
		//repaintParents();
    }
    private void refdsrepaint(RefDatasetElementObj refdsobj){
    	RefDatasetElementObj sourceparentp = refdsobj.getParent();
			if(sourceparentp != null){
		   	List<RefDatasetElementObj>list = refdsobj.getChildren();
			for (int i = 0; i < list.size(); i++) {
				setselfLocation(refdsobj, list.get(i));
				List<RefDatasetElementObj> childrenren = list.get(i).getChildren();
				repaintChild(list.get(i), childrenren);
			}
			refdsrepaint(sourceparentp);
		}
    }
    	
    
    private void dsrepaint(RefDatasetElementObj refdsobj){
    	//DatasetElementObj dsobj = source.getDsobj();
    	DatasetElementObj dsobj = refdsobj.getDsobj();
    	if(dsobj != null){
			int k = 1000;
			List<RefDatasetElementObj> cells = dsobj.getCells();
			for (int i = 0; i < cells.size(); i++) {
				if(cells.get(i).equals(refdsobj))
					k = i;
				if(!cells.get(i).equals(refdsobj)){
					if(k < i){
						repaint(dsobj, cells.get(i));
						List<RefDatasetElementObj> childrenren = cells.get(i).getChildren();
						repaintChild(cells.get(i), childrenren);
					}
				}
				
			}
    	}
	}

    
    
    private void repaintChild(RefDatasetElementObj source, List<RefDatasetElementObj> target){
    	int x = 0;
    	int y = 0;
     	if(target.size() > 0){
     		for (int i = 0; i < target.size(); i++) {
     			RefDatasetElementObj refchild = target.get(i);
	    		if(i == 0){
	    			x = source.getLocation().x + source.getSize().width + WEBPersConstants.BETWEEN;
	    	    	y = source.getLocation().y;
	    		}else{
	    		x = target.get(i - 1).getLocation().x;
	        	y = target.get(i - 1).getLocation().y + target.get(i - 1).getSize().height + WEBPersConstants.BETWEEN;
	    		}
	    		refchild.setLocation(new Point(x, y));
	    		repaintChild(refchild, refchild.getChildren());
			}
    	}
     }
    

	public void undo() {
		conn.disConnect();
	}

	public void execute() {
		try {
			conn = (Connection)constructor.newInstance(new LFWBasicElementObj[]{source, target});
			redo();
		   	
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}

	}
}