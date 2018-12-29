package nc.uap.lfw.chart.core;

import java.util.ArrayList;
import java.util.Arrays;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.ObjectComboPropertyDescriptor;
import nc.uap.lfw.core.comp.ChartConfig;
import nc.uap.lfw.core.comp.ChartConfigColumn2D;
import nc.uap.lfw.core.comp.ChartConfigLine2D;
import nc.uap.lfw.core.comp.ChartConfigPie3D;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.lang.M_chart;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ChartConfigEleObj extends LfwElementObjWithGraph{
	
	private ChartConfig chartconfig = null;
	public static final String PROP_CHARTCONFIG_ELEMENT ="chartconfig_element"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
	private static final Object PROP_GROUPCOLUMN = "element_GROUPCOLUMN"; //$NON-NLS-1$
	private static final Object PROP_GROUPNAME = "element_GROUPNAME"; //$NON-NLS-1$
	private static final Object PROP_NUNBERPREFIX = "element_NUNBERPREFIX"; //$NON-NLS-1$
	private static final Object PROP_SERIESCOLUMN = "element_SERIESCOLUMN"; //$NON-NLS-1$
	private static final Object PROP_SERIESNAME = "element_SERIESNAME"; //$NON-NLS-1$
	private static final Object PROP_SERIESTYPE = "element_SERIESTYPE"; //$NON-NLS-1$
	private static final Object PROP_SHOWTYPE = "element_SHOWTYPE"; //$NON-NLS-1$
	private static final Object PROP_XNAME = "element_XNAME"; //$NON-NLS-1$
	private static final Object PROP_YNAME = "element_YNAME"; //$NON-NLS-1$
	
	private static String[] SERIESTYPE={"单指标","多指标"}; //$NON-NLS-1$ //$NON-NLS-2$
	private static String[] SHOWTYPE={M_chart.ChartConfigEleObj_0,M_chart.ChartConfigEleObj_1,M_chart.ChartConfigEleObj_2};
	private String type;
	
	
	public ChartConfig getChartconfig() {
		return chartconfig;
	}

	public void setChartconfig(ChartConfig chartconfig) {
		this.chartconfig = chartconfig;
		fireStructureChange(PROP_CHARTCONFIG_ELEMENT, chartconfig);
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		PropertyDescriptor[] pds = new PropertyDescriptor[9];
		pds[0] = new TextPropertyDescriptor(PROP_CAPTION, M_chart.ChartConfigEleObj_3);
		pds[0].setCategory(M_chart.ChartConfigEleObj_4);
		pds[1] = new TextPropertyDescriptor(PROP_GROUPCOLUMN,M_chart.ChartConfigEleObj_5);
		pds[1].setCategory(M_chart.ChartConfigEleObj_4);
		pds[2] = new TextPropertyDescriptor(PROP_GROUPNAME,M_chart.ChartConfigEleObj_6);
		pds[2].setCategory(M_chart.ChartConfigEleObj_4);
		pds[3] = new TextPropertyDescriptor(PROP_NUNBERPREFIX,M_chart.ChartConfigEleObj_7);
		pds[3].setCategory(M_chart.ChartConfigEleObj_4);
		pds[4] = new TextPropertyDescriptor(PROP_SERIESCOLUMN,M_chart.ChartConfigEleObj_8);
		pds[4].setCategory(M_chart.ChartConfigEleObj_4);
		pds[5] = new TextPropertyDescriptor(PROP_SERIESNAME,M_chart.ChartConfigEleObj_9);
		pds[5].setCategory(M_chart.ChartConfigEleObj_4);
//		pds[6] = new ObjectComboPropertyDescriptor(PROP_SERIESTYPE,"指标类型",SERIESTYPE);
//		pds[6].setCategory("基本");
		pds[6] = new ObjectComboPropertyDescriptor(PROP_SHOWTYPE,M_chart.ChartConfigEleObj_10,SHOWTYPE);
		pds[6].setCategory(M_chart.ChartConfigEleObj_4);
		pds[7] = new TextPropertyDescriptor(PROP_XNAME,M_chart.ChartConfigEleObj_11);
		pds[7].setCategory(M_chart.ChartConfigEleObj_4);
		pds[8] = new TextPropertyDescriptor(PROP_YNAME,M_chart.ChartConfigEleObj_12);
		pds[8].setCategory(M_chart.ChartConfigEleObj_4);
//		if(chartconfig instanceof ChartConfigPie3D){
//			ChartConfig newchart = null;
//			if((getType()!=null&&getType().equals("饼图"))||chartconfig instanceof ChartConfigPie3D){
//				newchart = new ChartConfigPie3D();
//				newchart.setId(chartconfig.getId());
//				setChartconfig(newchart);
//			}
//		}
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if(PROP_CAPTION.equals(id))
			return chartconfig.getCaption() == null?"":chartconfig.getCaption(); //$NON-NLS-1$
		else if(PROP_GROUPCOLUMN.equals(id))
			return chartconfig.getGroupColumn() == null?"":chartconfig.getGroupColumn(); //$NON-NLS-1$
		else if(PROP_GROUPNAME.equals(id))
			return chartconfig.getGroupName() == null?"":chartconfig.getGroupName(); //$NON-NLS-1$
		else if(PROP_NUNBERPREFIX.equals(id))
			return chartconfig.getNumberPrefix()== null?"":chartconfig.getNumberPrefix(); //$NON-NLS-1$
		else if(PROP_SERIESCOLUMN.equals(id))
			return chartconfig.getSeriesColumns() == null?"":chartconfig.getSeriesColumns(); //$NON-NLS-1$
		else if(PROP_SERIESNAME.equals(id))
			return chartconfig.getSeriesNames() == null?"":chartconfig.getSeriesNames(); //$NON-NLS-1$
//		if(PROP_SERIESTYPE.equals(id))
//			return chartconfig.getSeriesType() == null?"":chartconfig.getSeriesType();
		else if(PROP_SHOWTYPE.equals(id)){
			if(getChartconfig() instanceof ChartConfigPie3D){
				setType(M_chart.ChartConfigEleObj_0);				
			}
			else if(getChartconfig() instanceof ChartConfigLine2D){
				setType(M_chart.ChartConfigEleObj_1);	
			}
			else if(getChartconfig() instanceof ChartConfigColumn2D){
				setType(M_chart.ChartConfigEleObj_2);	
			}
			return getType();
		}
		else if(PROP_XNAME.equals(id)){
			return chartconfig.getXAxisName() == null?"":chartconfig.getXAxisName(); //$NON-NLS-1$
		}
		else if(PROP_YNAME.equals(id)){
			return chartconfig.getYAxisName() == null?"":chartconfig.getYAxisName(); //$NON-NLS-1$
		}
		else 
			return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		WebElement webele = getWebElement();
		if(!canChange(webele))
			return;
		if(PROP_CAPTION.equals(id))
			chartconfig.setCaption((String)value);
		else if(PROP_GROUPCOLUMN.equals(id))
			chartconfig.setGroupColumn((String)value);
		else if(PROP_GROUPNAME.equals(id))
			chartconfig.setGroupName((String)value);
		else if(PROP_NUNBERPREFIX.equals(id))
			chartconfig.setNumberPrefix((String)value);
		else if(PROP_SERIESCOLUMN.equals(id))
			chartconfig.setSeriesColumns((String)value);
		else if(PROP_SERIESNAME.equals(id))
			chartconfig.setSeriesNames((String)value);
//		if(PROP_SERIESTYPE.equals(id))
//			chartconfig.setSeriesType((String)value);
		else if(PROP_SHOWTYPE.equals(id)){
//			chartconfig.setShowType((String)value);		
			setType((String)value);
			ChartConfig newchart = null;
			if(value.equals(M_chart.ChartConfigEleObj_0)){
				newchart = new ChartConfigPie3D();
			}
			else if(value.equals(M_chart.ChartConfigEleObj_1)){
				newchart = new ChartConfigLine2D();				
			}
			else if(value.equals(M_chart.ChartConfigEleObj_2)){
				newchart = new ChartConfigColumn2D();				
			}
			else return;
			newchart.setId(chartconfig.getId());
			setChartconfig(newchart);
		}
		else if(PROP_XNAME.equals(id)){
			chartconfig.setXAxisName((String)value);
		}
		else if(PROP_YNAME.equals(id)){
			chartconfig.setYAxisName((String)value);
		}
			
		
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public WebElement getWebElement() {
		return chartconfig;
	}
	

}
