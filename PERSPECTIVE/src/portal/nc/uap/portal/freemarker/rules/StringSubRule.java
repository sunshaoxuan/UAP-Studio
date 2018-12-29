package nc.uap.portal.freemarker.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;


public class StringSubRule extends SingleLineRule {

	private int unReadAmount;
	public StringSubRule(String startSequence, String endSequence, int unReadAmount, IToken token) {
		super(startSequence, endSequence, token);
	}

	protected boolean sequenceDetected(ICharacterScanner scanner,
			char[] sequence, boolean eofAllowed) {
		return true;
	}

	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		for (int i=0; i<unReadAmount; i++)
			scanner.unread();
		return true;
	}
}