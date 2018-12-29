package nc.uap.lfw.perspective.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.lfw.editor.common.NoEditableTextPropertyDescriptor;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.IRefDataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.data.RefMdDataset;
import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.editor.DataSetEditor;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


/**
 * Dataset 组件
 * @author zhangxya
 *
 */
public class DatasetElementObj extends LfwElementObjWithGraph{
	private static final long serialVersionUID = 6253081418703115641L;
	private boolean isDatasets;
	private String orginaldsId = null;
	private Dataset ds;
	private Boolean extendFlag;
	private String refdatasetid;
//	private boolean isPoolds = false;
	public static final String PROP_CHILD_ADD = "prop_child_add"; //$NON-NLS-1$
	public static final String PROP_CHILD_REMOVE = "prop_child_remove"; //$NON-NLS-1$
	private List<RefDatasetElementObj> refdss = new ArrayList<RefDatasetElementObj>();
	public static final String PROP_ISLAZY = "element_ISLAZY"; //$NON-NLS-1$
	public static final String PROP_ID = "element_ID"; //$NON-NLS-1$
	public static final String PROP_VOMETA = "element_VOMETA"; //$NON-NLS-1$
//	public static final String PROP_CONTROLOPERATESTATUS = "element_CONTROLOPERATESTATUS";
	public static final String PROP_CONTROLOPWIDGETSTATUS = "element_CONTROLOPWIDGETSTATUS"; //$NON-NLS-1$
	public static final String PROP_OBJMETA = "element_OBJMETA"; //$NON-NLS-1$
	public static final String PROP_ENABLED = "element_ENABLED"; //$NON-NLS-1$
	public static final String PROP_PAGESIZE = "element_PAGESIZE"; //$NON-NLS-1$
	public static final String PROP_CAPTION = "element_CAPTION"; //$NON-NLS-1$
//	public static final String PROP_OPERATORSTATUSARRY = "element_operatorStatusArray";
	public static final String PROP_UPDATE_CELL_PROPS = "update_cell_props"; //$NON-NLS-1$
	public static final String PROP_ADD_CELL_PROPS = "add_cell_props"; //$NON-NLS-1$
	public static final String PROP_ADD_FIELDRELATION_PROPS = "add_fieldrelation_props"; //$NON-NLS-1$
	public static final String PROP_DEL_FIELDRELATION_PROPS = "delete_fieldrelation_props"; //$NON-NLS-1$
	private List<Field> props = new ArrayList<Field>();
	public static final String PROP_DEL_ALLFIELDRELATION_PROPS = "delete_allfieldrelation_props"; //$NON-NLS-1$
	public static final String PROP_NOTNULLBODY = "element_NOTNULLBODY"; //$NON-NLS-1$

//	public boolean isPoolds() {
//		return isPoolds;
//	}
//
//	public void setPoolds(boolean isPoolds) {
//		this.isPoolds = isPoolds;
//	}

	public boolean isDatasets() {
		return isDatasets;
	}

	public void setDatasets(boolean isDatasets) {
		this.isDatasets = isDatasets;
	}
	
	public Boolean getExtendFlag() {
		return extendFlag;
	}

	public void setExtendFlag(Boolean extendFlag) {
		this.extendFlag = extendFlag;
	}

	
	public String getOrginaldsId() {
		return orginaldsId;
	}

	public void setOrginaldsId(String orginaldsId) {
		this.orginaldsId = orginaldsId;
	}

	public DatasetElementObj(String orginaldsId){
		super();
		this.orginaldsId = orginaldsId;
	}
	

	public Dataset getDs() {
		return ds;
	}
	
		
	public Boolean getChange() {
		return change;
	}

	public void setChange(Boolean change) {
		this.change = change;
	}

	public static final String PROP_DATASET_ELEMENT ="dataset_element"; //$NON-NLS-1$
	public void setDs(Dataset ds) {
		this.ds = ds;
		fireStructureChange(PROP_DATASET_ELEMENT,  ds);
	}
	
	public String getRefdatasetid() {
		return refdatasetid;
	}

	public void setRefdatasetid(String refdatasetid) {
		this.refdatasetid = refdatasetid;
	}

	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList<IPropertyDescriptor> al = new ArrayList<IPropertyDescriptor>();
		al.addAll(Arrays.asList(super.getPropertyDescriptors()));
		NoEditableTextPropertyDescriptor idDesc = new NoEditableTextPropertyDescriptor(PROP_ID, "ID"); //$NON-NLS-1$
		idDesc.setCategory(M_perspective.DatasetElementObj_0);
		al.add(idDesc);
		
		if(!(ds instanceof MdDataset) && !(ds instanceof RefMdDataset)){
			TextPropertyDescriptor voMetaDesc = new TextPropertyDescriptor(PROP_VOMETA, M_perspective.DatasetElementObj_1);
			voMetaDesc.setCategory(M_perspective.DatasetElementObj_0);
			al.add(voMetaDesc);
		}
		
		//caption
		TextPropertyDescriptor captionDes = new TextPropertyDescriptor(PROP_CAPTION,M_perspective.DatasetElementObj_2);
		captionDes.setCategory(M_perspective.DatasetElementObj_0);
		al.add(captionDes);
		
		if(!(ds instanceof IRefDataset)){
			ComboBoxPropertyDescriptor isLazyDes = new ComboBoxPropertyDescriptor(PROP_ISLAZY,M_perspective.DatasetElementObj_3, Constant.ISLAZY);
			isLazyDes.setCategory(M_perspective.DatasetElementObj_0);
			al.add(isLazyDes);
	//			OperateStatusPropertyDescriptor opeDes = new OperateStatusPropertyDescriptor(PROP_OPERATORSTATUSARRY ,"可用状态数组");
	//			opeDes.setCategory("基本");
	//			al.add(opeDes);
			ComboBoxPropertyDescriptor isEditableDes = new ComboBoxPropertyDescriptor(PROP_ENABLED,M_perspective.DatasetElementObj_4, Constant.ISLAZY);
			isEditableDes.setCategory(M_perspective.DatasetElementObj_0);
			al.add(isEditableDes);
			TextPropertyDescriptor pageDes = new TextPropertyDescriptor(PROP_PAGESIZE,M_perspective.DatasetElementObj_5);
			pageDes.setCategory(M_perspective.DatasetElementObj_0);
			al.add(pageDes);
	//			ComboBoxPropertyDescriptor isControlOpeDes = new ComboBoxPropertyDescriptor(PROP_CONTROLOPERATESTATUS,"是否控制页面操作状态", Constant.ISLAZY);
	//			isControlOpeDes.setCategory("基本");
	//			al.add(isControlOpeDes);
			
			ComboBoxPropertyDescriptor isControlWidgetOpeDes = new ComboBoxPropertyDescriptor(PROP_CONTROLOPWIDGETSTATUS,M_perspective.DatasetElementObj_6, Constant.ISLAZY);
			isControlWidgetOpeDes.setCategory(M_perspective.DatasetElementObj_0);
			al.add(isControlWidgetOpeDes);
			ComboBoxPropertyDescriptor notNullBody = new ComboBoxPropertyDescriptor(PROP_NOTNULLBODY,M_perspective.DatasetElementObj_7, Constant.ISYES_NO);
			notNullBody.setCategory(M_perspective.DatasetElementObj_0);
			al.add(notNullBody);
		}
		if(ds instanceof MdDataset || ds instanceof RefMdDataset){
			TextPropertyDescriptor objDesc = new TextPropertyDescriptor(PROP_OBJMETA, M_perspective.DatasetElementObj_8);
			objDesc.setCategory(M_perspective.DatasetElementObj_0);
			al.add(objDesc);
		}
		return al.toArray(new IPropertyDescriptor[0]);
	}

	private Boolean change = false;
	
	public void setPropertyValue(Object id, Object value) {
		WebElement webele = getWebElement();
		if(!canChange(webele))
			return;
		if(PROP_ID.equals(id)){
			ds.setId((String)value);
			if(orginaldsId != null && value != null && !orginaldsId.equals(value.toString())){
				change = true;
			}else
				change = false;
			DataSetEditor.getActiveEditor().refreshTreeItem(ds);
		}
		else if(PROP_ISLAZY.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ds.setLazyLoad(truevalue);
		}
		else if(PROP_CAPTION.equals(id)){
			String oldValue = ds.getCaption();
			if((oldValue == null && value != null) || (oldValue != null && value == null) || (oldValue != null && value != null && !oldValue.equals(value))){
				ds.setCaption((String)value);
				DataSetEditor.getActiveEditor().refreshTreeItemText(ds);
			}
		}
//		else if(PROP_CONTROLOPERATESTATUS.equals(id)){
//			boolean truevalue = false;
//			if((Integer)value == 0)
//				truevalue = true;
//			if(truevalue)
//				controlOperate();
//		}
		else if(PROP_CONTROLOPWIDGETSTATUS.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ds.setControlwidgetopeStatus(truevalue);
			if(truevalue)
				controlOperate();
		}
		else if(PROP_ENABLED.equals(id)){
			boolean truevalue = false;
			if((Integer)value == 0)
				truevalue = true;
			ds.setEnabled(truevalue);
		}
		else if(PROP_VOMETA.equals(id)){
			ds.setVoMeta((String)value);
			fireStructureChange(PROP_DATASET_ELEMENT,  ds);
		}
		else if(PROP_PAGESIZE.equals(id)){
			int intVal = LfwCommonTool.convertStrToInt((String)value); 
			if(intVal < -1)
				return;
			ds.setPageSize(intVal);
		}
		else if(PROP_OBJMETA.equals(id)){
			if(ds instanceof MdDataset){
				MdDataset mdds = (MdDataset) ds;
				mdds.setObjMeta((String)value);
			}
//			else if(ds instanceof RefMdDataset){
//				RefMdDataset mdds = (RefMdDataset) ds;
//				mdds.setObjMeta((String)value);
//			}
		}
//		else if(PROP_OPERATORSTATUSARRY.equals(id)){
//		}
		else if(PROP_NOTNULLBODY.equals(id)){
			ds.setNotNullBody((Integer)value == 0);
		}
	}
	
	/**
	 * 如果控制操作状态设置为true,则执行此方法
	 */
	private void controlOperate(){
//		Map<String, JsListenerConf> listenerMap = ds.getListenerMap();
//		boolean success = false;
//		DatasetListener dsListener = null;
//		for (Iterator<String> itwd = listenerMap.keySet().iterator(); itwd.hasNext();) {
//			String listenerId = (String) itwd.next();
//			JsListenerConf listener = listenerMap.get(listenerId);
//			if(listener instanceof DatasetListener){
//				dsListener = (DatasetListener) listener;
//				break;
//			}
////			String server = listener.getServerClazz();
////			try {
////				Class serverClass = Class.forName(server);
////				Class defaultClass = Class.forName("nc.uap.lfw.core.event.deft.DefaultDatasetServerListener");
////				if(serverClass.isAssignableFrom(defaultClass) && listener instanceof DatasetListener){
////					dsListener = (DatasetListener) listener;
////					break;
////				}	
////			} catch (ClassNotFoundException e) {
////				MainPlugin.getDefault().logError(e);
////			}
//		}
//		if(dsListener != null){
//			Map<String, EventHandlerConf>  eventMap = dsListener.getEventHandlerMap();
//			for (Iterator<String> it = eventMap.keySet().iterator(); it.hasNext();) {
//				String eventId = it.next();
//				if(eventId.equals("onAfterRowSelect")){
//					EventHandlerConf event = eventMap.get(eventId);
//					if(event.isOnserver()){
//						success = true;
//						break;
//					}
//				}
//			}
//		}
//		//新建Listener
//		else{
//			LfwWidget widget = LFWPersTool.getCurrentWidget();
//			dsListener = new DatasetListener();
//			EventHandlerConf eventHandler = dsListener.getOnDataLoadEvent();
//			DatasetRule dsRule = new DatasetRule();
//			dsRule.setId(ds.getId());
//			dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
//			WidgetRule widgetRule = new WidgetRule();
//			widgetRule.setId(widget.getId());
//			widgetRule.addDsRule(dsRule);
//			eventHandler.getSubmitRule().addWidgetRule(widgetRule);
//			eventHandler.setOnserver(true);
//			dsListener.addEventHandler(eventHandler);
//			dsListener.setId(NewDsAction.DEFAULTLISTENER);
//			dsListener.setServerClazz(NewDsAction.DEFAULTSERVER);
//			//onAfterRowSelect
//			EventHandlerConf roweventHandler = dsListener.getOnAfterRowSelectEvent();
//			roweventHandler.getSubmitRule().addWidgetRule(widgetRule);
//			roweventHandler.setOnserver(true);
//			dsListener.addEventHandler(roweventHandler);
//			ds.addListener(dsListener);
//			success = true;
//		}
//		//如果此类没有加默认datasetLoadListener
//		if(!success){
//			LfwWidget widget = LFWPersTool.getCurrentWidget();
//			EventHandlerConf eventHandler = dsListener.getOnAfterRowSelectEvent();
//			eventHandler.setOnserver(true);
//			DatasetRule dsRule = new DatasetRule();
//			dsRule.setId(ds.getId());
//			dsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
//			WidgetRule widgetRule = new WidgetRule();
//			widgetRule.setId(widget.getId());
//			widgetRule.addDsRule(dsRule);
//			eventHandler.getSubmitRule().addWidgetRule(widgetRule);
//			dsListener.addEventHandler(eventHandler);
//			ds.addListener(dsListener);
//			ListenerElementObj listener = (ListenerElementObj) this.getGraph().getCells().get(1);
//			if(listener != null)
//				listener.getFigure().refreshListeners();
//		}
	}
	
	public Object getPropertyValue(Object id) {
		if(PROP_ID.equals(id))
			return ds.getId() == null?"":ds.getId(); //$NON-NLS-1$
		else if(PROP_PAGESIZE.equals(id))
			return ds.getPageSize() == -1 ?"-1" : String.valueOf(ds.getPageSize()); //$NON-NLS-1$
		else if(PROP_CAPTION.equals(id))
			return ds.getCaption() == null?"":ds.getCaption(); //$NON-NLS-1$
		else if(PROP_ISLAZY.equals(id))
			return (ds.isLazyLoad() == true)? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_ENABLED.equals(id))
			return (ds.isEnabled() == true)? Integer.valueOf(0):Integer.valueOf(1);
//		else if(PROP_CONTROLOPERATESTATUS.equals(id))
//			return ds.isControloperatorStatus() == true? new Integer(0): new Integer(1);
		else if(PROP_CONTROLOPWIDGETSTATUS.equals(id))
			return ds.isControlwidgetopeStatus() == true? Integer.valueOf(0):Integer.valueOf(1);
		else if(PROP_VOMETA.equals(id))
			return ds.getVoMeta() == null?"":ds.getVoMeta(); //$NON-NLS-1$
		else if(PROP_OBJMETA.equals(id)){
			if(ds instanceof MdDataset){
				MdDataset mdds = (MdDataset)ds;
				return mdds.getObjMeta() == null? "": mdds.getObjMeta(); //$NON-NLS-1$
			}
			if(ds instanceof RefMdDataset){
				RefMdDataset mdds = (RefMdDataset)ds;
				return mdds.getObjMeta() == null? "": mdds.getObjMeta(); //$NON-NLS-1$
			}
			else return ""; //$NON-NLS-1$
		}
		else if(PROP_NOTNULLBODY.equals(id)){
			return ds.isNotNullBody()? Integer.valueOf(0):Integer.valueOf(1);
		}
//		else if(PROP_OPERATORSTATUSARRY.equals(id)){
//			return ds.getOperatorStatusArray() == null?"":ds.getOperatorStatusArray();
//		}
		else return null;	
	}
	
	public boolean addRefDataset(RefDatasetElementObj cell) {
		cell.setDsEle(this);
		boolean b = refdss.add(cell);
		if (b) {
			fireStructureChange(PROP_CHILD_ADD, cell);
		}
		return b;
	}

	public boolean removeCell(RefDatasetElementObj cell) {
		boolean b = refdss.remove(cell);
		cell.setDsEle(null);
		if (b) {
			fireStructureChange(PROP_CHILD_REMOVE, cell);
		}
		return b;
	}
	
	public List<RefDatasetElementObj> getCells() {
		return refdss;
	}

	
	public void addFieldRelation(FieldRelation fr){
		fireStructureChange(PROP_ADD_FIELDRELATION_PROPS, fr);
	}
	
	
	public void deleteFieldRelation(FieldRelation fr){
		fireStructureChange(PROP_DEL_FIELDRELATION_PROPS, fr);
	}

	
	public void deleteAllFR(DatasetElementObj dsobj){
		fireStructureChange(PROP_DEL_ALLFIELDRELATION_PROPS, dsobj);
	}
	
	public void firePropUpdate(Field prop){
		fireStructureChange(PROP_UPDATE_CELL_PROPS, prop);
	}

	
	public void addProp(Field prop){
		props.add(prop);
		fireStructureChange(PROP_ADD_CELL_PROPS, prop);
	}
	public WebElement getWebElement() {
		return ds;
	}
}
