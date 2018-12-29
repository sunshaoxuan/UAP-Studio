package nc.uap.lfw.chart.core;

import nc.lfw.editor.common.LFWBaseEditor;
import nc.uap.lfw.chart.model.BaseChartModelEleObj;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class ChartEditPartFactory  implements EditPartFactory {
	private LFWBaseEditor editor = null;
	public ChartEditPartFactory(LFWBaseEditor editor) {
		super();
		this.editor = editor;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if(model instanceof ChartGraph){
			editPart= new ChartGraphPart(editor);
		}
		else if(model instanceof ChartCompEleObj || model instanceof BaseChartModelEleObj||model instanceof ChartConfigEleObj){
			editPart = new ChartElementPart(editor);
		}
		editPart.setModel(model);
		return editPart;
	}
}