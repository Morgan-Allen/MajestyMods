


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
		
		/*
		IDocumentPartitioner partitioner = new FastPartitioner(
			new GPLPartitionScanner(),
			new String[] { XML_TAG, XML_COMMENT }
		);
		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);
		//*/
		return document;
	}
	
	
	/*
	class GPLPartitionScanner extends RuleBasedPartitionScanner {
		GPLPartitionScanner() {
			IToken xmlComment = new Token(XML_COMMENT);
			IToken tag = new Token(XML_TAG);
			IPredicateRule[] rules = new IPredicateRule[2];
			rules[0] = new MultiLineRule("<!--", "-->", xmlComment);
			rules[1] = new TagRule(tag);
			setPredicateRules(rules);
		}
	}
	//*/
}





