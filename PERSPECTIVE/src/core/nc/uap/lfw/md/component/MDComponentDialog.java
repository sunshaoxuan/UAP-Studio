package nc.uap.lfw.md.component;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.design.view.LFWConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.aciton.NCConnector;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.core.datamodel.MdModuleVO;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.lang.M_lfw_component;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 引入组件对话框
 * @author zhangxya
 *
 */
public class MDComponentDialog extends DialogWithTitle{

	private TreeViewer treeViewer;
	private List<MdComponnetVO> allComponents;
	private List<MdModuleVO> allModuels;
	private LfwView widget;
//	private Dataset dataset;
//
//	public Dataset getDataset() {
//		return dataset;
//	}
//
//	public void setDataset(Dataset dataset) {
//		this.dataset = dataset;
//	}

	
	public LfwView getWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	public MDComponentDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}
	
	protected Point getInitialSize() {
		return new Point(400, 400); 
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grouId.setLayout(new GridLayout(2,false));
			
		Label label = new Label(grouId, SWT.NONE);
		label.setText(M_lfw_component.MDComponentDialog_0);
		
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
				Tree tree = (Tree) e.getSource();
				TreeItem treeItem = tree.getSelection()[0];
				if(treeItem.getData() instanceof MdClassVO)
					okPressed();
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		return container;
	}
	
	
	protected void okPressed() {
		TreeItem treeItem = treeViewer.getTree().getSelection()[0];
		if(!(treeItem.getData() instanceof MdComponnetVO)){
			MessageDialog.openError(null, M_lfw_component.MDComponentDialog_1, M_lfw_component.MDComponentDialog_2);
			return;
		}
		MdComponnetVO componentVo = (MdComponnetVO)treeItem.getData();
		widget = LFWPersTool.getCurrentWidget();
		widget = NCConnector.getMdDsFromComponent(widget, componentVo.getId());
		setWidget(widget);
		super.okPressed();
		
	}
	
	private void buildTree(String filter){
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
		
	class LabelContentProvider extends LabelProvider{
		public Image getImage(Object element) {
			Image currentImage = null;
			if(element instanceof MdModuleVO){
				currentImage = 	ImageProvider.module;
				
			}else if(element instanceof MdComponnetVO){
				currentImage = 	ImageProvider.component;
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
			}
			else return null;
		}
		
	}
	
	class FuncContentProvider implements ITreeContentProvider{
		
		List<MdComponnetVO> components = getAllComponents();
		//List<MdClassVO> classes = getAllClasses();
		
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
}

