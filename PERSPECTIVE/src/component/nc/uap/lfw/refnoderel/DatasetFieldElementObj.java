package nc.uap.lfw.refnoderel;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Field;

@SuppressWarnings("serial")
public class DatasetFieldElementObj extends LfwElementObjWithGraph{

	public static final String PROP_DSFIELD_ELEMENT ="dsfield_element";
	private Field field;
	private String dsId;
	private String filterSql;
	public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public String getNullProcessor() {
		return nullProcessor;
	}

	public void setNullProcessor(String nullProcessor) {
		this.nullProcessor = nullProcessor;
	}

	private String nullProcessor;
	
	public String getDsId() {
		return dsId;
	}

	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		fireStructureChange(PROP_DSFIELD_ELEMENT,  field);
	}

	@Override
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}

}
