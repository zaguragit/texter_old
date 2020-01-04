package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

abstract class SyntaxHighlighter {
    abstract fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int)

    companion object {
        fun defaultTextStyle(): SimpleAttributeSet {
            val sas = SimpleAttributeSet()
            StyleConstants.setForeground(sas, Window.theme.textAreaFG)
            StyleConstants.setBackground(sas, Window.theme.textAreaBG)
            StyleConstants.setFontSize(sas, 15)
            StyleConstants.setItalic(sas, false)
            StyleConstants.setBold(sas, false)
            return sas
        }
    }
}