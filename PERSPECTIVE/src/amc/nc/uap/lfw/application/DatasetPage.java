package nc.uap.lfw.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


import nc.lfw.design.view.LFWConnector;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.core.datamodel.MdModuleVO;
import nc.uap.lfw.lang.M_application;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;





import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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
 * 模板创建APP向导页-选择数据集
 * @author qinjianc
 *
 */
public class DatasetPage extends WizardPage{
	
	private TypeGroup typeGroup;
	private DetailGroup detailGroup;
	
	
	private TreeViewer treeViewer;
	private List<MdComponnetVO> allComponents;
	private List<MdClassVO> allClasses;
	private List<MdModuleVO> allModuels;
	private Dataset dataset;
	
	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}


	protected DatasetPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_THDPAGE_DESC)); 
	}

	private final class TypeGroup extends Observable implements 
	IDialogFieldListener{
		
		final Group group ;
		final SelectionButtonDialogField mdRadio;
		final SelectionButtonDialogField blankRadio;
		
		public TypeGroup(Composite composite){
			
			final int numColumns = 3;
			
			group = new Group(composite, SWT.NONE);
			
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLayout(new GridLayout(numColumns, false));
			group.setText("选择数据集类型"); //$NON-NLS-1$
			mdRadio = new SelectionButtonDialogField(SWT.RADIO);
			mdRadio.setLabelText(M_application.DatasetPage_0);
			mdRadio.doFillIntoGrid(group, numColumns);
			blankRadio = new SelectionButtonDialogField(SWT.RADIO);
			blankRadio.setLabelText(M_application.DatasetPage_1);
			blankRadio.doFillIntoGrid(group, numColumns);
			mdRadio.setSelection(true);
			mdRadio.setDialogFieldListener(this);						
		}

		protected void fireEvent() {
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
	private final class DetailGroup implements Observer,
	IDialogFieldListener {
		
		final Group group;
		
		public DetailGroup(Composite composite){
			group = new Group(composite, SWT.NONE);
			group.setLayout(new GridLayout(1, false));
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 3;
			group.setLayoutData(layoutData);
			Composite container = new Composite(group, SWT.NONE);
			container.setLayout(new GridLayout(1, true));
			container.setLayoutData(new GridData(600,300));
			Group grouId = new Group(container, SWT.NONE);
			grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			grouId.setLayout(new GridLayout(2,false));
				
			Label label = new Label(grouId, SWT.NONE);
			label.setText(M_application.DatasetPage_2);
			
			Text searchText = new Text(grouId, SWT.NONE);
			searchText.setLayoutData(new GridData(220,15));
			searchText.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent e) {
					Text t =(Text) e.widget;
					String str = t.getText();
					buildTree(str);
				}
			});
		
			treeViewer = new TreeViewer(container,SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
			Tree tree = treeViewer.getTree();
			tree.setLayoutData(new GridData(GridData.FILL_BOTH));
			treeViewer.setContentProvider(new FuncContentProvider());
			treeViewer.setLabelProvider(new LabelContentProvider());
			treeViewer.setInput(getALlModules());
			treeViewer.getTree().addMouseListener(new MouseListener(){

				public void mouseDoubleClick(MouseEvent e) {
//					Tree tree = (Tree) e.getSource();
//					TreeItem treeItem = tree.getSelection()[0];
//					if(treeItem.getData() instanceof MdClassVO)
//						okPressed();
				}

				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					Tree tree = (Tree) e.getSource();
					TreeItem treeItem = tree.getSelection()[0];		
					if(!(treeItem.getData() instanceof MdClassVO)){
//						MessageDialog.openError(null, "元数据选择", "请选择元数据!");
						return;
					}
					MdClassVO classvo = (MdClassVO)treeItem.getData();
					TreeItem parentItem = treeItem.getParentItem();
					MdComponnetVO compvo = null;
					if(parentItem != null){
						compvo = (MdComponnetVO)parentItem.getData();
					}
					else{
						String compoId = classvo.getComponentid();
						List<MdComponnetVO> components = getAllComponents();
						int size = components.size();
						for (int i = 0; i < size; i++) {
							MdComponnetVO compVO = (MdComponnetVO) components.get(i);
							if(compVO.getId().equals(compoId)){
								compvo = compVO;
								break;
							}
						}
					}
					String compName = null;

					compName = compvo.getNamespace();
					String objmeta = compName + "." + classvo.getName(); //$NON-NLS-1$
					MainPlugin.getDefault().logError(M_application.DatasetPage_4 + objmeta);
					MdDataset mdds = new MdDataset();
					mdds.setObjMeta(objmeta);
					MdDataset ds = LFWConnector.getMdDataset(mdds);
					setDataset(ds);
				}

				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
		
		private void buildTree(String filter){
			if(filter == null || filter.equals("")){ //$NON-NLS-1$
				treeViewer.setInput(getALlModules());
				return;
			}
			List<MdClassVO> classes = getAllClasses();
			List<MdClassVO> showClasses = new ArrayList<MdClassVO>();
			int size = classes.size();
			for (int i = 0; i < size; i++) {
				MdClassVO classVO = classes.get(i);
				if(classVO.getDisplayname().toLowerCase().indexOf(filter.toLowerCase()) != -1)
					showClasses.add(classVO);
			}
			treeViewer.setInput(showClasses);
		}

		@Override
		public void update(Observable o, Object arg) {
			if(!typeGroup.isMDDataset()){
				group.setVisible(false);
			}
			else group.setVisible(true);
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
				
		
	}
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		typeGroup = new TypeGroup(composite);
		detailGroup = new DetailGroup(composite);
		typeGroup.addObserver(detailGroup);
		
		setControl(composite);
	}
	
	class LabelContentProvider extends LabelProvider{
		public Image getImage(Object element) {
//			ImageDescriptor imageDes = null;
			Image currentImage = null;
			if(element instanceof MdModuleVO){
				currentImage = 	ImageProvider.module;
				
			}else if(element instanceof MdComponnetVO){
				currentImage = 	ImageProvider.component;
			}
			else if(element instanceof MdClassVO){
				currentImage = 	ImageProvider.mdclass;
			}
			if(currentImage != null)
				return currentImage;
			else 
				return super.getImage(element);
		}

		public String getText(Object element) {
			if(element instanceof MdModuleVO){
				MdModuleVO vo = (MdModuleVO) element;
				return  vo.getDisplayname();
			}else if(element instanceof MdComponnetVO){
				MdComponnetVO comvo = (MdComponnetVO)element;
				return comvo.getDisplayname();
			}else if(element instanceof MdClassVO){
				MdClassVO classvo = (MdClassVO)element;
				return classvo.getDisplayname();
				
			}else return null;
		}
		
	}
	
	class FuncContentProvider implements ITreeContentProvider{
		
		List<MdComponnetVO> components = getAllComponents();
		List<MdClassVO> classes = getAllClasses();
		
		public FuncContentProvider() {
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
			}else if(parentElement instanceof MdComponnetVO){
				MdComponnetVO component = (MdComponnetVO)parentElement;
				List<MdClassVO> list = new ArrayList<MdClassVO>();
				int size = classes.size();
				for (int i = 0; i < size; i++) {
					MdClassVO mdc = classes.get(i);
					if(mdc.getComponentid().equals(component.getId()))
						list.add(mdc);
				}
				return list.toArray();
			}
			else return null;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if(element instanceof MdModuleVO){
				return true;
			}else if(element instanceof MdComponnetVO){
				return true;
			}else
				return false;
		}

		@SuppressWarnings("unchecked")
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

	//得到所有modulevo
	@SuppressWarnings("unchecked")
	public List<MdModuleVO> getALlModules(){
		if(allModuels == null){
			allModuels = LFWConnector.getAllModulse();
		}
		return allModuels;
	}
	
	//得到所有组件vo
	@SuppressWarnings("unchecked")
	private List<MdComponnetVO> getAllComponents(){
		if(allComponents == null){
			allComponents = LFWConnector.getAllComponents();
		}
		return allComponents;
	}
	
	//得到所有的classvo
	@SuppressWarnings("unchecked")
	private List<MdClassVO> getAllClasses(){
		if(allClasses == null){
			allClasses =  LFWConnector.getAllClasses();
		}
		return allClasses;
	}
	


}

