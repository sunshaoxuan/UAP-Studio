package nc.uap.lfw.toolbar;

import java.util.List;

import nc.lfw.editor.common.LfwBaseGraph;
import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.ToolBarItem;

public class ToolBarGraph extends LfwBaseGraph {
	private static final long serialVersionUID = -2154903434383093547L;
		
	public ToolBarGraph() {
		super();
	}
	
	/**
	 * 取消所有图形的所有子项选中状态
	 */
	public void unSelectAllLabels() {
		super.unSelectAllLabels();
		//toolbar
		List<LfwElementObjWithGraph> menuCells = getCells();
		for (int i = 0, n = menuCells.size(); i < n; i++) {
			if (menuCells.get(i) instanceof ToolBarElementObj) {
				ToolBarElementObj toolbarObj = (ToolBarElementObj) menuCells.get(i);
				toolbarObj.getFigure().unSelectAllLabels();
			}
		}
	}
	
	
	public void reloadListenerFigure(ToolBarItem toolbarItem) {
//		ToolBarEditor.getActiveEditor().setCurrentListenerToolItem(toolbarItem);
//		ListenerElementObj listenerObj = ToolBarEditor.getActiveEditor().getJsListenerObj();
//		ListenerElementFigure figure = listenerObj.getFigure();
//		IFigure title = (IFigure) figure.getTitleFigure().getChildren().get(1);
//		figure.getTitleFigure().remove(title);
//		if (null != toolbarItem) {
//			figure.setTitleText(toolbarItem.getText(), toolbarItem.getText());
//			refreshListenerItems(figure, toolbarItem);
//		}
	}
	
	/**
	 * 刷新Listener所有子项的显示
	 * @param figure
	 * @param menuItem
	 */
//	private void refreshListenerItems(ListenerElementFigure figure, ToolBarItem toolbarItem) {
//		// 移除全部原有项
//		while (figure.getContentFigure().getChildren().size() > 0) {
//			Object child = figure.getContentFigure().getChildren().get(0);
//			if (child instanceof JsListenerLabel) {
//				figure.getContentFigure().remove((JsListenerLabel) child);
//				figure.setHeight(figure.getHeight() - figure.LINE_HEIGHT);
//			}
//			else if(child instanceof JsEventLabel){
//				figure.getContentFigure().remove((JsEventLabel) child);
//				figure.setHeight(figure.getHeight() - figure.LINE_HEIGHT);
//			}
//		}
//		// 显示所有子项
//		addItems(figure, toolbarItem);
//	}


	
	/**
	 * 显示所有子项
	 * @param figure
	 * @param menuItem
	 */
//	private void addItems(ListenerElementFigure figure, ToolBarItem toolbarItem) {
////		Map<String, JsListenerConf> map = toolbarItem.getListenerMap();
////		if(map == null || map.isEmpty()){
////			ListenerElementObj  jsListener = LFWBaseEditor.getActiveEditor().getJsListenerObj();
////			Map<String, JsListenerConf> listenerMap = new HashMap<String, JsListenerConf>();
////			jsListener.setListenerMap(listenerMap);
////		}
////		for (String key : map.keySet()) {
////			JsListenerConf listener = map.get(key);
////			String fullName = listener.getClass().toString();
////			String className = fullName.substring(fullName.lastIndexOf(".") + 1);
////			JsListenerLabel label = new JsListenerLabel("[" + className + "]" + listener.getId(), listener);
////			figure.addToContent(label);
////			figure.setHeight(figure.getHeight() + figure.LINE_HEIGHT);
////			figure.addListenerLabelListener(label);
////			
////			Map<String, EventHandlerConf> eventMap = listener.getEventHandlerMap();
////			for (String id : eventMap.keySet()) {
////				EventHandlerConf event = eventMap.get(id);
////				if (event.getScript() != null || event.isOnserver() || event.getSubmitRule() != null) {
////					JsEventLabel eventLabel = new JsEventLabel(id, event);
////					figure.addToContent(eventLabel);
////					figure.setHeight(figure.getHeight() + figure.LINE_HEIGHT);
////					figure.addEventLabelListener(listener, eventLabel);
////				}
////			}
////		}
//	}

	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		
		// TODO Auto-generated method stub
		
	}

}
