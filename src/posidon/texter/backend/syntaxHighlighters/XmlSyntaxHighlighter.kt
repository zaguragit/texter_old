package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import java.awt.Color
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class XmlSyntaxHighlighter : SyntaxHighlighter() {
    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        if (line.isNotEmpty()) {
            var startPos = lineStart
            val str = StringBuilder()
            for (i in 0..line.length) {
                if (i == line.length || line[i] == ' ' || line[i] == '\t' || line[i] == '=') {
                    val string = str.toString()
                    doc.setCharacterAttributes(startPos, str.length, when {
                        string.startsWith('<') || string.endsWith('>') -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Color(0xFFB536))
                            sas
                        }
                        else -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Window.theme.textAreaFG)
                            StyleConstants.setBackground(sas, Window.theme.textAreaBG)
                            StyleConstants.setItalic(sas, false)
                            StyleConstants.setBold(sas, false)
                            sas
                        }
                    }, false)
                    startPos += str.length + 1
                    str.clear()
                } else str.append(line[i])
            }

            var stringStart = line.indexOf('"')
            var stringEnd: Int
            val stringAttrs = SimpleAttributeSet()
            StyleConstants.setForeground(stringAttrs, Color(0x49984D))
            while (line.substring(stringStart + 1).contains('"')) {
                stringEnd = line.indexOf('"', stringStart + 1)
                doc.setCharacterAttributes(
                    lineStart + stringStart,
                    stringEnd - stringStart + 1,
                    stringAttrs,
                    false
                )
                if (line.substring(stringEnd + 1).contains('"'))
                    stringStart = line.indexOf('"', stringEnd + 1)
                else break
            }
        }
    }
}