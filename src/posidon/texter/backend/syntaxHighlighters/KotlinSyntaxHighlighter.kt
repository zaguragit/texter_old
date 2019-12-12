package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import java.awt.Color
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument

class KotlinSyntaxHighlighter : SyntaxHighlighter() {

    private val declarations = ArrayList<String>()
    private val funcMods = ArrayList<String>()
    private val funcBreaks = ArrayList<String>()
    private val conditions = ArrayList<String>()
    private val operators = ArrayList<String>()
    private val undefined = ArrayList<String>()

    init {
        val text = KotlinSyntaxHighlighter::class.java.getResource("/code/highlighters/kt.txt").readText().split('\n')
        for (line in text) {
            val items = line.split(' ')
            when {
                line.startsWith("declarations") -> declarations
                line.startsWith("func_mods") -> funcMods
                line.startsWith("func_breaks") -> funcBreaks
                line.startsWith("conditions") -> conditions
                line.startsWith("operators") -> operators
                else -> undefined
            }.addAll(items.subList(2, items.size))
        }
    }

    override fun color(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        var startPos = lineStart
        val str = StringBuilder()
        for (i in 0..line.length) {
            if (i == line.length || line[i] == ' ') {
                val string = str.toString()
                doc.setCharacterAttributes(startPos, str.length, when (string) {
                    in declarations -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff3355))
                        sas
                    }
                    in funcMods -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xA05FFF))
                        sas
                    }
                    in funcBreaks -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    in conditions -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0xff8800))
                        sas
                    }
                    in operators -> {
                        val sas = SimpleAttributeSet()
                        StyleConstants.setForeground(sas, Color(0x60CCFF))
                        sas
                    }
                    else -> {
                        when {
                            string.startsWith('"') && string.endsWith('"') -> {
                                val sas = SimpleAttributeSet()
                                StyleConstants.setForeground(sas, Color(0x28A34F))
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