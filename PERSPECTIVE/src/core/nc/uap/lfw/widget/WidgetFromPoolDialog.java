package nc.uap.lfw.widget;

import java.util.ArrayList;
import java.util.Iterator;
//import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.page.LfwUIComponent;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.design.view.LFWAMCConnector;
import nc.uap.lfw.editor.common.tools.LFWAMCPersTool;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.lang.M_pagemeta;
import nc.uap.lfw.perspective.views.LFWViewPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 从公共池中引用widget
 * 
 * @author zhangxya
 * 
 */
public class WidgetFromPoolDialog extends DialogWithTitle {

	private DetailGroup detailgp;

	private Tree tree = null;
	private TreeViewer tv = null;
	private LfwView widget = null;
	Map<String, LfwView> pubViewMap = null;

	List contextList = null;
	Table detailTable = null;

	public WidgetFromPoolDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	public LfwView getSelectedWidget() {
		return widget;
	}

	public void setWidget(LfwView widget) {
		this.widget = widget;
	}

	protected void okPressed() {
		// TreeItem[] items = tree.getSelection();
		// if(items != null && items.length > 0){
		// TreeItem item = items[0];
		// LfwWidget widget = (LfwWidget)item.getData();
		// setWidget(widget);
		// if(item != null){
		// super.okPressed();
		// }
		// }else{
		// MessageDialog.openConfirm(this.getShell(), "提示", "请选择一个引用的VIEW");
		// tree.setFocus();
		// }
		String widgetid = detailgp.getSelectItem();
		if (widgetid != null) {
			String ctx = getContextPath();
			LfwView widget = pubViewMap.get(widgetid);
			setWidget(widget);
			if (widget != null)
				super.okPressed();
		} else {
			MessageDialog.openConfirm(this.getShell(), M_pagemeta.WidgetFromPoolDialog_0, M_pagemeta.WidgetFromPoolDialog_1);
		}

	}

	protected Point getInitialSize() {
		return new Point(900, 600);
	}

	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		// tv = new TreeViewer(container,
		// SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		// tree = tv.getTree();
		// tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		// PoolWidgetContentProvider poolWidgetContent = new
		// PoolWidgetContentProvider();
		// tv.setContentProvider(poolWidgetContent);
		// tv.setLabelProvider(new LabelContentProvider());
		// //String ctx =LFWPersTool.getProjectPath();
		// Map<String, Map<String, LfwWidget>> input = getAllPoolWidgets();
		// input.remove(null);
		// List<String> parentList = new ArrayList<String>();
		// for (Iterator<String> it = input.keySet().iterator(); it.hasNext();)
		// {
		// parentList.add(it.next());
		// }
		// tv.setInput(parentList);
		// contextgp = new ContextGroup(container);
		// 数据
		Group grouId = new Group(container, SWT.NONE);
		grouId.setLayoutData(new GridData(GridData.FILL));
		grouId.setLayout(new GridLayout(2, false));

		Label label = new Label(grouId, SWT.NONE);
		label.setText(M_pagemeta.WidgetFromPoolDialog_2);

		Text searchText = new Text(grouId, SWT.NONE);
		searchText.setLayoutData(new GridData(220, 15));
		searchText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text text = (Text) e.widget;
				String str = text.getText();
				showselwindow(str);
			}
		});
		detailgp = new DetailGroup(container);
		// contextgp.addObserver(detailgp);
		return container;
	}

	private void showselwindow(String filter){
//		List<LfwWindow> winConfList = LFWAMCConnector.getCacheWindows();
		detailTable.removeAll();
//		windowsort(winConfList);
		for (Iterator<LfwView> itchild = pubViewMap.values().iterator(); itchild.hasNext();) {
			LfwView widget = itchild.next();
			if(widget.getId().toLowerCase().indexOf(filter.toLowerCase())>-1){
				TableItem item = new TableItem(detailTable, SWT.NULL);
				String[] widgetDetail = new String[4];
				widgetDetail[0] = widget.getId();
				widgetDetail[1] = widget.getComponentId();
				widgetDetail[2] = widget.getCaption() == null ? widget.getId() : widget.getCaption();
				widgetDetail[3] = widget.getDesc();
				item.setText(widgetDetail);
			}
		}
//		TableItem item = new TableItem(windowtable, SWT.NONE);
//		item.setText(new String[]{filter, filter});
	}
	
	// private final class ContextGroup extends Observable implements
	// IDialogFieldListener{
	//
	// public ContextGroup(Composite composite){
	// final Group group = new Group(composite, SWT.NONE);
	// group.setLayoutData(new GridData(GridData.FILL_BOTH));
	// group.setLayout(new GridLayout(1, false));
	//			group.setText("context");//$NON-NLS-1$	
	// contextList = new List(group, SWT.SINGLE);
	// contextList.setLayoutData(new GridData(GridData.FILL_BOTH));
	// // Map<String, Map<String, LfwView>> input = getAllPoolWidgets();
	// Map<String, String> ctxMap = new HashMap<String, String>();
	// IProject[] bcpProjects = LFWPersTool.getOpenedBcpJavaProjects();
	// if (bcpProjects != null && bcpProjects.length > 0) {
	// for (IProject project : bcpProjects) {
	// ctxMap.put(LFWUtility.getContextFromResource(project), "/" +
	// LFWUtility.getContextFromResource(project));
	// }
	// }
	// ctxMap.remove(null);
	// if(ctxMap!=null){
	// for (Iterator<String> it = ctxMap.keySet().iterator(); it.hasNext();) {
	// contextList.add(it.next());
	// }
	// }
	// contextList.addSelectionListener(new SelectionListener(){
	// @Override
	// public void widgetDefaultSelected(SelectionEvent e) {
	// // TODO Auto-generated method stub
	// setChanged();
	// notifyObservers();
	// }
	//
	// @Override
	// public void widgetSelected(SelectionEvent e) {
	// // TODO Auto-generated method stub
	// setChanged();
	// notifyObservers();
	// }
	//
	// });
	//
	// }
	// @Override
	// public void dialogFieldChanged(DialogField field) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// protected void fireEvent() {
	// setChanged();
	// notifyObservers();
	// }
	// public int getTempIndex(){
	// return contextList.getSelectionIndex();
	// }
	//
	// }
	private final class DetailGroup {
		public DetailGroup(Composite composite) {
			final Group group = new Group(composite, SWT.NONE);
			group.setLayout(new FillLayout());
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 4;
			group.setLayoutData(layoutData);
			group.setText("detail");//$NON-NLS-1$	
			detailTable = new Table(group, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
			detailTable.setHeaderVisible(true);
			detailTable.setLinesVisible(true);
			TableColumn column1 = new TableColumn(detailTable, SWT.CENTER);
			column1.setText("id"); //$NON-NLS-1$
			column1.pack();
			TableColumn column2 = new TableColumn(detailTable, SWT.CENTER);
			column2.setText(M_pagemeta.WidgetFromPoolDialog_3);
			column2.pack();
			TableColumn column3 = new TableColumn(detailTable, SWT.CENTER);
			column3.setText("name"); //$NON-NLS-1$
			column3.pack();
			TableColumn column4 = new TableColumn(detailTable, SWT.CENTER);
			column4.setText("description"); //$NON-NLS-1$
			column4.pack();
			column1.setWidth(200);
			column2.setWidth(200);
			column3.setWidth(150);
			column4.setWidth(200);

			String ctx = getContextPath();
			pubViewMap = LFWAMCConnector.getPublicViews(ctx);
			detailTable.removeAll();

			for (Iterator<LfwView> itchild = pubViewMap.values().iterator(); itchild.hasNext();) {
				LfwView widget = itchild.next();
				if (widget.getId() != null) {
					TableItem item = new TableItem(detailTable, SWT.NULL);
					String[] widgetDetail = new String[4];
					widgetDetail[0] = widget.getId();
					widgetDetail[1] = widget.getComponentId();
					widgetDetail[2] = widget.getCaption() == null ? widget.getId() : widget.getCaption();
					widgetDetail[3] = widget.getDesc();
					item.setText(widgetDetail);
				}
			}
		}

		public String getSelectItem() {
			TableItem[] selItem = detailTable.getSelection();
			if (selItem == null) {
				return null;
			}
			String id = selItem[0].getText(0);
			String componentId = selItem[0].getText(1);
			String pubId = ""; //$NON-NLS-1$
			if (componentId == null || componentId.equals(LfwUIComponent.ANNOYUICOMPONENT))
				pubId = id;
			else
				pubId = componentId + "." + id; //$NON-NLS-1$
			return pubId;
		}

	}

	// private Map<String, Map<String, LfwView>> input;
	//
	// private Map<String, Map<String, LfwView>> getAllPoolWidgets(){
	// if(input == null){
	// String ctx =
	// LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
	// //String ctx =
	// LFWPersTool.getProjectModuleName(LFWPersTool.getCurrentProject());
	// input = LFWAMCConnector.getAllPublicViews();
	// }
	// return input;
	// }
	private static String getContextPath() {
		String ctxPath = LfwCommonTool.getLfwProjectCtx(LFWAMCPersTool.getCurrentProject());
		if (!ctxPath.startsWith("/")) { //$NON-NLS-1$
			ctxPath = "/" + ctxPath; //$NON-NLS-1$
		}
		return ctxPath;
	}

	// class LabelContentProvider extends LabelProvider{
	//
	// public Image getImage(Object element) {
	// return super.getImage(element);
	// }
	//
	// public String getText(Object element) {
	// if(element instanceof String)
	// return element.toString();
	// else if(element instanceof LfwView){
	// LfwView widget = (LfwView) element;
	// return widget.getId();
	// }
	// return "";
	// }
	//
	// }

	// class PoolWidgetContentProvider implements ITreeContentProvider{
	//
	// public PoolWidgetContentProvider() {
	// }
	//
	// public Object[] getChildren(Object parentElement) {
	// ArrayList<LfwView> widgetList = new ArrayList<LfwView>();
	// Map<String, Map<String, LfwView>> input = getAllPoolWidgets();
	// Map<String, LfwView> children = input.get(parentElement);
	// for (Iterator<String> itchild = children.keySet().iterator();
	// itchild.hasNext();) {
	// LfwView widget = children.get(itchild.next());
	// if(widget.getId()!=null) widgetList.add(widget);
	// }
	// LfwView[] transWidget = (LfwView[])widgetList.toArray(new
	// LfwView[widgetList.size()]);
	// for (int i = 0; i < transWidget.length; i++) {
	// for (int j = transWidget.length - 1; j > i; j--) {
	// if(transWidget[i].getId().compareTo(transWidget[j].getId()) > 0){
	// LfwView temp = transWidget[i];
	// transWidget[i] = transWidget[j];
	// transWidget[j] = temp;
	// }
	// }
	// }
	// return transWidget;
	// }
	//
	// public Object getParent(Object element) {
	// return null;
	// }
	//
	// public boolean hasChildren(Object element) {
	// if(element instanceof String)
	// return true;
	// return false;
	// }
	//
	// public Object[] getElements(Object inputElement) {
	// if(inputElement != null){
	// if(inputElement instanceof ArrayList){
	// return ((ArrayList) inputElement).toArray();
	// }
	// }
	// return new Object[0];
	// }
	//
	// public void dispose() {
	// }
	//
	// public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	// {
	// }
	// }

}
