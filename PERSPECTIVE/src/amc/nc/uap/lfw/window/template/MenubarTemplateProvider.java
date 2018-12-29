package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.EventConf;

public final class MenubarTemplateProvider {
	/**
	 * ������Ƭ�˵�
	 * @return
	 */
	public static MenubarComp createCardMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("����");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("copy");
		item.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onCopy");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("ɾ��");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onDelete");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("save");
		item.setText("����");
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
		item.setText("����/ͣ��");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("start");
		item1.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStart");
		item1.addEventConf(event);
		item.addMenuItem(item1);
		
		MenuItem item2 = new MenuItem();
		item2.setId("stop");
		item2.setText("ͣ��");
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
		item.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("��ӡ");
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
		item.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onBack");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * ������Ƭ���̲˵�
	 * @return
	 */
	public static MenubarComp createWfCardMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
                            
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("����");
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("copy");
		item.setText("����");
		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onCopy");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("ɾ��");
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
		item.setText("��ӡ");
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
		item.setText("����");
//		item.setStateManager("nc.uap.wfm.exetask.statemanager.WfmEnabledStateManager");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onBack");
		item.addEventConf(event);
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * �б�˵�
	 * @return
	 */
	public static MenubarComp createListMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("����");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Init_Ss_Ms_StateManager");
		menubar.addMenuItem(item);

		item = new MenuItem();
		item.setId("edit");
		item.setText("�޸�");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onEdit");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("ɾ��");
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
		item.setText("����/ͣ��");
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("start");
		item1.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onStart");
		item1.addEventConf(event);
		item1.setStateManager("nc.uap.lfw.core.bm.dft.StartStopStateManager");
		item.addMenuItem(item1);
		
		MenuItem item2 = new MenuItem();
		item2.setId("stop");
		item2.setText("ͣ��");
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
		item.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("��ӡ");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onPrint");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		return menubar;
	}
	
	/**
	 * �б����̲˵�
	 * @return
	 */
	public static MenubarComp createWfListMenubar() {
		MenubarComp menubar = new MenubarComp();
		menubar.setId(AbstractViewCreator.MENUBAR);
		
		MenuItem item = new MenuItem();
		item.setId("add");
		item.setText("����");
		EventConf event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAdd");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Init_Ss_Ms_StateManager");
		menubar.addMenuItem(item);

		item = new MenuItem();
		item.setId("edit");
		item.setText("�޸�");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onEdit");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("del");
		item.setText("ɾ��");
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
		item.setText("����");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onAttchFile");
		item.addEventConf(event);
		item.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		menubar.addMenuItem(item);
		
		item = new MenuItem();
		item.setId("print");
		item.setText("��ӡ");
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
		item.setText("����");
		menubar.addMenuItem(item);
		
		MenuItem item1 = new MenuItem();
		item1.setId("wflowing");
		item1.setText("���̽���");
		event = MouseEvent.getOnClickEvent();
		event.setMethodName("onFlow");
		item1.addEventConf(event);
		item1.setStateManager("nc.uap.lfw.core.bm.dft.Ss_StateManager");
		item.addMenuItem(item1);
      
		return menubar;
	}
	
}
