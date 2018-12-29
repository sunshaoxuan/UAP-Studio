package nc.uap.lfw.build.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.common.XmlCommonTool;
import nc.uap.lfw.lang.M_build;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ScrollBar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CompileDomainDialog extends DialogWithTitle{

	private ArrayList<String> selModuleList = new ArrayList<String>();

	private HashMap<String, ArrayList<String>> dependMapping = new HashMap<String, ArrayList<String>>();

	private String domain = null;
	
	private String selModule = null;
	
	private List moduleList;
	
	private String type;
	
	public CompileDomainDialog(String domain, String title) {
		super(null, title);
		this.domain = domain;
		getDomainDetail();
	}
	protected Control createDialogArea(Composite parent){
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(500, 200));
		composite.setLocation(150, 150);
		moduleList = new List(composite, SWT.V_SCROLL);
		moduleList.setItems(selModuleList.toArray(new String[0]));
		moduleList.setLayoutData(new GridData(GridData.FILL_BOTH));
		moduleList.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selModule = moduleList.getSelection()[0];
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selModule = moduleList.getSelection()[0];
			}
		});
		Group radioGroup = new Group(composite,SWT.NONE);
		radioGroup.setLayout(new GridLayout(1,false));
		radioGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button diskBtn1 = new Button(radioGroup, SWT.RADIO);
		diskBtn1.setText(M_build.CompileDomainDialog_0);
		diskBtn1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				type = "single"; //$NON-NLS-1$
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Button diskBtn2 = new Button(radioGroup, SWT.RADIO);
		diskBtn2.setText(M_build.CompileDomainDialog_1);
		diskBtn2.setSelection(true);
		setType("all"); //$NON-NLS-1$
		diskBtn2.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				type = "all"; //$NON-NLS-1$
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		return composite;
	}
	public void getDomainDetail() {
		String domainId = this.domain;
		IPath workpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("domain").append(domainId); //$NON-NLS-1$
		// File buildFile = new File("C:/setup.ini");
		File xmlFile = new File(workpath.toFile(), "domain.xml"); //$NON-NLS-1$
		if (xmlFile.exists()) {
			Document doc = XmlCommonTool.parseXML(xmlFile);
			NodeList list = doc.getElementsByTagName("project"); //$NON-NLS-1$
			for (int i = 0; i < list.getLength(); i++) {
				Element projectEle = (Element) list.item(i);
				String projectId = projectEle.getAttribute("id"); //$NON-NLS-1$
				selModuleList.add(projectId);
				NodeList dependlist = projectEle.getElementsByTagName("module"); //$NON-NLS-1$
				ArrayList<String> depends = new ArrayList();
				for (int j = 0; j < dependlist.getLength(); j++) {
					Element moduleEle = (Element) dependlist.item(j);
					String moduleId = moduleEle.getAttribute("id"); //$NON-NLS-1$
					depends.add(moduleId);
				}
				if (depends.size() > 0) {
					dependMapping.put(projectId, depends);
				}
			}
		} else {
			MessageDialog.openWarning(null, M_build.CompileDomainDialog_2, M_build.CompileDomainDialog_3);
		}
	}
	public String getSelModule() {
		return selModule;
	}
	public void setSelModule(String selModule) {
		this.selModule = selModule;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
