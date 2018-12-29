package nc.uap.lfw.editor.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.widget.WidgetGraph;
import nc.lfw.editor.widget.plug.PluginDescElementObj;
import nc.lfw.editor.widget.plug.PlugoutDescElementObj;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.uimodel.WindowConfig;
import nc.uap.lfw.editor.application.ApplicationEditor;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.model.Constant;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class WindowConfigObj extends LfwElementObjWithGraph{
	
	private WindowConfig windowConfig = null;
	
	private WindowConfigFigure figure = null;
	
	public static final String PROP_WINDOW_ELEMENT = "window_element"; //$NON-NLS-1$
	public static final String PROP_ID = "window_id"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "window_caption"; //$NON-NLS-1$
	public static final String PROP_FREEDESIGN = "window_freedesign"; //$NON-NLS-1$
	
	/*plugoutDesc列表*/
	private List<PlugoutDescElementObj> plugoutCells = new ArrayList<PlugoutDescElementObj>();
	/*pluginDesc列表*/
	private List<PluginDescElementObj> pluginCells = new ArrayList<PluginDescElementObj>();

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		PropertyDescriptor[] pds = new PropertyDescriptor[3];
		pds[0] = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		pds[0].setCategory(M_editor.WindowConfigObj_0);
		pds[1] = new TextPropertyDescriptor(PROP_CAPTION, M_editor.WindowConfigObj_1);
		pds[1].setCategory(M_editor.WindowConfigObj_0);
		pds[2] = new ComboBoxPropertyDescriptor(PROP_FREEDESIGN, M_editor.WindowConfigObj_2,Constant.ISLAZY);
		pds[2].setCategory(M_editor.WindowConfigObj_0);
		al.addAll(Arrays.asList(pds));
		return al.toArray(new IPropertyDescriptor[0]);
	}
	@Override
	public Object getPropertyValue(Object id) {
		if (PROP_ID.equals(id))
			return windowConfig.getId()== null ? "" : windowConfig.getId(); //$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return windowConfig.getCaption()==null?"":windowConfig.getCaption(); //$NON-NLS-1$
		else if(PROP_FREEDESIGN.equals(id))
			return windowConfig.isCanFreeDesign()? Integer.valueOf(0):Integer.valueOf(1);
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		ApplicationEditor editor = ApplicationEditor.getActiveEditor();
		if(editor == null){
			MessageDialog.openWarning(null, M_editor.WindowConfigObj_3, M_editor.WindowConfigObj_4);
			return;
		}
		if (PROP_ID.equals(id)){
			windowConfig.setId((String)value);
		}
		else if(PROP_CAPTION.equals(id)){
			windowConfig.setCaption((String)value);
			editor.setDirtyTrue();
		}
		else if(PROP_FREEDESIGN.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			windowConfig.setIsCanFreeDesign(truevalue);
//			ApplicationEditor editor = ApplicationEditor.getActiveEditor();
			editor.setDirtyTrue();
		}
		
	}
	public List<PlugoutDescElementObj> getPlugoutCells() {
		return plugoutCells;
	}

	public void addPlugoutCell(PlugoutDescElementObj cell) {
		plugoutCells.add(cell);
		firePlugChange();
	}

	public void removePlugoutCell(PlugoutDescElementObj cell) {
		plugoutCells.remove(cell);
		for (int i = 0 ; i < plugoutCells.size() ; i++){
			PlugoutDescElementObj plugoutCell = plugoutCells.get(i);
			Point p = new Point();
			p.x = plugoutCell.getLocation().x;
			p.y = plugoutCell.getWidgetObj().getLocation().y + i * 40;
			plugoutCell.setLocation(p);
		}
		firePlugChange();
	}

	public List<PluginDescElementObj> getPluginCells() {
		return pluginCells;
	}
	
	public void addPluginCell(PluginDescElementObj cell) {
		pluginCells.add(cell);
		firePlugChange();
	}
	
	public void removePluginCell(PluginDescElementObj cell) {
		pluginCells.remove(cell);
		for (int i = 0 ; i < pluginCells.size() ; i++){
			PluginDescElementObj pluginCell = pluginCells.get(i);
			Point p = new Point();
			p.x = pluginCell.getLocation().x;
			p.y = pluginCell.getWidgetObj().getLocation().y + i * 40;
			pluginCell.setLocation(p);
		}
		firePlugChange();
	}
	
	private void firePlugChange(){
		LfwBaseGraph graph = getGraph();
		if (graph instanceof WidgetGraph)
			((WidgetGraph)graph).fireWidgetPlugChange(windowConfig);
	}

	@Override
	public WebElement getWebElement() {
		// TODO Auto-generated method stub
		return null;
	}

	public WindowConfig getWindowConfig() {
		return windowConfig;
	}

	public void setWindowConfig(WindowConfig windowConfig) {
		this.windowConfig = windowConfig;		
	}

	public WindowConfigFigure getFigure() {
		return figure;
	}

	public void setFigure(WindowConfigFigure figure) {
		this.figure = figure;
	}
	

}
