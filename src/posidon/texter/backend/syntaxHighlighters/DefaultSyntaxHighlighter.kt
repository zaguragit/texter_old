package posidon.texter.backend.syntaxHighlighters

import javax.swing.text.StyledDocument

class DefaultSyntaxHighlighter : SyntaxHighlighter() {
    override fun color(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {}
}