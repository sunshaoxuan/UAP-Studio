package nc.uap.lfw.perspective.model;
import java.util.ArrayList;
import java.util.HashMap;

import nc.lfw.editor.common.LFWBaseRectangleFigure;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MDField;
import nc.uap.lfw.core.data.MatchField;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;


/**
 * Dataset Figure
 * @author zhangxya
 *
 */
public class DatasetElementFigure extends LFWBaseRectangleFigure{
	
	private static Color bgColor = new Color(null, 233,176,66);
	private HashMap<String, DsFieldLabel> hmAttrToPropLabel = new HashMap<String, DsFieldLabel>();
	private LfwElementObjWithGraph ele;
	private int height = 0;
	private DatasetElementObj datasetobj = null;
	
	public DatasetElementFigure(LfwElementObjWithGraph ele){
		super(ele);
		this.ele = ele;
		datasetobj = (DatasetElementObj) ele;
		setTypeLabText("数据集[" + datasetobj.getDs().getCaption() + "]");
		setBackgroundColor(bgColor);
//		setTitleText(datasetobj.getDs().getId(), datasetobj.getDs().getId());
		markError(datasetobj.validate());
		Point point = datasetobj.getLocation();
		int x, y;
		if(point != null){
			x = point.x;
			y = point.y;
		}else{
			x = 100;
			y = 100;
		}
		add(getAttrsFigure());
		FieldRelation[] frs = datasetobj.getDs().getFieldRelations().getFieldRelations();
		if(frs != null && (!(datasetobj.isDatasets()))){
			for (int i = 0; i < frs.length; i++) {
				FieldRelation fr = frs[i];
				addAttribute(fr);
			}
		}
		if(!datasetobj.isDatasets()){
			ArrayList<Field> fieldList = (ArrayList)datasetobj.getDs().getFieldSet().getFieldList();
			for(int i = 0 ; i< fieldList.size();i++){
				Field field = fieldList.get(i);
				showField(field);
			}
		}
		
		this.height += 3 * LINE_HEIGHT;
		setBounds(new Rectangle(x, y, 150, this.height > 150? this.height: 150));
	}
	
	private void resizeHeight() {
		setSize(180, this.height > 150 ? this.height : 150);
		//Dimension dimension = new Dimension(180, this.height > 150 ? this.height : 150);
		//ele.setSize(dimension);
	}
	
	public void addAttribute(FieldRelation fr){
		MatchField[] matches = fr.getMatchFields();
		for (int i = 0; i < matches.length; i++) {
			MatchField match = matches[i];
			String isMatch = match.getIsmacth();
			String writeField = match.getWriteField();
			DsFieldLabel lab = new DsFieldLabel(writeField, isMatch);
			hmAttrToPropLabel.put(writeField, lab);
			getAttrsFigure().add(lab);
			this.height += LINE_HEIGHT;
			resizeHeight();
		}
		
	}
	public void showField(Field field){
		String fieldId = field.getField();
		String fieldName = field.getText();
		String flag = "field";
		if(field.isPrimaryKey()) flag = "key";
		DsFieldLabel lab = new DsFieldLabel(fieldName, flag);
		hmAttrToPropLabel.put(fieldName, lab);
		getAttrsFigure().add(lab);
		this.height += LINE_HEIGHT;
		resizeHeight();
	}
	
	
	/**
	 * 清空所有属性
	 */
	public void deleteAllAttris(){
		getAttrsFigure().removeAll();
	}
	
	public void deleteAttribute(FieldRelation fr){
		MatchField[] matches = fr.getMatchFields();
		for (int i = 0; i < matches.length; i++) {
			MatchField match = matches[i];
			String writeField = match.getWriteField();
			DsFieldLabel lab = hmAttrToPropLabel.get(writeField);
			if(lab != null){
				hmAttrToPropLabel.remove(lab);
				getAttrsFigure().remove(lab);
				this.height -= LINE_HEIGHT;
				resizeHeight();
			}
		}
	}
	
		
	public void updateAttribute(Field attri){
		DatasetElementObj datasetobj = (DatasetElementObj) ele;
		Dataset ds = datasetobj.getDs();
		Field[] fields = ds.getFieldSet().getFields();
		for (int i = 0; i < fields.length; i++) {
//			if(fields[i].getExtendAttribute(PK_FIELD).equals(attri.getExtendAttribute(PK_FIELD)))
//					ds.getFieldSet().addField(attri);
		}
//		ds.getFieldSet().removeField(oldValue);
//		ds.getFieldSet().addField(attri);
		//ds.getFieldSet().
		//DsFieldLabel figure = hmAttrToPropLabel.get(attri);
		//figure.updateFigure(attri);
	}
	public void setTypeLab(String text){
		setTypeLabText(text);
	}
	public void setTitleLab(String text,String id){
		setTitleText(text, id);
	}
	
	protected String getTypeText() {
		return "数据集";
	}
	
}
