package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.jsp.uimeta.UICanvas;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMenubarComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIPanel;
import nc.uap.lfw.jsp.uimeta.UIPanelPanel;
import nc.uap.lfw.jsp.uimeta.UIView;

public class CardViewCreator extends AbstractViewCreator implements ICreatorWithChildren{
	protected static final String MAIN_FLOWV_LAYOUT = "mainFlowvLayout";
	protected static final String MAIN_FLOWH_LAYOUT = "mainFlowhLayout";
	protected static final String MAIN_PANEL_LAYOUT = "mainPanelLayout";
	private static final String CARDDS = "cardds";
	private static final String CARD_FORM = "cardform";
	private static final String BOTTOM_PADDING = "10";
	private static final String TOP_PADDING = "20";

	@Override
	public ViewPair createViewPair(LfwWindow window, UIMeta winUm) {
		window.setExtendAttribute("CARD_WINDOW", true);
		createViewConfig(window);
		createWinUI(winUm);
		
		LfwView view = createCardView(window);
		
		UIMeta viewUm = createCardViewUI(view);
		
		ViewPair pair = new ViewPair();
		pair.viewUm = viewUm;
		pair.view = view;
		
		return pair;
	}

	protected void createViewConfig(LfwWindow window) {
		ViewConfig viewConfig = new ViewConfig();
		viewConfig.setId(VIEW_MAIN);
		viewConfig.setRefId(VIEW_MAIN);
		viewConfig.setCanFreeDesign(true);
		window.addViewConfig(viewConfig);
	}
	
	protected void createWinUI(UIMeta winUm) {
		UIView uiView = new UIView();
		uiView.setId(VIEW_MAIN);
		winUm.setElement(uiView);
	}
	
	protected LfwView createCardView(LfwWindow window){
		LfwView view = new LfwView();
		view.setId(VIEW_MAIN);
		view.setCaption("编辑");
		view.setSourcePackage(window.getSourcePackage());
		int index = window.getControllerClazz().lastIndexOf(".");
		view.setControllerClazz(window.getControllerClazz().substring(0, index) + ".MainViewController");
		
		view.getViewModels().addDataset(createCardDataset());
		view.getViewMenus().addMenuBar(createMenubar());
		view.getViewComponents().addComponent(createForm());
		
		//事件
		EventConf conf = DialogEvent.getBeforeShowEvent();
		conf.setMethodName("beforeShow");
		view.addEventConf(conf);
		//plugin/plugout
		createPlugout(window, view);
		createPlugin(window, view);
		createConnectors(window);
		
		return view;
	}
	
	private FormComp createForm() {
		FormComp form = new FormComp();
		form.setId(CARD_FORM);
		return form;
	}

	private Dataset createCardDataset() {
		Dataset ds = new Dataset();
		ds.setId(CARDDS);
		ds.setLazyLoad(true);
		return ds;
	}

	protected MenubarComp createMenubar() {
		return MenubarTemplateProvider.createCardMenubar();
	}
	
	private void createPlugout(LfwWindow window, LfwView view) {
		PlugoutDesc plugout = new PlugoutDesc();
		plugout.setId("afterSavePlugout");
		view.addPlugoutDescs(plugout);
		
		PlugoutProxy proxy = new PlugoutProxy();
		proxy.setId("proxyAfterSavePlugout");
		window.addPlugoutDesc(proxy);
		proxy.setDelegatedViewId(VIEW_MAIN);
		proxy.setDelegatedPlugoutId("afterSavePlugout");
		
		Connector conn = new Connector(); 
		conn.setConnType(Connector.VIEW_WINDOW);
		conn.setSource(view.getId());
		conn.setPlugoutId("afterSavePlugout");
		conn.setTarget(window.getId());
		conn.setPluginId("proxyAfterSavePlugout");
		conn.setId("cardViewConnID");
		view.addConnector(conn);
	}
	
	protected void createPlugin(LfwWindow window, LfwView view) {}

	protected void createConnectors(LfwWindow window) {}
	
	protected UIMeta createCardViewUI(LfwView view){
		UIMeta viewUm = new UIMeta();
		viewUm.setId(VIEW_MAIN);
		viewUm.setFlowmode(true);
		addChildUI(viewUm);
		createChildren(view, viewUm);
		createOthers(view, viewUm);
		
		return viewUm;
	}
	
	private void addChildUI(UIMeta um) {
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId(MAIN_FLOWV_LAYOUT);
		
		UICanvas canvas = new UICanvas();
		canvas.setClassName("rightcanvas");
		canvas.setId("menubarcanvas");
		flowvLayout.addElementToPanel(canvas);
		
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId(MAIN_FLOWH_LAYOUT);
		canvas.addElementToPanel(flowhLayout);
		
		UIMenubarComp menubar = new UIMenubarComp();
		menubar.setId(MENUBAR);
		flowhLayout.addElementToPanel(menubar);
		
		//调整公共view布局
		this.adjustPubviewUI(flowhLayout);
		
		UIPanel panelLayout = new UIPanel();
		panelLayout.setId(MAIN_PANEL_LAYOUT);
		panelLayout.setTitle("基本信息");
		UIFormComp form = new UIFormComp();
		form.setId(CARD_FORM);
		UIPanelPanel formPanelPanel = panelLayout.addElementToPanel(form);
		formPanelPanel.setBottomPadding(BOTTOM_PADDING);
		formPanelPanel.setTopPadding(TOP_PADDING);
		
		UIFlowvPanel formFlowvPanel = flowvLayout.addElementToPanel(panelLayout);
		formFlowvPanel.setRightPadding(LEFT_RIGHT_PADDING);
		formFlowvPanel.setLeftPadding(LEFT_RIGHT_PADDING);
		
		um.setElement(flowvLayout);
	}

	protected void adjustPubviewUI(UILayout layout){}
	
	protected void createChildren(LfwView view, UIMeta viewUm) {
		Integer count = getChildCount();
		if(count > 0){
			UIFlowvLayout flowvLayout = (UIFlowvLayout) viewUm.getElement();
			for (int i = 0; i < count; i++) {
				GridComp grid = new GridComp();
				grid.setId(CHILDGRID_PREFIX + i);
				grid.setShowImageBtn(true);
				view.getViewComponents().addComponent(grid);
				
				Dataset childds = new Dataset();
				childds.setId(CHILDDS_PREFIX + i);
				childds.setLazyLoad(true);
				childds.setPageSize(10);
				view.getViewModels().addDataset(childds);
				
				UIGridComp uigrid = new UIGridComp();
				uigrid.setId(CHILDGRID_PREFIX + i);
				
				UIPanel uigridPanel = new UIPanel();
				uigridPanel.setId("childgridpanel_" + i);
				uigridPanel.setExpand(UIConstant.TRUE);
				uigridPanel.setTitle("子表" + i);
				uigridPanel.setClassName("small_panel_div");
				UIPanelPanel uiPanelPanel = uigridPanel.addElementToPanel(uigrid);
				uiPanelPanel.setBottomPadding("10");
				uiPanelPanel.setTopPadding("20");
				
				UIFlowvPanel panel = flowvLayout.addElementToPanel(uigridPanel);
				panel.setLeftPadding(LEFT_RIGHT_PADDING);
				panel.setRightPadding(LEFT_RIGHT_PADDING);
			}
		}
	}
	
	protected Integer getChildCount() {
		String countStr = getExtInfo(CHILD_COUNT_KEY);
		if(countStr==null)
			return 0;
		Integer count = Integer.parseInt(countStr);
		return count;
	}
	
	protected void createOthers(LfwView view, UIMeta viewUm) {}

	@Override
	protected String[] getTemplateFileName() {
		Integer count = getChildCount();
		if(count > 0)
			return new String[]{"cardviewtemplate_child.txt"};
		return new String[]{"cardviewtemplate.txt"};
	}
	
}
