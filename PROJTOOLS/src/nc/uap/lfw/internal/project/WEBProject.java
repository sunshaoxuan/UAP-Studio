package nc.uap.lfw.internal.project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;

public class WEBProject extends BaseProject {
	private String moduleName;
	private String moduleConfig;

	public WEBProject() {
	}

	public WEBProject(IProject project) {
		super.setProject(project);
	}

	public void configure() throws CoreException {
		addToBuildSpec(WEBProjConstants.MODULE_CONIFG_BUILDER_ID);
		// addToBuildSpec(WEBProjConstants.PORTAL_MODULE_CONFIG_BUILDER_ID);
	}

	public void deconfigure() throws CoreException {
		removeFromBuildSpec(WEBProjConstants.MODULE_CONIFG_BUILDER_ID);
		// removeFromBuildSpec(WEBProjConstants.PORTAL_MODULE_CONFIG_BUILDER_ID);
	}

	public String getModuleName() {
		init();
		return moduleName;
	}

	public String getDefaultModuleConfig() {
		init();
		return moduleConfig;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setDefaultModuleConfig(String moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	private void init() {
		if (moduleName == null) {
			IFile file = getProject().getFile(new Path(".module_prj"));
			if (!file.exists()) {
				moduleName = "zz";
				moduleConfig = "module.xml";
				return;
			}
			try {
				InputStream in = file.getContents();
				Properties prop = new Properties();
				prop.load(in);
				moduleName = prop
						.getProperty(WEBProjConstants.MODULE_NAME_PROPERTY);
				moduleConfig = prop
						.getProperty(WEBProjConstants.MODULE_CONFIG_PROPERTY);
				in.close();
			} catch (Exception e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
		}
	}

	public void save() {
		IFile file = getProject().getFile(".module_prj");
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		writer.println(WEBProjConstants.MODULE_NAME_PROPERTY + "=" + moduleName);
		writer.println(WEBProjConstants.MODULE_CONFIG_PROPERTY + "="
				+ moduleConfig);
		String initContent = swriter.getBuffer().toString();
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(
					initContent.getBytes("8859_1"));
			if (file.exists()) {
				file.setContents(stream, false, false, null);
			} else {
				file.create(stream, false, null);
			}
			stream.close();
		} catch (CoreException e) {
		} catch (IOException e) {
		}
	}

//	public IPath getNCHOME() {
//		// return getProject().getFolder("NC_HOME");
//		// return Path.fromOSString(getVariableManager().getValueVariable(
//		// "FIELD_NC_HOME").getValue
//		// ());
//		return Path.fromOSString(LfwCommonTool.getUapHome());
//	}

	public static IStringVariableManager getVariableManager() {
		return VariablesPlugin.getDefault().getStringVariableManager();
	}

//	public IPath getAntFoder() {
//		return getNCHOME().append("ant");
//	}
//
//
//	public IPath getModulesLanglibFoder() {
//		return getNCHOME().append("langlib");
//	}
//
//	public IPath getExternalFoder() {
//		return getNCHOME().append("external");
//	}
//
//	public IPath getEjbFoder() {
//		return getNCHOME().append("ejb");
//	}
//
//	public IPath getFrameworkFoder() {
//		return getNCHOME().append("framework");
//	}
//
//	public IPath getMiddlewareFoder() {
//		return getNCHOME().append("middleware");
//	}
//	
//	public IPath getTomcatLibFolder() {
//		return getNCHOME().append("lib");
//	}



//	public IPath[] getModulePrivateFolders() throws CoreException {
//		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
//		IPath[] subpublicfolders = new IPath[publicfolders.length];
//		for (int i = 0; i < publicfolders.length; i++) {
//			subpublicfolders[i] = publicfolders[i].append("META-INF");
//		}
//		return subpublicfolders;
//	}
//
//	public IPath[] getModuleDiscouragedClientFolders() throws CoreException {
//		ArrayList<IPath> list = new ArrayList<IPath>();
//		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
//		for (IPath folder : publicfolders) {
//			String name = folder.lastSegment();
//			if (!name.startsWith("uap") && !name.equals(getModuleName())) {
//				list.add(folder.append("client"));
//			}
//		}
//		return list.toArray(new IPath[0]);
//	}
//
//	public IPath[] getModuleAccessibleClientFolders() throws CoreException {
//		ArrayList<IPath> list = new ArrayList<IPath>();
//		IPath[] publicfolders = LfwCommonTool.getAccessibleModuleFolders();
//		for (IPath folder : publicfolders) {
//			String name = folder.lastSegment();
//			if (name.startsWith("uap") || name.equals(getModuleName())) {
//				list.add(folder.append("client"));
//			}
//		}
//		return list.toArray(new IPath[0]);
//	}
}
