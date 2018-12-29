package nc.uap.lfw.build.orm.mapping;

import java.util.ArrayList;
import java.util.List;

public class FkConstraint extends Constraint implements IFkConstraint {

	/** ������õ����� */
	private ITable refTable;
	/** ������������м� */
	private List<IColumn> refColumns;

	public FkConstraint() {
		super();
		this.refColumns = new ArrayList<IColumn>();
	}

	public ITable getRefTable() {
		return refTable;
	}

	public void setRefTable(ITable refTable) {
		this.refTable = refTable;
	}

	public List<IColumn> getRefColumns() {
		return refColumns;
	}

}
