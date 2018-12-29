/**
 * 
 */
package nc.uap.lfw.template;


import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.lang.M_template;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ImageList;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

/**
 * @author guomq1
 * 2012-7-24 
 */
public class CreateTempWindowPage extends TempNextUsedWizardPage {
	private templateGroup templategp;
	private PreviewGroup previewGroup;
//	private static final Image SIMPLEQUERY = ImageProvider.simplequery;
//	
//	private static final Image ADVQUERY = ImageProvider.advquery;
//	
//	private static final Image ADVFLOWQUERY =  ImageProvider.advquery;
	
	private ArrayList<String> tempList;
	
	private ArrayList<Image> imageList;
	
	private Map<String,WizardPage> pageMap;
	
	private List templateList;

	

	
	/**
	 * @param pageName
	 */
	protected CreateTempWindowPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_WINDOW_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_SECPAGE_DESC)); 
		this.setPreviousPage(null);
		
	}

	
	
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		templategp = new templateGroup(composite);
		previewGroup = new PreviewGroup(composite);
		templategp.addObserver(previewGroup);
		
		setControl(composite);
	}
	
	private final class templateGroup extends Observable implements 
	IDialogFieldListener{
		
        
		public templateGroup(Composite composite){
			
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLocation(100, 100);
			group.setSize(200, 150);
			group.setLayout(new GridLayout(1, false));
			group.setText("templates");//$NON-NLS-1$	
			templateList = new List(group, SWT.SINGLE);
			
			templateList.setLayoutData(new GridData(GridData.FILL_BOTH));
//			String[] templates ={"单列表单据模板","主子列表单据模板","主子列表流程模板"}; 
//			String[] templates ={"主子列表单据模板","主子列表流程模板"}; 
//		    templateList.setItems(templates);
		    if(tempList!=null){
		    	for(String title:tempList){
		    		templateList.add(title);
		    	}		    	
		    }
		    
		    templateList.addSelectionListener(new SelectionListener(){
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					setChanged();
					notifyObservers();
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					setChanged();
					notifyObservers();
				}
				
			});
			
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
		protected void fireEvent() {
			setChanged();
			notifyObservers();
		}
		public int getTempIndex(){
			return templateList.getSelectionIndex();
		}
		}
	
	private final class PreviewGroup implements Observer,
	IDialogFieldListener {
		
		final Canvas canvas;
		final ImageList imgList;
		
		public PreviewGroup(Composite composite){
			final Group group = new Group(composite, SWT.NONE);
//			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLayout(new GridLayout(1, false));
			group.setText("preview"); //$NON-NLS-1$
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 3;
			group.setLayoutData(layoutData);

			canvas = new Canvas(group,SWT.NONE);
			canvas.setLayoutData(new GridData(GridData.FILL_BOTH));

			imgList = new ImageList(SWT.None);

//			imgList.add(SIMPLEQUERY);
//			imgList.add(ADVQUERY);
//			imgList.add(ADVFLOWQUERY);
			if(imageList!=null){
				for(Image image:imageList){
					imgList.add(image);
				}
			}

		}

		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			int index = templategp.getTempIndex();
			((NewTempleteWindowWizard)getWizard()).setIndex(index);
			final Image previewImg = imgList.get(index);
			
			if (index == 1){
				
//				removeChild(1);
//				updateContainerButton();

			}
			else{
//				removeChild(0);
//				updateContainerButton();
			}
			
			canvas.addPaintListener(new PaintListener(){
				public void paintControl(final PaintEvent event){
					if(previewImg!=null){
						final Rectangle imgBounds = previewImg.getBounds();
						int imgwidth = imgBounds.width;
						int imgheight = imgBounds.height;
						final Rectangle canBounds = canvas.getBounds();
						int canwidth = canBounds.width;
						int canheight = canBounds.height;
						event.gc.drawImage(previewImg, 0, 0,imgwidth,imgheight,0,0,canwidth,(int)(imgheight*canwidth/imgwidth));
					}
				}
			});
			canvas.redraw();
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO Auto-generated method stub
			
		}
		
	}

		/* (non-Javadoc)
		 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField)
		 */
	
		public void setTemplateList(ArrayList<String> tempList){
			this.tempList = tempList;
		}
		public void setImageList(ArrayList<Image> imgList){
			this.imageList = imgList;
		}
		public void setPageMap(Map<String,WizardPage> pageMap){
			this.pageMap = pageMap;
		}

		public int getTempIndex(){
			return templategp.getTempIndex();
		}
		public String getTempTitle(){
			return templateList.getItem(getTempIndex());
		}
		public IWizardPage getPreviousPage() {
			return null;
		}
	    public IWizardPage getNextPage() {
	    	if (getWizard() == null) {
	    		return null;
	    	}
	    	int index = templategp.getTempIndex();
	    	
	    	if (index == -1){
	    		index = 0;
//	    		return getWizard().getPage(WEBProjPlugin.getResourceString(WEBProjConstants.KEY_TEMP_MAINPAGE_TITLE));
	    	}
//	    	else if (index == 0 || index == 1){
//	    		return getWizard().getPage(WEBProjPlugin.getResourceString(WEBProjConstants.KEY_TEMP_THDDPAGE_DESC));
//	    	}
//	    	else{
    		String title = templateList.getItem(index);
    		return pageMap.get(title);
//	    	}
	    }



		@Override
		protected boolean nextButtonClick() {
			if(getTempIndex()<0){
				MessageDialog.openError(getShell(), M_template.CreateTempWindowPage_0, M_template.CreateTempWindowPage_1);
				return false;
			}
			return true;
		}



		@Override
		protected boolean previousButtonClick() {
			return true;
		}
		
		
	}

	

