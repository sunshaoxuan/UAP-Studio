package nc.uap.lfw.build.core.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录产生预置脚本时的信息VO对象
 * @author mazqa
 *
 */
public class RecordGenerateMessageVO {

	/**
	 * 该条预置脚本是否生成成功，默认值是失败的
	 */
	private boolean success = false;;
	/**
	 * 信息列表
	 */
	private List<RecordValidateMessage> messageList = new ArrayList<RecordValidateMessage>();
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<RecordValidateMessage> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<RecordValidateMessage> messageList) {
		this.messageList = messageList;
	}
	
	
}
