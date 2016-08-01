

package majesty_game_gpl_plugin;
import java.util.*;

import org.eclipse.ui.texteditor.*;
import org.eclipse.ui.editors.text.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.jface.text.presentation.*;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


//  Using the following guides for the moment-
//  https://wiki.eclipse.org/FAQ_How_do_I_write_an_editor_for_my_own_language%3F
//  http://wiki.eclipse.org/FAQ_How_do_I_provide_syntax_coloring_in_an_editor%3F
//  http://stackoverflow.com/questions/299283/how-to-write-a-plugin-for-eclipse/299316#299316
//  http://stackoverflow.com/questions/3838558/eclipse-plug-in-create-a-new-file-extension-for-a-language-not-supported-by-ecl/4959583#4959583
//  http://www.ibm.com/developerworks/opensource/library/os-ecplug/



/**  Performs actual text-editing functions-
  */
public class GPLTextEditor extends TextEditor {
	
	
	public GPLTextEditor() {
		super();
		setSourceViewerConfiguration(new GPLConfiguration());
	}
	
	
	protected void createActions() {
		
	}
	
	
	/**  I'm... sure this will all prove useful some day...
	  */
	class GPLConfiguration extends SourceViewerConfiguration {

		public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
			PresentationReconciler pr = new PresentationReconciler();
			DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(new GPLScanner());
			pr.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);
			pr.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);
			return pr;
		}

		public IContentAssistant getContentAssistant(ISourceViewer sv) {
			ContentAssistant ca = new ContentAssistant();
			IContentAssistProcessor cap = new HippieProposalProcessor();
			ca.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
			ca.setInformationControlCreator(getInformationControlCreator(sv));
			return ca;
		}

		public ITextHover getTextHover(ISourceViewer sv, String contentType) {
			return new DefaultTextHover(sv);
		}
	}
	
	
	final static String GPL_KEYWORDS[] = {
		
		"declare",
		"define",
		"expression",
		
		"true",
		"false",
		"&&",
		"||",
		"if",
		"else",
		"begin",
		"end",
		"break",
		"continue",
		"while",
		"foreach",
		"return",
		
		"function",
		"prototype",
		
		"agent",
		"agentref",
		"boolean",
		"float",
		"integer",
		"coordinate",
		"string",
		"list",
	};
	
	
	class GPLScanner extends RuleBasedScanner {
		public GPLScanner() {
			
			final Token
				forKeyword = colourToken(255, 0  , 255),  //purple
				forComment = colourToken(0  , 255, 0  ),  //green
				forString  = colourToken(0  , 0  , 255);  //blue
			
			WordRule keywordRule = new WordRule(new IWordDetector() {
				public boolean isWordStart(char c) {
					return Character.isJavaIdentifierStart(c);
				}

				public boolean isWordPart(char c) {
					return Character.isJavaIdentifierPart(c);
				}
			});
			for (String word : GPL_KEYWORDS) {
				 keywordRule.addWord(word, forKeyword);
			}
			
			setRules(new IRule[] {
					keywordRule,
					new SingleLineRule("#", null, forComment),
					new SingleLineRule("\"", "\"", forString, '\\'),
					new SingleLineRule("'", "'", forString, '\\'),
					new WhitespaceRule(new IWhitespaceDetector() {
						public boolean isWhitespace(char c) {
							return Character.isWhitespace(c);
						}
					}),
			});
		}
		
		
		private Token colourToken(int r, int g, int b) {
			final Device device = Display.getDefault();
			return new Token(new TextAttribute(new Color(device, 255, 0  , 255)));
		}
	}

}










