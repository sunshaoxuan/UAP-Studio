package nc.uap.lfw.mlr;

import nc.uap.lfw.lang.M_mlr;
import nc.uap.lfw.tool.Helper;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.internal.corext.refactoring.nls.changes.CreateTextFileChange;
import org.eclipse.ltk.core.refactoring.Change;

/**
 * 多语资源的FileChange
 * 
 * @author dingrf
 *
 */
public class CreateResFileChange extends CreateTextFileChange{

	public CreateResFileChange(IPath path, String source, String encoding){
		super(path, source, encoding, M_mlr.CreateResFileChange_0);
	}

	@Override
	public Change perform(IProgressMonitor pm)throws CoreException, OperationCanceledException{
		IFile file = getOldFile(pm);
		IContainer parent = file.getParent();
		//创建资源文件
		if (!parent.exists())
			Helper.createResource(parent);
		return super.perform(pm);
	}
}
