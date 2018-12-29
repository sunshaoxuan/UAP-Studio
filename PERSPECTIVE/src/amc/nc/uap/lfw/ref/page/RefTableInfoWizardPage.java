package nc.uap.lfw.ref.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import nc.lfw.design.view.LFWConnector;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.core.datamodel.MdClassVO;
import nc.uap.lfw.core.datamodel.MdComponnetVO;
import nc.uap.lfw.core.datamodel.MdModuleVO;
import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_template;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.ref.model.MdFieldInfo;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class RefTableInfoWizardPage extends AbstractNewRefWizardPage {

	private DoubleDsDetailGroup doubleDsDg;
	private DsDetailGroup dsDg;
	private CheckboxTreeViewer mdTreeViewer;
	private CheckboxTreeViewer columnTreeViewer;
	private List<MdComponnetVO> allcomps;
	private List<MdClassVO> allClasses;
	private List<MdModuleVO> allModuels;
//	private LfwView widget;
	private List<MdComponnetVO> selCompList = new ArrayList<MdComponnetVO>();;
	private Map<String, List<MdFieldInfo>> selFieldMap = new HashMap<String, List<MdFieldInfo>>();

	// 得到所有模块
	private List<MdModuleVO> getAllModules() {
		if (allModuels == null) {
			allModuels = LFWConnector.getAllModulse();
		}
		return allModuels;
	}

	// 得到所有组件
	private List<MdComponnetVO> getAllComponents() {
		if (allcomps == null) {
			allcomps = LFWConnector.getAllComponents();
		}
		return allcomps;
	}
	
	//得到所有ClassVO
	private List<MdClassVO> getAllClasses() {
        if(allClasses == null){
        	allClasses = LFWConnector.getAllClasses();
        }
		return allClasses;
	}
	
	private void AddOrRemoveComponent(MdComponnetVO comp,boolean checked){
		boolean flag = false;
		int index = 0;
		for(int i=0;i<selCompList.size();i++){
			MdComponnetVO vo = selCompList.get(i);
			if(vo.getId().equals(comp.getId())){
				flag = true;
				index = i;
				break;
			}
		}
		if(flag && !checked){
			selCompList.remove(index);
		}else if(!flag && checked){
			selCompList.add(comp);
		}
	}

	public RefTableInfoWizardPage(Map<String, Object> context, String pageName) {
		super(context, pageName);
		this.setTitle(M_template.RefTableInfoWizardPage_0);
		this.setDescription(M_template.RefTableInfoWizardPage_1);
	}

	@Override
	protected void createUI(Composite parent) {
		initializeDialogUnits(parent);
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite left_composite = new Composite(container, SWT.NONE);
		left_composite.setLayout(new GridLayout(4, true));
		left_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		doubleDsDg = new DoubleDsDetailGroup(left_composite);

		Composite right_composite = new Composite(container, SWT.NONE);
		right_composite.setLayout(new GridLayout(1, true));
		right_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		dsDg = new DsDetailGroup(right_composite);

	}

	// 元数据选择及筛选界面
	class DoubleDsDetailGroup {
		final Group group;
		final String flag = "1"; //$NON-NLS-1$
		public DoubleDsDetailGroup(Composite composite) {
			group = new Group(composite, SWT.NONE);
			group.setText(M_template.RefTableInfoWizardPage_2);
			group.setLayout(new GridLayout(1, false));
			GridData gdata = new GridData(GridData.FILL_BOTH);
			gdata.horizontalSpan = 4;
			group.setLayoutData(gdata);

			final Composite comp = new Composite(group, SWT.NONE);
			comp.setLayout(new GridLayout(1, true));
			comp.setLayoutData(new GridData(GridData.FILL_BOTH));

			Group gDsInf = new Group(comp, SWT.NONE);
			gDsInf.setLayout(new GridLayout(6, false));
			gDsInf.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			// 选择主表元数据
			Label masterDslb = new Label(gDsInf, SWT.NONE);
			masterDslb.setText(M_template.DoubleDsSelectPage_2);

			Text searchText = new Text(gDsInf, SWT.NONE);
			searchText.setLayoutData(new GridData(200, 15));
			searchText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					Text t = (Text) e.widget;
					buildTree(t.getText());
				}
			});

			mdTreeViewer = new CheckboxTreeViewer(comp, SWT.MULTI | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.BORDER);
			final Tree tree = mdTreeViewer.getTree();
			tree.setLayoutData(new GridData(300,300));
			mdTreeViewer.setContentProvider(new TVContentProvider(flag));
			mdTreeViewer.setLabelProvider(new LabelContentProvider());
			mdTreeViewer.setInput(getAllModules());
			mdTreeViewer.addCheckStateListener(new ICheckStateListener() {

				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					if(event.getElement() instanceof MdComponnetVO) {
						MdComponnetVO componentVo = (MdComponnetVO) event.getElement();
						AddOrRemoveComponent(componentVo, event.getChecked());
					}
					columnTreeViewer.setInput(selCompList);
					columnTreeViewer.expandToLevel(2);
					
				}
			});
			
		}

		/**
		 * 根据过滤条件构造元数据树
		 * @param str
		 */
		private void buildTree(String filter) {
			if (filter == null || filter.equals("")) {  //$NON-NLS-1$
				mdTreeViewer.setInput(getAllModules());
				return;
			}
			List<MdComponnetVO> components = getAllComponents();
			List<MdComponnetVO> showComponents = new ArrayList<MdComponnetVO>();
			int size = components.size();
			for (int i = 0; i < size; i++) {
				MdComponnetVO componentVO = components.get(i);
				if (componentVO.getDisplayname().toLowerCase()
						.indexOf(filter.toLowerCase()) != -1)
					showComponents.add(componentVO);
			}
			mdTreeViewer.setInput(showComponents);
		}

	}
	
	// 元数据属性选择及筛选界面
	class DsDetailGroup {
		final Group group;
		final String flag = "2"; //$NON-NLS-1$
		public DsDetailGroup(Composite composite) {
			group = new Group(composite, SWT.NONE);
			group.setText(M_template.RefTableInfoWizardPage_3);
			group.setLayout(new GridLayout(1, false));
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			columnTreeViewer = new CheckboxTreeViewer(group,
					SWT.MULTI | SWT.BORDER);
			
			columnTreeViewer.setContentProvider(new TVContentProvider(flag));
			columnTreeViewer.setLabelProvider(new LabelContentProvider());
			columnTreeViewer.setInput(null);
			
			columnTreeViewer.expandAll();
			columnTreeViewer.addCheckStateListener(new ICheckStateListener() {

				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					if (event.getChecked()) {
						columnTreeViewer.setSubtreeChecked(event.getElement(), true);
					} else {
						columnTreeViewer.setSubtreeChecked(event.getElement(), false);
					}
					validatePage();
				}
			});
			columnTreeViewer.getTree().setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//			TreeItem[] treeItems = columnTreeViewer.getTree().getItems();
//			for (TreeItem item : treeItems) {
//				if(item.getChecked()){
//					checkAllChildren(item);
//					item.getParentItem().setChecked(true);
//					break;
//				}
//			}
			
			columnTreeViewer.refresh();
		}

	}
	
	/**
	 * 遍历选中树的节点及其子节点。
	 * 
	 * @param item
	 */
	private void checkAllChildren(TreeItem item) {
//		TreeItem parent = item.getParentItem();
//		if(!parent.getChecked()){
//			parent.setChecked(true);
//		}
		Stack<TreeItem> items = new Stack<TreeItem>();
		items.add(item);
		while (!items.isEmpty()) {
			TreeItem current = items.pop();
			current.setChecked(true);
			Collections.addAll(items, current.getItems());
		}
	}
	
	class LabelContentProvider extends LabelProvider {
		public Image getImage(Object element) {
			Image currentImage = null;

			if (element instanceof MdModuleVO) {
				currentImage = ImageProvider.module;
			} else if (element instanceof MdComponnetVO) {
				currentImage = ImageProvider.component;
			} else if(element instanceof MdClassVO){
				currentImage = 	ImageProvider.mdclass;
			} else if(element instanceof MdFieldInfo){
				currentImage = ImageProvider.ds;
			}
			if (currentImage != null)
				return currentImage;
			else
				return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof MdModuleVO) {
				MdModuleVO vo = (MdModuleVO) element;
				return vo.getDisplayname();
			} else if (element instanceof MdComponnetVO) {
				MdComponnetVO comvo = (MdComponnetVO) element;
				return comvo.getDisplayname();
			} else if (element instanceof MdClassVO) {
				MdClassVO classvo = (MdClassVO) element;
				return classvo.getDisplayname();
			} else if (element instanceof MdFieldInfo) {
				MdFieldInfo field = (MdFieldInfo) element;
				return field.getField().getField()+" "+field.getField().getText(); //$NON-NLS-1$
			} else
				return null;
		}
	}

	//元数据树内容提供者
	class TVContentProvider implements ITreeContentProvider {
		List<MdComponnetVO> components = getAllComponents();
		List<MdClassVO> classes = getAllClasses();
		
		private String flag;
		public TVContentProvider(String flag) {
			this.flag = flag;
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof MdModuleVO) {
				MdModuleVO md = (MdModuleVO) parentElement;
				List<MdComponnetVO> list = new ArrayList<MdComponnetVO>();
				int size = components.size();
				for (int i = 0; i < size; i++) {
					MdComponnetVO comp = components.get(i);
					if (comp.getOwnmodule() != null
							&& comp.getOwnmodule().equals(md.getId()))
						list.add(comp);
				}
				return list.toArray();
			}else if(flag.equals("2") && parentElement instanceof MdComponnetVO){ //$NON-NLS-1$
				 MdComponnetVO component = (MdComponnetVO)parentElement;
				 List<MdClassVO> list = new ArrayList<MdClassVO>();
				 int size = classes.size();
				 for (int i = 0; i < size; i++) {
					 MdClassVO mdc = classes.get(i);
					 if(mdc==null||mdc.getComponentid()==null)
						 continue;
					 if(mdc.getComponentid().equals(component.getId()))
						 list.add(mdc);
				 }
				 return list.toArray();
			}else if(parentElement instanceof MdClassVO){
				MdClassVO classVO = (MdClassVO) parentElement;
				String compName = null;
				int size = components.size();
				for (int i = 0; i < size; i++) {
					MdComponnetVO comp = components.get(i);
					if (comp.getId().equals(classVO.getComponentid())){
						compName = comp.getNamespace();
					}
				}
				String objmeta = compName + "." + classVO.getName(); //$NON-NLS-1$
				MdDataset mdds = new MdDataset();
				mdds.setObjMeta(objmeta);
//				MdDataset ds = LFWConnector.getMdDataset(mdds);
				FieldSet fieldSet = mdds.getFieldSet();
				List<MdFieldInfo> list = new ArrayList<MdFieldInfo>();
				for(Field field:fieldSet.getFieldList()){
					MdFieldInfo mdFieldInfo = new MdFieldInfo();
					mdFieldInfo.setField(field);
					mdFieldInfo.setTableName(classVO.getDefaulttablename());
					mdFieldInfo.setPkField(fieldSet.getPrimaryKeyField());
					mdFieldInfo.setParentClassVO(classVO);
					list.add(mdFieldInfo);
				}
				return list.toArray();
			}else{
				return null;
			}
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof MdModuleVO) {
				return true;
			} else if (element instanceof MdComponnetVO) {
				if(flag.equals("2")){ //$NON-NLS-1$
					return true;
				}
				return false;
			} else if (element instanceof MdClassVO) {
				return true;
			}else{
				return false;
			}
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
				return ((List) inputElement).toArray();
			else
				return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}
	
	@Override
	protected void doUpdateModel() {
		Object object = context.get(IConst.REF_TABLEINFO);
		if (object == null || !(object instanceof Map<?, ?>)) {
			context.put(IConst.REF_TABLEINFO, selFieldMap);
		}
		Object[] checkedItems = columnTreeViewer.getCheckedElements();
		selFieldMap.clear();
		Object refObject = context.get(IConst.REF_INFO);
		LfwRefInfoVO refinfo = null;
		if (refObject == null || !(refObject instanceof LfwRefInfoVO)) {
			refObject = new LfwRefInfoVO();
			context.put(IConst.REF_INFO, refObject);
			refinfo = (LfwRefInfoVO) refObject;
		}
		refinfo = (LfwRefInfoVO)context.get(IConst.REF_INFO);
		for (Object item : checkedItems) {
			if (item instanceof MdFieldInfo) {
				MdFieldInfo mdFieldInfo = (MdFieldInfo) item;
				if(!selFieldMap.containsKey(mdFieldInfo.getTableName())){
					selFieldMap.put(mdFieldInfo.getTableName(),new ArrayList<MdFieldInfo>());
				}
				selFieldMap.get(mdFieldInfo.getTableName()).add(mdFieldInfo);
				if(refinfo.getMetadataTypeName()==null){
					refinfo.setMetadataTypeName(mdFieldInfo.getParentClassVO().getName());
				}
			}
		}
	}

	@Override
	public boolean isPageComplete() {
		Object[] checkedItems = columnTreeViewer.getCheckedElements();
		if(checkedItems.length==0){
			return false;
		}
//		if(!validate()){
//			return false;
//		}
		return true ;
	}

	@Override
	protected void validatePage() {
		Object[] checkedItems = columnTreeViewer.getCheckedElements();
		if(checkedItems.length==0){
			setErrorMessage(M_template.RefTableInfoWizardPage_4);
			setPageComplete(false);
			return;
		}
//		if(!validate()){
//			setErrorMessage("元数据实体主键属性为必选项");
//			setPageComplete(false);
//			return;
//		}
		super.validatePage();
	}
	
	private boolean validate(){
		for(String key:selFieldMap.keySet()){
			List<MdFieldInfo> list = selFieldMap.get(key);
			List<String> fieldList = new ArrayList<String>();
			if(list.size()==0){
				return false;
			}
			for(MdFieldInfo info:list){
				fieldList.add(info.getField().getId());
			}
			String pk = list.get(0).getPkField();
			if(!fieldList.contains(pk)){
				return false;
			}
		}
		return true;
	}
	

}
