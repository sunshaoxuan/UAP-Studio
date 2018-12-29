package com.yonyou.studio.web.disk.impl.resolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.internal.bcp.BCPManifest;
import nc.uap.lfw.internal.bcp.BusinessComponent;
import nc.uap.lfw.internal.util.BCPUtils;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

import com.yonyou.studio.common.disk.core.construction.IDiskCommandExecutor;
import com.yonyou.studio.common.disk.core.construction.command.copy.CopyCommand;
import com.yonyou.studio.common.disk.core.model.ComponentContext;
import com.yonyou.studio.common.disk.core.model.GlobalContext;
import com.yonyou.studio.common.disk.core.model.ProjectContext;
import com.yonyou.studio.common.disk.core.resolver.AbstractProjectResolver;
import com.yonyou.studio.common.disk.core.resolver.IProjectResolver;

public class WEBProjectResovler extends AbstractProjectResolver implements
IProjectResolver {
	
	protected static final Pattern moduleNamePattern = Pattern
			.compile("module\\s+name\\s*=\"([^\"]+)\"");

	protected static final String JAR_ENCODE = "UTF-8";

	@Override
	public boolean accept(IProject project) throws CoreException {
		return project.hasNature("nc.uap.lfw.BizCompProjectNature");
	}

	@Override
	public String getResolverIdentity() {
		return "web.v63";
	}

	@Override
	public ProjectContext parseContext(IProject project) throws CoreException {
		ProjectContext context = new ProjectContext();
		// Ä£¿éÏîÄ¿
		File projectLocation = project.getLocation().toFile();
		File moduleXml = new File(projectLocation, "/META-INF/module.xml");
		if (moduleXml.exists() && moduleXml.isFile()) {
			String moduleCode = parseModuleName(moduleXml);
			context.setCode(moduleCode);
			context.setName(moduleCode);
		}

		IJavaProject javaProject = JavaCore.create(project);
		context.setClasspaths(JavaRuntime.computeDefaultRuntimeClassPath(javaProject));
		context.setEncoding(project.getDefaultCharset());
		context.setProject(project);
		return context;
	}
	
	/**
	 * 
	 * @param moduleXml
	 * @return
	 */
	private static String parseModuleName(File moduleXml) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(moduleXml), "gb2312"));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				sb.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
		if (sb.length() > 0) {
			Matcher matcher = moduleNamePattern.matcher(sb.toString());
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		return "uap";
	}

	@Override
	public ComponentContext[] parseComponentContexts(IProject project) {
		List<ComponentContext> results = new ArrayList<ComponentContext>();
		IFile manifestFile = project.getFile("manifest.xml"); //$NON-NLS-1$
		try {
			BCPManifest bcpmanifest = (BCPManifest) BCPUtils
					.read(manifestFile);
			if(bcpmanifest!=null && bcpmanifest.getBusinessComponentList()!=null){
				for(BusinessComponent component:bcpmanifest.getBusinessComponentList()){
					String componentName = component.getComponentName();
					File componentRoot = new File(project.getLocation().toFile(),componentName);
					if(componentRoot.exists() && componentRoot.isDirectory()){
						ComponentContext context = new ComponentContext();
						context.setCode(component.getComponentName());
						context.setName(component.getComponentDisplayName());
						context.setRoot(componentRoot);
						results.add(context);
					}
				}
			}
			
		} catch (CoreException e) {
			
		}
		return results.toArray(new ComponentContext[0]);
	}
	
	@Override
	public IDiskCommandExecutor[] getExtCommandExecutor(
			GlobalContext globalContext, ProjectContext projectContext,
			ComponentContext componentContext) {
		IDiskCommandExecutor[] results = new IDiskCommandExecutor[2];		
		String ctx = LfwCommonTool.getLfwProjectCtx(projectContext.getProject());
		CopyCommand webCopy = CopyCommand.newInstance("web=code/hotwebs/"+ctx+",Ehtml");
		results[0] = webCopy.getExecutor(globalContext, projectContext, componentContext);
		webCopy = CopyCommand.newInstance("web/html=code/hotwebs/"+ctx+"/sync/${module_code}/${component_code}/html");
		results[1] = webCopy.getExecutor(globalContext, projectContext, componentContext);
		return results;
	}

}
