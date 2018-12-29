package nc.uap.lfw.perspective;

import nc.uap.lfw.lang.M_perspective;
import nc.uap.lfw.perspective.views.ILFWViewPage;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * lfwƒ£–Õ ”Õº
 * @author zhangxya
 *
 */
public class LFWViewSheet extends PageBookView implements ISelectionListener {

	public LFWViewSheet() {
		// TODO Auto-generated constructor stub
	}

	
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		site.getPage().addSelectionListener(this);
		
	}

	
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}

	
	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(M_perspective.LFWViewSheet_0);
		return page;

	}

	
	protected void setPartName(String partName) {
	
		super.setPartName(partName);
	}

	
	protected PageRec doCreatePage(IWorkbenchPart part) {
		Object obj = part.getAdapter(ILFWViewPage.class);
		if (obj != null && obj instanceof ILFWViewPage) {
			ILFWViewPage page = (ILFWViewPage) obj;
			if (page instanceof IPageBookViewPage) {
				initPage((IPageBookViewPage) page);
			}
			page.createControl(getPageBook());
			return new PageRec(part, page);
		}
		return null;
	}

	
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		IPage page = pageRecord.page;
		page.dispose();
		pageRecord.dispose();

	}

	
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			return page.getActivePart();
		}
		return null;
	}

	
	protected boolean isImportant(IWorkbenchPart part) {
		return (part instanceof IEditorPart);

	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part == this || selection == null) {
			return;
		}
		if (!(getCurrentPage() instanceof ILFWViewPage))
			return;
		ILFWViewPage page = (ILFWViewPage) getCurrentPage();
		if (page != null && !selection.isEmpty()) {
			page.selectionChanged(part, selection);
		}
	}
	
}


