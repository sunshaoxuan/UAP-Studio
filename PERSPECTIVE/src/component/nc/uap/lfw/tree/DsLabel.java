package nc.uap.lfw.tree;

import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.figure.ui.IDirectEditable;
import nc.uap.lfw.figure.ui.NullBorder;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;

/**
 * ds label
 * @author zhangxya
 *
 */


public class DsLabel extends Label implements IDirectEditable {

	private Dataset ds;

	public DsLabel(String text, Dataset ds) {
		super(text);
		this.ds = ds;
		setLabelAlignment(PositionConstants.CENTER);
		setBorder(new NullBorder());
		// ªÒ»°Õº∆¨
		
		markError();
	}

	private void markError() {
	}

	
	public Object getEditableObj() {
		// TODO Auto-generated method stub
		return ds;
	}

	}