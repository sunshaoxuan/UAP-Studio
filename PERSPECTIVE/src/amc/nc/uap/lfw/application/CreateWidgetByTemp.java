package nc.uap.lfw.application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.base.ExtendAttributeSupport;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.CompIdGenerator;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.RenderTypeConst;
import nc.uap.lfw.core.common.StringDataTypeConst;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.template.mastersecondly.MasterSecondlyWindowAction;

/**
 * 根据模板生成页面文件
 * 
 * @author qinjianc
 * 
 */
public class CreateWidgetByTemp {

	private String tempName;
	private int inext = 0;
	private Map<String,String> entity = null;

	public CreateWidgetByTemp(String tempName) {
		this.tempName = tempName;
	}

	/*
	 * 替换模板文件内容并在指定位置生成新文件
	 */
	public List createFromTemp(String inputPath, String outputPath,
			String filename,HashMap data) {
		InputStream ins = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(inputPath + filename);
		String sourcecontent = "";
		if (filename.endsWith(".template")) {
			filename = filename.substring(0, filename.indexOf(".template"));
		}
		try {

			String content = "";
			if (!filename.endsWith(".java")) {
				content = FileUtilities.fetchFileContent(ins, "UTF-8");
			} else{
				if(ins==null){
					MainPlugin.getDefault().logError(inputPath + filename);
				}
				content = FileUtilities.fetchFileContent(ins, "GBK");
			}
			if(content==null){
				MainPlugin.getDefault().logError("content为null");
			}
			sourcecontent = content.trim();
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e);
		}
		finally{
			if(ins != null)
				try {
					ins.close();
				} 
				catch (IOException e) {
					MainPlugin.getDefault().logError(e);
				}
		}
		// 修改变化部分
		String targetcontent = "";
		String oldText = "";
		Object newValue;
		try {
			Iterator keys = data.keySet().iterator();
			int keysfirst = 0;
			while (keys.hasNext()) {
				oldText = (String) keys.next();
				newValue = data.get(oldText);
				if (newValue != null) {
					String newText = (String) newValue;
					inext = 0;
					if (keysfirst == 0) {
						targetcontent = replaceTemp(sourcecontent, oldText,
								newText);
						keysfirst = 1;
					} else {
						targetcontent = replaceTemp(targetcontent, oldText,
								newText);
						keysfirst = 1;
					}
				}
			}
			File directory = new File(outputPath);
			if (!directory.exists())
				directory.mkdirs();

//			String filePath = outputPath + filename;
			if(filename.startsWith("ListWinController"))
				filename = (String) data.get(MasterSecondlyWindowAction.DATA_LIST_WIN_CTR_NAME)+".java";
			if(filename.startsWith("ListWinMainViewController"))
				filename = (String) data.get(MasterSecondlyWindowAction.DATA_LIST_WIN_VIEW_CTR_NAME)+".java";
			if(filename.startsWith("CardWinController"))
				filename = (String) data.get(MasterSecondlyWindowAction.DATA_POP_WIN_CTR_NAME)+".java";
			if(filename.startsWith("CardWinMainViewController"))
				filename = (String) data.get(MasterSecondlyWindowAction.DATA_POP_WIN_VIEW_CTR_NAME)+".java";
			
			File file = new File(outputPath + filename);
			if (!file.exists())
				file.createNewFile();
			List<String> fileMap = new ArrayList<String>(2);
			
			if (targetcontent.equals("") || targetcontent == "") {
				fileMap.add(file.getPath());
				fileMap.add(sourcecontent);
			}
			else{
				fileMap.add(file.getPath());
				fileMap.add(targetcontent);
			}
			return fileMap;
//			if (!filename.endsWith(".java")) {
//				if (targetcontent.equals("") || targetcontent == "") {
//					FileUtilities.saveFile(file, sourcecontent, "UTF-8");
//				} else {
//					FileUtilities.saveFile(file, targetcontent, "UTF-8");
//				}
//			} else {
//				if (targetcontent.equals("") || targetcontent == "") {
//					FileUtilities.saveFile(file, sourcecontent, "GBK");
//				} else {
//					FileUtilities.saveFile(file, targetcontent, "GBK");
//				}
//			}


		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		return null;
	}

	/*
	 * 替换模板文件中的标签
	 */
	public String replaceTemp(String content, String markersign,
			String replacecontent) {
		markersign = "${" + markersign + "}";
		String target = content;
		target = target.replace(markersign, replacecontent);
		return target.trim();
	}

	/*
	 * 将view中需要关联数据集的控件填充数据
	 */
	public void fillWithData(WebComponent comp, Dataset data) {
		fillWithData(comp,data,null);
	}
	
	public void fillWithData(WebComponent comp, Dataset data,Map<String,String> IBDMap) {
		if (comp instanceof GridComp) {
			GridComp gridcomp = (GridComp) comp;
			List<String> notVisibleFields = new ArrayList<String>();
			List<IGridColumn> griddatas = gridcomp.getColumnList();
			if (griddatas != null)
				griddatas.clear();
			else
				griddatas = new ArrayList<IGridColumn>();

			Field[] fields = data.getFieldSet().getFields();
			if (fields == null)
				return;
			List<String> fieldList = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				fieldList.add(fields[i].getId());
			}
			LfwView lfwwidget = gridcomp.getWidget();
			for (int i = 0; i < fields.length; i++) {
				if (fieldList.contains(fields[i].getId() + "_mc"))
					continue;
				if (fields[i].getId().equals("dr")
						|| fields[i].getId().equals("ts")
						|| fields[i].getId().equals("status"))
					continue;
				ExtAttribute attr = fields[i].getExtendAttribute(ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + Field.MDFIELD_UFID);
				if (attr != null){
					String ufid = (String)attr.getValue();
					if (ufid != null &&  ufid.equals(Field.MDFIELD_UFID)){
						continue;
					}
				}
				GridColumn column = new GridColumn();
				column.setId(fields[i].getId());
				column.setField(fields[i].getId());
				column.setText(fields[i].getText());
				column.setI18nName(fields[i].getI18nName());
				column.setLangDir(fields[i].getLangDir());
				// datatype
				column.setDataType(fields[i].getDataType());
				column.setPrecision(fields[i].getPrecision());
				// editorType,rendertype
				String editorType = null;
				String renderType = null;
				String sourceField = fields[i].getSourceField();
				notVisibleFields.add(sourceField);
				if(fields[i].isPrimaryKey())
					notVisibleFields.add(fields[i].getId());
				if (sourceField != null) {
					String refNodeFlag = CompIdGenerator.generateRefCompId(
							data.getId(), fields[i].getId());
					IRefNode refnode = lfwwidget.getViewModels().getRefNode(
							refNodeFlag);
					if (refnode != null) {
						editorType = EditorTypeConst.REFERENCE;
						renderType = RenderTypeConst
								.getRenderTypeByString(fields[i].getDataType());
						column.setRefNode(refNodeFlag);
					}
				}
				if (editorType == null) {
					String comboDataFlag = CompIdGenerator.generateComboCompId(
							data.getId(), fields[i].getId());
					// Map<String, ComboData> comboDataMap =
					// lfwwidget.getViewModels().getComboDataMap();
					ComboData combo = lfwwidget.getViewModels().getComboData(
							comboDataFlag);
					if (combo != null) {
						editorType = EditorTypeConst.COMBODATA;
						renderType = RenderTypeConst.ComboRender;
						column.setRefComboData(comboDataFlag);
					} else {
						editorType = EditorTypeConst
								.getEditorTypeByString(fields[i].getDataType());
						renderType = RenderTypeConst
								.getRenderTypeByString(fields[i].getDataType());
					}
				}

				if (editorType == null)
					editorType = EditorTypeConst.STRINGTEXT;
				column.setEditorType(editorType);
				column.setRenderType(renderType);

				// 根据数据类型设置textAlign
				String textAlign = "";
				if (fields[i].getDataType() != null) {
					if (fields[i].getDataType().equals(
							StringDataTypeConst.bOOLEAN)
							|| fields[i].getDataType().equals(
									StringDataTypeConst.BOOLEAN)
							|| fields[i].getDataType().equals(
									StringDataTypeConst.UFBOOLEAN))
						textAlign = "center";
					else if (fields[i].getDataType() != null
							&& fields[i].getDataType().equals(
									StringDataTypeConst.Decimal)
							|| fields[i].getDataType().equals(
									StringDataTypeConst.UFDOUBLE)
							|| fields[i].getDataType().equals(
									StringDataTypeConst.DATE)
							|| fields[i].getDataType().equals(
									StringDataTypeConst.INTEGER))
						textAlign = "right";
					else
						textAlign = "left";
				} else
					textAlign = "left";
				column.setTextAlign(textAlign);
				column.setNullAble(fields[i].isNullAble());

				griddatas.add(column);
			}
			for (int j = 0; j < griddatas.size(); j++) {
				GridColumn column = (GridColumn) griddatas.get(j);
				if (notVisibleFields.contains(column.getId()))
					column.setVisible(false);
			}
			gridcomp.setColumnList(griddatas);
			// Map<String, WebComponent> map =
			// lfwwidget.getViewComponents().getComponentsMap();
			// map.put(gridcomp.getId(), gridcomp);
		}
		else if (comp instanceof FormComp) {
			FormComp formcomp = (FormComp) comp;
			LfwView lfwwidget = formcomp.getWidget();
			List<String> notVisibleFields = new ArrayList<String>();
			List<FormElement> formdatas = formcomp.getElementList();
			if (formdatas != null)
				formdatas.clear();
			else
				formdatas = new ArrayList<FormElement>();

			List<FormElement> formdatas2 = new ArrayList<FormElement>();
			Field[] fields = data.getFieldSet().getFields();
			if (fields == null)
				return;
			List<String> fieldList = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				fieldList.add(fields[i].getId());
			}
			String fieldStr = null;
			
			if(IBDMap!=null){
				fieldStr = IBDMap.get("pk_org");
			}
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getId().equals("dr")
						|| fields[i].getId().equals("ts")
						|| fields[i].getId().equals("status"))
					continue;
				ExtAttribute attr = fields[i].getExtendAttribute(ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + Field.MDFIELD_UFID);
				if (attr != null){
					String ufid = (String)attr.getValue();
					if (ufid != null &&  ufid.equals(Field.MDFIELD_UFID)){
						continue;
					}
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
				if (sourceField != null) {
					String refNodeFlag = CompIdGenerator.generateRefCompId(
							data.getId(), fields[i].getId());
					IRefNode refnode = lfwwidget.getViewModels().getRefNode(
							refNodeFlag);
					if (refnode != null) {
						editorType = EditorTypeConst.REFERENCE;
						element.setRefNode(refNodeFlag);
					}
				}
				if (editorType == null) {
					String comboDataFlag = CompIdGenerator.generateComboCompId(
							data.getId(), fields[i].getId());
					ComboData combo = lfwwidget.getViewModels().getComboData(
							comboDataFlag);
					if (combo != null) {
						editorType = EditorTypeConst.COMBODATA;
						element.setRefComboData(comboDataFlag);
					} else {
						editorType = EditorTypeConst
								.getEditorTypeByString(fields[i].getDataType());
					}
				}
				if (editorType == null)
					editorType = EditorTypeConst.STRINGTEXT;
				element.setEditorType(editorType);
				element.setNullAble(fields[i].isNullAble());
				element.setPrecision(fields[i].getPrecision());
				element.setEditable(true);
				if(entity!=null&&entity.get(fields[i].getId())!=null){
					element.setEnabled(false);
				}else element.setEnabled(true);		
								
				if(fieldStr!=null&&element.getId().equals(fieldStr)){
					formdatas.add(element);
				}
				else if(fieldStr!=null&&element.getId().equals(fieldStr+"_name")){
					element.setColSpan(3);
					element.setEnabled(false);
//					String comboDataFlag = element.getRefComboData();
//					ComboData combo = lfwwidget.getViewModels().getComboData(
//							comboDataFlag);
					formdatas.add(element);
					
				}					
				else formdatas2.add(element);
			}
			
			formdatas.addAll(formdatas2);
			for (int i = 0; i < formdatas.size(); i++) {
				FormElement formEle = formdatas.get(i);
				if (notVisibleFields.contains(formEle.getId()))
					formEle.setVisible(false);
			}
			formcomp.setElementList(formdatas);
		}

	}
	public void saveFile(List[] listArray){
		try{
			for(List<String> fileMap:listArray){
				File file = new File(fileMap.get(0));
				String content = fileMap.get(1);
				if (!fileMap.get(0).endsWith(".java")) {
					FileUtilities.saveFile(file, content, "UTF-8");
				}
				else
					FileUtilities.saveFile(file, content, "GBK");
			}
		}
		catch(Exception e){
			MainPlugin.getDefault().logError(e.getMessage(),e);
		}
	}

	public Map<String, String> getEntity() {
		return entity;
	}

	public void setEntity(Map<String, String> entity) {
		this.entity = entity;
	}
	
}
