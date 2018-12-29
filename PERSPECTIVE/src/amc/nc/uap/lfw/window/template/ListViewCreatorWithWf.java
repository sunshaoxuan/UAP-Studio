package nc.uap.lfw.window.template;

import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.LfwView;

public class ListViewCreatorWithWf extends ListViewCreator {

	@Override
	protected void createMenubar(LfwView view) {
		MenubarComp menubar = MenubarTemplateProvider.createWfListMenubar();
		if(menubar != null){
			view.getViewMenus().addMenuBar(menubar);
		}
	}

}
