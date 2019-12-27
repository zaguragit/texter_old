package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import java.awt.Color
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.max

class JavaSyntaxHighlighter : SyntaxHighlighter() {

    private val declarations = ArrayList<String>()
    private val mods = ArrayList<String>()
    private val funcBreaks = ArrayList<String>()
    private val conditions = ArrayList<String>()
    private val operators = ArrayList<String>()
    private val exceptions = ArrayList<String>()
    private val undefined = ArrayList<String>()
    private val values = ArrayList<String>()
    private val lineInfo = ArrayList<String>()

    init {
        val text = JavaSyntaxHighlighter::class.java.getResource("/code/highlighters/java.highlighter").readText().split('\n')
        for (line in text) {
            val items = line.split(' ')
            when {
                line.startsWith("declarations") -> declarations
                line.startsWith("mods") -> mods
                line.startsWith("func_breaks") -> funcBreaks
                line.startsWith("conditions") -> conditions
                line.startsWith("operators") -> operators
                line.startsWith("exceptions") -> exceptions
                line.startsWith("values") -> values
                else -> undefined
            }.addAll(items.subList(2, items.size))
        }
    }

    private val separators = " \t=,.;:-+*/()[]{}&|!"

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        if (line.isNotEmpty()) {
            var startPos = lineStart
            val str = StringBuilder()
            for (i in 0..line.length) {
                if (i == line.length || separators.contains(line[i])) {
                    val string = str.toString()
                    doc.setCharacterAttributes(startPos, max(str.length, 1), when (string) {
                        in declarations -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        in mods -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Color(0xff8800))
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
                        in exceptions -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Color(0xFF6649))
                            sas
                        }
                        in values -> {
                            val sas = SimpleAttributeSet()
                            StyleConstants.setForeground(sas, Color(0xff8800))
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

            if (line.length > 1) {
                var commentStart = line.indexOf("/*")
                var commentEnd: Int
                val commentAttrs = SimpleAttributeSet()
                StyleConstants.setForeground(commentAttrs, Color(0x7C7C7C))
                if (line.contains("/*")) do {
                    commentEnd = line.indexOf("*/", commentStart + 2)
                    doc.setCharacterAttributes(
                        lineStart + commentStart,
                        commentEnd - commentStart + 2,
                        commentAttrs,
                        false
                    )
                    if (line.substring(commentEnd + 2).contains("/*")) {
                        commentStart = line.indexOf("/*", commentEnd + 2)
                        if (line.substring(commentEnd + 2, commentStart).contains("//")) {
                            val tmp = line.substring(commentEnd + 2).indexOf("//") + commentEnd + 2
                            doc.setCharacterAttributes(
                                lineStart + tmp,
                                line.length - tmp,
                                commentAttrs,
                                false
                            )
                        }
                    } else {
                        if (line.substring(commentEnd + 2).contains("//")) {
                            val tmp = line.substring(commentEnd + 2).indexOf("//") + commentEnd + 2
                            doc.setCharacterAttributes(
                                lineStart + tmp,
                                line.length - tmp,
                                commentAttrs,
                                false
                            )
                        }
                        break
                    }
                } while (line.substring(commentStart + 2).contains("*/"))
                else if (line.contains("//")) {
                    commentStart = line.indexOf("//")
                    doc.setCharacterAttributes(
                        lineStart + commentStart,
                        line.length - commentStart,
                        commentAttrs,
                        false
                    )
                }
            }
        }
    }
}