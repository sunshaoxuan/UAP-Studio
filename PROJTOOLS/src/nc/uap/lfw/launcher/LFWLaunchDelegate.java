package nc.uap.lfw.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.util.ProjCoreUtility;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class LFWLaunchDelegate extends JavaLaunchDelegate {

	private File tempFile;
	private String orgMainType;

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		super.launch(configuration, mode, launch, monitor);
	}

	@Override
	public String[] getClasspath(ILaunchConfiguration configuration)
			throws CoreException {
		String nchome = LfwCommonTool.getUapHome();
		if (nchome.endsWith("\\") || nchome.endsWith("/")) {
			nchome = nchome.substring(0, nchome.length() - 1);
		}
		String[] entries = super.getClasspath(configuration);
		WebClassPathContainerID[] ids = WebClassPathContainerID.valuesUap();
		List<String> externalList = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			WebClassPathContainerID id = ids[i];
			IClasspathEntry[] entrys = ProjCoreUtility.getFullClasspathEntry(id);
			for (int j = 0; j < entrys.length; j++) {
				IClasspathEntry entry = entrys[j];
				externalList.add(entry.getPath().toOSString());
			}
		}
		List<String> proList = new ArrayList<String>();
//		List<String> ncList = new ArrayList<String>();
		for (int i = 0; i < entries.length; i++) {
			String entry = entries[i];
//			if (!entry.startsWith(nchome)) {
//				proList.add(entry);
//			}
			if(!externalList.contains(entry)){
				proList.add(entry);
			}
			else 
				break;
		}
//		proList.addAll(ncList);
		Iterator<String> strIt = externalList.iterator();
		while(strIt.hasNext()){
			String lib = strIt.next();
			if(proList.contains(lib))
				continue;
			proList.add(lib);
		}
		entries = proList.toArray(new String[0]);
		return MDEBootstrapLauncher.getClasspath(entries, getTempFile());
	}

	public String getProgramArguments(ILaunchConfiguration configuration)
			throws CoreException {
		String programArguments = super.getProgramArguments(configuration);
		return MDEBootstrapLauncher.insertArg(getTempFile(), this.orgMainType,
				programArguments);
	}

	public File getTempFile() {
		if (this.tempFile == null) {
			this.tempFile = MDEBootstrapLauncher.createTempFile();
		}
		return this.tempFile;
	}

	public String getMainTypeName(ILaunchConfiguration configuration)
			throws CoreException {
		return super.getMainTypeName(configuration);
	}

	public String verifyMainTypeName(ILaunchConfiguration configuration)
			throws CoreException {
		this.orgMainType = super.verifyMainTypeName(configuration);
		return MDEBootstrapLauncher.mainClassName;
	}
}
