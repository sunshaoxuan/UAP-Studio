/**
 * 
 */
package nc.uap.lfw.editor.application;

import nc.lfw.editor.common.LfwElementObjWithGraph;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.uimodel.Application;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 
 * ApplicationÕº–Œ¿‡
 * @author chouhl
 *
 */
public class ApplicationObj extends LfwElementObjWithGraph {
	
	private static final long serialVersionUID = -6000164025127809766L;

	private Application app = null;
	
	private ApplicationFigure figure = null;
	
	private String windowId = null;
	
	private String windowName = null;
	
	private String controllerClazz = null;
	
	private String errorMsg;
	
	public ApplicationObj(){}
	
	public ApplicationObj(Application app){
		this.app = app;
	}

	public WebElement getWebElement() {
		return null;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public ApplicationFigure getFigure() {
		return figure;
	}

	public void setFigure(ApplicationFigure figure) {
		this.figure = figure;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getControllerClazz() {
		return controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}
	
	

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public Object getPropertyValue(Object id) {
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return super.getPropertyDescriptors();
	}
	
}
