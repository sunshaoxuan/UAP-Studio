package nc.lfw.editor.widget.plug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.lfw.editor.common.SubmitRulePropertyDescriptor;
import nc.lfw.editor.pagemeta.RelationEditor;
import nc.lfw.editor.widget.WidgetEditor;
import nc.lfw.editor.widget.WidgetElementObj;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.editor.window.WindowObj;
import nc.uap.lfw.lang.M_parts;
import nc.uap.lfw.palette.ChildConnection;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * PlugoutDesc元素对象
 * @author dingrf
 *
 */
public class PlugoutDescElementObj extends LfwElementObjWithGraph {

	private static final long serialVersionUID = -5675125816072493807L;
	
	private WidgetElementObj widgetObj;
	
	private WindowObj windowObj;
	// PlugoutDesc对象
	private IPlugoutDesc plugout;
	// 当前图像
	private PlugoutDescElementFigure figure;
	
	private LfwView[] delegatedViews;
	
	private String delegatedViewId;
	
	private String[] delegatedPlugoutIds;
	//连接线
	private ChildConnection conn;

	public static final String PROP_PLUGOUT_ID = "prop_plugout_id"; //$NON-NLS-1$

	public static final String PROP_PLUGOUT_METHOD = "prop_plugout_method"; //$NON-NLS-1$
	public static final String PROP_PLUGOUT_SUBMITRULE = "prop_plugout_submitrule"; //$NON-NLS-1$
	public static final String PROP_PLUGOUT_CONTROL = "prop_plugout_control"; //$NON-NLS-1$
	public static final String PROP_PLUGOUT_VIEW = "prop_plugout_view"; //$NON-NLS-1$
	public static final String PROP_PLUGOUT_VIEWPLUGOUTID = "prop_plugout_viewplugoutid"; //$NON-NLS-1$
	
//	public static final String PROP_PLUGOUT_TYPE = "prop_plugout_type";
	

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		if(plugout instanceof PlugoutDesc){
//			PropertyDescriptor[] pds = new PropertyDescriptor[2];
			PropertyDescriptor pds0 = new NoEditableTextPropertyDescriptor(PROP_PLUGOUT_ID, "ID"); //$NON-NLS-1$
			pds0.setCategory(M_parts.PlugoutDescElementObj_0);
			al.add(pds0);
//			if(((PlugoutDesc)plugout).getSubmitRule()==null)
//				((PlugoutDesc)plugout).setSubmitRule(new EventSubmitRule());
//			if(widgetObj!=null){
//				PropertyDescriptor pds1 = new SubmitRulePropertyDescriptor(PROP_PLUGOUT_SUBMITRULE, "提交规则",plugout.getId(),widgetObj.getWidget(),((PlugoutDesc)plugout).getSubmitRule());
//				pds1.setCategory("基本");
//				al.add(pds1);
//			}
		}
		else if(plugout instanceof PlugoutProxy){
			PropertyDescriptor[] pds = new PropertyDescriptor[2];
			pds[0] = new NoEditableTextPropertyDescriptor(PROP_PLUGOUT_ID, "ID"); //$NON-NLS-1$
			pds[0].setCategory(M_parts.PlugoutDescElementObj_0);
			delegatedViews = windowObj.getWindow().getViews();
			List<String> viewList = new ArrayList<String>();
			if(delegatedViews!=null){
				for(LfwView view:delegatedViews){
					viewList.add(view.getId());
				}
			}
			pds[1] = new ComboBoxPropertyDescriptor(PROP_PLUGOUT_VIEW, M_parts.PlugoutDescElementObj_1,viewList.toArray(new String[0]));
			pds[1].setCategory(M_parts.PlugoutDescElementObj_0);
			al.addAll(Arrays.asList(pds));
			List<String> plugList = new ArrayList<String>();
			delegatedViewId = ((PlugoutProxy) plugout).getDelegatedViewId();
			if(delegatedViewId!=null){
				PlugoutDesc[] plugouts = windowObj.getWindow().getView(delegatedViewId).getPlugoutDescs();
				for(PlugoutDesc plugoutItem:plugouts){
					plugList.add(plugoutItem.getId());
				}
			}
			delegatedPlugoutIds = plugList.toArray(new String[0]);
			PropertyDescriptor pd2 = new ComboBoxPropertyDescriptor(PROP_PLUGOUT_VIEWPLUGOUTID, M_parts.PlugoutDescElementObj_2,delegatedPlugoutIds);
			pd2.setCategory(M_parts.PlugoutDescElementObj_0);
			al.add(pd2);
		}
		
		return al.toArray(new IPropertyDescriptor[0]);
	}

	public void setPropertyValue(Object id, Object value) {
		if (PROP_PLUGOUT_ID.equals(id)) {
			plugout.setId((String)value);
			getFigure().getTitleFigure().getChildren().remove(1);
			getFigure().setTitleText((String)value, (String)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			wdEditor.setDirtyTrue();
		} 
		else if (PROP_PLUGOUT_SUBMITRULE.equals(id)){
			((PlugoutDesc)plugout).setSubmitRule((EventSubmitRule)value);
			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
			wdEditor.setDirtyTrue();
		}
		else if(PROP_PLUGOUT_VIEW.equals(id)){
			delegatedViewId = delegatedViews[Integer.parseInt(value.toString())].getId();
			((PlugoutProxy)plugout).setDelegatedViewId(delegatedViewId);
			RelationEditor wdEditor = RelationEditor.getActiveRelationEditor();
			wdEditor.setDirtyTrue();
//			WidgetEditor wdEditor = WidgetEditor.getActiveWidgetEditor();
//			wdEditor.setDirtyTrue();
			
		}
		else if(PROP_PLUGOUT_VIEWPLUGOUTID.equals(id)){
			String plugoutId = delegatedPlugoutIds[Integer.parseInt(value.toString())];
			((PlugoutProxy)plugout).setDelegatedPlugoutId(plugoutId);
			RelationEditor wdEditor = RelationEditor.getActiveRelationEditor();
			wdEditor.setDirtyTrue();
		}
	}

	public Object getPropertyValue(Object id) {
		if (PROP_PLUGOUT_ID.equals(id)) {
			return plugout.getId() == null ? "" : plugout.getId(); //$NON-NLS-1$
		}
//		else if (PROP_PLUGOUT_SUBMITRULE.equals(id)){
//			return ((PlugoutDesc)plugout).getSubmitRule() == null ? "" : ((PlugoutDesc)plugout).getSubmitRule();
//		}
		else if (PROP_PLUGOUT_VIEW.equals(id)) {
			if(((PlugoutProxy)plugout).getDelegatedViewId() == null )
				return -1;
			int i = 0;
			for(LfwView view:delegatedViews){
				if(((PlugoutProxy)plugout).getDelegatedViewId().equals(view.getId()))
					return i;
				i++;
			}
			return -1;
//			return ((PluginProxy)plugin).getDelegatedView() == null ? 0 : ((PluginProxy)plugin).getDelegatedView().getId();
		}
		else if (PROP_PLUGOUT_VIEWPLUGOUTID.equals(id)) {
			if(((PlugoutProxy)plugout).getDelegatedPlugout() == null)
				return -1;
			int i = 0;
			for(String plugoutid:delegatedPlugoutIds){
				if(plugoutid.equals(((PlugoutProxy)plugout).getDelegatedPlugout().getId()))
					return i;
				i++;
			}
			return -1;
		}
		return null;
	}

//	public void setElementAttribute(Element ele) {
//		ele.setAttribute("id", getId());
//	}
//
//	public Object getEditableValue() {
//		return this;
//	}
//
//	public Element createElement(Document doc) {
//
//		return null;
//	}

	public IPlugoutDesc getPlugout() {
		if (plugout == null)
			plugout = new PlugoutDesc(); 
		return plugout;
	}

	public void setPlugout(IPlugoutDesc plugout) {
		this.plugout = plugout;
	}

	public PlugoutDescElementFigure getFigure() {
		return figure;
	}

	public void setFigure(PlugoutDescElementFigure figure) {
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
