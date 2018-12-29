package nc.lfw.editor.common;

import nc.uap.lfw.figure.ui.IDirectEditable;

import org.eclipse.draw2d.Label;

/**
 *»ù´¡label
 * @author zhangxya
 *
 */
public class NameLabel extends Label implements IDirectEditable {
	private String id = null;
	public NameLabel(String text,String id) {
		super(text);
		this.id = id;
	}

	public Object getEditableObj() {
		return id;
	}

}
