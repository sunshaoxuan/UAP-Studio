/**
 * TODO
 */
package nc.uap.lfw.template.mastersecondly;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nc.lfw.design.view.LFWConnector;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.core.datamodel.MdModuleVO;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 主子列表单据选择主子表数据集并设置外键
 * @author guomq1
 * 2012-8-1
 */
public class DoubleDsSelectPage extends WizardPage {


//	private TypeGroup typeGP;
	private DoubleDsDetailGroup doubleDsDg;
	private TreeViewer treeViewer;
	private List<MdComponnetVO> allcomps;
	private List<MdClassVO> allClasses;
	private List<MdModuleVO> allModuels;
	private LfwView widget;
	MdComponnetVO componentVo = null;


	
	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

/**
	 * @return the componentVo
	 */
	public MdComponnetVO getComponentVo() {
		return componentVo;
	}

	/**
	 * @param componentVo the componentVo to set
	 */
	public void setComponentVo(MdComponnetVO componentVo) {
		this.componentVo = componentVo;
	}

	/*
	public Dataset getMasterDs() {
		return masterds;
	}

	public void setMasterDs(Dataset masterds) {
		this.masterds = masterds;
	}
	public Dataset getSecondlyDs() {
		return secondlyds;
	}

	public void setSecondlyDs(Dataset secondlyds) {
		this.secondlyds = secondlyds;
	}
	public String getForeignKey() {
		return foreignkey;
	}

	public void setForeignKey(String foreignkey) {
		this.foreignkey = foreignkey;
	}*/
	/**
	 * @param pageName
	 */
	public DoubleDsSelectPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_THDDPAGE_DESC));
		
	}


	/**
	 * TODO
	 * @author guomq1
	 * 2012-8-1
	 */
	public final class TypeGroup extends Observable implements IDialogFieldListener {

		final Group group;
		final SelectionButtonDialogField mdRadio;
		final SelectionButtonDialogField blankRadio;
		public TypeGroup(Composite composite)
        {
        	final int column = 1;
        	group = new Group(composite, SWT.NONE);
        	
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLayout(new GridLayout(column, false));
			group.setText(uap.lfw.lang.M_template.DoubleDsSelectPage_0000/*选择数据集类型*/); //$NON-NLS-1$
			mdRadio = new SelectionButtonDialogField(SWT.RADIO);
			mdRadio.setLabelText(M_template.DoubleDsSelectPage_0);
			mdRadio.doFillIntoGrid(group, column);
			blankRadio = new SelectionButtonDialogField(SWT.RADIO);
			blankRadio.setLabelText(M_template.DoubleDsSelectPage_1);
			blankRadio.doFillIntoGrid(group, column);
			mdRadio.setSelection(true);
			mdRadio.setDialogFieldListener(this);	
        	
        }
        protected void fireEvent(){
        	setChanged();
        	notifyObservers();
        }
        
		@Override
		public void dialogFieldChanged(DialogField field) {
			fireEvent();

		}
		public boolean isMDDataset(){
			return mdRadio.isSelected();
		}

	}
	

	/**
	 * TODO
	 * @author guomq1
	 * 2012-8-1
	 */
	public final class DoubleDsDetailGroup extends Observable implements IDialogFieldListener, Observer {
	    final Group group;
	    public DoubleDsDetailGroup(Composite composite){
	    	group = new Group(composite,SWT.NONE);
	    	group.setLayout(new GridLayout(1, false));
	    	GridData gdata = new GridData(GridData.FILL_BOTH);
	    	gdata.horizontalSpan = 4;
	    	group.setLayoutData(gdata);
	    	
	    	final Composite comp = new Composite(group, SWT.NONE);
	    	comp.setLayout(new GridLayout(1,true));
//	    	comp.setLocation(600, 300);
	    	comp.setLayoutData(new GridData(600,300));
	    	
	    	Group gDsInf = new Group(comp,SWT.NONE);
	    	gDsInf.setLayout(new GridLayout(6, false));
	    	gDsInf.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    	
	    	//选择主表元数据
	    	Label masterDslb = new Label(gDsInf, SWT.NONE);
	    	masterDslb.setText(M_template.DoubleDsSelectPage_2);
	    	
			Text searchText = new Text(gDsInf, SWT.NONE);
			searchText.setLayoutData(new GridData(220,15));
			searchText.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent e) {
					Text t =(Text) e.widget;
					String str = t.getText();
					buildTree(str);
				}
			});
		
			treeViewer = new TreeViewer(comp,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
			Tree tree = treeViewer.getTree();
			tree.setLayoutData(new GridData(GridData.FILL_BOTH));
			treeViewer.setContentProvider(new TVContentProvider());
			treeViewer.setLabelProvider(new LabelContentProvider());
			treeViewer.setInput(getALlModules());
			treeViewer.getTree().addMouseListener(new MouseListener(){
//
//				private void okPressed() {
//					TreeItem treeItem = treeViewer.getTree().getSelection()[0];
//					if(!(treeItem.getData() instanceof MdComponnetVO)){
//						MessageDialog.openError(null, M_template.DoubleDsSelectPage_3, M_template.DoubleDsSelectPage_4);
//						return;
//					}
//					MdComponnetVO componentVo = (MdComponnetVO)treeItem.getData();
//					LfwView widget = LFWPersTool.getCurrentWidget();
//					widget = NCConnector.getMdDsFromComponent(widget, componentVo.getId());
//					setWidget(widget);
//					
//				}

			public void mouseDown(MouseEvent e) {
				Tree tree = (Tree) e.getSource();
				TreeItem treeItem = tree.getSelection()[0];	
				if(treeItem.getData() instanceof MdComponnetVO){
					MdComponnetVO componentVo = (MdComponnetVO)treeItem.getData();
//						String compName = componentVo.getNamespace();
					setComponentVo(componentVo);
				}
				else {
					setComponentVo(null);
					
				}
				
			}


			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
			
		});
	    }
	    	
		/**
		 * @param str
		 */
		private void buildTree(String filter) {
			if(filter == null || filter.equals("")){ //$NON-NLS-1$
				treeViewer.setInput(getALlModules());
				return;
			}
			List<MdComponnetVO> components = getAllComponents();
			List<MdComponnetVO> showComponents = new ArrayList<MdComponnetVO>();
			int size = components.size();
			for (int i = 0; i < size; i++) {
				MdComponnetVO componentVO = components.get(i);
				if(componentVO.getDisplayname().toLowerCase().indexOf(filter.toLowerCase()) != -1)
					showComponents.add(componentVO);
			}
			treeViewer.setInput(showComponents);
		}
			
		class LabelContentProvider extends LabelProvider {
			public Image getImage(Object element) {
				Image currentImage = null;
	
				if (element instanceof MdModuleVO) {
					currentImage = ImageProvider.module;
	
				} else if (element instanceof MdComponnetVO) {
					currentImage = ImageProvider.component;
				}
				if (currentImage != null)
					return currentImage;
				else
					return super.getImage(element);
			}
	
			public String getText(Object element) {
				if (element instanceof MdModuleVO) {
					MdModuleVO vo = (MdModuleVO) element;
					return vo.getDisplayname()+"["+vo.getName()+"]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				else if (element instanceof MdComponnetVO) {
					MdComponnetVO comvo = (MdComponnetVO) element;
					return comvo.getDisplayname()+"["+comvo.getName()+"]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				else
					return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Observer#update(java.util.Observable,
		 * java.lang.Object)
		 */
		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
	
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener
		 * #dialogFieldChanged(org.eclipse.jdt.internal.ui.wizards.dialogfields.
		 * DialogField)
		 */
		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
	
		}
	}
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		doubleDsDg = new DoubleDsDetailGroup(composite);
		setControl(composite);

	}

	/**
	 * @author guomq1
	 * 2012-8-1
	 */
	class TVContentProvider implements ITreeContentProvider {
		List<MdComponnetVO> components = getAllComponents();
		//List<MdClassVO> classes = getAllClasses();
		
		public TVContentProvider() {
		}

		public Object[] getChildren(Object parentElement) {
			if(parentElement instanceof MdModuleVO){
				MdModuleVO md = (MdModuleVO)parentElement;
				List<MdComponnetVO> list = new ArrayList<MdComponnetVO>();
				int size = components.size();
				for (int i = 0; i < size; i++) {
					MdComponnetVO comp = components.get(i);
					if(comp.getOwnmodule() != null && comp.getOwnmodule().equals(md.getId()))
						list.add(comp);
				}
				return list.toArray();
			}
//			else if(parentElement instanceof MdComponnetVO){
//				MdComponnetVO component = (MdComponnetVO)parentElement;
//				List<MdClassVO> list = new ArrayList<MdClassVO>();
//				int size = classes.size();
//				for (int i = 0; i < size; i++) {
//					MdClassVO mdc = classes.get(i);
//					if(mdc.getComponentid().equals(component.getId()))
//						list.add(mdc);
//				}
//				return list.toArray();
//			}
			else return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if(element instanceof MdModuleVO){
				return true;
			}else if(element instanceof MdComponnetVO){
				return false;
			}
			else 
				return false;
		}

		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof List)
				return ((List) inputElement).toArray();
			else 
				return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}
	}
//	/**
//	 * 得到所有ClassVO
//	 * @return allClasses
//	 */
//	private List<MdClassVO> getAllClasses() {
//        if(allClasses == null){
//        	allClasses = LFWConnector.getAllClasses();
//        }
//		return allClasses;
//	}
	/**
	 * 得到所有module
	 * @return allModuels
	 */
	@SuppressWarnings("unchecked")
	private List<MdModuleVO> getALlModules(){
		if(allModuels == null){
			allModuels = LFWConnector.getAllModulse();
		}
		return allModuels;
	}
	
	/**
	 * 得到所有组件VO
	 * @return allcomps
	 */
	private List<MdComponnetVO> getAllComponents() {
		if(allcomps == null){
			allcomps = LFWConnector.getAllComponents();
		}
		return allcomps;
	}
	
	public IWizardPage getNextPage() {
		if (getWizard() == null) {
    		return null;
    	}
		MdComponnetVO vo = getComponentVo();
		if(vo != null) {				
			List entitys = LFWConnector.getEntity(vo.getId());
			if(entitys == null){
				MessageDialog.openError(null, M_template.DoubleDsSelectPage_5, M_template.DoubleDsSelectPage_6);
				return null;
			}
		}
		return getWizard().getPage(WEBPersPlugin.getResourceString(WEBPersConstants.KEY_TEMP_FORTHPAGE_DESC));
	}
}



