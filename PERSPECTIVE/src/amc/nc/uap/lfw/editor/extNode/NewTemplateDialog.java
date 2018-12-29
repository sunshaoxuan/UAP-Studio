package nc.uap.lfw.editor.extNode;

import nc.lfw.design.view.LFWWfmConnector;
import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.lfw.lfwtools.perspective.MainPlugin;
import nc.uap.cpb.org.vos.CpAppsNodeVO;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.MdDataset;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.perspective.webcomponent.LFWAppsNodeTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWDirtoryTreeItem;
import nc.uap.lfw.perspective.webcomponent.LFWFuncTreeItem;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uap.lfw.lang.M_extNode;

public class NewTemplateDialog extends DialogWithTitle{
	
	
	
	/**
	 * ID输入框
	 */
	private Text idText;
	/**
	 * 名称输入框
	 */
	private Text nameText;
	/**
	 * 节点名称输入框
	 */
	private Text nodeText;
	/**
	 * 元数据输入框
	 */
	private Text metaText;
	
	private String mdClassid;
	
	private String mdFullName;
	
	private String type;
	
	private String nodecode;
	

	public NewTemplateDialog(Shell parentShell, String title) {
		super(parentShell, title);
		// TODO 自动生成的构造函数存根
	}
	protected Control createDialogArea(Composite parent){

		CpAppsNodeVO nodeVO = LFWWfmConnector.getAppsNodeById(nodecode);
		if(nodeVO==null){
			MessageDialog.openError(null, M_editor.NewTemplateDialog_0, M_editor.NewTemplateDialog_1);
			return null;
		}
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		// ID
		Label idLabel = new Label(container, SWT.NONE);
		idLabel.setText(M_editor.NewTemplateDialog_2);
		idText = new Text(container, SWT.NONE);
		idText.setLayoutData(new GridData(220, 20));
		String bcpPath = LFWPersTool.getBcpPath(LFWPersTool.getCurrentTreeItem());
		String bcpId = bcpPath.substring(bcpPath.lastIndexOf("\\")+1,bcpPath.length()); //$NON-NLS-1$
		idText.setText(nodeVO.getId()); //$NON-NLS-1$
		// 名称
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText(M_editor.NewTemplateDialog_3);
		nameText = new Text(container, SWT.NONE);
		nameText.setLayoutData(new GridData(220, 20));
		nameText.setText(nodeVO.getTitle()+M_extNode.NewTemplateDialog_0000/*模板*/); //$NON-NLS-1$
		
		Label nodeLabel = new Label(container, SWT.NONE);
		nodeLabel.setText(M_editor.NewTemplateDialog_4);
		nodeText = new Text(container, SWT.NONE);
		nodeText.setLayoutData(new GridData(220, 20));
		nodeText.setText(nodeVO.getTitle());
		
		Label metaLabel = new Label(container, SWT.NONE);
		metaLabel.setText(M_editor.NewTemplateDialog_5);
		Composite metaContainer = new Composite(container, SWT.NONE);
		metaContainer.setLayout(new GridLayout(2, false));
		metaContainer.setLayoutData(new GridData(SWT.LEFT));		
		metaText = new Text(metaContainer, SWT.BORDER);
		metaText.setLayoutData(new GridData(210, 16));
		metaText.setText(""); //$NON-NLS-1$
		Button btn_comp = new Button(metaContainer, SWT.NONE);
		btn_comp.setText(M_editor.NewTemplateDialog_6);
		btn_comp.setEnabled(true);
		btn_comp.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				MetaDataSelDialog dialog = new MetaDataSelDialog(null, M_editor.NewTemplateDialog_7);
				if(dialog.open() == IDialogConstants.OK_ID){
					mdClassid = dialog.getMdClassId();
					mdFullName = dialog.getFullName();
					String mdClassName = dialog.getMdClassName();
					metaText.setText(mdClassName);
					
				}
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO 自动生成的方法存根
				
			}		
		});
		
		return container;
	}
	
	protected void okPressed() {
		MainPlugin.getDefault().logInfo(M_editor.NewTemplateDialog_8+idText.getText());
		if(type.equals("query")) //$NON-NLS-1$
			LFWWfmConnector.genQryTemplate(nodecode, mdClassid, idText.getText(), nameText.getText(), mdFullName);
		else if(type.equals("print")) //$NON-NLS-1$
			LFWWfmConnector.genPrintTemplate(nodecode, mdClassid, idText.getText(), nameText.getText(), mdFullName);
		else{
			LFWWfmConnector.genQryTemplate(nodecode, mdClassid, idText.getText(), nameText.getText(), mdFullName);
			LFWWfmConnector.genPrintTemplate(nodecode, mdClassid, idText.getText(), nameText.getText(), mdFullName);
		}
			
		if(type.equals("query")||type.equals("print")){ //$NON-NLS-1$ //$NON-NLS-2$
			LFWFuncTreeItem currentItem = (LFWFuncTreeItem)LFWPersTool.getCurrentTreeItem();
			LFWAppsNodeTreeItem appsNode = new LFWAppsNodeTreeItem(currentItem, currentItem.getFile(), nameText.getText(),null);
			appsNode.setNodecode(nodecode);
			appsNode.setMetaclass(mdClassid);
			String trueType = type.equals("query")?LFWDirtoryTreeItem.QUERY_FOLDER:LFWDirtoryTreeItem.PRINT_FOLDER; //$NON-NLS-1$
			appsNode.setType(trueType);
		}
		MessageDialog.openInformation(null, M_editor.NewTemplateDialog_9, M_editor.NewTemplateDialog_10);
		super.okPressed();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNodecode() {
		return nodecode;
	}
	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}
	
	

}
