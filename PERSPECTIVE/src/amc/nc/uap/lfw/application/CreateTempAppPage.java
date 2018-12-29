package nc.uap.lfw.application;

import java.awt.GridBagConstraints;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nc.uap.lfw.core.WEBPersConstants;
import nc.uap.lfw.core.WEBPersPlugin;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jdt.core.dom.Message;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ImageList;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;

/**
 * 模板创建APP向导页-选择模板类型
 * @author qinjianc
 *
 */
public class CreateTempAppPage extends WizardPage{
	
	private TempGroup tempGroup;
	private PreviewGroup previewGroup;
	private static final Image SIMPLEQUERY = ImageProvider.simplequery;
	
	private static final Image ADVQUERY = ImageProvider.advquery;

	protected CreateTempAppPage(String pageName) {
		super(pageName);
		setTitle(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_MAINPAGE_TITLE));
		setDescription(WEBPersPlugin
				.getResourceString(WEBPersConstants.KEY_TEMP_SECPAGE_DESC)); 
	}

	private final class TempGroup extends Observable implements 
	IDialogFieldListener{
		
		final List tempList;
		
		
		public TempGroup(Composite composite){
			
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setSize(100, 200);
			group.setLayout(new GridLayout(1, false));
			group.setText("templetes"); //$NON-NLS-1$	
			tempList = new List(group,SWT.SINGLE);
			tempList.setLayoutData(new GridData(GridData.FILL_BOTH));
			String[] templetes ={"简单查询表单模板","多功能复杂表单模板","图表生成模板","报表生成模板","报销单据打印模板"}; 
//			ListDialogField list = new ListDialogField(null, null, new LabelProvider());
//			
//			ArrayList tempList = new ArrayList();
//			tempList.add(0,"简单查询表单模板");
//			tempList.add(1,"多功能复杂表单模板");
//			tempList.add(2,"图表生成模板");
//			tempList.add(3,"报表生成模板");
//			tempList.add(4,"报销单据打印模板");
//			list.addElements(tempList,0);
//			list.doFillIntoGrid(group, 3);
			tempList.setItems(templetes);
			tempList.addSelectionListener(new SelectionListener(){

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
			return tempList.getSelectionIndex();
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
//			ListDialogField list = new ListDialogField(null, null, new LabelProvider());
//			ImageLoader
			canvas = new Canvas(group,SWT.NONE);
			canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
//			ImageDescriptor SIMPLEQUERY = WEBProjPlugin.loadImage(getPath().substring(1), "query1.png");
//			ImageDescriptor ADVQUERY = WEBProjPlugin.loadImage(getPath().substring(1), "query2.png");
			
//			final Image previewImg = SIMPLEQUERY.createImage();
			imgList = new ImageList(SWT.None);
//			Image image1 = new Image(Display.getCurrent(),getPath()+"query1.png");
//			Image image2 = new Image(Display.getCurrent(),getPath()+"query2.png");
//			imgList.add(image1);
//			imgList.add(image2);
			imgList.add(SIMPLEQUERY);
			imgList.add(ADVQUERY);
//			group.setBackgroundImage(previewImg);
			
//			ArrayList tempList = new ArrayList();
//			tempList.add(0,"简单查询表单模板");
//			tempList.add(1,"多功能复杂表单模板");
//			tempList.add(2,"图表生成模板");
//			tempList.add(3,"报表生成模板");
//			tempList.add(4,"报销单据打印模板");
//			list.addElements(tempList,0);
//			list.doFillIntoGrid(group, 3);
		}

		@Override
		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
			int index = tempGroup.getTempIndex();
			final Image previewImg = imgList.get(index);
			
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

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		final Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		composite.setLayout(new GridLayout(4, true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		tempGroup = new TempGroup(composite);
		previewGroup = new PreviewGroup(composite);
		tempGroup.addObserver(previewGroup);
		
		setControl(composite);
		
		
		
	}
	public int getTempIndex(){
		return tempGroup.getTempIndex();
	}

}
