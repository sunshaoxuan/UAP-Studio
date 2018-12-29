package nc.uap.portal.freemarker.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;


public class StringRule extends SingleLineRule {

	private String rule;
	
	public StringRule(String rule, IToken token) {
		super(rule, "", token);
		this.rule = rule;
	}

	protected boolean sequenceDetected(ICharacterScanner scanner,
			char[] sequence, boolean eofAllowed) {
		return true;
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		return true;
	}
}