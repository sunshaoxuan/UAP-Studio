package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.core.uimodel.ViewConfig;
import nc.uap.lfw.jsp.uimeta.UIButton;
import nc.uap.lfw.jsp.uimeta.UIFlowhLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowhPanel;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;

public class SaveSaveaddCancelViewCreator extends AbstractViewCreator{

	private static final String CANCEL_BUTTON_ID = "btn_cancel";
	private static final String SAVE_BUTTON_ID = "btn_save";
	private static final String SAVE_ADD_BUTTON_ID = "btn_save_add";
	protected static final String MAIN_FLOWV_LAYOUT = "mainFlowvLayout";
	private static final String CARDDS = "cardds";
	private static final String CARD_FORM = "cardform";

	@Override
	public ViewPair createViewPair(LfwWindow window, UIMeta winUm) {
		this.createViewConfig(window);
		this.createWinUI(winUm);
		
		LfwView view = this.createCardView(window);
		
		UIMeta viewUm = this.createCardViewUI(view);
		
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
		view.setSourcePackage(window.getSourcePackage());
		int index = window.getControllerClazz().lastIndexOf(".");
		view.setControllerClazz(window.getControllerClazz().substring(0, index) + ".MainViewController");
		view.setCaption("对话框");
		
		this.createForm(view);
		this.createBottomButtons(view);
		this.createCardDataset(view);
		
		//事件
		EventConf conf = DialogEvent.getBeforeShowEvent();
		conf.setMethodName("beforeShow");
		view.addEventConf(conf);
		
		this.createPlugout(window, view);
		this.createPlugin(window, view);
		this.createConnectors(window);
		
		return view;
	}
	
	protected void createForm(LfwView view) {
		FormComp form = new FormComp();
		form.setId(CARD_FORM);
		view.getViewComponents().addComponent(form);
	}

	protected void createBottomButtons(LfwView view) {
		ButtonComp okButton = new ButtonComp();
		okButton.setId(SAVE_BUTTON_ID);
		okButton.setText("Save");
		okButton.setI18nName("save");
		okButton.setLangDir("lfwbuttons");
		EventConf okEvent = MouseEvent.getOnClickEvent();
		okEvent.setMethodName("onBtnOk");
		okButton.addEventConf(okEvent);
		view.getViewComponents().addComponent(okButton);
		
		ButtonComp saveAddButton = new ButtonComp();
		saveAddButton.setId(SAVE_ADD_BUTTON_ID);
		saveAddButton.setText("SaveAdd");
		saveAddButton.setI18nName("save_add");
		saveAddButton.setLangDir("lfwbuttons");
		EventConf saveAddEvent = MouseEvent.getOnClickEvent();
		saveAddEvent.setMethodName("onBtnSaveAdd");
		saveAddButton.addEventConf(saveAddEvent);
		view.getViewComponents().addComponent(saveAddButton);
		
		ButtonComp cancelButton = new ButtonComp();
		cancelButton.setId(CANCEL_BUTTON_ID);
		cancelButton.setText("Cancel");
		cancelButton.setI18nName("cancel");
		cancelButton.setLangDir("lfwbuttons");
		EventConf cancelEvent = MouseEvent.getOnClickEvent();
		cancelEvent.setMethodName("onBtnCancel");
		cancelButton.addEventConf(cancelEvent);
		view.getViewComponents().addComponent(cancelButton);
	}
	
	protected void createCardDataset(LfwView view) {
		Dataset ds = new Dataset();
		ds.setId(CARDDS);
		ds.setLazyLoad(true);
		view.getViewModels().addDataset(ds);
	}
	
	protected void createPlugout(LfwWindow window, LfwView view) {
		PlugoutDesc plugout = new PlugoutDesc();
		plugout.setId("afterSavePlugout");
		view.addPlugoutDescs(plugout);
		
		PlugoutProxy proxy = new PlugoutProxy();
		proxy.setId("proxyAfterSavePlugout");
		window.addPlugoutDesc(proxy);
		proxy.setDelegatedViewId(VIEW_MAIN);
		proxy.setDelegatedPlugoutId("afterSavePlugout");
	}
	
	protected void createPlugin(LfwWindow window, LfwView view) {}
	
	protected void createConnectors(LfwWindow window) {}
	
	protected UIMeta createCardViewUI(LfwView view){
		UIMeta viewUm = new UIMeta();
		viewUm.setId(VIEW_MAIN);
		viewUm.setFlowmode(false);
		
		this.addChildUI(viewUm);
		
		this.createOthers(view, viewUm);
		
		return viewUm;
	}
	
	protected void addChildUI(UIMeta um) {
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId(MAIN_FLOWV_LAYOUT);
		
		UIFormComp form = new UIFormComp();
		form.setId(CARD_FORM);
		UIFlowvPanel formFlowvPanel = flowvLayout.addElementToPanel(form);
		formFlowvPanel.setRightPadding(LEFT_RIGHT_PADDING);
		formFlowvPanel.setLeftPadding(LEFT_RIGHT_PADDING);
		
		
		UIFlowhLayout bottomFlowhLayout = new UIFlowhLayout();
		bottomFlowhLayout.setId("bottomFlowh");
		
		bottomFlowhLayout.addElementToPanel(null);
		
		UIButton okButton = new UIButton();
		okButton.setId(SAVE_BUTTON_ID);
		okButton.setWidth("70");
		okButton.setClassName("blue_button_div");
		UIFlowhPanel okPanel = bottomFlowhLayout.addElementToPanel(okButton);
		okPanel.setWidth("70");
		
		UIButton saveAddButton = new UIButton();
		saveAddButton.setId(SAVE_ADD_BUTTON_ID);
		saveAddButton.setWidth("80");
		saveAddButton.setLeft("10");
		UIFlowhPanel saveAddPanel = bottomFlowhLayout.addElementToPanel(saveAddButton);
		saveAddPanel.setWidth("90");
		
		UIButton cancelButton = new UIButton();
		cancelButton.setId(CANCEL_BUTTON_ID);
		cancelButton.setWidth("70");
		cancelButton.setLeft("10");
		UIFlowhPanel cancelPanel = bottomFlowhLayout.addElementToPanel(cancelButton);
		cancelPanel.setWidth("100");
		
		flowvLayout.addElementToPanel(bottomFlowhLayout).setHeight("33");
		um.setElement(flowvLayout);
	}
	
	protected void createOthers(LfwView view, UIMeta viewUm) {}

	@Override
	protected String[] getTemplateFileName() {
		return new String[]{"savesaveaddcanceltemplate.txt"};
	}
	
}
