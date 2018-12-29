package nc.uap.portal.freemarker.rules;

import org.eclipse.jface.text.rules.IToken;


public class MacroInstanceRuleEnd extends DirectiveRuleEnd {

	public MacroInstanceRuleEnd(IToken token) {
		super("", token, false);
	}

	protected char getIdentifierChar() {
		return '@';
	}
}