


package majesty_gpl_plugin.editors;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.ui.editors.text.FileDocumentProvider;



public class GPLDocumentProvider extends FileDocumentProvider {
	

	
	public final static String GPL_COMMENT = "__gpl_comment";
	public final static String GPL_TAG     = "__gpl_tag"    ;
	
	
	
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document == null) return null;
		
		//*
		IDocumentPartitioner partitioner = new FastPartitioner(
			new GPLPartitionScanner(),
			new String[] { GPL_TAG, GPL_COMMENT }
		);
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		//*/
		return document;
	}
	
	
	//*
	class GPLPartitionScanner extends RuleBasedPartitionScanner {
		GPLPartitionScanner() {
			IToken xmlComment = new Token(GPL_COMMENT);
			IPredicateRule[] rules = new IPredicateRule[1];
			rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
			setPredicateRules(rules);
		}
	}
	//*/
}





