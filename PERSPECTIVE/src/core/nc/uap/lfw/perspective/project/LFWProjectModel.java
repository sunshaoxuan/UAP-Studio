package nc.uap.lfw.perspective.project;

import java.io.File;
import java.io.Serializable;

import org.eclipse.core.resources.IProject;

public class LFWProjectModel implements Serializable{
	private static final long serialVersionUID = -7095058753382687747L;
	private IProject javaProject = null;
//	private File mdRoot = null;
	public LFWProjectModel(IProject project) {
		super();
		this.javaProject = project;
	}
	public IProject getJavaProject() {
		return javaProject;
	}
	public File getMDRoot(){
		return getJavaProject().getLocation().toFile();
//		if(mdRoot == null){
//			File file = getJavaProject().getLocation().toFile();
//			mdRoot = new File(file, LFWExplorerTreeBuilder.LFW_ROOT_DIR);
//			if(!mdRoot.exists()){
//				mdRoot.mkdirs();
//			}
//				
//		}
//		return mdRoot;
	}
	public String getProjectName(){
		return getJavaProject().getName();
	}
}
