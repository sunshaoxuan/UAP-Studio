package nc.uap.lfw.core.dev;

import java.io.Serializable;


/**
 * 开发使用的BusinessComponent组件模型
 * @author zhangxya
 *
 */
public class LfwDevBusiComponent implements Serializable{
	
	private static final long serialVersionUID = 9102891658997250281L;

	private String busiComponentId;
	
	private LfwDevViews lfwDevViews;
	
	private LfwDevNodes lfwDevNodes;
	
	private LfwDevApps lfwAppDevs;
	
	public LfwDevApps getLfwAppDevs() {
		return lfwAppDevs;
	}
	public void setLfwAppDevs(LfwDevApps lfwAppDevs) {
		this.lfwAppDevs = lfwAppDevs;
	}
	public LfwDevNodes getLfwDevNodes() {
		return lfwDevNodes;
	}
	public void setLfwDevNodes(LfwDevNodes lfwDevNodes) {
		this.lfwDevNodes = lfwDevNodes;
	}
	public LfwDevViews getLfwDevViews() {
		return lfwDevViews;
	}
	public void setLfwDevViews(LfwDevViews lfwDevViews) {
		this.lfwDevViews = lfwDevViews;
	}
	public String getBusiComponentId() {
		return busiComponentId;
	}
	public void setBusiComponentId(String busiComponentId) {
		this.busiComponentId = busiComponentId;
	}

}
