package posidon.texter.backend.syntaxHighlighters

import java.awt.Color
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class MarkdownSyntaxHighlighter : SyntaxHighlighter() {

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        val tokens = line.split(' ')
        if (tokens.size > 1) {
            var startPos = lineStart
            when (tokens[0]) {
                "#" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 48)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 1, line.length - 1, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 1, sas, false)
                }
                "##" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 40)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 2, line.length - 2, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 2, sas, false)
                }
                "###" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 32)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 3, line.length - 3, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 3, sas, false)
                }
                "####" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 24)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 4, line.length - 4, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 4, sas, false)
                }
                "#####" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 18)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 5, line.length - 5, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 5, sas, false)
                }
                "######" -> {
                    var sas = defaultTextStyle()
                    StyleConstants.setFontSize(sas, 16)
                    StyleConstants.setBold(sas, true)
                    doc.setCharacterAttributes(startPos + 6, line.length - 6, sas, false)

                    sas = defaultTextStyle()
                    StyleConstants.setForeground(sas, Color(0x88ffffff.toInt(), true))
                    doc.setCharacterAttributes(startPos, 6, sas, false)
                }
                else -> {
                    doc.setCharacterAttributes(startPos, line.length, defaultTextStyle(), false)
                }
            }
        }
    }
}