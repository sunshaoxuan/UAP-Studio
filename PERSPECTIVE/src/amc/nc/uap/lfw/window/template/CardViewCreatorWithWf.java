package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.Connector;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PluginDesc;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.jsp.uimeta.UIConstant;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UISplitterOne;
import nc.uap.lfw.jsp.uimeta.UISplitterTwo;
import nc.uap.lfw.jsp.uimeta.UIView;

public class CardViewCreatorWithWf extends CardViewCreator {
	private static final String PLUGOUT_EXETASK = "plugout_exetask";
	private static final String PLUGIN_EXETASK = "plugin_exetask";
	private static final String VIEW_SIMPLE_WF = "pubview_simpleexetask";
	private static final String VIEW_SIMPLE_WF_FULLPATH = "../uap.lfw.wfm.simpleapprove.pubview_simpleexetask";
	private static final String VIEW_APPROVE_WF = "pubview_approveeexetask";
	private static final String VIEW_APPROVE_WF_FULLPATH = "../uap.lfw.wfm.approve.pubview_approveeexetask";
	
	@Override
	protected void adjustPubviewUI(UILayout layout) {
		if(layout instanceof UIFlowhLayout){
			UIMeta viewUm = new UIMeta();
			viewUm.setId(VIEW_SIMPLE_WF + "_um");
			viewUm.setFlowmode(true);
			
			UIView viewUI = new UIView();
			viewUI.setId(VIEW_SIMPLE_WF);
			viewUI.setUimeta(viewUm);
			
			UIFlowhPanel panel = ((UIFlowhLayout) layout).addElementToPanel(viewUI);
			panel.setFloat("right");
		}
	}
	
	@Override
	protected void createPlugin(LfwWindow window, LfwView view) {
		super.createPlugin(window, view);
		PluginDesc plugin = new PluginDesc();
		plugin.setId(PLUGIN_EXETASK);
		plugin.setMethodName("doTaskExecute");
		view.addPluginDescs(plugin);
	}

	@Override
	protected void createConnectors(LfwWindow window) {
		super.createConnectors(window);
		Connector conn = new Connector();
		conn.setId("simplepubview_exetask_to_main");
		conn.setPluginId(PLUGIN_EXETASK);
		conn.setConnType(Connector.VIEW_VIEW);
		conn.setPlugoutId(PLUGOUT_EXETASK);
		conn.setSource(VIEW_SIMPLE_WF);
		conn.setTarget(VIEW_MAIN);
		window.addConnector(conn);
		  
		conn = new Connector();
		conn.setId("approvepubview_exetask_to_main");
		conn.setPluginId(PLUGIN_EXETASK);
		conn.setConnType(Connector.VIEW_VIEW);
		conn.setPlugoutId(PLUGOUT_EXETASK);
		conn.setSource(VIEW_APPROVE_WF);
		conn.setTarget(VIEW_MAIN);
		window.addConnector(conn);

	}

	@Override
	protected void createViewConfig(LfwWindow window) {
		super.createViewConfig(window);
		
		ViewConfig viewConfig = new ViewConfig();
		viewConfig.setId(VIEW_SIMPLE_WF);
		viewConfig.setRefId(VIEW_SIMPLE_WF_FULLPATH);
		viewConfig.setCanFreeDesign(false);
		window.addViewConfig(viewConfig);
		
		viewConfig = new ViewConfig();
		viewConfig.setId(VIEW_APPROVE_WF);
		viewConfig.setRefId(VIEW_APPROVE_WF_FULLPATH);
		viewConfig.setCanFreeDesign(false);
		window.addViewConfig(viewConfig);
	}
	
	@Override
	protected void createWinUI(UIMeta winUm) {
		winUm.setFlowmode(true);
		
		UISplitter spliter = new UISplitter();
		spliter.setId("mainWinSpliter");
		winUm.setElement(spliter);
		
		spliter.setBoundMode(1);
		spliter.setDivideSize("280");
		spliter.setInverse(UIConstant.FALSE);
		spliter.setOneTouch(UIConstant.FALSE);
		
		UISplitterOne one = new UISplitterOne();
		one.setId("spliter1");
		spliter.addPanel(one);
		
		UIView uiView = new UIView();
		uiView.setId(VIEW_MAIN);
		one.setElement(uiView);
		
		UISplitterTwo two = new UISplitterTwo();
		two.setId("spliter2");
		spliter.addPanel(two);
		UIView taskView = new UIView();
		taskView.setId(VIEW_APPROVE_WF);
		two.setElement(taskView);
	}

	@Override
	protected MenubarComp createMenubar() {
		return MenubarTemplateProvider.createWfCardMenubar();
	}
	
	@Override
	protected String[] getTemplateFileName() {
		Integer count = getChildCount();
		if(count > 0)
			return new String[]{"cardviewtemplate_child_wf.txt", "form.txt", "formoperator.txt"};
		return new String[]{"cardviewtemplate_wf.txt", "form.txt", "formoperator.txt"};
	}
	
}
