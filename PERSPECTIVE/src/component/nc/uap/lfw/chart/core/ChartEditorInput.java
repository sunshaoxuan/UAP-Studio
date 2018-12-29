package nc.uap.lfw.chart.core;

import nc.lfw.editor.common.ElementEditorInput;
import nc.uap.lfw.core.comp.ChartComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.lang.M_chart;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

public class ChartEditorInput extends ElementEditorInput{

	public ChartEditorInput(ChartComp charComp, LfwView widget, LfwWindow pagemeta){
		super(charComp, widget, pagemeta);
	}
	
	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return M_chart.ChartEditorInput_0;
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		return  M_chart.ChartEditorInput_1;
	}
}
