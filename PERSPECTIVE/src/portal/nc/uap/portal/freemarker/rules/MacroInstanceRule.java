package nc.uap.portal.freemarker.rules;

import org.eclipse.jface.text.rules.IToken;


public class MacroInstanceRule extends DirectiveRule {

	public MacroInstanceRule(IToken token) {
		super("", token, false);
	}

	protected char getIdentifierChar() {
		return '@';
	}
}