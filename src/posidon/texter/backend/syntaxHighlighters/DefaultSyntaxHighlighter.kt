package posidon.texter.backend.syntaxHighlighters

import javax.swing.text.StyledDocument

class DefaultSyntaxHighlighter : SyntaxHighlighter() {
    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {}
}