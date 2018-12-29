package nc.uap.lfw.window;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nc.lfw.editor.common.DialogWithTitle;
import nc.uap.lfw.lang.M_window;
import nc.uap.lfw.perspective.webcomponent.ImageProvider;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ImageList;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 *
 */
public class ChooseWindowTemplateDialog extends DialogWithTitle {
	
	private TemplateGroup templategp;
	private PreviewGroup previewGroup;
	private ArrayList<Image> imageList = new ArrayList<Image>();
	private int tempIndex;
	private static final String CHOOSE_WINDOW_TEMPLATE = M_window.ChooseWindowTemplateDialog_0;
	private static final List<Pair> templates = new ArrayList<Pair>();
	static{
		Pair pair = new Pair(IWindowTemplateType.TYPE_EMPTY, M_window.ChooseWindowTemplateDialog_1);
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_LIST, M_window.ChooseWindowTemplateDialog_2);
		pair.childCount = true;
		pair.org = true;
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_LIST_WF, M_window.ChooseWindowTemplateDialog_3);
		pair.childCount = true;
		pair.org = true;
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_CARD, M_window.ChooseWindowTemplateDialog_4);
		pair.childCount = true;
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_CARD_WF, M_window.ChooseWindowTemplateDialog_5);
		pair.childCount = true;
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_OK_CANCEL, M_window.ChooseWindowTemplateDialog_6);
		templates.add(pair);
		pair = new Pair(IWindowTemplateType.TYPE_SAVE_SAVEADD_CANCEL, M_window.ChooseWindowTemplateDialog_7);
		templates.add(pair);
//		pair = new Pair(IWindowTemplateType.TYPE_WIZARD, "向导窗口");
//		templates.add(pair);
	}
	private Button[] templatesButtons;
	private String viewType;
	private Text[] childCountTexts;
	private Button[] orgButtons;
	private int childCount;
	private boolean withOrg = false;
	
	public ChooseWindowTemplateDialog() {
		super(null, CHOOSE_WINDOW_TEMPLATE);
	}
	
	public ChooseWindowTemplateDialog(Shell parentShell) {
		super(parentShell, CHOOSE_WINDOW_TEMPLATE);
	}
	
	@Override
	protected Point getInitialSize() {
		Point point = new Point(1000, 400);
		return point;
	}

	protected Control createDialogArea(Composite parent) {
		childCountTexts = new Text[templates.size()];
		orgButtons = new Button[templates.size()];
		
		Composite currParent = new Composite(parent, SWT.NONE);
		currParent.setLayout(new GridLayout(2, true));
		currParent.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		templategp = new TemplateGroup(currParent);
		previewGroup = new PreviewGroup(currParent);
		
		templategp.addObserver(previewGroup);
		
		return currParent;
	}
	private final class TemplateGroup extends Observable implements 
	IDialogFieldListener{

//		private int index = 0;
		public TemplateGroup(Composite composite){
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_BOTH));
			group.setLocation(100, 100);
//			group.setSize(300, 200);
			group.setLayout(new GridLayout(3, true));
			GridData layoutData = new GridData(GridData.FILL_BOTH);
//			layoutData.horizontalSpan = 2;
			group.setLayoutData(layoutData);
			group.setText(M_window.ChooseWindowTemplateDialog_8);	
			
			int size = templates.size();
			templatesButtons = new Button[size];
			for(int i = 0; i < size; i ++){
				Pair pair = templates.get(i);
//				int count = 1;
//				if(pair.org)
//					count ++;
//				if(pair.childCount)
//					count ++;
				
				
				templatesButtons[i] = new Button(group, SWT.RADIO);
				templatesButtons[i].setSelection(i == 0);
				templatesButtons[i].setText(pair.value);
				templatesButtons[i].setData(pair);
				GridData gd = new GridData(GridData.FILL_BOTH);
				templatesButtons[i].setLayoutData(gd);
//				templatesButtons[i].addSelectionListener(new SelectionListener() {
//					
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						tempIndex = index;
//						setChanged();
//						notifyObservers();
//					}
//					
//					@Override
//					public void widgetDefaultSelected(SelectionEvent e) {
//						// TODO 自动生成的方法存根
//						
//					}
//				});
				templatesButtons[i].setData("index", i);  //$NON-NLS-1$
				templatesButtons[i].addMouseListener(new MouseListener() {
					
					@Override
					public void mouseUp(MouseEvent e) {
						tempIndex = (Integer)((Button)e.getSource()).getData("index");  //$NON-NLS-1$
						setChanged();
						notifyObservers();
					}
					
					@Override
					public void mouseDown(MouseEvent e) {
						// TODO 自动生成的方法存根
						
					}
					
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						// TODO 自动生成的方法存根
						
					}
				});
				
				if(pair.childCount){
					Composite childParent = new Composite(group, SWT.NONE);
					childParent.setLayout(new GridLayout(2, false));
					GridData data = new GridData(GridData.FILL_HORIZONTAL);
					data.horizontalIndent = 10;
					childParent.setLayoutData(data);
					childCountTexts[i] = new Text(childParent, SWT.NONE);
					childCountTexts[i].setText("0");  //$NON-NLS-1$
					childCountTexts[i].setLayoutData(new GridData(14,16));
					
					Label label = new Label(childParent, SWT.NONE);
					label.setText(M_window.ChooseWindowTemplateDialog_11);
				}
				else{
					new Label(group, SWT.NONE);
				}
				if(pair.org){
					orgButtons[i] = new Button(group, SWT.CHECK);
					orgButtons[i].setSelection(false);
					orgButtons[i].setText(M_window.ChooseWindowTemplateDialog_12);
					GridData data = new GridData(GridData.FILL_HORIZONTAL);
					data.horizontalIndent = 10;
					data.widthHint = 10;
					orgButtons[i].setLayoutData(data);
				}
				else{
					new Label(group, SWT.NONE);
				}
			}
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			// TODO 自动生成的方法存根
			
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
			group.setText(M_window.ChooseWindowTemplateDialog_9); 
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.horizontalSpan = 1;
			group.setLayoutData(layoutData);

			canvas = new Canvas(group,SWT.NONE);
			canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			imageList.add(ImageProvider.listwin_nonorg);
			imageList.add(ImageProvider.listwin_org);
			imageList.add(ImageProvider.cardwin);
			imageList.add(ImageProvider.cardwin_wfm);
			imageList.add(ImageProvider.okcancel);
			imageList.add(ImageProvider.saveadd);
			
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
		public void dialogFieldChanged(DialogField field) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			int index = tempIndex;
//			MainPlugin.getDefault().logError(""+index);
			if(index ==0){
				canvas.setVisible(false);
				return;
			}
			canvas.setVisible(true);
			final Image previewImg = imgList.get(index-1);
			
			canvas.addPaintListener(new PaintListener(){
				public void paintControl(final PaintEvent event){
					if(previewImg!=null){
						final Rectangle imgBounds = previewImg.getBounds();
						int imgwidth = imgBounds.width;
						int imgheight = imgBounds.height;
						final Rectangle canBounds = canvas.getBounds();
						int canwidth = canBounds.width;
//						int canheight = canBounds.height;
						event.gc.drawImage(previewImg, 0, 0,imgwidth,imgheight,0,0,canwidth,(int)(imgheight*canwidth/imgwidth));
					}
				}
			});
			canvas.redraw();
		}
	}
	protected void okPressed() {
		for(int i = 0; i < templatesButtons.length; i ++){
			Button bt = templatesButtons[i];
			if(bt.getSelection()){
				setViewType(((Pair)bt.getData()).key);
				if(childCountTexts[i] != null){
					String countStr = childCountTexts[i].getText();
					try{
						int count = parseCount(countStr);
						if(count < 0){
							MessageDialog.openError(null, M_window.ChooseWindowTemplateDialog_13, M_window.ChooseWindowTemplateDialog_14);
							return;
						}
						childCount = count;
					}
					catch(Exception e){
						MessageDialog.openError(null, M_window.ChooseWindowTemplateDialog_15, M_window.ChooseWindowTemplateDialog_16);
						return;
					}
				}
				if(orgButtons[i] != null){
					withOrg = orgButtons[i].getSelection();
				}
				break;
			}
		}
		//流式布局
		super.okPressed();
	}


	private int parseCount(String countStr) {
		return Integer.parseInt(countStr);
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	
	public String getViewType() {
		return viewType;
	}

	public int getChildCount() {
		return childCount;
	}

	public boolean isWithOrg() {
		return withOrg;
	}


}
class Pair {
	protected String key;
	protected String value;
	protected boolean org;
	protected boolean childCount;
	public Pair(String key, String value){
		this.key = key;
		this.value = value;
	}
}
