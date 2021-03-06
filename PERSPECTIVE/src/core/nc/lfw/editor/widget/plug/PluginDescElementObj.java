package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.common.SubmitRulePropertyDescriptor;
import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.pagemeta.WindowEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.page.IPluginDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_parts;
import nc.uap.lfw.palette.ChildConnection;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * PluginDesc元素对象
 * @author dingrf
 *
 */
public class PluginDescElementObj extends LfwElementObjWithGraph {

	private static final String PROXYED_VIEW_PLUGIN = M_parts.PluginDescElementObj_0;

	private static final String PROXYED_VIEW = M_parts.PluginDescElementObj_1;

	private static final long serialVersionUID = -5675125816072493807L;
	
	private WidgetElementObj widgetObj;
	
	private WindowObj windowObj;
	// PluginDesc对象
	private IPluginDesc plugin;
	//连接线
	private ChildConnection conn;
	// 当前图像
	private PluginDescElementFigure figure;
	
	private LfwView[] delegatedViews;
	
	private String delegatedViewId;
	
	private String[] delegatedPluginIds;

	public static final String PROP_PLUGIN_ID = "prop_plugin_id"; //$NON-NLS-1$
	public static final String PROP_PLUGIN_METHOD = "prop_plugin_method"; //$NON-NLS-1$
	public static final String PROP_PLUGIN_SUBMITRULE = "prop_plugin_submitrule"; //$NON-NLS-1$
	public static final String PROP_PLUGIN_CONTROL = "prop_plugin_control"; //$NON-NLS-1$
	public static final String PROP_PLUGIN_VIEW = "prop_plugin_view"; //$NON-NLS-1$
	public static final String PROP_PLUGIN_VIEWPLUGINID = "prop_plugin_viewpluginid"; //$NON-NLS-1$
	
//	public static final String PROP_PLUGIN_TYPE = "prop_plugin_type";
	
//	public static final String PROP_PLUGIN_SUBMITRULE = "prop_plugin_submitrule";

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		if(plugin instanceof PluginDesc){
//			PropertyDescriptor[] pds = new PropertyDescriptor[4];
			PropertyDescriptor pds0 = new NoEditableTextPropertyDescriptor(PROP_PLUGIN_ID, "ID"); //$NON-NLS-1$
			pds0.setCategory(M_parts.PluginDescElementObj_2);
			al.add(pds0);
			PropertyDescriptor pds1 = new TextPropertyDescriptor(PROP_PLUGIN_METHOD, M_parts.PluginDescElementObj_3);
			pds1.setCategory(M_parts.PluginDescElementObj_2);
			al.add(pds1);
//			if(((PluginDesc)plugin).getSubmitRule()==null)
//				((PluginDesc)plugin).setSubmitRule(new EventSubmitRule());
//			if(widgetObj!=null){
//				PropertyDescriptor pds2 = new SubmitRulePropertyDescriptor(PROP_PLUGIN_SUBMITRULE, "提交规则",plugin.getId(),widgetObj.getWidget(),((PluginDesc)plugin).getSubmitRule());
//				pds2.setCategory("基本");
//				al.add(pds2);
//			}
			PropertyDescriptor pds3 = new TextPropertyDescriptor(PROP_PLUGIN_CONTROL, M_parts.PluginDescElementObj_4);
			pds3.setCategory(M_parts.PluginDescElementObj_2);
			al.add(pds3);
		}
		else if(plugin instanceof PluginProxy){
			PropertyDescriptor[] pds = new PropertyDescriptor[2];
			pds[0] = new NoEditableTextPropertyDescriptor(PROP_PLUGIN_ID, "ID"); //$NON-NLS-1$
			pds[0].setCategory(M_parts.PluginDescElementObj_2);
			delegatedViews = windowObj.getWindow().getViews();
			List<String> viewList = new ArrayList<String>();
			if(delegatedViews!=null){
				for(LfwView view:delegatedViews){
					viewList.add(view.getId());
				}
			}
			pds[1] = new ComboBoxPropertyDescriptor(PROP_PLUGIN_VIEW, PROXYED_VIEW,viewList.toArray(new String[0]));
			pds[1].setCategory(M_parts.PluginDescElementObj_2);
			al.addAll(Arrays.asList(pds));
			List<String> plugList = new ArrayList<String>();
			delegatedViewId = ((PluginProxy) plugin).getDelegatedViewId();
			if(delegatedViewId!=null){
				PluginDesc[] plugins = windowObj.getWindow().getView(delegatedViewId).getPluginDescs();
				for(PluginDesc pluginItem:plugins){
					plugList.add(pluginItem.getId());
				}
			}
			delegatedPluginIds = plugList.toArray(new String[0]);
			PropertyDescriptor pd2 = new ComboBoxPropertyDescriptor(PROP_PLUGIN_VIEWPLUGINID, PROXYED_VIEW_PLUGIN,delegatedPluginIds);
			pd2.setCategory(M_parts.PluginDescElementObj_2);
			al.add(pd2);				
		
		}
//		pds[1] = new ObjectComboPropertyDescriptor(PROP_PLUGIN_TYPE, "类型", Constant.PLUGINTYPE);
//		pds[1].setCategory("基本");
//		pds[2] = new SubmitRulePropertyDescriptor(PROP_PLUGIN_SUBMITRULE, "提交规则", widgetObj.getWidget().getPagemeta(), plugin.getSubmitRule());
//		pds[2].setCategory("基本");
		
		return al.toArray(new IPropertyDescriptor[0]);
	}

	public void setPropertyValue(Object id, Object value) {
		if (PROP_PLUGIN_ID.equals(id)) {
			plugin.setId((String)value);
			getFigure().getTitleFigure().getChildren().remove(1);
			getFigure().setTitleText((String)value, (String)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			wdEditor.setDirtyTrue();
		} 
		else if (PROP_PLUGIN_METHOD.equals(id)) {
			((PluginDesc)plugin).setMethodName((String)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			if(wdEditor!=null){
				wdEditor.setDirtyTrue();
			}
			else{
				RelationEditor rlEditor = RelationEditor.getActiveRelationEditor();
				rlEditor.setDirtyTrue();
			}
			
		}
		else if (PROP_PLUGIN_SUBMITRULE.equals(id)) {
			((PluginDesc)plugin).setSubmitRule((EventSubmitRule)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			wdEditor.setDirtyTrue();
		}
		else if (PROP_PLUGIN_CONTROL.equals(id)) {
			((PluginDesc)plugin).setControllerClazz((String)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			if(wdEditor!=null){
				wdEditor.setDirtyTrue();
			}
			else{
				RelationEditor rlEditor = RelationEditor.getActiveRelationEditor();
				rlEditor.setDirtyTrue();
			}
		}
		else if(PROP_PLUGIN_VIEW.equals(id)){
			delegatedViewId = delegatedViews[Integer.parseInt(value.toString())].getId();
			((PluginProxy)plugin).setDelegatedViewId(delegatedViewId);
			RelationEditor wdEditor = RelationEditor.getActiveRelationEditor();
			wdEditor.setDirtyTrue();
//			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//			wdEditor.setDirtyTrue();
			
		}
		else if(PROP_PLUGIN_VIEWPLUGINID.equals(id)){
			String pluginId = delegatedPluginIds[Integer.parseInt(value.toString())];
			((PluginProxy)plugin).setDelegatedPluginId(pluginId);
			RelationEditor wdEditor = RelationEditor.getActiveRelationEditor();
			wdEditor.setDirtyTrue();
		}
//		else if (PROP_PLUGIN_TYPE.equals(id)){
//			plugin.setPluginType((String) value);
//			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//			wdEditor.setDirtyTrue();
//		}
	}

	public Object getPropertyValue(Object id) {
		if (PROP_PLUGIN_ID.equals(id)) {
			return plugin.getId() == null ? "" : plugin.getId(); //$NON-NLS-1$
		}
		else if (PROP_PLUGIN_METHOD.equals(id)) {
			return ((PluginDesc)plugin).getMethodName() == null ? "" : ((PluginDesc)plugin).getMethodName(); //$NON-NLS-1$
		}
//		else if (PROP_PLUGIN_SUBMITRULE.equals(id)) {
//			return ((PluginDesc)plugin).getSubmitRule() == null ? "" : ((PluginDesc)plugin).getSubmitRule();
//		}
		else if (PROP_PLUGIN_CONTROL.equals(id)) {
			return ((PluginDesc)plugin).getControllerClazz() == null ? "" : ((PluginDesc)plugin).getControllerClazz(); //$NON-NLS-1$
		}
		else if (PROP_PLUGIN_VIEW.equals(id)) {
			if(((PluginProxy)plugin).getDelegatedViewId() == null )
				return -1;
			int i = 0;
			for(LfwView view:delegatedViews){
				if(((PluginProxy)plugin).getDelegatedViewId().equals(view.getId()))
					return i;
				i++;
			}
			return -1;
//			return ((PluginProxy)plugin).getDelegatedView() == null ? 0 : ((PluginProxy)plugin).getDelegatedView().getId();
		}
		else if (PROP_PLUGIN_VIEWPLUGINID.equals(id)) {
			if(((PluginProxy)plugin).getDelegatedPlugin() == null)
				return -1;
			int i = 0;
			for(String pluginid:delegatedPluginIds){
				if(pluginid.equals(((PluginProxy)plugin).getDelegatedPlugin().getId()))
					return i;
				i++;
			}
			return -1;
		}
		return null;
	}

	public IPluginDesc getPlugin() {
		if (plugin == null)
			plugin = new PluginDesc(); 
		return plugin;
	}

	public void setPlugin(IPluginDesc plugin) {
		this.plugin = plugin;
	}

	public PluginDescElementFigure getFigure() {
		return figure;
	}

	public void setFigure(PluginDescElementFigure figure) {
		this.figure = figure;
	}

	
	public WebElement getWebElement() {
		return null;
	}

	public WidgetElementObj getWidgetObj() {
		return widgetObj;
	}

	public void setWidgetObj(WidgetElementObj widgetObj) {
		this.widgetObj = widgetObj;
	}
	
	public WindowObj getWindowObj() {
		return windowObj;
	}

	public void setWindowObj(WindowObj windowObj) {
		this.windowObj = windowObj;
	}

	public ChildConnection getConn() {
		return conn;
	}

	public void setConn(ChildConnection conn) {
		this.conn = conn;
	}

	
//	public String getRefId() {
//		return refId;
//	}
//
//	public void setRefId(String refId) {
//		this.refId = refId;
//	}

}
