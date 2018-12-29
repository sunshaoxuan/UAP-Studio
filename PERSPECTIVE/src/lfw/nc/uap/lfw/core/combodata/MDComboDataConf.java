package nc.uap.lfw.core.combodata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import uap.lfw.core.env.EnvProvider;

public class MDComboDataConf extends ComboData{
	private static final long serialVersionUID = 1L;
	private String fullclassName;
	private List<CombItem> listCombItem;
	
	public String getFullclassName() {
		return fullclassName;
	}

	public void setFullclassName(String fullclassName) {
		this.fullclassName = fullclassName;
	}

	@Override
	public CombItem[] getAllCombItems() {
		if(EnvProvider.getInstance().isEditMode()){
			return new CombItem[0];
		}
		if(this.listCombItem == null){
			if(this.fullclassName == null)
				return null;
			try {
				listCombItem = new ArrayList<CombItem>();
				if(fullclassName.equals("1783c073-08ee-44e4-9704-ee6cfbd955d2")){					
					listCombItem.add(new CombItem("0", "管理类型", "2cpb_funno-0004"));
					listCombItem.add(new CombItem("1", "业务类型", "2UC000-000017"));
					listCombItem.add(new CombItem("2", "集团管理类型", "'cpb_funno-0007"));
					listCombItem.add(new CombItem("3", "系统管理类型", "2cpb_funno-0008"));
					return listCombItem.toArray(new CombItem[0]);
				}			
			} catch (Throwable e) {
				LfwLogger.error(e.getMessage(), e);
				listCombItem = new ArrayList<CombItem>(0);
			}
		}
		return listCombItem.toArray(new CombItem[0]);
	}
	
	public void removeComboItem(String itemId)
	{
		if(itemId == null)
			return;
		if(this.listCombItem == null)
			getAllCombItems();
		Iterator<CombItem> it = this.listCombItem.iterator();
		while(it.hasNext()){
			CombItem combo = it.next();
			if(combo.getValue() != null && combo.getValue().equals(itemId)){
				it.remove();
				super.removeComboItem(itemId);
				break;
			}
		}
	}

	@Override
	public void removeAllComboItems() {
		if(listCombItem == null){
			listCombItem = new ArrayList<CombItem>();
			return;
		}
		listCombItem.clear();
		super.removeAllComboItems();
	}

	@Override
	public void addCombItem(CombItem item) {
		if(this.listCombItem == null)
			getAllCombItems();
		this.listCombItem.add(item);
		super.addCombItem(item);
	}
	public Object clone()
	{
		MDComboDataConf combo = (MDComboDataConf) super.clone();
		if(this.listCombItem != null){
			combo.listCombItem = new ArrayList<CombItem>();
			for(CombItem item : this.listCombItem)
			{
				combo.listCombItem.add((CombItem)item.clone());
			}
		}
		return combo;
	}

}

