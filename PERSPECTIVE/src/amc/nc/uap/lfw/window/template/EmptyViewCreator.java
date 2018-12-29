package nc.uap.lfw.window.template;

import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class EmptyViewCreator implements IViewCreator {

	@Override
	public ViewPair createViewPair(LfwWindow window, UIMeta winUm) {
		return null;
	}

	@Override
	public void replaceCode(String projPath, LfwView view) {
		
	}

}
