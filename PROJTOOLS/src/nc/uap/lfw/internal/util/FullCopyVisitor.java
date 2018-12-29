package nc.uap.lfw.internal.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import nc.uap.lfw.core.WEBProjPlugin;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class FullCopyVisitor implements IResourceVisitor {

    private IPath to;

    public FullCopyVisitor(IPath target) {
        this.to = target;
    }

    public boolean visit(IResource resource) throws CoreException {
        if (resource.getType() == IResource.FILE) {
            IFile fromFile = (IFile) resource;

            IPath path = getOutputPath(fromFile);
            IPath toPath = to.append(path);
           
            File toDir = toPath.removeLastSegments(1).toFile();
            File toFile = toPath.toFile();

            if(!toDir.exists())
            	toDir.mkdirs();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = fromFile.getContents();
                out = new FileOutputStream(toFile);
                IOUtils.copy(in, out);
            } 
            catch (Exception e) {
				WEBProjPlugin.getDefault().logError(e);
			} 
            finally {
            	try{
            		in.close();
            		out.close();
            	}
            	catch(Exception e){
            		WEBProjPlugin.getDefault().logError(e.getMessage());
            	}
//            	if(in!=null){
//            		IOUtils.closeQuietly(in);
//            	}
//            	if(out!=null){
//            		IOUtils.closeQuietly(out);
//            	}
            }
            return false;

        } 
        else if (resource.getType() == IResource.FOLDER) {
            IPath folder = to.append(resource.getProjectRelativePath());
            File dir = folder.toFile();
            if(!dir.exists())
            	dir.mkdirs();
            return true;
        }
        return true;
    }
    
    public IPath getOutputPath(IFile file) {
        IPath path = file.getProjectRelativePath().removeLastSegments(1);

        path = path.append(file.getName());

        return path;

    }

}
