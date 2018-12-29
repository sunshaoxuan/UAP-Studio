package nc.uap.lfw.dataset;

import java.util.List;

import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldRelation;
import nc.uap.lfw.core.data.MatchField;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class FieldRelationContentProvider extends LabelProvider implements
ITableLabelProvider, ITreeContentProvider {

	public FieldRelationContentProvider(){
		super();
	}
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List)
			return ((List) inputElement).toArray();
		else 
			return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof FieldRelation)
			return true;
		else{
			return false;
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof FieldRelation){
			FieldRelation fr = (FieldRelation)element;
			switch (columnIndex) {
			case 0:
				return fr.getId()==null?null:fr.getId();
			case 1:
				MatchField[] fields = fr.getMatchFields();
				if(fields!=null&&fields.length>0){
					String readField = "";
					for(MatchField field:fields){
						readField = readField + field.getReadField()+",";
					}
					readField = readField.substring(0, readField.length()-1);
					return readField;					
				}
				break;
			case 2:
				fields = fr.getMatchFields();
				if(fields!=null&&fields.length>0){
					String writeField = "";
					for(MatchField field:fields){
						writeField = writeField + field.getWriteField()+",";
					}
					writeField = writeField.substring(0, writeField.length()-1);
					return writeField;					
				}
				break;
			case 3:
				return fr.getRefDataset()==null?null:fr.getRefDataset();
			}
		}
		
		return null;
	}

}
