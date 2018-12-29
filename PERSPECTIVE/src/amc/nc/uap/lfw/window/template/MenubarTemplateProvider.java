package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.EventConf;

public final class MenubarTemplateProvider {
	/**
	 * 弹出卡片菜单
	 * @return
	 */
	public static MenubarComp createCardMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("新增");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("copy");
		item.setText("复制");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onCopy");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("删除");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onDelete");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("save");
		item.setText("保存");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onSave");
		item.addEventConf(event);
		menubar.addMenuItem(item);

		
		item = new MenuItem();
		item.setId("sep1");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("startorstrop");
		item.setText("启用/停用");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("start");
		item1.setText("启用");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStart");
		item1.addEventConf(event);
		item.addMenuItem(item1);
		
		MenuItem item2 = new MenuItem();
		item2.setId("stop");
		item2.setText("停用");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStop");
		item2.addEventConf(event);
		item.addMenuItem(item2);
		

		item = new MenuItem();
		item.setId("sep2");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("attachfile");
		item.setText("附件");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("打印");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onPrint");
		item.addEventConf(event);
		menubar.addMenuItem(item);

		
		item = new MenuItem();
		item.setId("sep3");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("back");
		item.setText("返回");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onBack");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * 弹出卡片流程菜单
	 * @return
	 */
	public static MenubarComp createWfCardMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
                            
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("新增");
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("copy");
		item.setText("复制");
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onCopy");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("删除");
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onDelete");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		
		item = new MenuItem();
		item.setId("sep1");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("打印");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onPrint");
		item.addEventConf(event);
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmPrintStateManager");
		menubar.addMenuItem(item);

		
		item = new MenuItem();
		item.setId("sep3");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("back");
		item.setText("返回");
//		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onBack");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * 列表菜单
	 * @return
	 */
	public static MenubarComp createListMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("新增");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Init_Ss_Ms_StateManager");
		menubar.addMenuItem(item);

		item = new MenuItem();
		item.setId("edit");
		item.setText("修改");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onEdit");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("删除");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onDelete");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);

		
		item = new MenuItem();
		item.setId("sep1");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("startorstrop");
		item.setText("启用/停用");
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("start");
		item1.setText("启用");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStart");
		item1.addEventConf(event);
		item1.setStateManager("nc.uap.lfw.core.bm.dft.StartStopStateManager");
		item.addMenuItem(item1);
		
		MenuItem item2 = new MenuItem();
		item2.setId("stop");
		item2.setText("停用");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStop");
		item2.addEventConf(event);
		item2.setStateManager("nc.uap.lfw.core.bm.dft.StartStopStateManager");
		item.addMenuItem(item2);

		
		item = new MenuItem();
		item.setId("sep2");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("attachfile");
		item.setText("附件");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("打印");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onPrint");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * 列表流程菜单
	 * @return
	 */
	public static MenubarComp createWfListMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("新增");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Init_Ss_Ms_StateManager");
		menubar.addMenuItem(item);

		item = new MenuItem();
		item.setId("edit");
		item.setText("修改");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onEdit");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("删除");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onDelete");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);

		
		item = new MenuItem();
		item.setId("sep1");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("attachfile");
		item.setText("附件");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("打印");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onPrint");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		
		item = new MenuItem();
		item.setId("sep2");
		item.setSep(true);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("wf");
		item.setText("流程");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("wflowing");
		item1.setText("流程进度");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onFlow");
		item1.addEventConf(event);
		item1.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		item.addMenuItem(item1);
      
		return menubar;
	}
	
}
