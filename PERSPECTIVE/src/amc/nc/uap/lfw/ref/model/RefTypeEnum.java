package nc.uap.lfw.ref.model;

import nc.uap.lfw.lang.M_editor;

public enum RefTypeEnum {
	//Ê÷£¬±í£¬Ê÷±í
	Tree(0),Grid(1),TreeGrid(2);
	private String name;
	
	RefTypeEnum(int type){
		if(type==0){
			setName(M_editor.RefTypeEnum_0);
		}else if(type==1){
			setName(M_editor.RefTypeEnum_1);
		}else if(type==2){
			setName(M_editor.RefTypeEnum_2);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
