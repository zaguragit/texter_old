package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class HighlighterSyntaxHighlighter : SyntaxHighlighter() {

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        val tokens = line.split(' ')
        if (tokens.size > 1) {
            var startPos = lineStart
            if (tokens[0].startsWith('@')) {
                if (tokens[0] in CustomSyntax.HIGHLIGHTER_FLAGS) {
                    val sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Window.theme.blue)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                    startPos += tokens[0].length + 1
                    StyleConstants.setForeground(sas, Window.theme.lime)
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, line.length - tokens[0].length - 1, sas, false)
                } else {
                    val sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Window.theme.red)
                    doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                    startPos += tokens[0].length + 1
                    doc.setCharacterAttributes(startPos, line.length - tokens[0].length - 1, defaultTextStyle(), false)
                }
            } else {
                var sas = defaultTextStyle()
                StyleConstants.setForeground(sas, Window.theme.yellow)
                StyleConstants.setBold(sas, true)
                doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                startPos += tokens[0].length + 1
                if (tokens[1] == "->") {
                    StyleConstants.setForeground(sas, Window.theme.blue_gray)
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, tokens[1].length, sas, false)
                } else {
                    StyleConstants.setForeground(sas, Window.theme.red)
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, tokens[1].length, sas, false)
                }
                startPos += tokens[1].length + 1
                doc.setCharacterAttributes(startPos, line.length - tokens[0].length - tokens[1].length - 2, defaultTextStyle(), false)
            }
        } else {
            val sas = SimpleAttributeSet()
            StyleConstants.setForeground(sas, Window.theme.red)
            doc.setCharacterAttributes(lineStart, line.length, sas, false)
        }
    }
}