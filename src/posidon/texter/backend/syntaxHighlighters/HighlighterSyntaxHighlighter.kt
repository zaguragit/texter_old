package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import java.awt.Color
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
                    StyleConstants.setForeground(sas, Color(0x7599FF))
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                    startPos += tokens[0].length + 1
                    StyleConstants.setForeground(sas, Color(0x9CDC45))
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, line.length - tokens[0].length - 1, sas, false)
                } else {
                    val sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0xFF001C))
                    doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                    startPos += tokens[0].length + 1
                    doc.setCharacterAttributes(startPos, line.length - tokens[0].length - 1, defaultTextStyle(), false)
                }
            } else {
                var sas = defaultTextStyle()
                StyleConstants.setForeground(sas, Color(0xFFB714))
                StyleConstants.setBold(sas, true)
                doc.setCharacterAttributes(startPos, tokens[0].length, sas, false)
                startPos += tokens[0].length + 1
                if (tokens[1] == "->") {
                    StyleConstants.setForeground(sas, Color(0x6E87B4))
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, tokens[1].length, sas, false)
                } else {
                    StyleConstants.setForeground(sas, Color(0xFF001C))
                    StyleConstants.setBold(sas, false)
                    doc.setCharacterAttributes(startPos, tokens[1].length, sas, false)
                }
                startPos += tokens[1].length + 1
                doc.setCharacterAttributes(startPos, line.length - tokens[0].length - tokens[1].length - 2, defaultTextStyle(), false)
            }
        } else {
            val sas = SimpleAttributeSet()
            StyleConstants.setForeground(sas, Color(0xFF001C))
            doc.setCharacterAttributes(lineStart, line.length, sas, false)
        }
    }
}