/**
 * 手动发布应用
 */
package nc.uap.lfw.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.util.Observable;


import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsCategoryVO;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.cpb.org.vos.CpModuleVO;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.uimodel.Application;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.editor.extNode.NewTemplateDialog;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.template.ITemplatePageFactory;
import nc.vo.pub.lang.UFBoolean;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 手动发布应用
 * @author guomq1
 * 2012-8-14
 */
@SuppressWarnings("restriction")
public class NewAppNodeManualDialog extends DialogWithTitle {


	private ComboDialogField appidcb;
	private ComboDialogField menucategoryname;
	
	private ComboDialogField appcategoryname;
//	private ComboDialogField modulename;
	private Combo modulename;
	
	//新建菜单分类
	private NewMenuInfoDialog newmenu = null;
	private StringDialogField menucategoryIdField; 
	private StringDialogField menucategoryNameField; 
	
	//新建父类菜单
	
	private MenuItemShowDidalog newparentmenu = null;
	private StringDialogField parentmenuitem;
	
	//新建功能分类
	private NewAppInfoDialog newapp = null;
	private StringDialogField appcategoryIdField; 
	private StringDialogField appcategoryNameField; 
	
	private StringDialogField menuIdField; 
	private StringDialogField menuNameField; 
	private StringDialogField funcNodeField; 
	private StringDialogField funcNameField; 
	

	
	private String[] Pk_menucategoryids;
	private String[] Pk_menucategorynames;
	private String[] Pk_parentmenuids;
	private String[] Pk_parentmenunames;
	
	private String[] Pk_appcategoryids;
	private String[] Pk_appcategorynames;
	
	private String pk_menucategory = null;

	
	private ArrayList<String> appFile;
	
	CpMenuCategoryVO[] menuCategoryVOs = null;
	CpMenuItemVO[] parentMenuItemVOs = null;
	CpAppsCategoryVO[] appCategoryVOs = null;
	CpModuleVO[] moduleVOs = null;
	List<CpModuleVO> moduleList = null;
	
	private CpAppsNodeVO exsitFunc = null;
	
	private CpMenuItemVO exsitMenu = null;

	
	
	
	public CpAppsNodeVO getExsitFunc() {
		return exsitFunc;
	}

	public void setExsitFunc(CpAppsNodeVO exsitFunc) {
		this.exsitFunc = exsitFunc;
	}

	/**
	 * 构造函数
	 * @param title
	 */
	public NewAppNodeManualDialog(String title) {
		super(null, title);
	}
	
	/**
	 * 构造函数
	 * @param parent
	 * @param title
	 */
	public NewAppNodeManualDialog(Shell parent,String title) {
		super(parent,title);
	}

	protected Control createDialogArea(Composite parent){
	final Composite composite = new Composite(parent, SWT.NULL);
	composite.setFont(parent.getFont());
	composite.setLayout(new GridLayout(1, true));
	composite.setLayoutData(new GridData(600, 400));
	composite.setLocation(150, 150);
	new appGroup(composite);
	new menuInfoGroup(composite);
	new funInfoGroup(composite);
	
	return composite;
	}
	

	/**
	 * 选择要发布的应用(app)
	 * @author guomq1
	 * 2012-8-16
	 */
	private final class appGroup extends Observable implements
			IDialogFieldListener {

		/**
		 * @param composite
		 */
		public appGroup(Composite composite) {
			Group group = new Group(composite,SWT.NONE);
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 5;
			group.setLayoutData(layoutData);
			group.setLocation(100, 100);
			group.setSize(200, 150);
			group.setLayout(new GridLayout(4, false));
			group.setText(M_application.NewAppNodeManualDialog_0);
			
			
			appidcb = new ComboDialogField(SWT.NONE);
			appidcb.setLabelText(M_application.NewAppNodeManualDialog_1);
			appidcb.doFillIntoGrid(group, 3);
			
			//从业务组件下的applications文件夹中读取已存在的application
			String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
			File appFolder = new File(bcpPath,"/web/html/applications"); //$NON-NLS-1$
			appFile = new ArrayList<String>();
			if(appFolder.exists()){
				for(File app:appFolder.listFiles()){
					appFile.add(app.getName());
				}
			}
			appidcb.setItems(appFile.toArray(new String[0]));
			appidcb.selectItem(M_application.NewAppNodeManualDialog_2);
			if(!M_application.NewAppNodeManualDialog_2.equals(appidcb.getText())) appidcb.selectItem(0);
			if(exsitFunc!=null){
				appidcb.setText(exsitFunc.getAppid());
				appidcb.setEnabled(false);
			}
		}


		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 菜单信息
	 * @author guomq1
	 * 2012-8-15
	 */
	private final class menuInfoGroup extends Observable implements
			IDialogFieldListener {

		/**
		 * @param composite
		 */
		public menuInfoGroup(Composite composite) {
			Group group = new Group(composite,SWT.NONE);
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 5;
			group.setLayoutData(layoutData);
			group.setLocation(100, 100);
			group.setSize(200, 150);
			group.setLayout(new GridLayout(4, false));
			group.setText(M_application.NewAppNodeManualDialog_3);
			
			
			menucategoryname = new ComboDialogField(SWT.HORIZONTAL|SWT.READ_ONLY);
			menucategoryname.setLabelText(M_application.NewAppNodeManualDialog_4);
			menucategoryname.doFillIntoGrid(group, 3);
			

			
			final Button newmenucategory = new Button(group, SWT.BUTTON1);
			newmenucategory.setText(M_application.NewAppNodeManualDialog_5);

			
			parentmenuitem = new StringDialogField();
			parentmenuitem.setLabelText(M_application.NewAppNodeManualDialog_6);
			parentmenuitem.doFillIntoGrid(group, 3);
			parentmenuitem.setEnabled(false);
			
			
			final Button newparentmenubt = new Button(group, SWT.BUTTON1);
			newparentmenubt.setText(M_application.NewAppNodeManualDialog_7);
			
			menuIdField = new StringDialogField();
			menuIdField.setLabelText(M_application.NewAppNodeManualDialog_8);
			menuIdField.doFillIntoGrid(group, 4);
			
			menuNameField = new StringDialogField();
			menuNameField.setLabelText(M_application.NewAppNodeManualDialog_9);
			menuNameField.doFillIntoGrid(group, 4);
			
			
			setMenucategorynameTtems();
//			menucategoryname.setDialogFieldListener(new IDialogFieldListener() {				
//				@Override
//				public void dialogFieldChanged(DialogField field) {
//					String Pk_menucategoryid = null;
//					int i = 0;
////					while(i != -1 && i < Pk_menucategorynames.length){
//					for(i = 0 ;i<Pk_menucategorynames.length;i++){
//						if(Pk_menucategorynames[i].equals(menucategoryname.getText()))break;
//					}
//					Pk_menucategoryid = Pk_menucategoryids[i];
//					pk_menucategory = (LFWWfmConnector.getMenuCategoryById(Pk_menucategoryid)).getPk_menucategory();
//				}
//			});
			
			menucategoryname.selectItem(M_application.NewAppNodeManualDialog_2);
			if(!M_application.NewAppNodeManualDialog_2.equals(menucategoryname.getText())) menucategoryname.selectItem(0);
//			setParentmenuTtems();

			newmenucategory.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event){
					newmenucategory.setEnabled(false);
					newmenu = new NewMenuInfoDialog(getShell(), M_application.NewAppNodeManualDialog_5);
					if(newmenu.open() == IDialogConstants.OK_ID){
						newmenucategory.setEnabled(true);
						CpMenuCategoryVO newmenucategoryvo = new CpMenuCategoryVO();
						newmenucategoryvo.setId(menucategoryIdField.getText());
						newmenucategoryvo.setTitle(menucategoryNameField.getText());
						newmenucategoryvo.setActiveflag(UFBoolean.TRUE);
						String pk = LFWWfmConnector.saveMenuCategory(newmenucategoryvo);
						setMenucategorynameTtems();
						menucategoryname.setText(menucategoryNameField.getText());

					}else{
						newmenucategory.setEnabled(true);
						}
				}
			});
			newparentmenubt.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event){
					newparentmenubt.setEnabled(false);
					String Pk_menucategoryid = null;
					if(menucategoryname.getSelectionIndex() != -1){
					Pk_menucategoryid = Pk_menucategoryids[menucategoryname.getSelectionIndex()];
					}else{
						int i = 0;
//						while(i != -1 && i < Pk_menucategorynames.length){
						for(i = 0 ;i<Pk_menucategorynames.length;i++){
							if(Pk_menucategorynames[i].equals(menucategoryname.getText()))break;
						}
						Pk_menucategoryid = Pk_menucategoryids[i];
						
					}
					pk_menucategory = (LFWWfmConnector.getMenuCategoryById(Pk_menucategoryid)).getPk_menucategory();
					newparentmenu = new MenuItemShowDidalog(getShell(), M_application.NewAppNodeManualDialog_7,pk_menucategory,menucategoryname.getText());
					if(newparentmenu.open() == IDialogConstants.OK_ID){
//						newparentmenubt.setEnabled(true);
//						CpMenuItemVO newparentmenuvo = new CpMenuItemVO();
/*						newparentmenuvo.setCode(parentmenuIdField.getText());
						newparentmenuvo.setName(parentmenuNameField.getText());*/
//						LFWWfmConnector.saveMenuItem(newparentmenuvo);
//						setParentmenuTtems();
						parentmenuitem.setText(newparentmenu.getMenuItemName());
					}else{
						newparentmenubt.setEnabled(true);
						}
					newparentmenubt.setEnabled(true);
				}
			});
			
			
			if(exsitFunc!=null){
				String pk_funcnode = exsitFunc.getPk_appsnode();
//				String pk_parent = exsitFunc.get
				CpMenuItemVO[] menus = LFWWfmConnector.getMenuItemsByCondition("pk_funnode='"+pk_funcnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				CpMenuItemVO menuitem = null;
				if(menus!=null&&menus.length>0){
					menuitem = menus[0];
					menuIdField.setText(menuitem.getCode());
					menuNameField.setText(menuitem.getName());								
					if(menuitem.getPk_parent()!=null){
						CpMenuItemVO parentItem = LFWWfmConnector.getMenuItemsByCondition("pk_menuitem='"+menuitem.getPk_parent()+"'")[0]; //$NON-NLS-1$ //$NON-NLS-2$
						parentmenuitem.setText(parentItem.getName());
					}
					CpMenuCategoryVO[] menuCates =  LFWWfmConnector.getMenuCategory();
					String cateName = null;
					for(CpMenuCategoryVO menucate:menuCates){
						if(menucate.getPk_menucategory().equals(menuitem.getPk_menucategory())){
							cateName = menucate.getTitle();
							menucategoryname.setText(cateName);
							break;
						}
					}
					exsitMenu = menuitem;
				}
			}
			
			
		}
		
		/**
		 * 对菜单分类名称下拉框设值
		 */		
		private void setMenucategorynameTtems(){
			menuCategoryVOs = LFWWfmConnector.getMenuCategory();
			int length = menuCategoryVOs.length;
			
			Pk_menucategoryids = new String[length];
			Pk_menucategorynames = new String[length];

			for(int i = 0;i<length;i++){
				CpMenuCategoryVO menuCategoryVO = menuCategoryVOs[i];
				Pk_menucategoryids[i] = menuCategoryVO.getId();
				if(menuCategoryVO.getTitle()!= null){
				Pk_menucategorynames[i] = menuCategoryVO.getTitle();
				}else{
					Pk_menucategorynames[i] = ""; //$NON-NLS-1$
				}
				
			}
			menucategoryname.setItems(Pk_menucategorynames);
		}
		
/*		private void setParentmenuTtems() {
			//通过selectionIndex获取Pk_menucategoryids数组中相应的Pk_menucategoryid
			String Pk_menucategoryid = null;
			if(menucategoryname.getSelectionIndex() != -1){
			Pk_menucategoryid = Pk_menucategoryids[menucategoryname.getSelectionIndex()];
			}else{
				int i = 0;
				while(i != -1 && i < Pk_menucategorynames.length){
					i++;
					if(Pk_menucategorynames[i].equals(menucategoryname.getText()))break;
				}
				Pk_menucategoryid = Pk_menucategoryids[i];
				
			}
			pk_menucategory = (LFWWfmConnector.getMenuCategoryById(Pk_menucategoryid)).getPk_menucategory();
			parentMenuItemVOs  = LFWWfmConnector.getMenuItemsByCondition("pk_menucategory = '"+pk_menucategory+"'");
			if(parentMenuItemVOs != null){
				int parentmenuitemlength = parentMenuItemVOs.length;
				Pk_appcategoryids = new String[parentmenuitemlength];
				Pk_appcategorynames = new String[parentmenuitemlength];
				for(int i = 0; i < parentmenuitemlength; i++){
					Pk_appcategorynames[i] = parentMenuItemVOs[i].getName();
					Pk_appcategoryids[i] = parentMenuItemVOs[i].getCode();
					}
				parentmenuitem.setItems(Pk_appcategorynames);
				}
			}*/


		@Override
		public void dialogFieldChanged(DialogField field) {
			

		}

	}
	/**
	 * 功能信息
	 * @author guomq1
	 * 2012-8-15
	 */
	private final class funInfoGroup extends Observable implements
			IDialogFieldListener {

		/**
		 * @param composite
		 */
		public funInfoGroup(Composite composite) {
			Group group = new Group(composite,SWT.NONE);
			
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 5;
			group.setLayoutData(layoutData);
			group.setLocation(100, 100);
			group.setSize(200, 150);
			group.setLayout(new GridLayout(4, false));
			group.setText(M_application.NewAppNodeManualDialog_10);

//			modulename = new ComboDialogField(SWT.NONE);
//			modulename.setLabelText("模块");
//			modulename.doFillIntoGrid(group, 3);
//			setModuleItems();
//			modulename.selectItem(M_application.NewAppNodeManualDialog_2);
//			if(!M_application.NewAppNodeManualDialog_2.equals(modulename.getText())) modulename.selectItem(0);
//			if(!M_application.NewAppNodeManualDialog_2.equals(appcategoryname.getText())) appcategoryname.selectItem(0);
			
//			modulename.
			
			Label moduleLabel = new Label(group, SWT.NONE);
			moduleLabel.setText(M_application.NewAppNodeManualDialog_35);
			modulename = new Combo(group, SWT.NONE);
			GridData moduleLay = new GridData();
			moduleLay.horizontalSpan = 2;
			modulename.setLayoutData(moduleLay);
			setModuleItems();
			modulename.select(0);
			modulename.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					setappcategorynameTtems();
				}
			});
//			if(!M_application.NewAppNodeManualDialog_2.equals(modulename.getText())) modulename.selectItem(0);
//			modulename.set
			
			final Button newmodule = new Button(group, SWT.BUTTON1);
			newmodule.setText(M_application.NewAppNodeManualDialog_36);
			newmodule.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					newmodule.setEnabled(false);
					
					NewModuleDialog moduleDialg = new NewModuleDialog(getShell(), M_application.NewAppNodeManualDialog_36);
					if(moduleDialg.open() == IDialogConstants.OK_ID){
						String moduleCode = moduleDialg.getModuleCode();
						String moduleId = moduleDialg.getModuleId();
						String moduleTitle = moduleDialg.getModuleTitle();
						String moduleParent = moduleDialg.getModuleParent();
						CpModuleVO mvo = new CpModuleVO();
						mvo.setActiveflag(UFBoolean.valueOf(true));
						mvo.setDevmodulecode(moduleCode);
						mvo.setId(moduleId);
						mvo.setTitle(moduleTitle);
						for(CpModuleVO vo:moduleList){
							if(vo.getTitle()!=null&&vo.getTitle().equals(moduleParent)){
								mvo.setPk_parent(vo.getPk_module());
							}
						}
						
						LFWWfmConnector.saveModule(mvo);
						moduleList.add(mvo);
						modulename.add(moduleTitle);
						modulename.setText(moduleTitle);						
					}				
						newmodule.setEnabled(true);
				}
				
			});
			
			appcategoryname = new ComboDialogField(SWT.NONE);
			appcategoryname.setLabelText(M_application.NewAppNodeManualDialog_11);
			appcategoryname.doFillIntoGrid(group, 3);
			setappcategorynameTtems();
			appcategoryname.selectItem(M_application.NewAppNodeManualDialog_2);
			
			if(!M_application.NewAppNodeManualDialog_2.equals(appcategoryname.getText())) appcategoryname.selectItem(0);
			
			
			
			final Button newappcategory = new Button(group, SWT.BUTTON1);
			newappcategory.setText(M_application.NewAppNodeManualDialog_12);
			newappcategory.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event){
					newappcategory.setEnabled(false);
					newapp = new NewAppInfoDialog(getShell(), M_application.NewAppNodeManualDialog_12);
					if(newapp.open() == IDialogConstants.OK_ID){
						IProject project = LFWPersTool.getCurrentProject();
//						String moduleName = LfwCommonTool.getProjectModuleName(project);
//						CpModuleVO[] modules = LFWWfmConnector.getModules();
//						String pk_module = null;
//						if(modules!=null){
//							for(CpModuleVO module:modules){
//								if(moduleName.equals(module.getDevmodulecode())){
//									pk_module = module.getPk_module();
//									break;
//								}
//							}
//						}
//						if(pk_module==null){
//							CpModuleVO newModule = new CpModuleVO();
//							newModule.setDevmodulecode(moduleName);
//							newModule.setId(moduleName);
//							newModule.setTitle(moduleName);
//							newModule.setActiveflag(UFBoolean.TRUE);
//							pk_module = LFWWfmConnector.saveModule(newModule);
//								
//						}
						
						newappcategory.setEnabled(true);
						CpAppsCategoryVO newappcategoryvo = new CpAppsCategoryVO();
						newappcategoryvo.setActiveflag(UFBoolean.TRUE);
						for(CpModuleVO vo:moduleList){
							if(vo.getTitle()!=null&&vo.getTitle().equals(modulename.getText())){
								newappcategoryvo.setPk_module(vo.getPk_module());
							}
						}
//						newappcategoryvo.setPk_module(pk_module);
						newappcategoryvo.setId(appcategoryIdField.getText());
						newappcategoryvo.setTitle(appcategoryNameField.getText());
						LFWWfmConnector.saveAppsCategory(newappcategoryvo);
						setappcategorynameTtems();
						appcategoryname.setText(appcategoryNameField.getText());
					}else{
						newappcategory.setEnabled(true);
					}
				}
			});
			
			
			funcNodeField = new StringDialogField();
			funcNodeField.setLabelText(M_application.NewAppNodeManualDialog_13);
			funcNodeField.doFillIntoGrid(group, 4);
			

			funcNameField = new StringDialogField();
			funcNameField.setLabelText(M_application.NewAppNodeManualDialog_14);
			funcNameField.doFillIntoGrid(group, 4);
			
			if(exsitFunc!=null){				
				funcNodeField.setText(exsitFunc.getId());
				funcNameField.setText(exsitFunc.getTitle());
				String cateName = null;
				String modulePK = null;
				CpAppsCategoryVO[] appsCates = LFWWfmConnector.getAppsCategory();				
				for(CpAppsCategoryVO cate:appsCates){
					if(cate.getPk_appscategory().equals(exsitFunc.getPk_appscategory())){
						cateName = cate.getTitle();
						modulePK = cate.getPk_module();						
						break;
					}						
				}
				String moduleName = null;
				CpModuleVO[] modules = LFWWfmConnector.getModules();
				for(CpModuleVO module:modules){
					if(module.getPk_module().equals(modulePK)){
						moduleName = module.getTitle();
						modulename.setText(moduleName);
						break;
					}
				}
				setappcategorynameTtems();
				appcategoryname.setText(cateName);			
				
			}
						
		}
		
		private void setModuleItems(){
			moduleVOs = LFWWfmConnector.getModules();
			List<String> modulenames = new ArrayList<String>();
			moduleList = new ArrayList<CpModuleVO>();
			for(CpModuleVO vo:moduleVOs){
				if(vo.getTitle()!=null&&!vo.getTitle().equals("")){ //$NON-NLS-1$
					modulenames.add(vo.getTitle());
					moduleList.add(vo);
				}
				
			}
			
			modulename.setItems(modulenames.toArray(new String[0]));
		}
		
		/**
		 * 对功能名称下拉框设值
		 */
		private void setappcategorynameTtems(){
			String moduleTitle = modulename.getText();
			String devCode = null;
			for(CpModuleVO vo:moduleList){
				if(vo.getTitle().equals(moduleTitle))
					devCode = vo.getDevmodulecode();
			}
			appCategoryVOs = LFWWfmConnector.getAppsCategoryByDevModule(devCode);
			if(appCategoryVOs ==null){
				appcategoryname.setItems(new String[0]);
				appcategoryname.setText(M_application.NewAppNodeManualDialog_39);
				return;
			}
			int length = appCategoryVOs.length;
			
			Pk_appcategoryids = new String[length];
			Pk_appcategorynames = new String[length];

			for(int i = 0;i<length;i++){
				CpAppsCategoryVO appCategoryVO = appCategoryVOs[i];
				Pk_appcategoryids[i] = appCategoryVO.getId();
				if(appCategoryVO.getTitle()!=null){
				Pk_appcategorynames[i] = appCategoryVO.getTitle();
				}else{
					Pk_appcategorynames[i] = ""; //$NON-NLS-1$
				}
				
			}
			appcategoryname.setItems(Pk_appcategorynames);
			appcategoryname.selectItem(0);
		}



		@Override
		public void dialogFieldChanged(DialogField field) {


		}

	}
	/**
	 * 新建菜单分类对话框
	 * @author guomq1
	 * 2012-8-15
	 */
	private final class NewMenuInfoDialog extends DialogWithTitle {

		/**
		 * @param parentShell
		 * @param title
		 */
		public NewMenuInfoDialog(Shell parentShell, String title) {
			super(parentShell, title);
			// TODO Auto-generated constructor stub
		}
		protected Control createDialogArea(Composite parent){
			final Composite composite = new Composite(parent, SWT.NULL);
			composite.setFont(parent.getFont());
			composite.setLayout(new GridLayout(3, true));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			menucategoryIdField = new StringDialogField();
			menucategoryIdField.setLabelText(M_application.NewAppNodeManualDialog_15);
			menucategoryIdField.doFillIntoGrid(composite, 3);
			menucategoryNameField = new StringDialogField();
			menucategoryNameField.setLabelText(M_application.NewAppNodeManualDialog_4);
			menucategoryNameField.doFillIntoGrid(composite, 3);
			return composite;
			}
		protected void okPressed() {
			if(menucategoryIdField.getText().equals("")||menucategoryNameField.getText().equals(""))
			{
				MessageDialog.openWarning(null, "", M_application.NewAppNodeManualDialog_37);
				return;
			}
			CpMenuCategoryVO mcvo = LFWWfmConnector.getMenuCategoryById(menucategoryIdField.getText());
			if(mcvo != null){
				    MessageDialog.openWarning(null, M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_17);
				    menucategoryIdField.setText(""); //$NON-NLS-1$
					menucategoryNameField.setText(""); //$NON-NLS-1$
					return;				
				}
			super.okPressed();
		}
	}
	
	
	
	/**
	 * 新建父菜单对话框
	 * @author guomq1
	 * 2012-8-17
	 */
/*	private final class NewParentMenuDialog extends DialogWithTitle {

		public NewParentMenuDialog(Shell parentShell, String title) {
			super(parentShell, title);
			// TODO Auto-generated constructor stub
		}

		protected Control createDialogArea(Composite parent){
			final Composite composite = new Composite(parent, SWT.NULL);
			composite.setFont(parent.getFont());
			composite.setLayout(new GridLayout(3, true));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			parentmenuIdField = new StringDialogField();
			parentmenuIdField.setLabelText("菜单编码");
			parentmenuIdField.doFillIntoGrid(composite, 3);
			parentmenuNameField = new StringDialogField();
			parentmenuNameField.setLabelText("菜单名称");
			parentmenuNameField.doFillIntoGrid(composite, 3);
			return composite;
			}
		protected void okPressed() {
			CpMenuItemVO parentmi = LFWWfmConnector.getMenuItemById(parentmenuIdField.getText());
			if(parentmi != null){
					if(MessageDialog.openConfirm(getShell(), "警告", "该菜单已存在,请重新输入")){
						parentmenuIdField.setText("");
						parentmenuNameField.setText("");
						return;
					}
					
				}
			super.okPressed();
		}
	}*/

	/**
	 * 新建模块对话框
	 * @author qinjianc
	 *
	 */
	private final class NewModuleDialog extends DialogWithTitle {
	
	
		private StringDialogField moduleIdField = null;
		private StringDialogField devModuleCodeField = null;
		private StringDialogField moduleTitleField = null;
		private ComboDialogField parentModuleField = null;
		public NewModuleDialog(Shell parentShell, String title) {
				super(parentShell, title);
				// TODO 自动生成的构造函数存根
			}
		protected Control createDialogArea(Composite parent){
			final Composite composite = new Composite(parent, SWT.NULL);
			composite.setFont(parent.getFont());
			composite.setLayout(new GridLayout(3, true));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			IProject project = LFWPersTool.getCurrentProject();
			String moduleName = LFWPersTool.getProjectModuleName(project);
			devModuleCodeField = new StringDialogField();
			devModuleCodeField.setLabelText(M_application.NewAppNodeManualDialog_40);
			devModuleCodeField.doFillIntoGrid(composite, 3);
			devModuleCodeField.setText(moduleName);
			
			moduleIdField = new StringDialogField();
			moduleIdField.setLabelText(M_application.NewAppNodeManualDialog_41);
			moduleIdField.doFillIntoGrid(composite, 3);
			
			moduleTitleField = new StringDialogField();
			moduleTitleField.setLabelText(M_application.NewAppNodeManualDialog_42);
			moduleTitleField.doFillIntoGrid(composite, 3);
			
			parentModuleField =  new ComboDialogField(SWT.NONE);
			parentModuleField.setLabelText(M_application.NewAppNodeManualDialog_43);
			parentModuleField.doFillIntoGrid(composite, 3);

			List<String> modulenames = new ArrayList<String>();
			for(CpModuleVO vo:moduleList){
				if(vo.getTitle()!=null&&!vo.getTitle().equals("")) //$NON-NLS-1$
					modulenames.add(vo.getTitle());
			}
			parentModuleField.setItems(modulenames.toArray(new String[0]));
			
			return composite;
		}
		protected void okPressed() {
			if(LFWWfmConnector.getModuleById(getModuleId())!=null){
				MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_45);
				moduleIdField.setText(""); //$NON-NLS-1$
				moduleTitleField.setText(""); //$NON-NLS-1$
				parentModuleField.setText(""); //$NON-NLS-1$
			}
			super.okPressed();
		}
		public String getModuleId(){
			return moduleIdField.getText();
		}
		public String getModuleCode(){
			return devModuleCodeField.getText();
		}
		public String getModuleTitle(){
			return moduleTitleField.getText();
		}
		public String getModuleParent(){
			return parentModuleField.getText();
		}
	}

	/**
	 * 新建功能分类对话框
	 * @author guomq1
	 * 2012-8-15
	 */
	private final class NewAppInfoDialog extends DialogWithTitle {

		/**
		 * @param parentShell
		 * @param title
		 */
		public NewAppInfoDialog(Shell parentShell, String title) {
			super(parentShell, title);
			// TODO Auto-generated constructor stub
		}
		protected Control createDialogArea(Composite parent){
			final Composite composite = new Composite(parent, SWT.NULL);
			composite.setFont(parent.getFont());
			composite.setLayout(new GridLayout(3, true));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			appcategoryIdField = new StringDialogField();
			appcategoryIdField.setLabelText(M_application.NewAppNodeManualDialog_18);
			appcategoryIdField.doFillIntoGrid(composite, 3);
			appcategoryNameField = new StringDialogField();
			appcategoryNameField.setLabelText(M_application.NewAppNodeManualDialog_11);
			appcategoryNameField.doFillIntoGrid(composite, 3);
			return composite;
			}
		protected void okPressed() {
			CpAppsCategoryVO acvo = LFWWfmConnector.getAppsCategoryById(appcategoryIdField.getText());
			if(acvo != null){
				if(MessageDialog.openConfirm(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_19)){
					appcategoryIdField.setText(""); //$NON-NLS-1$
					appcategoryNameField.setText(""); //$NON-NLS-1$
					return;
				}

			}
			super.okPressed();
		}
	}
	
	
	
	//点击确定按钮执行注册操作
	
	protected void okPressed() {
		
		if(exsitFunc!=null&&exsitMenu!=null){
			
			if(menucategoryname.getText() == null ||menucategoryname.getText().equals("")){ //$NON-NLS-1$
				MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_37);
				return;
			}
			if(parentmenuitem.getText() == null ||parentmenuitem.getText().equals("")){ //$NON-NLS-1$
				MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_20);
				return;
			}
			if(menuIdField.getText() == null ||menuIdField.getText().equals("") || menuNameField.getText() == null || menuNameField.getText().equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
				MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_21);
				return;
	        }
			if(funcNodeField.getText() == null ||funcNodeField.getText().equals("") || funcNameField.getText() == null || funcNameField.getText().equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
				MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_23);
				return;
			}
			if(!exsitMenu.getCode().equals(menuIdField.getText())){
				if(LFWWfmConnector.getMenuItemById(menuIdField.getText())!=null){
					MessageDialog.openError(null, "Error", "Menu Id has been used");
					return;
				}
			}
			
			exsitMenu.setCode(menuIdField.getText());
			exsitMenu.setName(menuNameField.getText());
			

			if(newparentmenu != null){
				if(newparentmenu.getMenuItemCode()!= null){
					String code = newparentmenu.getMenuItemCode();
					CpMenuItemVO parentMenu = LFWWfmConnector.getMenuItemById(code);
					if(parentMenu!=null){
						String pk_parent = LFWWfmConnector.getMenuItemById(code).getPk_menuitem();
						if(pk_parent!=null)
							exsitMenu.setPk_parent(pk_parent);
					}						
				}
			}

			int i = 0;
//			while(i != -1 && i < Pk_menucategorynames.length){
			for(i = 0 ;i<Pk_menucategorynames.length;i++){
				if(Pk_menucategorynames[i].equals(menucategoryname.getText()))break;
			}
			String Pk_menucategoryid = Pk_menucategoryids[i];
			pk_menucategory = (LFWWfmConnector.getMenuCategoryById(Pk_menucategoryid)).getPk_menucategory();
			
			//设置外键pk_menucategory
			exsitMenu.setPk_menucategory(pk_menucategory);
			//设置外键pk_appsnode
			exsitMenu.setPk_funnode(exsitFunc.getPk_appsnode());
			
			LFWWfmConnector.updateMenuItem(exsitMenu);
			
			if(!exsitFunc.getId().equals(funcNodeField.getText())){
				if(LFWWfmConnector.getAppsNodeById(funcNodeField.getText())!=null){
					MessageDialog.openError(null, "Error", "Function Id has been used");
					return;
				}
			}
			
			exsitFunc.setId(funcNodeField.getText());
			exsitFunc.setTitle(funcNameField.getText());
			
			i = 0;
			while(i < Pk_appcategorynames.length){				
				if(Pk_appcategorynames[i].equals(appcategoryname.getText()))break;
				i++;
			}
			String pk_appcategoryid = Pk_appcategoryids[i];
			
			String pk_appcategory = (LFWWfmConnector.getAppsCategoryById(pk_appcategoryid)).getPk_appscategory();
			exsitFunc.setPk_appscategory(pk_appcategory);
			
			LFWWfmConnector.updateAppsNode(exsitFunc);
			MessageDialog.openInformation(getShell(), M_application.NewAppNodeManualDialog_28, M_application.NewAppNodeManualDialog_29);
			super.okPressed();
			return;
		}
		//验证父菜单是否为空
		if(parentmenuitem.getText() == null ||parentmenuitem.getText().equals("")){ //$NON-NLS-1$
			MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_20);
			return;
		}
		//验证菜单是否为空或者已存在
        if(menuIdField.getText() == null ||menuIdField.getText().equals("") || menuNameField.getText() == null || menuNameField.getText().equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
			MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_21);
			return;
        }else{
			CpMenuItemVO mivo = LFWWfmConnector.getMenuItemById(menuIdField.getText());
			if(mivo != null){
				if(MessageDialog.openConfirm(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_22)){
					menuIdField.setText(""); //$NON-NLS-1$
					menuNameField.setText(""); //$NON-NLS-1$
				    return;	
				}
					
				}
        }
		//验证功能是否为空或者已存在
		if(funcNodeField.getText() == null ||funcNodeField.getText().equals("") || funcNameField.getText() == null || funcNameField.getText().equals("")){ //$NON-NLS-1$ //$NON-NLS-2$
			MessageDialog.openWarning(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_23);
			return;
		}else{
			CpAppsNodeVO anvo = LFWWfmConnector.getAppsNodeById(funcNodeField.getText());
			if(anvo != null){
					if(MessageDialog.openConfirm(getShell(), M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_24)){
						funcNodeField.setText(""); //$NON-NLS-1$
						funcNameField.setText(""); //$NON-NLS-1$
						return;
					}

					
				}
		}
		
		String appId = getApplicationId();
		if(appId!=null){
			//判断该app是否已注册
			CpAppsNodeVO[] exsitedFuncs =  LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+appId+"'"); //$NON-NLS-1$ //$NON-NLS-2$
			if (exsitedFuncs != null && exsitedFuncs.length>0) {
	
				if(MessageDialog.openConfirm(getShell(), M_application.NewAppNodeManualDialog_25, M_application.NewAppNodeManualDialog_26)){
					//删除相关菜单和功能点
					delFuncAndMenuByAppid(appId);
				}
				else return;
				
			}
		}
		//判断功能是否已被注册
		CpAppsNodeVO exsitedFunc =  LFWWfmConnector.getAppsNodeById(funcNodeField.getText());
		if(exsitedFunc!=null){
			if(MessageDialog.openConfirm(getShell(), M_application.NewAppNodeManualDialog_25, M_application.NewAppNodeManualDialog_27)){
				//删除相关功能点和菜单
				delFuncAndMenuByFunc(exsitedFunc);
			}
			else return;
			
		}
		if(exsitedFunc!=null)
			return;
		try{
		//注册功能
		funcRegister(appId);
		//注册菜单
		menuRegister(appId);
		
		String funcId = funcNodeField.getText();
		
		String projectPath = LFWAMCPersTool.getProjectPath();
		Application app = LFWAMCConnector.getApplication(projectPath+"/web/html/applications/"+appId+"/", "application.app"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if(app!=null&&app.getExtendAttribute("templateType")!=null){ //$NON-NLS-1$
			IExtensionPoint extPoint = Platform.getExtensionRegistry().getExtensionPoint("com.yonyou.studio.web.perspective.template"); //$NON-NLS-1$
			for(IConfigurationElement confElement:extPoint.getConfigurationElements()){
				String type = app.getExtendAttribute("templateType").getValue().toString(); //$NON-NLS-1$
				if(confElement.getAttribute("id").equals(type)){ //$NON-NLS-1$
					ITemplatePageFactory factory = (ITemplatePageFactory)confElement.createExecutableExtension("class"); //$NON-NLS-1$
					factory.publish(funcId, appId);
					break;
				}
			}
		}
		
		MessageDialog.openInformation(getShell(), M_application.NewAppNodeManualDialog_28, M_application.NewAppNodeManualDialog_29);
		}
		catch(Exception e){
			MessageDialog.openInformation(getShell(), M_application.NewAppNodeManualDialog_30, M_application.NewAppNodeManualDialog_31+e.getMessage());
			MainPlugin.getDefault().logError(e.getMessage());
		}
		if(MessageDialog.openConfirm(null, M_application.NewAppNodeManualDialog_49, M_application.NewAppNodeManualDialog_50)){
			NewTemplateDialog dialog = new NewTemplateDialog(null, M_application.NewAppNodeManualDialog_51);
			dialog.setNodecode(funcNodeField.getText());
			dialog.setType("all"); //$NON-NLS-1$
			dialog.open();
		}
		super.okPressed();
	}
	

	/**
	 * 获取applicationId
	 */
	public String getApplicationId(){
		if(appidcb==null) 
			return null;
		else{
			int index = appidcb.getSelectionIndex();
			if(index==-1) 
				return null;
			return appFile.get(index);
		}
	}
	
	
	/**
	 * 注册菜单
	 * @param appId
	 */
	private void menuRegister(String appId) {
		CpMenuItemVO newMenuItemVO = new CpMenuItemVO();
		newMenuItemVO.setCode(menuIdField.getText());
		newMenuItemVO.setName(menuNameField.getText());
		

		if(newparentmenu != null){
			if(newparentmenu.getMenuItemCode()!= null){
				String code = newparentmenu.getMenuItemCode();
				CpMenuItemVO parentMenu = LFWWfmConnector.getMenuItemById(code);
				if(parentMenu!=null){
					String pk_parent = LFWWfmConnector.getMenuItemById(code).getPk_menuitem();
					if(pk_parent!=null)
						newMenuItemVO.setPk_parent(pk_parent);
				}						
			}
		}

		
		//设置外键pk_menucategory
		newMenuItemVO.setPk_menucategory(pk_menucategory);
		newMenuItemVO.setIsnotleaf(UFBoolean.FALSE);
		//通过新建的功能编码获取对应的pk_appsnode（注意在此之前新建的功能信息要先保存）
		String pk_appsnode = LFWWfmConnector.getAppsNodeById(funcNodeField.getText()).getPk_appsnode();
		//设置外键pk_appsnode
		newMenuItemVO.setPk_funnode(pk_appsnode);
		
		LFWWfmConnector.saveMenuItem(newMenuItemVO);
		MainPlugin.getDefault().logInfo("insert menuItem " + menuNameField.getText()); //$NON-NLS-1$
	}
	
	/**
	 * 注册功能
	 * @param appId
	 */
	private void funcRegister(String appId){
		CpAppsNodeVO newAppNodeVO = new CpAppsNodeVO();
		newAppNodeVO.setId(funcNodeField.getText());
		newAppNodeVO.setAppid(appId);
		
		//通过selectionIndex获取Pk_menucategoryids数组中相应的Pk_menucategoryid
		String pk_appcategoryid = null;
		if(appcategoryname.getSelectionIndex() != -1){
			pk_appcategoryid = Pk_appcategoryids[appcategoryname.getSelectionIndex()];
		}
		else{
			int i = 0;
			while(i < Pk_appcategorynames.length){				
				if(Pk_appcategorynames[i].equals(appcategoryname.getText()))break;
				i++;
			}
			pk_appcategoryid = Pk_appcategoryids[i];
			
		}
		String pk_appcategory = (LFWWfmConnector.getAppsCategoryById(pk_appcategoryid)).getPk_appscategory();
		newAppNodeVO.setPk_appscategory(pk_appcategory);
		
		newAppNodeVO.setTitle(funcNameField.getText());
		
		newAppNodeVO.setActiveflag(UFBoolean.TRUE);
		
		//获取bcp名称
		String bcpIpath = LFWPersTool.getCurrentBusiCompTreeItem().getIPathStr();
		String bcp = bcpIpath.substring(bcpIpath.lastIndexOf("/")+1,bcpIpath.length()); //$NON-NLS-1$
		newAppNodeVO.setDevcomponent(bcp);
		
		newAppNodeVO.setType(1);
		
		//设置url
		String url = null;
		if(appId==null){
			TreeItem item = LFWPersTool.getCurrentTreeItem();
			if (item instanceof LFWDirtoryTreeItem) {
				url = "app/" + ((LFWDirtoryTreeItem) item).getId(); //$NON-NLS-1$
				newAppNodeVO.setAppid(((LFWDirtoryTreeItem) item).getId());
			}
		}
		else{
			newAppNodeVO.setAppid(appId);
			url = "app/" + appId; //$NON-NLS-1$
		}
		newAppNodeVO.setUrl(url);
		LFWWfmConnector.saveAppsNode(newAppNodeVO);
		MainPlugin.getDefault().logInfo("insert the apps node"); //$NON-NLS-1$
	}

	/**
	 * 应用功能删除相关功能点和菜单
	 * @param exsitedFunc
	 */
	private void delFuncAndMenuByFunc(CpAppsNodeVO exsitedFunc) {
		String pk_funnode = exsitedFunc.getPk_appsnode();		
		CpMenuItemVO[] menuItemVOs = LFWWfmConnector.getMenuItemsByCondition("pk_funnode = '"+pk_funnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
		if(menuItemVOs!=null && menuItemVOs.length>0){
			String pk_menuitem = menuItemVOs[0].getPk_menuitem();
			LFWWfmConnector.delMenuItem(pk_menuitem);
		}
		LFWWfmConnector.delAppsNode(pk_funnode);
		
		String appId = exsitedFunc.getAppid();
		String funcCode = exsitedFunc.getId();
		String queryPk = LFWWfmConnector.getQueryTemplateByNodeCode(funcCode);
		String printPk = LFWWfmConnector.getPrintTemplateByNodeCode(funcCode);
		if(queryPk!=null){
			LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			LFWWfmConnector.delQryTemplate(queryPk);
//			LFWWfmConnector.executeSql("DELETE FROM cp_query_condition WHERE pk_query_template = '"+queryPk+"'");
		}
		if(printPk!=null) {
			LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//			LFWWfmConnector.delPrintTemplate(printPk);
//			LFWWfmConnector.executeSql("DELETE FROM cp_print_condition WHERE pk_print_template = '"+printPk+"'");
		}
		MessageDialog.openInformation(null, M_application.NewAppNodeManualDialog_28, M_application.NewAppNodeManualDialog_32);
	}

	/**
	 * 应用appid删除相关功能点和菜单
	 * @param appId
	 */
	private void delFuncAndMenuByAppid(String appId) {
		TreeItem item = LFWPersTool.getCurrentTreeItem();
		if(item instanceof LFWDirtoryTreeItem){
			String id = appId;
			CpAppsNodeVO[] appsNodeVOs = LFWWfmConnector.getAppsNodeVOsByCondition("appid = '"+id+"'"); //$NON-NLS-1$ //$NON-NLS-2$
			if(appsNodeVOs!=null&&appsNodeVOs.length>0){
				String pk_funnode = appsNodeVOs[0].getPk_appsnode();				
				CpMenuItemVO[] menuItemVOs = LFWWfmConnector.getMenuItemsByCondition("pk_funnode = '"+pk_funnode+"'"); //$NON-NLS-1$ //$NON-NLS-2$
				if(menuItemVOs!=null && menuItemVOs.length>0){
					String pk_menuitem = menuItemVOs[0].getPk_menuitem();
					LFWWfmConnector.delMenuItem(pk_menuitem);
				}
				LFWWfmConnector.delAppsNode(pk_funnode);
				String funcCode = appsNodeVOs[0].getId();
				String queryPk = LFWWfmConnector.getQueryTemplateByNodeCode(funcCode);
				String printPk = LFWWfmConnector.getPrintTemplateByNodeCode(funcCode);
				if(queryPk!=null) {
					LFWWfmConnector.executeSql("UPDATE cp_query_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//					LFWWfmConnector.delQryTemplate(queryPk);
//					LFWWfmConnector.executeSql("DELETE FROM cp_query_condition WHERE pk_query_template = '"+queryPk+"'");
				}
				if(printPk!=null) {
					LFWWfmConnector.executeSql("UPDATE cp_print_template  set nodecode = '$tempcode_" + appId + "' WHERE nodecode = '" + funcCode + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//					LFWWfmConnector.delPrintTemplate(printPk);
//					LFWWfmConnector.executeSql("DELETE FROM cp_print_condition WHERE pk_print_template = '"+printPk+"'");
				}
				MessageDialog.openInformation(null, M_application.NewAppNodeManualDialog_28, M_application.NewAppNodeManualDialog_33);
			}
			else{
				MessageDialog.openWarning(null, M_application.NewAppNodeManualDialog_16, M_application.NewAppNodeManualDialog_34);
			}
		}
	}

}
