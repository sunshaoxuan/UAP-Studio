package nc.uap.lfw.build.dbrecord.itf;

/**
 * ���ӱ�ṹ�����ļ���������
 * <ul>
 * 	<li>���������ļ�: Items.xml</li>
 * 	<li>�������ӱ�����Ŀ¼multitablerule</li>
 * 	<li>ģ�����ӱ�����Ŀ¼multitablerule</li>
 * </ul>
 * 
 * @author PH
 */
public class DbRecordConfig {
//	@Deprecated
//	private String initDataFileName;
	
	/** �������� */
	private MainTableRecordItem mainTableItem;
	/** �������ӱ�����Ŀ¼ */
	private String commonMultiTableFileName;
	/** ģ�����ӱ�����Ŀ¼ */
	private String moduleMultiTableFileName;
	
	
	public String getCommonMultiTableFileName() {
		return commonMultiTableFileName;
	}

	public void setCommonMultiTableFileName(String commonMultiTableFileName) {
		this.commonMultiTableFileName = commonMultiTableFileName;
	}

	public String getModuleMultiTableFileName() {
		return moduleMultiTableFileName;
	}

	public void setModuleMultiTableFileName(String moduleMultiTableFileName) {
		this.moduleMultiTableFileName = moduleMultiTableFileName;
	}

	public MainTableRecordItem getMainTableItem() {
		return mainTableItem;
	}

	public void setMainTableItem(MainTableRecordItem mainTableItem) {
		this.mainTableItem = mainTableItem;
	}
	
	

}
