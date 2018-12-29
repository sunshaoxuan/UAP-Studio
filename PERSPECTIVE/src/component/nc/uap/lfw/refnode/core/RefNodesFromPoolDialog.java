package nc.uap.lfw.refnode.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.common.LFWUtility;
import nc.uap.lfw.core.page.config.RefNodeConf;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.lang.M_refnode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
 * 从公共池中得到所有的ds
 * @author zhangxya
 *
 */
public class RefNodesFromPoolDialog extends DialogWithTitle {

	private Tree tree = null;
	private TreeViewer tv = null;
	private IRefNode refnode = null;
	private Map<String, Map<String, IRefNode>> input;
	private boolean createNewRef = false;
	public RefNodesFromPoolDialog(Shell parentShell, String title) {
		super(parentShell,title);
	}
	
	public IRefNode getSelectedRefNode(){
		return refnode;
	}
	public void setRefNode(IRefNode refnode) {
		this.refnode = refnode;
	}
	
	protected void okPressed() {
		TreeItem[] items = tree.getSelection();
		if(items != null && items.length > 0){
			TreeItem item = items[0];
			IRefNode ds = (IRefNode)item.getData();
			setRefNode(ds);
			if(item != null){
				super.okPressed();
			}
		}else{
			MessageDialog.openConfirm(this.getShell(), M_refnode.RefNodesFromPoolDialog_0, M_refnode.RefNodesFromPoolDialog_1);
			tree.setFocus();
		}
	}
	
	protected Point getInitialSize() {
		return new Point(450,500); 
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Button bt = new Button(container, SWT.PUSH);
		bt.setText(M_refnode.RefNodesFromPoolDialog_2);
		bt.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				Button b = (Button) e.getSource();
				createNewRef = true;
				b.getShell().close();
			}

			public void mouseUp(MouseEvent e) {
			}
		});
		
		Group g = new Group(container, SWT.NONE);
		g.setText(M_refnode.RefNodesFromPoolDialog_3);
		g.setLayoutData(new GridData(GridData.FILL_BOTH));
		g.setLayout(new GridLayout());
		
		tv = new TreeViewer(g, SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		tree = tv.getTree();
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		PoolRefNodeContentProvider poolDatasetContent = new PoolRefNodeContentProvider();
		tv.setContentProvider(poolDatasetContent);
		tv.setLabelProvider(new LabelContentProvider());
		//String ctx =LFWPersTool.getProjectPath();
		Map<String, Map<String, IRefNode>> input = getAllPoolRefNodes();
		List<String> parentList = new ArrayList<String>();
		for (Iterator<String> it = input.keySet().iterator(); it.hasNext();) {
			parentList.add(it.next());
		}
		tv.setInput(parentList);
		return container;
	}

	
	
	private Map<String, Map<String, IRefNode>> getAllPoolRefNodes(){
		if(input == null){
			String ctx = LFWUtility.getContextFromResource(LFWPersTool.getCurrentProject());
			//String ctx = LFWPersTool.getProjectModuleName(LFWPersTool.getCurrentProject());
			input = RefRefNodeData.getRefNodes("/" + ctx); //$NON-NLS-1$
		}
		return input;
	}
	
	
	class LabelContentProvider extends LabelProvider{

		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			if(element instanceof String)
				return element.toString();
			else if(element instanceof IRefNode){
				IRefNode ds = (IRefNode) element;
				return ds.getId();
			}
			return ""; //$NON-NLS-1$
		}
		
	}
	
	class PoolRefNodeContentProvider implements ITreeContentProvider{

		public PoolRefNodeContentProvider() {
		}

		public Object[] getChildren(Object parentElement) {
			List<IRefNode> refnodeList = new ArrayList<IRefNode>();
			Map<String, Map<String, IRefNode>> input = getAllPoolRefNodes();
			Map<String, IRefNode> children = input.get(parentElement);
			for (Iterator<String> itchild = children.keySet().iterator(); itchild.hasNext();) {
				IRefNode ds = children.get(itchild.next());
					refnodeList.add(ds);
			}
			RefNodeConf[] transdss = (RefNodeConf[])refnodeList.toArray(new RefNodeConf[refnodeList.size()]);
			for (int i = 0; i < transdss.length; i++) {
				for (int j = transdss.length - 1; j > i; j--) {
					if(transdss[i].getId().compareTo(transdss[j].getId()) > 0){
						RefNodeConf temp = transdss[i];
						transdss[i] = transdss[j];
						transdss[j] = temp;
					}
			
				}
			}
			return transdss;
		}

		public Object getParent(Object element) {
			return null;
//			FuncNodeVO vo = (FuncNodeVO) element;
//			String pk_parent = vo.getPk_parent();
//			if(pk_parent == null)
//				return null;
//			for (int i = 0; i < funvosExfun.length; i++) {
//				if(pk_parent.equals(funvosExfun[i].getPk_func()))
//					return funvosExfun[i];
//			}
//			return null;
		}

		public boolean hasChildren(Object element) {
			if(element instanceof String)
				return true;
			return false;
//			if(version.equals(ExtAttrConstants.VERSION60)){
//				FuncNodeVO vo = (FuncNodeVO) element;
//				String pk = vo.getPk_func();
//				for (int i = 0; i < funvosExfun.length; i++) {
//					if(pk.equals(funvosExfun[i].getPk_parent())){
//						return true;
//					}
//				}
//				return false;
//			}
//			else{
//				FuncNodeVO vo = (FuncNodeVO) element;
//				String pk = vo.getPk_func();
//				for (int i = 0; i < funvos.length; i++) {
//					if(pk.equals(funvos[i].getPk_parent())){
//						return true;
//					}
//				}
//				return false;
//			}
		}

		public Object[] getElements(Object inputElement) {
			if(inputElement != null){
				if(inputElement instanceof List){
					return ((List) inputElement).toArray();
				}
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
	}

	public boolean isCreateNewRef() {
		return createNewRef;
	}

	public void setCreateNewRef(boolean createNewRef) {
		this.createNewRef = createNewRef;
	}

}
	

