package nc.uap.portal.page;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.SAXParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.XmlUtility;
import nc.uap.lfw.core.vo.CpDeviceVO;
import nc.uap.portal.core.PortalConnector;
import nc.uap.portal.om.Display;
import nc.uap.portal.om.LookAndFeel;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.PortletDisplay;
import nc.uap.portal.om.PortletDisplayCategory;
import nc.uap.portal.om.Skin;
import nc.uap.portal.om.Theme;
import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.swt.ole.win32.Variant;

/**
 *与Flex数据交互
 * 
 * @author dingrf
 */
public class PageDataProviderImpl implements IPageDataProvider {

	/**
	 * 加载Portlet分类
	 */
	public void loadPortletCates(PortalPageEditor portalPageEditor,String callBack) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		List<Display>  displays = PortalConnector.getAllDisplays(projectPath,projectModuleName);
		Document doc = XmlUtility.getNewDocument(); 
		Element root = doc.createElement("Cates");
		doc.appendChild(root);	
		for (Display display : displays){
			for (PortletDisplayCategory pdc :display.getCategory()){
				Element node = doc.createElement("Cate");
				node.setAttribute("id", pdc.getId());
				node.setAttribute("text", pdc.getText());
				node.setAttribute("i18name", pdc.getI18nName());
				root.appendChild(node);			
			}
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}

	/**
	 *读取Portlet
	 */
	public void loadPortlet(PortalPageEditor portalPageEditor,
			String callBack, String cateId) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		List<Display>  displays = PortalConnector.getAllDisplays(projectPath,projectModuleName);
		Document doc = XmlUtility.getNewDocument();
		Element root = doc.createElement("Portlets");
		doc.appendChild(root);		
		for (Display display : displays){
			for (PortletDisplayCategory pdc :display.getCategory()){
				if (pdc.getId().equals(cateId)){
					createElement(pdc,doc,root);
					break;
				}
			}
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}
	
	private void  createElement(PortletDisplayCategory pdc,Document doc,Element root){
		for (PortletDisplay pd :pdc.getPortletDisplayList()){
			Element node = doc.createElement("Portlet");
			node.setAttribute("id", pd.getId());
			node.setAttribute("title", pd.getTitle());
			root.appendChild(node);
		}
	} 

	/**
	 *读取skin
	 */
	public void loadSkin(PortalPageEditor portalPageEditor, String callBack,
			String type) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		List<Skin> skins = PortalConnector.getAllSkins(projectPath, projectModuleName, type);
		Document doc = XmlUtility.getNewDocument();
		Element root = doc.createElement("Skins");
		root.setAttribute("type", type);
		doc.appendChild(root);		
		for (Skin skin :skins){
			Element node = doc.createElement("Skin");
			node.setAttribute("id", skin.getId());
			node.setAttribute("name", skin.getName());
			root.appendChild(node);
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);

	}

	/**
	 *读取theme
	 */
	public void loadTheam(PortalPageEditor portalPageEditor, String callBack,
			String groupId) {
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		LookAndFeel lookAndFeel= PortalConnector.getLookAndFeel(projectPath,projectModuleName);
		Document doc = XmlUtility.getNewDocument();
		Element root = doc.createElement("Theams");
		doc.appendChild(root);
		if (lookAndFeel != null){
			for (Theme theme :lookAndFeel.getTheme()){
				Element node = doc.createElement("Theam");
				node.setAttribute("id", theme.getId());
				node.setAttribute("title", theme.getTitle());
				root.appendChild(node);
			}
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString(); 
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}

	public void loadXML(PortalPageEditor portalPageEditor, String callBack) {
		Page page = (Page) portalPageEditor.getPageTreeItem().getData();
		String xml = PortalConnector.pageToString(page);
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>";
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}

	public void saveXml(PortalPageEditor portalPageEditor, String callBack,String xml) throws UnsupportedEncodingException {
			Page page = (Page) portalPageEditor.getPageTreeItem().getData();
			String pageXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +URLDecoder.decode(xml, "UTF-8");
			String projectPath = LFWPersTool.getProjectWithBcpPath();
			String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
			PortalConnector.savePageToXml(projectPath, projectModuleName, page.getPagename(),pageXml );
			portalPageEditor.getPageTreeItem().setData(PortalConnector.getPage(projectPath, projectModuleName, page.getPagename())); 
			portalPageEditor.setDirty(false);
	}

	public void getLinkgroups(PortalPageEditor portalPageEditor, String callBack){
		String projectPath = LFWPersTool.getProjectWithBcpPath();
		String projectModuleName = LFWPersTool.getCurrentProjectModuleName();
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpMenuCategoryVO[] menuCates = LFWWfmConnector.getMenuCategory();
		Thread.currentThread().setContextClassLoader(currentLoader);
		Document doc = XmlUtility.getNewDocument();
		Element root = doc.createElement("linkgroups");
		doc.appendChild(root);		
		for (CpMenuCategoryVO menuCate :menuCates){
			if(!"".equals(menuCate.getTitle())){
				Element node = doc.createElement("linkgroup");
				node.setAttribute("code", menuCate.getPk_menucategory());
				node.setAttribute("name", menuCate.getTitle());
				root.appendChild(node);
			}			
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
		
	}
	public void getLangRes(PortalPageEditor portalPageEditor, String callBack){
		Document doc = XmlUtility.getNewDocument();
		String path = "nc/uap/portal/page/Langres.xml";
		InputStream ins = null;
		ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		try {
			DocumentBuilder builder = XmlUtility.getDocumentBuilder();
			doc = builder.parse(ins);			
		} catch (Exception e) {
			PortalPlugin.getDefault().logError(e.getMessage());
		}
		finally{		
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				PortalPlugin.getDefault().logError(e.getMessage());
			}
		}
		
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}
	
	public void getDevices(PortalPageEditor portalPageEditor, String callBack){
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
		CpDeviceVO[] devices = LFWWfmConnector.getAllDevices();
		Thread.currentThread().setContextClassLoader(currentLoader);
		Document doc = XmlUtility.getNewDocument();
		Element root = doc.createElement("Devices");
		doc.appendChild(root);		
		for (CpDeviceVO device :devices){
			if(!"".equals(device.getName())){
				Element node = doc.createElement("Device");
				node.setAttribute("code", device.getCode());
				node.setAttribute("name", device.getName());
				root.appendChild(node);
			}			
		}
		Writer wr = new StringWriter();
		XmlUtility.printDOMTree(wr, doc, 0, "UTF-8");
		String xml = wr.toString();
		int[] methodIDs = portalPageEditor.getAutomation()
		.getIDsOfNames(new String[] { "CallFunction" });
		String arg = "<invoke name=\""+callBack+"\" returntype=\"xml\"><arguments><string>"+xml+"</string></arguments></invoke>"  ;
		Variant[] methodArgs = {
				new Variant(arg) };
		portalPageEditor.getAutomation().invoke(methodIDs[0], methodArgs);
	}
	
}
