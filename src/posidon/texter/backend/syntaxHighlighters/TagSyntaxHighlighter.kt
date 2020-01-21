package posidon.texter.backend.syntaxHighlighters

import posidon.texter.Window
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyledDocument
import kotlin.math.min

class TagSyntaxHighlighter(val highlighter: Highlighter) : SyntaxHighlighter() {

    val tagColor = Window.theme.light_blue
    val tagParamsColor = Window.theme.yellow

    private enum class TagState {
        IN_STRING_DOUBLE,
        IN_STRING_SINGLE,
        IN_TAG,
        IN_TAG_PARAMS,
        TEXT,
        COMMENT
    }

    private val lineInfo = ArrayList<LineInfo>()

    override fun colorLine(doc: StyledDocument, lineStart: Int, line: String, lineI: Int) {
        if (line.isNotEmpty()) {
            doc.setCharacterAttributes(lineStart, line.length, defaultTextStyle(), false)
            var startPos = lineStart
            var state = TagState.TEXT
            var length = 0
            for (i in 0..line.lastIndex) {
                length++
                when (line[i]) {
                    '<' -> {
                        if (line[i + 1] == '!' && line[i + 2] == '-' && line[i + 3] == '-') {
                            state = TagState.COMMENT
                        } else if (state == TagState.TEXT) {
                            state = TagState.IN_TAG
                            startPos = i + lineStart
                            length = 1
                        }
                    }
                    '>' -> if (state == TagState.COMMENT && line[i - 1] == '-' && line[i - 2] == '-') {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, Window.theme.gray)
                        }, false)
                        state = TagState.TEXT
                    } else if (state == TagState.IN_TAG) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.TEXT
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length - 1, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        if (line[i-1] == '/') doc.setCharacterAttributes(lineStart + i - 1, 2, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        else doc.setCharacterAttributes(lineStart + i, 1, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.TEXT
                        startPos = i + lineStart + 1
                        length = 0
                    }
                    '"' -> if (state == TagState.IN_STRING_DOUBLE) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, Window.theme.green)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        state = TagState.IN_STRING_DOUBLE
                        startPos = i + lineStart
                        length = 1
                    }
                    '\'' -> if (state == TagState.IN_STRING_SINGLE) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, Window.theme.green)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    } else if (state == TagState.IN_TAG_PARAMS) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagParamsColor)
                        }, false)
                        state = TagState.IN_STRING_SINGLE
                        startPos = i + lineStart
                        length = 1
                    }
                    ' ' -> if (state == TagState.IN_TAG) {
                        doc.setCharacterAttributes(startPos, length, SimpleAttributeSet().apply {
                            StyleConstants.setForeground(this, tagColor)
                        }, false)
                        state = TagState.IN_TAG_PARAMS
                        startPos = i + lineStart + 1
                        length = 0
                    }
                }
            }

            val thisLineInfo = LineInfo(null, null, null)

            //lineInfo.add(thisLineInfo)

            line.indexOf("<!DOCTYPE").let {
                if (it != -1) {
                    doc.setCharacterAttributes(lineStart + it, line.indexOf('>', it) - it + 1, SimpleAttributeSet().apply {
                        StyleConstants.setForeground(this, Window.theme.blue_gray)
                    }, false)
                }
            }
        }
    }
}