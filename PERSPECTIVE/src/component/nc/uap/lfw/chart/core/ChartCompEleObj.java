package nc.uap.lfw.chart.core;

import nc.lfw.editor.common.LFWWebComponentObj;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.comp.WebElement;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class ChartCompEleObj extends LFWWebComponentObj {
	
	public static final String PROP_ID = "element_ID";
	public static final String PROP_CHART_ELEMENT ="chart_element";
	
	private static final long serialVersionUID = 1L;
	private ChartComp chartComp;
	
	public ChartComp getChartComp() {
		return chartComp;
	}

	public void setChartComp(ChartComp chartComp) {
		this.chartComp = chartComp;
		fireStructureChange(PROP_CHART_ELEMENT, chartComp);
	}
		
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return super.getPropertyDescriptors();
	}
	
	public void setPropertyValue(Object id, Object value) {
		WebElement webele = getWebElement();
		if(!canChange(webele))
			return;
		super.setPropertyValue(id, value);
	}
	public Object getPropertyValue(Object id) {
		return super.getPropertyValue(id);
	}

	
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return chartComp;
	}
	
}
