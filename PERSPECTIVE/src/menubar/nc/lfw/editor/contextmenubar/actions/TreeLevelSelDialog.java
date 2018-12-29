package nc.lfw.editor.contextmenubar.actions;

import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_menubar;
import nc.uap.lfw.tree.TreeGraph;
import nc.uap.lfw.tree.core.TreeLevelElementObj;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * treeLevel—°‘Ò
 * @author zhangxya
 *
 */
public class TreeLevelSelDialog extends DialogWithTitle {
	
	private TreeGraph treeGraph;
	private TreeLevelElementObj treeLevelObj;
	private Combo pagetypeComp = null;
	
	public TreeLevelElementObj getTreeLevelObj() {
		return treeLevelObj;
	}

	public void setTreeLevelObj(TreeLevelElementObj treeLevelObj) {
		this.treeLevelObj = treeLevelObj;
	}


	public TreeLevelSelDialog(Shell parentShell, String title, TreeGraph treeGraph) {
		super(parentShell, title);
		this.treeGraph = treeGraph;
	}
	
	protected Point getInitialSize() {
		return new Point(418, 150); 
	}

	protected void okPressed() {
		treeLevelObj = (TreeLevelElementObj)pagetypeComp.getData(pagetypeComp.getText());
		if(treeLevelObj == null || "".equals(treeLevelObj)){ //$NON-NLS-1$
			MessageDialog.openError(null, M_menubar.TreeLevelSelDialog_0, M_menubar.TreeLevelSelDialog_1);
			return;
		}
		super.okPressed();
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label pageTypeLabel = new Label(container, SWT.NONE);
		pageTypeLabel.setText(M_menubar.TreeLevelSelDialog_2);
		pagetypeComp = new Combo(container, SWT.READ_ONLY);
		pagetypeComp.setLayoutData(new GridData(200,15));
		List<TreeLevelElementObj> treeLevels = treeGraph.getALlTreeLevelElements();
		for (int i = 0; i < treeLevels.size(); i++) {
			TreeLevelElementObj level = treeLevels.get(i);
			pagetypeComp.add(level.getId());
			pagetypeComp.setData(level.getId(), level);
		}
		pagetypeComp.select(0);
		return container;
	}

}

