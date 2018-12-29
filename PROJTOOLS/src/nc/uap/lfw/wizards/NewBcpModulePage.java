package nc.uap.lfw.wizards;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import nc.uap.lfw.common.LfwCommonTool;
import nc.uap.lfw.internal.bcp.BCPObservable;
import nc.uap.lfw.internal.bcp.NewBCComposite;
import nc.uap.lfw.lang.M_wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * BCP组件添加page
 * @author qinjianc
 *
 */
public class NewBcpModulePage extends WizardPage  implements Observer{
	
	private NewBCComposite addNewBCPComposite;
	private IProject project;
//	private BCPManifest bcpManifest;

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	protected NewBcpModulePage(String pageName) {
		super(pageName);
		setTitle(M_wizards.NewBcpModulePage_0);
	}

	@Override
	public void createControl(Composite parent) {
	    Composite container = new Composite(parent, 0);
	    setControl(container);
	    container.setLayout(new GridLayout(1, false));
//	    if (!LfwCommonTool.isBCPProject(this.project))
//	    {
//	      setErrorMessage("Can't found any Business Component Project in this selection!");
//	      setPageComplete(false);
//	    }
//	    else
//	    {
	      this.addNewBCPComposite = new NewBCComposite(container, 0);
	      BCPObservable observable = new BCPObservable();
	      observable.addObserver(this);
	      this.addNewBCPComposite.setObservable(observable);
//	    }
		
	}
	 public NewBCComposite getAddNewBCPComposite()
	  {
	    return this.addNewBCPComposite;
	  }

	  public void setAddNewBCPComposite(NewBCComposite addNewBCPComposite)
	  {
	    this.addNewBCPComposite = addNewBCPComposite;
	  }
	  
	 /**
	  * 即时观察输入的组件名与原有组件名是否重复
	  */
	public void update(Observable o, Object arg)
	  {
	    setPageComplete(false);
	    String componentName = this.addNewBCPComposite.getBcpName();
	    IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IStatus validateName = workspace.validateName(componentName, 2);
	    if (!(validateName.isOK()))
	    {
	      setErrorMessage(validateName.getMessage());
	    }
	    else
	    {
	      String[] bcps = LfwCommonTool.getBCPNames(project);
	      if(bcps!=null && Arrays.asList(bcps).contains(componentName))
	      {
	        setErrorMessage(M_wizards.NewBcpModulePage_1 + componentName + M_wizards.NewBcpModulePage_2);
	        return;
	      }
	      setErrorMessage(null);
	      setPageComplete(true);
	    }
	  }

}
