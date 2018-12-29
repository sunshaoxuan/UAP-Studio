package nc.uap.lfw.internal.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.uap.lfw.common.FileUtilities;
import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.core.WEBProjConstants;
import nc.uap.lfw.core.WEBProjPlugin;
import nc.uap.lfw.core.WebClassPathContainerID;
import nc.uap.lfw.internal.bcp.BCPManifest;
import nc.uap.lfw.internal.bcp.BCPSourceFolder;
import nc.uap.lfw.internal.bcp.BusinessComponent;
import nc.uap.lfw.internal.bcp.SourceableFolder;
import nc.uap.lfw.internal.bcp.SourceableFolders;
import nc.uap.lfw.lang.M_internal;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.widgets.Shell;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 新建BCP方法类
 * @author qinjianc
 *
 */

public class BCPUtils {
	private static XStream xstream = null;
	private static String lineSeparator = System.getProperty("line.separator", //$NON-NLS-1$
			"\r\n"); //$NON-NLS-1$
	
	/*
	 * 生成默认文件夹
	 */
	public static SourceableFolder createPublicSourceableFoler() {
		return createSourceableFoler("public"); //$NON-NLS-1$
	}

	public static SourceableFolder createClinetSourceableFoler() {
		return createSourceableFoler("client"); //$NON-NLS-1$
	}

	public static SourceableFolder createPrivateSourceableFoler() {
		return createSourceableFoler("private"); //$NON-NLS-1$
	}

	public static SourceableFolder createTestSourceableFoler() {
		return createSourceableFoler("test"); //$NON-NLS-1$
	}

	public static SourceableFolder createResourceSourceableFoler() {
		return new SourceableFolder("resources"); //$NON-NLS-1$
	}
	public static SourceableFolder createWebSourceableFoler(){
		return createSourceableFoler("web/WEB-INF/"); //$NON-NLS-1$
	}

	public static SourceableFolder createSourceableFoler(String folderName) {
		return new SourceableFolder("src/" + folderName); //$NON-NLS-1$
	}

	/**
	 * 生成默认文件夹，并写入list
	 * @param project
	 * @param monitor
	 * @param businessComponent
	 * @return
	 * @throws CoreException
	 */
	public static List<BCPSourceFolder> checkBCFolder(IProject project,
			IProgressMonitor monitor, BusinessComponent businessComponent)
			throws CoreException {
		IFolder compFolder = project.getFolder(businessComponent
				.getComponentName());
		createFolder(compFolder, monitor);

		List<BCPSourceFolder> list = new ArrayList<BCPSourceFolder>();
		list.addAll(checkBCSourceableFolder(monitor, compFolder,
				businessComponent.getTestFolders()));
		list.addAll(checkBCSourceableFolder(monitor, compFolder,
				businessComponent.getResFolders()));
		list.addAll(checkBCSourceableFolder(monitor, compFolder,
				businessComponent.getPriFolders()));
		list.addAll(checkBCSourceableFolder(monitor, compFolder,
				businessComponent.getClientFolders()));
		list.addAll(checkBCSourceableFolder(monitor, compFolder,
				businessComponent.getPubFolders()));
		if(project.hasNature(WEBProjConstants.PORTAL_MODULE_NATURE)){
			ProjCoreUtility.createSourceEntry(project, businessComponent.getComponentName()+"/src/portalspec"); //$NON-NLS-1$
			list.add(new BCPSourceFolder(project.getFolder(businessComponent.getComponentName()+"/src/portalspec"), null)); //$NON-NLS-1$
			String localFilePath = project.getLocation().toString() + "/"+businessComponent.getComponentName()+"/src/portalspec/" +  //$NON-NLS-1$ //$NON-NLS-2$
			businessComponent.getComponentName() + "/portalspec"; //$NON-NLS-1$
			writePortal(localFilePath,businessComponent.getComponentName());

		}

		createFolder(compFolder.getFolder("META-INF"), monitor); //$NON-NLS-1$
		
		return list;
	}

	public static IFolder createFolder(IFolder folder, IProgressMonitor monitor)
			throws CoreException {
		if (!(folder.exists())) {
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder)
				createFolder((IFolder) parent, monitor);

			folder.create(true, true, monitor);
		}
		return folder;
	}
	
	/*
	 * 生成文件
	 */
	public static IFile createFile(IFile file, IProgressMonitor monitor)
			throws CoreException {
		if (!(file.exists())) {
			IContainer parent = file.getParent();
			if (parent instanceof IFolder)
				createFolder((IFolder) parent, monitor);

			file.create(null, true, monitor);
		}
		return file;
	}
	
 	private static void writePortal(String filePath, String module) 
 	{
 		try{
 		StringBuffer buffer = new StringBuffer();
 		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"); //$NON-NLS-1$
 		buffer.append("\n<portal>"); //$NON-NLS-1$
 		buffer.append("\n<module>"+module+"</module>"); //$NON-NLS-1$ //$NON-NLS-2$
 		buffer.append("\n<depends>pserver</depends>"); //$NON-NLS-1$
 		buffer.append("\n</portal>"); //$NON-NLS-1$
 		//ByteArrayInputStream stream = new ByteArrayInputStream(buffer.toString().getBytes());
 		File f = new File(filePath);
 		if (!f.exists()){
 			f.mkdirs();
 		}
 		FileUtilities.saveFile(filePath + "/portal.xml", buffer.toString().getBytes("UTF-8"));	 //$NON-NLS-1$ //$NON-NLS-2$
 		}
 		catch(Exception e){
 			WEBProjPlugin.getDefault().logError(e.getMessage(),e);
 		}
 	}
 		
	
	/*
	 * 将要生成的source文件夹写入list
	 */
	public static List<BCPSourceFolder> checkBCSourceableFolder(
			IProgressMonitor monitor, IFolder compFolder,
			SourceableFolders folders) throws CoreException {
		List<BCPSourceFolder> list = new ArrayList<BCPSourceFolder>();
		IFolder outfoler = compFolder.getFolder("classes"); //$NON-NLS-1$

		if ((folders != null) && (folders.getFolderList() != null)) {
			for (Iterator<SourceableFolder> localIterator = folders.getFolderList().iterator(); localIterator
					.hasNext();) {
				SourceableFolder folder = (SourceableFolder) localIterator
						.next();

				IFolder srcFolder = compFolder
						.getFolder(folder.getFolderName());
				IFolder forceCreateFolder = createFolder(srcFolder, monitor);
				list.add(new BCPSourceFolder(forceCreateFolder, outfoler));
			}
		}
		return list;
	}
	
	/**
	 * 根据新组建工程重新构造左侧树，生成相应组件文件
	 * @param project
	 * @param allSourceFolderList
	 * @param monitor
	 * @throws CoreException
	 */
	public static void rebuildBCPProject(IProject project,
			List<BCPSourceFolder> allSourceFolderList, IProgressMonitor monitor)
			throws CoreException {
		
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject != null) {
			IPath path;
			IPath outpath;
			IClasspathEntry newEntry;
			IClasspathEntry[] entries = javaProject.getRawClasspath();
			List<IClasspathEntry> cpe_otherList = new ArrayList<IClasspathEntry>();
			Map<IPath, IClasspathEntry> oldSourcePathSet = Collections.synchronizedMap(new HashMap<IPath, IClasspathEntry>());
			int k = entries.length;

			List<IClasspathEntry> newEntries = new ArrayList<IClasspathEntry>();
			for (int i = 0; i < k; ++i) {

				IClasspathEntry entry = entries[i];

				if (entry.getEntryKind() == 3) {
					oldSourcePathSet.put(entry.getPath(), entry);
					newEntries.add(entry);
				}
				if ((entry.getEntryKind() == 2) || (entry.getEntryKind() == 1)) {
					cpe_otherList.add(entry);
				}

			}

			int j = 0;

			for (Iterator<BCPSourceFolder> localObject = allSourceFolderList.iterator(); ((Iterator<BCPSourceFolder>) localObject).hasNext();) {

				BCPSourceFolder srcFolder = (BCPSourceFolder) ((Iterator<BCPSourceFolder>) localObject)
						.next();
				path = srcFolder.getSrcFolder().getFullPath();
				if (oldSourcePathSet.containsKey(path)) {
					newEntry = (IClasspathEntry) oldSourcePathSet.get(path);
					if (newEntries.contains(newEntry))
						continue;
					newEntries.add(0, newEntry);
				}

				j = 1;
				
				if(srcFolder.getOutFolder()!=null){
					outpath = srcFolder.getOutFolder().getFullPath();
				}
				else outpath = null;
				newEntry = JavaCore.newSourceEntry(path, new IPath[0],
						new IPath[0], outpath);
				if (newEntries.contains(newEntry))
					continue;

				newEntries.add(0, newEntry);

			}
			newEntries.size();

			if (j != 0) {
				newEntries.add(createJREEntry());
				newEntries.addAll(cpe_otherList);
				WebClassPathContainerID[] ids = WebClassPathContainerID
						.values();

				int num = ids.length;
				for (int i = 0; i < num; i++) {
					WebClassPathContainerID id = ids[i];
					newEntries.add(createContainerClasspathEntry(id));
				}
				

				IClasspathEntry[] array = (IClasspathEntry[]) newEntries
						.toArray(new IClasspathEntry[0]);
				javaProject.setRawClasspath(array, null);
			}
		}
	}

//	private static void add(IClasspathEntry newEntry) {
//
//	}

	public static IClasspathEntry createJREEntry() {
		return JavaCore.newContainerEntry(JavaRuntime
				.newDefaultJREContainerPath());
	}

	public static IClasspathEntry createContainerClasspathEntry(
			WebClassPathContainerID id) {
		return JavaCore.newContainerEntry(new Path(WEBProjConstants.LFW_LIBRARY_CONTAINER_ID).append(id.name()), false);
	}

	/*
	 * 将要生成的默认文件夹添加入BusinessComponent对象
	 */
	public static BusinessComponent createDefaultSourceableFolers(
			BusinessComponent businessComponent) {
		SourceableFolders pubFolders = businessComponent.getPubFolders();
		pubFolders.addSourceableFolder(createPublicSourceableFoler());
		businessComponent.setPubFolders(pubFolders);

		SourceableFolders clientFolders = businessComponent.getClientFolders();
		clientFolders.addSourceableFolder(createClinetSourceableFoler());
		businessComponent.setClientFolders(clientFolders);

		SourceableFolders priFolders = businessComponent.getPriFolders();
		priFolders.addSourceableFolder(createPrivateSourceableFoler());
		businessComponent.setPriFolders(priFolders);

		SourceableFolders resFolders = businessComponent.getResFolders();
		resFolders.addSourceableFolder(createResourceSourceableFoler());
		businessComponent.setResFolders(resFolders);

		SourceableFolders testFolders = businessComponent.getTestFolders();
		testFolders.addSourceableFolder(createTestSourceableFoler());
		businessComponent.setTestFolders(testFolders);
		
		return businessComponent;
	}

	/*
	 * 写xml文件
	 */
	public static void writeObjectXml2File(IFile file, Object obj, Shell shell,
			IProgressMonitor monitor) throws CoreException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String charset = "utf-8"; //$NON-NLS-1$
		write(obj, baos, charset);
		InputStream bais = new BufferedInputStream(new ByteArrayInputStream(
				baos.toByteArray()));
		LfwCommonTool.checkOutFile(file.getLocation().toOSString());
		writeFileContent(file, bais, shell, monitor);
	}

	public static void write(Object manifest, OutputStream out,
			String charsetName) throws CoreException {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, charsetName);
			writer.write("<?xml version=\"1.0\" encoding=\"" + charsetName //$NON-NLS-1$
					+ "\"?>" + lineSeparator); //$NON-NLS-1$
			getXstream().toXML(manifest, writer);
		} catch (Throwable e) {
			throw new CoreException(new Status(4, "nc.uap.mde", //$NON-NLS-1$
					M_internal.BCPUtils_0, e));
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static void writeFileContent(IFile file, InputStream content,
			Shell shell, IProgressMonitor monitor) throws CoreException {
		if (file.exists()) {
			file.setContents(content, true, true, monitor);
		} else {
			file.create(content, true, monitor);
		}
	}

	public static XStream getXstream() {
		if (xstream == null) {
			synchronized (BCPUtils.class) {
				if(xstream == null){
					xstream = new XStream(new DomDriver());
					xstream.setClassLoader(BCPUtils.class.getClassLoader()); 
					xstream.autodetectAnnotations(true);
					xstream.processAnnotations(new Class[] { BCPManifest.class,
							BusinessComponent.class, SourceableFolders.class,
							SourceableFolder.class });
				}
			}			
		}
		return xstream;
	}
	
	/*
	 * 读取xml文件
	 */
	public static Object read(InputStream in, String charsetName)
		    throws CoreException
		  {
		    Object fromXML = null;
		    InputStreamReader reader = null;
		    try
		    {
		      reader = new InputStreamReader(in, charsetName);
		      fromXML = getXstream().fromXML(reader);
		    }
		    catch (Throwable e)
		    {
		      throw new CoreException(new Status(4, "nc.uap.mde", M_internal.BCPUtils_1, e)); //$NON-NLS-1$
		    }
		    finally
		    {
		      if (reader != null)
		      {
		        try
		        {
		          reader.close();
		        }
		        catch (IOException localIOException1)
		        {
		        }
		      }
		    }

		    return fromXML;
		  }

		  public static Object read(IFile file) throws CoreException
		  {
		    Object fromXML = null;
		    if (file.exists())
		    {
		      fromXML = read(file.getContents(), file.getCharset());
		    }
		    return fromXML;
		  }

}
