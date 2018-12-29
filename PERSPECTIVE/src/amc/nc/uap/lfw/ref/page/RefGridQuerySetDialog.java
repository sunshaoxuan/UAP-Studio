package nc.uap.lfw.ref.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.ref.model.MdFieldInfo;

public class RefGridQuerySetDialog extends DialogWithTitle {

	private Combo master;
	private Combo secondly1;
	private Combo field1;
	private Combo secondly2;
	private Combo field2;
	private Map<String, List<MdFieldInfo>> map = new HashMap<String, List<MdFieldInfo>>();
	private String sql;

	public RefGridQuerySetDialog(Map<String, List<MdFieldInfo>> map,
			String title) {
		super(null, title);
		this.map = map;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label masterLabel = new Label(container, SWT.NONE);
		masterLabel.setText("主表");
		master = new Combo(container, SWT.READ_ONLY);
		master.setLayoutData(new GridData(220, 20));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label secondly1Label = new Label(container, SWT.NONE);
		secondly1Label.setText("子表一");
		secondly1 = new Combo(container, SWT.READ_ONLY);
		secondly1.setLayoutData(new GridData(220, 20));

		Label field1Label = new Label(container, SWT.NONE);
		field1Label.setText("对应主表字段");
		field1 = new Combo(container, SWT.READ_ONLY);
		field1.setLayoutData(new GridData(220, 20));

		Label secondly2Label = new Label(container, SWT.NONE);
		secondly2Label.setText("子表二");
		secondly2 = new Combo(container, SWT.READ_ONLY);
		secondly2.setLayoutData(new GridData(220, 20));

		Label field2Label = new Label(container, SWT.NONE);
		field2Label.setText("对应主表字段");
		field2 = new Combo(container, SWT.READ_ONLY);
		field2.setLayoutData(new GridData(220, 20));

		setValues();
		master.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] fields = (String[])getFieldsByTable(master.getText(), false,false).toArray(new String[0]);
				field1.setItems(fields);
				field2.setItems(fields);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		return container;
	}

	private void setValues() {
		List<String> tableList = new ArrayList<String>();
		for (String key : map.keySet()) {
			tableList.add(key);
		}
		String[] tables = (String[])tableList.toArray(new String[0]);
		master.setItems(tables);
		secondly1.setItems(tables);
		secondly2.setItems(tables);
	}

	private List<String> getFieldsByTable(String table, boolean hasPrefix,
			boolean removePk) {
		List<MdFieldInfo> mdFieldInfoList = new ArrayList<MdFieldInfo>();
		List<String> fieldList = new ArrayList<String>();
		mdFieldInfoList = map.get(table);
		for (MdFieldInfo info : mdFieldInfoList) {
			if (removePk && info.getPkField().equals(info.getField().getId())) {
				continue;
			}
			if (hasPrefix) {
				fieldList.add(table + "." + info.getField().getId());
			} else {
				fieldList.add(info.getField().getId());
			}

		}
		return fieldList;
	}

	private String getPkFieldsByTable(String table) {
		List<MdFieldInfo> mdFieldInfoList = new ArrayList<MdFieldInfo>();
		mdFieldInfoList = map.get(table);
		return mdFieldInfoList.get(0).getPkField();
	}

	public String getSQL(){
		return sql;
	}
	
	private void setSql(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("(SELECT ");
		String masterStr = getFieldsByTable(master.getText(), true, false).toString();
		masterStr = masterStr.substring(1, masterStr.length()-1);
		String join1 = "";
		if(secondly1.getText().trim().length()>0){
			String secondly1Str = getFieldsByTable(secondly1.getText(), true, true).toString();
			secondly1Str = secondly1Str.substring(1, secondly1Str.length()-1);
			masterStr = masterStr.replaceAll(master.getText()+"."+field1.getText(), secondly1Str);
			join1 = " LEFT JOIN "+secondly1.getText()+" on "
				+ master.getText()+"."+field1.getText()+"="+secondly1.getText()+"."+getPkFieldsByTable(secondly1.getText());
		}
		String join2 = "";
		if(secondly2.getText().trim().length()>0){
			String secondly2Str = getFieldsByTable(secondly2.getText(), true, true).toString();
			secondly2Str = secondly2Str.substring(1, secondly2Str.length()-1);
			masterStr = masterStr.replaceAll(master.getText()+"."+field2.getText(), secondly2Str);
			join2 = " LEFT JOIN "+secondly2.getText()+" on "
				+ master.getText()+"."+field2.getText()+"="+secondly2.getText()+"."+getPkFieldsByTable(secondly2.getText());
		}
		masterStr = masterStr.replaceAll(" ", "");
		buffer.append(masterStr);
		buffer.append(" FROM "+master.getText());
		buffer.append(join1);
		buffer.append(join2);
		buffer.append(")"+master.getText());
		sql = buffer.toString();
	}

	@Override
	protected void okPressed() {
		if (master.getText().trim().length() == 0) {
			MessageDialog.openInformation(null, "提示", "主表不能为空");
			return;
		}
		if (secondly1.getText().trim().length() == 0
				&& secondly2.getText().trim().length() == 0) {
			MessageDialog.openInformation(null, "提示", "至少选择一个子表");
			return;
		}
		if (master.getText().equals(secondly1.getText())
				|| master.getText().equals(secondly2.getText())) {
			MessageDialog.openInformation(null, "提示", "主表和子表不能相同");
			return;
		}
		if ((secondly1.getText().trim().length() > 0 && field1.getText().trim()
				.length() == 0)
				|| (secondly2.getText().trim().length() > 0 && field2.getText()
						.trim().length() == 0)) {
			MessageDialog.openInformation(null, "提示", "已选子表对应主表字段不能为空");
			return;
		}
		setSql();
		super.okPressed();
	}

}
