


package gpl_plugin.editors;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.jface.text.presentation.*;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.jface.text.hyperlink.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.editors.text.*;
import org.eclipse.ui.texteditor.*;



//  Using the following guides for the moment-
//  https://wiki.eclipse.org/FAQ_How_do_I_write_an_editor_for_my_own_language%3F
//  http://wiki.eclipse.org/FAQ_How_do_I_provide_syntax_coloring_in_an_editor%3F
//  http://www.vogella.com/tutorials/EclipsePlugin/article.html#extending-the-eclipse-ide
//  http://codeandme.blogspot.ie/2014/06/adding-hyperlink-detectors-to-editors.html
//  http://www.ibm.com/developerworks/opensource/tutorials/os-ecl-commplgin3/



public class GPLEditor extends TextEditor {

	//private ColorManager colorManager;

	public GPLEditor() {
		super();
		//colorManager = new ColorManager();
		setSourceViewerConfiguration(new GPLConfiguration());
		setDocumentProvider(new GPLDocumentProvider());
	}
	
	
	public void dispose() {
		//colorManager.dispose();
		super.dispose();
	}
	

	
	
	
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
		
		public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
			return new IHyperlinkDetector[] { new FunctionLinker() };
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
				forNormal  = colourToken(0  , 0  , 0  ),
				forKeyword = colourToken(191, 0  , 191),  //purple
				forComment = colourToken(0  , 127, 0  ),  //green
				forString  = colourToken(0  , 0  , 191);  //blue
			
			WordRule keywordRule = new WordRule(new IWordDetector() {
				public boolean isWordStart(char c) {
					return Character.isJavaIdentifierStart(c);
				}

				public boolean isWordPart(char c) {
					return Character.isJavaIdentifierPart(c);
				}
			}, forNormal, true);
			for (String word : GPL_KEYWORDS) {
				 keywordRule.addWord(word, forKeyword);
			}
			
			setRules(new IRule[] {
					keywordRule,
					new SingleLineRule("//", null, forComment),
					new SingleLineRule("\"", "\"", forString, '\\'),
					new WhitespaceRule(new IWhitespaceDetector() {
						public boolean isWhitespace(char c) {
							return Character.isWhitespace(c);
						}
					}),
			});
		}
		
		
		private Token colourToken(int r, int g, int b) {
			final Device device = Display.getDefault();
			return new Token(new TextAttribute(new Color(device, r, g, b)));
		}
	}
	
	
	
	/**  Allows hyper-linking between named functions (not functional yet.)
	  */
	class FunctionLinker extends AbstractHyperlinkDetector {
		
		public IHyperlink[] detectHyperlinks(
			ITextViewer viewer, IRegion region, boolean showMultipleLinks
		) {
			IDocument document = viewer.getDocument();
			int offset = region.getOffset();
			
			
			//  Extract relevant characters
			IRegion lineRegion;
			String candidate;
			try {
				lineRegion = document.getLineInformationOfOffset(offset);
				candidate  = document.get(lineRegion.getOffset(), lineRegion.getLength());
			}
			catch (BadLocationException e) { return null; }
			
			
			
			
			return null;
			/*
			// look for keyword
			int index = candidate.indexOf(PREFERENCES);
			if (index != -1) {

				// detect region containing keyword
				IRegion targetRegion = new Region(lineRegion.getOffset()
						+ index, PREFERENCES.length());
				if ((targetRegion.getOffset() <= offset)
						&& ((targetRegion.getOffset() + targetRegion
								.getLength()) > offset))
					// create link
					return new IHyperlink[] { new PreferencesHyperlink(
							targetRegion) };
			}
			return null;
			//*/
		}
	}
	
	class FunctionHyperlink implements IHyperlink {

		private final IRegion fUrlRegion;
		
		
		public FunctionHyperlink(IRegion urlRegion) {
			fUrlRegion = urlRegion;
		}
		
		
		public IRegion getHyperlinkRegion() {
			return fUrlRegion;
		}
		
		
		public String getTypeLabel() {
			return "function";
		}
		
		
		public String getHyperlinkText() {
			return null;
		}
		
		
		public void open() {
			//  TODO:  Implement this!
		}
	}

}










