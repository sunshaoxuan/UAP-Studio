package nc.uap.lfw.window.template;

import java.util.Map;

import nc.uap.lfw.window.IWindowTemplateType;

public final class ViewCreatorFactory {
	public static IViewCreator getViewCreator(String viewType, Map<String, String> extInfo){
		IViewCreator creator = null;
		if(IWindowTemplateType.TYPE_EMPTY.equals(viewType)){
			creator = new EmptyViewCreator();
		}
		else if(IWindowTemplateType.TYPE_LIST.equals(viewType)){
			creator = new ListViewCreator();
		}
		else if(IWindowTemplateType.TYPE_LIST_WF.equals(viewType)){
			creator = new ListViewCreatorWithWf();
		}
		else if(IWindowTemplateType.TYPE_OK_CANCEL.equals(viewType)){
			creator = new OkCancelViewCreator();
		}
		else if(IWindowTemplateType.TYPE_SAVE_SAVEADD_CANCEL.equals(viewType)){
			creator = new SaveSaveaddCancelViewCreator(); 
		}
		else if(IWindowTemplateType.TYPE_CARD.equals(viewType)){
			creator = new CardViewCreator(); 
		}
		else if(IWindowTemplateType.TYPE_CARD_WF.equals(viewType)){
			creator = new CardViewCreatorWithWf(); 
		}
		if(creator instanceof AbstractViewCreator){
			((AbstractViewCreator)creator).setExtInfo(extInfo);
		}
		return creator;
	}
}
