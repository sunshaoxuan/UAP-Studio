package nc.uap.lfw.ref.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.ref.LfwRefInfoVO;
import nc.uap.lfw.lang.M_editor;
import nc.uap.lfw.ref.model.IConst;
import nc.uap.lfw.ref.model.MdFieldInfo;
import nc.uap.lfw.ref.model.RefSQLInfo;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RefSQLInfoWizardPage extends AbstractNewRefWizardPage{

	private Combo pkCbo;
	private Combo codeCbo;
	private Combo nameCbo;
	private Group group_grid;
	private Combo fatherCbo;
	private Combo childCbo;
	private Text queryGridTableName;
	private Text queryGridRealTableName;
	private Text queryTreeTableName;
	private Text queryTreeRealTableName;
	private Text codesText;
	private Text namesText;
	private Text hiddensText;
	private Text multiLanText;
	private String[] pks = new String[]{};
	private Text orderbyText;
	private Button setQuery;
	private Map<String, List<MdFieldInfo>> fieldMap;
	private Group group_tree;
	private Combo pkCbo_tree;
	private Combo codeCbo_tree;
	private Combo nameCbo_tree;
	private Text codesText_tree;
	private Combo docjoinCbo_tree;
	private Text multiLanText_tree;
	private int type = 0;
	
	public RefSQLInfoWizardPage(Map<String, Object> context, String pageName) {
		super(context, pageName);
		this.setTitle(M_editor.RefSQLInfoWizardPage_0);
		this.setDescription(M_editor.RefSQLInfoWizardPage_1);
	}

	@Override
	protected void createUI(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		group_tree = new Group(container, SWT.NONE);
		group_tree.setLayout(new GridLayout(5, false));
		group_tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group_tree.setText(M_editor.RefSQLInfoWizardPage_2);
		
		Label pkLabel_tree = new Label(group_tree, SWT.NONE);
		pkLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		pkLabel_tree.setText(M_editor.RefSQLInfoWizardPage_3);
		pkCbo_tree = new Combo(group_tree, SWT.READ_ONLY);
		pkCbo_tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				false, 1, 1));

		Label docjoinLabel_tree = new Label(group_tree, SWT.NONE);
		docjoinLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		docjoinLabel_tree.setText(M_editor.RefSQLInfoWizardPage_4);
		docjoinCbo_tree = new Combo(group_tree, SWT.READ_ONLY);
		docjoinCbo_tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				false, 1, 1));
		new Label(group_tree, SWT.NONE);

		Label codeLabel_tree = new Label(group_tree, SWT.NONE);
		codeLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		codeLabel_tree.setText(M_editor.RefSQLInfoWizardPage_5);
		codeCbo_tree = new Combo(group_tree, SWT.READ_ONLY);
		codeCbo_tree.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				false, 1, 1));
		
		Label nameLabel_tree = new Label(group_tree, SWT.NONE);
		nameLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		nameLabel_tree.setText(M_editor.RefSQLInfoWizardPage_6);
		nameCbo_tree = new Combo(group_tree, SWT.READ_ONLY);
		nameCbo_tree.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(group_tree, SWT.NONE);
		
		Label codesLabel_tree = new Label(group_tree, SWT.NONE);
		codesLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		codesLabel_tree.setText(M_editor.RefSQLInfoWizardPage_7);
		codesText_tree = new Text(group_tree, SWT.NONE);
		GridData gd_codesText_tree = new GridData(GridData.FILL, GridData.FILL, true, false, 4, 1);
		gd_codesText_tree.heightHint = 20;
		codesText_tree.setLayoutData(gd_codesText_tree);
		
		Label fatherLabel = new Label(group_tree, SWT.NONE);
		fatherLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		fatherLabel.setText(M_editor.RefSQLInfoWizardPage_8);
		fatherCbo = new Combo(group_tree, SWT.READ_ONLY);
		fatherCbo.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label childLabel = new Label(group_tree, SWT.NONE);
		childLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		childLabel.setText(M_editor.RefSQLInfoWizardPage_9);
		childCbo = new Combo(group_tree, SWT.READ_ONLY);
		childCbo.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(group_tree, SWT.NONE);
		
		Label mutilLanLabel_tree = new Label(group_tree, SWT.NONE);
		mutilLanLabel_tree.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		mutilLanLabel_tree.setText(M_editor.RefSQLInfoWizardPage_10);
		multiLanText_tree = new Text(group_tree, SWT.NONE);
		GridData gd_multiLanText_tree = new GridData(GridData.FILL, GridData.FILL, true, false, 4, 1);
		gd_multiLanText_tree.heightHint = 20;
		multiLanText_tree.setLayoutData(gd_multiLanText_tree);
		
		Label queryTreeTableNameLabel = new Label(group_tree, SWT.NONE);
		queryTreeTableNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		queryTreeTableNameLabel.setText(M_editor.RefSQLInfoWizardPage_11);
		queryTreeTableName = new Text(group_tree, SWT.NONE);
		GridData gd_queryTreeTableName = new GridData(GridData.FILL, GridData.FILL, true, false, 4, 1);
		gd_queryTreeTableName.heightHint = 20;
		queryTreeTableName.setLayoutData(gd_queryTreeTableName);

		Label queryTreeRealTableNameLabel = new Label(group_tree, SWT.NONE);
		queryTreeRealTableNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		queryTreeRealTableNameLabel.setText(M_editor.RefSQLInfoWizardPage_12);
		queryTreeRealTableName = new Text(group_tree, SWT.NONE);
		GridData gd_queryTreeRealTableName = new GridData(SWT.FILL, GridData.FILL, true, false, 4, 1);
		gd_queryTreeRealTableName.heightHint = 20;
		queryTreeRealTableName.setLayoutData(gd_queryTreeRealTableName);
		
		group_grid = new Group(container, SWT.NONE);
		group_grid.setLayout(new GridLayout(7, false));
		group_grid.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group_grid.setText(M_editor.RefSQLInfoWizardPage_13);
		
		Label pkLabel = new Label(group_grid, SWT.NONE);
		pkLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		pkLabel.setText(M_editor.RefSQLInfoWizardPage_14);
		pkCbo = new Combo(group_grid, SWT.READ_ONLY);
		pkCbo.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label codeLabel = new Label(group_grid, SWT.NONE);
		codeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		codeLabel.setText(M_editor.RefSQLInfoWizardPage_15);
		codeCbo = new Combo(group_grid, SWT.READ_ONLY);
		codeCbo.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));
		
		Label nameLabel = new Label(group_grid, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		nameLabel.setText(M_editor.RefSQLInfoWizardPage_16);
		nameCbo = new Combo(group_grid, SWT.READ_ONLY);
		nameCbo.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(group_grid, SWT.NONE);
		
		Label codesLabel = new Label(group_grid, SWT.NONE);
		codesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		codesLabel.setText(M_editor.RefSQLInfoWizardPage_17);
		codesText = new Text(group_grid, SWT.NONE);
		GridData gd_codesText = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_codesText.heightHint = 20;
		codesText.setLayoutData(gd_codesText);
		
		Label namesLabel = new Label(group_grid, SWT.NONE);
		namesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		namesLabel.setText(M_editor.RefSQLInfoWizardPage_18);
		namesText = new Text(group_grid, SWT.NONE);
		GridData gd_namesText = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_namesText.heightHint = 20;
		namesText.setLayoutData(gd_namesText);
		
		Label hiddensLabel = new Label(group_grid, SWT.NONE);
		hiddensLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		hiddensLabel.setText(M_editor.RefSQLInfoWizardPage_19);
		hiddensText = new Text(group_grid, SWT.NONE);
		GridData gd_hiddensText = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_hiddensText.heightHint = 20;
		hiddensText.setLayoutData(gd_hiddensText);
		
		Label mutilLanLabel = new Label(group_grid, SWT.NONE);
		mutilLanLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		mutilLanLabel.setText(M_editor.RefSQLInfoWizardPage_20);
		multiLanText = new Text(group_grid, SWT.NONE);
		GridData gd_multiLanText = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_multiLanText.heightHint = 20;
		multiLanText.setLayoutData(gd_multiLanText);
		
		Label queryGridTableNameLabel = new Label(group_grid, SWT.NONE);
		queryGridTableNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		queryGridTableNameLabel.setText(M_editor.RefSQLInfoWizardPage_21);
		queryGridTableName = new Text(group_grid, SWT.NONE);
		GridData gd_queryGridTableName = new GridData(GridData.FILL, GridData.FILL, true, false, 5, 1);
		gd_queryGridTableName.heightHint = 20;
		queryGridTableName.setLayoutData(gd_queryGridTableName);
		setQuery = new Button(group_grid, SWT.NONE);
		setQuery.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		setQuery.setText(M_editor.RefSQLInfoWizardPage_22);

		Label queryGridRealTableNameLabel = new Label(group_grid, SWT.NONE);
		queryGridRealTableNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		queryGridRealTableNameLabel.setText(M_editor.RefSQLInfoWizardPage_23);
		queryGridRealTableName = new Text(group_grid, SWT.NONE);
		GridData gd_queryGridRealTableName = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_queryGridRealTableName.heightHint = 20;
		queryGridRealTableName.setLayoutData(gd_queryGridRealTableName);

		Label orderbyLabel = new Label(group_grid, SWT.NONE);
		orderbyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		orderbyLabel.setText(M_editor.RefSQLInfoWizardPage_24);
		orderbyText = new Text(group_grid, SWT.NONE);
		GridData gd_orderbyText = new GridData(GridData.FILL, GridData.FILL, true, false, 6, 1);
		gd_orderbyText.heightHint = 20;
		orderbyText.setLayoutData(gd_orderbyText);
		
		pkCbo_tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(pkCbo_tree.getText().trim().length()>0){
					List<String> fieldList = getFieldsByPk(pkCbo_tree.getText());
					String[] fields = fieldList.toArray(new String[0]);
					docjoinCbo_tree.setItems(fields);
					codeCbo_tree.setItems(fields);
					nameCbo_tree.setItems(fields);
					fatherCbo.setItems(fields);
					childCbo.setItems(fields);
					String codes = fieldList.toString().replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
					codesText_tree.setText(codes.substring(1, codes.length()-1));
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		pkCbo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(pkCbo.getText().trim().length()>0){
					List<String> fieldList = getFieldsByPk(pkCbo.getText());
					List<String> nameFieldList = getNameFieldsByPk(pkCbo.getText());
					String[] fields = fieldList.toArray(new String[0]);
					codeCbo.setItems(fields);
					nameCbo.setItems(fields);
					String codes = fieldList.toString().replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
					codesText.setText(codes.substring(1, codes.length()-1));
					String names = nameFieldList.toString().replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
					namesText.setText(names.substring(1, names.length()-1));
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		setQuery.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				RefGridQuerySetDialog dialog = new RefGridQuerySetDialog(fieldMap, M_editor.RefSQLInfoWizardPage_25);
				if(dialog.open() ==IDialogConstants.OK_ID){
					queryGridTableName.setText(dialog.getSQL());
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private List<String> getFieldsByPk(String pk){
		List<String> fieldList = new ArrayList<String>();
		for(String key:fieldMap.keySet()){
			List<MdFieldInfo> fieldInfos = fieldMap.get(key);
			if(fieldInfos.get(0).getPkField().equals(pk)){
				for(MdFieldInfo info:fieldInfos){
					fieldList.add(info.getField().getId());
				}
			}
		}
		return fieldList;
	}
	
	private List<String> getNameFieldsByPk(String pk){
		List<String> fieldList = new ArrayList<String>();
		for(String key:fieldMap.keySet()){
			List<MdFieldInfo> fieldInfos = fieldMap.get(key);
			if(fieldInfos.get(0).getPkField().equals(pk)){
				for(MdFieldInfo info:fieldInfos){
					fieldList.add(info.getField().getText());
				}
			}
		}
		return fieldList;
	}
	
	private int checkExistString(List<String> items,String item){
		int flag = -1;
		for(int i=0;i<items.size();i++){
			if(items.get(i).equals(item)){
				flag = i;
				break;
			}
		}
		return flag;
	}
	
	@Override
	public void doUpdateModel() {
		if(context!=null&&context.containsKey(IConst.REF_INFO)){
			LfwRefInfoVO refInfoVO = (LfwRefInfoVO) context.get(IConst.REF_INFO);
			if(refInfoVO!=null){
				type = refInfoVO.getRefType();
				if(type==0){
					group_grid.setEnabled(false);
					group_tree.setEnabled(true);
					docjoinCbo_tree.setEnabled(false);
				}else if(type==1){
					group_grid.setEnabled(true);
					group_tree.setEnabled(false);
				}else if(type==2){
					group_grid.setEnabled(true);
					group_tree.setEnabled(true);
					docjoinCbo_tree.setEnabled(true);
				}
					
					
			}
		}
		if(context!=null&&context.containsKey(IConst.REF_TABLEINFO)){
			fieldMap= (Map<String, List<MdFieldInfo>>) context.get(IConst.REF_TABLEINFO);
			List<String> tableList = new ArrayList<String>();
			List<String> pkList = new ArrayList<String>();
			for(String key:fieldMap.keySet()){
				List<MdFieldInfo> fieldInfos = fieldMap.get(key);
				pkList.add(fieldInfos.get(0).getPkField());
				tableList.add(key);
			}
			if(pkCbo.getSelectionIndex()==-1){
				pks = pkList.toArray(new String[0]);
				pkCbo.setItems(pks);
			}else{
				int index = checkExistString(pkList, pkCbo.getText());
				if(index==-1){
					pkList.add(pkCbo.getText());
					pks = pkList.toArray(new String[0]);
					pkCbo.setItems(pks);
					pkCbo.select(pkList.size()-1);
				}else{
					pks = pkList.toArray(new String[0]);
					pkCbo.setItems(pks);
					pkCbo.select(index);
				}
			}
			if(pkCbo_tree.getSelectionIndex()==-1){
				pks = pkList.toArray(new String[0]);
				pkCbo_tree.setItems(pks);
			}else{
				int index = checkExistString(pkList, pkCbo_tree.getText());
				if(index==-1){
					pkList.add(pkCbo_tree.getText());
					pks = pkList.toArray(new String[0]);
					pkCbo_tree.setItems(pks);
					pkCbo_tree.select(pkList.size()-1);
				}else{
					pks = pkList.toArray(new String[0]);
					pkCbo_tree.setItems(pks);
					pkCbo_tree.select(index);
				}
			}

			String table = tableList.toString().replaceAll(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
			if(queryGridRealTableName.getText().trim().length()==0){
				queryGridRealTableName.setText(table.substring(1, table.length()-1));
			}
			if(queryGridTableName.getText().trim().length()==0){
				queryGridTableName.setText(table.substring(1, table.length()-1));
			}
			if(queryTreeRealTableName.getText().trim().length()==0){
				queryTreeRealTableName.setText(table.substring(1, table.length()-1));
			}
			if(queryTreeTableName.getText().trim().length()==0){
				queryTreeTableName.setText(table.substring(1, table.length()-1));
			}
		}
		
		Object object = context.get(IConst.REF_SQLINFO);
		if (object == null || !(object instanceof RefSQLInfo)) {
			object = new RefSQLInfo();
			context.put(IConst.REF_SQLINFO, object);
		}
		RefSQLInfo refsqlinfo = (RefSQLInfo) object;
		refsqlinfo.setType(type);
		refsqlinfo.setPkField(pkCbo.getText());
		refsqlinfo.setCodeField(codeCbo.getText());
		refsqlinfo.setNameField(nameCbo.getText());
		refsqlinfo.setCodesField(codesText.getText());
		refsqlinfo.setNamesField(namesText.getText());
		refsqlinfo.setHiddenNamesField(hiddensText.getText());
		refsqlinfo.setMultiLanField(multiLanText.getText());
		
		refsqlinfo.setClassPkField(pkCbo_tree.getText());
		refsqlinfo.setDocjoinField(docjoinCbo_tree.getText());
		refsqlinfo.setClassCodeField(codeCbo_tree.getText());
		refsqlinfo.setClassNameField(nameCbo_tree.getText());
		refsqlinfo.setClassCodesField(codesText_tree.getText());
		refsqlinfo.setFatherField(fatherCbo.getText());
		refsqlinfo.setChildField(childCbo.getText());
		refsqlinfo.setClassMultiLanField(multiLanText_tree.getText());
		
		refsqlinfo.setGridTableName(queryGridTableName.getText());
		refsqlinfo.setGridRealTableName(queryGridRealTableName.getText());
		refsqlinfo.setOrderbyPart(orderbyText.getText());
		
		refsqlinfo.setTreeTableName(queryTreeTableName.getText());
		refsqlinfo.setTreeRealTableName(queryTreeRealTableName.getText());
	}

	@Override
	public boolean isPageComplete() {
		if(type==0){
			if (pkCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||codeCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||nameCbo_tree.getText().trim().equals("")) {  //$NON-NLS-1$
				return false;
			}
		}else if(type==1){
			if (pkCbo.getText().trim().equals("") //$NON-NLS-1$
					||codeCbo.getText().trim().equals("") //$NON-NLS-1$
					||nameCbo.getText().trim().equals("")) {  //$NON-NLS-1$
				return false;
			}
		}else if(type==2){
			if (pkCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||docjoinCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||codeCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||nameCbo_tree.getText().trim().equals("") //$NON-NLS-1$
					||pkCbo.getText().trim().equals("") //$NON-NLS-1$
					||codeCbo.getText().trim().equals("") //$NON-NLS-1$
					||nameCbo.getText().trim().equals("")) {  //$NON-NLS-1$
				return false;
			}
		}
		
		return true;
	}

	@Override
	protected void validatePage() {
		String pk_tree = pkCbo_tree.getText();
		String docjoin_tree = docjoinCbo_tree.getText();
		String code_tree = codeCbo_tree.getText();
		String name_tree = nameCbo_tree.getText();
		String pk = pkCbo.getText();
		String code = codeCbo.getText();
		String name = nameCbo.getText();
		if(type==0){
			if (pk_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_26);
				setPageComplete(false);
				return;
			}
			if (code_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_27);
				setPageComplete(false);
				return;
			}
			if (name_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_28);
				setPageComplete(false);
				return;
			}
		}else if(type==1){
			if (pk.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_29);
				setPageComplete(false);
				return;
			}
			if (code.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_30);
				setPageComplete(false);
				return;
			}
			if (name.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_31);
				setPageComplete(false);
				return;
			}
		}else if(type==2){
			if (pk_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_26);
				setPageComplete(false);
				return;
			}
			if (docjoin_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_33);
				setPageComplete(false);
				return;
			}
			if (code_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_34);
				setPageComplete(false);
				return;
			}
			if (name_tree.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_35);
				setPageComplete(false);
				return;
			}
			if (pk.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_29);
				setPageComplete(false);
				return;
			}
			if (code.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_30);
				setPageComplete(false);
				return;
			}
			if (name.trim().length() == 0) {
				setErrorMessage(M_editor.RefSQLInfoWizardPage_38);
				setPageComplete(false);
				return;
			}
		}
		super.validatePage();
	}

	@Override
	protected Control[] getModifyListenerControls() {
		return new Control[] { pkCbo_tree,docjoinCbo_tree,codeCbo_tree,nameCbo_tree,pkCbo,codeCbo,nameCbo};
	}

}
