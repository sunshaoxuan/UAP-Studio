package nc.lfw.editor.menubar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ViewRule;

/**
 * 默认菜单项
 * 
 * @author guoweic
 *
 */
public abstract class DefaultItem {
	
	private String cmdId = "";
	
	private String id = "";
	
	private String text = "";
	
	private String superClazz = "";
	
//	private String operatorStatusArray = "";
//
//	private String businessStatusArray = "";
	
	private List<DefaultItem> childList = null;

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}
	
	/**
	 * 将DefaultItem对象转换为MenuItem对象
	 */
	public MenuItem generateMenuItem() {
		MenuItem item = new MenuItem();
		item.setText(text);
		item.setId(id);
//		item.setOperatorStatusArray(operatorStatusArray);
//		item.setBusinessStatusArray(businessStatusArray);
		if (null != childList) {
			for (DefaultItem childDefaultItem : childList) {
				// 增加子菜单
				MenuItem childItem = childDefaultItem.generateMenuItem();
				item.addMenuItem(childItem);
			}
		} else {
			item.setChildList(new ArrayList<MenuItem>());
		}
		return item;
	}
	
	/**
	 * 将DefaultItem对象转换为MenuItem对象（仅为图片，不包含文字）
	 * @param imgIcon
	 * @param imgIconOn
	 * @param imgIconDisable
	 * @return
	 */
	public MenuItem generateImgMenuItem(String imgIcon, String imgIconOn, String imgIconDisable) {
		MenuItem item = new MenuItem();
		item.setId(id);
		item.setTip(text);
		item.setImgIcon(imgIcon);
		item.setImgIconOn(imgIconOn);
		item.setImgIconDisable(imgIconDisable);
//		item.setOperatorStatusArray(operatorStatusArray);
//		item.setBusinessStatusArray(businessStatusArray);
		if (null != childList) {
			for (DefaultItem childDefaultItem : childList) {
				// 增加子菜单
				MenuItem childItem = childDefaultItem.generateMenuItem();
				item.addMenuItem(childItem);
			}
		} else {
			item.setChildList(new ArrayList<MenuItem>());
		}
		return item;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public List<DefaultItem> getChildList() {
		return childList;
	}

	public void setChildList(List<DefaultItem> childList) {
		this.childList = childList;
	}
	
	public void addChild(DefaultItem defaultItem) {
		if (null == childList)
			childList = new ArrayList<DefaultItem>();
		if (!childList.contains(defaultItem))
			childList.add(defaultItem);
	}
	
	/**
	 * 生成提交规则
	 * @param widgetIdMap
	 * @param dsIdMap
	 * @return
	 */
	public EventSubmitRule generateSubmitRule(Map<String, String> widgetIdMap, Map<String, String> dsIdMap) {
		if(widgetIdMap.size() > 0) {
			EventSubmitRule sr = new EventSubmitRule();
			for (String widgetParamName : widgetIdMap.keySet()) {
				String widgetId = widgetIdMap.get(widgetParamName);
				ViewRule wr = new ViewRule();
				wr.setId(widgetId);
				processWidgetRule(wr);
				if (dsIdMap.size() > 0) {
					for (String dsParamName : dsIdMap.keySet()) {
						DatasetRule dsr = new DatasetRule();
						String dsId = dsIdMap.get(dsParamName);
						dsr.setId(dsId);
						dsr.setType(DatasetRule.TYPE_CURRENT_PAGE);
						processDatasetRule(dsr);
						wr.addDsRule(dsr);
					}
				}
				sr.addViewRule(wr);
			}
			return sr;
		}
		return null;
	}
	
	/**
	 * 生成提交规则
	 * @param widgetId
	 * @param dsIds
	 * @return
	 */
	public EventSubmitRule generateSubmitRule(String widgetId, String masterDsId, List<String> slaveDsIds, Map<String, String> tabiddsMap) {
		EventSubmitRule sr = new EventSubmitRule();
		ViewRule wr = new ViewRule();
		wr.setId(widgetId);
		processWidgetRule(wr);
		DatasetRule mdsr = new DatasetRule();
		mdsr.setId(masterDsId);
		mdsr.setType(DatasetRule.TYPE_CURRENT_PAGE);
		processDatasetRule(mdsr);
		wr.addDsRule(mdsr);
		if (slaveDsIds.size() > 0) {
			for (String slaveDsId : slaveDsIds) {
				DatasetRule dsr = new DatasetRule();
				dsr.setId(slaveDsId);
				dsr.setType(DatasetRule.TYPE_CURRENT_PAGE);
				processDatasetRule(dsr);
				wr.addDsRule(dsr);
			}
		}
		sr.addViewRule(wr);
		return sr;
	}
	
	protected void processDatasetRule(DatasetRule dsr) {
	}
	
	protected void processWidgetRule(ViewRule rule) {
		
	}

	public String getSuperClazz() {
		return superClazz;
	}

	public void setSuperClazz(String superClazz) {
		this.superClazz = superClazz;
	}
//
//	public String getOperatorStatusArray() {
//		return operatorStatusArray;
//	}
//
//	public void setOperatorStatusArray(String operatorStatusArray) {
//		this.operatorStatusArray = operatorStatusArray;
//	}
//
//	public String getBusinessStatusArray() {
//		return businessStatusArray;
//	}
//
//	public void setBusinessStatusArray(String businessStatusArray) {
//		this.businessStatusArray = businessStatusArray;
//	}
	
	/**
	 * 按钮项增加后执行的方法
	 */
	public abstract void afterAdd();
	
	/**
	 * 按钮项删除后执行的方法
	 */
	public abstract void afterDelete();
	
	
}
