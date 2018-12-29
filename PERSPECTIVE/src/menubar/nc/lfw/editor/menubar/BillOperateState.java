package nc.lfw.editor.menubar;

/**
 * 菜单按钮操作状态
 * @author guoweic
 *
 */
public class BillOperateState {

	// 初始化按钮处理
	public static final String OP_INIT = "0";
	// 非编辑时按钮处理
	public static final String OP_SINGLESEL = "1";
	public static final String OP_MULTISEL = "2";
	// 编辑时按钮处理
	public static final String OP_EDIT = "3";
	// 单据新增状态
	public static final String OP_ADD = "4";
	//TODO 其他状态
	
	// 所有状态
	public static final String OP_ALL = "7";
	
	// 显示名称
	private String text = "";
	
	// 值
	private String value;
	
	public BillOperateState(String text, String value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
