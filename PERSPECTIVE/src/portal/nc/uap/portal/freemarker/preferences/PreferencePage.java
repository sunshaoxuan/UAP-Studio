package nc.uap.portal.freemarker.preferences;


import nc.uap.portal.perspective.PortalPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class PreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage, IPreferenceConstants {
	
	public PreferencePage() {
		super(GRID);
		setPreferenceStore(PortalPlugin.getDefault().getPreferenceStore());
		setDescription("FreeMarker Settings");
	}

	public void createFieldEditors() {
		addField(new ColorFieldEditor(COLOR_DIRECTIVE,
				"Directive:", getFieldEditorParent()));
        addField(new ColorFieldEditor(COLOR_RELATED_ITEM,
                "Related Directives:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(HIGHLIGHT_RELATED_ITEMS,
				"Highlight Related Directives", getFieldEditorParent()));
		addField(new ColorFieldEditor(COLOR_INTERPOLATION,
				"Interpolation:", getFieldEditorParent()));
		addField(new ColorFieldEditor(COLOR_TEXT,
				"Text:", getFieldEditorParent()));
		addField(new ColorFieldEditor(COLOR_COMMENT,
				"Comment:", getFieldEditorParent()));
		addField(new ColorFieldEditor(COLOR_STRING,
				"String:", getFieldEditorParent()));
        addField(new ColorFieldEditor(COLOR_XML_TAG,
                "HTML/XML Tag:", getFieldEditorParent()));
        addField(new ColorFieldEditor(COLOR_XML_COMMENT,
                "HTML/XML Comment:", getFieldEditorParent()));
	}
	
	public void init(IWorkbench workbench) {
	}
}