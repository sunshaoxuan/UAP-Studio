package nc.uap.lfw.build.core.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ڼ�¼����Ԥ�ýű�ʱ����ϢVO����
 * @author mazqa
 *
 */
public class RecordGenerateMessageVO {

	/**
	 * ����Ԥ�ýű��Ƿ����ɳɹ���Ĭ��ֵ��ʧ�ܵ�
	 */
	private boolean success = false;;
	/**
	 * ��Ϣ�б�
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
