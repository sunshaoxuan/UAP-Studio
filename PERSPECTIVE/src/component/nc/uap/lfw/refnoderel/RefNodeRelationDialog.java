package nc.uap.lfw.refnoderel;

import java.util.ArrayList;
import java.util.List;

import nc.lfw.editor.common.DialogWithTitle;
import nc.lfw.editor.common.tools.LFWPersTool;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.MasterFieldInfo;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.lang.M_refnoderel;
import nc.uap.lfw.refnode.RefNodeElementObj;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class RefNodeRelationDialog extends DialogWithTitle{
	
	private class AddDSCommand extends Command{
		public AddDSCommand(){
			super(M_refnoderel.RefNodeRelationDialog_0);
		}
		
		public void execute() {
			redo();
		}

		
		public void redo() {
			}
		
		public void undo() {
		}
		
	}

	private DatasetFieldElementObj source;
	private RefNodeElementObj target;
	private Text fieldText;
	private Text fitersqlText;
	private Combo nullprocessorComp;
	
	private String filterSql;
	public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public String getNullProcessor() {
		return nullProcessor;
	}

	public void setNullProcessor(String nullProcessor) {
		this.nullProcessor = nullProcessor;
	}


	private String nullProcessor;
	
	protected Point getInitialSize() {
		return new Point(500,250); 
	}
	
	public RefNodeRelationDialog(Shell parentShell, String title, DatasetFieldElementObj source, RefNodeElementObj target) {
		super(parentShell, title);
		this.source = source;
		this.target = target;
	}

	protected void okPressed() {
		String nullProcessor = (String)nullprocessorComp.getData(nullprocessorComp.getText());
		//setNullProcessor(nullProcessor);
		String filterSql = fitersqlText.getText();
		//setFilterSql(filterSql);
		
		LfwView widget = LFWPersTool.getCurrentWidget();
		TreeItem treeItem = LFWPersTool.getCurrentTreeItem();
		String id = ((RefNodeRelation)treeItem.getData()).getId();
//		String id = treeItem.getText().substring(WEBProjConstants.COMPONENT_REFNODERELATION.length());
		target.setRefRelationId(id);
		RefNodeRelation refnodeRelation = widget.getViewModels().getRefNodeRelations().getRefNodeRelation(id);
		if(refnodeRelation == null){
			refnodeRelation = new RefNodeRelation();
			refnodeRelation.setId(id);
		}
		//String filterSql = dialog.getFilterSql();
		//String nullProcessor = dialog.getNullProcessor();
		source.setFilterSql(filterSql);
		source.setNullProcessor(nullProcessor);
		geneRefNodeRelation(target, source, filterSql, nullProcessor, refnodeRelation);
		
		super.okPressed();
		if(RefnoderelEditor.getActiveEditor() != null){
			RefnoderelEditor.getActiveEditor().executComand(new AddDSCommand());
		}
	}
	 
	private void geneRefNodeRelation(RefNodeElementObj targetnew, DatasetFieldElementObj sourcenew, String filterSql, String nullProcessor,
			RefNodeRelation refnodeRelation) {
		String detailId = targetnew.getRefnode().getId();
		String fieldID = sourcenew.getField().getId();
		String dsId = sourcenew.getDsId();
		List<MasterFieldInfo> masterFielinfos = refnodeRelation.getMasterFieldInfos();
		if(masterFielinfos == null)
			masterFielinfos = new ArrayList<MasterFieldInfo>();
		MasterFieldInfo masterInfo = refnodeRelation.getMasterFieldInfo(dsId, fieldID);
		if(masterInfo != null)
			refnodeRelation.removeMasterFieldInfo(dsId, fieldID);
		masterInfo = new MasterFieldInfo();
		masterInfo.setDsId(dsId);
		masterInfo.setFieldId(fieldID);
		masterInfo.setFilterSql(filterSql);
		masterInfo.setNullProcess(nullProcessor);
		masterFielinfos.add(masterInfo);
		refnodeRelation.setDetailRefNode(detailId);
		refnodeRelation.setMasterFieldInfos(masterFielinfos);
		LfwView widget = LFWPersTool.getCurrentWidget();
		widget.getViewModels().getRefNodeRelations().addRefNodeRelation(refnodeRelation);
		
	}
    
	
	
	protected Control createDialogArea(Composite parent) {
		Field field = source.getField();
		Composite container = new Composite(parent , SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label detailLabel = new Label(container, SWT.NONE);
		detailLabel.setText(M_refnoderel.RefNodeRelationDialog_1);
		detailLabel.setLayoutData(new GridData(120,15));
		
		BaseRefNode refnode = target.getRefnode();
		Text detailFieldText = new Text(container, SWT.BORDER);
		detailFieldText.setText(refnode.getId());
		detailFieldText.setLayoutData(new GridData(320,15));
		detailFieldText.setEditable(false);
		
		
		Label dsLabel = new Label(container, SWT.NONE);
		dsLabel.setText(M_refnoderel.RefNodeRelationDialog_2);
		dsLabel.setLayoutData(new GridData(120,15));
		
		Text dsfieldText = new Text(container, SWT.BORDER);
		dsfieldText.setLayoutData(new GridData(320,15));
		dsfieldText.setText(source.getDsId());
		dsfieldText.setEditable(false);
		
		
		Label fieldLabel = new Label(container, SWT.NONE);
		fieldLabel.setText(M_refnoderel.RefNodeRelationDialog_3);
		fieldLabel.setLayoutData(new GridData(120,15));
		
		fieldText = new Text(container, SWT.BORDER);
		fieldText.setLayoutData(new GridData(320,15));
		if(field != null)
			fieldText.setText(field.getId());
		fieldText.setEditable(false);
		
		Label fieldsqlLabel = new Label(container, SWT.NONE);
		fieldsqlLabel.setText(M_refnoderel.RefNodeRelationDialog_4);
		fieldsqlLabel.setLayoutData(new GridData(120,15));
		
		fitersqlText = new Text(container, SWT.BORDER| SWT.WRAP);
		fitersqlText.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		fitersqlText.setSize(320, 50);
		
		Label nullProcessorLabel = new Label(container, SWT.NONE);
		nullProcessorLabel.setText(M_refnoderel.RefNodeRelationDialog_5);
		nullProcessorLabel.setLayoutData(new GridData(120,15));
		
		nullprocessorComp = new Combo(container, SWT.NONE);
		nullprocessorComp.setLayoutData(new GridData(300,15));
		nullprocessorComp.add(MasterFieldInfo.NULL);
		nullprocessorComp.setData(MasterFieldInfo.NULL, MasterFieldInfo.NULL);
		
		nullprocessorComp.add(MasterFieldInfo.EMPTY);
		nullprocessorComp.setData(MasterFieldInfo.EMPTY, MasterFieldInfo.EMPTY);
		
		nullprocessorComp.add(MasterFieldInfo.BOTH);
		nullprocessorComp.setData(MasterFieldInfo.BOTH, MasterFieldInfo.BOTH);
		
		nullprocessorComp.add(MasterFieldInfo.IGNORE);
		nullprocessorComp.setData(MasterFieldInfo.IGNORE, MasterFieldInfo.IGNORE);
		setData(source);
		return container;
	}
	
	private void setData(DatasetFieldElementObj source){
		if(source.getFilterSql() != null)
			fitersqlText.setText(source.getFilterSql());
		if(source.getNullProcessor() != null){
			nullprocessorComp.setText(source.getNullProcessor());
		}
		
	}
}
