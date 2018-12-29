package nc.uap.lfw.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.WEBProjPlugin;

import org.eclipse.core.resources.ResourcesPlugin;

public class MDEBootstrapLauncher {

//	 public static final String mainClassName = MDELoader.class.getName();
	public static final String mainClassName = LFWLoader.class.getName();
	  public static final String jarPath = "lib/lfwloader.jar";
	  public static final String loaderjar = "lfwloader.jar";
	  private static File workspaceFile = null;

	  public static String[] getClasspath(String[] entries, File file)
	  {
	    List reserveList = new ArrayList();
	    BufferedWriter writer = null;
	    InputStream input = null;
	    FileOutputStream fos = null;
	    try
	    {
	      writer = new BufferedWriter(new FileWriter(file));	      
	      for (int i = 0; i < entries.length; ++i)
	      {
	        String entry = entries[i];
	        writer.write(entry);
	        writer.newLine();
	      }

	      writer.close();
	      File mdeLoader = new File(getWorkspaceFile(), "lfwloader.jar");
//	      if (!(mdeLoader.exists()))
//	      {
	        input = WEBProjPlugin.getDefault().getBundle().getResource("lib/lfwloader.jar").openStream();
//	        InputStream input = Thread.currentThread().getContextClassLoader()
//					.getResourceAsStream("lib/mdeloader.jar");
	        fos = new FileOutputStream(mdeLoader);
	        byte[] buf = new byte[1024];
	        int len = 0;
	        while ((len = input.read(buf)) != -1)
	        	fos.write(buf, 0, len);
	        input.close();
	        fos.close();
//	      }
	      reserveList.add(mdeLoader.getAbsolutePath());
	    }
	    catch (Exception e)
	    {
	    	WEBProjPlugin.getDefault().logError(e);
	    }
	    finally{
	    	try {
	    		if(input != null)
	    			input.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
	    	try {
	    		if(fos != null)
	    			fos.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
	    	try {
	    		if(writer != null)
	    			writer.close();
			} catch (IOException e) {
				WEBProjPlugin.getDefault().logError(e.getMessage(),e);
			}
	    }
	    return ((String[])reserveList.toArray(new String[0]));
	  }

	  public static File createTempFile()
	  {
	    File tempFile = null;
	    try
	    {
	      tempFile = File.createTempFile("mde", "launcher", getWorkspaceFile());
	    }
	    catch (IOException e)
	    {
	    	WEBProjPlugin.getDefault().logError(e);
	    }
	    return tempFile;
	  }

	  public static File getWorkspaceFile()
	  {
	    if (workspaceFile == null)
	    {
	    	synchronized (MDEBootstrapLauncher.class) {
				if(workspaceFile == null)
					workspaceFile = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();					
			}
	    }
	    return workspaceFile;
	  }

	  public static String insertArg(File file, String mainType, String programArguments)
	  {
	    return MessageFormat.format("\"{0}\" {1} {2}", new Object[] { file.getAbsolutePath(), mainType, programArguments });
	  }
}
