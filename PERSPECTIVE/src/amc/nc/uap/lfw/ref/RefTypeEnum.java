package nc.uap.lfw.ref;

public enum RefTypeEnum {
	//����������
	Tree(0),Grid(1),TreeGrid(2);
	private String name;
	
	RefTypeEnum(int type){
		if(type==0){
			setName("����");
		}else if(type==1){
			setName("����");
		}else if(type==2){
			setName("������");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
