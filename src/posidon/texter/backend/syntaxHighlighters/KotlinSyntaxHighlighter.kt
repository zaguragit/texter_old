package posidon.texter.backend.syntaxHighlighters

import com.sun.xml.internal.fastinfoset.util.StringArray
import posidon.texter.Window
import java.awt.Color
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class KotlinSyntaxHighlighter : AbstractSyntaxHighlighter() {



    init {
        val text = KotlinSyntaxHighlighter::class.java.getResource("code/highlighters/kt.txt").readText().split('\n')
        for (line in text) {
            when {
                line.startsWith("declaration") -> {

                }
                line.startsWith("visibility") -> {

                }
                line.startsWith("override") -> {

                }
            }
        }
    }

    override fun color(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        var startPos = lineStart
        val str = StringBuilder()
        for (i in 0..line.length) {
            if (i == line.length || line[i] == ' ') {
                val string = str.toString()
                doc.setCharacterAttributes(startPos, str.length, when (string) {
                    "val", "var", "fun", "package" -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    "when", "if", "else" -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    "public", "private", "protected" -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    "override" -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    else -> {
                        when {
                            string.startsWith('"') && string.endsWith('"') -> {
                                val sas = SimpleAttributeSet()
                                StyleConstants.setForeground(sas, Color(0x118855))
                                sas
                            }
                            else -> {
                                val sas = SimpleAttributeSet()
                                StyleConstants.setForeground(sas, Color(Window.theme.textAreaFG))
                                StyleConstants.setBackground(sas, Color(Window.theme.textAreaBG))
                                StyleConstants.setItalic(sas, false)
                                StyleConstants.setBold(sas, false)
                                sas
                            }
                        }
                    }
                }, false)
                startPos += str.length + 1
                str.clear()
            } else str.append(line[i])
        }
    }
}