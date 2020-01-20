package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.max

class MakefileSyntaxHighlighter : SyntaxHighlighter() {

    val variables = ArrayList<String>()

    companion object {
        val conditions = arrayOf("ifeq", "ifneq", "else", "endif", "if", "elif", "fi", "then", "for", "do", "while")
        val operators = arrayOf("in")
        val funcBreaks = arrayOf("continue", "done")
    }

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        doc.setCharacterAttributes(lineStart, line.length, defaultTextStyle(), false)
        if (line.isNotEmpty()) {
            var eqSignI = line.indexOf('=', 1)
            if (eqSignI != -1) {
                if ("?:+".contains(line[eqSignI-1])) eqSignI--
                val raw = line.substring(0, eqSignI)
                val variable = raw.trim()
                if (!variable.contains(' ') && !raw.contains('\t')) {
                    variables.add(variable)
                    doc.setCharacterAttributes(lineStart, eqSignI, defaultTextStyle().apply {
                        StyleConstants.setForeground(this, Window.theme.light_blue)
                    }, false)
                }
            }
            for (variable in variables) {
                val string = "$($variable)"
                if (line.contains(string)) {
                    val i = line.indexOf(string)
                    doc.setCharacterAttributes(lineStart + i, string.length, defaultTextStyle().apply {
                        StyleConstants.setForeground(this, Window.theme.light_blue)
                    }, false)
                }
            }

            var startPos = lineStart
            val str = StringBuilder()
            for (i in 0..line.length) {
                if (i == line.length || " \t".contains(line[i])) {
                    when (str.toString()) {
                        in conditions -> doc.setCharacterAttributes(
                            startPos,
                            max(str.length, 1),
                            defaultTextStyle().apply {
                                StyleConstants.setForeground(this, Window.theme.orange)
                            }, false)
                        in operators -> doc.setCharacterAttributes(
                            startPos,
                            max(str.length, 1),
                            defaultTextStyle().apply {
                                StyleConstants.setForeground(this, Window.theme.cyan)
                            }, false)
                        in funcBreaks -> doc.setCharacterAttributes(
                            startPos,
                            max(str.length, 1),
                            defaultTextStyle().apply {
                                StyleConstants.setForeground(this, Window.theme.orange)
                            }, false)
                    }
                    startPos += str.length + 1
                    str.clear()
                } else str.append(line[i])
            }

            val stringSides = arrayOf("\"", "'")
            for (stringSide in stringSides) {
                var stringStart = line.indexOf(stringSide)
                var stringEnd: Int
                val stringAttrs = SimpleAttributeSet()
                StyleConstants.setForeground(stringAttrs, Window.theme.green)
                while (line.substring(stringStart + 1).contains(stringSide)) {
                    stringEnd = line.indexOf(stringSide, stringStart + 1)
                    doc.setCharacterAttributes(
                        lineStart + stringStart,
                        stringEnd - stringStart + 1,
                        stringAttrs,
                        false
                    )
                    if (line.substring(stringEnd + 1).contains(stringSide))
                        stringStart = line.indexOf(stringSide, stringEnd + 1)
                    else break
                }
            }
            val lineCommentI = line.indexOf('#')
            if (lineCommentI != -1) {
                doc.setCharacterAttributes(
                    lineStart + lineCommentI,
                    line.length - lineCommentI,
                    defaultTextStyle().apply { StyleConstants.setForeground(this, Window.theme.gray) },
                    false
                )
            }
        }
    }
}