package nc.uap.lfw.internal.bcp;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * @author qinjianc
 *
 */
public class BusinessComponent implements Serializable {
	private static final long serialVersionUID = -7804107152520086504L;

	@XStreamAlias("name")
	@XStreamAsAttribute
	private String componentName;

	@XStreamAsAttribute
	private Boolean disable = null;

	@XStreamAlias("dispname")
	@XStreamAsAttribute
	private String componentDisplayName;

	@XStreamAlias("public")
	private SourceableFolders pubFolders = new SourceableFolders();

	@XStreamAlias("client")
	private SourceableFolders clientFolders = new SourceableFolders();

	@XStreamAlias("private")
	private SourceableFolders priFolders = new SourceableFolders();

	@XStreamAlias("test")
	private SourceableFolders testFolders = new SourceableFolders();

	@XStreamAlias("resource")
	private SourceableFolders resFolders = new SourceableFolders();

//	@XStreamAlias("web")
//	private SourceableFolders webFolders = new SourceableFolders();

	public Boolean getDisable() {
		return this.disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public SourceableFolders getPubFolders() {
		return this.pubFolders;
	}

	public void setPubFolders(SourceableFolders pubFolders) {
		this.pubFolders = pubFolders;
	}

	public SourceableFolders getClientFolders() {
		return this.clientFolders;
	}

	public void setClientFolders(SourceableFolders clientFolders) {
		this.clientFolders = clientFolders;
	}

	public SourceableFolders getPriFolders() {
		return this.priFolders;
	}

	public void setPriFolders(SourceableFolders priFolders) {
		this.priFolders = priFolders;
	}

	public SourceableFolders getTestFolders() {
		return this.testFolders;
	}

	public void setTestFolders(SourceableFolders testFolders) {
		this.testFolders = testFolders;
	}

	public SourceableFolders getResFolders() {
		return this.resFolders;
	}

	public void setResFolders(SourceableFolders resFolders) {
		this.resFolders = resFolders;
	}

//	public SourceableFolders getWebFolders() {
//		return webFolders;
//	}
//
//	public void setWebFolders(SourceableFolders webFolders) {
//		this.webFolders = webFolders;
//	}

	public String getComponentName() {
		return this.componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentDisplayName() {
		return this.componentDisplayName;
	}

	public void setComponentDisplayName(String componentDisplayName) {
		this.componentDisplayName = componentDisplayName;
	}
}
