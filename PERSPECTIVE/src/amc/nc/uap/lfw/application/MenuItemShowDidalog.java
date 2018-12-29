/**
 * 
 */
package nc.uap.lfw.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.cpb.org.vos.CpMenuCategoryVO;
import nc.uap.cpb.org.vos.CpMenuItemVO;
import nc.uap.lfw.lang.M_application;
import nc.vo.pub.lang.UFBoolean;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * @TODO 菜单树
 * @author guomq1
 * @data 2012-8-20
 */
public class MenuItemShowDidalog extends DialogWithTitle {

	private CpMenuItemVO menuItem;
	private String menuItemName;
	private String menuItemCode;
	private MenuItemTree menuItemTree;
	private TreeViewer treeViewer;
	private String menuCategory;
	private String menuCateName;
	private List<String> secondlyMenuItemList;
	private List<String> childMenuItemList;
	private List<String> thirdlyMenuItemList;
	private List<String> parentMenuItemList = new ArrayList<String>();
	private List<CpMenuItemVO> menuItemVoList = new ArrayList<CpMenuItemVO>();
	private List<String> grandParentMenuItemList = new ArrayList<String>();
	private CpMenuItemVO treeItemVo;
	private CpMenuItemVO parentItemVo;
	private Button editmenubt;
	private Button newmenubt;
	private Button delmenubt;
	
	

	/**
	 * 构造函数
	 * 
	 * @param parentShell
	 * @param title
	 */
	public MenuItemShowDidalog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造函数,传入上个对话框的菜单分类名称值
	 * 
	 * @param parentShell
	 * @param title
	 * @param menuCategory
	 */
	public MenuItemShowDidalog(Shell parentShell, String title,
			String menuCategory, String menuCateName) {
		super(parentShell, title);
		this.menuCategory = menuCategory;
		this.menuCateName = menuCateName;

	}

	protected Control createDialogArea(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		new MenuItemTree(composite);
		return composite;
	}

	/**
	 * @TODO TODO
	 * @author guomq1
	 * @data 2012-8-20
	 */
	private final class MenuItemTree extends Observable implements
			IDialogFieldListener, Observer {
		final Group group;

		public MenuItemTree(Composite composite) {
			group = new Group(composite, SWT.NONE);
			group.setLayout(new GridLayout(1, false));
			GridData gdata = new GridData(GridData.FILL_BOTH);
			gdata.horizontalSpan = 3;
			group.setLayoutData(gdata);
			Composite comp = new Composite(group, SWT.NONE);
			comp.setLayout(new GridLayout(1, true));
			comp.setLayoutData(new GridData(600, 300));

			Group groupID = new Group(comp, SWT.NONE);
			groupID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			groupID.setLayout(new GridLayout(4, false));

			newmenubt = new Button(groupID, SWT.BUTTON1);
			newmenubt.setText(M_application.MenuItemShowDidalog_0);
			newmenubt.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					newmenubt.setEnabled(false);
					// new NewMenuItemAction("add","新建菜单");
					NewMenuItemOnMenuItemTreeDialog newMenuItemOnMenuItemDialog = null;
//					if (treeItemVo != null) {
						newMenuItemOnMenuItemDialog = new NewMenuItemOnMenuItemTreeDialog(
								getShell(), M_application.MenuItemShowDidalog_1, menuCategory, menuCateName,
								treeItemVo);
						if (newMenuItemOnMenuItemDialog.open() == IDialogConstants.OK_ID) {

							String a = newMenuItemOnMenuItemDialog.parentmenuitemField
									.getText().trim();
							String newmenuItemId = newMenuItemOnMenuItemDialog.menuItemIdField
									.getText().trim();
							String newmenuItemName = newMenuItemOnMenuItemDialog.menuItemNameField
									.getText().trim();
							CpMenuItemVO newMenuVo = new CpMenuItemVO();
							newMenuVo.setCode(newmenuItemId);
							newMenuVo.setName(newmenuItemName);
							newMenuVo.setPk_menucategory(menuCategory);
							newMenuVo.setIsnotleaf(UFBoolean.TRUE);
							if(newMenuItemOnMenuItemDialog.getParentMenuItemVo()!=null){
								newMenuVo.setPk_parent(newMenuItemOnMenuItemDialog.getParentMenuItemVo().getPk_menuitem());
							}							
							LFWWfmConnector.saveMenuItem(newMenuVo);
							treeViewer.setExpandedState(treeViewer.getTree().getSelection()[0].getData(), true);
							TreeItem selItem = treeViewer.getTree().getSelection()[0];
							TreeItem addItem = new TreeItem(selItem,SWT.NONE);
							addItem.setData(newMenuVo);
							addItem.setText(newMenuVo.getName());
							treeViewer.add(treeViewer.getTree().getSelection()[0], addItem);
							treeViewer.setExpandedState(treeViewer.getTree().getSelection()[0].getData(), true);

						} else {
							newmenubt.setEnabled(true);

						}
						newmenubt.setEnabled(true);
					}
//				}
			});

			editmenubt = new Button(groupID, SWT.BUTTON1);
			editmenubt.setText(M_application.MenuItemShowDidalog_2);
			editmenubt.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					editmenubt.setEnabled(false);
					// new NewMenuItemAction("edit","编辑菜单");
//					if (parentItemVo != null && treeItemVo != null) {
						NewMenuItemOnMenuItemTreeDialog newMenuItemOnMenuItemDialog = new NewMenuItemOnMenuItemTreeDialog(
								getShell(), M_application.MenuItemShowDidalog_3, menuCategory, menuCateName,
								treeItemVo, parentItemVo);
						if (newMenuItemOnMenuItemDialog.open() == IDialogConstants.OK_ID) {

							String newmenuItemId = newMenuItemOnMenuItemDialog.menuItemIdField
									.getText().trim();
							String newmenuItemName = newMenuItemOnMenuItemDialog.menuItemNameField
									.getText().trim();
							CpMenuItemVO newMenuVo = new CpMenuItemVO();
							newMenuVo.setCode(newmenuItemId);
							newMenuVo.setName(newmenuItemName);
							newMenuVo.setPk_menucategory(menuCategory);
							if(newMenuItemOnMenuItemDialog.getParentMenuItemVo()!=null){
								newMenuVo.setPk_parent(newMenuItemOnMenuItemDialog.getParentMenuItemVo().getPk_menuitem());
							}	
							LFWWfmConnector.delMenuItem(treeItemVo
									.getPk_menuitem());
							LFWWfmConnector.saveMenuItem(newMenuVo);
							/*
							 * TreeItem newMenuTreeItem = new
							 * TreeItem(treeViewer.getTree(), 0);
							 * newMenuTreeItem.setData(newMenuVo);
							 */
							// treeViewer.getTree().clearAll(true);
							treeViewer.setInput(getMenuItems());
							// treeViewer.getTree().update();
							// treeViewer.refresh();
						} else {
							editmenubt.setEnabled(true);
						}
//					}
					editmenubt.setEnabled(true);

				}
			});

			delmenubt = new Button(groupID, SWT.BUTTON1);
			delmenubt.setText(M_application.MenuItemShowDidalog_4);
			delmenubt.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					Object element = treeViewer.getTree().getSelection()[0].getData();
					if (element instanceof CpMenuItemVO) {
						if (parentMenuItemList.contains(((CpMenuItemVO) element)
								.getPk_menuitem())){
							MessageDialog.openWarning(null, "提示", "此菜单含有子菜单，不能删除，请删除子菜单后再执行删除");
							return;
						}
							
					}
					if(MessageDialog.openConfirm(null, "提示", "此操作将删除此菜单项，是否确定删除？")){
						LFWWfmConnector.delMenuItem(treeItemVo.getPk_menuitem());
						treeViewer.remove(treeViewer.getTree().getSelection()[0].getData());
	//					treeViewer.setInput(getMenuItems());
						if(treeItemVo.getPk_parent()==null){
							treeItemVo = null;
						}
					}					
				}
			});

			treeViewer = new TreeViewer(comp, SWT.SINGLE | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.FULL_SELECTION);
			Tree tree = treeViewer.getTree();

			tree.setLayoutData(new GridData(GridData.FILL_BOTH));
			treeViewer.setContentProvider(new TVContentProvider());
			treeViewer.setLabelProvider(new LabelContentProvider());
			treeViewer.setInput(getMenuItems());
			treeViewer.setExpandedState(treeViewer.getTree().getItems()[0].getData(), true);
			treeViewer.getTree().addMouseListener(new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseDown(MouseEvent e) {
					Tree tree = (Tree) e.getSource();
					TreeItem treeItem = tree.getSelection()[0];
					if (!(treeItem.getData() instanceof CpMenuItemVO)) {
						treeItemVo = null;
						editmenubt.setEnabled(false);
						delmenubt.setEnabled(false);
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						return;
					}
					editmenubt.setEnabled(true);
					delmenubt.setEnabled(true);
					newmenubt.setEnabled(true);
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					treeItemVo = (CpMenuItemVO) treeItem.getData();
					if (tree.getSelection()[0].getParentItem() != null) {
						TreeItem parentItem = tree.getSelection()[0]
								.getParentItem();
						if(parentItem.getData() instanceof CpMenuItemVO){
							parentItemVo = (CpMenuItemVO) parentItem.getData();							
							newmenubt.setEnabled(false);
							if(parentItem.getParentItem().getData() instanceof CpMenuItemVO)	
								{
									getButton(IDialogConstants.OK_ID).setEnabled(false);
								}
						}
					}
					/**
					 * 增加右键菜单
					 * 
					 * @param manager
					 */
					/*
					 * IMenuManager manager = new MenuManager(); manager.add(new
					 * NewMenuItemAction("add","新建菜单")); manager.add(new
					 * NewMenuItemAction("edit","编辑菜单"));
					 * 
					 * ((LFWBasicTreeItem)treeItem).addMenuListener(manager);
					 */
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});

			/*
			 * TreeItem menuCateItem = null; if(menuCateName != null){
			 * menuCateItem = new TreeItem(tree,0);
			 * menuCateItem.setText(menuCateName); }else{
			 * 
			 * }
			 */

			/*
			 * Map<String, String> menuParentMap = new HashMap<String,
			 * String>(); Map<String, String> menuMap = new HashMap<String,
			 * String>();
			 * 
			 * for(CpMenuItemVO menuItemVo: menuItemVos){
			 * 
			 * menuParentMap.put(
			 * menuItemVo.getPk_parent(),menuItemVo.getPk_menuitem());
			 * menuMap.put(menuItemVo.getPk_menuitem(), menuItemVo.getName()); }
			 */
			/*
			 * if(!menuParentMap.isEmpty()&& !menuMap.isEmpty()){
			 * Collection<String> pk_parentCollection = menuParentMap.values();
			 * // Set<String> pk_menuItemSet = menuParentMap.keySet(); //
			 * Set<String> menuItemNameSet = menuMap.keySet(); for(int i = 0;i <
			 * pk_parentCollection.size();i++){ String pk_parentstr =
			 * pk_parentCollection.iterator().next().toString(); String
			 * pk_menuItem = menuParentMap.get(pk_parentstr);
			 * if(pk_parentstr.equals("~")||pk_parentstr.equals("")||
			 * pk_parentstr == null){
			 * 
			 * secondlyMenuItemList.add(pk_menuItem); TreeItem secondlyItem =
			 * new TreeItem(menuCateItem, 0); String secondlyMenuItemName =
			 * menuMap.get(pk_menuItem).toString();
			 * secondlyItem.setText(secondlyMenuItemName); if(menuCateItem !=
			 * null){ TreeItem secondlyItem = new TreeItem(menuCateItem, 0); }
			 * 
			 * }
			 * 
			 * } if(secondlyMenuItemList != null){ int
			 * secondlyMenuItemListLength = secondlyMenuItemList.size();
			 * 
			 * for(int i =0; i < secondlyMenuItemListLength;i++){ String
			 * secondlyMenuItem = secondlyMenuItemList.get(i).toString();
			 * thirdlyMenuItemList.add(menuParentMap.get(secondlyMenuItem));
			 * 
			 * } for(int i= 0; i< thirdlyMenuItemList.size(); i++){ String
			 * thirdlyMenuItem = thirdlyMenuItemList.get(i); String
			 * thirdlyMenuItemName = menuMap.get(thirdlyMenuItem).toString();
			 * TreeItem thirdlyItem = new TreeItem(menuCateItem, 0);
			 * thirdlyItem.setText(thirdlyMenuItemName); } } }
			 */
			// treeViewer.setLabelProvider(new LabelContentProvider());
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

	/**
	 * @TODO TODO
	 * @author guomq1
	 * @data 2012-8-20
	 */
	private final class LabelContentProvider extends LabelProvider implements
			IBaseLabelProvider {

		public String getText(Object element) {
			if (element instanceof CpMenuItemVO) {
				return ((CpMenuItemVO) element).getName();
			}
			if (element instanceof CpMenuCategoryVO) {
				return ((CpMenuCategoryVO) element).getTitle();
			} else
				return null;
		}

	}

	private final class TVContentProvider implements ITreeContentProvider {

		/*
		 * public TVContentProvider(){
		 * 
		 * }
		 */
		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else
				return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			List<CpMenuItemVO> childMenuItemVoList = new ArrayList<CpMenuItemVO>();
			if (parentElement instanceof CpMenuItemVO) {
				CpMenuItemVO cpmenuItemVo = (CpMenuItemVO) parentElement;

				int size = menuItemVoList.size();
				if (size != 0) {
					for (CpMenuItemVO menuItemVo : menuItemVoList) {

						if (menuItemVo.getPk_parent() != null
								&& menuItemVo.getPk_parent().equals(
										cpmenuItemVo.getPk_menuitem())) {
							childMenuItemVoList.add(menuItemVo);

						}

					}
					return childMenuItemVoList.toArray();

				} else
					return null;

			}
			else if(parentElement instanceof CpMenuCategoryVO){
				CpMenuCategoryVO cpMenuCategoryVO = (CpMenuCategoryVO)parentElement;
				int size = menuItemVoList.size();
				if (size != 0) {
					for (CpMenuItemVO menuItemVo : menuItemVoList) {

						if (menuItemVo.getPk_parent() == null
								&& menuItemVo.getPk_menucategory().equals(
										cpMenuCategoryVO.getPk_menucategory())) {
							childMenuItemVoList.add(menuItemVo);

						}

					}
					return childMenuItemVoList.toArray();

				} else
					return null;
			}else
				return null;

		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {

			if (element instanceof CpMenuItemVO) {
				if (parentMenuItemList.contains(((CpMenuItemVO) element)
						.getPk_menuitem()))
					return true;
			}
			if(element instanceof CpMenuCategoryVO){
				if(grandParentMenuItemList.size()>0)
					return true;
			}
			return false;

		}

	}

//	private List<CpMenuItemVO> getMenuItems() {
	private List<CpMenuCategoryVO> getMenuItems() {
		CpMenuItemVO[] menuItemVos = LFWWfmConnector
				.getMenuItemsByCondition("pk_menucategory = '" + menuCategory //$NON-NLS-1$
						+ "'"); //$NON-NLS-1$

		List<CpMenuItemVO> grandParentMenuItemVoList = new ArrayList<CpMenuItemVO>();

		List<CpMenuItemVO> parentMenuItemVoList = new ArrayList<CpMenuItemVO>();

		// menuItemVoList =
		// LFWWfmConnector.getMenuItemsByCondition("pk_menucategory = '" +
		// menuCategory +"'");
		grandParentMenuItemList.clear();
		parentMenuItemList.clear();	
		menuItemVoList.clear();
		for (CpMenuItemVO menuItemVo : menuItemVos) {
			if (menuItemVo.getPk_parent() == null) {
				if (!grandParentMenuItemList.contains(menuItemVo.getPk_menuitem())) {
//					grandParentMenuItemVoList.add(menuItemVo);
					grandParentMenuItemList.add(menuItemVo.getPk_menuitem());
				}
				} else if (!parentMenuItemList.contains(menuItemVo.getPk_parent())) {
					parentMenuItemList.add(menuItemVo.getPk_parent());
				}

			// if(menuItemVo.getPk_parent()!= null){
			// if(!parentMenuItemList.contains(menuItemVo.getPk_parent())){
			// parentMenuItemList.add(menuItemVo.getPk_parent());
			// }
			// }
			menuItemVoList.add(menuItemVo);

		}
		/*
		 * for(CpMenuItemVO menuItemVo: menuItemVos){
		 * if(parentMenuItemList.size()!=0){ for(int i =
		 * 0;i<parentMenuItemList.size();i++){
		 * if(menuItemVo.getPk_menuitem().equals(parentMenuItemList.get(i))){
		 * parentMenuItemVoList.add(menuItemVo); } } } }
		 */
		CpMenuCategoryVO cateVo = new CpMenuCategoryVO();
		cateVo.setTitle(menuCateName);
		cateVo.setId(menuItemCode);
		cateVo.setPk_menucategory(menuCategory);
		List<CpMenuCategoryVO> cateList = new ArrayList<CpMenuCategoryVO>();
		cateList.add(cateVo);
		return cateList;
	}

	private List<String> getPrentMenuItems() {
		return parentMenuItemList;
	}

	/**
	 * @param menuItem
	 *            the menuItem to set
	 */
	public void setMenuItem(CpMenuItemVO menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * @return the menuItem
	 */
	public CpMenuItemVO getMenuItem() {
		return menuItem;
	}

	/**
	 * @param menuItem
	 *            the menuItem to set
	 */
	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}

	/**
	 * @return the menuItem
	 */
	public String getMenuItemName() {
		return menuItemName;
	}

	/**
	 * @param menuItem
	 *            the menuItem to set
	 */
	public void setMenuItemCode(String menuItemCode) {
		this.menuItemCode = menuItemCode;
	}

	/**
	 * @return the menuItem
	 */
	public String getMenuItemCode() {
		return menuItemCode;
	}

	protected void okPressed() {

		setMenuItem(treeItemVo);
		setMenuItemName(treeItemVo.getName());
		setMenuItemCode(treeItemVo.getCode());

		super.okPressed();
	}

	/**
	 * @TODO TODO
	 * @author guomq1
	 * @data 2012-8-22
	 */
	private final class NewMenuItemAction extends Action {
		NewMenuItemOnMenuItemTreeDialog newMenuItemOnMenuItemDialog;
		String operate = null;

		public NewMenuItemAction(String oper) {
			super(M_application.MenuItemShowDidalog_7);
		}

		public NewMenuItemAction(String oper, String title) {
			super(title);
			this.operate = oper;
		}

		public void run() {
			if ("add".equals(operate)) { //$NON-NLS-1$
				newMenuItemOnMenuItemDialog = new NewMenuItemOnMenuItemTreeDialog(
						getShell(), M_application.MenuItemShowDidalog_9, menuCategory, menuCateName,
						treeItemVo);
				newMenuItemOnMenuItemDialog.open();
			} else if ("edit".equals(operate)) { //$NON-NLS-1$
				if (parentItemVo != null && treeItemVo != null) {
					newMenuItemOnMenuItemDialog = new NewMenuItemOnMenuItemTreeDialog(
							getShell(), M_application.MenuItemShowDidalog_11, menuCategory, menuCateName,
							treeItemVo, parentItemVo);
					newMenuItemOnMenuItemDialog.open();
				}
			}
			// newMenuItemOnMenuItemDialog.open();
		}

	}

}
