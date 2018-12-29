package nc.uap.lfw.ref;

public enum RefTypeEnum {
	//树，表，树表
	Tree(0),Grid(1),TreeGrid(2);
	private String name;
	
	RefTypeEnum(int type){
		if(type==0){
			setName("树型");
		}else if(type==1){
			setName("表型");
		}else if(type==2){
			setName("树表型");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
