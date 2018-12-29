package nc.uap.lfw.internal.project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class WEBProject extends BaseProject
{
	private String	moduleName;
	private String	moduleConfig;

	public WEBProject()
	{
	}

	public WEBProject(IProject project)
	{
		super.setProject(project);
	}

	public void configure() throws CoreException
	{
		addToBuildSpec(WEBPersConstants.MODULE_CONIFG_BUILDER_ID);
	}

	public void deconfigure() throws CoreException
	{
		removeFromBuildSpec(WEBPersConstants.MODULE_CONIFG_BUILDER_ID);
	}

	public String getModuleName()
	{
		init();
		return moduleName;
	}

	public String getDefaultModuleConfig()
	{
		init();
		return moduleConfig;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public void setDefaultModuleConfig(String moduleConfig)
	{
		this.moduleConfig = moduleConfig;
	}

	private void init()
	{
		if (moduleName == null)
		{
			IFile file = getProject().getFile(new Path(".module_prj"));
			if (!file.exists())
			{
				moduleName = "zz";
				moduleConfig = "module.xml";
				return;
			}
			try
			{
				InputStream in = file.getContents();
				Properties prop = new Properties();
				prop.load(in);
				moduleName = prop.getProperty(WEBPersConstants.MODULE_NAME_PROPERTY);
				moduleConfig = prop.getProperty(WEBPersConstants.MODULE_CONFIG_PROPERTY);
				in.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void save()
	{
		IFile file = getProject().getFile(".module_prj");
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		writer.println(WEBPersConstants.MODULE_NAME_PROPERTY + "=" + moduleName);
		writer.println(WEBPersConstants.MODULE_CONFIG_PROPERTY + "=" + moduleConfig);
		String initContent = swriter.getBuffer().toString();
		try
		{
			ByteArrayInputStream stream = new ByteArrayInputStream(initContent.getBytes("8859_1"));
			if (file.exists())
			{
				file.setContents(stream, false, false, null);
			}
			else
			{
				file.create(stream, false, null);
			}
			stream.close();
		}
		catch (CoreException e)
		{
		}
		catch (IOException e)
		{
		}
	}

	public IFolder getNCHOME()
	{
		return getProject().getFolder("NC_HOME");
	}

	public IFolder getAntFoder()
	{
		return getNCHOME().getFolder("ant");
	}

	public IFolder getModulesFoder()
	{
		return getNCHOME().getFolder("modules");
	}
	public IFolder getModulesLanglibFoder()
	{
		return getNCHOME().getFolder("langlib");
	}

	public IFolder getExternalFoder()
	{
		return getNCHOME().getFolder("external");
	}

	public IFolder getEjbFoder()
	{
		return getNCHOME().getFolder("ejb");
	}

	public IFolder getFrameworkFoder()
	{
		return getNCHOME().getFolder("framework");
	}

	public IFolder getMiddlewareFoder()
	{
		return getNCHOME().getFolder("middleware");
	}

	public IFolder[] getAccessibleModuleFolders() throws CoreException
	{
		//
		IResource[] subdir = getModulesFoder().members();
		String[] names = WEBPersPlugin.getExModuleNames();
		HashSet<String> exSet = new HashSet<String>();
		for (String name : names)
		{
			exSet.add(name);
		}
		ArrayList<IFolder> list = new ArrayList<IFolder>();
		for (IResource resource : subdir)
		{
			if (resource.getType() == IResource.FOLDER && !exSet.contains(resource.getName()))
			{
				list.add((IFolder) resource);
			}
		}
		return list.toArray(new IFolder[0]);
	}

	public IFolder[] getModulePrivateFolders() throws CoreException
	{
		IFolder[] publicfolders = getAccessibleModuleFolders();
		IFolder[] subpublicfolders = new IFolder[publicfolders.length];
		for (int i = 0; i < publicfolders.length; i++)
		{
			subpublicfolders[i] = publicfolders[i].getFolder("META-INF");
		}
		return subpublicfolders;
	}

	public IFolder[] getModuleDiscouragedClientFolders() throws CoreException
	{
		ArrayList<IFolder> list = new ArrayList<IFolder>();
		IFolder[] publicfolders = getAccessibleModuleFolders();
		for (IFolder folder : publicfolders)
		{
			String name = folder.getName();
			if (!name.startsWith("uap") && !name.equals(getModuleName()))
			{
				list.add(folder.getFolder("client"));
			}
		}
		return list.toArray(new IFolder[0]);
	}

	public IFolder[] getModuleAccessibleClientFolders() throws CoreException
	{
		ArrayList<IFolder> list = new ArrayList<IFolder>();
		IFolder[] publicfolders = getAccessibleModuleFolders();
		for (IFolder folder : publicfolders)
		{
			String name = folder.getName();
			if (name.startsWith("uap") || name.equals(getModuleName()))
			{
				list.add(folder.getFolder("client"));
			}
		}
		return list.toArray(new IFolder[0]);
	}
}
