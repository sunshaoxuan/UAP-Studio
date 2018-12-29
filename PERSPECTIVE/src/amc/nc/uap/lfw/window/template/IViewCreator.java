package nc.uap.lfw.window.template;

import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public interface IViewCreator {
	
	public ViewPair createViewPair(LfwWindow window, UIMeta winUm);
	public void replaceCode(String projPath, LfwView view);
	
}
