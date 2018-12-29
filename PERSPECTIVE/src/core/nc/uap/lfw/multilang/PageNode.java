package nc.uap.lfw.multilang;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

/**
 * 功能节点
 * 
 * @author dingrf
 *
 */
public class PageNode {

	/**ID值*/
	private String id;
	
	/**显示名称*/
	private String name;
	
	/**父节点*/
	private PageNode parent;
	
	/**子节点List*/
	
	private List<PageNode> childrens;
	
	/**路径*/
	private String path;
	
	/**是否为文件*/
	private boolean isFile;
	
	/**根*/
	private String root;
	
	/** key值前缀*/
	private String prefix;
	
	/**IFile*/
	private IFile file;
	
	/**文件发生改变*/
	private boolean changed = false;
	
	/** 资源文件模块名称*/
	private String resModuleName;
	
	/** 资源文件模块名称*/
	private String bcpName;
	
	/** 文件类型*/
	private String type;
	
	private MLResSubstitution[] substitutions;
	
	
	
	public PageNode(IFolder f,String resModule,String projectName){
		this.setId(f.getName());
		this.setName(f.getName());
		this.setPath(f.getLocation().toString());
		this.setRoot(projectName);
		this.setResModuleName(resModule);
	}
	
	public PageNode(IFile f,String resModule){
		this.setId(f.getName());
		this.setName(f.getName());
		this.setFile(true);
		this.setPath(f.getLocation().toString());
		this.setFile(f);
		this.setResModuleName(resModule);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public PageNode getParent() {
		return parent;
	}

	public void setParent(PageNode parent) {
		this.parent = parent;
	}

	public List<PageNode> getChildrens() {
		if (childrens==null){
			childrens = new ArrayList<PageNode>();
		}
		return childrens;
	}

	public void setChildrens(List<PageNode> childrens) {
		this.childrens = childrens;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
//		this.prefix = root + "-";
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getResModuleName() {
		return resModuleName;
	}

	public void setResModuleName(String resModuleName) {
		this.resModuleName = resModuleName;
	}

	public String getBcpName() {
		return bcpName;
	}

	public void setBcpName(String bcpName) {
		this.bcpName = bcpName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MLResSubstitution[] getSubstitutions() {
		return substitutions;
	}

	public void setSubstitutions(MLResSubstitution[] substitutions) {
		this.substitutions = substitutions;
	}
	

}
