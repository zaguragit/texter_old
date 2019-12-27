package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import java.awt.Color
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.max
import kotlin.math.min

class BracketedSyntaxHighlighter(highligher: String) : SyntaxHighlighter() {

    private val declarations = ArrayList<String>()
    private val mods = ArrayList<String>()
    private val funcBreaks = ArrayList<String>()
    private val conditions = ArrayList<String>()
    private val operators = ArrayList<String>()
    private val exceptions = ArrayList<String>()
    private val undefined = ArrayList<String>()
    private val values = ArrayList<String>()
    private val lineInfo = ArrayList<String>()

    private var lineComment: String? = null
    private var startComment: String? = null
    private var endComment: String? = null

    init {
        val text = BracketedSyntaxHighlighter::class.java.getResource(highligher).readText().split('\n')
        for (line in text) {
            val items = line.split(' ')
            if (line.startsWith('@')) {
                when(items[0]) {
                    "@line-comment" -> lineComment = items[1]
                    "@multiline-comment" -> {
                        startComment = items[1]
                        endComment = items[2]
                    }
                }
            } else when {
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
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        in mods -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        in funcBreaks -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        in conditions -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        in operators -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0x60CCFF))
                            sas
                        }
                        in exceptions -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xFF6649))
                            sas
                        }
                        in values -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Color(0xff8800))
                            sas
                        }
                        else -> {
                            var num = string.substring(
                                if (string.startsWith("0x") || string.startsWith("0b")) 2 else 0,
                                if (
                                    string.endsWith("d", true) ||
                                    string.endsWith("l", true) ||
                                    string.endsWith("f", true)
                                ) string.length - 1 else string.length
                            )
                            if (string.startsWith("0x") && string.substring(2).toIntOrNull(16) != null) {
                                val sas = defaultTextStyle()
                                StyleConstants.setForeground(sas, Color(0x60CCFF))
                                sas
                            } else if (string.startsWith("0b") && string.substring(2).toIntOrNull(2) != null) {
                                val sas = defaultTextStyle()
                                StyleConstants.setForeground(sas, Color(0x60CCFF))
                                sas
                            } else if (string.toDoubleOrNull() != null) {
                                val sas = defaultTextStyle()
                                StyleConstants.setForeground(sas, Color(0x60CCFF))
                                sas
                            } else {
                                defaultTextStyle()
                            }
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

            if (lineComment != null && startComment != null && endComment != null) {
                val lineComment = lineComment!!
                val startComment = startComment!!
                val endComment = endComment!!
                if (line.length >= min(lineComment.length, min(startComment.length, endComment.length))) {
                    var commentStart = line.indexOf(startComment)
                    var commentEnd: Int
                    val commentAttrs = SimpleAttributeSet()
                    StyleConstants.setForeground(commentAttrs, Color(0x7C7C7C))
                    if (line.contains(startComment)) do {
                        commentEnd = line.indexOf(endComment, commentStart + 2)
                        doc.setCharacterAttributes(
                            lineStart + commentStart,
                            commentEnd - commentStart + 2,
                            commentAttrs,
                            false
                        )
                        if (line.substring(commentEnd + 2).contains(startComment)) {
                            commentStart = line.indexOf(startComment, commentEnd + 2)
                            if (line.substring(commentEnd + 2, commentStart).contains(lineComment)) {
                                val tmp = line.substring(commentEnd + 2).indexOf(lineComment) + commentEnd + 2
                                doc.setCharacterAttributes(
                                    lineStart + tmp,
                                    line.length - tmp,
                                    commentAttrs,
                                    false
                                )
                            }
                        } else {
                            if (line.substring(commentEnd + 2).contains(lineComment)) {
                                val tmp = line.substring(commentEnd + 2).indexOf(lineComment) + commentEnd + 2
                                doc.setCharacterAttributes(
                                    lineStart + tmp,
                                    line.length - tmp,
                                    commentAttrs,
                                    false
                                )
                            }
                            break
                        }
                    } while (line.substring(commentStart + 2).contains(endComment))
                    else if (line.contains(lineComment)) {
                        commentStart = line.indexOf(lineComment)
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
}