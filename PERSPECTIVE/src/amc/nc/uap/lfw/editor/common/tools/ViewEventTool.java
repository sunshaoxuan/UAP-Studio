/**
 * 
 */
package nc.uap.lfw.editor.common.tools;

import java.util.ArrayList;
import java.util.List;

import nc.uap.lfw.core.comp.ContextMenuComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.event.conf.EventConf;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.BaseRefNode;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.jsp.uimeta.UIElement;

/**
 * @author chouhl
 * 2011-11-11
 */
public class ViewEventTool {
	
	/**
	 * 获取View对象事件集合（包括View中各个控件,UIMeta事件）
	 * @param viewConf
	 * @return
	 */
	public static EventConf[] getAllEvents(LfwView viewConf, UIElement uielement){
		//事件List集合
		List<EventConf> ecList= new ArrayList<EventConf>();
		if(viewConf != null){
			//Component
			ViewComponents vc = viewConf.getViewComponents();
			//Model
			ViewModels vm = viewConf.getViewModels();
			//菜单
			ViewMenus vmenu = viewConf.getViewMenus();	
			//View事件集合
			EventConf[] events = viewConf.getEventConfs();
			if(events != null && events.length > 0){
				for(EventConf event : events){
					ecList.add(event);
				}
			}
			//Web元素集合
			WebElement[] wcs = null;
			//控件元素集合
			wcs = vc.getComponents();
			//控件元素事件集合
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					EventConf[] ecs = wc.getEventConfs();
					if(ecs != null && ecs.length > 0){
						for(EventConf ec : ecs){
							ecList.add(ec);
						}
					}
					if(wc instanceof GridComp){
						GridComp gc = (GridComp)wc;
						if(gc.getMenuBar()!=null){
							List<WebElement> weList = new ArrayList<WebElement>();
							List<MenuItem> menuList = gc.getMenuBar().getMenuList();
							if(menuList!=null&&menuList.size()>0){
								for(MenuItem menu:menuList){
									weList.addAll(getMenuItems(menu));
								}
							}
							if(weList.size()>0){
								for(WebElement we : weList){
									EventConf[] ecfs = we.getEventConfs();
									if(ecfs != null && ecfs.length > 0){
										for(EventConf ec : ecfs){
											ecList.add(ec);
										}
									}
								}
							}
						}
					}
				}
			}
			//下拉数据集 集合
			wcs = vm.getComboDatas();
			//下拉数据集事件集合
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					EventConf[] ecs = wc.getEventConfs();
					if(ecs != null && ecs.length > 0){
						for(EventConf ec : ecs){
							ecList.add(ec);
						}
					}
				}
			}
			//数据集 集合
			wcs = vm.getDatasets();
			//数据集事件集合
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					EventConf[] ecs = wc.getEventConfs();
					if(ecs != null && ecs.length > 0){
						for(EventConf ec : ecs){
							ecList.add(ec);
						}
					}
				}
			}
			//参照集合
			IRefNode[] rns = vm.getRefNodes();
			//参照事件集合
			if(rns != null && rns.length > 0){
				for(IRefNode rn : rns){
					if(rn instanceof BaseRefNode){
						EventConf[] ecs = ((BaseRefNode)rn).getEventConfs();
						if(ecs != null && ecs.length > 0){
							for(EventConf ec : ecs){
								ecList.add(ec);
							}
						}
					}
				}
			}
			//菜单集合
			MenubarComp[] mcs = vmenu.getMenuBars();
			//菜单项集合
			wcs = null;
			if(mcs != null && mcs.length > 0){
				List<WebElement> weList = new ArrayList<WebElement>();
				for(MenubarComp comp : mcs){
					if(comp.getMenuList() != null){
						List<MenuItem> miList = comp.getMenuList();
						for(MenuItem mi : miList){
							weList.addAll(getMenuItems(mi));
						}
					}
				}
				wcs = weList.toArray(new WebElement[0]);
			}
			//菜单项事件集合
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					EventConf[] ecs = wc.getEventConfs();
					if(ecs != null && ecs.length > 0){
						for(EventConf ec : ecs){
							ecList.add(ec);
						}
					}
				}
			}
			//右键菜单集合
			ContextMenuComp[] cmcs = vmenu.getContextMenus();
			//右键菜单项集合
			wcs = null;
			if(cmcs != null && cmcs.length > 0){
				List<WebElement> weList = new ArrayList<WebElement>();
				for(ContextMenuComp comp : cmcs){
					if(comp.getItemList() != null){
						List<MenuItem> miList = comp.getItemList();
						for(MenuItem mi : miList){
							weList.addAll(getMenuItems(mi));
						}
					}
				}
				wcs = weList.toArray(new WebElement[0]);
			}
			//右键菜单项事件集合
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					EventConf[] ecs = wc.getEventConfs();
					if(ecs != null && ecs.length > 0){
						for(EventConf ec : ecs){
							ecList.add(ec);
						}
					}
				}
			}
		}
		//UIMeta事件
		if(uielement != null){
			EventConf[] eventConfs = uielement.getEventConfs();
			if(eventConfs != null && eventConfs.length > 0){
				for(EventConf event : eventConfs){
					ecList.add(event);
				}
			}
		}
		//View包含所有事件集合
		return ecList.toArray(new EventConf[0]);
	}
	
	/**
	 * 移除View对象事件（包括View中各个控件）
	 * @param viewConf
	 * @return
	 */
	public static void removeEvent(LfwView viewConf, String eventName, String methodName){
		//Component
		ViewComponents vc = viewConf.getViewComponents();
		//Model
		ViewModels vm = viewConf.getViewModels();
		//菜单
		ViewMenus vmenu = viewConf.getViewMenus();

		viewConf.removeEventConf(eventName, methodName);
		
		//Web元素集合
		WebElement[] wcs = null;
		//控件元素集合
		wcs = vc.getComponents();
		//控件元素事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//下拉数据集 集合
		wcs = vm.getComboDatas();
		//下拉数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//数据集 集合
		wcs = vm.getDatasets();
		//数据集事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//参照集合
		IRefNode[] rns = vm.getRefNodes();
		//参照事件集合
		if(rns != null && rns.length > 0){
			for(IRefNode rn : rns){
				if(rn instanceof BaseRefNode){
					((BaseRefNode)rn).removeEventConf(eventName, methodName);
				}
			}
		}
		//菜单集合
		MenubarComp[] mcs = vmenu.getMenuBars();
		//菜单项集合
		if(mcs != null && mcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(MenubarComp comp : mcs){
				if(comp.getMenuList() != null){
					List<MenuItem> miList = comp.getMenuList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
		//右键菜单集合
		ContextMenuComp[] cmcs = vmenu.getContextMenus();
		//右键菜单项集合
		if(cmcs != null && cmcs.length > 0){
			List<WebElement> weList = new ArrayList<WebElement>();
			for(ContextMenuComp comp : cmcs){
				if(comp.getItemList() != null){
					List<MenuItem> miList = comp.getItemList();
					for(MenuItem mi : miList){
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new WebElement[0]);
		}
		//右键菜单项事件集合
		if(wcs != null && wcs.length > 0){
			for(WebElement wc : wcs){
				wc.removeEventConf(eventName, methodName);
			}
		}
	}
	
	private static List<MenuItem> getMenuItems(MenuItem mi){
		List<MenuItem> miList = new ArrayList<MenuItem>();
		if(mi != null){
			miList.add(mi);
			List<MenuItem> childList = mi.getChildList();
			if(childList != null && childList.size() > 0){
				for(MenuItem item : childList){
//					miList.add(item);
					miList.addAll(getMenuItems(item));
				}
			}
		}
		return miList;
	}
	
	public static WebElement getWebElementById(LfwView viewConf, String id){
		if(viewConf != null && id != null){
			//Component
			ViewComponents vc = viewConf.getViewComponents();
			//Model
			ViewModels vm = viewConf.getViewModels();
			//菜单
			ViewMenus vmenu = viewConf.getViewMenus();
			
			//Web元素集合
			WebElement[] wcs = null;
			//控件元素集合
			wcs = vc.getComponents();
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					if(id.equals(wc.getId())){
						return wc;
					}
				}
			}
			//下拉数据集 集合
			wcs = vm.getComboDatas();
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					if(id.equals(wc.getId())){
						return wc;
					}
				}
			}
			//数据集 集合
			wcs = vm.getDatasets();
			if(wcs != null && wcs.length > 0){
				for(WebElement wc : wcs){
					if(id.equals(wc.getId())){
						return wc;
					}
				}
			}
			//参照集合
			IRefNode[] rns = vm.getRefNodes();
			if(rns != null && rns.length > 0){
				for(IRefNode rn : rns){
					if(id.equals(rn.getId()) && rn instanceof BaseRefNode){
						return (BaseRefNode)rn;
					}
				}
			}
			//菜单集合
			MenubarComp[] mcs = vmenu.getMenuBars();
			if(mcs != null && mcs.length > 0){
				//菜单项集合
				List<WebElement> weList = new ArrayList<WebElement>();
				for(MenubarComp comp : mcs){
					if(comp.getMenuList() != null){
						List<MenuItem> miList = comp.getMenuList();
						for(MenuItem mi : miList){
							weList.addAll(getMenuItems(mi));
						}
					}
				}
				wcs = weList.toArray(new WebElement[0]);
				for(WebElement we : wcs){
					if(id.equals(we.getId())){
						return we;
					}
				}
			}
			//右键菜单集合
			ContextMenuComp[] cmcs = vmenu.getContextMenus();
			if(cmcs != null && cmcs.length > 0){
				List<WebElement> weList = new ArrayList<WebElement>();
				for(ContextMenuComp comp : cmcs){
					if(comp.getItemList() != null){
						List<MenuItem> miList = comp.getItemList();
						for(MenuItem mi : miList){
							weList.addAll(getMenuItems(mi));
						}
					}
				}
				wcs = weList.toArray(new WebElement[0]);
				for(WebElement we : wcs){
					if(id.equals(we.getId())){
						return we;
					}
				}
			}
		}
		return null;
	}
	
}
