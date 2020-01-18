package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.max
import kotlin.math.min

class BracketedSyntaxHighlighter(private val highlighter: Highlighter) : SyntaxHighlighter() {

    private val lineInfo = ArrayList<LineInfo>()

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        if (line.isNotEmpty()) {
            var startPos = lineStart
            val str = StringBuilder()
            for (i in 0..line.length) {
                if (i == line.length || line[i] == ' ' || line[i] == '\t' || highlighter.separators.contains(line[i])) {
                    val string = str.toString()
                    doc.setCharacterAttributes(startPos, max(str.length, 1), when (string) {
                        in highlighter.declarations -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.orange)
                            sas
                        }
                        in highlighter.mods -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.purple)
                            sas
                        }
                        in highlighter.funcBreaks -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.orange)
                            sas
                        }
                        in highlighter.conditions -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.orange)
                            sas
                        }
                        in highlighter.operators -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.cyan)
                            sas
                        }
                        in highlighter.exceptions -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.coral)
                            sas
                        }
                        in highlighter.values -> {
                            val sas = defaultTextStyle()
                            StyleConstants.setForeground(sas, Window.theme.light_blue)
                            sas
                        }
                        else -> when {
                            highlighter.hexPrefix?.let { string.startsWith(it) } == true && string.substring(2).toIntOrNull(16) != null -> {
                                val sas = defaultTextStyle()
                                StyleConstants.setForeground(sas, Window.theme.light_blue)
                                sas
                            }
                            highlighter.binPrefix?.let { string.startsWith(it) } == true && string.substring(2).toIntOrNull(2) != null -> {
                                val sas = defaultTextStyle()
                                StyleConstants.setForeground(sas, Window.theme.light_blue)
                                sas
                            }
                            else -> {
                                val sas = defaultTextStyle()
                                if (string.toDoubleOrNull() == null) highlighter.numSuffixes?.forEach normalNum@ { suffix ->
                                    if (string.endsWith(suffix) && string.substring(0, string.length - suffix.length).toDoubleOrNull() != null) {
                                        StyleConstants.setForeground(sas, Window.theme.light_blue)
                                        return@normalNum
                                    }
                                } else StyleConstants.setForeground(sas, Window.theme.light_blue)
                                sas
                            }
                        }
                    }, false)
                    startPos += str.length + 1
                    str.clear()
                } else str.append(line[i])
            }

            val thisLineInfo = LineInfo(null, null, null)

            if (highlighter.stringSides != null) {
                val stringSides = highlighter.stringSides!!
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
            }

            if (highlighter.lineComment != null && highlighter.startComment != null && highlighter.endComment != null) {
                val lineComment = highlighter.lineComment!!
                val startComment = highlighter.startComment!!
                val endComment = highlighter.endComment!!
                val lastICommentStart = line.lastIndexOf(startComment)
                if (lastICommentStart > line.lastIndexOf(endComment)) thisLineInfo.unfinishedMultilineCommentI = lastICommentStart
                if (line.length >= min(lineComment.length, min(startComment.length, endComment.length))) {
                    var commentStart = line.indexOf(startComment)
                    var commentEnd: Int
                    val commentAttrs = SimpleAttributeSet()
                    StyleConstants.setForeground(commentAttrs, Window.theme.gray)
                    if (line.contains(startComment)) do {
                        commentEnd = line.indexOf(endComment)
                        if (commentEnd < commentStart + 2) {
                            thisLineInfo.unstartedMultilineCommentI = commentEnd
                            commentEnd = line.indexOf(endComment, commentStart + 2)
                            doc.setCharacterAttributes(
                                lineStart + commentStart,
                                commentEnd - commentStart + 2,
                                commentAttrs,
                                false
                            )
                        } else {
                            doc.setCharacterAttributes(
                                lineStart + commentStart,
                                commentEnd - commentStart + 2,
                                commentAttrs,
                                false
                            )
                        }
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

            lineInfo.add(thisLineInfo)
        }
    }
}