package nc.uap.lfw.perspective.editor;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;

public class DatasetInitData {
	
	public static Dataset getDataset(){
		Dataset dataset = new Dataset();
		FieldSet fieldset = new FieldSet();
		Field field = new Field();
		field.setDataType("String");
		field.setI18nName("pk_user");
		field.setId("pk_user");
		field.setNullAble(false);
		//dsItem.setNullable(false);
		fieldset.addField(field);
		dataset.setFieldSet(fieldset);
		return dataset;
	}
}
