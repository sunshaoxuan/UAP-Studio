package uap.lfw.portal.page;

import java.util.List;

import nc.uap.portal.om.ElementOrderly;
import nc.uap.portal.om.Layout;
import nc.uap.portal.om.Page;
import nc.uap.portal.om.Portlet;

/**
 * page oper
 * 
 * @author licza
 * 
 */
public class PageOper {
	/**
	 * ��ѯһ������
	 * 
	 * @param lay
	 * @param id
	 * @return
	 */
	public static Layout findLayout(Layout lay, String id) {
		if (lay != null) {
			if (lay.id.equals(id))
				return lay;
			List<ElementOrderly> childs = lay.getChild();
			for (ElementOrderly child : childs) {
				if (child instanceof Layout) {
					Layout layout = (Layout) child;
					if (layout.getId().equals(id)) {
						return (Layout) child;
					} else {
						Layout f = findLayout((Layout) child, id);
						if (f != null)
							return f;
					}
				}
			}
		}
		return null;
	}

	/**
	 * ��ѯ�˿ؼ��ĸ�Layout
	 * 
	 * @param lay
	 * @param portletId
	 * @return
	 */
	public static Layout findParentLayout(Layout lay, String portletId) {
		List<ElementOrderly> childs = lay.getChild();
		for (ElementOrderly child : childs) {
			if (child instanceof Layout) {
				Layout layout = (Layout) child;
				if (layout.getId().equals(portletId)) {
					return lay;
				}
				Layout _lay = findParentLayout(layout, portletId);
				if (_lay != null)
					return _lay;
			} else {
				Portlet portlet = (Portlet) child;
				if (portlet.getId().equals(portletId)) {
					return lay;
				}
			}
		}
		return null;
	}

	/**
	 * ��layout�в�����Ԫ��
	 * 
	 * @param lay
	 * @param portletId
	 * @return
	 */
	public static ElementOrderly findById(Layout lay, String portletId) {
		List<ElementOrderly> childs = lay.getChild();
		for (ElementOrderly child : childs) {
			String id = null;
			if (child instanceof Portlet) {
				id = ((Portlet) child).getId();
			} else {
				id = ((Layout) child).getId();
			}
			if (id.equals(portletId)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * �ݹ������Ԫ��
	 * 
	 * @param lay
	 * @param portletId
	 * @return
	 */
	public static Portlet findPortletRecursion(Layout lay, String portletId) {
		List<ElementOrderly> childs = lay.getChild();
		for (ElementOrderly child : childs) {
			if (child instanceof Layout) {
				Layout layout = (Layout) child;
				Portlet portlet = findPortletRecursion(layout, portletId);
				if (portlet != null)
					return portlet;
			} else {
				Portlet portlet = (Portlet) child;
				if (portlet.getId().equals(portletId)) {
					return portlet;
				}
			}
		}
		return null;
	}

	/**
	 * ����ҳ���е�һ��portlet���ڵ�layout
	 * 
	 * @param page
	 * @return
	 */
	public static Layout findFirstPortletLayout(Page page) {
		Layout lay = page.getLayout();
		Layout layout = findFirstPortletLayout(lay);
		return layout != null ? layout : lay;
	}

	/**
	 * ���������е�һ��portlet���ڵ�layout
	 * 
	 * @param lay
	 * @return
	 */
	private static Layout findFirstPortletLayout(Layout lay) {
		List<ElementOrderly> childs = lay.getChild();
		for (ElementOrderly child : childs) {
			if (child instanceof Portlet) {
				return lay;
			} else {
				Layout layout = findFirstPortletLayout((Layout) child);
				if (layout != null)
					return layout;
			}
		}
		return null;
	}

}
