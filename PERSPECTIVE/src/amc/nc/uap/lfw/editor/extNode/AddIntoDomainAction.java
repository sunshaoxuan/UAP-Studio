package nc.uap.lfw.editor.extNode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.base.NodeAction;
import nc.uap.lfw.domain.DomainBuildObj;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWBasicTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWBuildTreeItem;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AddIntoDomainAction extends NodeAction{

	private ArrayList<String> selModuleList = new ArrayList<String>();
	
	private HashMap<String,ArrayList<String>> dependMapping = new HashMap<String, ArrayList<String>>();
	
	private LFWBuildTreeItem item = null;
	public AddIntoDomainAction(LFWBasicTreeItem item){
		super(WEBPersConstants.DOMAIN_MANAGE);
		this.item = (LFWBuildTreeItem)item;
	}
	
	public void run(){
		addItem();
	}
	public void addItem(){
		Shell shell = Display.getCurrent().getActiveShell();
		DomainManageDialog dialog = new DomainManageDialog(shell, M_editor.AddIntoDomainAction_0);
		String domainId = item.getText();
		IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(domainId); //$NON-NLS-1$
		File xmlFile = new File(workpath.toFile(),"domain.xml"); //$NON-NLS-1$
		if(xmlFile.exists()){
			Document doc = XmlCommonTool.parseXML(xmlFile);
			NodeList list = doc.getElementsByTagName("project"); //$NON-NLS-1$
			for(int i = 0;i<list.getLength();i++){
				Element projectEle = (Element)list.item(i);
				String projectId = projectEle.getAttribute("id"); //$NON-NLS-1$
				selModuleList.add(projectId);
				NodeList dependlist = projectEle.getElementsByTagName("module"); //$NON-NLS-1$
				ArrayList<String> depends = new ArrayList();
				for(int j = 0;j<dependlist.getLength();j++){
					Element moduleEle = (Element)dependlist.item(j);
					String moduleId = moduleEle.getAttribute("id"); //$NON-NLS-1$
					depends.add(moduleId);
				}
				if(depends.size()>0){
					dependMapping.put(projectId, depends);
				}
			}
			dialog.setSelModuleList(selModuleList);
			dialog.setDependMapping(dependMapping);
		}
		
		if(dialog.open()==IDialogConstants.OK_ID){
			ArrayList<String> selModuleList = dialog.getSelModuleList();
			HashMap<String,ArrayList<String>> dependMapping = dialog.getDependMapping();
			if(selModuleList!=null){
				DomainBuildObj dObj = new DomainBuildObj();
				dObj.setDomainId(domainId);
				dObj.setModuleList(selModuleList);
				dObj.setDependMapping(dependMapping);
				
				toXml(dObj);
			}
		}
	}
	public void toXml(DomainBuildObj obj){
		try {
			Document doc = XmlCommonTool.createDocument();
			Element rootNode = doc.createElement("domain"); //$NON-NLS-1$
			doc.appendChild(rootNode);
			rootNode.setAttribute("id", obj.getDomainId());
			selModuleList = obj.getModuleList();
			dependMapping = obj.getDependMapping();
			for(String proName:selModuleList){
				Element projectNode = doc.createElement("project"); //$NON-NLS-1$
				rootNode.appendChild(projectNode);
				projectNode.setAttribute("id", proName); //$NON-NLS-1$
				ArrayList<String> dependModule = dependMapping.get(proName);
				if(dependModule!=null){
					Element dependNode = doc.createElement("depends"); //$NON-NLS-1$
					projectNode.appendChild(dependNode);
					for(String moduelName:dependModule){
						Element moduleNode = doc.createElement("module"); //$NON-NLS-1$
						dependNode.appendChild(moduleNode);
						moduleNode.setAttribute("id", moduelName); //$NON-NLS-1$
					}
				}
				
			}
			IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(item.getText()); //$NON-NLS-1$
			File folder = workpath.toFile();
			File file = new File(folder,"domain.xml"); //$NON-NLS-1$
			if(!folder.exists()){
				folder.mkdirs();
			}
	    	XmlCommonTool.documentToXml(doc, file);
	    	
		} catch (Exception e) {
			MainPlugin.getDefault().logError(e.getMessage());
		}
		
	}
	
}
