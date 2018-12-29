package nc.uap.lfw.editor.extNode;

import nc.uap.lfw.core.WEBPersConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class SimpleBrowserEditorInput implements IEditorInput {

	private String title;
	
	private String type;
	
	private String url;

	public SimpleBrowserEditorInput(String type){
		this.type = type;
	}
	
	public SimpleBrowserEditorInput(String type, String url){
		this.type = type;
		this.url = url;
	}
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		if(type.equals("node")){
			title = WEBPersConstants.NODEREG;
		}
		if(type.equals("menu")){
			title = WEBPersConstants.MENUREG;
		}
		if(type.equals("type")){
			title = WEBPersConstants.TYPEREG;
		}
		if(type.equals("wfmDesign")){
			title = WEBPersConstants.WFMDESIGN;
		}
		if(type.equals("QUERY_FOLDER")){
			title = WEBPersConstants.TEMPLATE_QUERY;
		}
		if(type.equals("PRINT_FOLDER")){
			title = WEBPersConstants.TEMPLATE_PRINT;
		}
		if(type.equals("printdesign")){
			title = WEBPersConstants.TEMPLATE_PRINT;
		}
		
		return title;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return getName();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
