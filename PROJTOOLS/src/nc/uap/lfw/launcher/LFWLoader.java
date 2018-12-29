package nc.uap.lfw.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class LFWLoader
{
  public static void main(String[] args)
    throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    if (args.length > 1)
    {
      String tmpFileName = args[0];
      String mainClassName = args[1];
      String[] subargs = new String[args.length - 2];
      System.arraycopy(args, 2, subargs, 0, subargs.length);
      File file = new File(tmpFileName);
      if (file.exists())
      {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List urlList = new ArrayList();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
          File f = new File(line);
          if (f.exists())
          {
            urlList.add(f.toURL());
          }
        }
        reader.close();
        file.delete();
        URLClassLoader loader = new URLClassLoader((URL[])urlList.toArray(new URL[0]), LFWLoader.class.getClassLoader());
        Thread.currentThread().setContextClassLoader(loader);

        Class mainClass = loader.loadClass(mainClassName);
        Method method = mainClass.getMethod("main", new Class[] {java.lang.String[].class});
        method.invoke(null, new Object[] { subargs });
      }
    }
  }
}
