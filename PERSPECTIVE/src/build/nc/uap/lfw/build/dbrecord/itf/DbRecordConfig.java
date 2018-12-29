package nc.uap.lfw.build.dbrecord.itf;

/**
 * 主子表结构配置文件，包括：
 * <ul>
 * 	<li>主表配置文件: Items.xml</li>
 * 	<li>公共主子表配置目录multitablerule</li>
 * 	<li>模块主子表配置目录multitablerule</li>
 * </ul>
 * 
 * @author PH
 */
public class DbRecordConfig {
//	@Deprecated
//	private String initDataFileName;
	
	/** 主表导出项 */
	private MainTableRecordItem mainTableItem;
	/** 公共主子表配置目录 */
	private String commonMultiTableFileName;
	/** 模块主子表配置目录 */
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
