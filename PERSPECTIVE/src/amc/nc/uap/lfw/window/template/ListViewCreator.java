package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.conf.DatasetRule;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.conf.ViewRule;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.page.PluginDescItem;
import nc.uap.lfw.core.page.PluginProxy;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UIView;

public class ListViewCreator extends AbstractViewCreator implements ICreatorWithChildren, ICreatorWithOrgs{

	protected static final String REFRESH_PLUGIN_ID = "refresh_plugin";
	protected static final String VIEW_ORG_PATH = "../uap.lfw.bd.org.pubview_modeConfigOrg";
	protected static final String VIEW_SIMPLE_QRY_PATH = "../uap.lfw.imp.query.pubview_simplequery";
	protected static final String VIEW_MODEORG = "modeorg";
	protected static final String VIEW_SIMPLEQUERY = "simplequery";
	private static final String LISTDS = "listds";
	private static final String LISTGRID = "listgrid";
	
	private static final String LIST_WIN_FLOW_H_LAYOUT_ID = "listwinFlowhLayout";
	private static final String LIST_WIN_LEFT_FLOW_V_LAYOUT_ID = "listwinLeftFlowvLayout";
	private static final String LIST_WIN_LEFT_TITLE_CANVAS_ID = "listwinLeftTitleCanvas";
	
	@Override
	public ViewPair createViewPair(LfwWindow window, UIMeta winUm) {
		this.createViewConfig(window);
		this.createWindowUI(winUm);
		
		LfwView view = new LfwView();
		view.setId(VIEW_MAIN);
		view.setSourcePackage(window.getSourcePackage());
		int index = window.getControllerClazz().lastIndexOf(".");
		view.setControllerClazz(window.getControllerClazz().substring(0, index) + ".ListViewController");
		view.setCaption("列表");
		
		this.createListDataset(view);
		this.createChildDatasets(view);
		
		this.createMenubar(view);
		this.createMasterGrid(view);
				
		UIMeta viewUm = new UIMeta();
		viewUm.setId(VIEW_MAIN);
		viewUm.setFlowmode(true);
		addChildUI(view, viewUm);
		
		createPlugin(window, view);
		
		ViewPair pair = new ViewPair();
		pair.viewUm = viewUm;
		pair.view = view;
		
		return pair;
	}
	
	protected void createViewConfig(LfwWindow window) {
		//主view
		ViewConfig viewConfig = new ViewConfig();
		viewConfig.setId(VIEW_MAIN);
		viewConfig.setRefId(VIEW_MAIN);
		viewConfig.setCanFreeDesign(true);
		window.addViewConfig(viewConfig);
		
		//简单查询public view
		ViewConfig qryConfig = new ViewConfig();
		qryConfig.setId(VIEW_SIMPLEQUERY);
		qryConfig.setRefId(VIEW_SIMPLE_QRY_PATH);
		qryConfig.setCanFreeDesign(false);
		//设置使用简单查询加载列表数据
		qryConfig.setExtendAttribute(LfwView.AUTOQUERY, "true");
		window.addViewConfig(qryConfig);
		
		if(this.isWithOrg()){
			//组织public view
			ViewConfig orgConfig = new ViewConfig();
			orgConfig.setId(VIEW_MODEORG);
			orgConfig.setRefId(VIEW_ORG_PATH);
			orgConfig.setCanFreeDesign(false);
			window.addViewConfig(orgConfig);
			
			ViewConfig vc_sq = window.getViewConfig(VIEW_SIMPLEQUERY);
			if(vc_sq != null){
				//设置禁用简单查询加载列表数据
				vc_sq.setExtendAttribute(LfwView.AUTOQUERY, "false");
			}
		}
	}
	
	protected void createWindowUI(UIMeta winUm) {
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId(LIST_WIN_FLOW_H_LAYOUT_ID);
		winUm.setElement(flowhLayout);
		flowhLayout.setAutoFill(UIConstant.TRUE);
		
		UIFlowhPanel leftPanel = new UIFlowhPanel();
		leftPanel.setId(flowhLayout.getPanelId());
		flowhLayout.addPanel(leftPanel);
		leftPanel.setWidth("196");
		leftPanel.setRightBorder("#");
		leftPanel.setClassName("left_side_style");
		
		UIFlowvLayout leftFlowvLayout = new UIFlowvLayout();
		leftFlowvLayout.setId(LIST_WIN_LEFT_FLOW_V_LAYOUT_ID);
		leftPanel.setElement(leftFlowvLayout);
		
		UICanvas canvas = new UICanvas();
		canvas.setId(LIST_WIN_LEFT_TITLE_CANVAS_ID);
		canvas.setClassName("leftcanvas");
		canvas.addElementToPanel(null);
		UIFlowvPanel leftFlowvPanel = leftFlowvLayout.addElementToPanel(canvas);
		leftFlowvPanel.setHeight("39");
		
		this.createOtherUI(leftFlowvLayout);
		
		UIView mainView = new UIView();
		mainView.setId(VIEW_MAIN);
		flowhLayout.addElementToPanel(mainView);
	}
	
	protected void createOtherUI(UIFlowvLayout leftFlowvLayout) {
		this.createSimpleQueryUI(leftFlowvLayout);
		if(this.isWithOrg()){
			this.createOrgUI(leftFlowvLayout);
		}
	}
	
	/**
	 * 简单查询
	 * @param leftFlowvLayout
	 */
	protected void createSimpleQueryUI(UIFlowvLayout leftFlowvLayout){
		UIView qryView = new UIView();
		qryView.setId(VIEW_SIMPLEQUERY);
		
		UIFlowvPanel leftPanel_sq = leftFlowvLayout.addElementToPanel(qryView);
		leftPanel_sq.setLeftPadding("20");
		leftPanel_sq.setRightPadding("20");
	}
	
	/**
	 * 主组织
	 * @param leftFlowvLayout
	 */
	protected void createOrgUI(UIFlowvLayout leftFlowvLayout) {
		UIPanel orgPanel = new UIPanel();
		orgPanel.setId("leftOrgPanel");
		orgPanel.setTitle("组织");
		
		UIView orgView = new UIView();
		orgView.setId(VIEW_MODEORG);
		orgPanel.addElementToPanel(orgView);
		
		UIFlowvPanel leftPanel2 = leftFlowvLayout.addElementToPanel(orgPanel);
		leftPanel2.setLeftPadding("20");
		leftPanel2.setRightPadding("20");
	}
	
	private void addChildUI(LfwView view, UIMeta viewUm) {
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("mainlistflowvLayout");
		viewUm.setElement(flowvLayout);
		
		UICanvas canvas = new UICanvas();
		canvas.setId("listmenubarCanvas");
		flowvLayout.addElementToPanel(canvas);
		canvas.setClassName("rightcanvas");
		
		UIMenubarComp menubar = new UIMenubarComp();
		menubar.setId(MENUBAR);
		canvas.addElementToPanel(menubar);
		
		UIGridComp grid = new UIGridComp();
		grid.setId(LISTGRID);
		flowvLayout.addElementToPanel(grid);
		
		createChildListUI(view, flowvLayout);
	}

	private void createChildListUI(LfwView view, UIFlowvLayout flowvLayout) {
		Integer count = 0;
		String countStr = getExtInfo(CHILD_COUNT_KEY);
		if(countStr != null){
			count = Integer.parseInt(countStr);
		}
		if(count > 0){
			UITabComp tab = new UITabComp();
			tab.setId("children_tab");
			flowvLayout.addElementToPanel(tab);
			for (int i = 0; i < count; i++) {				
				UITabItem item = new UITabItem();
				item.setId("tabitem_" + i);
				tab.addPanel(item);
				
				item.setText("页签" + i);
				item.setShowCloseIcon(UIConstant.FALSE);
				
				UIGridComp uigrid = new UIGridComp();
				uigrid.setId(CHILDGRID_PREFIX + i);
				item.setElement(uigrid);
			}
		}
	}

	private void createMasterGrid(LfwView view) {
		GridComp grid = new GridComp();
		grid.setId(LISTGRID);
		view.getViewComponents().addComponent(grid);
	}

	private void createListDataset(LfwView view) {
		Dataset ds = new Dataset();
		ds.setId(LISTDS);
		ds.setLazyLoad(true);
		
		Integer count = 0;
		String countStr = getExtInfo(CHILD_COUNT_KEY);
		if(countStr != null){
			count = Integer.parseInt(countStr);
		}
		if(count > 0)
			ds.setPageSize(10);
		else
			ds.setPageSize(15);
		
		//事件
		EventConf event = DatasetEvent.getOnDataLoadEvent();
		event.setMethodName("onDataLoad");
		event.setOnserver(true);
		ds.addEventConf(event);
		
		//事件
		EventConf event1 = DatasetEvent.getOnAfterRowSelectEvent();
		event1.setMethodName("onAfterRowSelect");
		event1.setOnserver(true);
		ds.addEventConf(event1);
		
		view.getViewModels().addDataset(ds);
	}

	protected void createChildDatasets(LfwView view){
		Integer count = 0;
		String countStr = this.getExtInfo(CHILD_COUNT_KEY);
		if(countStr != null){
			count = Integer.parseInt(countStr);
		}
		
		if(count > 0){
			DatasetRelations drs = new DatasetRelations();
			drs.setId(DATASET_RELATIONS);
			view.getViewModels().setDsrelations(drs);
			
			for (int i = 0; i < count; i++) {
				GridComp grid = new GridComp();
				grid.setId(CHILDGRID_PREFIX + i);
				view.getViewComponents().addComponent(grid);
				
				Dataset childds = new Dataset();
				childds.setId(CHILDDS_PREFIX + i);
				childds.setLazyLoad(true);
				childds.setPageSize(10);
				view.getViewModels().addDataset(childds);
				
				DatasetRelation dr = new DatasetRelation(CHILD_DS_RELATION_PREFIX + i);
				dr.setMasterDataset(LISTDS);
				dr.setDetailDataset(childds.getId());
				
				drs.addDsRelation(dr);
			}
		}
	}
	
	protected void createMenubar(LfwView view) {
		MenubarComp menubar = MenubarTemplateProvider.createListMenubar();
		if(menubar != null){
			view.getViewMenus().addMenuBar(menubar);
		}
	}

	private void createPlugin(LfwWindow window, LfwView view) {
		PluginDesc qryPlugin = new PluginDesc();
		qryPlugin.setId("simpleQuery_plugin");
		qryPlugin.setMethodName("doQueryChange");
		view.addPluginDescs(qryPlugin);
		
		PluginDesc orgPlugin = new PluginDesc();
		orgPlugin.setId("org_plugin");
		orgPlugin.setMethodName("doOrgChange");
		
		DatasetRule dsr = new DatasetRule();
		dsr.setId(LISTDS);
		dsr.setType(DatasetRule.TYPE_CURRENT_LINE);
		
		ViewRule vr = new ViewRule();
		vr.setId(view.getId());
		vr.addDsRule(dsr);
		
		EventSubmitRule sr = new EventSubmitRule();
		sr.addViewRule(vr);
		
		orgPlugin.setSubmitRule(sr);
		
		view.addPluginDescs(orgPlugin);
		
		PluginDesc refreshPlugin = new PluginDesc();
		refreshPlugin.setId(REFRESH_PLUGIN_ID);
		refreshPlugin.setMethodName("doRefresh");
		PluginDescItem item = new PluginDescItem();
		item.setId("row");
		refreshPlugin.addDescItem(item);
		view.addPluginDescs(refreshPlugin);
		
		PluginProxy refreshProxy = new PluginProxy();
		refreshProxy.setId("refreshProxy");
		window.addPluginDesc(refreshProxy);
		refreshProxy.setDelegatedViewId(VIEW_MAIN);
		refreshProxy.setDelegatedPluginId(REFRESH_PLUGIN_ID);
		
		Connector conn = new Connector(); 
		conn.setConnType(Connector.INLINEWINDOW_VIEW);
		conn.setSource("CARD_WIN_ID");
		conn.setPlugoutId("proxyAfterSavePlugout");
		conn.setTarget(view.getId());
		conn.setPluginId(REFRESH_PLUGIN_ID);
		conn.setId("listViewConnID");
		view.addConnector(conn);
		
		Connector conn1 = new Connector(); 
		conn1.setConnType(Connector.VIEW_VIEW);
		conn1.setSource(VIEW_SIMPLEQUERY);
		conn1.setPlugoutId("qryout");
		conn1.setTarget(view.getId());
		conn1.setPluginId("simpleQuery_plugin");
		conn1.setId("simpleQueryConnListView");
		window.addConnector(conn1);
		
		Connector conn2 = new Connector(); 
		conn2.setConnType(Connector.VIEW_VIEW);
		conn2.setSource(VIEW_MODEORG);
		conn2.setPlugoutId("orgout");
		conn2.setTarget(view.getId());
		conn2.setPluginId("org_plugin");
		conn2.setId("modeOrgConnListView");
		window.addConnector(conn2);
	}
	
	@Override
	protected String[] getTemplateFileName() {
		if(this.isWithOrg()){
			return new String[]{"listviewtemplate_orgs.txt"};
		}else{
			return new String[]{"listviewtemplate.txt"};
		}
	}
	
	/**
	 * 是否带主组织
	 * @return
	 */
	protected boolean isWithOrg(){
		String orgs = this.getExtInfo(ORGS_KEY);
		return orgs != null && orgs.equals("true");
	}
	
}
