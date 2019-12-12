package posidon.texter.backend.syntaxHighlighters

import javax.swing.text.StyledDocument

abstract class SyntaxHighlighter() {
    abstract fun color(doc: StyledDocument, lineStart: Int, line: String, lineI: Int)
}